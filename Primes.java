/** 
 * Calculates and stores prime integers for quick and inexpensive lookups.
 *
 * Running times:
 *  - Looking up pre-calculated prime:            O(1)
 *  - Calculating the next prime in the sequence: O(N)
 *  - Calculating a prime out of sequence:        O(N^2)
 *
 * Memory usage:
 *  - 1/2 bit for each digit
 *  - Example: Memory to store all primes up to the number 64 costs 32 bits (1 int)
 * 
 * TODO Convert all ints to longs, overload methods to receive ints.
 * 
 * @author derv
 */
public class Primes {
	
	/** 
	 * Size of an integer in Java.
	 * Standard on all JVMs, but put here for explicitness.
	 */
	private final int INT_SIZE = 32;
	
	/**
	 * The most-recently-calculated prime (in sequence)
	 * All numbers equal to or less than currentPrime are already in the bitmap.
	 */
	private long currentPrime = 1;
	
	/** 
	 * Bitmap tracking which integers are prime.
	 * We know even numbers will not be prime (excluding 2).
	 *
	 * Example:
	 * First element's 7 (LSBs): ...  1   1   1   0   1    1    0
	 * These bits translate to:  ... [3] [5] [7] [9] [11] [13] [15]
	 * 1 indicates number is prime, 0 means number is not prime.
	 * We can easily check if a number is a prime by ANDing with the appropriate bit.
	 */
	private int[] oddBitmap;
	
	/** Initializes bitmap. */
	public Primes() { 
		oddBitmap = new int[1];
	}

	/** 
	 * Initializes bitmap to contain all primes up to capcity.
	 * @param capacity The highest prime to pre-calculate.
	 */
	public Primes(long capacity) {
		// Initialize array to hold all the incoming bits.
		// Code will resize the bitmap on the fly, but this prevents that slow-down.
		int bitmapSize = (int) ((capacity - 3) / 64) + 1;
		oddBitmap = new int[bitmapSize];
		// Generate bitmap for primes up (and possibly beyond) to the given number
		isPrime(capacity);
	}
	/**
	 * Initializes bitmap to contain all primes up to capacity.
	 * @param capacity The highest prime to pre-calculate.
	 */
	public Primes(int capacity) { 
		this( (long) capacity);
	}

	
	/** 
	 * Calculates the next prime in sequence and adds to bitmap.
	 * @return The next prime in the sequence.
	 */
	public long nextPrime() {
		// First prime must be 2. This requires some ugly hacking.
		if (currentPrime < 2) {
			currentPrime += 2;
			return 2;
		} else if (currentPrime == 3) {
			currentPrime -= 2; // isPrime will increase currentPrime by 2.
		}
		while (!isPrime(currentPrime));
		return currentPrime - 2;
	}
	
	/** 
	 * Adds a number (prime or not) to the bitmap.
	 * Doubles bitmap size as needed.
	 * @param n The number to add.
	 * @param prime True if n is prime, false otherwise.
	 */
	private void addNumber(long n, boolean prime) {
		long oddIndex = ((n - 3) / 2); // '3' is the first bit, ignore evens.
		int row = (int) (oddIndex / 32);
		int col = (int) (oddIndex % 32);
		if (row >= oddBitmap.length) {
			// We need to increase the size of the bitmap
			// Increase by 2x current size
			int[] newBitmap = new int[oddBitmap.length * 2];
			System.arraycopy(oddBitmap, 0, newBitmap, 0, oddBitmap.length);
			for (int i = oddBitmap.length; i < newBitmap.length; i++) {
				newBitmap[i] = 0;
			}
			oddBitmap = newBitmap;
		}
		int i = (prime ? 1 : 0);
		i = i << col;
		oddBitmap[row] = (oddBitmap[row] | i);
	}

	/** 
	 * Performs the O(n) computation to see if a number is prime.
	 * @param n The number to check if prime or not.
	 * @return True if n is prime, false otherwise.
	 */
	private boolean calculatePrime(long n) {
		// Ignore multiples of 2 (except 2).
		if (n < 2)      return false;
		if (n == 2)     return true;
		if (n % 2 == 0) return false;
		long maxFactor = (long) Math.sqrt(n); // Highest unique factor of n
		// Only check odd factors.
		for (long i = 3; i <= maxFactor; i += 2) {
			// If is a factor, then n is not prime.
			if (n % i == 0) return false;
		}
		return true;
	}

