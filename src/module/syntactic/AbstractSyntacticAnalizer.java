package module.syntactic;

import module.Token;
import module.Token.TypeToken;

public abstract class AbstractSyntacticAnalizer {

	protected SyntaticStrean sntStrean;
	
	protected Token currentToken(){
		return sntStrean.getCurrentToken();
	}
	protected boolean equalsAndHasNext(TypeToken type){
		return (currentToken()!= null  && currentToken().getType() == type  && sntStrean.nextToken() );
	}

	protected boolean toNextIfEquals(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type){
			sntStrean.nextToken();
			return true;
		}
		return false;
	}
	
	protected boolean type(){
		switch (currentToken().getType()) {
		case  INT:
		case DOUBLE:
		case FLOAT:
			this.sntStrean.nextToken();
			return true;
		}

		return false;
	}
	
}
