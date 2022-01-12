package guitar.ui.swt;

import guitar.core.Guitar;
import guitar.core.ITuningListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


/**
 * @author Serge Yuschenko
 */
public class Fretboard extends Canvas {
	public static int DISPLAY_MODE = 0;
	public static int DETECT_MODE = 1;

	private static int OFFW = 30;
	private static int OFFH = 40;

	private RGB bkgRGB = getBackground().getRGB();
	private Guitar guitar;
	private int[] chordNotes;
	private int[][] fingersFromScreen;
	private int mode = DISPLAY_MODE;

	private int nFrets;
	private int nStrings;
	private int fWidth;
	private int sHeight;

	private List<Integer> markedNotes = new ArrayList<Integer>();
	private List<FretboardListener> listeners = new ArrayList<FretboardListener>();

	private MouseListener mouseListener = new MouseListener() {
		public void mouseDoubleClick( MouseEvent ev ) {
		}

		public void mouseDown( MouseEvent ev ) {
			toggleNote( ev.x, ev.y );
		}

		public void mouseUp( MouseEvent ev ) {
		}
	};

	private ITuningListener tuningListener = new ITuningListener() {
		public void tuned( int[] strings, int nfrets ) {
			redraw();
		}
	};

	/**
	 * @param parent
	 * @param style
	 */
	public Fretboard( Composite parent, int style ) {
		super( parent, style | SWT.NO_BACKGROUND  );

		addPaintListener( new PaintListener() {
			public void paintControl( PaintEvent e ) {
				draw( e.gc );
			}
		});
	}

	public Guitar getGuitar() {
		return guitar;
	}

	private void toggleNote( int x, int y ) {
		int nStrings = guitar.getStrings().length;
		int nFrets = guitar.getNFrets();
		int fret, string;

		string = ( y - OFFH ) / sHeight;

		if( x <= OFFW )
			fret = 0;
		else
			fret = ( x - OFFW ) / fWidth + 1;

		if( y >= (( string + 1 ) * sHeight ) - 5 &&
			y <= (( string + 1 ) * sHeight ) + 5 ) {
			int note = guitar.getNote( fret, string );
			int[][] f = guitar.getFingers( new int[] { note } );
//			System.out.println( "fret " + fret +
//								", string " + ( string + 1 ) +
//								", note " + Resources.getNoteName( note ));

			if( -1 == fingersFromScreen[ string ][ fret ] ) {
				for( int i = 0; i < nStrings; i++ )
					for( int j = 0; j <= nFrets; j++ )
						if( note == f[ i ][ j ] )
							fingersFromScreen[ i ][ j ] = note;

				if( ! markedNotes.contains( note ))
					markedNotes.add( note );

				Iterator<FretboardListener> iter = listeners.iterator();

				while( iter.hasNext()) {
					FretboardListener fl = iter.next();
					fl.fingersMoved( markedNotes );
				}
			}
			else {
				for( int i = 0; i < nStrings; i++ )
					for( int j = 0; j <= nFrets; j++ )
						if( note == f[ i ][ j ] )
							fingersFromScreen[ i ][ j ] = -1;

				markedNotes.remove( new Integer( note ));

				Iterator<FretboardListener> iter = listeners.iterator();

				while( iter.hasNext()) {
					FretboardListener fl = iter.next();
					fl.fingersMoved( markedNotes );
				}
			}

			redraw();
		}
	}

	public void setGuitar( Guitar guitar ) {
		if( this.guitar != null )
			this.guitar.removeListener( tuningListener );

		this.guitar = guitar;

		if( this.guitar != null )
			this.guitar.addListener( tuningListener );
	}

	public void setNotes( int[] notes ) {
		this.chordNotes = notes;
	}

	private void draw( GC gc ) {
		Rectangle rect = getClientArea();

		if( rect.width <= 0 || rect.height <= 0 )
			return;

		Image img = new Image( getDisplay(), rect );
		GC imgGC = new GC( img );

		/*
		 * Draw background
		 */
		Color color = new Color( getDisplay(), bkgRGB );
		imgGC.setBackground( color );
		imgGC.fillRectangle( rect );
		color.dispose();

		nFrets = guitar.getNFrets();
		nStrings = guitar.getStrings().length;
		fWidth = ( rect.width - OFFW * 2 ) / nFrets;
		sHeight = ( rect.height - OFFH * 2 ) / nStrings;

		drawGrid( imgGC );
		drawFingers( imgGC );

		/*
		 * Display the built image
		 */
		gc.drawImage( img, 0, 0 );
		imgGC.dispose();
		img.dispose();
	}

