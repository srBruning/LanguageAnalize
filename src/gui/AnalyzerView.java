package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import controller.AnalyzerControllerInterface;
import examples.Token;
import examples.Token.TypeToken;

public class AnalyzerView extends JFrame implements AnalyzerViewInterface {
	private JTextField txtFileName;

	JTextPane console;
	JPanel panel;
	JEditorPane editorPane;


	private AnalyzerControllerInterface controller;


	private File entrada;
	private JTable table;

	private MyTableModel myTableModel;

	//	public File file;

	/**
	 * Create the frame.
	 */
	public AnalyzerView() {
		setBounds(100, 100, 895, 628);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();

		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(12)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
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
		panel_2.setBounds(12, 12, 443, 384);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 49, 423, 336);
		panel_2.add(scrollPane);

		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);


		txtFileName = new JTextField();
		txtFileName.setEnabled(false);
		txtFileName.setEditable(false);
		txtFileName.setBounds(53, 12, 106, 19);
		panel_2.add(txtFileName);
		txtFileName.setText("File name");
		txtFileName.setColumns(10);

		BufferedImage buttonIconOpen;
		BufferedImage buttonIconSave;
		BufferedImage buttonIconRun;
		JButton btnInputfile;
		JButton btnSaveFile;
		JButton btnAnalizar;
		try {
			buttonIconOpen = ImageIO.read(new File("src/img/open_icon.png"));
			buttonIconSave = ImageIO.read(new File("src/img/save_icon.png"));
			buttonIconRun = ImageIO.read(new File("src/img/run_icon.png"));
			btnAnalizar = new JButton(new ImageIcon(buttonIconRun));
			btnInputfile= new JButton(new ImageIcon(buttonIconOpen));
			btnSaveFile = new JButton(new ImageIcon(buttonIconSave));
		} catch (IOException e2) {
			btnInputfile= new JButton("Abrir");
			btnSaveFile = new JButton("Salvar");
			btnAnalizar = new JButton("Analizar");
			e2.printStackTrace();
		} 

		btnSaveFile.setBounds(171, 12, 31, 25);
		panel_2.add(btnSaveFile);		

		btnInputfile.setBounds(10, 12, 31, 25);
		panel_2.add(btnInputfile);

		btnAnalizar.setBounds(332, 12, 54, 25);
		panel_2.add(btnAnalizar);
		btnAnalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AnalyzerView.this.saveFile();
					console.setText("");
					AnalyzerView.this.controller.lexicalAnalyzerFile(entrada);
				} catch (FileNotFoundException   e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnInputfile.addActionListener(new OpenL());
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AnalyzerView.this.saveFile();
				}  catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

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
		panel_4.setBounds(469, 12, 376, 384);
		panel_1.add(panel_4);
		panel_4.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 5, 364, 367);
		panel_4.add(scrollPane_2);
		this.myTableModel = new MyTableModel(new ArrayList<Token>());
		table = new JTable(myTableModel);
		scrollPane_2.setViewportView(table);
		getContentPane().setLayout(groupLayout);

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
		if(this.entrada == null|| !this.entrada.isFile()){
			JFileChooser c = new JFileChooser();
			int returnVal = c.showOpenDialog(AnalyzerView.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				this.entrada =   c.getSelectedFile();

			}
		}
		return this.entrada;
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
			int rVal = c.showOpenDialog(AnalyzerView.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				AnalyzerView.this.entrada = c.getSelectedFile();
				txtFileName.setText(AnalyzerView.this.entrada.getName());
				setFileIputStrean(AnalyzerView.this.entrada);
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				txtFileName.setText("You pressed cancel");
			}
		}
	}
	@Override
	public void setController(AnalyzerControllerInterface controller) {
		this.controller= controller;
	}

	@Override
	public void onAnalyzeError(ArrayList<Token> lexemas, String message, int linha, int coluna, String expected) {
		try {
			Document doc = console.getDocument();
			doc.insertString(doc.getLength(), message+"\n", null);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}
		this.onResult(lexemas, null);
	}

	@Override
	public void onResult(ArrayList<Token> saida, HashMap<String, ArrayList<Token>> tableids) {
		System.out.println("on result");
		this.myTableModel.setTokens(saida);
		this.table.setModel(this.myTableModel);
		this.myTableModel.fireTableDataChanged();

	}

}
