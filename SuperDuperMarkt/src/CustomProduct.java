import java.util.Date;
import java.util.Random;


// A Product that has a random quality change and does not expire
// Only when the quality value of the product is lower than the lower quality limit,
// the product should be removed from the shelf
public class CustomProduct extends Product {

    // Random integer generator within a given interval [min, max]
    Random rand = new Random();
    int max = 10;
    int min = -10;

    // Constructor
    public CustomProduct(String description, int quality, double basePrice, Date expiryDate) {
        super(description, quality, basePrice, expiryDate);

        int randomNum = rand.nextInt((max - min) + 1) + min;
        this.setQualityChange(randomNum);
        this.setQualityChangeRate(2);
        this.setQualityLowerLimit(-50);
        this.setQualityUpperLimit(50);
        this.setHasDailyPrice(true);
        this.setDoesExpire(false);
        this.setMinimumDays(0);
        this.setMaximumDays(Integer.MAX_VALUE);
        this.setVariableQuality(true);
    }

    @Override
    public boolean updateProduct(Date currentDate) {
        boolean qualityChangeUpdated = this.updateQualityChange();
        boolean anyChange = super.updateProduct(currentDate);
        if (anyChange || qualityChangeUpdated) {
            return true;
        } else {
            return false;
        }
    }

    // Update the quality change and log it to the console if any change to the quality was made
    public boolean updateQualityChange() {
        int oldQualityChange = this.getQualityChange();

        int randomNum = rand.nextInt((max - min) + 1) + min;
        super.setQualityChange(randomNum);

        if (oldQualityChange == this.getQualityChange()) {
            return false;
        } else {
            System.out.println("   -> Quality change of \"" + this.getDescription() + "\" is set to " + this.getQualityChange());
            return true;
        }
    }

}
