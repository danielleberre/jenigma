package jenigma;

public class DLBEnigma implements Enigma, InOut {

    private Rotor left, middle, right;
    private Reflector reflector;

    private StringBuilder encodedMessage = new StringBuilder();

    public DLBEnigma(Rotor left, Rotor middle, Rotor right, Reflector reflector) {
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.reflector = reflector;
        this.left.setPrevRotor(middle);
        this.left.setNextRotor(reflector);
        this.middle.setPrevRotor(right);
        this.middle.setNextRotor(left);
        this.right.setPrevRotor(this);
        this.right.setNextRotor(middle);
        this.reflector.setPrevRotor(left);
    }

    public void setKey(char l, char m, char r) {
        this.left.setLetter(l);
        this.middle.setLetter(m);
        this.right.setLetter(r);
    }

    public String getKey() {
        StringBuilder stb = new StringBuilder();
        stb.append(this.left.getLetter());
        stb.append(this.middle.getLetter());
        stb.append(this.right.getLetter());
        return stb.toString();
    }

    public void push(String str) {
        for (int i = 0; i < str.length(); i++) {
            pushChar(str.charAt(i));
        }
    }

    public void pushChar(char a) {
        if (Character.isLetter(a)) {
            char c = Character.toUpperCase(a);
            this.pushIndex((char) (c - 'A'));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void pushIndex(int a) {
        this.middle.doubleStepping();
        this.right.nextLetter();
        this.right.pushIndex(a);
    }

    @Override
    public void highlightIndex(int a) {
        encodedMessage.append((char) ('A' + a));
    }

    public String getEncryptedMessage() {
        return encodedMessage.toString();
    }

    public void resetMessage() {
        encodedMessage.delete(0, encodedMessage.length());
    }

    @Override
    public void setRotor(Position pos, int rotorNumber) {
        switch (pos) {
        case LEFT:
            left = RealRotor.getByNumber(rotorNumber);
            if (middle != null) {
                this.left.setPrevRotor(middle);
                this.middle.setNextRotor(left);
            }
            this.left.setNextRotor(reflector);
            this.reflector.setPrevRotor(left);
            left.setLetter('A');
            break;

        case MIDDLE:
            middle = RealRotor.getByNumber(rotorNumber);
            if (left != null) {
                this.left.setPrevRotor(middle);
                this.middle.setNextRotor(left);
            }
            if (right != null) {
                this.middle.setPrevRotor(right);
                this.right.setNextRotor(middle);
            }
            middle.setLetter('A');
            break;
        case RIGHT:
            right = RealRotor.getByNumber(rotorNumber);
            if (middle != null) {
                this.middle.setPrevRotor(right);
                this.right.setNextRotor(middle);
            }
            right.setPrevRotor(this);
            right.setLetter('A');
            break;
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public int getRotor(Position pos) {
        switch (pos) {
        case LEFT:
            if (left == null) {
                return 0;
            }
            return left.number();
        case MIDDLE:
            if (middle == null) {
                return 0;
            }
            return middle.number();
        case RIGHT:
            if (right == null) {
                return 0;
            }
            return right.number();
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public void moveRotorToLetter(Position pos, char letter) {
        switch (pos) {
        case LEFT:
            left.setLetter(letter);
            break;
        case MIDDLE:
            middle.setLetter(letter);
            break;
        case RIGHT:
            right.setLetter(letter);
            break;
        }
    }

    @Override
    public char getRotorLetter(Position pos) {
        switch (pos) {
        case LEFT:
            if (left == null) {
                return ' ';
            }
            return left.getLetter();
        case MIDDLE:
            if (middle == null) {
                return ' ';
            }
            return middle.getLetter();
        case RIGHT:
            if (right == null) {
                return ' ';
            }
            return right.getLetter();
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String getCurrentLetters() {
        StringBuilder stb = new StringBuilder();
        if (this.left == null) {
            stb.append(" ");
        } else {
            stb.append(this.left.getLetter());
        }
        if (this.middle == null) {
            stb.append(" ");
        } else {
            stb.append(this.middle.getLetter());
        }
        if (this.right == null) {
            stb.append(" ");
        } else {
            stb.append(this.right.getLetter());
        }
        return stb.toString();
    }

    @Override
    public char encode(char c) {
        pushChar(c);
        return encodedMessage.charAt(encodedMessage.length() - 1);
    }
}