	/** Array of all single bit masks. */
	public static final int[] BIT_MASKS = {
		2 ^ 0,  2 ^ 1,  2 ^ 2,  2 ^ 3,  2 ^ 4, 
		2 ^ 5,  2 ^ 6,  2 ^ 7,  2 ^ 8,  2 ^ 9, 
		2 ^ 10, 2 ^ 11, 2 ^ 12, 2 ^ 13, 2 ^ 14, 
		2 ^ 15, 2 ^ 16, 2 ^ 17, 2 ^ 18, 2 ^ 19, 
		2 ^ 20, 2 ^ 21, 2 ^ 22, 2 ^ 23, 2 ^ 24, 
		2 ^ 25, 2 ^ 26, 2 ^ 27, 2 ^ 28, 2 ^ 29,
		2 ^ 30, 2 ^ 31
	};
	
	/** 
	 * Performs the O(n) computation to see if a number is prime.
	 * Only checks prime factors, ignores non-prime factors.
	 * @param n The number to check if prime or not.
	 * @return True if n is prime, false otherwise.
	 */
	private boolean newcalculatePrime(int n) {
		// Ignore multiples of 2 (except 2).
		if (n < 2)      return false;
		if (n == 2)     return true;
		if (n % 2 == 0) return false;
		long maxFactor = (long) Math.sqrt(n); // Highest possible factor of n.
		long current = 1;
		int currentRow = 0, currentCol = 0;
		while (current <= maxFactor) {
			current += 2;
			if (currentCol >= INT_SIZE) {
				currentRow++;
				currentCol = 0;
			}
			if ( (oddBitmap[currentRow] & BIT_MASKS[currentCol]) == 0) {
				currentCol++;
				continue;
			}
			if (n % current == 0) return false;
		}
		return true;
	}
	
	/** 
	 * Checks if n is a prime. 
	 * Calculates all primes up to n if they haven't been calculated yet.
	 * @param n The number to check for primality.
	 * @return True if number is prime, false otherwise.
	 */
	public boolean isPrime(long n) {
		// Fill bitmap with all primes up to n (if needed)
		while (currentPrime <= n) {
			currentPrime += 2;
			addNumber(currentPrime, calculatePrime(currentPrime));
		}
		// Check for even numbers
		if (n < 2)      return false;
		if (n == 2)     return true;
		if (n % 2 == 0) return false;
		// Find prime in bitmap
		long oddIndex = (n - 3) / 2; // '3' is the first bit, ignore evens.
		int row = (int) (oddIndex / 32);
		int col = (int) (oddIndex % 32);
		int i = 1 << col; // Create bitmask for current col index
		return (oddBitmap[row] & i) > 0;
	}
	/** 
	 * Checks if n is a prime. 
	 * Calculates all primes up to n if they haven't been calculated yet.
	 * @param n The number to check for primality.
	 * @return True if number is prime, false otherwise.
	 */
	public boolean isPrime(int n) {
		return isPrime( (long) n );
	}

	/** Displays bitmap. */
	public void printBitmap() {
		// Highest row that may contain at least 1 bit.
		long maxRow = (((currentPrime - 1) / 2) / 32);
		System.out.print("Bitmap of odd primes (from 3 to " + (currentPrime - 2) + "):\n\t");
		// Iterate over every row in bitmap (except non-calculated rows)
		for (int row = 0; row < oddBitmap.length && row <= maxRow; row++) {
			int temp = 1; // Will be shifted to check for bits in bitmap
			// Iterate over every bit in this row
			for (int i = 0; i < INT_SIZE; i++) {
				boolean prime = (temp & oddBitmap[row]) > 0;
				System.out.print( prime ? "1" : "0" );
				temp = temp << 1; // Shift to next bit
			}
			System.out.print("\n\t");
		}
		System.out.println();
	}
}
