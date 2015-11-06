package examples;

import java.io.PushbackInputStream;
import java.util.ArrayList;

import examples.Token.TypeToken;

public class AnalizadorLexon {

	Token token;
	int atual;
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
			case 10: //tk abreconch
				token.type = TypeToken.TK_ABRECONCH;
				estado =0;
			break;
			case 11: //tk fechaconch
				token.type = TypeToken.TK_FECHCONCH;
				estado =0;
			break;
			case 12: //tk abrechaves
				token.type = TypeToken.TK_ABRECHAVE;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 13: // tk fechachave	
				token.type = TypeToken.TK_FECHACHAVE;
				estado =RECONHECEU_SEM_TRANSICAO;	
			break;
			case 15: //tk maior;
				token.type = TypeToken.TK_MAIOR;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 16: //tk menor
				token.type = TypeToken.TK_MENOR;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 31: //tk div
				token.type = TypeToken.TK_DIV;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 19: // tk plus 
				token.type = TypeToken.TK_PLUS;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 20: // tk sub
				token.type = TypeToken.TK_SUB;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			case 22: //tk neg
				token.type = TypeToken.TK_NEG;
				estado =RECONHECEU_SEM_TRANSICAO;
			break;
			
			}
			if(estado==RECONHECEU_SEM_TRANSICAO || estado == RECONHECEU_COM_TRANSICAO){
				if(estado == RECONHECEU_COM_TRANSICAO){
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
		}while(atual!=-1);
		
		return tks; 
	}

	private int estado0() throws Exception{
		token.setType(TypeToken.NONE);
		if( atual== '\0')return RECONHECEU_SEM_TRANSICAO;
		int new_estado;
		if(atual=='\t' || atual==' ' || atual=='\n'){
			if(atual=='\n')	token.incrementLinha();
			return 0;
		}else if(Character.isDigit(atual)){
			new_estado =  6;
		}else{
			switch(atual){
			case '.': new_estado =  1;
			break;
			case '/': new_estado =  31;
			break;
			case '+': new_estado =  19;
			break;
			case '-': new_estado =  20;
			break;
			case '!': new_estado =  22;
			break;
			case '>': new_estado =  15;
			break;
			case '<': new_estado =  16;
			break;
			case '[': new_estado =  10;
			break;
			case ']': new_estado =  11;
			break;
			case '{': new_estado =  12;
			break;
			case '}': new_estado =  13;
			case -1: new_estado =  0;
			break;
				default:
					System.out.println(atual);
					throw new Exception("caracter inesperado no estado nao final 0.");
			}
		}
		token.concatValue((char) atual);
		return new_estado;
	}

	private int estado6(){
		token.setType(TypeToken.NUM_REAL);
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
		token.setType(TypeToken.NUM_REAL);
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
		token.setType(TypeToken.NUM_REAL);
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
		token.setType(TypeToken.NUM_REAL);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 5;
		}
		return RECONHECEU_COM_TRANSICAO;
	}	
	
	private int estado7() {
		token.setType(TypeToken.NUM_REAL);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 2;
		}
		return RECONHECEU_COM_TRANSICAO;
	}


}
