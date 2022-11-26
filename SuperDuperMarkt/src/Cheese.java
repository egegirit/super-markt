import java.util.Date;

// Cheese product
// Expiry date can be 50 or 100 days in the future, any other value is not accepted by the shelf.
// Cheese loses 1 quality every day and it has a daily price.
// Minimum viable quality of a cheese is 30, if the quality goes under 30, it will be removed from shelf
public class Cheese extends Product{

    // Constructor
    public Cheese(String description, int quality, double basePrice, Date expiryDate) {
        super(description, quality, basePrice, expiryDate);
        this.setQualityChange(-1);
        this.setQualityChangeRate(1);
        this.setQualityLowerLimit(Integer.MIN_VALUE);
        this.setQualityUpperLimit(Integer.MAX_VALUE);
        this.setQualityLowerValidLimit(30);
        this.setHasDailyPrice(true);
        this.setDoesExpire(true);
        this.setMinimumDays(50);
        this.setMaximumDays(100);
        this.setVariableQuality(false);
    }

}
