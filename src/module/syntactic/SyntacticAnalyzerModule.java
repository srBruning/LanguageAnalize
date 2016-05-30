package module.syntactic;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import module.CausaErro;
import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	public String analyzer(SyntaticStrean sntStrean) {
		setSntStrean(sntStrean);
		if(!sntStrean.nextToken()){
			addErro("esta em branco ");
			return null;
		}

		PlaceCod ePlaceCod = new PlaceCod();
		if (! ExpressionAnalyzer.isExpressao(sntStrean, ePlaceCod) )
			return null;
		if (getSntStrean().hasNextToken()){
			addErro("Esperava tokem de final de comando");
			return null;
		}
		return ePlaceCod.cod;
	}

	public List<CausaErro> getErro(){
		return getSntStrean().getErro();		
	}

	
	


}