package org.avida.hime.hime.SQL;
import java.sql.*;
public class SQLConnection {
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String USER = "himehost";
    static final String PASS = "mermaiddancer";
    static final String QUERY = "SELECT id, testString FROM testTable";
    private Statement stmt;
    public SQLConnection(){
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY);) {
            // Extract data from result set
            /**while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", First: " + rs.getString("testString"));
            }
        **/} catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public Statement getStatement(){
        return stmt;
    }
    public String getQuery(){
        return QUERY;
    }
}
