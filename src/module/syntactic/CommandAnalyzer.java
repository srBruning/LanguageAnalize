package module.syntactic;

import javafx.scene.SnapshotParameters;
import module.Token.TypeToken;

public class CommandAnalyzer extends AbstractSyntacticAnalizer {
	
	private CommandAnalyzer(SyntaticStrean strean ){
		sntStrean = strean;
	}
	public static boolean isCommand(SyntaticStrean strean){
		return new CommandAnalyzer(strean).iscommand();
	}

	public static boolean isListCommands(SyntaticStrean strean){
		return new CommandAnalyzer(strean).isListCommands();
	}

	public static boolean isVariablesDeclarationscommand(SyntaticStrean strean){
		return new CommandAnalyzer(strean).variablesDeclarations();
	}

	private boolean variablesDeclarations() {
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if(type() && variablesDeclarations_b()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean variablesDeclarations_b(){
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if( equalsAndHasNext(TypeToken.TK_ID ) && variablesDeclarations_c()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean variablesDeclarations_c() {
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.TK_ASSINGMENT) && ExpressionAnalyzer.isExpressao(sntStrean) &&
				variablesDeclarations_d()	){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.peekPosition();
		if(variablesDeclarations_d()){
			sntStrean.popPosition();
			return true;
			
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean variablesDeclarations_d() {
		if(currentToken()==null)return false;
		if(toNextIfEquals(TypeToken.TK_SEMICOLON))return true;
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.TK_COMMA) &&
				variablesDeclarations_b()){
			sntStrean.popPosition();
			return true;
			
		}
		sntStrean.popPositionToToken();
		return false;
	}
	
	private boolean isListCommands() {
		sntStrean.pushPosition();
		if(currentToken()==null ||
				( iscommand()
						&& isListCommands() ) ){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return true;
	}

	private boolean iscommand() {
		if(currentToken()==null ) return false;
		if(currentToken().getType()==TypeToken.TK_SEMICOLON ){
			sntStrean.nextToken();
			return true;
		}
		sntStrean.pushPosition();
		if( variablesDeclarations() ||
				cmdReturn() 
				|| cmdFor() || cmdWhile() ||
				cmdSwitch() ||
				cmdIf() ||
				AnalyzerAssignment.isAssignmet(sntStrean)||
				cmdExpression() || 
				FunctionAnalyzer.isFunction(sntStrean)){
			sntStrean.popPosition();
			return true;
		}

		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdExpression(){
		sntStrean.pushPosition();
		if(ExpressionAnalyzer.isExpressao(sntStrean) && toNextIfEquals(TypeToken.TK_SEMICOLON)){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdReturn() {
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.RETURN)){
			ExpressionAnalyzer.isExpressao(sntStrean);
			boolean has_semicolon;
			if( toNextIfEquals(TypeToken.TK_SEMICOLON)){
				sntStrean.popPosition();
				return true;				
			}else{
				sntStrean.popPositionToToken();
				return false;
			}

		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdFor(){
		if(sntStrean.getCurrentToken()==null)return false;
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.FOR) &&
				equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS) && expressao_for() &&
				equalsAndHasNext(TypeToken.TK_SEMICOLON) && expressao_for() &&
				equalsAndHasNext(TypeToken.TK_SEMICOLON) && expressao_for() && 
				equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS) ){
			if(cmdFor_B()){
				sntStrean.popPosition();
				return true;
			}
		}
		sntStrean.popPositionToToken();
		return false;	
	}
	private boolean cmdFor_B(){	
		if(sntStrean.getCurrentToken()==null)return false;
		sntStrean.pushPosition();
		if (equalsAndHasNext(TypeToken.TK_SEMICOLON) ){
			sntStrean.popPosition();
			return true;			
		}
		if (equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && isListCommands() &&
				equalsAndHasNext(TypeToken.TK_CLOSE_BRAKET)){
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.peekPosition();
		if (iscommand()){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;		
	}		
	private boolean expressao_for(){
		if(sntStrean.getCurrentToken()==null)return true;//accepts empty
		ExpressionAnalyzer.isExpressao(sntStrean);
		return true;		
	}


	private boolean cmdWhile(){
		if(sntStrean.getCurrentToken()== null) return false;
		sntStrean.pushPosition();

		if ( equalsAndHasNext( TypeToken.WHILE)  ){
			if ( !equalsAndHasNext( TypeToken.TK_OPENPARENTHESIS ) ){
				sntStrean.popPositionToToken();
				return false;
			}
			if(!ExpressionAnalyzer.isExpressao(sntStrean) || 
					!equalsAndHasNext( TypeToken.TK_CLOSEPARENTHESIS ) || !cmdWhile_B()){
				sntStrean.popPositionToToken();
				return false;
			}

			sntStrean.popPosition();
			return true;

		}
		if ( equalsAndHasNext( TypeToken.DO) ){
			if(cmdWhile_B()){
				if ( equalsAndHasNext( TypeToken.WHILE)  &&
						equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS) &&
						ExpressionAnalyzer.isExpressao(sntStrean) &&
						equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS) && 
						toNextIfEquals(TypeToken.TK_SEMICOLON) ){
					sntStrean.popPosition();
					return true;
				}
			}

		}
		sntStrean.popPositionToToken();
		return false;		
	}
	private boolean cmdWhile_B(){
		sntStrean.pushPosition();

		if (equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && isListCommands() && 
				toNextIfEquals( TypeToken.TK_CLOSE_BRAKET)  ){
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.peekPosition();
		if (iscommand()){
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
			if(sntStrean.nextToken() && isListCommands() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
				sntStrean.nextToken();
				sntStrean.popPosition();
				return true;
			}
			sntStrean.popPosition();
			return false;
		}
		if (iscommand() ){
			sntStrean.popPosition();
			return true;
		} 
		sntStrean.popPositionToToken();
		return false;
	}


	private boolean cmdIfB(){
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if (iscommand() && cmdElse()){
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
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken() && isListCommands() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			if ( cmdElse() ){
				sntStrean.popPosition();
				return true;
			}
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
		if (iscommand())return true;
		sntStrean.peekPosition();
		if (currentToken().getType() == TypeToken.TK_SEMICOLON){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}        
		if (currentToken().getType() == TypeToken.TK_OPEN_BRAKET && sntStrean.nextToken()&& isListCommands() && currentToken().getType() == TypeToken.TK_CLOSE_BRAKET){
			sntStrean.nextToken();
			sntStrean.popPosition();
			return true;
		}  
		sntStrean.popPositionToToken();
		return false;
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



}
