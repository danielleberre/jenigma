package jenigma;
public class Reflector implements RightToLeft, InOutNext {

	public static final Reflector REFLECTOR_A = new Reflector(
			"EJMZALYXVBWFCRQUONTSPIKHGD");
	public static final Reflector REFLECTOR_B = new Reflector(
			"YRUHQSLDPXNGOKMIEBFZCWVJAT");
	public static final Reflector REFLECTOR_C = new Reflector(
			"FVPJIAOYEDRZXWGCTKUQSBNMHL");
	public static final Reflector REFLECTOR_B_THIN = new Reflector(
			"ENKQAUYWJICOPBLMDXZVFTHRGS");
	public static final Reflector REFLECTOR_C_THIN = new Reflector(
			"RDOBJNTKVEHMLFCWZAXGYIPSUQ");

	public static final Reflector IDENTITY = new Reflector(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	
	private int[] mapping;
	private InOut prevRotor;

	/**
	 * Creates a reflector according to existing real Enigma mappings.
	 * 
	 * Reflector A EJMZALYXVBWFCRQUONTSPIKHGD Reflector B
	 * YRUHQSLDPXNGOKMIEBFZCWVJAT Reflector C FVPJIAOYEDRZXWGCTKUQSBNMHL
	 * Reflector B Thin ENKQAUYWJICOPBLMDXZVFTHRGS 1940 M4 R1 (M3 + Thin)
	 * Reflector C Thin RDOBJNTKVEHMLFCWZAXGYIPSUQ 1940 M4 R1 (M3 + Thin) ETW
	 * ABCDEFGHIJKLMNOPQRSTUVWXYZ Enigma I source:
	 * http://en.wikipedia.org/wiki/Enigma_rotor_details
	 *
	 * @param assignment
	 */
	public Reflector(String assignment) {
		if (assignment.length() != 26) {
			throw new IllegalArgumentException();
		}
		mapping = new int[26];
		for (int i = 0; i < mapping.length; i++) {
			mapping[i] = (char) (assignment.charAt(i) - 'A');
		}
	}

	@Override
	public void pushIndex(int a) {
		highlightIndex(a);
	}

	@Override
	public void highlightIndex(int a) {
		prevRotor.highlightIndex(mapping[a]);
	}

	@Override
	public void setPrevRotor(InOut r) {
		this.prevRotor = r;
	}

	@Override
	public void nextLetter() {
		// do nothing???
	}

    @Override
    public void doubleStepping() {
        // do nothing
    }
}
