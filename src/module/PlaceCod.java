package module;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import module.Token.TypeToken;

public class PlaceCod{

	public String cod,  place, tipo;
	public CausaErro erro;
	public String  returnType;
	public Integer address = null;
	
	public PlaceCod(){	}
	
	public PlaceCod(PlaceCod x){
		cod = x.cod;
		place = x.place;
		tipo = x.tipo;
		erro = x.erro;
		address = x.address;
		returnType = x.returnType;
	}

	public void addCods(String...cods){
		cods = ArrayUtils.add(cods, 0, cod );
		setCods(cods);
	}
	
	
	public void setCods(String...cods){
		if (cods!= null && cods.length>0)
			cod = "";
			for( String c : cods){
				if( c!=null)
					cod += c+"\n";
			}
	}
}