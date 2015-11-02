package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import examples.NumReal;
import examples.Token;

public class NumRealTest {
	NumReal numReal;
	@Before
	public void initialize(){
		numReal = new NumReal();
	}
	
	@Test
	public void lexanTest1() throws Exception {
		lexanReconheceNumReal("123", "123", 6, 2);
		lexanReconheceNumReal(".1", ".1", 2, 1);
		lexanReconheceNumReal(".123", ".123", 2, 3);
		lexanReconheceNumReal("1.23", "1.23", 2, 3);
		lexanReconheceNumReal("12.3", "12.3", 2, 3);
		lexanReconheceNumReal("12.", "12.", 7, 2);
		lexanReconheceNumReal("1.", "1.", 7, 1);
		lexanReconheceNumReal("1.4E", "1.4E", 3, 3);
		lexanReconheceNumReal("14E", "14E", 3, 2);
		lexanReconheceNumReal("1.4E23", "1.4E23", 5, 5);
		lexanReconheceNumReal("1.4E-2", "1.4E-2", 5, 5);
		lexanReconheceNumReal("1.4E+23", "1.4E+23", 5, 6);
		lexanReconheceNumReal("3E", "3E", 3, 1);
		lexanReconheceNumReal("34E3", "34E3", 5, 3);
		lexanReconheceNumReal("35E36", "35E36", 5, 4);

		lexanReconheceNumReal(" \t\n \n 123 d \t\n", "123", 6, 13, 3);
		
	}	

	private void lexanReconheceNumReal(String input, String reconhecida, 
			int estado, int finalPosition) throws Exception {
		lexanReconheceNumReal( input, reconhecida, estado, finalPosition, 1);
	}
	private void lexanReconheceNumReal(String input, String reconhecida, 
			int estado, int finalPosition, int linha) throws Exception {
		PushbackInputStream pbInput = newStrean(input);
		Token token = numReal.lexan(pbInput, 0);
		assertEquals(token.getType(), Token.TypeToken.NUM_REAL);
		assertEquals(token.getValue(), reconhecida);
		assertEquals(token.getEstado(), estado);
		assertEquals(token.getLinha(), linha);
	}
	
	
	private PushbackInputStream newStrean(String exampleString ){
		InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
		return  new PushbackInputStream(stream);
	}

}
