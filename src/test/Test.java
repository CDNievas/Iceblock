package test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;

import iceblock.IBlock;
import iceblock.connection.ConnectionManager;
import models.*;

public class Test {

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		
		// Connect to DB
		if (ConnectionManager.idExists("hsqldb")){
			return ConnectionManager.getConnection();
		} else {
			ConnectionManager.create("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/","sa","","hsqldb");
			ConnectionManager.changeConnection("hsqldb");
			return ConnectionManager.getConnection();
		}
		
	}
	
	// Test OneToOne
	
	@org.junit.Test
	public void query_without_XQL_in_EAGER() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		Connection conn = this.getConnection();
		List<Person> list = IBlock.select(conn, Person.class, null);
		
		Assert.assertEquals(list.size(),11);
		Assert.assertEquals(list.get(0).getName(),"Sarah");
		Assert.assertEquals(list.get(1).getAddress().getStreet(),"False");
		
		Assert.assertNull(list.get(7).getAddress());
		Assert.assertNull(list.get(9).getAddress());
		
	}
	
	@org.junit.Test
	public void query_with_XQL_in_EAGER() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		Connection conn = this.getConnection();
		
		String xql = "address.id_address = 0";
		List<Person> list = IBlock.select(conn, Person.class, xql);
		
		Assert.assertEquals(list.size(),3);
		
		xql = "person.id_address = 7";
		list = IBlock.select(conn, Person.class, xql);
		
		Assert.assertEquals(list.size(), 0);
		
		xql = "person.id_person = 7";
		list = IBlock.select(conn, Person.class, xql);
		Person x = list.get(0);
		
		Assert.assertNull(x.getAddress());
		
		xql = "person.id_person = 9";
		list = IBlock.select(conn, Person.class, xql);
		x = list.get(0);
		
		Assert.assertNull(x.getAddress());
		
	}
	
	@org.junit.Test
	public void query_without_XQL_in_LAZY() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		Connection conn = this.getConnection();
		
		List<Occupation> list = IBlock.select(conn, Occupation.class, null);
		
		Assert.assertEquals(list.size(), 8);
		
		Assert.assertNotNull(list.get(0).getTypeOccupation());
		Assert.assertNull(list.get(6).getTypeOccupation());
		Assert.assertNull(list.get(7).getTypeOccupation());

		
	}
	
	@org.junit.Test
	public void query_with_XQL_in_LAZY() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		Connection conn = this.getConnection();
		
		String xql = "occupation.id_type_occupation = 1";
		List<Occupation> list = IBlock.select(conn, Occupation.class, xql);
		
		Assert.assertEquals(list.size(), 3);
		
		for(Occupation x : list) {
			Assert.assertNotNull(x.getTypeOccupation());
		}
		
		xql = "occupation.id_occupation = 6";
		list = IBlock.select(conn, Occupation.class, xql);

		Assert.assertNull(list.get(0).getTypeOccupation());
		
		xql = "occupation.id_occupation = 7";
		list = IBlock.select(conn, Occupation.class, xql);

		Assert.assertNull(list.get(0).getTypeOccupation());
		
	}	
	
}
