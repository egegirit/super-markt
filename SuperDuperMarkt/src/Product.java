import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Product {
    // The description of the product
    private String description;

    // The current quality of the product (higher is better)
    private int quality;

    // The start quality of the product, when the product was created.
    // With the help of the start quality, it is possible to calculate the quality of all non-variable quality
    // products for any date
    private int startQuality;

    // The change that will be applied to the current quality of the product when the quality check day has come
    private int qualityChange;

    // How often the quality should be updated
    private int qualityChangeRate;

    // If the products quality is below the lower limit value and the products can expire,
    // it should be removed from the shelf
    private int qualityLowerLimit;

    // The maximum quality value a product can have
    private int qualityUpperLimit;

    // The base price of the product without the consideration of its quality
    private double basePrice;

    // The price of the quality of the product based on its quality (Sell price)
    private double price;

    // If the products price can change and it does not have a fixed price
    private boolean hasDailyPrice;

    // Determines if the product can expire.
    // Products that doesn't expire can have an expiry date in the past
    private boolean doesExpire;

    // The date that the product will expire and should be removed from the shelf, if the product can expire
    private Date expiryDate;

    // The date that the product is added to the shelf
    // This value is used to calculate the quality of products by
    // calculating the difference between current date and the shelf Date
    private Date shelfDate;

    // The minimum amounts of days that the product should not expire
    private int minimumDays;

    // The maximum amounts of days that the product should not expire
    private int maximumDays;

    // If the products quality change rate is not fixed
    private boolean variableQuality;

    // Constructor
    public Product(String description, int quality, double basePrice, Date expiryDate) {
        setDescription(description);
        setQuality(quality);
        setBasePrice(basePrice);
        setExpiryDate(expiryDate);
        setStartQuality(quality);
        setPrice(this.getBasePrice() + 0.1 * this.getQuality());
        setMinimumDays(0);
        setMaximumDays(Integer.MAX_VALUE);
    }

    // Show the main attributes of the product in a compact form in the console
    public String listProductInfoForShelf() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String expiryDateString = formatter.format(this.getExpiryDate());
        return "(" + this.getDescription() + " | " + this.getQuality() + " | " + this.getPrice() + " | " + expiryDateString + ")";
    }

    // Update the quality of the product based on the daily quality change variable and the day it was put to the shelf
    public boolean updateQualityOfProduct(Date currentDate) {
        // Calculate the time difference in milliseconds
        long differenceInMilliseconds = -1;

        // Handle exception in case currentDate or shelfDate or current date is null
        try {
            differenceInMilliseconds = currentDate.getTime() - this.getShelfDate().getTime();
        } catch (NullPointerException e) {
            System.out.println("NullPointerException thrown!");
        }

        // Calculate the day difference between current Date and the day the product was put to shelf
        if (differenceInMilliseconds < 0) {
            System.out.println("    The item is not yet in the shelf! (shelfDate is in the future)");
            return false;
        }

        // Convert the time difference from milliseconds to days
        long differenceInDays = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS);

        // Only update the quality when the quality check day has come
        if (differenceInDays % this.getQualityChangeRate() == 0) {

            // Store the old quality before updating to determine if the quality has changed or not
            int oldQuality = this.getQuality();

            // If there was at least qualityChangeRate days passed after putting the product to the shelf,
            // then calculate how much quality must be added to product
            if (differenceInDays >= this.getQualityChangeRate()) {
                long qualityToAdd = 0;
                long result = 0;
                int qualityResult = 0;

                // If the quality change rage value is not fixed,
                // don't use the start quality value for the calculation of the new quality
                if (this.isVariableQuality()) {
                    // TODO: this condition is already met?
                    if (((int) differenceInDays % this.getQualityChangeRate()) == 0) {
                        qualityResult = oldQuality + (this.getQualityChange());
                    }
                } else {
                    qualityToAdd = (int) (differenceInDays / this.getQualityChangeRate());
                    result = this.getQualityChange() * qualityToAdd;
                    qualityResult = this.getStartQuality() + (int) (result);
                }

                // If the quality result is in a valid range, update the quality (result <= lower <= upper)
                if ((qualityResult <= this.getQualityUpperLimit()) && (qualityResult >= this.getQualityLowerLimit())) {
                    this.setQuality(qualityResult);
                    // If result <= lower < upper, set quality to lower limit
                } else if (qualityResult <= this.getQualityUpperLimit() && qualityResult < this.getQualityLowerLimit()) {
                    this.setQuality(this.getQualityLowerLimit());
                    // If  lower <= upper < result , set quality to upper limit
                } else if (qualityResult > this.getQualityUpperLimit() && (qualityResult >= this.getQualityLowerLimit())) {
                    this.setQuality(this.getQualityUpperLimit());
                } else {
                    this.setQuality(qualityResult);
                }

                // Only log update changes to the console when the quality value is changed
                if (!(oldQuality == this.getQuality())) {
                    System.out.println("   -> Updated quality of \"" + this.getDescription() + "\" from " + oldQuality + " to " + this.getQuality());
                    return true;
                } else {
                    return false;
                }
            }
        }
        // Quality change day is not today
        return false;
    }

    // Update the price for the current day based on the base price of product and the current quality
    public boolean updateDailyPrice() {
        // The product has a varying price
        if (this.hasDailyPrice()) {
            double oldPrice = this.getPrice();
            this.setPrice(this.getBasePrice() + 0.1 * this.getQuality());

            // Only log to console when the price is changed
            if (!(oldPrice == this.getPrice())) {
                System.out.println("   -> Updated price of \"" + this.getDescription() + "\" from " + oldPrice + " to " + this.getPrice());
                return true;
            } else {
                return false;
            }
        }
        // The product has a fixed price
        return false;
    }

    // Update the products quality and daily price
    public boolean updateProduct(Date currentDate) {
        boolean qualityChange = this.updateQualityOfProduct(currentDate);
        boolean priceChange = this.updateDailyPrice();

        // If any change was made to the product, it has logged its change to the console.
        // Do a print line to show all the changes of different products separately in a cleaner way
        if (qualityChange || priceChange) {
            System.out.println("");
            return true;
        } else {
            return false;
        }
    }

    // Determines if the product should be removed from shelf
    public boolean shouldBeRemovedFromShelf(Date currentDate) {
        boolean expired = false;
        boolean lowQuality = false;
        boolean expireDateValid = true;

        // Check if the expiry date of the product is valid within the given interval
        if (!this.expiryDateIsValid(this.getExpiryDate(), this.getShelfDate())) {
            expireDateValid = false;
        }
        if (currentDate != null) {
            // If the products is expired
            if (this.canExpire() && this.isExpired(currentDate)) {
                expired = true;
            }
            // If the products has low quality
            if (this.getQuality() < this.getQualityLowerLimit()) {
                lowQuality = true;
            }

            // Log the detected information in the console and decide if the product should be removed from shelf
            if (expired && lowQuality) {
                System.out.println("  (!) Low quality and expired item found: " + this.getDescription());
                return true;
            } else if (expired) {
                System.out.println("  (!) Expired (" + new SimpleDateFormat("dd-MM-yyyy").format(this.getExpiryDate()) + ") item found: " + this.getDescription());
                return true;
            } else if (lowQuality) {
                System.out.println("  (!) Low quality (" + this.getQuality() + ") item found: " + this.getDescription());
                return true;
            } else if (!expireDateValid) {
                System.out.println("  (!) Expire date is not valid (" + new SimpleDateFormat("dd-MM-yyyy").format(this.getExpiryDate()) + ") for item: " + this.getDescription());
                return true;
            } else {
                return false;
            }

        } else {
            System.out.println("Current date cannot be null!");
            System.exit(1);
            return false;
        }
    }

    // Determines if the product is expired by calculating the time difference between its expiry date and the current date
    public boolean isExpired(Date currentDate) {
        long differenceInMilliseconds = expiryDate.getTime() - currentDate.getTime();
        if (differenceInMilliseconds > 0) {
            return false;
        } else {
            return true;
        }
    }

    // Check if the expiry date is within the allowed time interval
    public boolean expiryDateIsValid(Date expiryDate, Date currentDate) {
        try {
            long differenceInMilliseconds = expiryDate.getTime() - currentDate.getTime();
            long differenceInDays = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS);

            if (differenceInDays >= this.getMinimumDays() && differenceInDays <= this.getMaximumDays()) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException thrown!");
            System.exit(1);
            return false;
        }
    }


    // Getter and setter methods
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getStartQuality() {
        return startQuality;
    }

    public void setStartQuality(int startQuality) {
        this.startQuality = startQuality;
    }

    public int getQualityChange() {
        return qualityChange;
    }

    public void setQualityChange(int qualityChange) {
        this.qualityChange = qualityChange;
    }

    public int getQualityChangeRate() {
        return qualityChangeRate;
    }

    public void setQualityChangeRate(int qualityChangeRate) {
        this.qualityChangeRate = qualityChangeRate;
    }

    public int getQualityLowerLimit() {
        return qualityLowerLimit;
    }

    public void setQualityLowerLimit(int qualityLowerLimit) {
        this.qualityLowerLimit = qualityLowerLimit;
    }

    public int getQualityUpperLimit() {
        return qualityUpperLimit;
    }

    public void setQualityUpperLimit(int qualityUpperLimit) {
        this.qualityUpperLimit = qualityUpperLimit;
    }

    public double getBasePrice() {
        return basePrice;
    }

    // Base price must be equal or greater than 0
    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            System.out.println("Base price cannot be negative!");
            this.basePrice = 0;
        } else {
            this.basePrice = basePrice;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean hasDailyPrice() {
        return hasDailyPrice;
    }

    public void setHasDailyPrice(boolean hasDailyPrice) {
        this.hasDailyPrice = hasDailyPrice;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean canExpire() {
        return doesExpire;
    }

    public void setDoesExpire(boolean doesExpire) {
        this.doesExpire = doesExpire;
    }

    public void setExpiryDate(Date expiryDate) {
        if (expiryDate != null) {
            this.expiryDate = expiryDate;
        }
    }

    public Date getShelfDate() {
        return shelfDate;
    }

    public void setShelfDate(Date shelfDate) {
        this.shelfDate = shelfDate;
        // System.out.println("Shelf date of " + this.description + " set to: " + shelfDate.toString());
    }

    public int getMinimumDays() {
        return minimumDays;
    }

    public void setMinimumDays(int minimumDays) {
        this.minimumDays = minimumDays;
    }

    public int getMaximumDays() {
        return maximumDays;
    }

    public void setMaximumDays(int maximumDays) {
        this.maximumDays = maximumDays;
    }

    public boolean isVariableQuality() {
        return variableQuality;
    }

    public void setVariableQuality(boolean variableQuality) {
        this.variableQuality = variableQuality;
    }

}
