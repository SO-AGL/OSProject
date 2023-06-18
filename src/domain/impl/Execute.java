package domain.impl;

public class Execute extends ProgramLine {

    @Override
    public int getTimeEstimate() {
        return 1;
    }

    @Override
    public String toString() {
        return "execute";
    }

}
