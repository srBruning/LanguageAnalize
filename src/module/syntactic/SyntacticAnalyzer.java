package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;

import module.Token;
import module.Token.TypeToken;

public class SyntacticAnalyzer {

	private HashMap<String, ArrayList<Token>> tableids;
	private SyntaticStrean sntStrean;

	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.tableids = tableids;
		this.sntStrean = new SyntaticStrean(entrada);
		//		this.expressao = new ExpressionAnalyzer(sntStrean);
		if(!sntStrean.nextToken()) return false;
		if(!listaComandos() || 
				sntStrean.hasNextToken()){
			return false;
		}
		return true;
	}

	private boolean cmdIf(){
		if(currentToken()== null) return false;
		sntStrean.pushPosition();
		if ( currentToken().getType() == TypeToken.IF)
			if (sntStrean.nextToken() && 
					currentToken().getType() == TypeToken.TK_OPENPARENTHESIS)
				if(sntStrean.nextToken() && ExpressionAnalyzer.isExpressao(sntStrean) && currentToken()!=null && currentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
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
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if (comando() && cmdElse()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.peekPosition();
		if (currentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			if(cmdElse() ){
				sntStrean.popPosition();
				return true;
			}
		}
		sntStrean.peekPosition();
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken() && listaComandos() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
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
		if(currentToken()==null ||
				( comando()
						&& listaComandos() ) ){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return true;
	}

	private boolean comando() {
		if(currentToken()==null ) return false;
		if(currentToken().getType()==TypeToken.TK_SEMICOLON ){
			sntStrean.nextToken();
			return true;
		}
		sntStrean.pushPosition();
		if( cmdSwitch() ||
				cmdIf() ||
				AnalyzerAssignment.isAssignmet(sntStrean)  ){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdElse(){

		if(currentToken()==null) return true;// empyt
		sntStrean.pushPosition();
		if ( currentToken().getType() == TypeToken.ELSE && sntStrean.nextToken() && corpoElse()) return true;
		sntStrean.popPositionToToken();
		return true;
	}
	private boolean corpoElse(){
		sntStrean.pushPosition();
		if (comando())return true;
		sntStrean.peekPosition();
		if (currentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}        
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken()&& listaComandos() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean cmdSwitch(){
		if(currentToken()== null) return false;
		sntStrean.pushPosition();
		if ( currentToken().getType() == TypeToken.SWITCH)
			if (sntStrean.nextToken() && 
					currentToken().getType() == TypeToken.TK_OPENPARENTHESIS)
				if(sntStrean.nextToken() &&
						ExpressionAnalyzer.isExpressao(sntStrean) &&
						currentToken()!=null && 
						currentToken().getType() == TypeToken.TK_CLOSEPARENTHESIS){
					sntStrean.nextToken();
					if(cmdSwitch_B()){
						sntStrean.popPosition();
						return true;
					}
				}
		sntStrean.popPositionToToken();
		return false;		
	}

	private boolean cmdSwitch_B(){
		if(currentToken() == null)return false;
		if(currentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			return true;
		}
		sntStrean.pushPosition();
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET && 
				sntStrean.nextToken()&& cmdSwitch_c() &&
				currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.peekPosition();
		if(cmdCase() || switchDefault()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;	
	}

	private boolean cmdSwitch_c(){
		if(currentToken() == null)return false;
		sntStrean.pushPosition();
		if(switchDefault() 
				|| (cmdCase() && listCmdCase())  ){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;	
	}

	private boolean listCmdCase(){	
		sntStrean.pushPosition();
		if(cmdCase() && listCmdCase() ){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.peekPosition();
		if(switchDefault()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return true;
	}

	private boolean switchDefault() {
		//default : SWITCH_Comandos
		sntStrean.pushPosition();
		if(currentToken()!=null && currentToken().getType()== TypeToken.DEFAULT && 
				sntStrean.nextToken() && currentToken().getType() == TypeToken.TK_COLON &&
				sntStrean.nextToken() && SwitchComands()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdCase(){		
		if(currentToken()== null) return false;
		sntStrean.pushPosition();
		if(currentToken().getType() == TypeToken.CASE && 
				sntStrean.nextToken() && ExpressionAnalyzer.isExpressao(sntStrean)&& 
				currentToken()!= null  && 
				currentToken().getType() == TypeToken.TK_COLON &&
				sntStrean.nextToken() && SwitchComands()){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return false;	
	}
	private boolean SwitchComands(){
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET){
			if(sntStrean.nextToken() && listaComandos() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
				sntStrean.nextToken();
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPosition();
			return false;
		}
		if (comando() ){
			sntStrean.popPosition();
			return true;
		} 
		sntStrean.popPositionToToken();
		return false;
	}

	private Token currentToken(){
		return sntStrean.getCurrentToken();
	}
	//	private boolean tipo(){
	//		switch (currentToken().getType()) {
	//		case  INT:
	//		case DOUBLE:
	//		case FLOAT:
	//			this.sntStrean.nextToken();
	//			return true;
	//		}
	//
	//		return false;
	//	}


}