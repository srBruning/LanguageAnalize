package examples;

import java.util.ArrayList;
import java.util.HashMap;

import examples.Token.TypeToken;

public class SyntaticAnalyzer {

	private ArrayList<Token> entrada;
	private HashMap<String, ArrayList<Token>> tableids;
	private int currentPosition;
	Token currentToken;

	private boolean hasToken(){
			return currentPosition < entrada.size()-1;
	}
	
	private boolean nextToken(){
		currentPosition++;
		if(currentPosition >= entrada.size()) return false;
		currentToken = entrada.get(currentPosition);
		return true;
	}	
	private boolean previuToken(){
		currentPosition--;
		if(currentPosition <0) return false;
		currentToken = entrada.get(currentPosition);
		return true;
	}

	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.entrada = entrada;
		this.tableids = tableids;
		currentPosition=-1;
		while(hasToken()){
			if(!expressao()){
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	private boolean baseExpressao()	{
		if(!nextToken()) return false;
	    switch ( currentToken.getType() ) {
	        case CONST_NUM:
	        case TK_ID:
	            return true;
	        case TK_OPENPARENTHESIS:
	            if (expressao()){
	            	if(!nextToken()) return false;
	                if (currentToken.getType() == TypeToken.TK_CLOSEPARENTHESIS){
	                    return true;
	                }else previuToken();
	            }else previuToken();
	           
	     }

	     return false;
	} 

	private boolean expressao() {
		return baseExpressao();		
	}

}
