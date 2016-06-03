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
	
	private static AnaliseComando instan;
	private static AnaliseComando getInstancia(SyntaticStrean strean){
		if (instan == null){
			instan =  new AnaliseComando(strean);
		}else{
			instan.setSntStrean(strean);
		}
		return instan;
	}
	
	public static boolean isCommands(SyntaticStrean strean, PlaceCod lc){
		
//		PlaceCod lc1 = new PlaceCod();
		
		return getInstancia(strean).isCommand(strean, lc);
		
//		if ( getInstancia(strean).isCommand(strean, lc1)){
//			isListCommands(strean, lc1);
//		}
//			lc.addCods( lc1.cod, lc.cod);	
//			lc.erro = lc1.erro;
//		return lc.erro ==null;
	}

}
