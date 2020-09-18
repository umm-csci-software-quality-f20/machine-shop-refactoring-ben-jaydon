package applications;

import dataStructures.LinkedQueue;

class Job {
    // data members
    private LinkedQueue taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier

    // constructor
    Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    Job(int theId, int[] taskSpecifications) {
        id = theId;
        taskQ = new LinkedQueue();
        addTasksFromSpecifications(taskSpecifications);
    }

    Job(int theId, Task[] theTasks){
        id = theId;
        taskQ = new LinkedQueue();
        for (Task theTask: theTasks){
            if (theTask != null){
                taskQ.put(theTask);
            }
        }

    }

    // other methods

    /**
     * @deprecated
     * @param theMachine
     * @param theTime
     */
    public void addTask(int theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    public void addTask(Task theTask) {
        taskQ.put(theTask);
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        length = getLength() + theTime;
        return theTime;
    }

    public LinkedQueue getTaskQ() {
        return taskQ;
    }

    public int getLength() {
        return length;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

	void addTasksFromSpecifications(int[] taskSpecifications) {
	    for (int j = 1; j <= taskSpecifications.length - 1; j += 2) {
	        int theMachine = taskSpecifications[j];
	        int theTaskTime = taskSpecifications[j + 1];
	        addTask(theMachine, theTaskTime); // add to
	    } // task queue
	}

}
