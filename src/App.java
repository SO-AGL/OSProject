import java.util.Arrays;

import application.SchedulerSimulator;
import domain.api.SchedulingStrategy;
import domain.impl.RoundRobin;
import domain.impl.ShortestJobFirst;

public class App {
    public static void main(String[] args) throws Exception {
        var argsList = Arrays.asList(args); // convert arguments array to list to facilitate finding the indexes of the flags
        int npIndex = argsList.indexOf("-np"); // index of the number of process flag in args array
        int stratIndex = argsList.indexOf("-strat"); // index of the strategy flag in args array (rr or sjf)
        int maxProcessReadySize = 100; // default max number of processes in the ready queue
        String stratName = "sjf"; // default strategy: SJF
        SchedulingStrategy strategy;

        if (npIndex != -1) { // the -np flag was used
            try {
                maxProcessReadySize = Integer.parseInt(args[npIndex+1]);
                if (maxProcessReadySize <= 0) {
                    throw new NumberFormatException();
                }
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform a value in sequence
                    throw new ArrayIndexOutOfBoundsException("If using -np flag, inform a value (>0)");
                }
                if (e instanceof NumberFormatException) { // if the value informed wasn't valid (couldn't convert to int, or <=0)
                    throw new NumberFormatException("Inform a valid value for np (>0)");
                }
                throw e;
            }
        }

        if (stratIndex != -1) { // the -strat flag was used
            try {
                stratName = args[stratIndex+1];
                if (!stratName.equals("sjf") && !stratName.equals("rr")) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform a value in sequence
                    throw new ArrayIndexOutOfBoundsException("If using -strat flag, inform a value ('rr' or 'sjf')");
                }
                if (e instanceof IllegalArgumentException) { // if the value informed wasn't valid (not 'sjf' nor 'rr')
                    throw new IllegalArgumentException("Inform a valid strategy ('rr' or 'sjf')");
                }
                throw e;
            }
        }

        if (stratName.equals("rr")) {
            strategy = new RoundRobin();
        } else {
            strategy = new ShortestJobFirst();
        }

        new SchedulerSimulator(maxProcessReadySize, strategy);
    }
}
