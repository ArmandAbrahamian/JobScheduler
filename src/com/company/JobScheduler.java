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
            int tempIndex = 0;

            // Sort items by deadline from small to large.
            for(int i = 0; i < nJobs; i++)
            {
                int earliestDeadlineJobIndex = i;

                for(int j = i + 1; j < nJobs; j++)
                {
                    if(jobs[j].deadline < jobs[earliestDeadlineJobIndex].deadline)
                    {
                        tempIndex = j;
                    }
                }
                
                swap(tempIndex, i, jobs);
            }

            return EDF_Schedule;
        }

        public Schedule makeScheduleSJF()
        //shortest job first schedule. Schedule items contributing 0 to total profit last
        {
            Schedule SJF_Schedule = new Schedule();

            return SJF_Schedule;
        }

        public Schedule makeScheduleHPF()
        //highest profit first schedule. Schedule items contributing 0 to total profit last
        {
            Schedule HPF_Schedule = new Schedule();

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
    }

}//end of JobScheduler class
