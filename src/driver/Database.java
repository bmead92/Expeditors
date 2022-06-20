package driver;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import org.sqlite.SQLiteDataSource;

/**
 * Driver class for Expeditor Dev Design and Development Exercise 1.2.
 * Contains main method.
 * 
 * @author Bryce Meadors
 * @Version 20 June 2022
 *
 */
public class Database {
	
	/**
	 * Main method used for user and console interactivity.
	 * @param theArgs  String[] used for console interactions.
	 */
	public static void main(String[] theArgs) {
		String filePath = new File("input.txt").getAbsolutePath();
		establishConnection("jdbc:sqlite:customers.db");
		ArrayList<String> fileData = readFromFile(filePath);
        createDatabase(fileData);
	}
	
	/**
	 * Method establishConnection establishes a connected with SQLite.
	 * 
	 * @param theURL String of the URL we want to connect to. Creates a .db object based on the
	 * name.
	 * @return Connection con - the Connection object
	 */
	public static Connection establishConnection(final String theURL) {
		String url = theURL;
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException E) {
            System.out.print(E.getMessage());
        }
        return con;
	}
	
	/**
	 * Method readFromFile accepts a String of the file path and parses through the file.
	 * As it parses through, it assigns each String in the file to a position in the
	 * ArrayList<String> that will be returned.
	 * 
	 * @param theFilePath	
	 * @return
	 */
	public static ArrayList<String> readFromFile(final String theFilePath) {
		ArrayList<String> fileData = new ArrayList<String>();
		try (Scanner input = new Scanner(Paths.get(theFilePath))) {
			input.useDelimiter("\",\"|\"\"|\"\n|\\u000d");
			while (input.hasNextLine()) {
				String currentString = input.next();
				currentString = currentString.trim();
				currentString = currentString.replaceAll("\\.", "");
				currentString = currentString.replaceAll(",", "");
				currentString = currentString.replaceAll("\"", "");
				//data is formatted First, Last, Street address, City, State, Age
				currentString = currentString.toUpperCase();
				fileData.add(currentString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return fileData;
	}
	
	/**
	 * Method createDatabase creates a SQLite database using the ArrayList<String> of Strings.
	 * 
	 * @param theFileData ArrayList<String> theFileData the ArrayList<String> of Strings that
	 * we are interested in and need to turn into formatted data for our database.
	 */
	public static void createDatabase(final ArrayList<String> theFileData) {
		SQLiteDataSource database = null;
        try {
            database = new SQLiteDataSource();
            database.setUrl("jdbc:sqlite:customers.db");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        
         //Create table statement for customers table.
         
        final String creation = "CREATE TABLE IF NOT EXISTS customers ( "
        						+ "FIRST TEXT NOT NULL, "
        						+ "LAST TEXT NOT NULL, "
                                + "ADDRESS TEXT NOT NULL, "
                                + "CITY TEXT NOT NULL, "
                                + "STATE TEXT NOT NULL, "
                                + "AGE INT NOT NULL)";
        
        try (final Connection connection = database.getConnection();
                final Statement statement = connection.createStatement();) {
                statement.executeUpdate(creation);
            } catch (SQLException e ) {
                     e.printStackTrace();
                     System.exit(0);
            }
        
        /*
         * Every time createDatabase  method runs, the database fills with the information. 
         * This stub of code ensures that it doesn't re-populate each time with duplicate data.
         */
        final String deleter = "DELETE FROM customers";
        
        try (final Connection connection = database.getConnection();
                final Statement statement = connection.createStatement();) {
                statement.executeUpdate(deleter);
            } catch (SQLException e ) {
                     e.printStackTrace();
                     System.exit(0);
            }
        /*
         * In order to make the insertion more general / robust, I decided to use a loop
         * and cycle through filling "?" values. This method can be scaled up easily assuming
         * incoming cleaned data is 10 rows by 6 columns wide with 5 Strings and an int in
         * position 6.
         */
        final String insertQuery = "INSERT INTO customers (FIRST, LAST, ADDRESS, CITY, "
				+ "STATE, AGE) values (?, ?, ?, ?, ?, ?)";
        
	    try (final Connection connection = database.getConnection();
                final Statement statement = connection.createStatement();) {
	    	
	    	final PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
	    	
	    	for(int i = 0; i < theFileData.size(); i += 6) {
	    		prepStatement.setString(1, theFileData.get(i));
	    		prepStatement.setString(2, theFileData.get(i + 1));
	    		prepStatement.setString(3, theFileData.get(i + 2));
	    		prepStatement.setString(4, theFileData.get(i + 3));
	    		prepStatement.setString(5, theFileData.get(i + 4));
	    		prepStatement.setInt(6, Integer.parseInt(theFileData.get(i + 5)));
	    		prepStatement.executeUpdate();
	    	}
	    	
            } catch (SQLException e ) {
                     e.printStackTrace();
                     System.exit(0);
            }
	    
	    /*
	     * A general query that generates the household and number of occupants requirement
	     * per the description.
	     */
        final String generalQuery = "SELECT *, COUNT(ADDRESS) as noOfOccupants "
        							+ "FROM customers "
        							+ "GROUP BY ADDRESS, CITY, STATE";
        
        try (final Connection connection = database.getConnection();
                final Statement statement = connection.createStatement();) {;
               
           final ResultSet generalResults = statement.executeQuery(generalQuery);
           
           System.out.println("Household and number of occupants:" + "\n"); 
           
           while(generalResults.next() ) {
           	String address = generalResults.getString("ADDRESS");
           	String city = generalResults.getString("CITY");
           	String state = generalResults.getString("STATE");
           	address = address.toUpperCase().replaceAll("\\p{Punct}", "");
           	city = city.toUpperCase();
           	state = state.toUpperCase();
           	int occupants = generalResults.getInt("noOfOccupants");
            System.out.println(address + " " + city + ", " + state + "\n"
               				   + "Number of occupants: " + occupants + "\n");
       }
           }
           catch (SQLException e) {
               e.printStackTrace();
               System.exit(0);
           }
	
        /*
         * A more specific query that filters age to only show customers OVER 18 years old
         * (excludes 18). Also sorts by last name then first name.
         */
        final String specificQuery = "SELECT * "
									+ "FROM customers "
									+ "WHERE (AGE > 18) "
									+ "ORDER BY LAST, FIRST ";
        
        try (final Connection connection = database.getConnection();
                final Statement statement = connection.createStatement();) {
        	
            final ResultSet specificResults = statement.executeQuery(specificQuery);
            
            System.out.println("First name, last name, and full address for all customers"
            				  + " over 18 years old:" + "\n");
            
            while (specificResults.next()) {
            	String first = specificResults.getString("FIRST");
            	String last = specificResults.getString("LAST");
                String address = specificResults.getString("ADDRESS");
                String city = specificResults.getString("CITY");
                String state = specificResults.getString("STATE");
                address = address.toUpperCase().replaceAll("\\p{Punct}", "");
                city = city.toUpperCase();
                state = state.toUpperCase();
                int age = specificResults.getInt("AGE");
         
                System.out.println(first + " " + last +  "\n" 
                				  + address + "\n" 
                				  + city + ", " + state + "\n"  
                				  + age + " y/o " +"\n");
            }
		}
    		catch (SQLException e) {
    			e.printStackTrace();
    			System.exit(0);
    		}
	}
}
