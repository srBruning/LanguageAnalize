package examples;

import java.io.PushbackInputStream;
import java.util.ArrayList;

import examples.Token.TypeToken;

public class AnalizadorLexon {

	Token token;
	int atual, anterior;
	final int RECONHECEU_SEM_TRANSICAO =-1;
	final int RECONHECEU_COM_TRANSICAO =-2;
	
	public ArrayList<Token> lexan(PushbackInputStream strean, int currentPosition) throws Exception {
		ArrayList<Token> tks = new ArrayList<>();

		token = new Token();
		token.setLinha(1);
		atual =0;
		int   estado= 0;
		int ultimo_estado=-1;
		do{
			ultimo_estado = estado;
			 atual = strean.read();
			switch(estado){
			case 0:
				estado = estado0();
				break;
			case 1:
				estado = estado1();
				break;
			case 2:
				estado = estado2();
				break;
			case 3:
				estado = estado3();
				break;
			case 4:
				estado = estado4();
				break;
			case 5:
				estado = estado5();
				break;
			case 6:
				estado = estado6();
				break;
			case 7:
				estado = estado7();		
				break;
			case 22:
				if(atual== '=')	{
					TypeToken t = TypeToken.NONE;
					if(anterior=='!') t = TypeToken.TK_DIFF;
					if(anterior=='>') t = TypeToken.TK_MAIORIGUAL;
					if(anterior=='<') t = TypeToken.TK_MENORIGUAL;
					if(anterior=='=') t = TypeToken.TK_EQUALS;
					token.setType(t);
					estado =  RECONHECEU_SEM_TRANSICAO;
				}else estado =  RECONHECEU_COM_TRANSICAO;
				break;
			case 31:
				if(atual=='/'){
					token.setValue("");
					token.setType(TypeToken.NONE);
					estado=35;
				} else if(atual=='*'){
					token.setValue("");
					token.setType(TypeToken.NONE);
					estado=33;
				}
				else{
					estado = RECONHECEU_COM_TRANSICAO;
				}
				break;
			case 33:
				if(atual=='*')	estado=34;
				else estado = 33;
				break;	
			case 34:
				if(atual=='/')	estado=0;
				else estado = 33;
				break;	
			case 35:
				if(atual=='\n')	estado=0;
				else estado = 35;
				break;				
			}
			if(estado==RECONHECEU_SEM_TRANSICAO || estado == RECONHECEU_COM_TRANSICAO){
				if(estado== RECONHECEU_COM_TRANSICAO){
					strean.unread(atual);
					currentPosition--;
				}				
				estado =0;
				token.setEstado(ultimo_estado);
				token.setPosFin(currentPosition);
				int l = token.getLinha();
				tks.add(token);
				token = new Token();	
				token.setLinha(l);
			}
			currentPosition++;
			anterior = atual;
		}while(atual!=-1);
		
		return tks; 
	}

	private int estado0() throws Exception{
		token.setType(TypeToken.NONE);
		if( atual== '\0')return RECONHECEU_SEM_TRANSICAO;
		int new_estado;
		if(atual=='\n')	token.incrementLinha();
		if(atual<14 || atual==' ' || atual=='\n'){
			return 0;
		}else if(Character.isDigit(atual)){
			new_estado =  6;
		}else if(atual =='.'){
			new_estado=1;
		}else if(atual ==-1){
			new_estado=0;
		}else{
			TypeToken t;
			new_estado =RECONHECEU_SEM_TRANSICAO;
			switch(atual){
				case '/': t = TypeToken.TK_DIV; new_estado= 31; break;
				case '+': t= TypeToken.TK_PLUS;break;
				case '-': t=  TypeToken.TK_SUB;break;
				case '!': t=  TypeToken.TK_NEG; new_estado= 22; break;
				case '>':t=  TypeToken.TK_MAIOR; new_estado=22; break;
				case '<': t=  TypeToken.TK_MENOR; new_estado=22; break;
				case '{': t=  TypeToken.TK_ABRECHAVE;break;
				case '}': t=  TypeToken.TK_FECHACHAVE;break;
				case '=': t=  TypeToken.TK_ATTRIB; new_estado=22;break;
				default: throw new Exception("caracter inesperado no estado nao final 0.");
			}
			token.setType(t);
		}
		token.concatValue((char) atual);
		return new_estado;
	}
	

	private int estado6(){
		token.setType(TypeToken.CONST_NUM);
		int new_estado;
		if(Character.isDigit(atual)){
			new_estado= 6;
		}else if(atual=='.'){
			new_estado= 7;
		}else if(atual=='E'){
			new_estado= 3;
		}else return RECONHECEU_COM_TRANSICAO;

		token.concatValue((char) atual);
		return new_estado;
	}
	
	private int estado1() throws Exception{
		token.setType(TypeToken.NONE);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 2;
		}
		throw new Exception("caracter inesperado no estado nao final 1.");
	}
	
	private int estado2() {
		token.setType(TypeToken.CONST_NUM);
		int new_estado;
		if(Character.isDigit(atual)){
			new_estado=  2;
		}else if(atual=='E'){
			new_estado= 3;
		}else return RECONHECEU_COM_TRANSICAO;

		token.concatValue((char) atual);
		return new_estado;
	}	
	
	private int estado3() {
		token.setType(TypeToken.CONST_NUM);
		int new_estado;
		if(Character.isDigit(atual)){
			new_estado= 5;
		}else if(atual=='-' || atual=='+'){
			new_estado= 4;
		}else return RECONHECEU_COM_TRANSICAO;
		
		token.concatValue((char) atual);
		return new_estado;
	}

	private int estado4() throws Exception{
		token.setType(TypeToken.NONE);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 5;
		}
		throw new Exception("caracter inesperado no estado nao final 4.");
	}
	
	private int estado5() {
		token.setType(TypeToken.CONST_NUM);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 5;
		}
		return RECONHECEU_COM_TRANSICAO;
	}	
	
	private int estado7() {
		token.setType(TypeToken.CONST_NUM);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 2;
		}
		return RECONHECEU_COM_TRANSICAO;
	}


}
