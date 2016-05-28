package controller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import excptions.InvalidCharacterExcption;
import gui.AnalyzerViewInterface;
import module.Token;
import module.Token.TypeToken;
import module.lexical.AnalisadorLexico;
import module.syntactic.SyntacticAnalyzerModule;
import module.syntactic.SyntaticStrean;
import gui.AnalyzerView;

public class AnalyzerController implements AnalyzerControllerInterface {
	AnalisadorLexico numReal = new AnalisadorLexico();
	private AnalyzerViewInterface view;
	AnalisadorLexico mAnalisadorLexico;
	private PushbackInputStream entrada;
	private ArrayList<Token> saida;
	private HashMap<String, ArrayList<Token>> tableids;

	public AnalyzerController(AnalyzerViewInterface frame) {
		this.view = frame;
		view.setController(this);
		this.mAnalisadorLexico = new AnalisadorLexico();
	}

	public static void main(String[] args) {
		AnalyzerView frame = new AnalyzerView();
		@SuppressWarnings("unused")
		AnalyzerController analyzerController = new AnalyzerController(frame);
		launchView(frame);
	}

	/**
	 * Launch the application.
	 */
	public static void launchView(AnalyzerView frame) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void analiseLexicaArquivo(File file) throws FileNotFoundException {
		InputStream stream = new FileInputStream(file);
		this.entrada = new PushbackInputStream(stream);
		try {
			this.tableids =  new HashMap<String, ArrayList<Token>>();
			this.saida = this.mAnalisadorLexico.analisar(this.entrada, 0, this.tableids);
			this.view.onResultLexicon(this.saida, this.tableids );
			System.out.println(saida.size());
		} catch (IOException   e) {
			e.printStackTrace();
		} catch (InvalidCharacterExcption e) {
			this.view.onAnalyzeError(e.getLexemas(), e.getMessage(), e.getLinha(), e.getColuna(), e.getExpected());
		}

	}

	@Override
	public void analiseSintatica(ArrayList<Token> entrada, HashMap<String, ArrayList<Token>> tableids2) {
		SyntacticAnalyzerModule sa = new SyntacticAnalyzerModule();
		 boolean result = sa.analyzer(new SyntaticStrean(entrada));
		this.view.onResultSyntatic(result, -1, -1,sa.getErro());
		
	}

}
