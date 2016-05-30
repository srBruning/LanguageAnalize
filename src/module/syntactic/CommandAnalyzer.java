package module.syntactic;

import javafx.scene.SnapshotParameters;
import module.Token.TypeToken;

public class CommandAnalyzer extends AbstractSyntacticAnalizer {
	
	private CommandAnalyzer(SyntaticStrean strean ){
		setSntStrean(strean);
	}
	public static boolean isCommand(SyntaticStrean strean){
		return false;
	}

	public static boolean isListCommands(SyntaticStrean strean){
		return false;
	}

	public static boolean isVariablesDeclarationscommand(SyntaticStrean strean){
		return false;
	}
}
