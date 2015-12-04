package examples;

import java.util.ArrayList;
import java.util.HashMap;

import examples.Token.TypeToken;

public class SyntaticAnalyzer {

	private HashMap<String, ArrayList<Token>> tableids;
	private SyntaticStrean sntStrean;
	private AnalizadorExpressao expressao;

	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.tableids = tableids;
		this.sntStrean = new SyntaticStrean(entrada);
		this.expressao = new AnalizadorExpressao(sntStrean);
		if(!sntStrean.nextToken()) return false;
		if(!listaComandos() || 
				sntStrean.hasNextToken()){
			return false;
		}
		return true;
	}

	private boolean cmdIf(){
		if(sntStrean.getCurrentToken()== null) return false;
		sntStrean.pushPosition();
		if ( sntStrean.getCurrentToken().getType() == TypeToken.IF)
			if (sntStrean.nextToken() && 
					sntStrean.getCurrentToken().getType() == TypeToken.TK_OPENPARENTHESIS)
				if(sntStrean.nextToken() && expressao.isExpressao() && sntStrean.getCurrentToken()!=null && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
					sntStrean.nextToken();
					if(cmdIfB()){
						sntStrean.popPosition();
						return true;
					}
				}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdIfB(){
		if(sntStrean.getCurrentToken()==null)return false;
		sntStrean.pushPosition();
		if (comando() && cmdElse()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.peekPosition();
		if (sntStrean.getCurrentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			if(cmdElse() ){
				sntStrean.popPosition();
				return true;
			}
		}
		sntStrean.peekPosition();
		if (sntStrean.getCurrentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken() && listaComandos() && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			if ( cmdElse() ){
				sntStrean.popPosition();
				return true;
			}
		}  
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean listaComandos() {
		sntStrean.pushPosition();
		if(sntStrean.getCurrentToken()==null ||
				( comando()
						&& listaComandos() ) ){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return true;
	}

	private boolean comando() {
		if(sntStrean.getCurrentToken()==null ) return false;
		if(sntStrean.getCurrentToken().type==TypeToken.TK_SEMICOLON ){
			sntStrean.nextToken();
			return true;
		}
		sntStrean.pushPosition();
		if( cmdIf() || 
				cmdAssignmet()  ){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdElse(){

		if(sntStrean.getCurrentToken()==null) return true;// empyt
		sntStrean.pushPosition();
		if ( sntStrean.getCurrentToken().getType() == TypeToken.ELSE && sntStrean.nextToken() && corpoElse()) return true;
		sntStrean.popPositionToToken();
		return true;
	}
	private boolean corpoElse(){
		sntStrean.pushPosition();
		if (comando())return true;
		sntStrean.peekPosition();
		if (sntStrean.getCurrentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}        
		if (sntStrean.getCurrentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken()&& listaComandos() && sntStrean.getCurrentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean tipo(){
		switch (sntStrean.getCurrentToken().getType()) {
		case  INT:
		case DOUBLE:
		case FLOAT:
			this.sntStrean.nextToken();
			return true;
		}

		return false;
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
	//CMD_ATRIBUICAO ->
	//    TK_ID = CMD_ATRIBUICAO_b
	private boolean cmdAssignmet(){
		sntStrean.pushPosition();
		if(sntStrean.getCurrentToken()==null || sntStrean.getCurrentToken().getType()!=TypeToken.TK_ID)return false;
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
		if(this.expressao.isExpressao() && cmdAssignmetAux2()){
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