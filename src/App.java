import application.SchedulerSimulator;

public class App {
    public static void main(String[] args) throws Exception {
        
        //First arg is the max number of processes in the ready queue
        if(args.length != 1) {
            System.out.println("Usage: java App <max number of processes in the ready queue>");
            return;
        }
        int maxProcessReadySize = Integer.parseInt(args[0]);

        var schedulerSimulator = new SchedulerSimulator(maxProcessReadySize);
        schedulerSimulator.start();
    }
}
