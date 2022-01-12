package guitar.core;

/**
 * @author Serge Yuschenko
 */
public class Chord {
	public static final int MAJOR					= 0;
	public static final int MINOR					= 1;
	public static final int SEVENTH					= 2;
	public static final int MINOR_7TH				= 3;
	public static final int MAJOR_7TH				= 4;
	public static final int SIXTH					= 5;
	public static final int MINOR_6TH				= 6;
	public static final int AUGMENTED				= 7;
	public static final int AUGMENTED_7TH			= 8;
	public static final int DIMINISHED				= 9;
	public static final int DIMINISHED_7TH			= 10;
	public static final int SEVENTH_FF				= 11;
	public static final int MINOR_7TH_FF			= 12;
	public static final int NINTH					= 13;
	public static final int MINOR_9TH				= 14;
	public static final int MAJOR_9TH				= 15;
	public static final int ELEVENTH				= 16;
	public static final int DIMINISHED_9TH			= 17;
	public static final int ADDED_9TH				= 18;
	public static final int ADDED_4TH				= 19;
	public static final int SUSPENDED				= 20;
	public static final int SUSPENDED_9TH			= 21;
	public static final int SEVENTH_SUSPENDED_4TH	= 22;
	public static final int SEVENTH_SUSPENDED_9TH	= 23;
	public static final int FIFTH					= 24;

	private static final int intervals[] = {
		MAJOR, MINOR, SEVENTH, MINOR_7TH, MAJOR_7TH,
		SIXTH, MINOR_6TH, AUGMENTED, AUGMENTED_7TH,
		DIMINISHED, DIMINISHED_7TH, SEVENTH_FF,
		MINOR_7TH_FF, NINTH, MINOR_9TH, MAJOR_9TH,
		ELEVENTH, DIMINISHED_9TH, ADDED_9TH,
		ADDED_4TH, SUSPENDED, SUSPENDED_9TH,
		SEVENTH_SUSPENDED_4TH,
		SEVENTH_SUSPENDED_9TH, FIFTH
	};

	private static final int[][] metrics = {
		{0, 4, 7},				// MAJOR
		{0, 3, 7},				// MINOR
		{0, 4, 7, 10},			// SEVENTH
		{0, 3, 7, 10},			// MINOR_7TH
		{0, 4, 7, 11},			// MAJOR_7TH
		{0, 4, 7, 9},			// SIXTH
		{0, 3, 7, 9},			// MINOR_6TH
		{0, 4, 8},				// AUGMENTED
		{0, 4, 8, 10},			// AUGMENTED_7TH
		{0, 3, 6},				// DIMINISHED
		{0, 3, 6, 9},			// DIMINISHED_7TH
		{0, 4, 6, 10},			// SEVENTH_FF
		{0, 3, 6, 10},			// MINOR_7TH
		{0, 4, 7, 10, 14},		// NINTH
		{0, 3, 7, 10, 14},		// MINOR_9TH
		{0, 4, 7, 11, 14},		// MAJOR_9TH
		{0, 4, 7, 10, 14, 17},	// ELEVENTH
		{0, 4, 7, 10, 13},		// DIMINISHED_9TH
		{0, 4, 7, 14},			// ADDED_9TH
		{0, 4, 7, 17},			// ADDED_4TH
		{0, 5, 7},				// SUSPENDED
		{0, 2, 7},				// SUSPENDED_9TH
		{0, 5, 7, 10},			// SEVENTH_SUSPENDED_4TH
		{0, 2, 7, 10},			// SEVENTH_SUSPENDED_9TH
		{0, 7}					// FIFTH
	};

	private static int[] getMetrics( int chord ) {
		if( chord < MAJOR || chord > FIFTH )
			return new int[ 0 ];

		return metrics[ chord ];
	}

	public static int[] getIntervals() {
		return intervals;
	}

	public static int[] getNotes( int base, int type ) {
		int[] intv = getMetrics( type );
		int[] notes = new int[ intv.length ];

		/*
		 * Get the notes based on the corresponding intervals
		 */
		for( int i = 0; i < intv.length; i++ )
			notes[ i ] = Octave.getNote( base, intv[ i ] );

		/*
		 * Make sure that the chord does not contain something
		 * like A and A# together
		 */
		for( int i = 0; i < notes.length; i++ ) {
			if( Octave.isModified( notes[ i ] )) {
				int n = Octave.getBase( notes[ i ] );

				for( int j = i; j < notes.length; j++ ) {
					if( n == notes[ j ] ) {
						notes[ i ] = Octave.getAlias( notes[ i ] );
						break;
					}
				}
			}
		}

		return notes;
	}
}
