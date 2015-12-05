package module.syntactic;

import module.Token.TypeToken;

public class ExpressionAnalyzer {
	private SyntaticStrean sntStrean;
//
//	public void setStrean(SyntaticStrean strean) {
//		this.sntStrean = strean;
//	}
//
	private ExpressionAnalyzer(SyntaticStrean strean){
		this.sntStrean = strean;
	}

	public static boolean isExpressao(SyntaticStrean strean){
		return new ExpressionAnalyzer(strean).expressao();
	}

	public boolean expressao()	{
		sntStrean.pushPosition();
		if (expressao2() && expressao_b()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean expressao_b(){
		if(sntStrean.getCurrentToken()==null)return true;//dedrivou vazil
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_AND:
		case TK_OR:
			sntStrean.pushPosition();
			if ( sntStrean.nextToken() && expressao2() &&  expressao_b()){
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPositionToToken();
		}
		return true;//dedrivou vazil
	}

	private boolean expressao2()	{
		sntStrean.pushPosition();
		if (expressao3() && expressao2_b()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean expressao2_b(){
		if(sntStrean.getCurrentToken() == null)return true;
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_EQUALS:
		case TK_DIFF:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao3() &&  expressao2_b()){
				sntStrean.popPosition();
				return true;				
			}
			sntStrean.popPositionToToken();
		}
		return true;
	} 

	private boolean expressao3(){
		sntStrean.pushPosition();
		if (expressao4() && expressao3_b()){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return false;
	}

	private boolean expressao3_b(){
		if(sntStrean.getCurrentToken()==null)return true;
		switch (sntStrean.getCurrentToken().getType()){
		case TK_BIGGEREQUAL:
		case TK_LESSEQUAL:
		case TK_LESS:
		case TK_BIGGER:
			sntStrean.pushPosition();
			if(sntStrean.nextToken() && expressao4() && expressao3_b()){
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPositionToToken();
		}
		return true;
	}   

	private boolean expressao4(){
		sntStrean.pushPosition();
		if (expressao5() && expressao4_b()){
			sntStrean.popPosition();
			return true;			
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean expressao4_b(){
		if(sntStrean.getCurrentToken()==null)return true;
		sntStrean.pushPosition();
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_PLUS: 
		case TK_SUB: 
			if(sntStrean.nextToken() && expressao5() && expressao4_b()){
				sntStrean.popPosition();
				return true;			
			}
		}
		sntStrean.popPositionToToken();
		return true;
	}   

	private boolean expressao5(){
		sntStrean.pushPosition();
		if (expressao6() && expressao5_b()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean expressao5_b(){
		if(sntStrean.getCurrentToken()==null)return true;
		switch (sntStrean.getCurrentToken().getType()){
		case TK_MULTP:
		case TK_DIV:
		case TK_MOD:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao6() && expressao5_b() ){
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPositionToToken();
			return false;
		}
		return true;
	} 	

	@SuppressWarnings("incomplete-switch")
	private boolean expressao6(){
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_PLUS: 
		case TK_SUB: 
		case TK_NEG: 
			sntStrean.pushPosition();
			if( sntStrean.nextToken() && expressao6()){
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPositionToToken();
			return false;
		}
		return isBaseExpressao();
	}     

	@SuppressWarnings("incomplete-switch")
	public boolean isBaseExpressao()	{

		switch ( sntStrean.getCurrentToken().getType() ) {
		case CONST_NUM:
		case TK_ID:
			sntStrean.nextToken();
			return true;
		case TK_OPENPARENTHESIS:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao()){
				if (sntStrean.getCurrentToken()!=null && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
					sntStrean.nextToken();
					sntStrean.popPosition();
					return true;
				}
			}
			sntStrean.popPositionToToken();

		}

		return false;
	}


}