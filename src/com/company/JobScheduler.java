/*
    Project #2: Job Scheduler
    Description: Create three java classes to solve the job scheduling problem described.
    Class: COMP496 ALG 11:00AM, Professor Schwartz
    Team Members: Inkyu Park, Armand Abrahamian
    Date: April 6, 2017
 */

package com.company;

import java.util.ArrayList;

public class JobScheduler
{

        private int nJobs;
        private Job[] jobs;

        /**
         * Constructor for JobScheduler.
         * @param joblength the time needed to complete each job.
         * @param deadline  the deadline of each job
         * @param profit    the amount of profit for each job
         */
        public JobScheduler( int[] joblength, int[] deadline, int[] profit)
        {
            //Set nJobs
            nJobs = joblength.length;

            //Fill jobs array. The kth job entered has JobNo = k;
            jobs = new Job[nJobs];
            for(int index = 0; index < nJobs; index++)
            {
                jobs[index] = new Job(index, joblength[index], deadline[index], profit[index]);
            }
        }

        public void printJobs()  //prints the array jobs
        {
            for(int index = 0; index < nJobs; index++)
            {
                System.out.println(jobs[index].toString());
            }
        }

        //Brute force. Try all n! orderings. Return the schedule with the most profit
        public Schedule bruteForceSolution()
        {
            Schedule bruteForce_Schedule = new Schedule();
            
            return bruteForce_Schedule;
        }


        public Schedule makeScheduleEDF()
        //earliest deadline first schedule. Schedule items contributing 0 to total profit last
        {
            Schedule EDF_Schedule = new Schedule();
            int tempIndex;

            // Sort items by deadline from small to large.
            for(int i = 0; i < nJobs; i++)
            {
                int earliestDeadlineJobIndex = i;

                for(int j = i + 1; j < nJobs; j++)
                {
                    if(jobs[j].deadline < jobs[earliestDeadlineJobIndex].deadline)
                    {
                        tempIndex = j;
                        swap(tempIndex, i, jobs);
                    }
                }

            }
            System.out.println("EDF");
            printJobs();
            return EDF_Schedule;
        }

    /**
     * sort the jobs in shortest job(length) first order
     * if each job unable to finish before the deadline,
     * it move to end of array.
     * @return
     */
    public Schedule makeScheduleSJF()
        //shortest job first schedule. Schedule items contributing 0 to total profit last
        {
            // create sjf_schedule object, temporary job array list
            Schedule SJF_Schedule = new Schedule();
            Schedule temp_jobs = new Schedule();

            // for sort
            int tempIndex;

            // Sort items by deadline from small to large.
            for(int i = 0; i < nJobs; i++)
            {
                for(int j = i + 1; j < nJobs; j++)
                {
                    if(jobs[j].length < jobs[i].length)
                    {
                        tempIndex = j;
                        swap(tempIndex, i, jobs);
                    }
                }
            }

            //schedule jobs
            int startTime = 0;
            int profits = 0;
            for (int i = 0; i < nJobs; i++) {

                // if the job can finish earlier than deadline
                // update profits, and add to schedule
                if((startTime + jobs[i].length) <= jobs[i].deadline) {
                    jobs[i].start = startTime;
                    jobs[i].finish = startTime + jobs[i].length;
                    startTime = jobs[i].finish;
                    profits = profits + jobs[i].profit;
                    SJF_Schedule.add(jobs[i]);
                }

                // we move to temporary array list if it can't be done in deadline
                else {
                    temp_jobs.add(jobs[i]);
                }

            }
            // schedule 0 profit jobs
            // and put temporary jobs to end of schedule
            for (int i = 0; i < temp_jobs.schedule.size(); i++) {
                temp_jobs.schedule.get(i).start = startTime;
                temp_jobs.schedule.get(i).finish = startTime + temp_jobs.schedule.get(i).length;
                startTime = temp_jobs.schedule.get(i).finish;
                SJF_Schedule.add(temp_jobs.schedule.get(i));
            }

            // update profit of this schedule
            SJF_Schedule.profit = profits;

            return SJF_Schedule;

        }

        public Schedule makeScheduleHPF()
        //highest profit first schedule. Schedule items contributing 0 to total profit last
        {
            // create hpf_schedule object, temporary job array list
            Schedule HPF_Schedule = new Schedule();
            Schedule temp_jobs = new Schedule();

            // for sort
            int tempIndex;

            // Sort items by deadline from small to large.
            for(int i = 0; i < nJobs; i++)
            {
                for(int j = i + 1; j < nJobs; j++)
                {
                    if(jobs[j].profit > jobs[i].profit)
                    {
                        tempIndex = j;
                        swap(tempIndex, i, jobs);
                    }
                }
            }

            //schedule jobs
            int startTime = 0;
            int profits = 0;
            for (int i = 0; i < nJobs; i++) {

                // if the job can finish earlier than deadline
                // update profits, and schedule jobs
                if((startTime + jobs[i].length) <= jobs[i].deadline) {
                    jobs[i].start = startTime;
                    jobs[i].finish = startTime + jobs[i].length;
                    startTime = jobs[i].finish;
                    profits = profits + jobs[i].profit;
                    HPF_Schedule.add(jobs[i]);
                }

                // we move to temporary array list if it can't be done in deadline
                else {
                    temp_jobs.add(jobs[i]);
                }

            }
            // schedule 0 profit jobs
            // and put temporary jobs to end of schedule
            for (int i = 0; i < temp_jobs.schedule.size(); i++) {
                temp_jobs.schedule.get(i).start = startTime;
                temp_jobs.schedule.get(i).finish = startTime + temp_jobs.schedule.get(i).length;
                startTime = temp_jobs.schedule.get(i).finish;
                HPF_Schedule.add(temp_jobs.schedule.get(i));
            }

            // update profit of this schedule
            HPF_Schedule.profit = profits;

            return HPF_Schedule;
        }


        public Schedule newApproxSchedule() //Your own creation. Must be <= O(n3)
        {
            return null;
        }

        /**
         *  Helper function to swap two elements in the array.
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
    class Job
    {
        int jobNumber;
        int length;
        int deadline;
        int profit;
        int start;
        int finish;


        public Job( int jn , int len, int d, int p)
        {
            jobNumber = jn; length = len; deadline = d;
            profit = p;  start = -1;  finish = -1;
        }


        public String toString()
        {
            return "#" + jobNumber + ":(" + length + ","
                    + deadline + "," + profit +
                    "," + start + "," + finish + ")";
        }

    }//end of Job class

    // ----------------------------------------------------
    class Schedule
    {
        ArrayList<Job> schedule;
        int profit;

        public Schedule()
        {
            profit = 0;
            schedule = new ArrayList<Job>();
        }

        public void add(Job job)
        {
            schedule.add(job);
        }

        public int getProfit()
        {
            return profit;
        }

        public String toString()
        {
            String s = "Schedule Profit = " + profit ;
            for(int k = 0 ; k < schedule.size(); k++)
            {
                s = s + "\n"  + schedule.get(k);

            }

            return s;
        }
    }// end of Schedule class


    public static void main(String[] args)
    {
	    // write your code here
        int[] length = {7,4,2,5};
        int[] deadline = {7 ,16 ,8, 10};
        int[] profit = {10, 9, 14, 13};
        JobScheduler js = new JobScheduler(length, deadline, profit);
        System.out.println("Jobs to be scheduled");
        System.out.println("Job format is " +
                "(length, deadline, profit, start, finish)" );
        js.printJobs();

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
    }

}//end of JobScheduler class
