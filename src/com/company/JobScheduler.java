/*
    Project #2: Job Scheduler
    Description: Create three java classes to solve the job scheduling problem described.
    Class: COMP496 ALG 11:00AM, Professor Schwartz
    Team Members: Inkyu Park, Armand Abrahamian
    Date: April 6, 2017
 */

package com.company;

import java.util.ArrayList;
import java.util.Random;

public class JobScheduler
{
    private int nJobs;
    private Job[] jobs;
    // these variables are for brute force schedule
    private static int maxProfit;
    private static Schedule bruteForceSchedule;
    
    /**
     * Constructor for JobScheduler.
     *
     * @param joblength the time needed to complete each job.
     * @param deadline  the deadline of each job
     * @param profit    the amount of profit for each job
     */
    public JobScheduler(int[] joblength, int[] deadline, int[] profit)
    {
        //Set nJobs
        nJobs = joblength.length;

        //Fill jobs array. The kth job entered has JobNo = k;
        jobs = new Job[nJobs];
        for (int index = 0; index < nJobs; index++)
        {
            jobs[index] = new Job(index, joblength[index], deadline[index], profit[index]);
        }
    }

    public void printJobs()  //prints the array jobs
    {
        for (int index = 0; index < nJobs; index++) {
            System.out.println(jobs[index].toString());
        }
    }

    /**
     * try all the possible schedule with n jobs.
     *
     * @return Schedule that has maximum profit
     */
    //Brute force. Try all n! orderings. Return the schedule with the most profit
    public Schedule bruteForceSolution()
    {
        bruteForceSchedule = new Schedule();

        // initialize to 0 for max profit when this method called
        maxProfit = 0;

        // find maximum profit Schedule
        findMaxProfitSchedule(jobs,0);

        // update schedule profit
        bruteForceSchedule.profit = maxProfit;

        // if all jobs has 0 profit at first
        if(maxProfit == 0) {
            return scheduleJobs(bruteForceSchedule);
        }

        return bruteForceSchedule;
    }

    /**
     * try all the combination of jobs schedule and calculate total profit, and compare with current max profit: O(n!)
     * and if it is bigger than current Total max profit, deep copy to static schedule object (worst case): O(n^2)
     * Totally: O(n!)
     *
     * @param jobs
     * @param start
     */
    private void findMaxProfitSchedule(Job[] jobs, int start) {

        int size = jobs.length;

        if(start >= size - 1) {
            // create schedule
            Schedule sc = new Schedule();
            // it will schedule jobs and calculate total profit
            sc = scheduleJobs(sc);
            // compare current schedule profit with current max profit
            if(sc.profit > maxProfit) {
                // if it is bigger than current profit, update max profit
                maxProfit = sc.profit;
                // initialize index, brute force schedule object
                int i = 0;
                bruteForceSchedule = new Schedule();
                // deep copy the current schedule jobs array list
                for (Job j : sc.schedule) {
                    bruteForceSchedule.schedule.add(new Job(j.jobNumber, j.length, j.deadline, j.profit));
                    bruteForceSchedule.schedule.get(i).start = j.start;
                    bruteForceSchedule.schedule.get(i).finish = j.finish;
                    i++;
                }
            }
            return;
        }
        // driver to try all the combinations (permutation)
        else {
            for (int i = start; i < size; i++) {
                // Swap those jobs at indices start and i
                Job temp = jobs[i];
                jobs[i] = jobs[start];
                jobs[start] = temp;

                // Recurse on the sub array of jobs [start+1 .... end]
                findMaxProfitSchedule(jobs, start + 1);

                // Swap the jobs back
                temp = jobs[start];
                jobs[start] = jobs[i];
                jobs[i] = temp;

            }
        }

    }

    /**
     * Earliest deadline first schedule. Schedule items contributing 0 to total profit last.
     * T(n) = O(n^2)
     *
     * @return Earliest Deadline First schedule.
     */
    public Schedule makeScheduleEDF()
    {
        // create edf_schedule object
        Schedule EDF_Schedule = new Schedule();

        // Sort items by deadline from small to large.
        int tempIndex;
        
        for (int i = 0; i < nJobs; i++)
        {
            int earliestDeadlineJobIndex = i;

            for (int j = i + 1; j < nJobs; j++) {
                if (jobs[j].deadline < jobs[earliestDeadlineJobIndex].deadline) {
                    tempIndex = j;
                    swap(tempIndex, i, jobs);
                }
            }
        }

        return scheduleJobs(EDF_Schedule);
    }

    /**
     * sort the jobs in shortest job(length) first order if each job unable to finish before the deadline,
     * it move to end of array.
     * T(n) = O(n^2)
     *
     * @return Shortest Job FIrst schedule.
     */
    public Schedule makeScheduleSJF()
    //shortest job first schedule. Schedule items contributing 0 to total profit last
    {
        // create sjf_schedule object
        Schedule SJF_Schedule = new Schedule();

        // Sort items by deadline from small to large = O(n).
        int tempIndex;

        for (int i = 0; i < nJobs; i++) {
            for (int j = i + 1; j < nJobs; j++) {
                if (jobs[j].length < jobs[i].length) {
                    tempIndex = j;
                    swap(tempIndex, i, jobs);
                }
            }
        }
        
        return scheduleJobs(SJF_Schedule);
    }

