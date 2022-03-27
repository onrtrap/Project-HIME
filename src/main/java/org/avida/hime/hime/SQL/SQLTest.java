package org.avida.hime.hime.SQL;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

import static org.avida.hime.hime.Main.readConfig;

public class SQLTest {
    static JSONObject json;

    static {
        try {
            json = readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String DB_URL = json.getString("DB");
    static final String USER = json.getString("SQLUsername");
    static final String PASS = json.getString("SQLPassword");
    static final String QUERY = "SELECT id, testString FROM testTable";

    public static void main(String[] args) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY)) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", First: " + rs.getString("testString"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
