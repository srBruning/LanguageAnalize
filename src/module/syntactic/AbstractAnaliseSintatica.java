package module.syntactic;

import java.util.HashMap;

import module.CausaErro;
import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public abstract class AbstractAnaliseSintatica {
	
	private int seqVar;
	
	protected String  criaTemp() {
		return "T"+seqVar++;
	}
	
	private SyntaticStrean sntStrean;

	protected CausaErro formateErro(String erro){
		Token tk = currentToken();
		tk = sntStrean.getPreviwToken();
		return new CausaErro(erro, tk.getLinha(), tk.getPosIni(), tk.getPosFin());
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

	protected boolean type(PlaceCod tipo){
		if (currentToken()==null)return false;
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
	protected String selectTipo(PlaceCod h, PlaceCod e4){
		return selectTipo(h.tipo, e4.tipo);
	}
	protected String selectTipo(String t1, String t2){
		if ( t1.equals("INT") && t2.equals("INT") )					
			return "INT"; 
		else if ( t1.equals("FLOAT") || t2.equals("FLOAT") )					
			return  "FLOAT"; 
		else return "DOUBLE";
	}
}
