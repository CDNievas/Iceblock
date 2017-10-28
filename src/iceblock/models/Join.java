package iceblock.models;

public class Join {
	
	public String table;
	public String colIn;
	public String colOut;
	
	public Join(String _table, String _colIn, String _colOut){
		this.table = _table;
		this.colIn = _colIn;
		this.colOut = _colOut;
	}
	
}
