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

		return r;
	}

	private boolean declaracao(PlaceCod d){
		PlaceCod pcType = new PlaceCod();
		if (type(pcType)){
			PlaceCod d2 = new PlaceCod();
			d2.tipo = pcType.tipo;
			if( declaracao2(d2)){
				d.cod = d2.cod;
//				d.place = d2.place;
				d.tipo = d2.tipo;
				d.erro = d2.erro;
				return true;
			}
			d.erro = coalesce(d2.erro, formateErro("Esperava uma declaração!."));
		}

		return false;
	}

	private boolean dec_funcao(PlaceCod df){
		return false;
	}

	private boolean declaracao2(PlaceCod d2) {

		if (currentIsEquals(TypeToken.TK_ID)){
			if( getSntStrean().findSimbolById(currentToken().getValue())!=null ){
				d2.erro = formateErro("Variavel ja existe!");
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
					return true;
				}else{
					d2.erro = d4.erro;
				}
			}else{
				d2.erro = d3.erro;
			}
			if (d2.erro == null)
				d2.erro =formateErro("Esperava uma declaração");

		}
		return false;
	}

	private boolean declaracao3(PlaceCod d3) {

		if ( toNextIfEquals(TypeToken.TK_ASSINGMENT)){
			PlaceCod e = new PlaceCod();
			if (AnaliseExpressao.isExpressao(getSntStrean(), e)){
				if (!d3.tipo.equals(selectTipo(d3,  e)) ){
					d3.erro = coalesce(d3.erro, formateErro("Esta tentando colocar um "+e.tipo+ "em um "+d3.tipo));
					return false;
				}
				d3.addCods(e.cod,  gen("=", d3.place, e.place) );
				return true;
			}
			
		}
		return true;
	}

	private boolean declaracao4(PlaceCod d4) {
		if (toNextIfEquals(TypeToken.TK_COMMA)){
			PlaceCod d2 = new PlaceCod(d4);
			if(declaracao2(d2)){
				d4.tipo = d2.tipo;
				d4.cod = d2.cod;
				return true;
			}
			d4.erro = coalesce(d2.erro, formateErro("Esperava um valor para atribuição!"));
			return false;
		}
		if (toNextIfEquals(TypeToken.TK_SEMICOLON)){
			return true;
		}
		d4.erro = formateErro("Esperava ';'");
		return false;
	}
}
