import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


// The class that creates SQLite database to store products
// and read the products from a database file
public class SQLConnection {

    // Connect to the database with the given name
    public static void connect(String databaseName) {
        Connection conn = null;
        try {
            // DB parameters
            String url = "jdbc:sqlite:" + databaseName;
            // Create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Create a new database with the given name
    public static void createNewDatabase(String databaseName) {

        String url = "jdbc:sqlite:" + databaseName;

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Driver name: " + meta.getDriverName());
                System.out.println("Database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Create a new table inside the database with the given name
    public static void createNewTable(String databaseName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + databaseName;

        // SQL statement for creating a new table
        // Type;Description;Quality;Price;Expiry Date
        String sql = """
                CREATE TABLE IF NOT EXISTS products (
                 id INTEGER PRIMARY KEY NOT NULL,
                 type TEXT NOT NULL,
                 description TEXT NOT NULL,
                 quality INTEGER NOT NULL,
                 price REAL NOT NULL,
                 expiry_date TEXT NOT NULL
                );""";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Products table created");
    }

    // Insert a record into the table
    public static void insert(String databaseName, int primaryKey, String type, String description, int quality, double price, Date expiryDate) {
        String url = "jdbc:sqlite:" + databaseName;
        String sql = "INSERT INTO products(id, type, description, quality, price, expiry_date) VALUES(?,?,?,?,?,?)";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, primaryKey);
                pstmt.setString(2, type);
                pstmt.setString(3, description);
                pstmt.setInt(4, quality);
                pstmt.setDouble(5, price);
                pstmt.setString(6, new SimpleDateFormat("dd-MM-yyyy").format(expiryDate));
                pstmt.executeUpdate();
                System.out.println("Record inserted into table");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete a record of the table via primary key
    public void delete(String databaseName, int id) {
        String url = "jdbc:sqlite:" + databaseName;

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the corresponding parameter
            pstmt.setInt(1, id);
            // Execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // List all the products in the table and store them in an Arraylist
    public static ArrayList<Product> selectAll(String databaseName) {
        String url = "jdbc:sqlite:" + databaseName;
        String sql = "SELECT * FROM products";

        ArrayList<Product> productsInSQL = new ArrayList<>();

        // Define the arguments of the constructor of our Product class
        Class[] parameterType = new Class[4];
        parameterType[0] = String.class;
        parameterType[1] = int.class;
        parameterType[2] = double.class;
        parameterType[3] = Date.class;

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                // loop through the result set
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    String description = rs.getString("description");
                    int quality = rs.getInt("quality");
                    double price = rs.getDouble("price");
                    String expiryDate = rs.getString("expiry_date");

                    System.out.println(
                            id + "\t" +
                                    type + "\t" +
                                    description + "\t" +
                                    quality + "\t" +
                                    price + "\t" +
                                    expiryDate
                    );

                    // Convert the type of expiryDate from String to Date
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Date date = formatter.parse(expiryDate);

                    // Create the product object
                    try {
                        Object o = Class.forName(type).getDeclaredConstructor(parameterType).newInstance(description, quality, price, date);
                        productsInSQL.add((Product) o);
                    } catch (ClassNotFoundException e) {
                        System.out.println("The given product type " + type + " is not supported!");
                        System.out.println("Skipping the undefined type...");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ParseException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return productsInSQL;

    }

}
