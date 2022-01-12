package guitar.core;

/**
 * @author Serge Yuschenko
 */
public class Scale {
	public static final int MAJOR		= 0;
	public static final int MINOR		= 1;
	public static final int PENTATONIC	= 2;
	public static final int HEXATONIC2	= 3;
	public static final int HEXATONIC6	= 4;

	private static final int[]scales = {
		MAJOR, MINOR, PENTATONIC, HEXATONIC2, HEXATONIC6
	};

	private static final int[][] intervals = {
		{0, 2, 4, 5, 7, 9, 11, 12},	// MAJOR
		{0, 2, 3, 5, 7, 8, 10, 12},	// MINOR
		{0,    3, 5, 7,    10, 12},	// PENTATONIC
		{0, 2, 3, 5, 7,    10, 12},	// HEXATONIC 1
		{0,    3, 5, 7, 8, 10, 12}	// HEXATONIC 2
	};

	public static int[] getIntervals( int scale ) {
		if( scale < 0 || scale > scales.length )
			return new int[ 0 ];

		return intervals[ scale ];
	}
	
	public static int[] getScales() {
		return scales;
	}

	public static int[] getNotes( int root, int scale ) {
		int[] intv = getIntervals( scale );
		int[] notes = new int[ intv.length ];

		/*
		 * Get the notes based on the corresponding intervals
		 */
		for( int i = 0; i < intv.length; i++ )
			notes[ i ] = Octave.getNote( root, intv[ i ] );

		/*
		 * Make sure that the sequence does not contain something
		 * like A and A# together
		 */
		for( int i = 0; i < notes.length; i++ ) {
			if( Octave.isModified( notes[ i ] )) {
				int n = Octave.getBase( notes[ i ] );

				for( int j = 0; j < notes.length; j++ ) {
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
