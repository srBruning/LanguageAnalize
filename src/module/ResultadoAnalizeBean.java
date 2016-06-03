package module;

import java.util.List;

public class ResultadoAnalizeBean{
	String codigo;
	private List<CausaErro> erros;
	
	/**
	 * 
	 * @param codigo 
	 * @param erros
	 */
	public ResultadoAnalizeBean(String codigo, List<CausaErro> erros) {
		this.codigo = codigo;
		this.erros = erros;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public List<CausaErro> getErros() {
		return erros;
	}
	public void setErros(List<CausaErro> erros) {
		this.erros = erros;
	}
	
}