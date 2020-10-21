package applications;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

@RunWith(JUnitQuickcheck.class)
public class SimulationProperties {
    @Property
    public void lastJobCompletesAtOverallFinishTime(
            @From(SimulationSpecificationGenerator.class)
                    SimulationSpecification specification)
    {
        MachineShopSimulator simulator = new MachineShopSimulator();
        final SimulationResults results = simulator.runSimulation(specification);
        final int finishTime = results.getFinishTime();
        final JobCompletionData[] jobCompletionData = results.getJobCompletionData();
        final int lastJobCompletionTime = jobCompletionData[jobCompletionData.length-1].getCompletionTime();
        assertEquals(finishTime, lastJobCompletionTime);
    }

    @Property
    public void waitTimesShouldMatch(
            @From(SimulationSpecificationGenerator.class)
                    SimulationSpecification specification)
    {
        MachineShopSimulator simulator = new MachineShopSimulator();
        final SimulationResults results = simulator.runSimulation(specification);

        int totalMachineWaitTime = 0;
        for (int waitTime : results.getTotalWaitTimePerMachine()) {
            assertThat(waitTime, greaterThanOrEqualTo(0));
            totalMachineWaitTime += waitTime;
        }

        int totalJobWaitTime = 0;
        for (JobCompletionData jobCompletionData : results.getJobCompletionData()) {
            final int jobWaitTime = jobCompletionData.getTotalWaitTime();
            assertThat(jobWaitTime, greaterThanOrEqualTo(0));
            totalJobWaitTime += jobWaitTime;
        }

        assertEquals(totalJobWaitTime, totalMachineWaitTime);
    }

    @Property
    public void jobsOutputInTimeOrder(
            @From(SimulationSpecificationGenerator.class)
                SimulationSpecification specification)
    {
        MachineShopSimulator simulator = new MachineShopSimulator();
        final SimulationResults results = simulator.runSimulation(specification);

        JobCompletionData[] jobCompletionData = results.getJobCompletionData();
        for (int i=1; i<jobCompletionData.length-1; ++i) {
            assertThat(jobCompletionData[i].getCompletionTime(),
                    lessThanOrEqualTo(jobCompletionData[i+1].getCompletionTime()));
        }
    }

    @Property
    public void machinesCompletedCorrectNumberOfTasks(
            @From(SimulationSpecificationGenerator.class)
                SimulationSpecification specification)
    {
        MachineShopSimulator simulator = new MachineShopSimulator();


        int numMachines = specification.getNumMachines();
        int numJobs = specification.getNumJobs();
        int[] expectedMachineTaskCounts = new int[numMachines+1];
        assertTrue("" + numJobs + " jobs, should be greater than 0", numJobs > 0);

        for (int i=1; i<=numJobs; ++i) {

            Job theJob = specification.getJob(i);
            assertTrue("Should be tasks in the task queue itself", theJob.getTaskQ().size() > 0);
            ArrayList<Object> jobTasks = theJob.getTaskQ().toArrayList();
            int numTasks = jobTasks.size();
            assertTrue("Should actually be tasks", numTasks > 0);
            //System.err.println("numTasks: " + numTasks);
            for (Object t: jobTasks){
                ++expectedMachineTaskCounts[((Task) t).getMachine()];
            } 
        }


        final SimulationResults results = simulator.runSimulation(specification);
        int[] actualMachineTasksCounts = results.getNumTasksPerMachine();
        for (int i=1; i<=numMachines; ++i) {
            assertEquals(expectedMachineTaskCounts[i], actualMachineTasksCounts[i]);
        }
    }
}
