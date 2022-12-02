import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Main {

    // The Date variable which stores the current date
    // TODO: Localdate
    // TODO: Maven/Gradle dependencies
    // TODO: IDE meldungen
    public static Date currentDate;

    // Driver code
    public static void main(String[] args) {

        // Initialize the start day as today
        currentDate = new Date();

        // Possible input options are:
        // InputType.SQL  InputType.CSV  InputType.SAMPLE
        // TODO: args
        InputType inputType = InputType.SAMPLE;

        // Initialize products from the given source
        ArrayList<Product> products =  InputManager.getProductsFrom(inputType);

        // Show the start day on the console
        System.out.println("==== Start day (" + new SimpleDateFormat("dd-MM-yyyy").format(currentDate) + ") ====");

        // Create a shelf to store and manage the products
        Shelf shelf = new Shelf();

        // Add the initialized products to the shelf
        for (Product p : products) {
            shelf.addProductToShelf(p);
        }

        // Simulate the next x days
        int amountOfDaysToSimulate = 300;
        for (int i = 0; i < amountOfDaysToSimulate; i++) {
            // Adding products to the shelf at a specific day is possible
            // if(i == 2){
            //     Cheese cheese = new Cheese("Cheese that is added at day " + (i+1), 40, 10.0, Utility.addDaysToCurrentDate(currentDate, 60));
            //     shelf.addProductToShelf(cheese);
            // }

            currentDate = Utility.nextDay(currentDate, i+1);
            shelf.updateAndList(shelf, currentDate);
            // If you want to see the output slowly
            // Thread.sleep(1000);
        }
    }
}