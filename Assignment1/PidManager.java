/*Name:Victor Li
  Last modified: 2/6/18
  Class: Operating Systems(CISC. 3320-ET6)
  Assignment 1                          */


      /*An operating system’s pid manager is responsible for managing process identifiers. When a process is
        first created, it is assigned a unique pid by the pid manager. The pid is returned to the pid manager
        when the process completes execution, and the manager may later reassign this pid. Process identifiers
        must be unique; no two active processes may have the same pid.
        Use the following constants to identify the range of possible pid values:
        #define MIN_PID 300
        #define MAX_PID 5000
        You may use any data structure of your choice to represent the availability of process identifiers. One
        strategy is to adopt what Linux has done and use a bitmap in which a value of 0 at position i indicates
        that a process id of value i is available and a value of 1 indicates that the process id is currently in use.
        Implement the following API for obtaining and releasing a pid:
        • int allocate_map(void) – Creates and initializes a data structure for representing pids;
        returns -1 if unsuccessful and 1 if successful
        • int allocate_pid(void) – Allocates and returns a pid; returns -1 if if unable to allocate a
        pid (all pids are in use)
        • void release_pid(int_pid) – Releases a pid.
        NOTE: This programming problem will be modified later in Chapters 4 and 5.*/

/*My approach to this problem is to use 2 arrays to associate with each other. One of the two arrays,booleanArray, represents
the availability of the PID by storing a boolean value. The second array is processArray. ProcessArray is used to store a process.
In this program three notepad processes are created with a unique PID  and then each terminated with a 5 second interval.*/

//NOTE: ALL OUTPUT IS PRINTED IN "VictorLi_output.txt"

import java.io.PrintWriter;
import java.lang.Process;
import java.util.Random;

public class PidManager
{
        static boolean booleanArray[];
        static Process processArray[];
        static final int MIN_PID = 300;
        static final int MAX_PID = 5000;
        static  boolean empty = true;                        //empty is used for booleanArray
        static PrintWriter writer;

    public static int allocate_map()
    {
        processArray = new Process [5001];                   //5001 positions for position 300 to 5000
        booleanArray = new boolean [5001];
        int position = 300;

        while((position != 5001) && empty == true )           //True = occupied,   False = available
        {
            if(booleanArray[position] == true)
            empty = false;

            position++;
        }
        if(empty)
            return 1;
        else
            return -1;
    }

   public static int allocate_pid()
   {
       boolean full = true;
       int position = 300;

       while( position <= 5001 && full == true)              // Check if the booleanArray is full from position 300 to 5000.
       {
           if(booleanArray[position] == false)
               full = false;
           position++;
       }
       if(!full)
       {
           int pid = 0;
           boolean avialablePID = false;
           while(!avialablePID) {
               Random rand = new Random();
               pid = rand.nextInt(MAX_PID - MIN_PID + 1) + MIN_PID;  //Generates random number from 300 to 5000
               if(booleanArray[pid] == false)
                   avialablePID = true;
           }
           booleanArray[pid] = true;
           empty = false;
           return pid;
       }
       else
           return -1;
       }

    public static void release_pid(int int_pid)
    {

        if(!empty)
        {
            booleanArray[int_pid] = false;
            processArray[int_pid].destroy();
            writer.println("PID "+ int_pid + " is released.");
            int numFreeSpace = 0;

            for(int i = 300; i < 5001; i++)                              //After releasing pid it checks if the booleanArray is completely empty
            {
                if(booleanArray[i] == false )
                    numFreeSpace++;
            }
            if(numFreeSpace == (MAX_PID-MIN_PID+1) )                      //If numFreeSpace equal to 4701 the booleanArray is empty
                empty = true;
        }
        else
            writer.println("There are no occupied pid to be release.");

    }

    public static void main(String[] args)
    {
        try {
            writer = new PrintWriter("VictorLi_output.txt");

            //Initializing 2 arrays in allocate_map.
            if(allocate_map() == 1)
                writer.println("Successful allocation.\n");
            else
                writer.println("Failed allocation.\n");

           ProcessBuilder processbuilder = new ProcessBuilder("notepad.exe");

           //Creating first process
           int pid1 = allocate_pid();
           if(pid1 == -1)
               writer.println("Allocation has failed. No available pid at the moment.");
           else {
               writer.println("Creating a notepad process with pid " + pid1);;
               Process process1 = processbuilder.start();
               processArray [pid1] = process1;
           }

           //Creating second process
           int pid2 = allocate_pid();
           if(pid2 == -1)
               writer.println("Allocation has failed. No available pid at the moment.");
           else {
               writer.println("Creating a notepad process with pid " + pid2);
               Process process2 = processbuilder.start();
               processArray [pid2] = process2;
            }

            //Creating thrid process
            int pid3 = allocate_pid();
            if(pid3 == -1)
                writer.println("Allocation has failed. No available pid at the moment.");
            else {
                writer.println("Creating a notepad process with pid " + pid3);
                Process process3 = processbuilder.start();
                processArray[pid3] = process3;
            }

            //Terminating first process
            writer.println("\n");
            Thread.sleep(5000);
            writer.println("Before releasing  booleanArray ["+ pid1 + "]:" + booleanArray[pid1]);
            writer.println("Before releasing  processArray ["+ pid1 + "]:" + processArray[pid1].isAlive());
            release_pid(pid1);
     //       processArray[pid1].destroy();
            writer.println("After releasing  booleanArray ["+ pid1 + "]:" + booleanArray[pid1]);
            writer.println("After releasing  processArray ["+ pid1 + "]:" + processArray[pid1].isAlive());

            //Terminating second process
            writer.println("\n");
            Thread.sleep(5000);
            writer.println("Before releasing  booleanArray ["+ pid2 + "]:" + booleanArray[pid2]);
            writer.println("Before releasing  processArray ["+ pid2 + "]:" + processArray[pid2].isAlive());
            release_pid(pid2);
         //   processArray[pid2].destroy();
            writer.println("After releasing  booleanArray ["+ pid2 + "]:" + booleanArray[pid2]);
            writer.println("After releasing  processArray ["+ pid2 + "]:" + processArray[pid2].isAlive());

            //Terminating third process
            writer.println("\n");
            Thread.sleep(5000);
            writer.println("Before releasing  booleanArray ["+ pid3 + "]:" + booleanArray[pid3]);
            writer.println("Before releasing  processArray ["+ pid3 + "]:" + processArray[pid3].isAlive());
            release_pid(pid3);
          //  processArray[pid3].destroy();
            writer.println("After releasing  booleanArray ["+ pid3 + "]:" + booleanArray[pid3]);
            writer.println("After releasing  processArray ["+ pid3 + "]:" + processArray[pid3].isAlive());

            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception caught in main.");
            e.printStackTrace();
        }
    }
}


