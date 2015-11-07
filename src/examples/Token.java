package examples;

public class Token {
	public Token(){
	}
	public Token(String value, TypeToken type){
		this.value = value;
		this.type = type;
	}

	public Token(String value, TypeToken type, int stado, int linha, int posIn , int posFim ){
		this.value = value;
		this.estado = stado;
		this.linha = linha;
		this.posFin	 = posFim;
		this.posIni	 = posIn;
		this.type	 = type;
	}
	public static enum TypeToken{CONST_NUM, NONE,TK_ABRECONCH, TK_FECHCONCH, 
		TK_ABRECHAVE, TK_FECHACHAVE, TK_MAIOR, TK_MENOR, TK_DIV, TK_PLUS, 
		TK_SUB, TK_NEG, TK_ADD};
	
	int linha;
	String value="";
	int estado; 
	int posIni; 
	int posFin; 
	
	TypeToken type = TypeToken.NONE;
	public TypeToken getType() {
		return type;
	}
	public void setType(TypeToken type) {
		this.type = type;
	}
	public void concatValue(char a ){
		value = value +a;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public void incrementLinha() {
		this.linha+=1;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getPosIni() {
		return posIni;
	}
	public void setPosIni(int posIni) {
		this.posIni = posIni;
	}
	public int getPosFin() {
		return posFin;
	}
	public void setPosFin(int posFin) {
		this.posFin = posFin;
	}
	
	
}
