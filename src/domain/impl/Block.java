package domain.impl;

import java.util.regex.Pattern;

import domain.api.BlockLine;

public class Block extends ProgramLine implements BlockLine {
    private int blockFor = 0;

    Block(String line) {
        var pattern = Pattern.compile("^block ([1-5])$");
        var matcher = pattern.matcher(line);

        if (matcher.find()) {
            blockFor = Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException("Invalid program body. Line: '" + line + "'");
        }
    }

    @Override
    public int getBlockFor() {
        return blockFor;
    }

    @Override
    public int getTimeEstimate() {
        return 1 + blockFor;
    }

    @Override
    public String toString() {
        return "block " + blockFor;
    }
}
