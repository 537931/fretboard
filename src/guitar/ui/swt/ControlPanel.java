package guitar.ui.swt;

import guitar.core.Chord;
import guitar.core.Guitar;
import guitar.core.Octave;
import guitar.core.Scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;


/**
 * @author Serge Yuschenko
 */
public class ControlPanel {
	private static int VIEW_CHORD = 1;
	private static int VIEW_SCALE = 2;

	private Label usedNotes;
	private int viewType = VIEW_CHORD;

	private Combo rootBox;
	private Combo chordBox;
	private Combo scaleBox;

	private Fretboard fretBoard;

	class NameScore {
		String		name;
		float		score;

	};

	public ControlPanel( Composite parent, Fretboard fb ) {
		fretBoard = fb;

		CTabFolder tabFolder = new CTabFolder( parent, SWT.BOTTOM );
		tabFolder.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));

		final CTabItem displayTab = new CTabItem( tabFolder, SWT.NONE );
		displayTab.setText( "Display" );
		displayTab.setControl( createDisplayTab( tabFolder ));

		final CTabItem detectTab = new CTabItem( tabFolder, SWT.NONE );
		detectTab.setText( "Detect" );
		detectTab.setControl( createDetectTab( tabFolder ));

		final CTabItem configTab = new CTabItem( tabFolder, SWT.NONE );
		configTab.setText( "Config" );
		configTab.setControl( createConfigTab( tabFolder ));

		tabFolder.addSelectionListener( new SelectionListener() {
			public void widgetSelected( SelectionEvent ev ) {
				if( ev.item == displayTab ) {
					fretBoard.setMode( Fretboard.DISPLAY_MODE );
				}
				else if( ev.item == detectTab ) {
					fretBoard.setMode( Fretboard.DETECT_MODE );
				}
			}

			public void widgetDefaultSelected( SelectionEvent ev ) {
			}
		});
	}

	private Composite createDisplayTab( Composite parent ) {
		Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ));
		container.setLayout( new GridLayout( 2, false ));

		Composite column1 = new Composite( container, SWT.NONE );
		column1.setLayout( new GridLayout( 2, false ));

		/*
		 * A button to switch to chords view
		 */
		Button chordButton = new Button( column1, SWT.RADIO );
		chordButton.setText( "Chord" );
		chordButton.setSelection( viewType == VIEW_CHORD );
		chordButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				if(((Button) e.widget).getSelection()) {
					viewType = VIEW_CHORD;
					drawNotes();
				}
			}
		});

		/*
		 * Chords list
		 */
		chordBox = new Combo( column1, SWT.DROP_DOWN | SWT.READ_ONLY );
		String[] chordNames = Resources.getChordNames();

		for( int i = 0; i < chordNames.length; i++ )
			chordBox.add( chordNames[ i ] );

		chordBox.setText( chordNames[ 0 ] );
		chordBox.setLayoutData( new GridData());

		chordBox.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				drawNotes();
			}
		});

		/*
		 * A button to switch to scales view
		 */
		Button scaleButton = new Button( column1, SWT.RADIO );
		scaleButton.setText( "Scale" );
		scaleButton.setSelection( viewType == VIEW_SCALE );
		scaleButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				if(((Button) e.widget).getSelection()) {
					viewType = VIEW_SCALE;
					drawNotes();
				}
			}
		});

		/*
		 * Scales list
		 */
		scaleBox = new Combo( column1, SWT.DROP_DOWN | SWT.READ_ONLY );
		String[] scaleNames = Resources.getScaleNames();

		for( int i = 0; i < scaleNames.length; i++ )
			scaleBox.add( scaleNames[ i ] );

		scaleBox.setText( scaleNames[ 0 ] );
		scaleBox.setLayoutData( new GridData());

		scaleBox.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				drawNotes();
			}
		});

		Composite column2 = new Composite( container, SWT.NONE );
		column2.setLayoutData( new GridData( GridData.FILL_BOTH ));
		column2.setLayout( new GridLayout());

		/*
		 * List of notes to be chosen as root
		 */
		rootBox = new Combo( column2, SWT.DROP_DOWN | SWT.READ_ONLY );
		String[] noteNames = Resources.getNoteNames();

		for( int i = 0; i < noteNames.length; i++ ) {
			rootBox.add( noteNames[ i ] );
		}

		rootBox.setText( noteNames[ 0 ] );

		rootBox.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				drawNotes();
			}
		});

		/*
		 * Notes used in the chord or scale
		 */
		usedNotes = new Label( column2, SWT.NONE );

		return container;
	}

	private void drawNotes() {
		int notes[];
		int root = Resources.getNoteByIndex( rootBox.getSelectionIndex());

		if( viewType == VIEW_CHORD )
			notes = Chord.getNotes( root, Resources.getChordByIndex( chordBox.getSelectionIndex()));
		else
			notes = Scale.getNotes( root, Resources.getScaleByIndex( scaleBox.getSelectionIndex()));

		fretBoard.setFocus();
		fretBoard.setNotes( notes );
		fretBoard.redraw();

		String str = ""; //$NON-NLS-1$

		for( int i = 0; i < notes.length; i++ )
			str += Resources.getNoteName( notes[ i ] ) + " "; //$NON-NLS-1$

		usedNotes.setText( str );
		usedNotes.pack();
	}

	private Composite createDetectTab( Composite parent ) {
		final Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ));
		container.setLayout( new GridLayout( 2, false ));
		
		final Label scaleLabel = new Label( container, SWT.NONE );
		scaleLabel.setText( "Scales" );
		
		final Composite scaleList = new Composite( container, SWT.NONE );
		scaleList.setLayoutData(  new GridData( GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER ));
		scaleList.setLayout( new RowLayout( SWT.HORIZONTAL ));

		final Label chordLabel = new Label( container, SWT.NONE );
		chordLabel.setText( "Chords" );
		chordLabel.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_CENTER ));
		
		final Composite chordList = new Composite( container, SWT.NONE );
		chordList.setLayoutData( new GridData( GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER ));
		chordList.setLayout( new RowLayout( SWT.HORIZONTAL ));

		fretBoard.addListener( new FretboardListener() {
			public void fingersMoved( List<Integer>notes ) {
				Object[] nt = notes.toArray();
				int[] ntint = new int[ nt.length ];

				for( int i = 0; i < nt.length; i++ )
					ntint[ i ] = (Integer) nt[ i ];

				displayList( idScale( ntint ), scaleList );
				displayList( idChord( ntint ), chordList );
				container.pack();
			}
		});

		return container;
	}

	private void displayList( NameScore[] score, Composite parent ) {
		Arrays.sort( score, new Comparator<NameScore>() {
			public int compare( NameScore cs1, NameScore cs2 ) {
				if( cs1.score < cs2.score )
					return 1;
				else if( cs1.score > cs2.score )
					return -1;
				else
					return 0;
			}
		});

		Control[] oldLabels = parent.getChildren();

		for( int i = 0; i < oldLabels.length; i++ )
			oldLabels[ i ].dispose();

		for( int i = 0; i < score.length; i++ ) {
			Label chord = new Label( parent, SWT.NONE );
			chord.setText( score[ i ].name + " " );

			if( score[ i ].score == 1.0 )
				chord.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_RED ));
			else if( score[ i ].score > 0.5 )
				chord.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_DARK_GREEN ));
		}

		parent.layout();
		parent.pack();
	}

	private boolean inNote( int[] chordNotes, int note ) {
		for( int i = 0; i < chordNotes.length; i++ )
			if( note == chordNotes[ i ] )
				return true;

		return false;
	}

	private boolean inNotes( int[] chordNotes, int[] notes ) {
		if( notes.length == 0 )
			return false;

		for( int i = 0; i < notes.length; i++ )
			if( ! inNote( chordNotes, notes[ i ] ))
				return false;

		return true;
	}

	private NameScore[] idChord( int[] notes ) {
		List<NameScore> chords = new ArrayList<NameScore>();
		int[] roots = Octave.getNotes();
		int[] intv = Chord.getIntervals();

		for( int i = 0; i < roots.length; i++ ) {
			for( int j = 0; j < intv.length; j++ ) {
				int[] chordNotes = Chord.getNotes( roots[ i ], intv[ j ] );

				if( inNotes( chordNotes, notes )) {
					NameScore cs = new NameScore();
					cs.name = Resources.getNoteName( roots[ i ] ) + Resources.getChordName( intv[ j ] );
					cs.score = (float) notes.length / (float) chordNotes.length;
					chords.add( cs );
				}
			}
		}

		return chords.toArray( new NameScore[ chords.size() ]);
	}

	private NameScore[] idScale( int[] notes ) {
		List<NameScore> score = new ArrayList<NameScore>();
		int[] roots = Octave.getNotes();
		int[] scales = Scale.getScales();

		for( int i = 0; i < roots.length; i++ ) {
			for( int j = 0; j < scales.length; j++ ) {
				int[] scaleNotes = Scale.getNotes( roots[ i ], scales[ j ] );

				if( inNotes( scaleNotes, notes )) {
					NameScore cs = new NameScore();
					cs.name = Resources.getNoteName( roots[ i ] ) + Resources.getScaleShortName( scales[ j ] );
					cs.score = (float) notes.length / ((float) scaleNotes.length - 1 );
					score.add( cs );
				}
			}
		}

		return score.toArray( new NameScore[ score.size() ]);
	}

	private Composite createConfigTab( Composite parent ) {
		Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ));
		container.setLayout( new GridLayout( 2, false ));

		new Label( container, SWT.NONE ).setText( "Strings" );

		final Composite stringList = new Composite( container, SWT.NONE );
		stringList.setLayoutData( new GridData( GridData.FILL_BOTH ));
		stringList.setLayout( new GridLayout( 6, false ));

		String[] noteNames = Resources.getNoteNames();
		final int[] strings = fretBoard.getGuitar().getStrings();

		final Combo[] str = new Combo[ 6 ];

		for( int i = 0; i < 6; i++ ) {
			str[ i ] = new Combo( stringList, SWT.DROP_DOWN | SWT.READ_ONLY );
			str[ i ].setItems( noteNames );
			str[ i ].select( strings[ i ] + 1 );
			str[ i ].setData( i );
			str[ i ].addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					Integer index = (Integer) e.widget.getData();
					int i = index;
					strings[ i ] = str[ i ].getSelectionIndex() - 1;
					fretBoard.getGuitar().setStrings( strings );
				}
			});
		}

		new Label( container, SWT.NONE ).setText( "Frets" );

		final Combo frets = new Combo( container, SWT.DROP_DOWN | SWT.READ_ONLY );

		for( int i = Guitar.FRETS_MIN; i <= Guitar.FRETS_MAX; i++ )
			frets.add( Integer.toString( i ));
		
		frets.select( fretBoard.getGuitar().getNFrets() - Guitar.FRETS_MIN );
		frets.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				fretBoard.getGuitar().setNFrets( frets.getSelectionIndex() + Guitar.FRETS_MIN );
			}
		});

		return container;
	}
}
