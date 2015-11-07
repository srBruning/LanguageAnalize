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
		Token[] tks = new Token[9];
		tks[0] = new Token("123",TypeToken.NUM_REAL,  6, 1, 0, 2);
		tks[1] = new Token(".1", TypeToken.NUM_REAL,  2, 1, 4, 5);
		tks[2] = new Token(".123", TypeToken.NUM_REAL,  2, 1, 7, 10);
		tks[3] = new Token("190.23", TypeToken.NUM_REAL,  2, 1, 12, 17);
		tks[4] = new Token("190.", TypeToken.NUM_REAL,  7, 1, 18, 22);
		tks[5] = new Token("1.4E", TypeToken.NUM_REAL,  3, 1, 23, 27);
		tks[6] = new Token("1E4", TypeToken.NUM_REAL,  5, 1, 0, 31);
		tks[7] = new Token("1.5E-3", TypeToken.NUM_REAL,  5, 1, 0, 38);
		tks[8] = new Token("1.5E+3", TypeToken.NUM_REAL,  5, 1, 0, 45);
		lexanReconheceNumReal("\t123 .1 \n.123 190.23 190. 1.4E 1E4 1.5E-3 1.5E+3", tks);
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
