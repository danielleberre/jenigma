package jenigma;

public interface Rotor extends LeftToRight,RightToLeft {

    Rotor NONE = new Rotor() {
        @Override
        public void setNextRotor(InOutNext r) {
            // do nothing
        }
        @Override
        public void setPrevRotor(InOut r) {
            // do nothing
        }
        @Override
        public void setLetter(char a) {
            // do nothing
        }
        @Override
        public char getLetter() {
            return '-';
        }
        @Override
        public int number() {
            return 0;
        }
        @Override
        public void nextLetter() {
            // do nothing
        }
        @Override
        public void doubleStepping() {
            // do nothing
        }
        @Override
        public void pushIndex(int a) {
            // do nothing
        }
        @Override
        public void highlightIndex(int a) {
            // do nothing
        }
    };

	void setLetter(char a);
		
	char getLetter();
	
	int number();
}
