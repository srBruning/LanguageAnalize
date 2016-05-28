package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

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

import controller.AnalyzerControllerInterface;
import module.Token;

public abstract class AnalyzerViewLay extends JFrame  implements AnalyzerViewInterface {

	private JTextField txtNomeArquivo;

	protected JTextPane console;
	protected JPanel panel;
	protected JEditorPane editorPane;


	protected File entrada;
	protected JTable tabela;

	protected MyTableModel myTableModel;

	protected JPanel panel_2;

	protected JPanel panel_1;

	protected JButton btnAnalizar;

	protected JButton btnInputfile;

	protected JButton btnSaveFile;

	protected JPanel panel_3;

	protected JPanel panel_4;
	
	/**
	 * Create the frame.
	 */
	public AnalyzerViewLay() {
		setBounds(100, 100, 895, 628);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createPanel2();
		creatPanel3();
		creatPanel4();

		panel_1 = new JPanel();		
		panel_1.setLayout(null);		
		


		panel_1.add(panel_2);
		panel_1.add(panel_3);
		panel_1.add(panel_4);
		getContentPane().setLayout(getGroupLayout());
		
		addActions();

	}
	
	private void creatPanel4() {
		this.myTableModel = new MyTableModel(new ArrayList<Token>());
		tabela = new JTable(myTableModel);
		panel_4 = new JPanel();
		panel_4.setBounds(469, 12, 376, 384);
		panel_4.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 5, 364, 367);
		scrollPane_2.setViewportView(tabela);
		
		panel_4.add(scrollPane_2);
		
	}

	private void creatPanel3() {
		panel_3 = new JPanel();
		panel_3.setBounds(12, 420, 555, 93);
		panel_3.setLayout(null);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 5, 533, 76);
		console = new JTextPane();
		scrollPane_1.setViewportView(console);
		panel_3.add(scrollPane_1);
	}

	private void addActions() {
		btnAnalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazAnalizeLexica();
			}
		});
		btnInputfile.addActionListener(new OpenL());
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AnalyzerViewLay.this.saveFile();
				}  catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
	}

	protected abstract void fazAnalizeLexica() ;

	protected abstract void saveFile() throws FileNotFoundException;

	private void createPanel2() {

		panel_2 = new JPanel();
		panel_2.setBounds(12, 12, 443, 384);
		panel_2.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 49, 423, 336);
		panel_2.add(scrollPane);

		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);


		txtNomeArquivo = new JTextField();
		txtNomeArquivo.setEnabled(false);
		txtNomeArquivo.setEditable(false);
		txtNomeArquivo.setBounds(53, 12, 106, 19);
		panel_2.add(txtNomeArquivo);
		txtNomeArquivo.setText("File name");
		txtNomeArquivo.setColumns(10);

		
		BufferedImage buttonIconOpen;
		BufferedImage buttonIconSave;
		BufferedImage buttonIconRun;

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
		
	}

	private GroupLayout getGroupLayout() {
		panel = new JPanel();
		
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
		return groupLayout;
	}

	private void setFileIputStrean(File file){
		try {
			this.entrada= file;
			editorPane.setPage(file.toURI().toURL());			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	class OpenL implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			// Demonstrate "Open" dialog:
			int rVal = c.showOpenDialog(AnalyzerViewLay.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				AnalyzerViewLay.this.entrada = c.getSelectedFile();
				txtNomeArquivo.setText(AnalyzerViewLay.this.entrada.getName());
				setFileIputStrean(AnalyzerViewLay.this.entrada);
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				txtNomeArquivo.setText("You pressed cancel");
			}
		}
	}

}
