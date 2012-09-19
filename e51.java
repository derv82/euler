public class e51 {
	public static Primes primes = new Primes();

	public static void main(String[] args) {
		generateNumbers(6);
	}

	public static void generateNumbers(int size) {
		int highestPrimeCount = 0; 
		String highestPrimePattern = "";
		// Array of integers that constitute the generated number
		// e.g. {1, 0, 2, 4} constitutes the number 1024.
		int[] a = new int[size];
		for (int i = 0; i < size; i++)
			a[i] = 0;
		a[0] = 1;
		int arrIndex = size - 1; // Start at LSD
		while (true) {
			// Every possible number has been hit, stop.
			if (arrIndex < 0) break; 
			
			// Increase current index
			a[arrIndex]++;
			if (a[arrIndex] > 10) {
				// We have exceeded the capacity of the index
				// Reset and move onto the next LSD
				a[arrIndex] = 0;
				arrIndex--;
				continue;
			}
			// Reset index back to LSD after successful iteration
			arrIndex = size - 1;
			
			// Ensure the generated number contains at least one wildcard
			boolean containsWildcard = false;
			for (int i = 0; i < a.length && !containsWildcard; i++)
				if (a[i] == 10)
					containsWildcard = true;
			if (!containsWildcard) {
				continue;
			}
			
			// try all combinations of 0-9 for the wildcard(s)
			int primeCount = 0;
			for (int guess = 0; guess < 10; guess++) {
				int result = 0;
				for (int i = 0; i < a.length; i++) {
					int pow = (int) Math.pow(10, a.length - i - 1);
					if (a[i] == 10) {
						// Wildcard, replace with guess
						if (i == 0 && guess == 0) {
							result = 0;
							break;
						}
						result += guess * pow;
					} else {
						result += a[i] * pow;
					}
				}
				if (primes.isPrime(result))
					primeCount++;
			}
			if (primeCount > highestPrimeCount)
				highestPrimePattern = "";
			if (primeCount >= highestPrimeCount) {
				highestPrimeCount = primeCount;
				highestPrimePattern += arrToString(a) + ",";
			}
			//System.out.println("Prime count: " + primeCount);
		}

		System.out.println("\nHighest prime pattern: " + highestPrimePattern);
		System.out.println("Highest prime count: " + highestPrimeCount);
	}

	public static String arrToString(int[] a) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			result.append(a[i] == 10 ? "*" : a[i]);
		}
		return result.toString();
	}
}
