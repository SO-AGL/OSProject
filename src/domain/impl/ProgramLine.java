package domain.impl;

import java.util.regex.Pattern;

public class ProgramLine {
    private int blockFor = 0;
    private int timeEstimate = 1;
    private String rawContent;

    /**
     * Constructs an instance of ProgramLine from a String line by parsing it
     * and setting the blockFor and timeEstimate properties that will be used to
     * simulate the execution of a process.
     *
     * @param line
     * @throws IllegalArgumentException
     */
    public ProgramLine(String line) throws IllegalArgumentException {
        rawContent = line;

        if (line.startsWith("block")) {
            var pattern = Pattern.compile("^block ([1-5])$");
            var matcher = pattern.matcher(line);

            if (matcher.find()) {
                blockFor = Integer.parseInt(matcher.group(1));
                timeEstimate += blockFor;
            } else {
                throw new IllegalArgumentException("Invalid program body. Line: '" + line + "'");
            }
        } else if (!line.equals("execute")) {
            throw new IllegalArgumentException("Invalid line: " + line);
        }
    }

    /**
     * Get the amount of quanta that this ProgramLine will block the program
     * for.
     *
     * @return quanta
     */
    public int getBlockFor() {
        return blockFor;
    }

    /**
     * Get the time estimate for this ProgramLine. This is calculated like this:
     * one quantum for any type of line, plus the amount of quanta that this
     * line will block the program for, if it begins with "block".
     *
     * @return
     */
    public int getTimeEstimate() {
        return timeEstimate;
    }

    @Override
    public String toString() {
        return rawContent;
    }
}
