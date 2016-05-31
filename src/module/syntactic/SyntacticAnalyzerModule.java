package module.syntactic;

import java.util.HashMap;
import java.util.List;

import module.CausaErro;
import module.PlaceCod;

public class SyntacticAnalyzerModule extends AbstractAnaliseSintatica{

	public String analyzer(SyntaticStrean sntStrean) {
		setSntStrean(sntStrean);
		if(!sntStrean.nextToken()){
			addErro("esta em branco ");
			return null;
		}
		getVariables().clear();		
		PlaceCod d = new PlaceCod();
		AnaliseComando.isListCommands(getSntStrean(), d);
		
		return d.cod;
	}

	public List<CausaErro> getErro(){
		return getSntStrean().getErro();		
	}

	public HashMap<String, String> getVariables(){
		return getSntStrean().getVariables();
	}
	
}