package module.syntactic;

import java.util.HashMap;

import module.CausaErro;
import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public abstract class AbstractAnaliseSintatica {

	private static int seqVar;
	private SyntaticStrean sntStrean;
	private int countLabel;
	public static String LINHA_SEPARADORA = "'LINHA_SEPARADORA'";

	protected String criaTemp() {
		return "T" + seqVar++;
	}

	protected boolean addTabSimbulos(String place, String tipo, Integer address) {
		if (!getSntStrean().getStackSimbVaraibles().isEmpty())
			return getSntStrean().addTabSimbulos(place, tipo, address, criaTemp());
		else
			return getSntStrean().addTabSimbulos(place, tipo, address, null);
	}

	protected void clear() {
		getVariables().clear();
		getSntStrean().getPilhaFunc().clear();
		getSntStrean().getTabFunc().clear();
		seqVar = 0;
		countLabel = 0;
	}

	protected String creatLabel() {
		// FIXME tratar
		return "LABEL" + (countLabel++);
	}

	public HashMap<String, Object[]> getVariables() {
		return getSntStrean().getVariables();
	}

	protected CausaErro formateErro(String erro) {
		Token tk = currentToken();
		tk = sntStrean.getPreviwToken();
		String meta = getClass().toString();
		meta = meta.substring(meta.lastIndexOf(".") + 1);
		meta += ":" + getLineNumber();
		return new CausaErro(erro, tk.getLinha(), tk.getPosIni(), tk.getPosFin(), meta);
	}

	protected String gen(String op, String place, Object place2, Object place3) {

		return getPlaceBp(place) + "=" + getPlaceBp(place2.toString()) + op + getPlaceBp(place3.toString());
	}

	protected String genGotoCond(String place, String op, String place2, String lbElse) {
		Object[] var = getSntStrean().findSimbolById(place);
		if(var!=null && var[2]!=null)
			place = (String) var[2];
		var = getSntStrean().findSimbolById(place2);
		if(var!=null && var[2]!=null)
			place2 = (String) var[2];
		return "if " + place + "==" + place2 + " goto " + lbElse;

	}

	private String getPlaceBp(String place) {
		Object[] op1 = null;
		op1 = this.sntStrean.findSimbolById(place);

		if (op1 != null && op1[2] != null)
			place = (String) op1[2];

		return place;
	}

	protected String gen(String op, String place, String place2) {

		return getPlaceBp(place) + op + getPlaceBp(place2);
	}

	protected Token currentToken() {
		return sntStrean.getCurrentToken();
	}

	protected boolean equalsAndHasNext(TypeToken type) {
		if (currentToken() != null && currentToken().getType() == type && sntStrean.nextToken()) {
			return true;
		}
		;

		return false;
	}

	protected boolean toNextIfEquals(TypeToken type) {
		if (currentToken() != null && currentToken().getType() == type) {
			sntStrean.nextToken();
			return true;
		}
		return false;
	}

	protected boolean toNextToken() {
		return sntStrean.nextToken();
	}

	protected boolean currentIsEquals(TypeToken type) {
		if (currentToken() != null && currentToken().getType() == type) {
			return true;
		}
		return false;
	}

	protected boolean type(PlaceCod tipo) {

		if (currentToken() != null && currentToken().isType()) {
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

	protected <T> T coalesce(T ob, T ob2) {
		if (ob != null)
			return ob;
		return ob2;
	}

	protected String selectTipo(PlaceCod h, PlaceCod e4) {
		return selectTipo(h.tipo, e4.tipo);
	}

	protected String selectTipo(String t1, String t2) {
		if (t1.equals("INT") && t2.equals("INT"))
			return "INT";
		else if (t1.equals("FLOAT") || t2.equals("FLOAT"))
			return "FLOAT";
		else
			return "DOUBLE";
	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[3].getLineNumber();
	}
}
