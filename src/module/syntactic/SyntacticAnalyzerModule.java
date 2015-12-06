package module.syntactic;

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

	public String getErro(){
		return this.sntStrean.getErro();		
	}

	private boolean start(){
		if(currentToken()==null)return false;
		if( FunctionAnalyzer.isFunction(sntStrean) &&
				start2()){
			return true;
		}
		if( CommandAnalyzer.isVariablesDeclarationscommand(sntStrean) &&
				start()){
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