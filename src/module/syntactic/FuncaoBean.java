package module.syntactic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import module.ParametrosBean;

public class FuncaoBean {

	private String name;
	private String type;
	private List<ParametrosBean> parametros;
	private String lbRetutn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ParametrosBean> getParametros() {
		if ( parametros == null)
			parametros = new ArrayList<>();
		return parametros;
	}

	public void setParametros(List<ParametrosBean> parametros) {
		this.parametros = parametros;
	}

	public String getLbRetutn() {
		return lbRetutn;
	}

	public void setLbRetutn(String lbRetutn) {
		this.lbRetutn = lbRetutn;
	}

	public ParametrosBean findParam(String id){
		for (ParametrosBean p : parametros){
			if ( p.tokenId.equals(id))
				return p;
		}
		return null;
	
	}
	
}
