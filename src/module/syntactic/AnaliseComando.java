package module.syntactic;

import module.PlaceCod;
import module.Token.TypeToken;

public class AnaliseComando extends AbstractAnaliseSintatica {

	private AnaliseComando(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	private boolean isCommand(PlaceCod c) {
		PlaceCod lc1 = new PlaceCod();
		lc1.lbBreak = c.lbBreak;
		lc1.lbContinue = c.lbContinue;
		lc1.address = c.address;

		if (currentIsEquals(TypeToken.TK_ID)) {
			lc1.place = currentToken().getValue();
			toNextToken();
			if (AnaliseExpressao.getInstance(getSntStrean()).isChamadaFuncao(lc1)) {

				if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
					c.cod = lc1.cod;
					return true;
				} else {
					c.erro = formateErro("Esperava um token de fim de sentença!");
					return false;
				}
			}
			if (lc1.erro != null) {
				c.erro = lc1.erro;
				return false;
			}

			if (AnaliseAtribuicao.isCmdAtribicao(getSntStrean(), lc1)) {
				c.cod = lc1.cod;
				return true;
			}
			if (lc1.erro != null) {
				c.erro = lc1.erro;
				return false;
			}
		}

		if (AnaliseDeclaracao.isDeclaracao(getSntStrean(), lc1)) {
			c.cod = lc1.cod;
			c.tipo = lc1.tipo;
			c.address = lc1.address;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (AnaliseIf.isIf(getSntStrean(), lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (AnaliseWhile.isWhile(getSntStrean(), lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (AnaliseSwitch.isSwitch(getSntStrean(), lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (AnaliseFor.isFor(getSntStrean(), lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (isCmsReturn(lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}
		if (isBreakContinue(lc1)) {
			c.cod = lc1.cod;
			return true;
		}
		if (lc1.erro != null) {
			c.erro = lc1.erro;
			return false;
		}

		// c.erro = coalesce(lc1.erro, formateErro("Esperava um Comando"));
		return false;
	}

	private boolean isCmsReturn(PlaceCod c) {

		if (toNextIfEquals(TypeToken.RETURN)) {
			PlaceCod er = new PlaceCod();
			if (!eReturn(er)) {
				c.erro = er.erro;
				return false;
			}
			;
			if (!getSntStrean().isFunction()) {
				c.erro = formateErro("Deve estar em uma funÃ§Ã£o!");
				return false;
			}

			c.addCods(er.cod);
			FuncaoBean func = getSntStrean().peekFuncao();
			if (er.returnType == null) {
				if (func.getType() != null) {
					c.erro = formateErro("Deve retornar um valor valido.");
					return false;
				}

			} else if (!er.returnType.equals(func.getType())) {
				c.erro = formateErro("Tipo de retorno incompativel");
				return false;
			}
			c.addCods(gen("=", "[_BP+" + func.getEndReturn() + "]", er.place), "goto " + func.getLbRetutn());

			return true;
		}

		return false;
	}

	private boolean eReturn(PlaceCod er) {
		PlaceCod e = new PlaceCod();
		if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
			er.cod = e.cod;
			er.returnType = e.tipo;
			er.place = e.place;
			if (!toNextIfEquals(TypeToken.TK_SEMICOLON)) {
				er.erro = formateErro("esperava um token de fim de sentenÃ§a");
				return false;
			}

		} else if (e.erro != null) {
			er.erro = e.erro;
			return false;
		}
		return true;
	}

	private boolean isBreakContinue(PlaceCod bc) {

		if (toNextIfEquals(TypeToken.BREAK)) {
			if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
				if (bc.lbBreak == null || bc.lbBreak.isEmpty()) {
					bc.erro = formateErro("Break Fora de Contexto");
					return false;
				} else {
					bc.cod = "goto " + bc.lbBreak;
					return true;
				}
			} else {
				bc.erro = formateErro("Esperava Ponto e Virgula");
				return false;
			}

		}
		if (toNextIfEquals(TypeToken.CONTINUE)) {
			if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
				if (bc.lbContinue == null || bc.lbContinue.isEmpty()) {
					bc.erro = formateErro("Continue Fora de Contexto");
					return false;
				} else {
					bc.cod = "goto " + bc.lbContinue;
					return true;
				}
			} else {
				bc.erro = formateErro("Esperava Ponto e Virgula");
				return false;
			}
		}
		return false;
	}

	public boolean comandOrListComand(PlaceCod clc) {

		if (toNextIfEquals(TypeToken.TK_OPEN_BRAKET)) {
			PlaceCod lc1 = new PlaceCod();
			lc1.lbBreak = clc.lbBreak;
			lc1.lbContinue = clc.lbContinue;
			lc1.address = clc.address;
			if (listaComando(lc1)) {
				if (toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)) {
					clc.cod = lc1.cod;
					clc.address = lc1.address;
					return true;
				}
			} else {
				clc.erro = coalesce(lc1.erro, formateErro("Esperava Lista Comandos"));
				return false;
			}
		} else {

			PlaceCod cmd = new PlaceCod();
			cmd.address = clc.address;
			cmd.returnType = clc.returnType;
			cmd.lbBreak = clc.lbBreak;
			cmd.lbContinue = clc.lbContinue;
			if (isCommand(cmd)) {
				clc.cod = cmd.cod;
				clc.address = cmd.address;
				return true;
			} else {
				if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
					return true;
				}
				clc.erro = cmd.erro;
			}

			return false;
		}
		clc.erro = formateErro("Comando ou ListaComando");
		return false;
	}

	public boolean listaComando(PlaceCod lc) {
		// FIXME alterar
		PlaceCod c = new PlaceCod();
		c.lbContinue = lc.lbContinue;
		c.lbBreak = lc.lbBreak;
		c.address = lc.address;
		if (isCommand(c)) {
			PlaceCod lc1 = new PlaceCod();
			lc1.lbContinue = c.lbContinue;
			lc1.lbBreak = c.lbBreak;
			lc1.address= c.address;
			// if (lc.cod != null) lc1.cod = lc.cod + c.cod; else lc1.cod =
			// c.cod;
			if (listaComando(lc1)) {
				lc.addCods( c.cod, lc1.cod);
				lc.address = lc1.address;
				return true;
			} else if (lc1.erro != null) {
				lc.erro = lc1.erro;
				return false;
			}
		}
		if (c.erro != null) {
			lc.erro = c.erro;
			return false;
		}
		lc.address = c.address;
		lc.addCods(c.cod);
		return true;
	}

	private static AnaliseComando instan;

	public static AnaliseComando getInstancia(SyntaticStrean strean) {
		if (instan == null) {
			instan = new AnaliseComando(strean);
		} else {
			instan.setSntStrean(strean);
		}
		return instan;
	}

	public static boolean isCommands(SyntaticStrean strean, PlaceCod lc) {

		// PlaceCod lc1 = new PlaceCod();

		return getInstancia(strean).isCommand(lc);

		// if ( getInstancia(strean).isCommand(strean, lc1)){
		// isListCommands(strean, lc1);
		// }
		// lc.addCods( lc1.cod, lc.cod);
		// lc.erro = lc1.erro;
		// return lc.erro ==null;
	}

}