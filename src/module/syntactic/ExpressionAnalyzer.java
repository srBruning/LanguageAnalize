package module.syntactic;

import module.Token.TypeToken;

public class ExpressionAnalyzer extends AbstractSyntacticAnalizer{
	private ExpressionAnalyzer(SyntaticStrean strean){
		this.sntStrean = strean;
	}

	public static boolean isExpressao(SyntaticStrean strean){
		return new ExpressionAnalyzer(strean).expressao();
	}

	private boolean expressao()	{
		sntStrean.pushPosition();
		int p = getPositionErrors();
		if(equalsAndHasNext(TypeToken.TK_ID) && 
				AnalyzerAssignment.isAssignmetOperator(sntStrean) && expressao()	){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}
		sntStrean.peekPosition();
		if(expressao1()){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}
		
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean expressao1()	{
		sntStrean.pushPosition();
		int p = getPositionErrors();
		if (expressao2() && expressao1_b()){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean expressao1_b(){
		int p = getPositionErrors();
		if(sntStrean.getCurrentToken()==null)return true;//dedrivou vazil
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_AND:
		case TK_OR:
			sntStrean.pushPosition();
			if ( sntStrean.nextToken() && expressao2() &&  expressao1_b()){
				sntStrean.popPosition();
				setPositionErrors(p);
				return true;
			}
			sntStrean.popPositionToToken();
		}
		return true;//dedrivou vazil
	}

	private boolean expressao2()	{
		sntStrean.pushPosition();
		int p = getPositionErrors();
		if (expressao3() && expressao2_b()){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean expressao2_b(){
		int p = getPositionErrors();
		if(sntStrean.getCurrentToken() == null)return true;
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_EQUALS:
		case TK_DIFF:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao3() &&  expressao2_b()){
				sntStrean.popPosition();
				setPositionErrors(p);
				return true;				
			}
			sntStrean.popPositionToToken();
		}
		return true;
	} 

	private boolean expressao3(){
		sntStrean.pushPosition();
		int p = getPositionErrors();
		if (expressao4() && expressao3_b()){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}

		sntStrean.popPositionToToken();
		return false;
	}

	private boolean expressao3_b(){
		if(sntStrean.getCurrentToken()==null)return true;
		int p = getPositionErrors();
		switch (sntStrean.getCurrentToken().getType()){
		case TK_BIGGEREQUAL:
		case TK_LESSEQUAL:
		case TK_LESS:
		case TK_BIGGER:
			sntStrean.pushPosition();
			if(sntStrean.nextToken() && expressao4() && expressao3_b()){
				sntStrean.popPosition();
				setPositionErrors(p);
				return true;
			}
			sntStrean.popPositionToToken();
		}
		return true;
	}   

	private boolean expressao4(){
		sntStrean.pushPosition();
		int p = getPositionErrors();
		if (expressao5() && expressao4_b()){
			sntStrean.popPosition();
			setPositionErrors(p);
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
		int p = getPositionErrors();
		if (expressao6() && expressao5_b()){
			sntStrean.popPosition();
			setPositionErrors(p);
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean expressao5_b(){
		int p= getPositionErrors();
		if(sntStrean.getCurrentToken()==null)return true;
		switch (sntStrean.getCurrentToken().getType()){
		case TK_MULTP:
		case TK_DIV:
		case TK_MOD:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao6() && expressao5_b() ){
				sntStrean.popPosition();
				setPositionErrors(p);
				return true;
			}
			sntStrean.popPositionToToken();
			pushError("esperava uma expreÃ§Ã£o apos operador");
			return false;
		}
		return true;
	} 	

	@SuppressWarnings("incomplete-switch")
	private boolean expressao6(){
		int p = getPositionErrors();
		switch (sntStrean.getCurrentToken().getType()) {
		case TK_PLUS: 
		case TK_SUB: 
		case TK_NEG: 
			sntStrean.pushPosition();
			if( sntStrean.nextToken() && expressao6()){
				sntStrean.popPosition();
				setPositionErrors(p);
				return true;
			}
			sntStrean.popPositionToToken();
			pushError("esperava um expressao apos operador binario");
			return false;
		}
		if(isBaseExpressao()){
			setPositionErrors(p);
			return true;
		}
		pushError("esperava um operador binario");
		return false;
	}     

	@SuppressWarnings("incomplete-switch")
	private boolean isBaseExpressao()	{
		int p = getPositionErrors();
		switch ( sntStrean.getCurrentToken().getType() ) {
		case CONST_NUM:
			sntStrean.nextToken();
			return true;			
		case TK_ID:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && sntStrean.getCurrentToken().getType() == TypeToken.TK_OPENPARENTHESIS){
				if (sntStrean.nextToken() && expressao() || sntStrean.getCurrentToken()!=null && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
					if (sntStrean.getCurrentToken()!=null && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
						sntStrean.nextToken();
						sntStrean.popPosition();
						setPositionErrors(p);
						return true;
					}
				}			
			}	
			sntStrean.popPositionToToken();
			sntStrean.nextToken();
			setPositionErrors(p);
			return true;
		case TK_OPENPARENTHESIS:
			sntStrean.pushPosition();
			if (sntStrean.nextToken() && expressao()){
				if (sntStrean.getCurrentToken()!=null && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
					sntStrean.nextToken();
					sntStrean.popPosition();
					setPositionErrors(p);
					return true;
				}else{
					sntStrean.setErro("esperava fecha parentesis proximo a posição"+sntStrean.getCurrentPosition());
				}
			}
			sntStrean.popPositionToToken();
		}
		pushError("esperava uma constante ou um identificador");
		return false;
	}


}
