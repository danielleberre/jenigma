package jenigma;

public interface Rotor extends LeftToRight,RightToLeft {

	void setLetter(char a);
		
	char getLetter();
	
	int number();
}
