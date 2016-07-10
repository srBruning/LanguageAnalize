package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import module.CausaErro;
import module.Token;

public class SyntaticStrean {
	private ArrayList<Token> entrada;
	private int currentPosition;
	// Stack<Integer> stakPosition= new Stack<>();
	Token currentToken;
	private List<CausaErro> erro;

	private static HashMap<String, Object[]> simbVaraibles;

	private static HashMap<String, FuncaoBean> tabFunc;

	private Stack<HashMap<String, Object[]>> stackSimbVaraibles = new Stack<>();

	private Stack<FuncaoBean> pilhaFunc = new Stack<>();

	public List<CausaErro> getErro() {
		if (erro == null)
			erro = new ArrayList<>();
		return erro;
	}

	public void addErro(String msg, int linha, int colIni, int colFim) {
		getErro().add(new CausaErro(msg, linha, colIni, colFim));
	}

	public ArrayList<Token> getEntrada() {
		return entrada;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public Token getCurrentToken() {
		return currentToken;
	}

	public SyntaticStrean(ArrayList<Token> entrada) {
		this.entrada = entrada;
		currentPosition = -1;

	}

	public boolean hasNextToken() {
		return currentPosition <= entrada.size() - 1;
	}

	public boolean nextToken() {
		currentPosition++;
		if (currentPosition >= entrada.size()) {
			currentToken = null;
			return false;
		}
		currentToken = entrada.get(currentPosition);
		return true;
	}

	public Token getLastToken() {
		return entrada.get(entrada.size() - 1);
	}

	public Token getPreviwToken() {
		return entrada.get(currentPosition > 0 ? currentPosition - 1 : currentPosition);
	}

	public void setCurrentToken(Token currentToken) {
		this.currentToken = currentToken;
	}

	public HashMap<String, Object[]> getVariables() {
		if (simbVaraibles == null)
			simbVaraibles = new HashMap<>();
		return simbVaraibles;
	}

	protected boolean addTabSimbulos(String place, String tipo, Integer address) {
		return addTabSimbulos(place, tipo, address, null);
	}
	protected boolean addTabSimbulos(String place, String tipo, Integer address, String varTmp) {
		if (findSimbolById(place) != null)
			return false;
		
		if( !getStackSimbVaraibles().isEmpty() ){
			 getStackSimbVaraibles().peek().put(place, new Object[] { tipo, address, varTmp });
			 return true;
		}
		
		
		getVariables().put(place, new Object[] { tipo, address, varTmp });
		return true;
	}

	protected Object[] findSimbolById(String value) {
		Object[] r = findSimbolLocalTableById(value);
		if (r == null)
			r = getVariables().get(value);
		return r;
	}

	protected Object[] findSimbolLocalTableById(String value) {
		if( getStackSimbVaraibles().isEmpty() )
			return null;
		return getStackSimbVaraibles().peek().get(value);
	}

	protected void pushTabSimbulo() {
		getStackSimbVaraibles().push(new HashMap<>());
	}

	protected void popTabSimbulo() {
		getStackSimbVaraibles().pop();
	}

	protected void removTabSimbulo() {
		getStackSimbVaraibles().pop();
	}

	public Stack<HashMap<String, Object[]>> getStackSimbVaraibles() {
		if (stackSimbVaraibles == null)
			stackSimbVaraibles = new Stack<>();
		return stackSimbVaraibles;
	}

	public  HashMap<String, FuncaoBean> getTabFunc() {
		if (tabFunc == null)
			tabFunc = new HashMap<>();

		return tabFunc;
	}

	public FuncaoBean findFuncao(String place) {
		return getTabFunc().get(place);
	}

	public void addTabFunc(FuncaoBean bean) {
		getTabFunc().put(bean.getName(), bean);
	}
	

	public void pushFuncao(FuncaoBean funcBean) {
		pilhaFunc.push(funcBean);

	}
	public FuncaoBean peekFuncao() {
		return pilhaFunc.peek();

	}
	public boolean isFunction(){
		return pilhaFunc.size() >0;
	}
	public FuncaoBean popFuncao() {
		return pilhaFunc.pop();				

	}

	public Stack<FuncaoBean> getPilhaFunc() {
		return pilhaFunc;
	}

	public void setPilhaFunc(Stack<FuncaoBean> pilhaFunc) {
		this.pilhaFunc = pilhaFunc;
	}

	public void setStackSimbVaraibles(Stack<HashMap<String, Object[]>> stackSimbVaraibles) {
		this.stackSimbVaraibles = stackSimbVaraibles;
	}

}
