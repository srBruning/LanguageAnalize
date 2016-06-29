package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseWhile extends AbstractAnaliseSintatica {
	private AnaliseWhile(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static boolean isWhile(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseWhile(strean).isWhile(ePlaceCod);
	}

	private boolean isWhile(PlaceCod whi) {
		if (toNextIfEquals(TypeToken.WHILE)) {
			if (toNextIfEquals(TypeToken.TK_OPENPARENTHESIS)) {
				PlaceCod e = new PlaceCod();
				if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
					if (toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS)) {
						PlaceCod clc = new PlaceCod();
						String lbInicio = creatLabel();
						String lbFim = creatLabel();
						clc.lbContinue = lbInicio;
						clc.lbBreak = lbFim;
						if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
							whi.cod = lbInicio + ":" + e.cod + "if" + e.place + "==" + "0" + "goto" + lbFim +
						        clc.cod + lbFim + ":";
							return true;
						}else{
							whi.erro = coalesce(clc.erro, formateErro("Esperava um comando"));
						}
					} else {
						whi.erro = formateErro("Esperava fecha parentesis depois do While");
						return false;
					}
				} else {
					whi.erro = coalesce(e.erro, formateErro("Esperava uma Expressão"));
				}
			} else {
				whi.erro = formateErro("Esperava abre parentesis depois do While");
				return false;
			}
			return false;
		}
		return false;
	}


}