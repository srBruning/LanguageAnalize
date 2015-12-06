package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import module.Token;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	private HashMap<String, ArrayList<Token>> tableids;

	public Stack<String> analyzer(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids) {
		this.tableids = tableids;
		this.sntStrean = new SyntaticStrean(entrada);
		//		this.expressao = new ExpressionAnalyzer(sntStrean);
		if(!sntStrean.nextToken()){
			pushError("esta em branco ");

		}else   start()   ;

		start()   ;
		return  sntStrean.getErrors();
	}

	public String getErro(){
		return this.sntStrean.getErro();		
	}

	private boolean start(){
		if(currentToken()==null)return false;
		if( FunctionAnalyzer.isFunction(sntStrean) && start2()){
			return true;
		}
		if( CommandAnalyzer.isVariablesDeclarationscommand(sntStrean) && start()){
			return true;
		}
		return false;
	}

	private boolean start2() {
		if(currentToken()==null)return true;
		if(start()){return true;}
		return true;
	}


}