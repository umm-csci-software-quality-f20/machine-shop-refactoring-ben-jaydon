package applications;

import java.util.Arrays;

public class SimulationSpecification {
    private int numMachines;
    private int numJobs;
    private int[] changeOverTimes;
    //private JobSpecification[] jobSpecifications;
    public Job[] jobs;

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

    /*
    /**
     * @deprecated
     * @param jobNumber
     * @param specificationsForTasks
     * Automatically applies tasks to the appropriate job.
     */
    /*
    public void setSpecificationsForTasks(int jobNumber, int[] specificationsForTasks) {
        jobSpecifications[jobNumber].setSpecificationsForTasks(specificationsForTasks);
        jobs[jobNumber].addTasksFromSpecifications(specificationsForTasks);
    }
    */

    /**
     * @deprecated
     * @param theSpecifications
     * Automatically sets up Jobs, given specifications.
     */
    /*
    public void setJobSpecification(JobSpecification[] theSpecifications) {
        this.jobSpecifications = theSpecifications;
        jobs = new Job[theSpecifications.length];
        for(int i = 1; i < theSpecifications.length; i++) {
            if (theSpecifications[i].getSpecificationsForTasks() != null) {
                jobs[i] = new Job(i, theSpecifications[i].getSpecificationsForTasks());
            } else {
                jobs[i] = new Job(i);
            }
        }
    }
    */

    /**
     * @ deprecated
     * @param jobNumber
     * @return
     */
    /*
    public JobSpecification getJobSpecifications(int jobNumber) {
        //return jobSpecifications[jobNumber];
        return jobs[jobNumber].getSpecificationFromJob();
    }
    */
    
    

    public void setJobs(Job[] theJobs) {
        this.jobs = theJobs;
    }

    public Job getJob(int index) {
        return jobs[index];
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<").append(numMachines).append(" machines, ");
        builder.append(numJobs).append(" jobs; ");
        builder.append("change overs: ").append(Arrays.toString(changeOverTimes));
        /*
        for (int i=1; i<=numJobs; ++i) {
            builder.append("; job ").append(i).append(" tasks: ");
            builder.append(Arrays.toString(jobSpecifications[i].getSpecificationsForTasks()));
        }
        */
        for (Job j: jobs) {
            if (j != null){
                builder.append("; job ").append(j).append(" tasks: ");
                builder.append(j.getTaskQ().toArrayList());
            }
        }

        builder.append(">");
        return builder.toString();
    }
}
