package guitar.core;

/**
 * @author Serge Yuschenko
 */
public class Octave {
	public static final int A		= 0;
	public static final int A_FLAT	= 1;
	public static final int A_SHARP	= 2;
	public static final int B		= 3;
	public static final int B_FLAT	= 4;
	public static final int C		= 5;
	public static final int C_SHARP	= 6;
	public static final int D		= 7;
	public static final int D_FLAT	= 8;
	public static final int D_SHARP = 9;
	public static final int E		= 10;
	public static final int E_FLAT	= 11;
	public static final int F		= 12;
	public static final int F_SHARP	= 13;
	public static final int G		= 14;
	public static final int G_SHARP	= 15;
	public static final int G_FLAT	= 16;

	private static final int[] order = {
		A, A_SHARP,
		B,
		C, C_SHARP,
		D, D_SHARP,
		E,
		F, F_SHARP,
		G, G_SHARP
	};

	private static final int[][] alias = {
		{ A_SHARP, B_FLAT },
		{ B_FLAT, A_SHARP },
		{ C_SHARP, D_FLAT },
		{ D_FLAT, C_SHARP },
		{ D_SHARP, E_FLAT },
		{ E_FLAT, D_SHARP },
		{ F_SHARP, G_FLAT },
		{ G_FLAT, F_SHARP },
		{ G_SHARP, A_FLAT },
		{ A_FLAT, G_SHARP }
	};

	private static final int[][] deviation = {
		{ A, A_SHARP },
		{ A, A_FLAT },
		{ B, B_FLAT },
		{ C, C_SHARP },
		{ D, D_SHARP },
		{ D, D_FLAT },
		{ E, E_FLAT },
		{ F, F_SHARP },
		{ G, G_FLAT },
		{ G, G_SHARP }
	};

	public static int getNote( int key, int interval ) {
		for( int i = 0; i < order.length; i++ )
			if( order[ i ] == key )
				return order[ ( i + interval ) % order.length ];

		key = getAlias( key );

		for( int i = 0; i < order.length; i++ )
			if( order[ i ] == key )
				return order[ ( i + interval ) % order.length ];

		return -1; // this will never happen
	}

	public static int[] getNotes() {
		return order;
	}

	public static int getAlias( int note ) {
		for( int i = 0; i < alias.length; i++ )
			if( note == alias[ i ][ 0 ] )
				return alias[ i ][ 1 ];

		return note;
	}

	public static boolean isModified( int note ) {
		return	isNote( note ) && note != A && note != B && note != C &&
				note != D && note != E && note != F && note != G;
	}

	public static boolean isNote( int note ) {
		return	note >= A && note <= G_FLAT;
	}

	public static int getBase( int note ) {
		for( int i = 0; i < deviation.length; i++ )
			if( note == deviation[ i ][ 1 ] )
				return deviation[ i ][ 0 ];

		return note;
	}
}
