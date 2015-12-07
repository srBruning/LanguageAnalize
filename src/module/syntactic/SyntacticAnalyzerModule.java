package module.syntactic;

import module.Token.TypeToken;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	public boolean analyzer(SyntaticStrean sntStrean) {
		this.sntStrean = sntStrean;
		//		this.expressao = new ExpressionAnalyzer(sntStrean);
		if(!sntStrean.nextToken()){
			setError("esta em branco ");
			return false;
		}

		return  start()   ;
	}

	private boolean start(){
		if(currentToken()==null)return false;

		return declarator() && start2();
	}

	private boolean start2() {	
		if(currentToken()==null)return true;
		
		sntStrean.pushPosition();
		if( declarator() && start())
			return true;
		sntStrean.popPositionToToken();
		
		return false;
				
	}


//	private boolean listFunction(){
//		if(currentToken()==null)return true;
//
//		sntStrean.pushPosition();
//		if(declarator() && listFunction()){
//			sntStrean.popPosition();
//
//			return true;
//		}
//		sntStrean.popPositionToToken();
//		return true;
//	}

	private boolean declarator(){
		sntStrean.pushPosition();

		if(typeFunction() && equalsAndHasNext(TypeToken.TK_ID)){
			
			if( equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS)){
				if(function_b()){ 
					sntStrean.popPosition();
					setError(null);
					return true;
				}else {
//					sntStrean.popPosition();
//					setError("Esperava uma função ");
					return false;
				}
			}
			if(variablesDeclarations_0()){
				sntStrean.popPosition();
				setError(null);
				return true;
			}
		}
		sntStrean.popPositionToToken();
		return false;

	}
	private boolean function_b(){
		sntStrean.pushPosition();

		if(	declarationParamsFunction() && equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS) ){
			setError(null);
			if(headerFunction()){
				sntStrean.popPosition();
				setError(null);
				return true;
			}
		}
		sntStrean.popPositionToToken();
		if(sntStrean.getErro()==null)setError("Esperava uma função ");
		return false;

	}


	private boolean declarationParamsFunction() {
		sntStrean.pushPosition();


		if(type() && equalsAndHasNext(TypeToken.TK_ID) && listDeclarationParamsFunction()){
			sntStrean.popPosition();
		}else sntStrean.popPositionToToken();


		return true;
	}

	private boolean listDeclarationParamsFunction() {
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.TK_COMMA) && declarationParamsFunction() && listDeclarationParamsFunction()){
			sntStrean.popPosition();

		}else sntStrean.popPositionToToken();

		return true;
	}

	private boolean headerFunction() {
		if(iscommand()){
			return true;
		}
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && isListCommands() &&
				toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean typeFunction(){

		boolean r = equalsAndHasNext(TypeToken.VOID) || type();
		setError(null);
		return r;
	}


//	private boolean variablesDeclarations_0() {
//		if(currentToken()==null)return false;
//		sntStrean.pushPosition();
//		if(type() && variablesDeclarations_b()){
//			sntStrean.popPosition();
//			return true;
//		}
//		sntStrean.popPositionToToken();
//		return false;
//	}
	private boolean variablesDeclarations_0(){
		if(currentToken()==null)return false;
		sntStrean.pushPosition();
		if(  variablesDeclarations_c()){
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
		if( 	cmdReturn() 
				|| cmdFor() || cmdWhile() ||
				cmdSwitch() ||
				cmdIf() ||
				AnalyzerAssignment.isAssignmet(sntStrean)||
				cmdExpression() || 
				declarator()){
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
		if ( equalsAndHasNext(TypeToken.SWITCH) && 
				equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS))
			if(ExpressionAnalyzer.isExpressao(sntStrean) &&
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

		if(toNextIfEquals( TypeToken.TK_SEMICOLON)){

			return true;
		}
		sntStrean.pushPosition();
		if (toNextIfEquals(TypeToken.TK_OPEN_BRAKET )&& cmdSwitch_c() &&
				toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
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

		if(equalsAndHasNext(TypeToken.DEFAULT) && equalsAndHasNext(TypeToken.TK_COLON)
				&& SwitchComands()){
			sntStrean.popPosition();

			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}

	private boolean cmdCase(){		
		if(currentToken()== null) return false;

		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.CASE) && ExpressionAnalyzer.isExpressao(sntStrean)&& 
				equalsAndHasNext(TypeToken.TK_COLON) && SwitchComands()){
			sntStrean.popPosition();

			return true;
		}

		sntStrean.popPositionToToken();
		return false;	
	}
	private boolean SwitchComands(){
		if(currentToken()==null)return false;

		sntStrean.pushPosition();
		if (equalsAndHasNext(TypeToken.TK_OPEN_BRAKET)){
			if(isListCommands() && equalsAndHasNext(TypeToken.TK_CLOSE_BRAKET)){

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
		if (toNextIfEquals( TypeToken.TK_SEMICOLON)){
			if(cmdElse() ){
				sntStrean.popPosition();

				return true;
			}
		}
		sntStrean.peekPosition();
		if (equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && isListCommands() &&
				toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
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
		if ( equalsAndHasNext(TypeToken.ELSE) && corpoElse()){

			return true;
		}
		sntStrean.popPositionToToken();

		return true;
	}
	private boolean corpoElse(){
		if (iscommand())return true;

		sntStrean.pushPosition();
		if (equalsAndHasNext(TypeToken.TK_SEMICOLON)){
			sntStrean.popPosition();

			return true;
		}       
		if (equalsAndHasNext( TypeToken.TK_OPEN_BRAKET) && 
				isListCommands() && equalsAndHasNext(TypeToken.TK_CLOSE_BRAKET)){
			sntStrean.popPosition();

			return true;
		}   
		sntStrean.popPositionToToken();
		return false;
	}


	private boolean cmdIf(){
		if(currentToken()== null) return false;
		sntStrean.pushPosition();

		if ( equalsAndHasNext(TypeToken.IF)){
			if (equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS)){
				if( ExpressionAnalyzer.isExpressao(sntStrean) && 
						equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS)){
					if(cmdIfB()){
						sntStrean.popPosition();

						return true;
					}
				}
			}

		}
		sntStrean.popPositionToToken();
		return false;
	}




}