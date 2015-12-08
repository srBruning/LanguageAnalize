package module.syntactic;

import module.Token;
import module.Token.TypeToken;

public abstract class AbstractSyntacticAnalizer {

	protected SyntaticStrean sntStrean;

	protected void setError(String item){
		if(currentToken()!=null)
			item = item+", linha:"+currentToken().getLinha()+", coluna: "+currentToken().getPosIni();
		sntStrean.setErro(item);
	}


	protected Token currentToken(){
		return sntStrean.getCurrentToken();
	}
	protected boolean equalsAndHasNext(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type  && sntStrean.nextToken()  ){
			return true;
		};
		setError("esperava  "+type);
	
		return false;
	}

	protected boolean toNextIfEquals(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type){
			sntStrean.nextToken();
			return true;
		}
		setError("esperava  "+type);
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
