package excptions;

public class InvalidCharacterExcption extends Exception {
	public InvalidCharacterExcption(int linha, int coluna, char expected){
		super("Unexpected character in line "+linha+" column "+coluna+",  expected "+expected);
	}
	public InvalidCharacterExcption(int linha, int coluna, String expected){
		super("Unexpected character in line "+linha+" column "+coluna+",  expected "+expected);
	}
}
