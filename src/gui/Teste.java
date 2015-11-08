package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import controller.AnalyzerControllerInterface;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Teste extends JFrame implements AnalyzerViewInterface {
	private JTextField txtFileName;


	JPanel panel;
	JEditorPane editorPane;


	private AnalyzerControllerInterface setController;


	private File entrada;

	/**
	 * Create the frame.
	 */
	public Teste() {
		setBounds(100, 100, 628, 504);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(78, Short.MAX_VALUE)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 591, GroupLayout.PREFERRED_SIZE)
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
		panel_2.setBounds(12, 12, 243, 384);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 230, 380);
		panel_2.add(scrollPane);
		
		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
		
		JButton btnInputfile = new JButton("InputFile");
		btnInputfile.addActionListener(new OpenL());
		panel.add(btnInputfile);
		
		txtFileName = new JTextField();
		txtFileName.setText("File name");
		panel.add(txtFileName);
		txtFileName.setColumns(10);
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
		    	  txtFileName.setText(c.getSelectedFile().getName());
		    	  setFileIputStrean(c.getSelectedFile());
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
}
