package module.syntactic;

import module.PlaceCod;
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
						PlaceCod clc = new PlaceCod();
						clc.lbContinue = a.lbContinue;  
						clc.lbBreak = a.lbBreak;
						PlaceCod pcElse = new PlaceCod();
						if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
							if (isElse(pcElse)) {
								if (pcElse.cod == null || pcElse.cod.isEmpty()){
									String lbFinal = creatLabel();
									a.addCods( e.cod , genGotoCond(e.place, "==", "0", lbFinal) ,
										      clc.cod ,lbFinal + ":");
								} else{
									String lbElse = creatLabel();
									String lbFinal = creatLabel();
									
									a.addCods( e.cod + genGotoCond(e.place, "==", "0", lbElse) ,
							        clc.cod , " goto " + lbFinal , lbElse + ":" , pcElse.cod , lbFinal + ":");									
								}
								return true;
							}
							a.erro = coalesce(clc.erro, formateErro("Esperava um comando"));
							//a.erro = clc.erro;
							return false;
						}
					} else {
						a.erro = formateErro("Esperava fecha parentesis depois do If");
						return false;
					}
				} else {
					a.erro = coalesce(e.erro, formateErro("Esperava uma ExpreÃ§Ã£o"));
					//a.erro = e.erro;
					return false;
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
		if (toNextIfEquals(TypeToken.ELSE)) {
			PlaceCod clc = new PlaceCod();
			clc.lbContinue = a.lbContinue;
			clc.lbBreak = a.lbBreak;
			if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
				a.cod = clc.cod;
				return true;
			}else{
				a.erro = coalesce(clc.erro, formateErro("Esperava um comando"));
				return false;
			}
		}
		
		return true;
	}

}