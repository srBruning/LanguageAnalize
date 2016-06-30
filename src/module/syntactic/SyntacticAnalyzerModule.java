package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import module.CausaErro;
import module.PlaceCod;
import module.ResultadoAnalizeBean;

public class SyntacticAnalyzerModule extends AbstractAnaliseSintatica{

	private List<CausaErro> erros;

	public ResultadoAnalizeBean  analyzer(SyntaticStrean sntStrean) {

		erros = new ArrayList<>();

		setSntStrean(sntStrean);

		if(!sntStrean.nextToken()){
			erros.add( new CausaErro("esta em branco ", 0, 0, 0) );
			return new ResultadoAnalizeBean(null, erros);
		}
		getVariables().clear();	
		clear();

		PlaceCod mPlaceCod = new PlaceCod();

		while(getSntStrean().hasNextToken()){
			PlaceCod d = new PlaceCod();

			if (!AnaliseDeclaracao.isDeclaracao(getSntStrean(), d)){
				if (d.erro!=null)
					erros.add(d.erro);
				getSntStrean().nextToken();
			}
			
			mPlaceCod.addCods(d.cod);
		}
		mPlaceCod.addCods("goto main");

		return new ResultadoAnalizeBean(mPlaceCod.cod, erros);
	}



}