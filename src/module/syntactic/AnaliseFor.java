package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseFor extends AbstractAnaliseSintatica {
	private AnaliseFor(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static boolean isFor(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseFor(strean).isFor(ePlaceCod);
	}

	private boolean isFor(PlaceCod whi) {
		if (toNextIfEquals(TypeToken.FOR)) {
			if (toNextIfEquals(TypeToken.TK_OPENPARENTHESIS)) {
				PlaceCod forp1 = new PlaceCod();
				if (isForP1(forp1)) {
					PlaceCod forp2 = new PlaceCod();
					if (isForP2(forp2)) {
						if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
							PlaceCod forp3 = new PlaceCod();
							if (isForP3(forp3)) {
								if (toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS)) {
									PlaceCod clc = new PlaceCod();
									String lbInicio = creatLabel();
									String lbFim = creatLabel();
									clc.lbContinue = lbInicio;
									clc.lbBreak = lbFim;
									if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
										whi.addCods(forp1.cod, lbInicio + ":", forp2.cod,
												"if " + forp2.place + "==" + "0" + " goto " + lbFim, clc.cod,
												forp3.cod + "goto" + lbInicio, lbFim + ":");
										return true;
									} else {
										whi.erro = coalesce(clc.erro,
												formateErro("Esperava um comando ou Lista Comandos"));
									}
								} else {
									whi.erro = formateErro("Esperava fecha parentesis depois do For");
									return false;
								}
							} else {
								whi.erro = coalesce(forp3.erro, formateErro("Esperava 3 argumento For"));
								return false;
							}
						} else {
							whi.erro = formateErro("Esperava ;");
							return false;
						}
					} else {
						whi.erro = coalesce(forp2.erro, formateErro("Esperava 2 argumento For"));
						return false;
					}

				} else {
					whi.erro = coalesce(forp1.erro, formateErro("Esperava 1 argumento For"));
					return false;
				}
			} else {
				whi.erro = formateErro("Esperava abre parentesis depois do for");
				return false;
			}
		}
		return false;
	}

	private boolean isForP1(PlaceCod forp1) {
		PlaceCod d = new PlaceCod();
		if (AnaliseDeclaracao.isDeclaracao(getSntStrean(), d)) {
			forp1.cod = d.cod;
			return true;
		}

		if (d.erro != null) {
			forp1.erro = d.erro;
			return false;
		}

		PlaceCod a = new PlaceCod();
		a.place = currentToken().getValue();
		if (toNextIfEquals(TypeToken.TK_ID)) {
			if (AnaliseAtribuicao.isCmdAtribicao(getSntStrean(), a)) {
				forp1.cod = a.cod;
				return true;
			}
			forp1.erro  =  a.erro;
		}
		return true;
	}

	private boolean isForP2(PlaceCod forp2) {
		PlaceCod e = new PlaceCod();
		if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
			forp2.cod = e.cod;
			forp2.place = e.place;
			return true;
		}
		return true;
	}

	private boolean isForP3(PlaceCod forp3) {
		PlaceCod a = new PlaceCod();
		
		if (AnaliseAtribuicao.isAtribicao1(getSntStrean(), a)) {
			forp3.cod = a.cod;
		}
		if (a.erro != null) {
			forp3.erro = a.erro;
			return false;
		}
		return true;
	}

}