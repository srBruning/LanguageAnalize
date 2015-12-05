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
import module.Token.TypeToken;
import module.lexical.LexiconAnalyzer;

public class AnalizadorLexicoTest {
	LexiconAnalyzer numReal;
	@Before
	public void initialize(){
		numReal = new LexiconAnalyzer();
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

	@Test
	public void lexanTest2() throws Exception {
		PushbackInputStream pbInput = newStrean("//comentario = ! - + 122 fimcomentario\n\t !{100/2+53}!= /*comentario 123 */< > >= <= = ==");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_NEG);
		assertEquals(tks.get(1).getType(), TypeToken.TK_OPEN_BRAKET);
		assertEquals(tks.get(2).getType(), TypeToken.CONST_NUM);
		assertEquals(tks.get(3).getType(), TypeToken.TK_DIV);
		assertEquals(tks.get(4).getType(), TypeToken.CONST_NUM);
		assertEquals(tks.get(5).getType(), TypeToken.TK_PLUS);
		assertEquals(tks.get(6).getType(), TypeToken.CONST_NUM);
		assertEquals(tks.get(7).getType(), TypeToken.TK_CLOSE_BRAKET);
		assertEquals(tks.get(8).getType(), TypeToken.TK_DIFF);
		assertEquals(tks.get(9).getType(), TypeToken.TK_LESS);
		assertEquals(tks.get(10).getType(), TypeToken.TK_BIGGER);
		assertEquals(tks.get(11).getType(), TypeToken.TK_BIGGEREQUAL);
		assertEquals(tks.get(12).getType(), TypeToken.TK_LESSEQUAL);
		assertEquals(tks.get(13).getType(), TypeToken.TK_ASSINGMENT);
		assertEquals(tks.get(14).getType(), TypeToken.TK_EQUALS);
	}

	@Test
	public void lexanTest3() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"my_var = true && false || oi\nmai_var2+=6");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(1).getType(), TypeToken.TK_ASSINGMENT);
		assertEquals(tks.get(2).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(3).getType(), TypeToken.TK_AND);
		assertEquals(tks.get(4).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(5).getType(), TypeToken.TK_OR);
		assertEquals(tks.get(6).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(7).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(8).getType(), TypeToken.TK_ADDASSIGNMENT);
		assertEquals(tks.get(9).getType(), TypeToken.CONST_NUM);
	}

	@Test
	public void pusAtribTest() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"my_var+=1");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(1).getType(), TypeToken.TK_ADDASSIGNMENT);
		assertEquals(tks.get(2).getType(), TypeToken.CONST_NUM);
	}

	@Test
	public void subAtribTest() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"my_var-=1");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(1).getType(), TypeToken.TK_SUBASSIGNMENT);
		assertEquals(tks.get(2).getType(), TypeToken.CONST_NUM);
	}

	@Test
	public void multAtribTest() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"my_var*=1");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(1).getType(), TypeToken.TK_MULTPASSIGNMENT);
		assertEquals(tks.get(2).getType(), TypeToken.CONST_NUM);
	}
	@Test
	public void divAtribTest() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"my_var/=1");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.TK_ID);
		assertEquals(tks.get(1).getType(), TypeToken.TK_DIVASSIGNMENT);
		assertEquals(tks.get(2).getType(), TypeToken.CONST_NUM);
	}

	@Test
	public void palavresReservadasTest() throws Exception {
		PushbackInputStream pbInput = newStrean(
			"int char");
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
		assertEquals(tks.get(0).getType(), TypeToken.INT);
		assertEquals(tks.get(1).getType(), TypeToken.CHAR);
	}

	private void lexanReconheceNumReal(String input, Token[] reconhecidos) throws Exception {
		PushbackInputStream pbInput = newStrean(input);
		ArrayList<Token> tks = numReal.lexan(pbInput, 0, new HashMap<>());
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
