/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import driver.Database;

/**
 * ArrayListTest class for testing the array list in the database.
 * 
 * @author Bryce Meadors
 * @version 20 June 2022
 */
public class ArrayListTest {

	/**
	 * arrayListTestValid ensures that the file is being read properly and matches a method
	 * call to the readFromFile method.
	 */
	@Test
	public void arrayListTestValid() {
		String filePath = new File("input.txt").getAbsolutePath();
		Database db = new Database();
		ArrayList<String> testMe = db.readFromFile(filePath);
		assertEquals(testMe, db.readFromFile(filePath));
	}
	
	/**
	 * arrayListTestNull tests that a null ArrayList<String> is in fact null.
	 */
	@Test
	public void arrayListTestNull() {
		ArrayList<String> testMe = null;
		assertNull(testMe);
	}
}
