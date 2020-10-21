package seedu.trippie.command;

import seedu.trippie.Ui;
import seedu.trippie.data.Expense;
import seedu.trippie.data.TrippieData;
import seedu.trippie.exception.TrippieExceedBudgetException;

import java.util.List;

public class ListExpenseCommand extends Command {

    private static final String EXCEED_BUDGET_MESSAGE = "WARNING! You have exceeded your initial budget.\n"
            + "Recommended action: Increase budget limit.";

    private static final int BAR_SIZE = 10;
    private static final String SYMBOL_START_BOUNDARY = "[";
    private static final String SYMBOL_SPEND = "=";
    private static final String SYMBOL_REMAINING = "-";
    private static final String SYMBOL_END_BOUNDARY = "]";

    public ListExpenseCommand() {
    }

    @Override
    public boolean isExit() {
        return false;
    }

    public static void createBudgetPercentageBar(float spent, float budget) throws TrippieExceedBudgetException {
        if (spent == budget) {
            System.out.println("You have spent finish your budget.");
        } else if (spent > budget) {
            throw new TrippieExceedBudgetException(EXCEED_BUDGET_MESSAGE);
        } else {
            System.out.println("You are still spending within your budget.");
        }
        float spentPercentage = (100 * spent) / budget;
        float spentLength = BAR_SIZE * spentPercentage / 100;
        StringBuilder bar = new StringBuilder(SYMBOL_START_BOUNDARY);
        for (int i = 0; i < BAR_SIZE; i++) {
            if (i < spentLength) {
                bar.append(SYMBOL_SPEND);
            } else {
                bar.append(SYMBOL_REMAINING);
            }
        }
        bar.append(SYMBOL_END_BOUNDARY);
        System.out.println(bar + " "
                + String.format("%.1f", spentPercentage) + "%");
    }

    @Override
    public void execute(Ui ui, TrippieData trippieData) {
        List<Expense> expenses = trippieData.getCurrentTrip().getExpenseListObject().getExpenseList();
        Float exchangeRate = trippieData.getCurrentTrip().getExpenseListObject().getForExValue();
        String currencyAbbreviation = trippieData.getCurrentTrip().getExpenseListObject().getCurrencyAbbreviation();
        if (expenses.isEmpty()) {
            System.out.println("There is currently nothing in your Expense list.");
        } else {
            int listIndex = 1;
            float budget = trippieData.getCurrentTrip().getExpenseListObject().getBudgetValue() * exchangeRate;
            System.out.println("Total budget: $" + String.format("%.2f ", budget) + currencyAbbreviation
                    + " (" + String.format("%.2f", budget / exchangeRate) + " SGD)");
            System.out.println("Expense List:");
            for (Expense expense : expenses) {
                System.out.println("[" + listIndex + "] " + expense.toString());
                listIndex++;
            }
            float totalSpending = trippieData.getCurrentTrip().getExpenseListObject().getTotalExpenses();
            System.out.println("Your current total spending is $"
                    + String.format("%.2f ", totalSpending) + currencyAbbreviation
                    + " (" + String.format("%.2f", totalSpending / exchangeRate) + " SGD)");
            try {
                float remainingBudget = budget - totalSpending;
                checkRemainingBudget(remainingBudget, exchangeRate, currencyAbbreviation);
                createBudgetPercentageBar(totalSpending, budget);
            } catch (TrippieExceedBudgetException e) {
                System.out.println(EXCEED_BUDGET_MESSAGE);
            }
        }
    }

    private void checkRemainingBudget(float remainingBudget, float exchangeRate, String currencyAbbreviation) {
        if (remainingBudget < 0) {
            System.out.print("");
        } else {
            System.out.println("Your current remaining budget is $"
                    + String.format("%.2f ", remainingBudget) + currencyAbbreviation
                    + " (" + String.format("%.2f", remainingBudget / exchangeRate) + " SGD)");
        }
    }
}