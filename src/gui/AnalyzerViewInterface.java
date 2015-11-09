package gui;

import java.util.ArrayList;

import controller.AnalyzerControllerInterface;
import examples.Token;

public interface AnalyzerViewInterface  {
	public void setController(AnalyzerControllerInterface controller);

	public void onAnalyzeError(ArrayList<Token> lexemas, String message, int linha, int coluna, String expected);

	public void onResult(ArrayList<Token> saida);
}
