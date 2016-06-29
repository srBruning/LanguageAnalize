package module.syntactic;

import module.PlaceCod;
import module.Token.TypeToken;

public class AnaliseComando extends AbstractAnaliseSintatica {

	private AnaliseComando(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	private boolean isCommand(PlaceCod c) {

		Object typeToken = currentToken().getType();
		if (typeToken.equals(TypeToken.TK_ID)) {
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
		c.erro = formateErro("Um comando");
		return false;
	}

	public boolean comandOrListComand(PlaceCod clc) {
		// FIXME alterar
		if (toNextIfEquals(TypeToken.TK_SEMICOLON))
			return true;
		if (toNextIfEquals(TypeToken.TK_OPEN_BRAKET)){
			PlaceCod lc1 = new PlaceCod();
			lc1.lbBreak = clc.lbBreak;
			lc1.lbContinue = clc.lbBreak;			
			if (listaComando(lc1)){
				if (toNextIfEquals(TypeToken.TK_CLOSE_BRAKET)){
					clc.cod = lc1.cod;
					return true;
				}
			}else {
				clc.erro = coalesce(lc1.erro, formateErro("Esperava Lista Comandos"));
				return false;
			}			
		}
		PlaceCod lc1 = new PlaceCod();
		lc1.lbBreak = clc.lbBreak;
		lc1.lbContinue = clc.lbBreak;			
		if (isCommand(lc1)){
			if (toNextIfEquals(TypeToken.TK_SEMICOLON)){
				clc.cod = lc1.cod;
				return true;
			}
		}
		clc.erro = coalesce(lc1.erro, formateErro("Esperava comando ou Lista Comando"));
		return false;
	}
	public boolean listaComando(PlaceCod lc) {
		// FIXME alterar
		PlaceCod c = new PlaceCod();
		c.lbContinue = lc.lbContinue;
		c.lbBreak = lc.lbBreak;
		if (isCommand(c)){
			PlaceCod lc1 = new PlaceCod();
			lc1.cod = c.cod;
			if (listaComando(lc1)){
				lc.cod = lc1.cod;
				return true;
			}
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