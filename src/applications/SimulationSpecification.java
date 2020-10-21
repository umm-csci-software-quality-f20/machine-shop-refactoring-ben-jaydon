package applications;

import java.util.Arrays;

public class SimulationSpecification {
    private int numMachines;
    private int numJobs;
    private int[] changeOverTimes;
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
