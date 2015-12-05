package module.syntactic;

import module.Token;
import module.Token.TypeToken;

public class AnalyzerAssignment {
	private SyntaticStrean sntStrean;

	private AnalyzerAssignment(SyntaticStrean strean){
		this.sntStrean = strean;
	}
	//OPERADOR_ATRIB
	//	TK_ASSINGMENT | TK_ADDASSIGNMENT | TK_SUBASSIGNMENT | TK_DIVASSIGNMENT | TK_MULTPASSIGNMENT | TK_MODASSIGNMENT
	private boolean assignmentOperator(){
		if(sntStrean.getCurrentToken()==null)return false;
		switch (sntStrean.getCurrentToken().getType()) {
		case  TK_ASSINGMENT:
		case TK_ADDASSIGNMENT:
		case TK_SUBASSIGNMENT:
		case TK_DIVASSIGNMENT:
		case TK_MULTPASSIGNMENT:
			//case TK_MODASSIGNMENT:
			this.sntStrean.nextToken();
			return true;
		}		
		return false;
	}
	
	public static boolean isAssignmet(SyntaticStrean strean){
		return new AnalyzerAssignment(strean).cmdAssignmet();
	}
	
	//CMD_ATRIBUICAO ->
	//    TK_ID = CMD_ATRIBUICAO_b
	private boolean cmdAssignmet(){
		if(sntStrean.getCurrentToken()==null || 
				sntStrean.getCurrentToken().getType()!=TypeToken.TK_ID)return false;
		sntStrean.pushPosition();
		sntStrean.nextToken();
		if(! assignmentOperator()){
			sntStrean.popPosition();
			return false;
		}
		if(!cmdAssignmetAux()){
			sntStrean.popPositionToToken();
			return false;
		}
		sntStrean.popPosition();
		return true;

	}


	//CMD_ATRIBUICAO_b ->
	//    EXPRESSAO CMD_ATRIBUICAO_c
	//    CMD_ATRIBUICAO
	private boolean cmdAssignmetAux() {
		if(sntStrean.getCurrentToken()==null)return false;
		sntStrean.pushPosition();
		if(ExpressionAnalyzer.isExpressao(sntStrean) && cmdAssignmetAux2()){
			sntStrean.popPosition();
			return true;
		}
		if(cmdAssignmet()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}
	
	//CMD_ATRIBUICAO_c
	//    , CMD_ATRIBUICAO
	//    ø
	private boolean cmdAssignmetAux2() {
		Token cToken = sntStrean.getCurrentToken();
		if(cToken==null )return false;
		
		if( cToken.getType()== TypeToken.TK_COMMA){
			sntStrean.pushPosition();
			if(sntStrean.nextToken() &&  cmdAssignmet()){
				sntStrean.popPosition();
				return true;
			}
		}
		if( cToken.getType()== TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			return true;
		}
		return false;
	}
	
}
