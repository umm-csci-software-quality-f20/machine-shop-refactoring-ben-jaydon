package applications;

import java.util.Arrays;

public class SimulationSpecification {
    private int numMachines;
    private int numJobs;
    private int[] changeOverTimes;
    private JobSpecification[] jobSpecifications;
    private Job[] jobs;

    public void setNumMachines(int numMachines) {
        this.numMachines = numMachines;
    }

    public void setNumJobs(int numJobs) {
        this.numJobs = numJobs;
    }

    public int getNumMachines() {
        return numMachines;
    }

    public int getNumJobs() {
        return numJobs;
    }

    public void setChangeOverTimes(int[] changeOverTimes) {
        this.changeOverTimes = changeOverTimes;
    }

    public int getChangeOverTimes(int machineNumber) {
        return changeOverTimes[machineNumber];
    }

    /**
     * @deprecated
     * @param jobNumber
     * @param specificationsForTasks
     * Automatically applies tasks to the appropriate job.
     */
    public void setSpecificationsForTasks(int jobNumber, int[] specificationsForTasks) {
        jobSpecifications[jobNumber].setSpecificationsForTasks(specificationsForTasks);
        jobs[jobNumber].addTasksFromSpecifications(specificationsForTasks);
    }

    /**
     * @deprecated
     * @param jobSpecifications
     * Automatically sets up Jobs, given specifications.
     */
    public void setJobSpecification(JobSpecification[] jobSpecifications) {
        this.jobSpecifications = jobSpecifications;
        for(int i = 1; i < jobSpecifications.length; i++) {
            jobs[i] = new Job(i, jobSpecifications[i].getSpecificationsForTasks());
        }
    }

    /**
     * @deprecated
     * @param jobNumber
     * @return
     */
    public JobSpecification getJobSpecifications(int jobNumber) {
        //return jobSpecifications[jobNumber];
        return jobs[jobNumber].getSpecificationFromJob();
    }

    public void setJobs(Job[] theJobs) {
        this.jobs = theJobs;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<").append(numMachines).append(" machines, ");
        builder.append(numJobs).append(" jobs; ");
        builder.append("change overs: ").append(Arrays.toString(changeOverTimes));
        for (int i=1; i<=numJobs; ++i) {
            builder.append("; job ").append(i).append(" tasks: ");
            builder.append(Arrays.toString(jobSpecifications[i].getSpecificationsForTasks()));
        }

        builder.append(">");
        return builder.toString();
    }
}
