package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnalyzerAssignment extends AbstractSyntacticAnalizer{	
	private static AnalyzerAssignment instance;

	private AnalyzerAssignment(SyntaticStrean strean){
		setSntStrean(strean);
	}

	public static AnalyzerAssignment getInstance(SyntaticStrean strean){
		if (instance == null)
			instance = new AnalyzerAssignment(strean);
		else instance.setSntStrean(strean);
		
		return instance;
	}

	public static boolean isAssignmet(SyntaticStrean strean, PlaceCod d){
		return getInstance(strean).declaracao(d);
	}

	public static boolean isAssignmetOperator(SyntaticStrean strean){
		return false;
	}

	private boolean declaracao(PlaceCod d){
		PlaceCod pcType = new PlaceCod();
		if (type(pcType)){
			PlaceCod d2 = new PlaceCod();
			d2.tipo = pcType.tipo;
			if( declaracao1(d2)){
				d.cod = d2.cod;
				d.place = d2.place;
				d.tipo = d2.tipo;
				return true;
			}
			addErro("Esperava uma declaração!.");
		}
		
		return false;
	}

	private boolean dec_funcao(PlaceCod df){
		return false;
	}
	
	private boolean  declaracao1(PlaceCod d1) {

		PlaceCod df = new PlaceCod();
		if (dec_funcao(df )){
			d1.cod = df.cod;
			d1.place = df.place;
			d1.tipo = df.tipo;
			return true;
		}
		
		if (declaracao2(df )){
			d1.cod = df.cod;
			d1.place = df.place;
			d1.tipo = df.tipo;
			return true;
		}
		
		return false;
	}

	private boolean declaracao2(PlaceCod d2) {
		
		if (currentIsEquals(TypeToken.TK_ID)){
			if( findSimbolById(currentToken().getValue())!=null ){
				addErro("Variavel ja existe!");
				return false;
			}
			addTabSimbulos(currentToken().getValue());
			toNextToken();
			PlaceCod d3 = new PlaceCod(d2);
			
			if (declaracao2(d3)){
				d2.cod = d3.cod;
				d2.tipo = d3.tipo;
				d2.place = d3.place;
				return true;
			}
			addErro("Esperava uma declaração");
			
		}
		return false;
	}
}
