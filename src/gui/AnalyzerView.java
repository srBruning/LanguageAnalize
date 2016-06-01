package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import controller.AnalyzerControllerInterface;
import module.CausaErro;
import module.Token;

public class AnalyzerView extends AnalyzerViewLay {

	protected AnalyzerControllerInterface controller;

	protected void saveFile() throws FileNotFoundException {
		File f = AnalyzerView.this.currentFile();
		if(f==null)return ;
		PrintWriter out = new PrintWriter(f);
		String text = AnalyzerView.this.editorPane.getText();
		out.println(text);
		out.close();
	}

	protected File currentFile() {
		if(this.entrada == null|| !this.entrada.isFile()){
			JFileChooser c = new JFileChooser();
			int returnVal = c.showOpenDialog(AnalyzerView.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				this.entrada =   c.getSelectedFile();

			}
		}
		return this.entrada;
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
		System.out.println("on result");
		//		this.myTableModel.setTokens(saida);
		//		this.tabela.setModel(this.myTableModel);
		//		this.myTableModel.fireTableDataChanged();
		this.controller.analiseSintatica(saida, tableids);
	}

	@Override
	protected void fazAnalizeLexica() {

		try {
			this.saveFile();
			console.setText("");
			this.controller.analiseLexicaArquivo(entrada);
		} catch (FileNotFoundException   e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.toString(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}		
	}

	@Override
	public void onResultSyntatic(boolean valide, String codigoIntermediario, CausaErro erros,
			HashMap<String, String> variables) {
		System.out.println("___________"+valide);
		writh_out("Sintatico: "+ (valide ? "Valido" : "Invalido: "));
		if ( erros!=null)
				writh_out(erros.getFormatedMessage());

		variablesTableModel.setTokens(variables);
		editorPaneResult.setText(codigoIntermediario);
	}

}






