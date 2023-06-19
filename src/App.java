import java.util.Arrays;

import application.SchedulerSimulator;
import domain.api.SchedulingStrategy;
import domain.impl.RoundRobin;
import domain.impl.ShortestJobFirst;

public class App {
    public static void main(String[] args) throws Exception {
        int npIndex = Arrays.asList(args).indexOf("-np"); // index of the number of process flag in args array
        int stratIndex = Arrays.asList(args).indexOf("-strat"); // index of the strategy flag in args array (rr or sjf)
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
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform an value in sequence
                    System.err.println("If using -np flag, inform an value (>0)");
                    return;
                }
                if (e instanceof NumberFormatException) { // if the value informed wasn't valid (couldn't convert to int, or <=0)
                    System.err.println("Inform an valid value for np (>0)");
                    return;
                }
            }
        }

        if (stratIndex != -1) {
            try {
                stratName = args[stratIndex+1];
                if (!stratName.equals("sjf") && !stratName.equals("rr")) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform an value in sequence
                    System.err.println("If using -strat flag, inform an value ('rr' or 'sjf')");
                    return;
                }
                if (e instanceof IllegalArgumentException) { // if the value informed wasn't valid (not 'sjf' nor 'rr')
                    System.err.println("Inform an valid strategy ('rr' or 'sjf')");
                    return;
                }
            }
        }

        if (stratName.equals("rr")) {
            strategy = new RoundRobin();
        } else {
            strategy = new ShortestJobFirst();
        }

        var schedulerSimulator = new SchedulerSimulator(maxProcessReadySize, strategy);
        schedulerSimulator.start();
    }
}
