package module.syntactic;

import module.PlaceCod;
import module.Token.TypeToken;

public class AnaliseDeclaracao extends AbstractAnaliseSintatica{	
	private static AnaliseDeclaracao instance;

	private AnaliseDeclaracao(SyntaticStrean strean){
		setSntStrean(strean);
	}

	public static AnaliseDeclaracao getInstance(SyntaticStrean strean){
		if (instance == null)
			instance = new AnaliseDeclaracao(strean);
		else instance.setSntStrean(strean);

		return instance;
	}

	public static boolean isDeclaracao(SyntaticStrean strean, PlaceCod d){
		AnaliseDeclaracao anlAssing = getInstance(strean);
		boolean r =  anlAssing.declaracao(d);
		if (d.erro!=null)
			anlAssing.addErro(d.erro);
		return r;
	}

	private boolean declaracao(PlaceCod d){
		PlaceCod pcType = new PlaceCod();
		if (type(pcType)){
			PlaceCod d2 = new PlaceCod();
			d2.tipo = pcType.tipo;
			if( declaracao2(d2)){
				d.cod = d2.cod;
				d.place = d2.place;
				d.tipo = d2.tipo;
				d.erro = d2.erro;
				return true;
			}
			d.erro = coalesce(d2.erro, "Esperava uma declaração!.");
		}

		return false;
	}

	private boolean dec_funcao(PlaceCod df){
		return false;
	}

	//	private boolean  declaracao1(PlaceCod d1) {
	//
	//		PlaceCod df = new PlaceCod();
	//		if (dec_funcao(df )){
	//			d1.cod = df.cod;
	//			d1.place = df.place;
	//			d1.tipo = df.tipo;
	//			return true;
	//		}
	//		
	//		if (declaracao2(df )){
	//			d1.cod = df.cod;
	//			d1.place = df.place;
	//			d1.tipo = df.tipo;
	//			return true;
	//		}
	//		
	//		return false;
	//	}

	private boolean declaracao2(PlaceCod d2) {

		if (currentIsEquals(TypeToken.TK_ID)){
			if( getSntStrean().findSimbolById(currentToken().getValue())!=null ){
				d2.erro = "Variavel ja existe!";
				return false;
			}
			getSntStrean().addTabSimbulos(currentToken().getValue(), d2.tipo);
			PlaceCod d3 = new PlaceCod(d2);
			d3.place = currentToken().getValue(); 
			toNextToken();
			if (declaracao3(d3)){
				PlaceCod d4= new PlaceCod(d3);
				if (declaracao4(d4)){
					d2.cod = d4.cod;
					if (currentIsEquals(TypeToken.TK_SEMICOLON)){
						toNextToken();
						return true;
					}
					d2.erro = "Esperava ';'";
				}else{
					d2.erro = d4.erro;
				}
			}else{
				d2.erro = d3.erro;
			}
			if (d2.erro == null)
				d2.erro ="Esperava uma declaração";

		}
		return false;
	}

	private boolean declaracao3(PlaceCod d3) {

		if ( toNextIfEquals(TypeToken.TK_ASSINGMENT)){
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)){
				if (!d3.tipo.equals(e.tipo)){
					d3.erro = coalesce(d3.erro, "Esta tentando colocar um "+e.tipo+ "em um "+d3.tipo);
					return false;
				}
				PlaceCod d4 = new PlaceCod();
				d4.tipo = d3.tipo;

				d4.addCods(d3.cod, e.cod, gen("=", d3.place, e.place));

				if (declaracao4(d4)){
					d3.cod = d4.cod;
					d3.place = d4.place;
					d3.erro = d4.erro;
					return true;
				}else{
					d3.erro = d4.erro;
				}

			}
			return false;
		}
		return true;
	}

	private boolean declaracao4(PlaceCod d4) {
		if (toNextIfEquals(TypeToken.TK_COMMA)){
			PlaceCod d2 = new PlaceCod(d4);
			if(declaracao2(d2)){
				d4.tipo = d2.tipo;
				return true;
			}
			d4.erro = coalesce(d2.erro, "Esperava um valor para atribuição!");
			return false;
		}

		return true;
	}
}
