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
import module.lexical.AnalisadorLexico;
import module.syntactic.SyntacticAnalyzerModule;
import module.syntactic.SyntaticStrean;

public class SyntacticAnalyzerTest {
	AnalisadorLexico lexico;
	SyntacticAnalyzerModule syntactic;
	@Before
	public void initialize(){
		lexico = new AnalisadorLexico();
		syntactic = new SyntacticAnalyzerModule();
	}

//	@Test
//	public void expressionTest() throws Exception {
//		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("a+b-1/5*6;"))  );	
//		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("a+b-1/5*6"))  );//sem ponto-e-virgula		
//	}


	@Test
	public void functionTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(){}"))  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(int b, int c){}"))  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(int b, int c){ int ax=b;b=c;c=a; return;}")) );	
	}

	@Test
	public void chamadfaFuncaoTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("funcao(a);"))  );
		
	}
	@Test
	public void variablesDeclaretionsTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a=0;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a=b=0;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a=b+=8;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a,b,c;"))  );	
		assertFalse(syntactic.analyzer(newfunctionInputSyntactic("int a+=0;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("int a=1,b=2,c=4;"))  );
	}
	
	@Test
	public void assignmetTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("a=1;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("a=!-a/1;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("a=1,b=2,c=4;"))  );
		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("c=0"))  );//sem ponto-e-virgula

		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("c+=1;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("c/=1;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("c*=1;"))  );
	}

	@Test
	public void cimandoIfTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a+1);"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a+1){;}"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a+1);else;"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a+1){;}else;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a+1){;}else{;}"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1);else;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1){if(1);} if(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1){if(1);}else;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1)if(1);else;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(1){if(1);}else{if(1);};"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("if(a=b=9+8);"))  );
		
		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("if((a)=b);"))  );
	}

	@Test
	public void cimandoSwitcTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1){case a+1:;}"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1+a){default:;}"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1+a) {case 0:;case 1: ;}"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1+a) {case 0:;case 1: ;default:;}"))  );	
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1+a);;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("switch(1+a)case a:;"))  );

		assertFalse( syntactic.analyzer(newfunctionInputSyntactic("switch(1 ) {default:;default: ;}"))  );
		assertFalse( syntactic.analyzer(newfunctionInputSyntactic("switch(1 ) {default:;case 0 : ;}"))  );
		assertFalse( syntactic.analyzer(newfunctionInputSyntactic("switch(1){case :;}"))  );
		assertFalse( syntactic.analyzer(newfunctionInputSyntactic("switch(1)case 1:;case 1:;"))  );
	}


	@Test
	public void commandWhileTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("while(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("while(1){;}"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("do ; while(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("do{ ;; }while(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("do; while(1);"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("do{}while(1);"))  );

		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("do while(1);"))  );
		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("do ;"))  );
		assertFalse(	syntactic.analyzer(newfunctionInputSyntactic("do{}while(1)"))  );


	}

	@Test
	public void commandForTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("for(;;);")) );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("for(a=b=c=!a-b;a+b;a=1);"))  );


	}

	@Test
	public void commandReturnTest() throws Exception {
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("return;"))  );
		assertTrue(	syntactic.analyzer(newfunctionInputSyntactic("return !(-a+1-b*c);"))  );


	}

	private SyntaticStrean  newInputSyntactic(String input) throws Exception {
		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pbInput =  new PushbackInputStream(stream);
		return  new SyntaticStrean(lexico.analisar(pbInput, 0, new HashMap<>())); 
	}
	
	private SyntaticStrean  newfunctionInputSyntactic(String input) throws Exception {
		return newInputSyntactic("int c=0; int my_function(){"+input+"}");

	}
}
