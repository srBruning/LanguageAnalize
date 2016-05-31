package gui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class VariablesTableModel extends AbstractTableModel {

	private ArrayList<String> tipos;
	private ArrayList<String> ids;

	public VariablesTableModel(HashMap<String, String> variables){
		setTokens(variables);
	}
	public VariablesTableModel() {
	}
	@Override
	public int getRowCount() {
		return this.ids.size();
	}


	public void setTokens(HashMap<String, String> saida) {
		tipos = new ArrayList<String>(saida.values());
		ids= new ArrayList<String>(saida.keySet());
	}
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0)
			return ids.get(rowIndex);
		
		return tipos.get(rowIndex);
	}

}
