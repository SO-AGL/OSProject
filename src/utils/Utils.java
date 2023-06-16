package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Utils {
    public static <T, S extends Comparable<S>> int upperBound(List<T> list, S target, Function<T, S> getter) {
        int l = 0;
        int r = list.size() - 1;
        
        while (l < r) {
            int m = (l + r) / 2;
            var element = list.get(m);
            var value = getter.apply(element);

            if (value.compareTo(target) <= 0) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }

        return r;
    }

    public static void generateRandomProcesses(int n) {
        for (int i : IntStream.range(1, n + 1).toArray()) {
            var filename = i + ".txt";
            var process = Utils.makeProcess(filename, 20);

            try {
                var writer = new BufferedWriter(new FileWriter("data/processes/" + filename));
                writer.write(process);
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String makeProcess(String filename, int maxBodySize) {
        int priority = ThreadLocalRandom.current().nextInt(0, 9);
        var program = "program " + filename + " " + priority + "\nbegin\n";

        int nLines = ThreadLocalRandom.current().nextInt(1, maxBodySize);

        for (var line : IntStream.range(0, nLines).toArray())  {
            if (ThreadLocalRandom.current().nextBoolean()) {
                program += "execute\n";
            } else {
                var waitFor = ThreadLocalRandom.current().nextInt(1, 6);
                program += "block " + waitFor + "\n";
            }
        }

        program += "end\n";

        return program;
    }
}
