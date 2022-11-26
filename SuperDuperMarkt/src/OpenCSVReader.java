import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVReader;

// The class that reads the products from a csv file
public class OpenCSVReader {

    // Store the products in the csv file in an ArrayList
    public static ArrayList<Product> productsInFile = new ArrayList<>();

    // Read the contents of the CSV file and store the products in an ArrayList
    public static ArrayList<Product> readCSV(String filePath) {
        // The delimiter that is used to separate the items in the CSV file
        char separator = ';';

        try {
            // Create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            // Create csv reader
            CSVReader csvReader = new CSVReader(reader, separator);
            // Read all records (lines) at once
            List<String[]> records = csvReader.readAll();
            int lineCountOfFile = records.size();
            int amountOfProductsInFile = lineCountOfFile - 1;
            System.out.println("  Amount of products in the CSV file: " + amountOfProductsInFile + "\n");

            // Define the arguments of the constructor of our Product class
            Class[] parameterType = new Class[4];
            parameterType[0] = String.class;
            parameterType[1] = int.class;
            parameterType[2] = double.class;
            parameterType[3] = Date.class;

            try {
                // Iterate through the list of records
                int indexOfType = 0;
                int indexOfDescription = 0;
                int indexOfQuality = 0;
                int indexOfPrice = 0;
                int indexOfExpiryDate = 0;
                for (int i = 0; i < lineCountOfFile; i++) {

                    // Read the header (first line of CSV file) to determine the positions of the attributes
                    if (i == 0) {
                        for (int j = 0; j < records.get(0).length; j++) {

                            if (records.get(0)[j].equalsIgnoreCase("Type")) {
                                indexOfType = j;
                            }
                            if (records.get(0)[j].equalsIgnoreCase("Description")) {
                                indexOfDescription = j;
                            }
                            if (records.get(0)[j].equalsIgnoreCase("Quality")) {
                                indexOfQuality = j;
                            }
                            if (records.get(0)[j].equalsIgnoreCase("Price")) {
                                indexOfPrice = j;
                            }
                            if (records.get(0)[j].equalsIgnoreCase("Expiry Date")) {
                                indexOfExpiryDate = j;
                            }
                        }
                    }
                    // For every product in the CSV file, create a Product object and store it in an ArrayList
                    if (i != 0) {
                        int quality = 0;
                        double price = 0;
                        Date date = new Date();

                        // Cast the read contents (parameters of contstructor of Product) from string accordingly
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            quality = Integer.parseInt(records.get(i)[indexOfQuality]);
                            price = Double.parseDouble(records.get(i)[indexOfPrice]);
                            date = formatter.parse(records.get(i)[indexOfExpiryDate]);
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            Object o = Class.forName("" + records.get(i)[0]).getDeclaredConstructor(parameterType).newInstance(records.get(i)[indexOfDescription], quality, price, date);
                            productsInFile.add((Product) o);
                        } catch (ClassNotFoundException e) {
                            System.out.println("The given product type \"" + records.get(i)[0] + "\" is not supported!");
                            System.out.println("Skipping the undefined type...");
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error reading file " + filePath);
                ex.printStackTrace();
            }

            // Close readers
            csvReader.close();
            reader.close();

        } catch (IOException ex) {
            System.out.println("Error opening file " + filePath);
            ex.printStackTrace();
        }

        return productsInFile;
    }

}
