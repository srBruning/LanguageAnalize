package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseSwitch extends AbstractAnaliseSintatica {
	private AnaliseSwitch(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static boolean isSwitch(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseSwitch(strean).isSwitch(ePlaceCod);
	}

	private boolean isSwitch(PlaceCod swi) {
		if (toNextIfEquals(TypeToken.SWITCH)) {
			if (toNextIfEquals(TypeToken.TK_OPENPARENTHESIS)) {
				PlaceCod e = new PlaceCod();
				if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
					if (toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS)) {
						PlaceCod clc = new PlaceCod();
						String lbInicio = creatLabel();
						String lbFim = creatLabel();
						clc.lbBreak = lbFim;
						clc.lbInicio = lbInicio;
						if (cmdSwitch(clc)) {
							swi.addCods(e.cod, " goto " + lbInicio, clc.cod, lbFim + ":");
							return true;
						} else {
							swi.erro = coalesce(clc.erro, formateErro("Esperava um comando"));
						}
					} else {
						swi.erro = formateErro("Esperava fecha parentesis depois do Switch");
						return false;
					}
				} else {
					swi.erro = coalesce(e.erro, formateErro("Esperava uma ExpressÃ£o"));
				}
			} else {
				swi.erro = formateErro("Esperava abre parentesis depois do Switch");
				return false;
			}
		}
		return false;

	}

	public boolean cmdSwitch(PlaceCod clc) {

		if (toNextIfEquals(TypeToken.TK_OPEN_BRAKET)) {
			PlaceCod lc1 = new PlaceCod();
			lc1.lbBreak = clc.lbBreak;
			lc1.lbInicio = clc.lbInicio;
			lc1.address = clc.address;
			// lc1.testeCod = clc.lbInicio + ":\n";
			if (lcsd(lc1)) {
				if (toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)) {

					clc.addCods(lc1.cod, "goto " + lc1.lbBreak, lc1.lbInicio + ":", lc1.testeCod);
					// clc.addCods(lc1.cod);
					return true;
				} else {
					clc.erro = coalesce(lc1.erro, formateErro("Esperava Fecha Chaves"));
					return false;
				}
			} else {
				clc.erro = coalesce(lc1.erro, formateErro("Lista de Cases"));
				return false;
			}
		}

		if (toNextIfEquals(TypeToken.CASE)) {
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
				if (toNextIfEquals(TypeToken.TK_COLON)) {
					PlaceCod lc1 = new PlaceCod();
					lc1.lbBreak = clc.lbBreak;
					if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(lc1)) {
						String label = creatLabel();
						clc.addCods(label + ":", lc1.cod, "goto " + clc.lbBreak, clc.lbInicio + ':', e.cod,
								"if " + e.place + "==" + "1" + " goto " + label);
						return true;
					} else {
						clc.erro = coalesce(lc1.erro, formateErro("Esperava Comando ou Lista de Comando"));
						return false;
					}
				} else {
					clc.erro = formateErro("Esperava Dois Pontos");
					return false;
				}
			} else {
				clc.erro = coalesce(e.erro, formateErro("Esperava Expressão"));
				return false;
			}
		}
		if (toNextIfEquals(TypeToken.DEFAULT)) {
			if (toNextIfEquals(TypeToken.TK_COLON)) {
				PlaceCod lc1 = new PlaceCod();
				lc1.lbBreak = clc.lbBreak;
				if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(lc1)) {
					clc.addCods(clc.lbInicio + ":", lc1.cod);
					return true;
				} else {
					clc.erro = coalesce(lc1.erro, formateErro("Esperava Comando ou Lista de Comando"));
					return false;
				}
			} else {
				clc.erro = formateErro("Esperava Dois Pontos");
				return false;
			}
		}
		if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
			return true;
		}
		clc.erro = formateErro("Esperava Case, Default ou Abre Chaves");
		return false;
	}

	public boolean lcsd(PlaceCod lcsd) {

		if (toNextIfEquals(TypeToken.CASE)) {
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
				if (toNextIfEquals(TypeToken.TK_COLON)) {
					PlaceCod lc1 = new PlaceCod();
					lc1.lbBreak = lcsd.lbBreak;
					lc1.address = lcsd.address;
					if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(lc1)) {
						String label = creatLabel();
						PlaceCod lcsdX = new PlaceCod();
						lcsdX.lbBreak = lcsd.lbBreak;
						lcsdX.address = lcsd.address;
						lcsdX.addCods(lcsd.cod, label + ": ", lc1.cod);
						if( lcsd.testeCod ==null)
							lcsd.testeCod = ""; 
						if(  e.cod ==null)
							 e.cod = ""; 
						lcsdX.testeCod = lcsd.testeCod + e.cod + "\nif " + e.place + "==" + "1" + " goto " + label
								+ "\n";
						if (lcsd(lcsdX)) {
							lcsd.cod = lcsdX.cod;// + "\n" + "goto "+
													// lcsd.lbBreak+"\n"
													// +lcsd.lbInicio + ":\n" +
													// lcsdX.testeCod;
							lcsd.testeCod = lcsdX.testeCod;
							lcsd.address = lcsdX.address;
							return true;
						} else {
							lcsd.erro = coalesce(lcsdX.erro, formateErro("Esperava Lista Cases"));
							return false;
						}
					} else {
						lcsd.erro = coalesce(lc1.erro, formateErro("Esperava Comando ou Lista de Comando"));
						return false;
					}
				} else {
					lcsd.erro = formateErro("Esperava Dois Pontos");
					return false;
				}
			} else {
				lcsd.erro = coalesce(e.erro, formateErro("Esperava Expressão"));
				return false;
			}
		}
		if (toNextIfEquals(TypeToken.DEFAULT)) {
			if (toNextIfEquals(TypeToken.TK_COLON)) {
				PlaceCod lc1 = new PlaceCod();
				lc1.lbBreak = lcsd.lbBreak;
				if (AnaliseComando.getInstancia(getSntStrean()).listaComando(lc1)
						|| AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(lc1)) {
					String label = creatLabel();
					PlaceCod lcsdX = new PlaceCod();
					lcsdX.lbBreak = lcsd.lbBreak;
					lcsdX.addCods(lcsd.cod , label + ": " , lc1.cod ,"\n");
					if( lcsd.testeCod==null)
						lcsd.testeCod ="";
					lcsdX.testeCod = lcsd.testeCod + "\n goto " + label + "\n";
					if (lcsdX(lcsdX)) {
						lcsd.addCods(lcsdX.cod); // + "\n" + "goto "+
												// lcsd.lbBreak+"\n"
												// +lcsd.lbInicio + ":\n" +
												// lcsdX.testeCod;
						lcsd.testeCod = lcsdX.testeCod;
						return true;
					} else {
						lcsd.erro = coalesce(lcsdX.erro, formateErro("Esperava Lista Cases"));
						return false;
					}
				} else {
					lcsd.erro = coalesce(lc1.erro, formateErro("Esperava Comando ou Lista de Comando"));
					return false;
				}
			} else {
				lcsd.erro = formateErro("Esperava Dois Pontos");
				return false;
			}
		}
		// lcsd.erro = formateErro("Esperava Case, Default ou Abre Chaves");
		return true;
	}

	public boolean lcsdX(PlaceCod lcsd) {

		if (toNextIfEquals(TypeToken.CASE)) {
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
				if (toNextIfEquals(TypeToken.TK_COLON)) {
					PlaceCod lc1 = new PlaceCod();
					lc1.lbBreak = lcsd.lbBreak;
					if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(lc1)) {
						String label = creatLabel();
						PlaceCod lcsdX = new PlaceCod();
						lcsdX.lbBreak = lcsd.lbBreak;
						lcsdX.cod = lcsd.cod + "\n" + label + ": " + lc1.cod + "\n";
						lcsdX.testeCod = lcsd.testeCod + e.cod + "\nif " + e.place + "==" + "1" + " goto " + label
								+ "\n";
						if (lcsdX(lcsdX)) {
							lcsd.cod = lcsdX.cod;// + "\n" + "goto "+
													// lcsd.lbBreak+"\n"
													// +lcsd.lbInicio + ":\n" +
													// lcsdX.testeCod;
							lcsd.testeCod = lcsdX.testeCod;
							return true;
						} else {
							lcsd.erro = coalesce(lcsdX.erro, formateErro("Esperava Lista Cases"));
							return false;
						}
					} else {
						lcsd.erro = coalesce(lc1.erro, formateErro("Esperava Comando ou Lista de Comando"));
						return false;
					}
				} else {
					lcsd.erro = formateErro("Esperava Dois Pontos");
					return false;
				}
			} else {
				lcsd.erro = coalesce(e.erro, formateErro("Esperava Expressão"));
				return false;
			}
		}
		// lcsd.erro = formateErro("Esperava Case, Default ou Abre Chaves");
		return true;
	}
}