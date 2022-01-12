package guitar.ui.swt;

import guitar.core.Chord;
import guitar.core.Octave;
import guitar.core.Scale;

/**
 * @author Serge Yuschenko
 */
public class Resources {
	private static class NameMap {
		private int id;
		String name;

		public NameMap( int id, String name ) {
			this.id = id;
			this.name = name;
		}
	}

	private static NameMap[] notes = {
		new NameMap( -1, "" ), //$NON-NLS-1$
		new NameMap( Octave.A, "A" ), //$NON-NLS-1$
		new NameMap( Octave.A_FLAT, "Ab" ), //$NON-NLS-1$
		new NameMap( Octave.A_SHARP, "A#" ), //$NON-NLS-1$
		new NameMap( Octave.B, "B" ), //$NON-NLS-1$
		new NameMap( Octave.B_FLAT, "Bb" ), //$NON-NLS-1$
		new NameMap( Octave.C, "C" ), //$NON-NLS-1$
		new NameMap( Octave.C_SHARP, "C#" ), //$NON-NLS-1$
		new NameMap( Octave.D, "D" ), //$NON-NLS-1$
		new NameMap( Octave.D_FLAT, "Db" ), //$NON-NLS-1$
		new NameMap( Octave.D_SHARP, "D#" ), //$NON-NLS-1$
		new NameMap( Octave.E, "E" ), //$NON-NLS-1$
		new NameMap( Octave.E_FLAT, "Eb" ), //$NON-NLS-1$
		new NameMap( Octave.F, "F" ), //$NON-NLS-1$
		new NameMap( Octave.F_SHARP, "F#" ), //$NON-NLS-1$
		new NameMap( Octave.G, "G" ), //$NON-NLS-1$
		new NameMap( Octave.G_FLAT, "Gb" ), //$NON-NLS-1$
		new NameMap( Octave.G_SHARP, "G#" ) //$NON-NLS-1$
	};

	private static NameMap[] chords = {
		new NameMap( Chord.MAJOR, "" ), //$NON-NLS-1$
		new NameMap( Chord.MINOR, "m" ), //$NON-NLS-1$
		new NameMap( Chord.SEVENTH, "7" ), //$NON-NLS-1$
		new NameMap( Chord.MINOR_7TH, "m7" ), //$NON-NLS-1$
		new NameMap( Chord.MAJOR_7TH, "maj7" ), //$NON-NLS-1$
		new NameMap( Chord.SIXTH, "6" ), //$NON-NLS-1$
		new NameMap( Chord.MINOR_6TH, "m6" ), //$NON-NLS-1$
		new NameMap( Chord.AUGMENTED, "+" ), //$NON-NLS-1$
		new NameMap( Chord.AUGMENTED_7TH, "7+" ), //$NON-NLS-1$
		new NameMap( Chord.DIMINISHED, "dim" ), //$NON-NLS-1$
		new NameMap( Chord.DIMINISHED_7TH, "dim7" ), //$NON-NLS-1$
		new NameMap( Chord.SEVENTH_FF, "7(5b)" ), //$NON-NLS-1$
		new NameMap( Chord.MINOR_7TH_FF, "m7(5b)" ), //$NON-NLS-1$
		new NameMap( Chord.NINTH, "9" ), //$NON-NLS-1$
		new NameMap( Chord.MINOR_9TH, "m9" ), //$NON-NLS-1$
		new NameMap( Chord.MAJOR_9TH, "maj9" ), //$NON-NLS-1$
		new NameMap( Chord.ELEVENTH, "11" ), //$NON-NLS-1$
		new NameMap( Chord.DIMINISHED_9TH, "dim9" ), //$NON-NLS-1$
		new NameMap( Chord.ADDED_9TH, "(9)" ), //$NON-NLS-1$
		new NameMap( Chord.ADDED_4TH, "(4)" ), //$NON-NLS-1$
		new NameMap( Chord.SUSPENDED, "sus" ), //$NON-NLS-1$
		new NameMap( Chord.SUSPENDED_9TH, "sus9" ), //$NON-NLS-1$
		new NameMap( Chord.SEVENTH_SUSPENDED_4TH, "7sus" ), //$NON-NLS-1$
		new NameMap( Chord.SEVENTH_SUSPENDED_9TH, "7sus2" ), //$NON-NLS-1$
		new NameMap( Chord.FIFTH, "5" ) //$NON-NLS-1$
	};

	private static NameMap[] scales = {
		new NameMap( Scale.MAJOR, "major" ), //$NON-NLS-1$
		new NameMap( Scale.MINOR, "minor" ), //$NON-NLS-1$
		new NameMap( Scale.PENTATONIC, "pentatonic" ), //$NON-NLS-1$
		new NameMap( Scale.HEXATONIC2, "hexatonic II" ), //$NON-NLS-1$
		new NameMap( Scale.HEXATONIC6, "hexatatonic VI" ) //$NON-NLS-1$
	};

	private static NameMap[] scalesShort = {
		new NameMap( Scale.MAJOR, "" ), //$NON-NLS-1$
		new NameMap( Scale.MINOR, "m" ), //$NON-NLS-1$
		new NameMap( Scale.PENTATONIC, "5" ), //$NON-NLS-1$
		new NameMap( Scale.HEXATONIC2, "6 II" ), //$NON-NLS-1$
		new NameMap( Scale.HEXATONIC6, "6 VI" ) //$NON-NLS-1$
	};

	private static String[] getNames( NameMap[] map ) {
		String[] names = new String[ map.length ];

		for( int i = 0; i < map.length; i++ )
			names[i] = map[ i ].name;

		return names;
	}

	private static String getName( NameMap[] map, int note ) {
		for( int i = 0; i < map.length; i++ )
			if( note == map[ i ].id )
				return map[ i ].name;

		return null;
	}

	private static int getByIndex( NameMap[] map, int index ) {
		if( 0 <= index && index < map.length ) {
			return map[ index ].id;
		}

		return -1;
	}

	public static String[] getNoteNames() {
		return getNames( notes );
	}

	public static String getNoteName( int note ) {
		return getName( notes, note );
	}

	public static int getNoteByIndex( int index ) {
		return getByIndex( notes, index );
	}

	public static String[] getChordNames() {
		return getNames( chords );
	}

	public static String getChordName( int type ) {
		return getName( chords, type );
	}

	public static int getChordByIndex( int index ) {
		return getByIndex( chords, index );
	}

	public static String[] getScaleNames() {
		return getNames( scales );
	}

	public static String getScaleName( int type ) {
		return getName( scales, type );
	}

	public static String getScaleShortName( int type ) {
		return getName( scalesShort, type );
	}

	public static int getScaleByIndex( int index ) {
		return getByIndex( scales, index );
	}
}
