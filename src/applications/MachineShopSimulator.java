
package applications;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";

    // data members of MachineShopSimulator
    private int timeNow; // current time
    private int numMachines; // number of machines
    private int numJobs; // number of jobs
    private EventList eList; // pointer to event list
    private Machine[] machine; // array of machines
    private int largeTime; // all machines finish before this

    // methods
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    boolean moveToNextMachine(Job theJob, SimulationResults simulationResults) {
        if (theJob.getTaskQ().isEmpty()) {// no next task
            simulationResults.setJobCompletionData(theJob.getId(), timeNow, timeNow - theJob.getLength());
            return false;
        } else {// theJob has a next task
            // get machine for next task
            return scheduleTask(theJob);
        }
    }

    boolean scheduleTask(Job job) {
        int p = ((Task) job.getTaskQ().getFrontElement()).getMachine();
        // put on machine p's wait queue
        getMachine()[p].getJobQ().put(job);
        job.setArrivalTime(getTimeNow());
        // if p idle, schedule immediately
        if (geteList().nextEventTime(p) == getLargeTime()) {// machine is idle
            changeState(p);
        }
        return true;
    }

    /**
     * change the state of theMachine
     *
     * @return last job run on this machine
     */
    Job changeState(int theMachine) {// Task on theMachine has finished,
        // schedule next one.
        Job lastJob;
        EventList elist = geteList();
        Machine machine = getMachine()[theMachine];
        lastJob = machine.processWork(theMachine, getLargeTime(), getTimeNow(), elist);

        return lastJob;
    }

    private void setMachineChangeOverTimes(SimulationSpecification specification) {
        for (int i = 1; i<=specification.getNumMachines(); ++i) {
            machine[i].setChangeTime(specification.getChangeOverTimes(i));
        }
    }

    private void createEventAndMachineQueues(SimulationSpecification specification) {
        // create event and machine queues
        eList = new EventList(specification.getNumMachines(), largeTime);
        machine = new Machine[specification.getNumMachines() + 1];
        for (int i = 1; i <= specification.getNumMachines(); i++)
            machine[i] = new Machine();
    }

    /** load first jobs onto each machine
     * @param specification
     * */
    void startShop(SimulationSpecification specification) {
        // Move this to startShop when ready
        numMachines = specification.getNumMachines();
        numJobs = specification.getNumJobs();
        createEventAndMachineQueues(specification);

        // Move this to startShop when ready
        setMachineChangeOverTimes(specification);

        // Move this to startShop when ready
        specification.setUpJobs(getMachine());

        for (int p = 1; p <= numMachines; p++)
            changeState(p);
    }

    /** process all jobs to completion
     * @param simulationResults
     * */
    void simulate(SimulationResults simulationResults) {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob, simulationResults))
                numJobs--;
        }
    }

    /** output wait times at machines
     * @param simulationResults
     * */
    void outputStatistics(SimulationResults simulationResults) {
        simulationResults.setFinishTime(timeNow);
        simulationResults.setNumMachines(numMachines);
        setNumTasksPerMachine(simulationResults);
        setTotalWaitTimePerMachine(simulationResults);
    }

    private void setTotalWaitTimePerMachine(SimulationResults simulationResults) {
        int[] totalWaitTimePerMachine = new int[numMachines+1];
        for (int i=1; i<=numMachines; ++i) {
            totalWaitTimePerMachine[i] = machine[i].getTotalWait();
        }
        simulationResults.setTotalWaitTimePerMachine(totalWaitTimePerMachine);
    }

    private void setNumTasksPerMachine(SimulationResults simulationResults) {
        int[] numTasksPerMachine = new int[numMachines+1];
        for (int i=1; i<=numMachines; ++i) {
            numTasksPerMachine[i] = machine[i].getNumTasks();
        }
        simulationResults.setNumTasksPerMachine(numTasksPerMachine);
    }

    public  SimulationResults runSimulation(SimulationSpecification specification) {
        largeTime = Integer.MAX_VALUE;
        timeNow = 0;
        startShop(specification); // initial machine loading
        SimulationResults simulationResults = new SimulationResults(numJobs);
        simulate(simulationResults); // run all jobs through shop
        outputStatistics(simulationResults);
        return simulationResults;
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        final SpecificationReader specificationReader = new SpecificationReader();
        SimulationSpecification specification = specificationReader.readSpecification();
        MachineShopSimulator simulator = new MachineShopSimulator();
        SimulationResults simulationResults = simulator.runSimulation(specification);
        simulationResults.print();
    }

    public int getTimeNow() {
        return timeNow;
    }

    public EventList geteList() {
        return eList;
    }

    public Machine[] getMachine() {
        return machine;
    }

    public void setMachine(Machine[] machine) {
        this.machine = machine;
    }

    public int getLargeTime() {
        return largeTime;
    }

}
