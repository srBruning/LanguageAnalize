package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import module.PlaceCod;
import module.syntactic.AnaliseDeclaracao;
import module.syntactic.SyntaticStrean;

public class AnaliseDeclaracaoTest  {

	private SyntacticAnalyzerTestUtil util;
	private PlaceCod pc;

	@Before
	public void initialize(){
			util = SyntacticAnalyzerTestUtil.getInstance();
			pc = new PlaceCod();
	}
	
	@Test
	public void test() throws Exception {
		SyntaticStrean st =util.newInputSyntactic("int a, b;");
		AnaliseDeclaracao.isDeclaracao(st, pc);
		assertNotNull(pc.erro);
		
	}

}
