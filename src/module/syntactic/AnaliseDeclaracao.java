package module.syntactic;

import module.PlaceCod;
import module.Token;
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



	private boolean declaracao(PlaceCod d) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean corpoFuncao(PlaceCod cf) {
		
		PlaceCod t =new PlaceCod();
		if (type(t)){
			Token ct = currentToken();
			if (toNextIfEquals(TypeToken.TK_ID) ){
				PlaceCod d2 = new PlaceCod();
				d2.tipo = t.tipo;
				d2.place = ct.getValue();
				if ( declaracao2(d2)){
					
				}else{
					cf.erro = d2.erro; 
				}
			}else{
				cf.erro  = formateErro("Esperava um identificador depois de um tipo!");
				return false;
			}
			
		}
		return false;
	}

	private boolean declaracao2(PlaceCod d2) {
		PlaceCod cf = new PlaceCod();
		cf.returnType = d2.tipo;
		cf.place = d2.place;
		if ( corpoFuncao(cf) ){
			d2.cod = d2.cod+ cf.cod;
			d2.address = cf.address;
			return true;
		}else{
			if( getSntStrean().findSimbolById(currentToken().getValue())!=null ){
				d2.erro  = formateErro("Já há uma varivel com o nome "+currentToken().getValue());
				return false;
			}
			getSntStrean().addTabSimbulos(currentToken().getValue(), d2.tipo, d2.address);
			if ( d2.address!=null)
				d2.address++;
			
			PlaceCod d3 = new PlaceCod(d2);
			d3.place = currentToken().getValue(); 
			toNextToken();			
			if (declaracao3(d3)){
				d2.cod = d3.cod;
				d2.address = d3.address;
			}
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
				d3.setCods(e.cod,  gen("=", d3.place, e.place) );
				PlaceCod d4 = new PlaceCod(d3);
				if (declaracao4(d4)){
					d3.cod = d3.cod + gen("=", d3.place, e.place) + d4.cod;
					d3.address = d4.address;
					
				}else{
					d3.erro = d4.erro;
				}
			}else{
				d3.erro = coalesce(e.erro, formateErro("Esperava um valor para a atribuição"));
				return false;
			}		
			

		}else{
			PlaceCod d4 = new PlaceCod(d3);
			if ( declaracao4(d4)){
				d3.cod = d4.cod;
				d3.address = d4.address;
				return true;
				
			}
			d3.erro = d4.erro;
		}
		return false;
	}

	private boolean declaracao4(PlaceCod d4) {
		if (toNextIfEquals(TypeToken.TK_COMMA)){
			PlaceCod d5 = new PlaceCod(d4);
			if(declaracao5(d5)){
				d4.tipo = d5.tipo;
				d4.cod = d5.cod;
				d4.address = d5.address;

				return true;
			}
			d4.erro = coalesce(d5.erro, formateErro("Esperava uma declaração!"));
			return false;
		}
		if (toNextIfEquals(TypeToken.TK_SEMICOLON)){
			return true;
		}
		d4.erro = formateErro("Esperava token de Fim de Sentença!");
		return false;
	}

	private boolean declaracao5(PlaceCod d5) {
		if (currentIsEquals(TypeToken.TK_ID)){
			if( getSntStrean().findSimbolLocalTableById(currentToken().getValue())!=null ){
				d5.erro  = formateErro("Já há uma varivel com o nome "+currentToken().getValue());
				return false;
			}
			
			getSntStrean().addTabSimbulos(d5.place	, d5.tipo, d5.address);
			if (d5.address != null){
				d5.address += 4;
			}
			PlaceCod d3 = new PlaceCod(d5);
			
			if (declaracao3(d3)){
				d5.cod = d3.cod;
				d5.address = d3.address;
				return true;
			}else{
				d5.erro = d3.erro;
			}
		}
		return false;
	}
}
