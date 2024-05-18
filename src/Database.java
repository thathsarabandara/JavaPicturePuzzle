


// Import necessary libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Database class for handling database operations
public final class Database {
    
    // Database connection variables
    Connection con;
    PreparedStatement pst;
    ResultSet rst;
    
    // Variable for storing the name to be displayed
    String nameDisplay;

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/picturepuzzle";
    String user = "root";
    String password = "";

    // Constructor to establish connection when the object is created
    public Database() {
        connect();
    }

    // Method to establish database connection
    public void connect() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                // Connect to the database
                con = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Method to insert data into the database
    public void sendDataToDatabase(String name, int time) {
        try {
            // Prepare the SQL statement for insertion
            pst = con.prepareStatement("INSERT INTO leaderboard (name, time) VALUES (?, ?)");
            pst.setString(1, name); // Set the name parameter
            pst.setInt(2, time);    // Set the time parameter
            pst.executeUpdate();    // Execute the SQL statement
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Method to retrieve the name of a player based on their rank
    public String getPlace(int place) {
        try {
            // Calculate the actual index in the leaderboard (0-indexed)
            int actual = place - 1;
            // Prepare the SQL statement to select the name based on rank
            pst = con.prepareStatement("SELECT name FROM leaderboard ORDER BY time LIMIT 1 OFFSET ?");
            pst.setInt(1, actual);  // Set the offset parameter
            rst = pst.executeQuery();  // Execute the SQL query
            
            // Check if a result is found
            if (rst.next()) {
                nameDisplay = rst.getString("name");  // Retrieve the name from the result set
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nameDisplay;  // Return the retrieved name
    }
}
