package module.syntactic;

import java.util.HashMap;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public abstract class AbstractSyntacticAnalizer {

	private static HashMap<String, String> simbVaraibles;
	
	protected HashMap<String, String> getVariables(){
		if ( simbVaraibles == null)
			simbVaraibles = new HashMap<>();
		return simbVaraibles;
	}
	
	private int seqVar;
	
	protected String  criaTemp() {
		return "T"+seqVar++;
	}
	
	private SyntaticStrean sntStrean;

	protected void addErro(String item){
		Token tk = currentToken();
		tk = sntStrean.getPreviwToken();
		sntStrean.addErro(item, tk.getLinha(), tk.getPosIni(), tk.getPosFin());
	}

	protected String gen(String op, String place, String place2, String place3) {		
		return place + "=" + place2 + op + place3;   
	}

	protected String gen(String op, String  place, String place2) {
		return place + op + place2;
	}
	
	protected Token currentToken(){
		return sntStrean.getCurrentToken();
	}
	protected boolean equalsAndHasNext(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type  && sntStrean.nextToken()  ){
			return true;
		};

		return false;
	}
	


	protected boolean toNextIfEquals(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type){
			sntStrean.nextToken();
			return true;
		}
		return false;
	}

	protected boolean toNextToken(){
		return sntStrean.nextToken();
	}
	
	protected boolean currentIsEquals(TypeToken type){
		if(currentToken()!= null  && currentToken().getType() == type){
			return true;
		}
		return false;
	}
	
	
	protected boolean addTabSimbulos(String place, String tipo){
		if (findSimbolById(place)!=null)
			return false;
		
		getVariables().put(place, tipo);
		return true;
	}
	
	protected String findSimbolById(String value) {
		return getVariables().get(value);
	}

	protected boolean type(PlaceCod tipo){
		switch (currentToken().getType()) {
		case  INT:
		case DOUBLE:
		case FLOAT:
			tipo.tipo = currentToken().getType().toString();
			this.sntStrean.nextToken();
			return true;
		}

		return false;
	}

	public SyntaticStrean getSntStrean() {
		return sntStrean;
	}

	public void setSntStrean(SyntaticStrean sntStrean) {
		this.sntStrean = sntStrean;
	}
	
	protected <T> T coalesce(T ob, T ob2){
		if (ob!=null)
			return ob;
		return ob2;		
	}
}
