package module;
public class PlaceCod{

	public String cod,  place, tipo;
	
	public PlaceCod(){	}
	
	public PlaceCod(PlaceCod x){
		cod = x.cod;
		place = x.place;
		tipo = x.tipo;
	}
	
	public void addCods(String...cods){
		if (cods!= null && cods.length>0)
			cod = "";
			for( String c : cods){
				cod += c+"\n";
			}
	}
}