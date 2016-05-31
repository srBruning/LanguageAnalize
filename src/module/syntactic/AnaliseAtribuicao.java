package module.syntactic;

import module.PlaceCod;
import module.Token.TypeToken;

public class AnaliseAtribuicao extends AbstractAnaliseSintatica {
	private AnaliseAtribuicao(SyntaticStrean strean){
		setSntStrean(strean);
	}

	public static boolean isAtribicao(SyntaticStrean strean, PlaceCod ePlaceCod ){
		return new AnaliseAtribuicao(strean).atribuicao(ePlaceCod);
	}

	private boolean atribuicao(PlaceCod ePlaceCod) {
		if (toNextIfEquals(TypeToken.TK_ID)){
			if( getSntStrean().findSimbolById(currentToken().getValue())!=null ){
				ePlaceCod.erro = "Variavel ja existe!";
				return false;
			}
			String op = operador();
			if ( op!=null){
				
			}
		}
		return false;
	}

	private String operador() {
		
		switch (currentToken().getType()) {
		case TK_ADDASSIGNMENT:
		case TK_SUBASSIGNMENT:
		case TK_DIVASSIGNMENT:
		case TK_MULTPASSIGNMENT:
		case TK_ASSINGMENT:
			return currentToken().getValue();
		}
		
		return null;
	}

	
}
