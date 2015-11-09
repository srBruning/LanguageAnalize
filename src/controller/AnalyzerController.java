package controller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;

import examples.AnalizadorLexon;
import examples.Token;
import excptions.InvalidCharacterExcption;
import gui.AnalyzerViewInterface;
import gui.AnalyzerView;

public class AnalyzerController implements AnalyzerControllerInterface {
	AnalizadorLexon numReal = new AnalizadorLexon();
	private AnalyzerViewInterface view;
	AnalizadorLexon analizador;
	private PushbackInputStream entrada;
	private ArrayList<Token> saida;
	
	public AnalyzerController(AnalyzerViewInterface frame) {
		this.view = frame;
		view.setController(this);
		this.analizador = new AnalizadorLexon();
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
			this.saida = this.analizador.lexan(this.entrada, 0);
			this.view.onResult(saida);
			System.out.println(saida.size());
		} catch (IOException   e) {
			e.printStackTrace();
		} catch (InvalidCharacterExcption e) {
			this.view.onAnalyzeError(e.getLexemas(), e.getMessage(), e.getLinha(), e.getColuna(), e.getExpected());
		}
		
	}
}
