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
		return e1(ePlaceCod);
	}

	private boolean e1(PlaceCod e1PlaceCod ) {
		return e2(e1PlaceCod);
	}

	private boolean e2(PlaceCod e2Pc) {
//		PlaceCod e3Pc = new PlaceCod();
//		// TODO Auto-generated method stub
//		if ( e3(e3Pc) ){
//			PlaceCod t1h = new PlaceCod(e3Pc);
//			PlaceCod t1s = new PlaceCod();
//			if( T(t1h, t1s)){
//				e2Pc.cod = t1s.cod;
//				e2Pc.tipo = t1s.tipo;
//				e2Pc.place = t1s.place;
//				return true;
//			}
//		};
//		 
//		 return false;
		return e3(e2Pc);
	}

	private boolean T(PlaceCod th, PlaceCod ts){
		
		if ( toNextIfEquals(TypeToken.TK_BIGGEREQUAL)){
			PlaceCod pcE3 = new PlaceCod();
			if (e3(pcE3)){
				PlaceCod t1h = new PlaceCod();
				t1h.place = criaTemp();
				// T.hcod + E3.cod + gen(‘&&’,T¹.hplace,T.hplace,E3.place)
				t1h.addCods(pcE3.cod, gen("&&", t1h.place, pcE3.place));
				PlaceCod t1s = new PlaceCod();
				if ( T(t1h, t1s)){
					ts.place = t1s.place;
					ts.cod = t1s.cod;
					ts.tipo = t1s.tipo;
				}else ts.erro = t1s.erro;
			}
			ts.erro = coalesce(pcE3.erro, formateErro("Esperava uma expreção"));
			return false;
		}
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
				u1h.addCods(h.cod, e4.cod,  gen(op, u1h.place, h.place, e4.place));
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
			}

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
				else if ( tk.getType() == TypeToken.TK_MOD )
					op = "%";

				v1h.addCods(e5.cod, h.cod,  gen(op, v1h.place, h.place, e5.place));
				PlaceCod u1sPlaceCod = new PlaceCod();
				if (v(v1h, u1sPlaceCod)){
					s.place= u1sPlaceCod.place;
					s.cod= u1sPlaceCod.cod;
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
		if ( toNextIfEquals(TypeToken.TK_SUB) || toNextIfEquals(TypeToken.TK_PLUS) 
				|| toNextIfEquals(TypeToken.TK_NEG) ){
			if ( e6(e6) ){
				e5.place = criaTemp();
				e5.tipo = e6.tipo;
				e5.cod = e6.cod+ gen(tk.getValue(), e5.place, e6.place);
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
			String tipo = getSntStrean().findSimbolById(currentToken().getValue());
			if ( tipo ==null){
				e6.erro = formateErro("Identificador não declarado");
				return false;
			}
			e6.place = currentToken().getValue();
			e6.cod = "";
			e6.tipo = tipo;
			toNextToken();
			return true;
		}
		if(toNextIfEquals(TypeToken.TK_OPENPARENTHESIS) ){
			PlaceCod e = new PlaceCod();
			if ( expressao(e) && toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS) ){
				e6.tipo = e.tipo;
				return true;
			}
		}
		return false;
	}



}
