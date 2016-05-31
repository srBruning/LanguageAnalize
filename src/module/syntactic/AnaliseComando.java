package module.syntactic;

import module.PlaceCod;

public class AnaliseComando extends AbstractAnaliseSintatica {
	
	private AnaliseComando(SyntaticStrean strean ){
		setSntStrean(strean);
	}
	public static boolean isCommand(SyntaticStrean strean, PlaceCod c){
		return AnaliseDeclaracao.isDeclaracao(strean, c);
	}

	public static boolean isListCommands(SyntaticStrean strean, PlaceCod lc){
		if ( isCommand(strean, lc)){
			isListCommands(strean, lc);
		}
		return true;
	}

}
