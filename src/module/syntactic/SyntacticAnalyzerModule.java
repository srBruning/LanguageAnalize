package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import module.CausaErro;
import module.PlaceCod;
import module.ResultadoAnalizeBean;

public class SyntacticAnalyzerModule extends AbstractAnaliseSintatica {

	private List<CausaErro> erros;

	public ResultadoAnalizeBean analyzer(SyntaticStrean sntStrean) {

		erros = new ArrayList<>();

		setSntStrean(sntStrean);

		if (!sntStrean.nextToken()) {
			erros.add(new CausaErro("esta em branco ", 0, 0, 0));
			return new ResultadoAnalizeBean(null, erros);
		}
		getVariables().clear();
		clear();

		PlaceCod mPlaceCod = new PlaceCod();

		while (getSntStrean().hasNextToken()) {
			PlaceCod d = new PlaceCod();

			if (!AnaliseDeclaracao.isDeclaracao(getSntStrean(), d)) {
				if (d.erro != null) {
					erros.add(d.erro);
					getSntStrean().nextToken();

				} else {
					if (!AnaliseComando.isCommands(getSntStrean(), d)) {
						if (d.erro != null) {
							erros.add(d.erro);
							getSntStrean().nextToken();

						} else {
							erros.add(formateErro("Esperava uma declaração ou u comando."));
							return new ResultadoAnalizeBean(mPlaceCod.cod, erros);
						}
					}
				}
			}

			mPlaceCod.addCods(d.cod);
		}
//		mPlaceCod.addCods(" call main");
		String cod = mPlaceCod.cod;
		if (cod != null) {
			while (cod.indexOf("\n\n") > 0)
				cod = cod.replaceAll("\n\n", "\n");

			cod = cod.replaceAll(LINHA_SEPARADORA, "\n");
		}

		return new ResultadoAnalizeBean(cod, erros);
	}

}