    /**
     * highest profit first schedule. Schedule items contributing 0 to total profit last
     * T(n) = O(n^2)
     *
     * @return  Highest Profit first schedule.
     *
     */
    public Schedule makeScheduleHPF()
    {
        // create hpf_schedule object,
        Schedule HPF_Schedule = new Schedule();

        // Sort items by deadline from small to large.
        int tempIndex;
        
        for (int i = 0; i < nJobs; i++) {
            for (int j = i + 1; j < nJobs; j++) {
                if (jobs[j].profit > jobs[i].profit) {
                    tempIndex = j;
                    swap(tempIndex, i, jobs);
                }
            }
        }

        return scheduleJobs(HPF_Schedule);
    }

    /**
     * Helper function to do figure out if jobs finish by their deadline, adding the profit to the sum if they do.
     * Time complexity: O(2n) = O(n)
     * @param schedule We pass in the schedule for a particular scheduling algorithm.
     * @return We return the final schedule for that scheduling algorithm.
     */
    public Schedule scheduleJobs(Schedule schedule)
    {
        // temporary job array list
        Schedule temp_jobs = new Schedule();

        //schedule jobs
        int startTime = 0;
        int profits = 0;
        for (int i = 0; i < nJobs; i++)
        {
            // if the job can finish earlier than deadline
            // update profits, and schedule jobs
            if ((startTime + jobs[i].length) <= jobs[i].deadline)
            {
                jobs[i].start = startTime;
                jobs[i].finish = startTime + jobs[i].length;
                startTime = jobs[i].finish;
                profits = profits + jobs[i].profit;
                schedule.add(jobs[i]);
            }

            // Move the job to a temporary array list if it can't be done by the deadline.
            else
            {
                temp_jobs.add(jobs[i]);
            }
        }
        // schedule 0 profit for jobs that don't meet the deadline
        // and place the temporary jobs at the end of the schedule
        for (int i = 0; i < temp_jobs.schedule.size(); i++)
        {
            temp_jobs.schedule.get(i).start = startTime;
            temp_jobs.schedule.get(i).finish = startTime + temp_jobs.schedule.get(i).length;
            startTime = temp_jobs.schedule.get(i).finish;
            schedule.add(temp_jobs.schedule.get(i));
        }

        // update profit of this schedule
        schedule.profit = profits;

        return schedule;                                                                             
    }
    
    /**
     * Scheduling algorithm that organizes the jobs based on the ratio of profit to job length. Takes O(n^2) time.
     * @return Schedule based on ratio of profit to job length.
     */
    public Schedule newApproxSchedule() //Your own creation. Must be <= O(n3)
    {
        // create newApproxSchedule object,
        Schedule newApprox_Schedule = new Schedule();

        // Sort items based on the the ratio of profit to job length.
        int tempIndex;

        for (int i = 0; i < nJobs; i++) {
            for (int j = i + 1; j < nJobs; j++) {
                if ((jobs[j].profit / jobs[j].length) > (jobs[i].profit / jobs[i].length)
                        && jobs[j].deadline < jobs[i].deadline)
                {
                    tempIndex = j;
                    swap(tempIndex, i, jobs);
                }
            }
        }

        return scheduleJobs(newApprox_Schedule);
    }

    /**
     * Helper function to swap two elements in the job array.
     *
     * @param item1
     * @param item2
     * @param a
     */
    private static void swap(int item1, int item2, Job a[])
    {
        Job tempItem = a[item1]; // item1 into tempItem, tempItem is used to swap elements in the array;
        a[item1] = a[item2];     // item2 into item1
        a[item2] = tempItem;     // tempItem into item2
    } // end swap


    //---------------------------Include Job and Schedule classes in JobScheduler. java-----------------------------
    class Job {
        int jobNumber;
        int length;
        int deadline;
        int profit;
        int start;
        int finish;


        public Job(int jn, int len, int d, int p) {
            jobNumber = jn;
            length = len;
            deadline = d;
            profit = p;
            start = -1;
            finish = -1;
        }


        public String toString() {
            return "#" + jobNumber + ":(" + length + ","
                    + deadline + "," + profit +
                    "," + start + "," + finish + ")";
        }


    }//end of Job class

    // ----------------------------------------------------
    class Schedule {
        ArrayList<Job> schedule;
        int profit;

        public Schedule() {
            profit = 0;
            schedule = new ArrayList<Job>();
        }

        public void add(Job job) {
            schedule.add(job);
        }

        public int getProfit() {
            return profit;
        }

