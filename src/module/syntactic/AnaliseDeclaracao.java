package module.syntactic;

import module.ParametrosBean;
import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseDeclaracao extends AbstractAnaliseSintatica {
	private static AnaliseDeclaracao instance;

	private AnaliseDeclaracao(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static AnaliseDeclaracao getInstance(SyntaticStrean strean) {
		if (instance == null)
			instance = new AnaliseDeclaracao(strean);
		else
			instance.setSntStrean(strean);

		return instance;
	}

	public static boolean isDeclaracao(SyntaticStrean strean, PlaceCod d) {
		AnaliseDeclaracao anlAssing = getInstance(strean);
		boolean r = anlAssing.declaracao(d);

		return r;
	}

	public boolean corpoFuncao(PlaceCod cf) {
		// FIXME implementar
		getSntStrean().pushTabSimbulo();
		try {

			PlaceCod lp = new PlaceCod();
			lp.address = 8;
			FuncaoBean funcBean = new FuncaoBean();
			funcBean.setName(cf.place);
			funcBean.setType(cf.returnType);
			funcBean.setLbRetutn(creatLabel());
			getSntStrean().pushFuncao(funcBean);

			if (toNextIfEquals(TypeToken.TK_OPENPARENTHESIS)) {
				PlaceCod clc = new PlaceCod();
				clc.address = 0;
				if (listParameters(lp) && toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS)) {
					funcBean.setEndReturn(lp.address+4);
					if (getSntStrean().findFuncao(cf.place) != null) {
						cf.erro = formateErro("Metodo " + cf.place + "duplicado!");
						return false;
					}
					getSntStrean().addTabFunc(funcBean);
					if (AnaliseComando.getInstancia(getSntStrean()).comandOrListComand(clc)) {
						String labelFim = creatLabel();
						cf.addCods("goto " + labelFim, funcBean.getName() + ":");
						cf.addCods("push _Bp ");
						cf.addCods(gen("=", "_Bp", "_Sp"), gen("+", "_Sp", "_Sp", clc.address.toString()));
						for (ParametrosBean p : funcBean.getParametros()) {
							cf.addCods(gen("=", p.varTemp, "[_Bp +" + p.addres + "]"));
						}
						cf.addCods(clc.cod, funcBean.getLbRetutn()+":", gen("-", "_Sp", "_Sp", clc.address.toString())  );
						cf.addCods("pop _Bp", "return");
						cf.addCods(labelFim+":");
						return true;
					}else{
						cf.erro = coalesce(clc.erro, formateErro("Esperava um comando!"));
					}
				} else {
					cf.erro = formateErro("esperava um token fecha parentes");
				}
			}

			return false;
		} finally {
			getSntStrean().popFuncao();
			getSntStrean().popTabSimbulo();
		}
	}

	private boolean listParameters(PlaceCod lp) {
		PlaceCod t = new PlaceCod();

		if (type(t)) {
			Token crt = currentToken();
			if (toNextIfEquals(TypeToken.TK_ID)) {
				if (getSntStrean().peekFuncao().findParam(crt.getValue()) != null) {
					lp.erro = formateErro("Ja ha um parametro com esse nome");
					return false;
				}
				ParametrosBean bean = new ParametrosBean(crt.getValue(), t.tipo, lp.address, criaTemp());
				getSntStrean().peekFuncao().getParametros().add(bean);
				getSntStrean().addTabSimbulos(crt.getValue(), t.tipo, lp.address);
				lp.address += 4;
			} else {
				lp.erro = formateErro("Esperava um identificador deposide um tipo");
				return false;
			}
		}
		return true;
	}

	private boolean declaracao(PlaceCod d) {

		PlaceCod t = new PlaceCod();
		if (type(t)) {
			Token ct = currentToken();
			if (toNextIfEquals(TypeToken.TK_ID)) {
				PlaceCod d2 = new PlaceCod();
				d2.tipo = t.tipo;
				d2.place = ct.getValue();
				d2.address = d.address;
				if (declaracao2(d2)) {
					d.cod = d2.cod;
					return true;
				} else {
					d.erro = d2.erro;
					return false;
				}
			} else {
				d.erro = formateErro("Esperava um identificador depois de um tipo!");
				return false;
			}

		} else if (toNextIfEquals(TypeToken.VOID)) {
			Token ct = currentToken();
			if (toNextIfEquals(TypeToken.TK_ID)) {
				PlaceCod cf = new PlaceCod();
				cf.returnType = null;
				cf.place = ct.getValue();
				if (corpoFuncao(cf)) {
					d.cod = cf.cod;
				} else {
					d.erro = coalesce(cf.erro, formateErro("Esperava a declaração de uma função "));
					return false;
				}
			} else {
				d.erro = formateErro("esperava um identificador apos um token void!");
				return false;
			}
		}
		return false;
	}

	private boolean declaracao2(PlaceCod d2) {
		PlaceCod cf = new PlaceCod();
		cf.returnType = d2.tipo;
		cf.place = d2.place;
		if (corpoFuncao(cf)) {
			d2.addCods(d2.cod , cf.cod);
			d2.address = cf.address;
			return true;
		} else {
			
			if ( cf.erro !=null){
				d2.erro = cf.erro;
				return false;			}
			
			if (getSntStrean().findSimbolById(d2.place) != null) {
				d2.erro = formateErro("Já há uma varivel com o nome " + currentToken().getValue());
				return false;
			}
			getSntStrean().addTabSimbulos(d2.place, d2.tipo, d2.address);
			if (d2.address != null)
				d2.address += 4;

			PlaceCod d3 = new PlaceCod(d2);
			d3.place = d2.place;
			if (declaracao3(d3)) {
				d2.cod = d3.cod;
				d2.address = d3.address;
				return true;
			}
			d2.erro = d3.erro;
		}
		return false;
	}

	private boolean declaracao3(PlaceCod d3) {

		if (toNextIfEquals(TypeToken.TK_ASSINGMENT)) {
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
				if (!d3.tipo.equals(e.tipo)) {
					d3.erro = coalesce(d3.erro, formateErro("Esta tentando colocar um " + e.tipo + "em um " + d3.tipo));
					return false;
				}
				PlaceCod d4 = new PlaceCod(d3);
				if (declaracao4(d4)) {
					d3.setCods(d3.cod, e.cod, gen("=", d3.place, e.place), d4.cod);
					d3.address = d4.address;
					return true;

				} else {
					d3.erro = d4.erro;
				}
			} else {
				d3.erro = coalesce(e.erro, formateErro("Esperava um valor para a atribuição"));
				return false;
			}

		} else {
			PlaceCod d4 = new PlaceCod(d3);
			if (declaracao4(d4)) {
				d3.cod = d4.cod;
				d3.address = d4.address;
				return true;

			}
			d3.erro = d4.erro;
		}
		return false;
	}

	private boolean declaracao4(PlaceCod d4) {
		if (toNextIfEquals(TypeToken.TK_COMMA)) {
			PlaceCod d5 = new PlaceCod(d4);
			if (declaracao5(d5)) {
				d4.tipo = d5.tipo;
				d4.cod = d5.cod;
				d4.address = d5.address;

				return true;
			}
			d4.erro = coalesce(d5.erro, formateErro("Esperava uma declaração!"));
			return false;
		}
		if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
			return true;
		}
		d4.erro = formateErro("Esperava token de Fim de Sentença!");
		return false;
	}

	private boolean declaracao5(PlaceCod d5) {
		if (currentIsEquals(TypeToken.TK_ID)) {
			if (getSntStrean().findSimbolLocalTableById(currentToken().getValue()) != null) {
				d5.erro = formateErro("Já há uma varivel com o nome " + currentToken().getValue());
				return false;
			}

			getSntStrean().addTabSimbulos(d5.place, d5.tipo, d5.address);
			if (d5.address != null) {
				d5.address += 4;
			}
			PlaceCod d3 = new PlaceCod(d5);

			if (declaracao3(d3)) {
				d5.cod = d3.cod;
				d5.address = d3.address;
				return true;
			} else {
				d5.erro = d3.erro;
			}
		}
		return false;
	}
}
