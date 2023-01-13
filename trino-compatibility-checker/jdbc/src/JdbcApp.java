import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class JdbcApp {
    private static final String JDBC_CONNECTION = "jdbc:trino://localhost:port/catalog";
    private static final String SCHEMA = "schema";
    private static final List<String> TABLES = Arrays.asList("table1");

    public static void main(String[] args) {
        System.out.println("Application starting...");
        try (Connection con = DriverManager.getConnection(JDBC_CONNECTION,"jdbc-check", null)) {
            Driver driver = DriverManager.getDriver(JDBC_CONNECTION);
            System.out.println("Connected using: " + driver + " (version: " + driver.getMajorVersion() + "." + driver.getMinorVersion() + ").");
            try (Statement stmt = con.createStatement()){
                for (String tbl : TABLES) {
                    String query = "select * from " + SCHEMA + "." + tbl + " limit 1";
                    System.out.println("Querying " + query);
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (rs.next()) {
                            System.out.println("------------ROW------------");
                            for (int i = 1; i <= columnsNumber; i++) {
                                System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i) + "(Type: " + rsmd.getColumnTypeName(i) + ")");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Application encountered SQLException.");
            e.printStackTrace();
        }

        System.out.println("Application completed.");
    }
}
