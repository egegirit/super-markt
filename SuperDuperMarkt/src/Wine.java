import java.util.Date;


// Wine does not expire, its quality increments every 10-th day,
// minimum acceptable quality value is 0, maximum quality limit is 50,
// wine does not have a daily price and its price is fixed
public class Wine extends Product {

    public Wine(String description, int quality, double basePrice, Date expiryDate) {
        super(description, quality, basePrice, expiryDate);
        this.setQualityChange(1);
        this.setQualityChangeRate(10);
        this.setQualityLowerLimit(0);
        this.setQualityUpperLimit(50);
        this.setHasDailyPrice(false);
        this.setDoesExpire(false);
        this.setVariableQuality(false);
    }

    public boolean expiryDateIsValid(Date expiryDate, Date currentDate) {
        return true;
    }

}
