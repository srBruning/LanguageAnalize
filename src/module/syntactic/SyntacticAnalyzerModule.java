package module.syntactic;

import java.util.HashMap;
import java.util.List;

import module.CausaErro;
import module.PlaceCod;

public class SyntacticAnalyzerModule extends AbstractAnaliseSintatica{

	public PlaceCod  analyzer(SyntaticStrean sntStrean) {
		setSntStrean(sntStrean);
		PlaceCod d = new PlaceCod();
		
		if(!sntStrean.nextToken()){
			d.erro = new CausaErro("esta em branco ", 0, 0, 0);
			return d;
		}
		getVariables().clear();	
		clear();
		AnaliseComando.isListCommands(getSntStrean(), d);
		
		return d;
	}

	
	
}