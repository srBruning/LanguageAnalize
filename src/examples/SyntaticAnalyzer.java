package examples;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xml.internal.utils.Hashtree2Node;

import examples.Token.TypeToken;

public class SyntaticAnalyzer {

	private ArrayList<Token> entrada;
	private HashMap<String, ArrayList<Token>> tableids;
	private int currentPosition;
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
	private boolean previuToken(){
		currentPosition--;
		if(currentPosition <0) return false;
		currentToken = entrada.get(currentPosition);
		return true;
	}

	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.entrada = entrada;
		this.tableids = tableids;
		currentPosition= -1;
		if(!nextToken()) return false;
		while(hasToken()){
			if(!expressao()){
				return false;
			}
		}
		return true;
	}
	
	private boolean expressao5(){
    	int m_poss= currentPosition;
	    if (expressao6())
	    {
	        if(expressao5_b()){
	        	return true;
	        }else{
	        	currentPosition = m_poss;
	        	return false;
	        }
	    }
	    return false;
	}
	    
	@SuppressWarnings("incomplete-switch")
	private boolean expressao5_b()
	{
		if(!hasToken())return true;
	    switch (currentToken.getType()) 
	    {
	        case TK_MULTP:
	        case TK_DIV:
	        case TK_MOD:
	        	int m_poss= currentPosition;
	            if(!nextToken())return false;
	            if (expressao6())
	            {
	                if( expressao5_b())
	                	return true;
	                else{
	                	currentPosition = m_poss;
	                	return false;
	                }
	            }
	            previuToken();
	            return false;
	     }
	    return true;
	} 	
    
    @SuppressWarnings("incomplete-switch")
	private boolean expressao6()
    {
        switch (currentToken.getType()) 
        {
            case TK_PLUS: 
            case TK_SUB: 
            case TK_NEG: 
                if(!nextToken()) return false;
                if( expressao6()){
                	return true;
                }else{
                	previuToken();
                	return false;
                }
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
	        	if(!nextToken()) return false;
	            if (expressao()){
	                if (currentToken.getType() == TypeToken.TK_CLOSEPARENTHESIS){
	                	nextToken();
	                    return true;
	                }
	            }else previuToken();
	           
	     }

	     return false;
	} 

	private boolean expressao() {
        // por enquanto so reconhece elemetos basicos como: "(1)" ou "(var)" ou "1 2 3 "
        // agora deve reconhece !(1) +6 - 7 +(-var)
		return expressao5();		
	}

}