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
		if(!listaComandos() || sntStrean.hasNextToken()){
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
		// TODO Auto-generated method stub
		if(sntStrean.getCurrentToken()==null ) return false;
		if(sntStrean.getCurrentToken().type==TypeToken.TK_SEMICOLON ){
			sntStrean.nextToken();
			return true;
		}
		sntStrean.pushPosition();


		if( cmdIf()  ){
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

}