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

    /**
     * change the state of theMachine
     *
     * @return last job run on this machine
     */
    static Job changeState(Machine[] machine, EventList eList, int largeTime, int timeNow, int theMachine) {// Task on theMachine has finished,
                                            // schedule next one.
        Job lastJob;
        if (machine[theMachine].activeJob == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            if (machine[theMachine].jobQ.isEmpty()) // no waiting job
                eList.setFinishTime(theMachine, largeTime);
            else {// take job off the queue and work on it
                machine[theMachine].newActiveJob();
                machine[theMachine].setWait(timeNow);
                machine[theMachine].numTasks++;
                int t = machine[theMachine].activeJob.removeNextTask();
                eList.setFinishTime(theMachine, timeNow + t);
            }
        } else {// task has just finished on machine[theMachine]
                // schedule change-over time
            lastJob = machine[theMachine].activeJob;
            machine[theMachine].activeJob = null;
            eList.setFinishTime(theMachine, timeNow + machine[theMachine].changeTime);
        }

        return lastJob;
    }

    private void newActiveJob() {
        this.activeJob = ((Job) this.jobQ.remove());
    }
    private void setWait(int timeNow) {
        this.totalWait = (this.totalWait + timeNow - this.activeJob.getArrivalTime());
    }

    LinkedQueue getJobQ() {
        return jobQ;
    }

    int getTotalWait() {
        return totalWait;
    }

    int getNumTasks() {
        return numTasks;
    }

    void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }
}
