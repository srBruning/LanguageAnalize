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
		c.erro = formateErro("Um comando");
		return false;
	}

	public boolean comandOrListComand(PlaceCod clc) {
		// FIXME alterar
		if (toNextIfEquals(TypeToken.TK_SEMICOLON))
			return true;
		return false;
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
