package domain.impl;

import java.util.regex.Pattern;

public class ProgramLine {
    private int blockFor = 0;
    private int timeEstimate = 1;
    private String rawContent;

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

    public int getBlockFor() {
        return blockFor;
    }

    public int getTimeEstimate() {
        return timeEstimate;
    }

    @Override
    public String toString() {
        return rawContent;
    }
}
