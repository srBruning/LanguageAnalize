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

import examples.LexiconAnalyzer;
import examples.Token;
import examples.Token.TypeToken;
import excptions.InvalidCharacterExcption;
import gui.AnalyzerViewInterface;
import gui.AnalyzerView;

public class AnalyzerController implements AnalyzerControllerInterface {
	LexiconAnalyzer numReal = new LexiconAnalyzer();
	private AnalyzerViewInterface view;
	LexiconAnalyzer analizador;
	private PushbackInputStream entrada;
	private ArrayList<Token> saida;
	private HashMap<TypeToken, ArrayList<Token>> tableids;
	
	public AnalyzerController(AnalyzerViewInterface frame) {
		this.view = frame;
		view.setController(this);
		this.analizador = new LexiconAnalyzer();
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
	public void analizeFile(File file) throws FileNotFoundException {
		InputStream stream = new FileInputStream(file);
		this.entrada = new PushbackInputStream(stream);
		try {
			this.tableids =  new HashMap<TypeToken, ArrayList<Token>>();
			this.saida = this.analizador.lexan(this.entrada, 0, this.tableids);
			this.view.onResult(this.saida, this.tableids );
			System.out.println(saida.size());
		} catch (IOException   e) {
			e.printStackTrace();
		} catch (InvalidCharacterExcption e) {
			this.view.onAnalyzeError(e.getLexemas(), e.getMessage(), e.getLinha(), e.getColuna(), e.getExpected());
		}
		
	}
}
