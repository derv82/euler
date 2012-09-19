public class e52 {
	public static Primes primes = new Primes();
	public static final int MAX_NUMBER = 256;
	
	public static void main(String[] args) {
		int[] a = generateArray(5);
		printArray(a);
		while (incrementArray(a)) {
			if (primeConcat(a)) {
				System.out.print("Prime concat: ");
				int sum = 0;
				for (int i = 0; i < a.length; i++) {
					System.out.print(a[i] + " ");
					sum += a[i];
				}
				System.out.print("\nSum: ");
				System.out.println(sum);
				break;
			}
		}
		printArray(a);
	}

	public static void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) 
			System.out.print(a[i] + ",");
		System.out.println();
	}

	public static int[] generateArray(int len) {
		if (len == 5) {
			return new int[] { 3, 7, 109, 673, 677};
		}
		int[] a = new int[len];
		for (int i = 0; i < len; i++)
			a[i] = (i * 2) + 3;
		int index = len - 1;
		return a;
	}

	public static boolean incrementArray(int[] a) {
		int index = a.length - 1;
		while (true) {
			if (index < 0) break;
			a[index] += 2;
			if (a[index] > MAX_NUMBER) {
				if (index == 0) return false;
				a[index] = a[index - 1] + 4;
				index--;
				continue;
			}
			if (index < a.length - 1) { 
				index = a.length - 1;
				continue;
			}
			if (primes.isPrime(a[index]))
				return true;
			index = a.length - 1;
		}
		return false;
	}

	// Concatenates two integers (a + b). Same as Integer.parseInt(a + "" + b);
	public static int concat(int a, int b) {
		int l = (int) Math.log10((double) b) + 1;
		return a * (int) Math.pow(10, l) + b;
	}

	// Checks if all combinations of any two elements of array a are prime.
	public static boolean primeConcat(int[] a) {
		for (int i = 0; i < a.length - 1; i++)
			for (int j = i + 1; j < a.length; j++)
				if (!primes.isPrime(concat(a[i], a[j])) || !primes.isPrime(concat(a[j], a[i])))
					return false;
		return true;
	}

}
