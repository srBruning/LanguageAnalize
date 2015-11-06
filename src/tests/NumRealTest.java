package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import examples.AnalizadorLexon;
import examples.Token;
import examples.Token.TypeToken;

public class NumRealTest {
	AnalizadorLexon numReal;
	@Before
	public void initialize(){
		numReal = new AnalizadorLexon();
	}
	
	@Test
	public void lexanTest1() throws Exception {
		Token[] tks = new Token[4];
		tks[0] = new Token("123",TypeToken.NUM_REAL,  6, 1, 0, 2);
		tks[1] = new Token(".1", TypeToken.NUM_REAL,  2, 1, 4, 5);
		tks[2] = new Token(".123", TypeToken.NUM_REAL,  2, 1, 7, 10);
		tks[3] = new Token("1.23", TypeToken.NUM_REAL,  2, 1, 12, 15);
		
		lexanReconheceNumReal("123 .1 .123 1.23", tks);
//		lexanReconheceNumReal("1.23 ",  new String[]{"1.23"}, 2, 3);
//		lexanReconheceNumReal("12.3 ",  new String[]{"12.3"}, 2, 3);
//		lexanReconheceNumReal("12. ",  new String[]{"12."}, 7, 2);
//		lexanReconheceNumReal("1. ",  new String[]{"1."}, 7, 1);
//		lexanReconheceNumReal("1.4E ",  new String[]{"1.4E"}, 3, 3);
//		lexanReconheceNumReal("14E ",  new String[]{"14E"}, 3, 2);
//		lexanReconheceNumReal("1.4E23 ",  new String[]{"1.4E23"}, 5, 5);
//		lexanReconheceNumReal("1.4E-2 ",  new String[]{"1.4E-2"}, 5, 5);
//		lexanReconheceNumReal("1.4E+23 ",  new String[]{"1.4E+23"}, 5, 6);
//		lexanReconheceNumReal("3E",  new String[]{"3E"}, 3, 1);
//		lexanReconheceNumReal("34E3 ",  new String[]{"34E3"}, 5, 3);
//		lexanReconheceNumReal("35E36 ",  new String[]{"35E36"}, 5, 4);
//
//		lexanReconheceNumReal(" \t\n \n 123 \t\n",  new String[]{"123"}, 6, 13, 3);
		
	}	

	private void lexanReconheceNumReal(String input, Token[] reconhecidos) throws Exception {
		PushbackInputStream pbInput = newStrean(input);
		ArrayList<Token> tks = numReal.lexan(pbInput, 0);
		assertEquals(tks.get(0).getType(), Token.TypeToken.NUM_REAL);
		assertEquals(tks.size(), reconhecidos.length);	
		for(int i =0; i<reconhecidos.length; i++){
			Token a = tks.get(i);
			Token b = reconhecidos[i];
			assertEquals(a.getValue(), b.getValue());			
			assertEquals(a.getEstado(), b.getEstado());
			assertEquals(a.getLinha(), b.getLinha());
			assertEquals(a.getPosFin(), b.getPosFin());
		}
	}
	
	
	private PushbackInputStream newStrean(String exampleString ){
		InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
		return  new PushbackInputStream(stream);
	}

}
