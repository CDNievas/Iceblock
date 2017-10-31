package iceblock.auxiliar;

public class DELBuilder {
	
	private String table;
	
	public <T> String delete(Class<T> aClass) {
		this.table = Auxiliar.getTableName(aClass);
		return "DELETE FROM ";
	}
	
	public String table(){
		return this.table + "\n";
	}
	
	public String where(String xql){
		
		String str = "";
		
		if(xql == null) {
			return str;
		} else if (xql.isEmpty() || xql.equals("")) {
			return str;
		} else {
			str = "WHERE " + xql;
			return str;
		}
		
	}

}
