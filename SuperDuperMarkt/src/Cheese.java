import java.util.Date;


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
