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
	public void lexanTestNums() throws Exception {
		Token[] tks = new Token[9];
		tks[0] = new Token("123",TypeToken.CONST_NUM,  6, 1, 0, 3);
		tks[1] = new Token(".1", TypeToken.CONST_NUM,  2, 1, 4, 6);
		tks[2] = new Token(".123", TypeToken.CONST_NUM,  2, 2, 7, 12);
		tks[3] = new Token("190.23", TypeToken.CONST_NUM,  2, 2, 12, 19);
		tks[4] = new Token("190.", TypeToken.CONST_NUM,  7, 2, 18, 24);
		tks[5] = new Token("1.4E", TypeToken.CONST_NUM,  3, 2, 23, 29);
		tks[6] = new Token("1E4", TypeToken.CONST_NUM,  5, 2, 0, 33);
		tks[7] = new Token("1.5E-3", TypeToken.CONST_NUM,  5, 2, 0, 40);
		tks[8] = new Token("1.5E+3", TypeToken.CONST_NUM,  5, 2, 0, 47);
		lexanReconheceNumReal("\t123 .1 \n.123 190.23 190. 1.4E 1E4 1.5E-3 1.5E+3", tks);
//		lexanReconheceNumReal(" \t\n \n 123 \t\n",  new String[]{"123"}, 6, 13, 3);
		
	}
//	@Test
//	public void lexanTest2() throws Exception {
//		PushbackInputStream pbInput = newStrean("\n\t {100/2+53} ");
//		ArrayList<Token> tks = numReal.lexan(pbInput, 0);
//		assertEquals(tks.size()	, 11);
//	}

	private void lexanReconheceNumReal(String input, Token[] reconhecidos) throws Exception {
		PushbackInputStream pbInput = newStrean(input);
		ArrayList<Token> tks = numReal.lexan(pbInput, 0);
		assertEquals(tks.size(), reconhecidos.length);	
		for(int i =0; i<reconhecidos.length; i++){
			Token a = tks.get(i);
			Token b = reconhecidos[i];
			assertEquals(a.getType(), b.getType());
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
