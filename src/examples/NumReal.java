package examples;

import java.io.PushbackInputStream;

import examples.Token.TypeToken;

public class NumReal {

	Token token;
	int atual;

	public Token lexan(PushbackInputStream strean, int currentPosition) throws Exception {
		token = new Token();
		token.setLinha(1);
		atual =0;
		int   estado= 0;
		int ultimo_estado=-1;
		while(atual!=-1){
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
			break;
			case 11: //tk fechaconch
			break;
			case 12: //tk abrechaves
			break;
			case 13: // tk fechachave		
//			break
//			case '>': new_estado =  15;
//			break;
//			case '<': new_estado =  16;
//			break;
//			case '/': new_estado =  31;
//			break;
//			case '+': new_estado =  19;
//			break;
//			case '-': new_estado =  20;
//			break;
//			case '!': new_estado =  22;
//			break;
			
			}
			if(estado==-1){
				strean.unread(atual);
				token.setEstado(ultimo_estado);
				token.setPosFin(currentPosition);
				return token;
			}
			currentPosition++;
		}
		token.setEstado(ultimo_estado);
		token.setPosFin(currentPosition);
		return token; 
	}

	private int estado0() throws Exception{
		token.setType(TypeToken.NONE);
		if( atual== '\0')return -1;
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
			break;
				default:
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
		}else return -1;

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
		}else return -1;

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
		}else return -1;
		
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
		return -1;
	}	
	
	private int estado7() {
		token.setType(TypeToken.NUM_REAL);
		if(Character.isDigit(atual)){
			token.concatValue((char) atual);
			return 2;
		}
		return -1;
	}


}