        public String toString() {
            String s = "Schedule Profit = " + profit;
            for (int k = 0; k < schedule.size(); k++) {
                s = s + "\n" + schedule.get(k);

            }

            return s;
        }
    }// end of Schedule class

    
    public static void main(String[] args)
    {
        // Test Case A:
        int[] lengthA = { 7,4,2,5};
        int[] deadlineA = {7 ,16 ,8, 10};
        int[] profitA = { 10, 9, 14, 13};
        JobScheduler js = new JobScheduler(lengthA, deadlineA, profitA);

        System.out.println("Test Case A:\n");
        System.out.println("Jobs to be scheduled");
        System.out.println("Job format is " +
                "(length, deadline, profit, start, finish)");
        js.printJobs();

        //---------------------------------------
        System.out.println("\nOptimal Solution Using Brute Force O(n!)");
        Schedule bestSchedule = js.bruteForceSolution();
        System.out.println(bestSchedule);

        //---------------------------------------
        System.out.println("\nEDF with unprofitable jobs last ");
        Schedule EDFPSchedule = js.makeScheduleEDF();
        System.out.println(EDFPSchedule);

        //-------------------------------------
        System.out.println("\nSJF with unprofitable jobs last");
        Schedule SJFPSchedule = js.makeScheduleSJF();
        System.out.println(SJFPSchedule);

        //--------------------------------------------
        System.out.println("\nHPF with unprofitable jobs last");
        Schedule HPFSchedule = js.makeScheduleHPF();
        System.out.println(HPFSchedule);

        // ------------------------------
        System.out.println("\nYour own creative solution");
        Schedule NASSchedule = js.newApproxSchedule();
        System.out.println(NASSchedule);

        // Test Case B:
        int[] lengthB = {2,3,1,10,7,4,2,5,7,7};
        int[] deadlineB = {10,12, 9 ,22,  10, 4, 18, 15, 5, 9};
        int[] profitB = {2,5,13,28,9,14, 2, 7, 3, 10};
        JobScheduler jsB = new JobScheduler(lengthB, deadlineB, profitB);

        System.out.println("Test Case B:\n");
        System.out.println("Jobs to be scheduled");
        System.out.println("Job format is " +
                "(length, deadline, profit, start, finish)");
        jsB.printJobs();

        //---------------------------------------
        System.out.println("\nOptimal Solution Using Brute Force O(n!)");
        Schedule bestScheduleB = jsB.bruteForceSolution();
        System.out.println(bestScheduleB);

        //---------------------------------------
        System.out.println("\nEDF with unprofitable jobs last ");
        Schedule EDFPScheduleB = jsB.makeScheduleEDF();
        System.out.println(EDFPScheduleB);

        //-------------------------------------
        System.out.println("\nSJF with unprofitable jobs last");
        Schedule SJFPScheduleB = jsB.makeScheduleSJF();
        System.out.println(SJFPScheduleB);

        //--------------------------------------------
        System.out.println("\nHPF with unprofitable jobs last");
        Schedule HPFScheduleB = jsB.makeScheduleHPF();
        System.out.println(HPFScheduleB);

        // ------------------------------
        System.out.println("\nYour own creative solution");
        Schedule NASScheduleB = jsB.newApproxSchedule();
        System.out.println(NASScheduleB);

        // Test Case C:
        int[] lengthC = {2,3,1,10,7,  4,6,9,3,2,  5,2,5,7,7,  6,3,7,8,4,  5,2,9,10,5};
        int[] deadlineC = {10,12,15,8,10,  9,22,12,15,35,  29,32,45,41,13,
                16,10,20,10,4,  18,15,5,9, 30 };
        int[] profitC = {2,5,13,28,8, 7,6,5,3,4,  9,7,6,9,14,  2,7,11,3,10,
                8,5,9,10,3};
        JobScheduler jsC = new JobScheduler(lengthC, deadlineC, profitC);

        System.out.println("Test Case C:\n");
        System.out.println("Jobs to be scheduled");
        System.out.println("Job format is " +
                "(length, deadline, profit, start, finish)");
        jsC.printJobs();

        // Do not run brute force algorithm since the test case has 25 jobs.

        //---------------------------------------
        System.out.println("\nEDF with unprofitable jobs last ");
        Schedule EDFPScheduleC = jsC.makeScheduleEDF();
        System.out.println(EDFPScheduleC);

        //-------------------------------------
        System.out.println("\nSJF with unprofitable jobs last");
        Schedule SJFPScheduleC = jsC.makeScheduleSJF();
        System.out.println(SJFPScheduleC);

        //--------------------------------------------
        System.out.println("\nHPF with unprofitable jobs last");
        Schedule HPFScheduleC = jsC.makeScheduleHPF();
        System.out.println(HPFScheduleC);

        // ------------------------------
        System.out.println("\nYour own creative solution");
        Schedule NASScheduleC = jsC.newApproxSchedule();
        System.out.println(NASScheduleC);
    }

}//end of JobScheduler class
