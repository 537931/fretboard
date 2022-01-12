package guitar.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Serge Yuschenko
 */
public class Guitar {
	public static final int FRETS_MIN = 3;
	public static final int FRETS_MAX = 23;
	private int nFrets = FRETS_MAX;

	private List<ITuningListener> listeners = new ArrayList<ITuningListener>();

	private int[] strings = {
		Octave.E,	// first string
		Octave.B,
		Octave.G,
		Octave.D,
		Octave.A,
		Octave.E
	};

	public Guitar() {
	}

	public Guitar( int fr, int[] str ) {
		setNFrets( fr );
		setStrings( str );
	}

	public int getNFrets() {
		return nFrets;
	}

	public void setNFrets( int frets ) {
		if( frets >= FRETS_MIN && frets <= FRETS_MAX ) {
			nFrets = frets;
			notifyListeners();
		}
	}

	public int[] getStrings() {
		return strings;
	}

	public void setStrings( int[] str ) {
		strings = str;
		notifyListeners();
	}

	public int[][] getFingers( int[] notes ) {
		int[][] fin = new int[ strings.length ][ nFrets + 1 ];

		for( int i = 0; i < strings.length; i++ ) {
			for( int j = 0; j <= nFrets; j++ ) {
				fin[ i ][ j ] = -1;
				int note = Octave.getNote( strings[ i ], j );

				if( -1 != note && notes != null ) {
					for( int k = 0; k < notes.length; k++ ) {
						if( note == notes[ k ])
							fin[ i ][ j ] = note;
					}
				}
			}
		}

		return fin;
	}

	public int getNote( int fret, int string ) {
		return Octave.getNote( strings[ string ], fret );
	}

	public void addListener( ITuningListener listener ) {
		if( ! listeners.contains( listener ))
			listeners.add( listener );
	}

	public void removeListener( ITuningListener listener ) {
		if( listeners.contains( listener ))
			listeners.remove( listener );
	}

	private void notifyListeners() {
		Iterator<ITuningListener> iter = listeners.iterator();

		while( iter.hasNext()) {
			ITuningListener fl = iter.next();
			fl.tuned( strings, nFrets );
		}
	}
}
