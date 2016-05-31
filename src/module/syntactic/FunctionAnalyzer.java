package module.syntactic;

import module.Token.TypeToken;

public class FunctionAnalyzer extends AbstractSyntacticAnalizer {
	private FunctionAnalyzer(SyntaticStrean strean){
		this.setSntStrean(strean);
	}

	public static boolean isListFunctions(SyntaticStrean strean){
		return new FunctionAnalyzer(strean).listFunction();
	}

	public static boolean isFunction(SyntaticStrean strean){
		return new FunctionAnalyzer(strean).isFunction();
	}

	private boolean listFunction(){
		if(currentToken()==null)return true;
		
		getSntStrean().pushPosition();
		if(isFunction() && listFunction()){
			getSntStrean().popPosition();
			
			return true;
		}
		getSntStrean().popPositionToToken();
		return true;
	}

	private boolean isFunction(){
		getSntStrean().pushPosition();
		
		if(typeFunction()&& equalsAndHasNext(TypeToken.TK_ID) && 
				equalsAndHasNext(TypeToken.TK_OPENPARENTHESIS) &&
				declarationParamsFunction() && equalsAndHasNext(TypeToken.TK_CLOSEPARENTHESIS) &&
				headerFunction()){
			getSntStrean().popPosition();
			
			return true;
		}
		getSntStrean().popPositionToToken();
		return false;
	}

	private boolean declarationParamsFunction() {
//		getSntStrean().pushPosition();
//
//		
//		if(type() && equalsAndHasNext(TypeToken.TK_ID) && listDeclarationParamsFunction()){
//			getSntStrean().popPosition();
//		}else getSntStrean().popPositionToToken();
//
//		
		return true;
	}

	private boolean listDeclarationParamsFunction() {
		getSntStrean().pushPosition();
		if(equalsAndHasNext(TypeToken.TK_COMMA) && declarationParamsFunction() && listDeclarationParamsFunction()){
			getSntStrean().popPosition();
			
		}else getSntStrean().popPositionToToken();
		
		return true;
	}

	private boolean headerFunction() {
		if(CommandAnalyzer.isCommand(getSntStrean())){
			return true;
		}
		getSntStrean().pushPosition();
		if(equalsAndHasNext(TypeToken.TK_OPEN_BRAKET) && 
				CommandAnalyzer.isListCommands(getSntStrean()) &&
				toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
			getSntStrean().popPosition();
			return true;
		}
		getSntStrean().popPositionToToken();
		return false;
	}
	private boolean typeFunction(){
		return false;
		
//		if(equalsAndHasNext(TypeToken.VOID)) return true;
//		
//		return type();
	}

}
