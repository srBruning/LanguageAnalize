package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import module.Token;

public interface AnalyzerControllerInterface {
	public void analiseLexicaArquivo(File file) throws FileNotFoundException ;
	public void analiseSintatica(ArrayList<Token> saida2, HashMap<String, ArrayList<Token>> tableids2);
}
