package domain.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Process {
    private String name;
    private int priority;
    private List<ProgramLine> body = new ArrayList<>();
    private String rawContent;
    private int timeEstimate;
    private int remainingTime;
    private int lineNumber = 0;
    private int blockedFor = 0;

    public Process(String filePath) throws IOException, NoSuchFileException {
        try {
            rawContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8).replaceAll("\r", "");

            setHeader();
            setBody();
            makeTimeEstimate();
        } catch (Exception e) {
            throw e;
        }
    }

    public String getName() {
        return name;
    }

    public int getBlockedFor() {
        return blockedFor;
    }

    public void setBlockedFor(int blockedFor) {
        this.blockedFor = blockedFor;
    }

    public ProgramLine getNextLine() throws NoSuchElementException {
        if (hasNextLine()) {
            return body.get(lineNumber++);
        }

        throw new NoSuchElementException();
    }

    public boolean hasNextLine() {
        return lineNumber < body.size();
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void decrementBlockedTime() {
        if (blockedFor > 0) {
            blockedFor--;
        }
    }

    private void setHeader() throws IllegalArgumentException {
        var headerPattern = Pattern.compile("^program ([a-zA-Z0-9.]+) (\\d)$", Pattern.MULTILINE);
        var headerMatcher = headerPattern.matcher(rawContent);

        if (headerMatcher.find()) {
            name = headerMatcher.group(1);
            priority = Integer.parseInt(headerMatcher.group(2));
        } else {
            throw new IllegalArgumentException("Invalid program header. Raw input:\n" + rawContent);
        }
    }

    private void setBody() throws IllegalArgumentException {
        var contentPattern = Pattern.compile("begin\n([a-zA-Z0-5 \n]+)end\n?");
        var contentMatcher = contentPattern.matcher(rawContent);

        if (contentMatcher.find()) {
            var lines = contentMatcher.group(1).split("\n");

            for (var line : lines) {
                var programLine = new ProgramLine(line);
                body.add(programLine);
            }
        } else {
            throw new IllegalArgumentException("Invalid program content. Raw input:\n" + rawContent);
        }
    }

    private void makeTimeEstimate() {
        for (var line : body) {
            timeEstimate += line.getTimeEstimate();
        }

        remainingTime = timeEstimate;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "Program name: " + name + "\n" +
                "Priority: " + priority + "\n" +
                "Time estimate: " + timeEstimate + "\n" +
                "Body:\n" + body + "\n" +
                "Raw content:\n" + rawContent + "\n";
    }
}
