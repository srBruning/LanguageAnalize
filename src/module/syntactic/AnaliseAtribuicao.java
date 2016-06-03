package module.syntactic;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseAtribuicao extends AbstractAnaliseSintatica {
	private AnaliseAtribuicao(SyntaticStrean strean){
		setSntStrean(strean);
	}

	public static boolean isAtribicao(SyntaticStrean strean, PlaceCod ePlaceCod ){
		return new AnaliseAtribuicao(strean).atribuicao(ePlaceCod);
	}

	private boolean atribuicao(PlaceCod a) {
		if (currentIsEquals(TypeToken.TK_ID)){
			String tipo_id = getSntStrean().findSimbolById(currentToken().getValue());
			if( tipo_id ==null ){
				a.erro = formateErro("Variavel não declarada!");
				return false;
			}
			Token tk_id = currentToken();
			toNextToken();
			String op = operador();
			PlaceCod e =new PlaceCod();
			if ( op == null){
				a.erro= formateErro("Esperava um operador de atribuição!");
				return false;
			}
			
			toNextToken();
			if( AnaliseExpressao.isExpressao(getSntStrean(), e) ) {
				if (!e.tipo.equals(tipo_id) && !tipo_id.equals(selectTipo(tipo_id, e.tipo))){
					a.erro = formateErro("Tipo imcompativel!");
					return false;
				}
				if( !toNextIfEquals(TypeToken.TK_SEMICOLON)){
					a.erro = formateErro("Esperava toquem de fim de sentença!");
					return false;
				}
				
				String t1 = e.place;
				a.addCods(e.cod);
				// se é uma atribuição com operação. Ex: a+=1;
				if (!op.isEmpty()){
					t1 = criaTemp();
					a.addCods(gen(op, t1, tk_id.getValue(), e.place));
				}
				//TODO aqui
				a.addCods( gen("=",tk_id.getValue(),t1));
				a.place = e.place;
				a.tipo = tipo_id;
				return true;
			}
			a.erro = coalesce(e.erro, formateErro("Esperava uma atribuição"));
		}

		return false;
	}

	private String operador() {

		String vl =null;
		switch (currentToken().getType()) {
		case TK_ADDASSIGNMENT:
			vl =  "+";
			break;
		case TK_SUBASSIGNMENT:
			vl =  "-";
			break;
		case TK_DIVASSIGNMENT:
			vl =  "/";
			break;
		case TK_MULTPASSIGNMENT:
			vl =  "*";
			break;
		case TK_ASSINGMENT:
			vl =  "";
			break;
		}

		return vl;
	}


}
