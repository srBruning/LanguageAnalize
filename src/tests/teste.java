package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import module.lexical.AnalisadorLexico;
import module.syntactic.SyntacticAnalyzerModule;
import module.syntactic.SyntaticStrean;

public class teste {
	AnalisadorLexico lexico;
	SyntacticAnalyzerModule syntactic;
	@Before
	public void initialize(){
		lexico = new AnalisadorLexico();
		syntactic = new SyntacticAnalyzerModule();
	}
	@Test
	public void testFalse() throws Exception {
		assertFalse(	syntactic.analyzer(newInputSyntactic("*"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("-"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("+"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("/"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("/a"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("*a"))  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("1 2 "))  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("id1 id2"))  );			
		assertTrue(	syntactic.analyzer(newInputSyntactic("1+a-b*3"))  );
	}

	@Test
	public void testTrue() throws Exception {
		assertFalse(	syntactic.analyzer(newInputSyntactic("*"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("-"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("+"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("/"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("/a"))  );	
		assertFalse(	syntactic.analyzer(newInputSyntactic("*a"))  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("1 2 "))  );
		assertFalse(	syntactic.analyzer(newInputSyntactic("id1 id2"))  );			
		assertTrue(	syntactic.analyzer(newInputSyntactic("1+a-b*3"))  );
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
