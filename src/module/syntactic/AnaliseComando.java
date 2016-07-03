package module.syntactic;

import module.PlaceCod;
import module.Token.TypeToken;

public class AnaliseComando extends AbstractAnaliseSintatica {

	private AnaliseComando(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	private boolean isCommand(PlaceCod c) {

		if ( currentToken() == null)
			return false;
		
		Object typeToken = currentToken().getType();
		if (toNextIfEquals(TypeToken.TK_ID)) {
			PlaceCod cf =new PlaceCod();
			if (! AnaliseExpressao.getInstance(getSntStrean()).isChamadaFuncao(cf) &&  cf.erro !=null){
				c.erro = cf.erro;
				return false;
			}
			return AnaliseAtribuicao.isAtribicao(getSntStrean(), c);
		}
		if (currentToken().isType()) {
			return AnaliseDeclaracao.isDeclaracao(getSntStrean(), c);
		}
		if (typeToken.equals(TypeToken.IF)) {
			return AnaliseIf.isIf(getSntStrean(), c);
		}
		if (typeToken.equals(TypeToken.WHILE)) {
			return AnaliseWhile.isWhile(getSntStrean(), c);
		}

		if (typeToken.equals(TypeToken.RETURN)) {
			return isCmsReturn(c);
		}

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
				c.erro = formateErro("Deve estar em uma função!");
				return false;
			}

			FuncaoBean func = getSntStrean().peekFuncao();
			if (er.returnType == null) {
				if (func.getType() != null) {
					c.erro = formateErro("Deve retornar um valor valido.");
					return false;
				}

				c.addCods(gen("=", "[_Bp+" + func.getEndReturn() + "]", er.place), "goto " + func.getLbRetutn());
			} else if (!er.returnType.equals(func.getType())) {
				c.erro = formateErro("Tipo de retorno incompativel");
				return false;
			}
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
				er.erro = formateErro("esperava um token de fim de sentença");
				return false;
			}
		}
		if( e.erro !=null){
			er.erro  = e.erro;
			return false;
		}
		return true;
	}

	public boolean comandOrListComand(PlaceCod clc) {
	
		if (toNextIfEquals(TypeToken.TK_OPEN_BRAKET)) {
			PlaceCod lc1 = new PlaceCod();
			lc1.lbBreak = clc.lbBreak;
			lc1.lbContinue = clc.lbBreak;
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
		}
		
		PlaceCod cmd =new PlaceCod();
		cmd.address = clc.address;
		cmd.returnType = clc.returnType;
		if ( isCommand(cmd)){
			clc.cod = cmd.cod;
			clc.address = cmd.address;
			return true;
		}else{
			clc.erro = cmd.erro;
		}
		
		return false;
	}

	public boolean listaComando(PlaceCod lc) {
		// FIXME alterar
		PlaceCod c = new PlaceCod();
		c.lbContinue = lc.lbContinue;
		c.lbBreak = lc.lbBreak;
		if (isCommand(c)) {
			PlaceCod lc1 = new PlaceCod();
			lc1.cod = c.cod;
			if (listaComando(lc1)) {
				lc.cod = lc1.cod;
				return true;
			}
		}
		
		if(c.erro!=null){
			lc.erro = c.erro;
			return false;
		}
		
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