	private void drawGrid( GC gc ) {
		Rectangle bound = gc.getClipping();
		Rectangle frame = new Rectangle( bound.x + OFFW, bound.y + OFFH,
								bound.width - OFFW * 2, bound.height - OFFH * 2 );

		gc.setLineWidth( 2 );
		gc.drawRectangle( frame );

		gc.setLineWidth( 0 );

		for( int i = 0; i < nFrets; i++ ) {
			gc.drawLine(	frame.x + fWidth * i,
							frame.y,
							frame.x + fWidth * i,
							frame.y + frame.height );
			String str = String.valueOf( i + 1 );
			int x = frame.x + fWidth * i + ( fWidth - gc.stringExtent( str ).x ) / 2;
			gc.drawString( str, x, frame.y - OFFH / 2, true );
			gc.drawString( str, x, frame.y + frame.height + 10, true );
		}

		for( int i = 0; i < nStrings; i++ ) {
			gc.drawLine(	frame.x,
							frame.y + sHeight / 2 + sHeight * i,
							frame.x + frame.width,
							frame.y + sHeight / 2 + sHeight * i );
			String str = Resources.getNoteName( guitar.getNote( 0, i ));
			int y = frame.y + sHeight * i + ( sHeight - gc.stringExtent( str ).y ) / 2;
			gc.drawString( str, frame.x + frame.width + OFFW / 2, y, true );
		}
	}

	private int[] list2array( List<Integer> list ) {
		Integer[] ar1 = list.toArray( new Integer[ list.size() ]);
		int[] ar2 = new int[ ar1.length ];

		for( int i = 0; i < ar2.length; i++ )
			ar2[ i ] = ar1[ i ];

		return ar2;
	}

	private void drawFingers( GC gc ) {
		int[][] fingers;

		if( mode == DISPLAY_MODE )
			fingers = guitar.getFingers( chordNotes );
		else
			fingers = guitar.getFingers( list2array( markedNotes ));

		if( null == fingers || 0 == fingers.length )
			return;

		Rectangle bound = gc.getClipping();
		Rectangle frame = new Rectangle( bound.x + OFFW, bound.y + OFFH,
										bound.width - OFFW * 2, bound.height - OFFH * 2 );

		gc.setLineWidth( 0 );

		for( int i = 0; i < nStrings; i++ ) {
			for( int j = 0; j <= nFrets; j++ ) {
				if( -1 != fingers[ i ][ j ] ) {
					boolean key = ( mode == DISPLAY_MODE ) && fingers[ i ][ j ] == chordNotes[ 0 ];

					if( 0 == j )
						drawLabel( gc, Resources.getNoteName( fingers[ i ][ j ] ),
								key,
								frame.x - 16 ,
								frame.y + sHeight / 2 + sHeight * i - 4 );
					else
						drawLabel( gc, Resources.getNoteName( fingers[ i ][ j ] ),
								key,
								frame.x - fWidth / 2 + fWidth * j - 4,
								frame.y + sHeight / 2 + sHeight * i - 4 );
				}
			}
		}
	}

	private void drawLabel( GC gc, String note, boolean key, int x, int y ) {
		Point p = gc.stringExtent( "C#" );
		int s = (int)((float) Math.max( p.x, p.y ) * 1.5 );
		int off = s / 3;

		gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_BLUE ));
		gc.setBackground( gc.getDevice().getSystemColor( SWT.COLOR_BLUE ));

		if( key ) {
			gc.drawRectangle( x - off, y - off, s, s );
			gc.fillRectangle( x - off, y - off, s, s );
		}
		else {
			gc.drawOval( x - off, y - off, s, s );
			gc.fillOval( x - off, y - off, s, s );
		}
			
		gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_WHITE ));
		gc.drawString( note, x - gc.stringExtent( note ).x / 5, y - off / 2, true );
	}

	public void setMode( int mode ) {
		if( DETECT_MODE == mode ) {
			this.mode = mode;
			addMouseListener( mouseListener );

			if( fingersFromScreen == null ) {
				fingersFromScreen = new int[ nStrings ][ nFrets + 1 ];
				
				for( int i = 0; i < nStrings; i++ )
					for( int j = 0; j <= nFrets; j++ )
						fingersFromScreen[ i ][ j ] = -1;
			}

			redraw();
		}
		else {
			this.mode = mode;
			removeMouseListener( mouseListener );
			redraw();
		}
	}

	public void addListener( FretboardListener listener ) {
		if( ! listeners.contains( listener ))
			listeners.add( listener );
	}

	public void removeListener( FretboardListener listener ) {
		if( listeners.contains( listener ))
			listeners.remove( listener );
	}
}
