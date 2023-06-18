package domain.impl;

public abstract class ProgramLine {
    public static ProgramLine fromString(String line) {
        if (line.equals("execute")) {
            return new Execute();
        } else if (line.startsWith("block")) {
            return new Block(line);
        } else {
            throw new IllegalArgumentException("Invalid line: " + line);
        }
    }

    public abstract int getTimeEstimate();
}
