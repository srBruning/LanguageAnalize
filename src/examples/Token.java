package examples;

public class Token {
	public Token(){
	}
	public Token(String value, TypeToken type){
		this.value = value;
		this.type = type;
		this.id = IDReservado.NULL;
	}

	public Token(String value, TypeToken type, int stado, int linha, int posIn , int posFim ){
		this.value = value;
		this.estado = stado;
		this.linha = linha;
		this.posFin	 = posFim;
		this.posIni	 = posIn;
		this.type	 = type;
		this.id = IDReservado.NULL;
	}
	public static enum TypeToken{CONST_NUM, NONE, 
		TK_ABRECHAVE, TK_FECHACHAVE, TK_MAIOR,TK_MAIORIGUAL, TK_MENOR,TK_MENORIGUAL, TK_DIV, TK_PLUS, 
		TK_SUB, TK_NEG, TK_EQUALS,TK_ATTRIB, TK_DIFF,TK_ABREPAR,TK_FECHAPAR,TK_PONTOVIRG,TK_ID,TK_MOD,TK_PLUSIGUAL,
		TK_SUBIGUAL,TK_MULTP,TK_MULTPIGUAL,TK_DIVIGUAL,TK_VIRG,TK_AND,TK_OR};
		
	public static enum IDReservado{NULL,VAR,AUTO,BREAK,CASE,CHAR,CONST,CONTINUE,DEFAULT,DO,INT,LONG,REGISTER,RETURN,SHORT,SIGNED,
		SIZEOF,STATIC,STRUCT,SWITCH,TYPEDEF,UNION,UNSIGNED,VOID,VOLATILE,WHILE,DOUBLE,ELSE,ENUM,EXTERN,FLOAT,FOR,GOTO,IF,
		};		
	
	int linha;
	String value="";
	int estado; 
	int posIni; 
	int posFin; 
	IDReservado id = IDReservado.NULL;
	TypeToken type = TypeToken.NONE;
	
	
	
	
	public IDReservado getId() {
		return id;
	}
	public void setId(IDReservado id) {
		this.id = id;
	}
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
