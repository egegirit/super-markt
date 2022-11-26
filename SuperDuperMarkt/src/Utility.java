import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// Utility class for time related calculations
public class Utility {

    public static Date currentDate;

    // Calculate the millisecond difference between two dates
    public static long calculateDayDifference(Date date1, Date date2) {
        try {
            long differenceInMiliseconds = Math.abs(date1.getTime() - date2.getTime());
            return TimeUnit.DAYS.convert(differenceInMiliseconds, TimeUnit.MILLISECONDS);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException thrown!");
            System.exit(1);
            return -1;
        }
    }

    // Adds the given amounts of days to the given date
    public static Date addDaysToCurrentDate(Date currentDate, int days) {
        // Convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // Add the given days to the current day
        c.add(Calendar.DATE, days);
        // Convert calendar to date
        Date changedDate = c.getTime();
        return changedDate;
    }

    // Returns the next day
    public static Date nextDay(Date currentDate, int currentDay) {

        if(currentDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDayString = formatter.format(currentDate);

            Date tomorrow = addDaysToCurrentDate(currentDate, 1);
            String nextDayString = formatter.format(tomorrow);
            System.out.println("\n==== [ DAY " + currentDay + " ] (" + nextDayString + ") ====");

            return tomorrow;
        }
        else{
            System.out.println("Date is not initialized!");
            System.exit(1);
            return null;
        }
    }

}
