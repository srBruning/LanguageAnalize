package module.syntactic;

import module.Token;
import module.Token.TypeToken;

public class AnalyzerAssignment extends AbstractSyntacticAnalizer{

	private AnalyzerAssignment(SyntaticStrean strean){
		this.sntStrean = strean;
	}

	public static boolean isAssignmet(SyntaticStrean strean){
		return new AnalyzerAssignment(strean).cmdAssignmet();
	}

	public static boolean isAssignmetOperator(SyntaticStrean strean){
		return new AnalyzerAssignment(strean).assignmentOperator();
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
		pushError("experava um tokem tipo");
		return false;
	}


	//CMD_ATRIBUICAO ->
	//    TK_ID = CMD_ATRIBUICAO_b
	private boolean cmdAssignmet(){
		sntStrean.pushPosition();
		if(!equalsAndHasNext(TypeToken.TK_ID)){
			pushError("eperava um identificador");
			sntStrean.popPosition();
			return false;
		}
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
		Token cToken = currentToken();
		int pr = getPositionErrors();
		if(cToken!=null ){
			sntStrean.pushPosition();
			if( equalsAndHasNext(TypeToken.TK_COMMA) &&  cmdAssignmet()){
				sntStrean.popPosition();
				setPositionErrors(pr);
				return true;
			}
			if( equalsAndHasNext(TypeToken.TK_SEMICOLON)){
				sntStrean.popPosition();
				setPositionErrors(pr);
				return true;
			}
			sntStrean.popPositionToToken();
		}
		pushError("esperava uma virgula ou um ponto e virgula");
		return false;
	}

}
