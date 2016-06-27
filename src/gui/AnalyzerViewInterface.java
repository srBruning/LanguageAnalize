package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.AnalyzerControllerInterface;
import module.CausaErro;
import module.Token;

public interface AnalyzerViewInterface  {
	public void setController(AnalyzerControllerInterface controller);

	public void onAnalyzeError(ArrayList<Token> lexemas, String message, int linha, int coluna, String expected);

	public void onResultLexicon(ArrayList<Token> saida, HashMap<String, ArrayList<Token>> tableids);
	
	public void onResultSyntatic(boolean valide, String codigoIntermediario, List<CausaErro> errors);	
}

