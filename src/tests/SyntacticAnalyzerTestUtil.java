package tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import module.Token;
import module.lexical.AnalisadorLexico;
import module.syntactic.SyntacticAnalyzerModule;
import module.syntactic.SyntaticStrean;

public class SyntacticAnalyzerTestUtil {
	private static SyntacticAnalyzerTestUtil instance;
	AnalisadorLexico lexico;

	private  SyntacticAnalyzerTestUtil(){
		lexico = new AnalisadorLexico();
	}
	
	public static SyntacticAnalyzerTestUtil getInstance(){
		if (instance == null){
			instance = new SyntacticAnalyzerTestUtil();
		}
		return instance;
	}

	public   SyntaticStrean  newInputSyntactic(String input) throws Exception {
		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pbInput =  new PushbackInputStream(stream);
		ArrayList<Token> entrada = lexico.analisar(pbInput, 0, new HashMap<>());
		return  new SyntaticStrean(entrada); 
	}
	
	public SyntaticStrean  newfunctionInputSyntactic(String input) throws Exception {
		return newInputSyntactic("int c=0; int my_function(){"+input+"}");

	}
}
