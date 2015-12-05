package gui;

import java.math.RoundingMode;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import module.Token;

public class MyTableModel extends AbstractTableModel {

	private ArrayList<Token> tokens;

	public MyTableModel(ArrayList<Token> tks){
		this.tokens = tks;
	}
	@Override
	public int getRowCount() {
		return this.tokens.size();
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}
	public void setTokens(ArrayList<Token> saida) {
		this.tokens = saida;
	}
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Token tk = this.tokens.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return Integer.toString(tk.getLinha());
		case 1:
			return Integer.toString(tk.getPosIni());
		case 2:
			return Integer.toString(tk.getPosFin());
		case 3:
			return tk.getType().toString();
		case 4:
			return tk.getValue();
		default:
			break;
		}
		return null;
	}

}
