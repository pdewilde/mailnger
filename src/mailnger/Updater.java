package mailnger;
import com.mysql.jdbc.Driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by Parker on 6/28/2017.
 */
public class Updater {
    public static boolean update(String email, String listName, boolean subscribe) {
        boolean updated = false;

        // Declare variables as null here so can close if necessary given exception
        Connection conn = null;

        // Prepared Statements avoid SQL Injection
        PreparedStatement checkEmail = null;
        PreparedStatement checkList = null;
        PreparedStatement checkSubscription = null;
        PreparedStatement addEmail = null;
        PreparedStatement addSubscription = null;
        PreparedStatement addList = null;
        PreparedStatement removeSubscription = null;

        // The Strings for various Queries. ? is filled in later.
        String checkEmailString = "SELECT EMAIL FROM USERS " +
                "WHERE EMAIL = ?";
        String checkListString = "SELECT LIST_NAME FROM LISTS " +
                "WHERE LIST_NAME = ?";
        String checkSubscriptionString = "SELECT * FROM SUBSCRIPTIONS " +
                "WHERE LIST_NAME = ? AND EMAIL = ?";
        String addEmailString = "INSERT INTO USERS (EMAIL, MEMBER) " +
                "VALUES (?,?)";
        String addSubscriptionString = "INSERT INTO SUBSCRIPTIONS (LIST_NAME, EMAIL) " +
                "VALUES (?,?)";
        String addListString = "INSERT INTO LISTS (LIST_NAME, RESTRICTED) " +
                "VALUES (?,?)";
        String removeSubscriptionString = "DELETE FROM SUBSCRIPTIONS " +
                "WHERE LIST_NAME = ? AND EMAIL = ?";

        try {
            conn = getConnection();

            // Create Prepared Statements
            checkEmail = conn.prepareStatement(checkEmailString);
            checkList = conn.prepareStatement(checkListString);
            checkSubscription = conn.prepareStatement(checkSubscriptionString);
            addEmail = conn.prepareStatement(addEmailString);
            addSubscription = conn.prepareStatement(addSubscriptionString);
            addList = conn.prepareStatement(addListString);
            removeSubscription = conn.prepareStatement(removeSubscriptionString);

            // See if email is already in system
            checkEmail.setString(1, email);
            ResultSet checkEmailResult = checkEmail.executeQuery();
            boolean emailExists = checkEmailResult.next();  // If .next() returns at true, there is
                                                            // at least one entry, so email exists

            // If not we will add it
            if (!emailExists) {
                addEmail.setString(1, email);
                addEmail.setBoolean(2, false); // Users default to non-member
                addEmail.executeUpdate();
            }

            // See if list Exists
            checkList.setString(1, listName);
            ResultSet checkListResult = checkList.executeQuery();
            boolean listExists = checkListResult.next();

            // If list does not exist, create list, assume it is privileged
            if (!listExists) {
                addList.setString(1, listName);
                addList.setBoolean(2, true); // List Defaults to restricted
                addList.executeUpdate();
            }

            // See if user is subscribed to list
            checkSubscription.setString(1, listName);
            checkSubscription.setString(2, email);
            boolean subscribed = checkSubscription.executeQuery().next();

            // If subscribed and wants to unsubscribe, remove
            if(subscribed && !subscribe) {
                removeSubscription.setString(1, listName);
                removeSubscription.setString(2, email);
                removeSubscription.executeUpdate();
            }

            // If unsubscribed and wants to subscribe, add
            if(!subscribed && subscribe) {
                addSubscription.setString(1, listName);
                addSubscription.setString(2, email);
                addSubscription.executeUpdate();
            }
            updated = true;

        } catch (SQLException e) { // Catch block modified from https://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html
            printSQLException(e);
            if(conn != null) {
                System.err.print("There was a problem"); // Should add a more meaningful error message at some point

            }
        } finally {
            try {
                if (checkEmail != null)
                    checkEmail.close();

                if (checkList != null)
                    checkList.close();

                if (checkSubscription != null)
                    checkSubscription.close();

                if (addEmail != null)
                    addEmail.close();

                if (addSubscription != null)
                    addSubscription.close();

                if (addList != null)
                    addList.close();

                if (removeSubscription != null)
                    removeSubscription.close();

                if (conn != null)
                    conn.close();
            } catch (SQLException except) {
                printSQLException(except);
            }
        }
        return updated;
    }

    // Adapted from https://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html
    public static void printSQLException(SQLException ex) {

        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        ((SQLException)e).getSQLState());

                System.err.println("Error Code: " +
                        ((SQLException)e).getErrorCode());

                System.err.println("Message: " + e.getMessage());

                Throwable t = ex.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    // Gets connection to SQL server
    public static Connection getConnection() throws SQLException{
        String[] credentials = readCredentials(); // URL, Username, Password
        Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2]);
        return conn;
    }

    // Reads credentials from credentials.txt file
    // Should be formatted as three lines, URL, Username, Password
    // Stops program is something is wrong with credentials file
    public static String[] readCredentials() {
        String[] credentials = new String[3];
        try {
            Scanner credentialReader = new Scanner(new File("credentials.txt"));
            for (int i = 0; i < 3; i++) {
                if (credentialReader.hasNextLine()) {
                    credentials[i] = credentialReader.nextLine();
                } else {
                    throw new InvalidCredentialsFileException("You only have " + i + " lines, you need 3");
                }
            }
            credentialReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
            System.err.println("credentials.txt not found");
            System.exit(0);
        } catch (InvalidCredentialsFileException e) {
            e.printStackTrace(System.err);
            System.err.println("Message: " + e.getMessage());
            System.err.println("Improperly Formatted Credentials File");
            System.exit(0);
        }
        return credentials;
    }
}
