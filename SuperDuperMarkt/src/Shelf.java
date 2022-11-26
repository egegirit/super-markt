import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;


// A shelf that can store an ArrayList of products
public class Shelf {

    // The products that are inside the shelf
    ArrayList<Product> products = new ArrayList<>();

    // Check the given product if it is suitable to store in the shelf and
    // add the product to the shelf
    public void addProductToShelf(Product product) {
        if (product != null) {
            System.out.println("\nTrying to add product into the shelf: " + product.listProductInfoForShelf());
            Date dateAddedToShelf = Main.currentDate;

            // Initialize the shelf date of the product
            product.setShelfDate(dateAddedToShelf);

            // Only add the product to the shelf, when (if it can expire) it is not already expired and its quality is valid
            if (!product.shouldBeRemovedFromShelf(Main.currentDate)) {
                products.add(product);
                System.out.println("  (+) Added item to the shelf: " + product.listProductInfoForShelf());
            } else {
                System.out.println("    Item \"" + product.getDescription() + "\" is not suitable to store, skipping");
            }
        }
    }

    // Remove the given product from the shelf
    public void removeProductFromShelf(Product product) {
        System.out.println("  (-) Removing item from shelf: " + product.listProductInfoForShelf());
        products.remove(product);
    }

    // List all the products in the shelf for the current day
    public void listProductsOfShelf() {
        System.out.println("Listing the products in the shelf (" + new SimpleDateFormat("dd-MM-yyyy").format(Main.currentDate) + ")");
        System.out.println("  (Item Number | Description | Quality | Price | Expiry Date)");

        int item_count_index = 1;
        for (Product p : products) {
            System.out.print("  [" + item_count_index + "] ");
            System.out.println(p.listProductInfoForShelf());
            item_count_index += 1;
        }
    }

    // Update the attributes of all the products in the shelf for the current day,
    // delete all the items that should be removed from the shelf
    public void updateShelf(Date currentDate) {
        // Update all the product information
        for (int index = 0; index < products.size(); index++) {
            products.get(index).updateProduct(currentDate);
        }
        // After updating products, remove all the items that should be removed from the shelf
        for (int index = 0; index < products.size(); index++) {
            if (products.get(index).shouldBeRemovedFromShelf(currentDate)) {
                removeProductFromShelf(products.get(index));
            }
        }
    }

    // Update the contents of the shelf by checking whether the products should be removed
    public void updateAndList(Shelf shelf, Date currentDate) {
        shelf.updateShelf(currentDate);
        shelf.listProductsOfShelf();
    }

}
