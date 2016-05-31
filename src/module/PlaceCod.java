package module;
public class PlaceCod{

	public String cod,  place, tipo, erro;
	
	public PlaceCod(){	}
	
	public PlaceCod(PlaceCod x){
		cod = x.cod;
		place = x.place;
		tipo = x.tipo;
		erro = x.erro;
	}
	
	public void addCods(String...cods){
		if (cods!= null && cods.length>0)
			cod = "";
			for( String c : cods){
				if( c!=null)
					cod += c+"\n";
			}
	}
}