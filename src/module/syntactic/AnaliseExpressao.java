package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseExpressao extends AbstractAnaliseSintatica{
	private AnaliseExpressao(SyntaticStrean strean){
		setSntStrean(strean);
	}

	public static boolean isExpressao(SyntaticStrean strean, PlaceCod ePlaceCod ){
		return new AnaliseExpressao(strean).expressao(ePlaceCod);
	}

	private boolean expressao(PlaceCod ePlaceCod ) {

		PlaceCod e1PlaceCod = new PlaceCod();
		if ( e1(e1PlaceCod)){
			PlaceCod rhPlaceCod = new PlaceCod(e1PlaceCod);
			PlaceCod rsPlaceCod = new PlaceCod();

			if ( r(rhPlaceCod, rsPlaceCod)){
				ePlaceCod.place = rsPlaceCod.place;
				ePlaceCod.cod = rsPlaceCod.cod;
				ePlaceCod.tipo = rsPlaceCod.tipo;
				return true;
			}else{
				ePlaceCod.erro = rsPlaceCod.erro;
			}

		}else{
			ePlaceCod.erro = e1PlaceCod.erro;
		}

		return false;		
		//return e1(ePlaceCod);
	}

	private boolean r(PlaceCod h, PlaceCod s ) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_AND)  ||  toNextIfEquals(TypeToken.TK_OR) ) {
			PlaceCod e1 = new PlaceCod();
			if ( e1(e1)){
				PlaceCod r1h = new PlaceCod();
				r1h.place = criaTemp();
				r1h.tipo = selectTipo(h, e1);

				String op = tk.getType() == TypeToken.TK_AND ? "&&" :"||";
				r1h.setCods(h.cod, e1.cod,  gen(op, r1h.place, h.place, e1.place));
				PlaceCod r1s = new PlaceCod();
				if (r(r1h, r1s)){
					s.place= r1s.place;
					s.cod= r1s.cod;
					s.tipo= r1s.tipo;
					return true;
				}

			}
			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		s.place = h.place;
		s.cod = h.cod;
		s.tipo = h.tipo;
		return true;
	}

	private boolean e1(PlaceCod e1PlaceCod ) {

		PlaceCod e2PlaceCod = new PlaceCod();
		if ( e2(e2PlaceCod)){
			PlaceCod shPlaceCod = new PlaceCod(e2PlaceCod);
			PlaceCod ssPlaceCod = new PlaceCod();

			if ( s(shPlaceCod, ssPlaceCod)){
				e1PlaceCod.place = ssPlaceCod.place;
				e1PlaceCod.cod = ssPlaceCod.cod;
				e1PlaceCod.tipo = ssPlaceCod.tipo;
				return true;
			}else{
				e1PlaceCod.erro = ssPlaceCod.erro;
			}

		}else{
			e1PlaceCod.erro = e2PlaceCod.erro;
		}

		return false;
	}

	private boolean s(PlaceCod h, PlaceCod s ) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_EQUALS)  ||  toNextIfEquals(TypeToken.TK_DIFF) ) {
			PlaceCod e2 = new PlaceCod();
			if ( e2(e2)){
				PlaceCod s1h = new PlaceCod();
				s1h.place = criaTemp();
				s1h.tipo = selectTipo(h, e2);

				String op = tk.getType() == TypeToken.TK_EQUALS ? "==" :"!=";
				s1h.setCods(h.cod, e2.cod,  gen(op, s1h.place, h.place, e2.place));
				PlaceCod s1s = new PlaceCod();
				if (s(s1h, s1s)){
					s.place= s1s.place;
					s.cod= s1s.cod;
					s.tipo= s1s.tipo;
					return true;
				}

			}
			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		s.place = h.place;
		s.cod = h.cod;
		s.tipo = h.tipo;
		return true;
	}	

	private boolean e2(PlaceCod e2PlaceCod) {
		PlaceCod e3PlaceCod = new PlaceCod();
		if ( e3(e3PlaceCod)){
			PlaceCod thPlaceCod = new PlaceCod(e3PlaceCod);
			PlaceCod tsPlaceCod = new PlaceCod();

			if ( t(thPlaceCod, tsPlaceCod)){
				e2PlaceCod.place = tsPlaceCod.place;
				e2PlaceCod.cod = tsPlaceCod.cod;
				e2PlaceCod.tipo = tsPlaceCod.tipo;
				return true;
			}

		}else{
			e2PlaceCod.erro = e3PlaceCod.erro;
		}

		return false;
	}

	private boolean t(PlaceCod h, PlaceCod s ) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_BIGGEREQUAL)  ||  toNextIfEquals(TypeToken.TK_LESSEQUAL) ) {
			PlaceCod e3 = new PlaceCod();
			if ( e3(e3)){
				PlaceCod t1h = new PlaceCod();
				t1h.place = criaTemp();
				t1h.tipo = selectTipo(h, e3);

				String op = tk.getType() == TypeToken.TK_BIGGEREQUAL ? ">=" :"<=";
				t1h.setCods(h.cod, e3.cod,  gen(op, t1h.place, h.place, e3.place));
				PlaceCod t1s = new PlaceCod();
				if (t(t1h, t1s)){
					s.place= t1s.place;
					s.cod= t1s.cod;
					s.tipo= t1s.tipo;
					return true;
				}

			}
			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		if ( toNextIfEquals(TypeToken.TK_LESS)  ||  toNextIfEquals(TypeToken.TK_BIGGER) ) {
			PlaceCod e3 = new PlaceCod();
			if ( e3(e3)){
				PlaceCod t1h = new PlaceCod();
				t1h.place = criaTemp();
				t1h.tipo = selectTipo(h, e3);

				String op = tk.getType() == TypeToken.TK_LESS ? ">" :"<";
				t1h.setCods(h.cod, e3.cod,  gen(op, t1h.place, h.place, e3.place));
				PlaceCod t1s = new PlaceCod();
				if (t(t1h, t1s)){
					s.place= t1s.place;
					s.cod= t1s.cod;
					s.tipo= t1s.tipo;
					return true;
				}

			}			
			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		s.place = h.place;
		s.cod = h.cod;
		s.tipo = h.tipo;
		return true;
	}
	private boolean u(PlaceCod h, PlaceCod s ) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_PLUS)  ||  toNextIfEquals(TypeToken.TK_SUB) ) {
			PlaceCod e4 = new PlaceCod();
			if ( e4(e4)){
				PlaceCod u1h = new PlaceCod();
				u1h.place = criaTemp();
				u1h.tipo = selectTipo(h, e4);

				String op = tk.getType() == TypeToken.TK_PLUS ? "+" :"-";
				u1h.setCods(h.cod, e4.cod,  gen(op, u1h.place, h.place, e4.place));
				PlaceCod u1s = new PlaceCod();
				if (u(u1h, u1s)){
					s.place= u1s.place;
					s.cod= u1s.cod;
					s.tipo= u1s.tipo;
					return true;
				}

			}
			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		s.place = h.place;
		s.cod = h.cod;
		s.tipo = h.tipo;
		return true;
	}


	private boolean e3(PlaceCod e3PlaceCod) {

		PlaceCod e4PlaceCod = new PlaceCod();
		if ( e4(e4PlaceCod)){
			PlaceCod uhPlaceCod = new PlaceCod(e4PlaceCod);
			PlaceCod usPlaceCod = new PlaceCod();

			if ( u(uhPlaceCod, usPlaceCod)){
				e3PlaceCod.place = usPlaceCod.place;
				e3PlaceCod.cod = usPlaceCod.cod;
				e3PlaceCod.tipo = usPlaceCod.tipo;
				return true;
			}

		}else{
			e3PlaceCod.erro = e4PlaceCod.erro;
		}

		return false;

	}

	private boolean e4(PlaceCod e4) {

		PlaceCod e5 = new PlaceCod();
		if ( e5(e5)){
			PlaceCod vh = new PlaceCod(e5);
			PlaceCod vs = new PlaceCod();

			if ( v(vh, vs)){
				e4.place = vs.place;
				e4.cod = vs.cod;
				e4.tipo = vs.tipo;
				return true;
			}else{
				e4.erro = vs.erro;
			}

		}else{
			e4.erro = e5.erro;
		}


		return false;

	}

	private boolean v(PlaceCod h, PlaceCod s) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_MULTP)  ||  toNextIfEquals(TypeToken.TK_DIV )
				||  toNextIfEquals(TypeToken.TK_MOD ) ){
			PlaceCod e5 = new PlaceCod();
			if ( e5(e5)){
				PlaceCod v1h = new PlaceCod();
				v1h.tipo = selectTipo(h, e5);				
				v1h.place = criaTemp();

				String op = "*";
				if ( tk.getType() == TypeToken.TK_DIV )
					op = "/";
				else if ( tk.getType() == TypeToken.TK_MOD ){
					op = "%";
					if (h.tipo.equals("FLOAT") || e5.tipo.equals("FLOAT")){
						s.erro = formateErro("Resto de divizão so aplica a tipos inteiros");
						return false;
					}
				}

				v1h.setCods(e5.cod, h.cod,  gen(op, v1h.place, h.place, e5.place));
				PlaceCod u1sPlaceCod = new PlaceCod();
				if (v(v1h, u1sPlaceCod)){
					s.place= u1sPlaceCod.place;
					s.cod= u1sPlaceCod.cod;
					s.tipo= u1sPlaceCod.tipo;
					return true;
				}

			}

			s.erro = formateErro("Expressão invalida!");
			return false;
		}
		s.cod = h.cod;
		s.place = h.place;
		s.tipo = h.tipo;
		return true;
	}

	private boolean e5(PlaceCod e5) {
		Token tk = currentToken();
		PlaceCod e6 = new PlaceCod();
		if ( currentIsEquals(TypeToken.TK_SUB) || currentIsEquals(TypeToken.TK_PLUS) 
				|| toNextIfEquals(TypeToken.TK_NEG) ){
			TypeToken op = currentToken().getType();
			if ( e6(e6) ){
				e5.place = criaTemp();
				e5.tipo = e6.tipo;
				switch(op){
				case TK_PLUS:				
					e5.cod = e6.cod+ gen("=",  e5.place, e6.place);
					break;
				case TK_SUB:
					e5.cod = e6.cod+ gen("-",  e5.place,"0",  e6.place);
					break;
				case TK_NEG:// TODO AQUI
					e5.cod = "if "+e6.place+"= 0 goto false\n"+
							gen("=", e5.place, "1")+"\ngoto fim\n"+
							"false:"+gen("=", e5.place, "0")+
							"fim:";

				}
				return true;
			}
		}else if ( e6(e6) ){
			e5.place = e6.place;
			e5.cod = e6.cod;
			e5.tipo = e6.tipo;
			return true;
		}
		return false;
	}


	private boolean e6(PlaceCod e6) {
		if (currentIsEquals(TypeToken.CONST_NUM)  ){
			e6.place = criaTemp();
			if (currentToken().getValue().toString().indexOf(".") > 0) 
				e6.tipo =  "FLOAT" ;
			else if (currentToken().getValue().length()>8)
				e6.tipo = "DOUBLE";
			else e6.tipo = "INT";				

			e6.cod = gen("=", e6.place, currentToken().getValue());
			toNextToken();
			return true;
		}
		if (currentIsEquals(TypeToken.TK_ID) ){
			 Object[] tipo = getSntStrean().findSimbolById(currentToken().getValue());
			if ( tipo ==null){
				e6.erro = formateErro("Identificador não declarado");
				return false;
			}
			e6.place = currentToken().getValue();
			e6.cod = "";
			e6.tipo = (String) tipo[0];
			toNextToken();
			return true;
		}
		if(toNextIfEquals(TypeToken.TK_OPENPARENTHESIS) ){
			PlaceCod e = new PlaceCod();
			if ( expressao(e) && toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS) ){
				e6.place = e.place;
				e6.cod = e.cod;
				e6.tipo = e.tipo;
				return true;
			}
		}
		return false;
	}



}
