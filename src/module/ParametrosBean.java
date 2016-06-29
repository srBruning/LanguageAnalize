package module;

import java.util.ArrayList;

public class ParametrosBean {

	public ParametrosBean(){
		
	}
	public ParametrosBean(	 String tokenId, String type,  Integer addres){
		this.type = type;
		this.tokenId = tokenId;
		this.addres = addres;
	}
	public String type;
	public String tokenId;
	public String varTemp;
	public Integer addres;
	
	@Override
	public boolean equals(Object obj) {
		if ( tokenId!=null)
			return tokenId.equals(obj);
		return super.equals(obj);
	}
	
}
