package jenigma;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class RealRotor implements Rotor, Transferable {

    private static final String IDENTITY_MAPPING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final RealRotor I = new RealRotor("I", 1, "EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    public static final RealRotor II = new RealRotor("II", 2, "AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E');
    public static final RealRotor III = new RealRotor("III", 3, "BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V');
    public static final RealRotor IV = new RealRotor("IV", 4, "ESOVPZJAYQUIRHXLNFTGKDCMWB", 'J');
    public static final RealRotor V = new RealRotor("V", 5, "VZBRGITYUPSDNHLXAWMJQOFECK", 'Z');
    public static final RealRotor VI = new DoubleAdvanceRotor("VI", 6, "JPGVOUMFYQBENHZRDKASXLICTW", 'Z', 'M');
    public static final RealRotor VII = new DoubleAdvanceRotor("VII", 7, "NZJHGRCXMYSWBOUFAIVLPEKQDT", 'Z', 'M');
    public static final RealRotor VIII = new DoubleAdvanceRotor("VIII", 8, "FKQHTLXOCBJSPDZRAMEWNIUYGV", 'Z', 'M');

    public static final RealRotor IDENTITY1 = new RealRotor("I1", 9, IDENTITY_MAPPING, 'Z');
    public static final RealRotor IDENTITY2 = new RealRotor("I2", 10, IDENTITY_MAPPING, 'Z');
    public static final RealRotor IDENTITY3 = new RealRotor("I3", 11, IDENTITY_MAPPING, 'Z');

    public static final DataFlavor ROTOR_FLAVOR = new DataFlavor(RealRotor.class, "Enigma Rotor");

    private static final RealRotor[] rotors = { I, II, III, IV, V, VI, VII, VIII, IDENTITY1, IDENTITY2, IDENTITY3 };

    protected int index = 0;
    protected InOutNext nextRotor;
    private InOut prevRotor;
    private int[] mapping;
    private final int[] invmapping;
    private final int changerOne;
    private final String name;
    private final int number;

    public static class DoubleAdvanceRotor extends RealRotor {
        private final int changerTwo;

        public DoubleAdvanceRotor(String name, int number, String assignment, char changerOne, char changerTwo) {
            super(name, number, assignment, changerOne);
            if (changerOne == changerTwo) {
                throw new IllegalArgumentException("Changer positions must be different");
            }
            this.changerTwo = changerTwo - 'A';
        }

        @Override
        public void doubleStepping() {
            super.doubleStepping();
            if (this.index == changerTwo) {
                nextLetter();
            }
        }

        @Override
        public void nextLetter() {
            if (this.index == changerTwo) {
                nextRotor.nextLetter();
            }
            super.nextLetter();
        }

    }

    /**
     * 
     * @param assignment
     * 
     *  EKMFLGDQVZNTOWYHXUSPAIBRCJ 1930 Enigma I II
     *  AJDKSIRUXBLHWTMCQGZNPYFVOE 1930 Enigma I III
     *  BDFHJLCPRTXVZNYEIWGAKMUSQO 1930 Enigma I IV
     *  ESOVPZJAYQUIRHXLNFTGKDCMWB DEC 1938 M3 Army V
     *  VZBRGITYUPSDNHLXAWMJQOFECK DEC 1938 M3 Army VI
     *  JPGVOUMFYQBENHZRDKASXLICTW 1939 M3 & M4 Naval (FEB 1942) VII 
     *  NZJHGRCXMYSWBOUFAIVLPEKQDT 1939 M3 & M4 Naval (FEB 1942) VIII 
     *  FKQHTLXOCBJSPDZRAMEWNIUYGV 1939 M3 & M4 Naval (FEB 1942)
     */
    public RealRotor(String name, int number, String assignment, char changerOne) {
        if (assignment.length() != 26) {
            throw new IllegalArgumentException("Substitution should be exactly 26 letters");
        }
        this.name = name;
        this.number = number;
        this.changerOne = changerOne - 'A';
        mapping = new int[26];
        invmapping = new int[26];
        char j;
        for (int i = 0; i < mapping.length; i++) {
            j = (char) (assignment.charAt(i) - 'A');
            if (j < 0 || j >= mapping.length) {
                throw new IllegalArgumentException("Chars should be upper case letters.");
            }
            mapping[i] = j;
            invmapping[j] = i;
        }
    }

    @Override
    public void setLetter(char a) {
        if (Character.isLetter(a)) {
            char c = Character.toUpperCase(a);
            index = c - 'A';
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public char getLetter() {
        return (char) ('A' + index);
    }

    public static int mod(int a) {
        if (a < 0) {
            return a + 26;
        }
        if (a >= 26) {
            return a - 26;
        }
        return a;
    }

    @Override
    public void pushIndex(int a) {
        nextRotor.pushIndex(mod(mapping[mod(a + index)] - index));
    }

    @Override
    public void doubleStepping() {
        if (this.index == changerOne) {
            nextLetter();
        }
    }

    @Override
    public void nextLetter() {
        if (this.index == changerOne) {
            nextRotor.nextLetter();
        }
        this.index++;
        if (this.index == mapping.length) {
            this.index = 0;
        }
    }

    @Override
    public void setNextRotor(InOutNext r) {
        this.nextRotor = r;
    }

    @Override
    public void setPrevRotor(InOut r) {
        this.prevRotor = r;
    }

    @Override
    public void highlightIndex(int a) {
        prevRotor.highlightIndex(mod(invmapping[mod(a + index)] - index));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int number() {
        return number;
    }

    public static Rotor getByNumber(int i) {
        if (i > 0 && i <= rotors.length) {
            return rotors[i - 1];
        }
        throw new IllegalArgumentException();
    }

    public static RealRotor[] rotors() {
        return rotors;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { ROTOR_FLAVOR };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ROTOR_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return number;
    }
}
