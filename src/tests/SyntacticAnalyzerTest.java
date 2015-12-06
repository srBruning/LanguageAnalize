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
import module.syntactic.SyntacticAnalyzerModule;

public class SyntacticAnalyzerTest {
	LexiconAnalyzer lexico;
	SyntacticAnalyzerModule syntactic;
	@Before
	public void initialize(){
		lexico = new LexiconAnalyzer();
		syntactic = new SyntacticAnalyzerModule();
	}

//	@Test
//	public void expressionTest() throws Exception {
//		assertTrue(	syntactic.analyzer(newInputSyntactic("a+b-1/5*6;"), null)  );	
//		assertFalse(	syntactic.analyzer(newInputSyntactic("a+b-1/5*6"), null)  );//sem ponto-e-virgula		
//	}


	@Test
	public void functionTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(){}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(int b, int c){}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("int myFunction(int b, int c){ int ax=b;b=c;c=a; return;}"), null)  );	
	}

	@Test
	public void variablesDeclaretionsTest() throws Exception {
//		assertTrue(	syntactic.analyzer(newInputSyntactic("int a;"), null)  );
//		assertTrue(	syntactic.analyzer(newInputSyntactic("int a=0;"), null)  );	
//		assertTrue(	syntactic.analyzer(newInputSyntactic("int a=b=0;"), null)  );	
//		assertTrue(	syntactic.analyzer(newInputSyntactic("int a=b+=8;"), null)  );	
//		assertTrue(	syntactic.analyzer(newInputSyntactic("int a,b,c;"), null)  );	
//		assertFalse(	syntactic.analyzer(newInputSyntactic("int a+=0;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("int a=1,b=2,c=4;"), null)  );
	}
	
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
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1);"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1);else;"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a+1){;}else{;}"), null)  );	
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1);else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);} if(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);}else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1)if(1);else;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(1){if(1);}else{if(1);};"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("if(a=b=9+8);"), null)  );
		
		assertFalse(	syntactic.analyzer(newInputSyntactic("if((a)=b);"), null)  );
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


	@Test
	public void commandWhileTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("while(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("while(1){;}"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("do ; while(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("do{ ;; }while(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("do; while(1);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("do{}while(1);"), null)  );

		assertFalse(	syntactic.analyzer(newInputSyntactic("do while(1);"), null)  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("do ;"), null)  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("do{}while(1)"), null)  );


	}

	@Test
	public void commandForTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("for(;;);"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("for(a=b=c=!a-b;a+b;a=1);"), null)  );


	}

	@Test
	public void commandReturnTest() throws Exception {
		assertTrue(	syntactic.analyzer(newInputSyntactic("return;"), null)  );
		assertTrue(	syntactic.analyzer(newInputSyntactic("return !(-a+1-b*c);"), null)  );


	}


	private ArrayList<Token>  newInputSyntactic(String input) throws Exception {
		input = "int function(){"+input+"}";
		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pbInput =  new PushbackInputStream(stream);
		return  lexico.lexan(pbInput, 0, new HashMap<>());

	}
}
