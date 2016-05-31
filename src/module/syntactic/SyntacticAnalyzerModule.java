package module.syntactic;

import java.util.HashMap;
import java.util.List;

import module.CausaErro;
import module.PlaceCod;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	public String analyzer(SyntaticStrean sntStrean) {
		setSntStrean(sntStrean);
		if(!sntStrean.nextToken()){
			addErro("esta em branco ");
			return null;
		}
		getVariables().clear();		
		PlaceCod d = new PlaceCod();
		AnalyzerAssignment.isAssignmet(getSntStrean(), d);
		
		return d.cod;
	}

	public List<CausaErro> getErro(){
		return getSntStrean().getErro();		
	}

	public HashMap<String, String> getVariables(){
		return getSntStrean().getVariables();
	}
	
}