package gui;

import java.util.ArrayList;
import java.util.HashMap;

import controller.AnalyzerControllerInterface;
import examples.Token;
import examples.Token.TypeToken;

public interface AnalyzerViewInterface  {
	public void setController(AnalyzerControllerInterface controller);

	public void onAnalyzeError(ArrayList<Token> lexemas, String message, int linha, int coluna, String expected);

	public void onResult(ArrayList<Token> saida, HashMap<String, ArrayList<Token>> tableids);
}
