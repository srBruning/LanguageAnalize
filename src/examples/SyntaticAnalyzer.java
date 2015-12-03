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


	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.entrada = entrada;
		this.tableids = tableids;
		stakPosition= new Stack<>();
		currentPosition= -1;
		if(!nextToken()) return false;
		if(!expressao() || hasToken()){
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


	//    private boolean cmdIf(){
	//        if (currentToken.getType() == TypeToken.IF){
	//            if (currentToken.getType() == TypeToken.TK_OPENPARENTHESIS)
	//            {
	//	        	if(!nextToken()) return false;
	//	            if (expressao()){
	//	                if (currentToken.getType() == TypeToken.TK_CLOSEPARENTHESIS){
	//	                	nextToken();
	//	                    return cmdIfB();
	//	                }
	//	            }else previuToken();
	//            }
	//	   }    
	//        return false;
	//    }

	//    private boolean cmdIfB()
	//    {
	//        if (comando())
	//        {
	//            return cmdElse();
	//        }else
	//        {
	//            if (currentToken.getType() == TypeToken.PONTOVIRGULA)
	//            {
	//                nextToken();
	//                return cmdElse();
	//            }        
	//        }else
	//        {  
	//            if (currentToken.getType() == TK_OPENCHAVES)
	//            {
	//            	if(!nextToken()) return false;
	//	            if (listaComandos()){
	//	                if (currentToken.getType() == TypeToken.TK_CLOSECHAVES){
	//	                	nextToken();
	//	                    return cmdElse();
	//	                }
	//	            }else previuToken();
	//            }  
	//    }    

	//    private boolean cmdElse()
	//    {
	//        if (currentToken.getType() == TypeToken.ELSE){
	//            return corpoElse();
	//        }
	//        return true;
	//    }
	//    private boolean corpoElse(){
	//        if (comando()){
	//            return true;
	//        }else{
	//            if (currentToken.getType() == TypeToken.PONTOVIRGULA){
	//                return true;
	//            }        
	//        }else{  
	//            if (currentToken.getType() == TK_OPENCHAVES){
	//                if(!nextToken()) return false;
	//	            if (listaComandos()){
	//	                if (currentToken.getType() == TypeToken.TK_CLOSECHAVES){
	//	                	nextToken();
	//	                    return true;
	//	                }
	//	            }else previuToken();
	//            }  
	//        }
	//        return false;
	//    }

}