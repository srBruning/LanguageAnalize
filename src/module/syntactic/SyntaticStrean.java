package module.syntactic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import module.CausaErro;
import module.Token;

public class SyntaticStrean {
	private ArrayList<Token> entrada;
	private int currentPosition;
	Stack<Integer> stakPosition= new Stack<>();
	Token currentToken;
	private List<CausaErro> erro;

	private static HashMap<String, String> simbVaraibles;
	
	public List<CausaErro> getErro() {
		if (erro == null )
			erro = new ArrayList<>();
		return erro;
	}
	public void addErro(String msg, int linha, int colIni, int colFim) {
		getErro().add(new CausaErro(msg, linha, colIni, colFim));
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

	public Token getLastToken(){
		return entrada.get(entrada.size()-1);
	}
	
	public Token getPreviwToken(){
		return entrada.get(currentPosition >0 ? currentPosition-1 : currentPosition);
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
	
	public HashMap<String, String> getVariables(){
		if ( simbVaraibles == null)
			simbVaraibles = new HashMap<>();
		return simbVaraibles;
	}
	
	protected boolean addTabSimbulos(String place, String tipo){
		if (findSimbolById(place)!=null)
			return false;
		
		getVariables().put(place, tipo);
		return true;
	}
	
	protected String findSimbolById(String value) {
		return getVariables().get(value);
	}

}
