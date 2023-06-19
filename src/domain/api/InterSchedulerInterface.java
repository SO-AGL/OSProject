package domain.api;

import domain.impl.Process;

public interface InterSchedulerInterface {
    void addProcess(Process process);

    int getProcessLoad();
}
