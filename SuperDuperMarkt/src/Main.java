import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Main {

    public static Date currentDate;

    // TODO: Tests, Design patterns,

    // Driver code
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {

        // Initialize the start day
        currentDate = new Date();

        // InputType.SQL  InputType.CSV  InputType.CONSOLE
        InputType inputType = InputType.SQL;

        // Initialize products from the given source
        ArrayList<Product> products = InputManager.getProductsFrom(inputType);

        // Show the start day
        System.out.println("==== Start day (" + new SimpleDateFormat("dd-MM-yyyy").format(currentDate) + ") ====");

        // Create a shelf to store and manage the products
        Shelf shelf = new Shelf();

        // Add the products to the shelf
        for (Product p : products) {
            shelf.addProductToShelf(p);
        }

        // Simulate the next x days
        int amountOfDaysToSimulate = 300;
        for (int i = 0; i < amountOfDaysToSimulate; i++) {
            // Adding products to the shelf at a specific day is possible
            // if(i == 2){
            //     Cheese cheese3 = new Cheese("Cheese that is added at day " + (i+1), 40, 10.0, Utility.addDaysToCurrentDate(currentDate, 60));
            //     shelf.addProductToShelf(cheese3);
            // }

            currentDate = Utility.nextDay(currentDate);
            Utility.updateShelf(shelf, currentDate);
            // If you want to see the output slowly
            // Thread.sleep(1000);
        }
    }
}