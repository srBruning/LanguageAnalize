package module.syntactic;

import java.security.InvalidParameterException;

import module.PlaceCod;
import module.Token;
import module.Token.TypeToken;

public class AnaliseAtribuicao extends AbstractAnaliseSintatica {
	private AnaliseAtribuicao(SyntaticStrean strean) {
		setSntStrean(strean);
	}

	public static boolean isCmdAtribicao(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseAtribuicao(strean).cmdAtribuicao(ePlaceCod);
	}

	public static boolean isAtribicao(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseAtribuicao(strean).atribuicao(ePlaceCod);
	}

	public static boolean isAtribicao1(SyntaticStrean strean, PlaceCod ePlaceCod) {
		return new AnaliseAtribuicao(strean).isAtribicao1(ePlaceCod);
	}

	public boolean isAtribicao1(PlaceCod ePlaceCod) {
		if (currentIsEquals(TypeToken.TK_ID)) {
			ePlaceCod.place = currentToken().getValue();
			toNextToken();
			if (atribuicao(ePlaceCod))
				return true;
			if (ePlaceCod.erro == null)
				ePlaceCod.erro = formateErro("esperava uma atribuição apos um id");
		}
		return false;
	}

	private boolean cmdAtribuicao(PlaceCod a) {

		if (atribuicao(a)) {
			if (toNextIfEquals(TypeToken.TK_SEMICOLON)) {
				return true;
			}
			a.erro = formateErro("Esperava toquem de fim de sentença!");
		}
		return false;
	}

	private boolean atribuicao(PlaceCod a) {

		if (a.place == null)
			throw new InvalidParameterException();

		Object[] tipo_id = getSntStrean().findSimbolById(a.place);
		if (tipo_id == null) {
			a.erro = formateErro("Variavel não declarada!");
			return false;
		}
		String tk_id = a.place;
//		toNextToken();
		String op = operador();
		PlaceCod e = new PlaceCod();
		if (op == null) {
			a.erro = formateErro("Esperava um operador de atribuição!");
			return false;
		}

		if (AnaliseExpressao.isExpressao(getSntStrean(), e)) {
			if (!e.tipo.equals(tipo_id[0]) && !tipo_id.equals(selectTipo((String) tipo_id[0], e.tipo))) {
				a.erro = formateErro("Tipo imcompativel!");
				return false;
			}

			String t1 = e.place;
			a.addCods(e.cod);
			// se é uma atribuição com operação. Ex: a+=1;
			if (!op.isEmpty()) {
				t1 = criaTemp();
				a.addCods(gen(op, t1, tk_id, e.place));
			}
			// TODO aqui
			a.addCods(gen("=", tk_id, t1));
			a.place = e.place;
			a.tipo = (String) tipo_id[0];
			return true;
		}
		a.erro = coalesce(e.erro, formateErro("Esperava uma atribuição"));

		return false;
	}

	private String operador() {
		Token tk = currentToken();
		toNextToken();
		if (tk == null)
			return null;
		String vl = null;
		switch (tk.getType()) {
		case TK_ADDASSIGNMENT:
			vl = "+";
			break;
		case TK_SUBASSIGNMENT:
			vl = "-";
			break;
		case TK_DIVASSIGNMENT:
			vl = "/";
			break;
		case TK_MULTPASSIGNMENT:
			vl = "*";
			break;
		case TK_ASSINGMENT:
			vl = "";
			break;
		}

		return vl;
	}

}
