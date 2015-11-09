package excptions;

import java.util.ArrayList;

import examples.Token;

public class InvalidCharacterExcption extends Exception {
	private int linha;
	private int coluna;
	private String expected;
	private ArrayList<Token> lexemas;
	public InvalidCharacterExcption(ArrayList<Token> tks, int linha, int coluna, char expected){
		this( tks,  linha,  coluna,  new String(new char[]{expected}));
	}
	public InvalidCharacterExcption(ArrayList<Token> tks, int linha, int coluna, String expected){
		super("Unexpected character in line "+linha+" column "+coluna+",  expected "+expected);
		this.linha = linha;
		this.coluna = coluna;
		this.expected = expected;
		this.lexemas = tks;
	}
	public String simpleMessage(){
		return "Caracter inesperado, esperava: "+this.expected;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
	public ArrayList<Token> getLexemas() {
		return lexemas;
	}
	public void setLexemas(ArrayList<Token> lexemas) {
		this.lexemas = lexemas;
	}
	
	
}
