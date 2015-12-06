package module.syntactic;

import module.Token.TypeToken;

public class FunctionAnalyzer extends AbstractSyntacticAnalizer {
	private FunctionAnalyzer(SyntaticStrean strean){
		this.sntStrean = strean;
	}

	public static boolean isListFunctions(SyntaticStrean strean){
		return new FunctionAnalyzer(strean).listFunction();
	}

	public static boolean isFunction(SyntaticStrean strean){
		return new FunctionAnalyzer(strean).isFunction();
	}

	private boolean listFunction(){
		if(currentToken()==null)return true;
		
		sntStrean.pushPosition();
		if(isFunction() && listFunction()){
			sntStrean.popPosition();
			
			return true;
		}
		sntStrean.popPositionToToken();
		return true;
	}

	private boolean isFunction(){
		sntStrean.pushPosition();
		
		if(typeFunction()&& equalsAndHasNext(TypeToken.TK_ID) && 
				equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS) &&
				declarationParamsFunction() && equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS) &&
				headerFunction()){
			sntStrean.popPosition();
			
			return true;
		}
		sntStrean.popPositionToToken();
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
		if(CommandAnalyzer.isCommand(sntStrean)){
			return true;
		}
		sntStrean.pushPosition();
		if(equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && CommandAnalyzer.isListCommands(sntStrean) &&
				toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
			sntStrean.popPosition();
			return true;
		}
		sntStrean.popPositionToToken();
		return false;
	}
	private boolean typeFunction(){
		
		if(equalsAndHasNext(TypeToken.VOID)) return true;
		
		return type();
	}

}
