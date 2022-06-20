package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.Test;
import driver.Database;

/**
 * Class ConnectionTest tests the SQLite connection.
 * 
 * @author Bryce Meadors
 * @version 20 June 2022
 */
public class ConnectionTest {
	
	/**
	 * connectionTestNotNull method tests to ensure that the connection to SQLite is not null.
	 */
	@Test
	public void connectionTestNotNull() {
		Database db = new Database();
		Connection validConnection = db.establishConnection("jdbc:sqlite:customers.db");
		String customerDatabase = validConnection.toString();
		assertNotNull(customerDatabase);
	}
	
	/**
	 * connectionTestNull method tests to ensure that the connection to SQLite is null.
	 */
	@Test
	public void connectionTestNull() {
		Database db = new Database();
		String nullCheck = null;
		Connection nullConnection = db.establishConnection(nullCheck);
		assertNull(nullConnection);
	}

}
