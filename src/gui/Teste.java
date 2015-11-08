package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import controller.AnalyzerControllerInterface;
import examples.Token;
import junit.framework.Test;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.JTable;

public class Teste extends JFrame implements AnalyzerViewInterface {
	private JTextField txtFileName;

	JTextPane console;
	JPanel panel;
	JEditorPane editorPane;


	private AnalyzerControllerInterface setController;


	private File entrada;
	private JTable table;

	private MyTableModel myTableModel;

	public File file;

	/**
	 * Create the frame.
	 */
	public Teste() {
		setBounds(100, 100, 732, 623);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 697, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(12, 12, 324, 384);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 314, 380);
		panel_2.add(scrollPane);
		
		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(12, 420, 555, 93);
		panel_1.add(panel_3);
		panel_3.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 5, 533, 76);
		panel_3.add(scrollPane_1);
		
		console = new JTextPane();
		scrollPane_1.setViewportView(console);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(348, 12, 324, 384);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 5, 300, 367);
		panel_4.add(scrollPane_2);
		this.myTableModel = new MyTableModel(new ArrayList<Token>());
		table = new JTable(myTableModel);
		scrollPane_2.setViewportView(table);
		
		JButton btnInputfile = new JButton("InputFile");
		btnInputfile.addActionListener(new OpenL());
		panel.add(btnInputfile);
		
		txtFileName = new JTextField();
		txtFileName.setText("File name");
		panel.add(txtFileName);
		txtFileName.setColumns(10);
		
		JButton btnAnalizar = new JButton("Analizar");
		btnAnalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Teste.this.setController.analizeFile(entrada);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					 JOptionPane.showMessageDialog(null, e.toString(), "Error",
                             JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton btnSaveFile = new JButton("Save File");
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  BufferedOutputStream out;
                      out = new BufferedOutputStream(new FileOutputStream(Teste.this.file.getName()));
                      Teste.this.editorPane.write(out, Teste.this.editorPane.getDocument(), Teste.this.editorPane.getDocument().getStartPosition().getOffset(), doc.getLength());

			}
		});
		panel.add(btnSaveFile);
		panel.add(btnAnalizar);
		getContentPane().setLayout(groupLayout);

	}
	
	private void setFileIputStrean(File file){
		try {
			this.entrada= file;
			editorPane.setPage(file.toURI().toURL());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	  class OpenL implements ActionListener {

		public void actionPerformed(ActionEvent e) {
	      JFileChooser c = new JFileChooser();
	      // Demonstrate "Open" dialog:
	      int rVal = c.showOpenDialog(Teste.this);
	      if (rVal == JFileChooser.APPROVE_OPTION) {
	    	  Teste.this.file = c.getSelectedFile();
	    	  txtFileName.setText(Teste.this.file.getName());
	    	  setFileIputStrean(Teste.this.file);
	      }
	      if (rVal == JFileChooser.CANCEL_OPTION) {
	    	  txtFileName.setText("You pressed cancel");
	      }
	    }
	  }
	@Override
	public void setController(AnalyzerControllerInterface controller) {
		this.setController= controller;
	}

	@Override
	public void onAnalyzeError(String message, int linha, int coluna, String expected) {
	   try {
		       Document doc = console.getDocument();
		      doc.insertString(doc.getLength(), message, null);
		   } catch(BadLocationException exc) {
		      exc.printStackTrace();
		   }
	}

	@Override
	public void onResult(ArrayList<Token> saida) {
		System.out.println("on result");
		this.myTableModel.setTokens(saida);
		this.table.setModel(this.myTableModel);
		this.myTableModel.fireTableDataChanged();
		
	}
}
