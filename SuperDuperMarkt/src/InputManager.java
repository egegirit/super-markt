import java.util.ArrayList;


public class InputManager {

    // Initialize the products based on the desired input type
    public static ArrayList<Product> getProductsFrom(InputType inputType) throws ClassNotFoundException {

        // Store the imported products
        ArrayList<Product> products = new ArrayList<>();

        if (Main.currentDate == null) {
            System.out.println("Date cannot be null!");
            System.exit(1);
        }

        System.out.println("Selected input type: " + inputType.toString() + "\n");

        // Read the products via created objects from code
        if (inputType.equals(InputType.SAMPLE)) {
            // Create some sample products
            Cheese cheese1 = new Cheese("Cheese Not In Interval", 45, 10.0, Utility.addDaysToCurrentDate(Main.currentDate, 15));
            Cheese cheese2 = new Cheese("Cheese Bad Quality", 10, 7.0, Utility.addDaysToCurrentDate(Main.currentDate, 55));
            Cheese cheese3 = new Cheese("Cheese Valid", 40, 5.0, Utility.addDaysToCurrentDate(Main.currentDate, 65));
            Wine wine1 = new Wine("Wine Valid", 15, 30.0, Utility.addDaysToCurrentDate(Main.currentDate, 30));
            Wine wine2 = new Wine("Wine Low Quality", -5, 20.0, Utility.addDaysToCurrentDate(Main.currentDate, 5));
            Wine wine3 = new Wine("Wine Expired", 3, 10.0, Utility.addDaysToCurrentDate(Main.currentDate, -2));
            CustomProduct custom1 = new CustomProduct("Custom Expired", 3, 10.0, Utility.addDaysToCurrentDate(Main.currentDate, -2));
            CustomProduct custom2 = new CustomProduct("Custom Low Quality", -100, 15.0, Utility.addDaysToCurrentDate(Main.currentDate, 5));
            CustomProduct custom3 = new CustomProduct("Custom Valid", 10, 12.0, Utility.addDaysToCurrentDate(Main.currentDate, 50));

            // Add the products to the list
            products.add(cheese1);
            products.add(cheese2);
            products.add(cheese3);
            products.add(wine1);
            products.add(wine2);
            products.add(wine3);
            products.add(custom1);
            products.add(custom2);
            products.add(custom3);

        } // Read the products from a CSV file
        else if (inputType.equals(InputType.CSV)) {

            // Name of the CSV file to read
            String filePath = "Products.csv";

            System.out.println("  Reading CSV file from " + filePath);
            products = OpenCSVReader.readCSV(filePath);

        } // Initialise and read the products from a newly created SQLite database file
        else if (inputType.equals(InputType.SQL)) {
            Class.forName("org.sqlite.JDBC");

            // Create database and a products table in it
            String databaseName = "products.db";
            SQLConnection.createNewDatabase(databaseName);
            SQLConnection.connect(databaseName);
            SQLConnection.createNewTable(databaseName);

            // Insert products to the products table of the database
            SQLConnection.insert(databaseName, 0, "Wine", "Wine Valid", 10, 15, Utility.addDaysToCurrentDate(Main.currentDate, 50));
            SQLConnection.insert(databaseName, 1, "Wine", "Wine Valid Highest Quality", 50, 15, Utility.addDaysToCurrentDate(Main.currentDate, 50));
            SQLConnection.insert(databaseName, 2, "Wine", "Wine Low Quality", -5, 10.5, Utility.addDaysToCurrentDate(Main.currentDate, 40));
            SQLConnection.insert(databaseName, 3, "Wine", "Wine Expired", 2, 10.5, Utility.addDaysToCurrentDate(Main.currentDate, -3));
            SQLConnection.insert(databaseName, 4, "Cheese", "Cheese Valid", 50, 5, Utility.addDaysToCurrentDate(Main.currentDate, 70));
            SQLConnection.insert(databaseName, 5, "Cheese", "Cheese Valid 2", 45, 7, Utility.addDaysToCurrentDate(Main.currentDate, 55));
            SQLConnection.insert(databaseName, 6, "Cheese", "Cheese Not In Interval", 40, 3, Utility.addDaysToCurrentDate(Main.currentDate, 120));
            SQLConnection.insert(databaseName, 7, "Cheese", "Cheese Low Quality", 12, 7, Utility.addDaysToCurrentDate(Main.currentDate, 65));
            SQLConnection.insert(databaseName, 8, "Cheese", "Cheese Soon Bad Quality", 31, 3, Utility.addDaysToCurrentDate(Main.currentDate, 60));
            SQLConnection.insert(databaseName, 9, "Cheese", "Cheese Expired and Low Quality", 10, 5, Utility.addDaysToCurrentDate(Main.currentDate, -5));
            SQLConnection.insert(databaseName, 10, "CustomProduct", "Custom Valid", 10, 5, Utility.addDaysToCurrentDate(Main.currentDate, 40));
            SQLConnection.insert(databaseName, 11, "CustomProduct", "Custom Low Quality", -5, 3, Utility.addDaysToCurrentDate(Main.currentDate, 50));

            // Read and initialize products from the database file
            products = SQLConnection.selectAll(databaseName);

        }

        return products;

    }

}
