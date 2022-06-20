package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Class TestRunner is used for running the test suite.
 * 
 * @author Bryce Meadors
 * @version 20 June 2022
 *
 */
public class TestRunner {
	
	/**
	 * Main method used for user interactivity and console interactivity.
	 * 
	 * @param theArgs			String array used for console.
	 */
   public static void main(String[] theArgs) {
      final Result result = JUnitCore.runClasses(AllTests.class);
      
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println("Result == " + result.wasSuccessful());
   }
}