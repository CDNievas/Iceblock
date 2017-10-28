import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import iceblock.*;
import iceblock.connection.ConnectionManager;
import models.*;

public class Main {
	
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
	
		// Connect to DB
		ConnectionManager.create("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/","sa","","hsqldb");
		ConnectionManager.changeConnection("hsqldb");
		Connection conn = ConnectionManager.getConnection();
		
		
		Person p = IBlock.find(conn, Person.class, 2);
		System.out.println("Nombre:" + p.getName());
		List<Occupation> occup = p.getOccupations();
		
		System.out.println("Objetos encontrados: " + occup.size());
		for(Occupation oa : occup) {
			System.out.println(oa.getDescription() + " - " + oa.getIdOccupation());
		}
		
		Occupation o = IBlock.find(conn, Occupation.class, 4);
		List<Person> pers = o.getPersons();
		System.out.println("Objetos encontrados: " + pers.size());
		
		for(Person pa : pers) {
			System.out.println(pa.getName());
		}
		
		IBlock.insert(conn,Person.class,p,2);
		
	}
	
}
