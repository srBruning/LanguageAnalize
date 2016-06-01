package module.syntactic;

import module.PlaceCod;

public class AnaliseComando extends AbstractAnaliseSintatica {
	
	private AnaliseComando(SyntaticStrean strean ){
		setSntStrean(strean);
	}
	private boolean isCommand(SyntaticStrean strean, PlaceCod c){
		if( AnaliseDeclaracao.isDeclaracao(strean, c))
			return true;
		if(c.erro == null && AnaliseAtribuicao.isAtribicao(strean, c))
			return true;		

		return false;
	}

	public static boolean isListCommands(SyntaticStrean strean, PlaceCod lc){
		
		AnaliseComando instan = new AnaliseComando(strean);
		if ( instan.isCommand(strean, lc)){
			isListCommands(strean, lc);
		}
				
		return lc.erro ==null;
	}

}
