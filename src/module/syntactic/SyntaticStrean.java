package module.syntactic;

import java.util.ArrayList;
import java.util.Stack;

import module.Token;

public class SyntaticStrean {
	private ArrayList<Token> entrada;
	private int currentPosition;
	Stack<Integer> stakPosition= new Stack<>();
	Stack<String> errors= new Stack<>();
	Token currentToken;
	private String erro;


	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}

	public Stack<String> getErrors() {
		return errors;
	}
	public void setErrors(Stack<String> errors) {
		this.errors = errors;

	}
	public ArrayList<Token> getEntrada() {
		return entrada;
	}
	public int getCurrentPosition() {
		return currentPosition;
	}
	public Token getCurrentToken() {
		return currentToken;
	}
	public SyntaticStrean(ArrayList<Token> entrada){
		this.entrada = entrada;
		stakPosition= new Stack<>();
		currentPosition= -1;

	}
	public void peekPosition(){
		currentPosition = this.stakPosition.peek();		
		currentToken = entrada.get(currentPosition);
	}

	public boolean hasNextToken(){
		return currentPosition <= entrada.size()-1;
	}

	public boolean nextToken(){
		currentPosition++;
		if(currentPosition >= entrada.size()){
			currentToken=null;
			return false;
		}
		currentToken = entrada.get(currentPosition);
		return true;
	}	

	public void pushPosition(){
		this.stakPosition.push(currentPosition);		
	}

	public void popPosition(){
		this.stakPosition.pop();		
	}

	public void setCurrentToken(Token currentToken) {
		this.currentToken = currentToken;
	}
	public void popPositionToToken(){
		currentPosition = this.stakPosition.pop();	
		currentToken = entrada.get(currentPosition);
	}
}
