package module.syntactic;

import module.Token;
import module.Token.TypeToken;

public class SyntacticAnalyzerModule extends AbstractSyntacticAnalizer{

	public boolean analyzer(SyntaticStrean sntStrean) {
		this.sntStrean = sntStrean;
		//		this.expressao = new ExpressionAnalyzer(sntStrean);
		if(!sntStrean.nextToken()){
			setError("esta em branco ");
			return false;
		}

		return  exprecao(null)   ;
	}

	private boolean exprecao(PlaceCod ePlaceCod ) {
		return e1(ePlaceCod);
	}


	class PlaceCod{
		public PlaceCod(){

		}
		public PlaceCod(PlaceCod x){
			cod = x.cod;
			place = x.place;
		}
		String cod;
		Integer place;
	}


	private Integer criaTemp() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean e1(PlaceCod e1PlaceCod ) {
		return e2(e1PlaceCod);
	}

	private boolean e2(PlaceCod e1PlaceCod) {
		// TODO Auto-generated method stub
		return e3(e1PlaceCod);
	}


	private boolean u(PlaceCod hPlaceCod, PlaceCod sPlaceCod ) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_PLUS)  ||  toNextIfEquals(TypeToken.TK_SUB) ) {
			PlaceCod e4PlaceCod = new PlaceCod();
			if ( e4(e4PlaceCod)){
				PlaceCod u1hPlaceCod = new PlaceCod();
				u1hPlaceCod.place = criaTemp();
				String op = tk.getType() == TypeToken.TK_PLUS ? "+" :"-";
				u1hPlaceCod.cod = e4PlaceCod.cod+ gem(op, u1hPlaceCod.place, hPlaceCod.place, e4PlaceCod.place);
				PlaceCod u1sPlaceCod = new PlaceCod();
				if (u(u1hPlaceCod, u1sPlaceCod)){
					sPlaceCod.place= u1sPlaceCod.place;
					sPlaceCod.cod= u1sPlaceCod.cod;
					return true;
				}

			}
			return false;
		}
		return true;
	}


	private String gem(String op, Integer place, Integer place2, Integer place3) {
		// TODO Auto-generated method stub
		return null;
	}

	private String gem(String string, Object place, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean e3(PlaceCod e3PlaceCod) {

		PlaceCod e4PlaceCod = new PlaceCod();
		if ( e4(e4PlaceCod)){
			PlaceCod uhPlaceCod = new PlaceCod(e4PlaceCod);
			PlaceCod usPlaceCod = new PlaceCod();

			if ( u(uhPlaceCod, usPlaceCod)){
				e3PlaceCod.place = usPlaceCod.place;
				e3PlaceCod.cod = usPlaceCod.cod;
				return true;
			}

		}

		return false;

	}

	private boolean e4(PlaceCod e4PlaceCod) {

		PlaceCod e5PlaceCod = new PlaceCod();
		if ( e5(e5PlaceCod)){
			PlaceCod vhPlaceCod = new PlaceCod(e4PlaceCod);
			PlaceCod vsPlaceCod = new PlaceCod();

			if ( v(vhPlaceCod, vsPlaceCod)){
				e4PlaceCod.place = vsPlaceCod.place;
				e4PlaceCod.cod = vsPlaceCod.cod;
				return true;
			}

		}

		return false;

	}

	private boolean v(PlaceCod hPlaceCod, PlaceCod sPlaceCod) {
		Token tk = currentToken();
		if ( toNextIfEquals(TypeToken.TK_MULTP)  ||  toNextIfEquals(TypeToken.TK_DIV )
				||  toNextIfEquals(TypeToken.TK_MOD ) ){
			PlaceCod e5PlaceCod = new PlaceCod();
			if ( e5(e5PlaceCod)){
				PlaceCod v1hPlaceCod = new PlaceCod();
				v1hPlaceCod.place = criaTemp();

				String op = "*";
				if ( tk.getType() == TypeToken.TK_DIV )
					op = "/";
				else if ( tk.getType() == TypeToken.TK_MOD )
					op = "%";

				v1hPlaceCod.cod = e5PlaceCod.cod+ gem(op, v1hPlaceCod.place, hPlaceCod.place, e5PlaceCod.place);
				PlaceCod u1sPlaceCod = new PlaceCod();
				if (u(v1hPlaceCod, u1sPlaceCod)){
					sPlaceCod.place= u1sPlaceCod.place;
					sPlaceCod.cod= u1sPlaceCod.cod;
					return true;
				}

			}
			return false;
		}
		return true;
	}

	private boolean e5(PlaceCod e5PlaceCod) {
		Token tk = currentToken();
		PlaceCod e6PlaceCod = new PlaceCod();
		if ( toNextIfEquals(TypeToken.TK_SUB) || toNextIfEquals(TypeToken.TK_PLUS) 
				|| toNextIfEquals(TypeToken.TK_NEG) ){
			if ( e6(e6PlaceCod) ){
				e5PlaceCod.place = criaTemp();
				e5PlaceCod.cod = e6PlaceCod.cod+ gem(tk.getValue(), e5PlaceCod.place, e6PlaceCod.place);
				return true;
			}
		}else if ( e6(e6PlaceCod) ){
			e5PlaceCod.place = e6PlaceCod.place;
			e5PlaceCod.cod = e6PlaceCod.cod;
			return true;
		}
		return false;
	}


	private boolean e6(PlaceCod e6PlaceCod) {
		if (toNextIfEquals(TypeToken.CONST_NUM) || toNextIfEquals(TypeToken.TK_ID) ){
			return true;
		}
		if(toNextIfEquals(TypeToken.TK_OPENPARENTHESIS) ){
			PlaceCod ePlaceCod = new PlaceCod();
			if ( exprecao(ePlaceCod) && toNextIfEquals(TypeToken.TK_CLOSEPARENTHESIS) )
		}
		return false;
	}

	public String getErro(){
		return this.sntStrean.getErro();		
	}




}