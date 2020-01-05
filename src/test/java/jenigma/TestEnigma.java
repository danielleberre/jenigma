package jenigma;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEnigma {

    @Test
    public void testLetters() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'A', 'A');
        enigma.pushChar('E');
        enigma.pushChar('E');
        enigma.pushChar('E');
        enigma.pushChar('E');
        assertEquals("FLCW", enigma.getEncryptedMessage());
    }

    @Test
    public void testMessage() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'A', 'A');
        assertEquals("AAA", enigma.getKey());
        enigma.push("AAAAA");
        assertEquals("AAF", enigma.getKey());
        assertEquals("BDZGO", enigma.getEncryptedMessage());
    }

    @Test
    public void testSelf() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'A', 'A');
        assertEquals("AAA", enigma.getKey());
        enigma.push("BDZGO");
        assertEquals("AAF", enigma.getKey());
        assertEquals("AAAAA", enigma.getEncryptedMessage());
    }

    @Test
    public void testIdentity() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.IDENTITY1, RealRotor.IDENTITY2, RealRotor.IDENTITY3,
                Reflector.IDENTITY);
        enigma.setKey('A', 'A', 'A');
        assertEquals("AAA", enigma.getKey());
        enigma.push("AAAAA");
        assertEquals("AAF", enigma.getKey());
        assertEquals("AAAAA", enigma.getEncryptedMessage());
    }

    @Test
    public void testEncoding() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        // taken from http://en.wikipedia.org/wiki/Enigma_rotor_details
        enigma.push("AAAAA");
        assertEquals("BDZGO", enigma.getEncryptedMessage());
    }

    @Test
    public void simpleRotorChange() {
        // taken from http://en.wikipedia.org/wiki/Enigma_rotor_details
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'A', 'U');
        enigma.pushChar('X');
        assertEquals("AAV", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("ABW", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("ABX", enigma.getKey());
    }

    @Test
    public void doubleRotorChange() {
        // taken from http://en.wikipedia.org/wiki/Enigma_rotor_details
        DLBEnigma enigma = new DLBEnigma(RealRotor.I, RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'D', 'U');
        enigma.pushChar('X');
        assertEquals("ADV", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("AEW", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("BFX", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("BFY", enigma.getKey());
    }

    @Test
    public void doubleRotorChangeBis() {
        // taken from
        // http://users.telenet.be/d.rijmenants/en/enigmatech.htm#steppingmechanism
        // KDO KDP, KDQ, KER, LFS, LFT, LFU
        DLBEnigma enigma = new DLBEnigma(RealRotor.III, RealRotor.II, RealRotor.I, Reflector.REFLECTOR_B);
        enigma.setKey('K', 'D', 'O');
        enigma.pushChar('X');
        assertEquals("KDP", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("KDQ", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("KER", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("LFS", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("LFT", enigma.getKey());
        enigma.pushChar('X');
        assertEquals("LFU", enigma.getKey());
    }

    @Test
    public void testDLB() {
        DLBEnigma enigma = new DLBEnigma(RealRotor.I,RealRotor.II, RealRotor.III, Reflector.REFLECTOR_B);
        enigma.setKey('A', 'A', 'A');
        enigma.push("TROPXFORTEXCETTEXMACHINE");
        assertEquals("OCNXTQMTHCHKLAUNQDPIWSZZ",enigma.getEncryptedMessage());
        enigma.resetMessage();
        enigma.setKey('A', 'A', 'A');
        enigma.push("ELLEXESTXTROPXFORTEXCETTEXMACHINE");
        assertEquals("FEBWTNVRQAVHOHWVPIOUVPEWCALHAZZHR",enigma.getEncryptedMessage());
    }
}
