package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import module.Token;
import module.lexical.LexiconAnalyzer;
import module.syntactic.SyntacticAnalyzer;

public class SyntacticAnalyzerTest {
	LexiconAnalyzer lexico;
	SyntacticAnalyzer syntactic;
	@Before
	public void initialize(){
		lexico = new LexiconAnalyzer();
		syntactic = new SyntacticAnalyzer();
	}

//	@Test
//	public void expressionTest() throws Exception {
//		assertTrue(	syntactic.analyzer(newInputSyntactic("a+b-1/5*6;"), null)  );	
//		assertFalse(	syntactic.analyzer(newInputSyntactic("a+b-1/5*6"), null)  );//sem ponto-e-virgula		
//	}
	
	@Test
	public void assignmetTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("a=1;"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("a=!-a/1;"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("a=1,b=2,c=4;"), null)  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("c=0"), null)  );//sem ponto-e-virgula

		assertTrue(	syntactic.analyzer(newInputSyntactic("c+=1;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("c-=1;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("c/=1;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("c*=1;"), null)  );
	}

	@Test
	public void cimandoIfTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1);"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1);else;"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}else{;}"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a);;;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1);else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);} if(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);}else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1)if(1);else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);}else{if(1);};"), null)  );
	}

	@Test
	public void cimandoSwitcTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1){case a+1:;}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1+a){default:;}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1+a) {case 0:;case 1: ;}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1+a) {case 0:;case 1: ;default:;}"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1+a);;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("switch(1+a)case a:;"), null)  );

		assertFalse( syntactic.analyzer(newInputSyntactic("switch(1 ) {default:;default: ;}"), null)  );
		assertFalse( syntactic.analyzer(newInputSyntactic("switch(1 ) {default:;case 0 : ;}"), null)  );
		assertFalse( syntactic.analyzer(newInputSyntactic("switch(1){case :;}"), null)  );
		assertFalse( syntactic.analyzer(newInputSyntactic("switch(1)case 1:;case 1:;"), null)  );
	}

	private ArrayList<Token>  newInputSyntactic(String input) throws Exception {
		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pbInput =  new PushbackInputStream(stream);
		return  lexico.lexan(pbInput, 0, new HashMap<>());

	}
}
