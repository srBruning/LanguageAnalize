package gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import controller.AnalyzerControllerInterface;
import module.CausaErro;
import module.Token;

public class AnalyzerView extends AnalyzerViewLay {

	protected AnalyzerControllerInterface controller;
	private SimpleAttributeSet styleType;
	private Thread thrRunAnalise;


	@Override
	protected void init() {
		editorPane.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				fazAnalizeLexicaLater();			
			}			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
	}

	protected void saveFile() throws FileNotFoundException {
		File f = AnalyzerView.this.currentFile();
		if(f==null)return ;
		PrintWriter out = new PrintWriter(f);

		String text = AnalyzerView.this.editorPane.getText();
		out.println(text);
		out.close();
	}

	protected File currentFile() {
		if(this.fileEntrada == null|| !this.fileEntrada.isFile()){
			JFileChooser c = new JFileChooser();
			int returnVal = c.showOpenDialog(AnalyzerView.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				this.fileEntrada =   c.getSelectedFile();

			}
		}
		return this.fileEntrada;
	}



	@Override
	public void setController(AnalyzerControllerInterface controller) {
		this.controller= controller;
	}

	@Override
	public void onAnalyzeError(ArrayList<Token> lexemas, String message, int linha, int coluna, String expected) {
		writh_out(message);
		this.onResultLexicon(lexemas, null);
	}

	private void writh_out(String message)	{
		try {
			Document doc = console.getDocument();
			doc.insertString(doc.getLength(), message+"\n", null);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}	
	}
	@Override
	public void onResultLexicon(ArrayList<Token> saida, HashMap<String, ArrayList<Token>> tableids) {
		this.controller.analisarSintatico(saida, tableids);
	}

	private MutableAttributeSet getStyleType() {
		if ( styleType == null ){
			styleType = new SimpleAttributeSet();
			StyleConstants.setForeground(styleType, new Color(219, 93, 50));
			StyleConstants.setBold(styleType , true);
		}
		return styleType;
	}

	@Override
	protected void fazAnalizeLexica() {

		try {
			String text =editorPane.getText();
			if ( text.isEmpty())
				return;

			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));			
			this.controller.analiseLexicaArquivo(stream);
		} catch (FileNotFoundException   e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.toString(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}		
	}

	@Override
	public void onResultSyntatic(boolean valide, String codigoIntermediario, List<CausaErro> erros,
			HashMap<String, String> variables) {
		System.out.println("sintatico");
		console.setText("");
		// TODO sintatico
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Document doc = console.getDocument();
				
				writh_out("Sintatico: "+ (valide ? "Valido" : "Invalido: "));
				if ( erros!=null)
					for (CausaErro erro : erros){
						writh_out(erro.getFormatedMessage());
					}

				variablesTableModel.setTokens(variables);
				editorPaneResult.setText(codigoIntermediario);				
			}
		});
	}

//	protected void setStyle(Token tk, MutableAttributeSet attr){
//		Document document = editorPane.getDocument();
//
//		try {						
//			int ini = tk.getLinha()* tk.getPosIni();
//			int fim = tk.getLinha()* tk.getPosFin()+1;
//			int position = editorPane.getCaretPosition();
//				
//			document.remove(ini, fim);
//			
//			document.insertString(ini,tk.getValue(), attr);			
//
//			position = position > document.getLength() ? document.getLength()-1: position;
//			
//			editorPane.setCaretPosition(position);
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//		}
//	}

	private void setFileIputStrean(File file){
		try {
			this.fileEntrada= file;
			editorPane.setPage(file.toURI().toURL());	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void inputFile() {

		JFileChooser c = new JFileChooser();
		// Demonstrate "Open" dialog:
		int rVal = c.showOpenDialog(AnalyzerView.this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			AnalyzerView.this.fileEntrada = c.getSelectedFile();
			txtNomeArquivo.setText(AnalyzerView.this.fileEntrada.getName());
			setFileIputStrean(AnalyzerView.this.fileEntrada);
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			txtNomeArquivo.setText("You pressed cancel");
		}
		fazAnalizeLexicaLater();
	};

	private void fazAnalizeLexicaLater(){
		
		if ( thrRunAnalise==null || !thrRunAnalise.isAlive()){
			console.setText("");
			thrRunAnalise = new Thread(){
				@Override
				public void run() {
					try {
						AnalyzerView.this.fazAnalizeLexica();
						sleep(600);
						thrRunAnalise = null;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			thrRunAnalise.start();
		}
	}

}






