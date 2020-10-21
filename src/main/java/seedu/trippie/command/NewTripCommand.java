package seedu.trippie.command;

import seedu.trippie.Ui;
import seedu.trippie.data.Trip;
import seedu.trippie.data.TrippieData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTripCommand extends Command {

    public NewTripCommand() {
    }

    public boolean isExit() {
        return false;
    }

    public void execute(Ui ui, TrippieData trippieData) {
        System.out.print("Enter your new trip's name:");

        System.out.print("Enter your new trip's start date (dd-mm-yyyy):");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = null;
        try {
            startDate = df.parse(ui.getLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int index = trippieData.getTripList().size();

        System.out.print("Enter the foreign exchange rate:");
        Float forEx = Float.parseFloat(ui.getLine());

        System.out.print("Enter the foreign currency abbreviation (eg. MYR): ");
        String currencyAbbreviation = ui.getLine();

        System.out.print("Enter your budget for the trip (in SGD):");
        Float budget = Float.parseFloat(ui.getLine());

        String name = ui.getLine();
        Trip newTrip = new Trip(index, name, startDate);
        newTrip.getExpenseListObject().setForExValue(forEx);
        newTrip.getExpenseListObject().setCurrencyAbbreviation(currencyAbbreviation);
        newTrip.getExpenseListObject().setBudgetValue(budget);
        trippieData.getTripList().add(newTrip);

        System.out.println("Added the trip " + newTrip.toString());
        trippieData.setCurrentTripFromIndex(index);
    }
}
