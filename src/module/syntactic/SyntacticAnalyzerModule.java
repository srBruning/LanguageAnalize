package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;

import module.Token;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	private HashMap<String, ArrayList<Token>> tableids;

	public boolean analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.tableids = tableids;
		this.sntStrean = new SyntaticStrean(entrada);
		//		this.expressao = new ExpressionAnalyzer(sntStrean);
		if(!sntStrean.nextToken()) return false;
		if(!start() || sntStrean.hasNextToken()){
			return false;
		}
		return true;
	}

private boolean start(){
	if(currentToken()==null)return true;
	if(FunctionAnalyzer.isFunction(sntStrean) || CommandAnalyzer.isVariablesDeclarationscommand(sntStrean)){
		return start();
	}
	return true;
}


}