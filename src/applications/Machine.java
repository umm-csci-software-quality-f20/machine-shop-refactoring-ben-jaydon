package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    Machine() {
        jobQ = new LinkedQueue();
    }

    public void newActiveJob() {
        this.activeJob = ((Job) this.jobQ.remove());
    }
    public static void setWait(Machine machine, int timeNow) {
        machine.totalWait = (machine.totalWait + timeNow - machine.activeJob.getArrivalTime());
    }

    public void beginNextJob(MachineShopSimulator machineShopSimulator) {
        newActiveJob();
        setWait(this, machineShopSimulator.getTimeNow());
        numTasks++;
    }

    public LinkedQueue getJobQ() {
        return jobQ;
    }

    public int getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public Job getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(Job activeJob) {
        this.activeJob = activeJob;
    }

    Job processWork(int theMachine, MachineShopSimulator machineShopSimulator) {
        Job lastJob;
        if (getActiveJob() == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            if (getJobQ().isEmpty()) // no waiting job
                machineShopSimulator.geteList().setFinishTime(theMachine, machineShopSimulator.getLargeTime());
            else {// take job off the queue and work on it
                beginNextJob(machineShopSimulator);
                int t = getActiveJob().removeNextTask();
                machineShopSimulator.geteList().setFinishTime(theMachine, machineShopSimulator.getTimeNow() + t);
            }
        } else {// task has just finished on machine[theMachine]
                // schedule change-over time
            lastJob = getActiveJob();
            setActiveJob(null);
            machineShopSimulator.geteList().setFinishTime(theMachine, machineShopSimulator.getTimeNow() + getChangeTime());
        }
        return lastJob;
    }
}
