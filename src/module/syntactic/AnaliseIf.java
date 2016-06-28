package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseIf extends AbstractAnaliseSintatica {
	private AnaliseIf(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static boolean isIf(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseIf(strean).isIf(ePlaceCod);
	}

	private boolean isIf(PlaceCod a) {
		if (toNextIfEquals(TypeToken.IF)) {

			if (toNextIfEquals(TypeToken.TK_OPENPARENTHESIS)) {

				PlaceCod e = new PlaceCod();
				if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
					if (toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS)) {
						// criar labels
						// comando ou lista comando
						PlaceCod clc = new PlaceCod();
						clc.lbContinue = a.lbContinue;
						clc.lbBreak = a.lbBreak;
						PlaceCod pcElse = new PlaceCod();
						if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
							if (isElse(pcElse)) {
								
							}

							a.erro = coalesce(clc.erro, formateErro("Esperava um comando"));
						}
					} else {
						a.erro = formateErro("Esperava fecha parentesis depois do If");
						return false;
					}
				} else {
					a.erro = coalesce(e.erro, formateErro("Esperava uma Expreção"));
				}
			} else {
				a.erro = formateErro("Esperava abre parentesis depois do If");
				return false;
			}
			return false;
		}
		return false;
	}


	private boolean isElse(PlaceCod a) {
		// criar labels
		if (currentIsEquals(TypeToken.ELSE)) {
			// comando ou lista de comando
		}
		return false;
	}

}
