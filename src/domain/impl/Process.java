package domain.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Process {
    private String name;
    private int priority;
    private String body;
    private String rawContent;
    private int timeEstimate;

    public Process(String filePath) throws RuntimeException {
        try {
            var lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

            var header = lines.get(0);
            setHeader(header);

            rawContent = String.join("\n", lines);
            setBody(rawContent);

            makeTimeEstimate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setHeader(String header) throws RuntimeException {
        var headerPattern = Pattern.compile("program ([a-zA-Z0-9.]+) (\\d)");
        var headerMatcher = headerPattern.matcher(header);
        if (headerMatcher.find()) {
            name = headerMatcher.group(1);
            priority = Integer.parseInt(headerMatcher.group(2));
        } else {
            throw new RuntimeException("Invalid program header");
        }
    }

    private void setBody(String rawBody) throws RuntimeException {
        var contentPattern = Pattern.compile("begin\n([a-zA-Z0-5 \n]+)end\n?");
        var contentMatcher = contentPattern.matcher(rawBody);
        if (contentMatcher.find()) {
            body = contentMatcher.group(1);
        } else {
            throw new RuntimeException("Invalid program content");
        }
    }

    private void makeTimeEstimate() {
        var scanner = new Scanner(body);

        while (scanner.hasNext()) {
            var line = scanner.nextLine();
            timeEstimate += parseLine(line);
        }

        scanner.close();
    }

    private int parseLine(String line) throws RuntimeException {
        if (line.equalsIgnoreCase("execute")) {
            return 1;
        }

        var pattern = Pattern.compile("^block ([1-5])$");
        var matcher = pattern.matcher(line);
        if (matcher.find()) {
            return 1 + Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException("Invalid program body. Line: '" + line + "'");
        }
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