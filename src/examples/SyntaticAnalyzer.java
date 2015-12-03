package examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import examples.Token.TypeToken;

public class SyntaticAnalyzer {
	private ArrayList<Token> entrada;
	private HashMap<String, ArrayList<Token>> tableids;
	private int currentPosition;
	Stack<Integer> stakPosition= new Stack<>();
	Token currentToken;

	private boolean hasToken(){
		return currentPosition <= entrada.size()-1;
	}

	private boolean nextToken(){
		currentPosition++;
		if(currentPosition >= entrada.size()){
			currentToken=null;
			return false;
		}
		currentToken = entrada.get(currentPosition);
		return true;
	}	

	private void pushPosition(){
		this.stakPosition.push(currentPosition);		
	}

	private void popPosition(){
		currentPosition = this.stakPosition.pop();		
		currentToken = entrada.get(currentPosition);
	}

	private void peekPosition(){
		currentPosition = this.stakPosition.peek();		
		currentToken = entrada.get(currentPosition);
	}


	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.entrada = entrada;
		this.tableids = tableids;
		stakPosition= new Stack<>();
		currentPosition= -1;
		if(!nextToken()) return false;
		if(!listaComandos() || hasToken()){
			return false;
		}
		return true;
	}

	private boolean expressao()	{
		pushPosition();
		if (expressao2() && expressao_b())
			return true;
		popPosition();
		return false;
	}

	private boolean expressao_b(){
		if(!hasToken())return true;//dedrivou vazil
		switch (currentToken.getType()) {
		case TK_AND:
		case TK_OR:
			pushPosition();
			if ( nextToken() && expressao2() &&  expressao_b())
				return true;
			popPosition();
		}
		return true;//dedrivou vazil
	}

	private boolean expressao2()	{
		pushPosition();
		if (expressao3() && expressao2_b())
			return true;
		popPosition();
		return false;
	}

	private boolean expressao2_b(){
		if(!hasToken())return true;
		switch (currentToken.getType()) {
		case TK_EQUALS:
		case TK_DIFF:
			pushPosition();
			if (nextToken() && expressao3() &&  expressao2_b())
				return true;				
			popPosition();
		}
		return true;
	} 

	private boolean expressao3(){
		pushPosition();
		if (expressao4() && expressao3_b())
			return true;

		popPosition();
		return false;
	}

	private boolean expressao3_b(){
		if(!hasToken())return true;
		switch (currentToken.getType()){
		case TK_BIGGEREQUAL:
		case TK_LESSEQUAL:
		case TK_LESS:
		case TK_BIGGER:
			pushPosition();
			if(nextToken() && expressao4() && expressao3_b())
				return true;
			popPosition();
		}
		return true;
	}   

	private boolean expressao4(){
		pushPosition();
		if (expressao5() && expressao4_b()){
			return true;			
		}
		popPosition();
		return false;
	}

	private boolean expressao4_b(){
		if(!hasToken())return true;
		pushPosition();
		switch (currentToken.getType()) {
		case TK_PLUS: 
		case TK_SUB: 
			if(nextToken() && expressao5() && expressao4_b())
				return true;			
		}
		popPosition();
		return true;
	}   

	private boolean expressao5(){
		pushPosition();
		if (expressao6() && expressao5_b())return true;
		popPosition();
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean expressao5_b(){
		if(!hasToken())return true;
		switch (currentToken.getType()){
		case TK_MULTP:
		case TK_DIV:
		case TK_MOD:
			pushPosition();
			if (nextToken() && expressao6() && expressao5_b() ){
				return true;
			}
			popPosition();
			return false;
		}
		return true;
	} 	

	@SuppressWarnings("incomplete-switch")
	private boolean expressao6(){
		switch (currentToken.getType()) {
		case TK_PLUS: 
		case TK_SUB: 
		case TK_NEG: 
			int m_token = currentPosition;
			if( nextToken() && expressao6()){
				return true;
			}
			currentPosition = m_token;
			return false;
		}
		return baseExpressao();
	}     

	@SuppressWarnings("incomplete-switch")
	private boolean baseExpressao()	{

		switch ( currentToken.getType() ) {
		case CONST_NUM:
		case TK_ID:
			nextToken();
			return true;
		case TK_OPENPARENTHESIS:
			int m_token = currentPosition;
			if (nextToken() && expressao()){
				if (hasToken() && currentToken.getType() == TypeToken.TK_CLOSEPARENTHESIS){
					nextToken();
					return true;
				}
			}
			currentPosition = m_token;

		}

		return false;
	} 


	private boolean cmdIf(){
		if(!hasToken()) return false;
		pushPosition();
		if ( currentToken.getType() == TypeToken.IF)
			if (nextToken() && currentToken.getType() == TypeToken.TK_OPENPARENTHESIS)
				if(nextToken() && expressao() && currentToken.getType() == TypeToken.TK_CLOSEPARENTHESIS){
					nextToken();
					if(cmdIfB()) return true;
				}
		popPosition();
		return false;
	}

	private boolean cmdIfB(){
		pushPosition();
		if (comando() && cmdElse()) return true;
		peekPosition();
		if (currentToken.getType() == TypeToken.TK_SEMICOLON){
			nextToken();
			if(cmdElse() )return true;
		}
		peekPosition();
		if (currentToken.getType() == TypeToken.TK_OPEN_BRAKET && nextToken() && listaComandos() && currentToken.getType() == TypeToken.TK_CLOSE_BRAKET){
			nextToken();
			if ( cmdElse() ) return true;
		}  
		popPosition();
		return true;
	}

	private boolean listaComandos() {
		pushPosition();
		if(!hasToken()) return true;
		if( comando() && listaComandos() ) return true;

		popPosition();
		return true;
	}

	private boolean comando() {
		// TODO Auto-generated method stub
		if(!hasToken() ) return false;
		pushPosition();

		if( cmdIf() ) return true;

		popPosition();
		return false;
	}

	private boolean cmdElse(){
		if(!hasToken()) return true;// empyt
		pushPosition();
		if ( currentToken.getType() == TypeToken.ELSE && corpoElse()) return true;
		popPosition();
		return true;
	}
	private boolean corpoElse(){
		pushPosition();
		if (comando())return true;
		peekPosition();
		if (currentToken.getType() == TypeToken.TK_SEMICOLON){
			nextToken();
			return true;
		}        
		if (currentToken.getType() == TypeToken.TK_OPEN_BRAKET && nextToken()&& listaComandos() && currentToken.getType() == TypeToken.TK_CLOSE_BRAKET){
			nextToken();
			return true;
		}  
		popPosition();
		return false;
	}

}