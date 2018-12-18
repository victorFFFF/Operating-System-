/*Name:Victor Li
  Last modified: 3/11/18
  Class: Operating Systems(CISC. 3320-ET6)
  Assignment 2                          */

/*
      Modify HW#1. Write a multithreaded program that tests your solution to HW#1. You will create several threads – for
              example, 100 – and each thread will request a pid, sleep for a random period of time, and then release
              the pid. (Sleeping for a random period approximates the typical pid usage in which a pid is assigned to a
              new process, the process executes and terminates, and the pid is released on the process’ termination).
              On UNIX and Linux systems, sleeping is accomplished through the sleep() function, which is passed an
              integer value representing the number of seconds to sleep. */

//NOTE: ALL OUTPUT IS PRINTED IN "VictorLi_output.txt"
//NOTE: GIVE THE PROGRAM 20-30 SECONDS TO FINISH ITS EXECUTION.
//NOTE: NOT ALL PRINT LINE IS NOT GOING TO BE ALIGN AND IN ORDER DUE TO RUN() NOT IMPLEMENTING SYNCHRONIZATION.

/*This program is a modified version of Assignment 1 program.
  First 3 notepad process and requests for a pid. After a random period of seconds it releases its pid and terminates its process.
  Subsequently, 100 threads are created and requests for a pid. After a random period of seconds it releases its pid. */


import java.io.PrintWriter;
import java.lang.Process;
import java.util.Random;

public class PidManager extends Thread
{
    boolean booleanArray[];                        //The array booleanArray is a associate array.Its processArray.
    Process processArray[];                       //The array processArray is a associate array. Its associated with booleanArray
    final int MIN_PID = 300;
    final int MAX_PID = 5000;
    boolean empty = true;                        //empty is used for booleanArray
    static PrintWriter writer;
    Thread thread;
    static int numThread = 1;


    public void run()
    {
        try {
            Random r = new Random();
            int randomPid = allocate_pid();
            writer.println("Requested pid: " + randomPid );
            int randomTime = r.nextInt(2001) + 3000;                  //Generates random second from 5 to 3 seconds.
            long sleep = (long) randomTime;
            thread.sleep(sleep);
            writer.println("\n");
            writer.println("Before releasing thread "+ numThread +  " with pid " + randomPid+": " + booleanArray[randomPid]);
            release_pid(randomPid);
            writer.println("After releasing thread "+ numThread + " with pid " + randomPid+ ": " + booleanArray[randomPid]);
            writer.println("\n");
            numThread++;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public  int allocate_map()
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

    public  int allocate_pid()
    {
        boolean full = true;
        int position = 300;

        while( position <= 5001 && full == true)              // Check if the booleanArray is full from position 300 to 5000.
        {
            if(booleanArray[position] == false)
                full = false;
            else
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

    public  void release_pid(int int_pid)
    {

        if(!empty)
        {
            booleanArray[int_pid] = false;
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

            PidManager pidMan = new PidManager();

            writer = new PrintWriter("VictorLi_output.txt");

            //Initializing 2 arrays in allocate_map.
            if(pidMan.allocate_map() == 1)
                writer.println("Successful allocation.\n");
            else
                writer.println("Failed allocation.\n");
            ProcessBuilder processbuilder = new ProcessBuilder("notepad.exe");

            //Creating first process
            int pid1 = pidMan.allocate_pid();
            if(pid1 == -1)
                writer.println("Allocation has failed. No available pid at the moment.");
            else {
                writer.println("Creating a notepad process with pid " + pid1);;
                Process process1 = processbuilder.start();
                pidMan.processArray [pid1] = process1;
            }

            //Creating second process
            int pid2 = pidMan.allocate_pid();
            if(pid2 == -1)
                writer.println("Allocation has failed. No available pid at the moment.");
            else {
                writer.println("Creating a notepad process with pid " + pid2);
                Process process2 = processbuilder.start();
                pidMan. processArray [pid2] = process2;
            }

            //Creating third process
            int pid3 = pidMan.allocate_pid();
            if(pid3 == -1)
                writer.println("Allocation has failed. No available pid at the moment.");
            else {
                writer.println("Creating a notepad process with pid " + pid3);
                Process process3 = processbuilder.start();
                pidMan.processArray[pid3] = process3;
            }

            //Terminating first process
            int counter = 1;
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("Process " + counter);
            counter++;
            writer.println("Before releasing  booleanArray ["+ pid1 + "]:" + pidMan.booleanArray[pid1]);
            writer.println("Before releasing  processArray ["+ pid1 + "]:" + pidMan.processArray[pid1].isAlive());
            pidMan.release_pid(pid1);
            pidMan.processArray[pid1].destroy();
            writer.println("After releasing  booleanArray ["+ pid1 + "]:" + pidMan.booleanArray[pid1]);
            writer.println("After releasing  processArray ["+ pid1 + "]:" + pidMan.processArray[pid1].isAlive());

            //Terminating second process
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("Process " + counter);
            counter++;
            writer.println("Before releasing  booleanArray ["+ pid2 + "]:" + pidMan.booleanArray[pid2]);
            writer.println("Before releasing  processArray ["+ pid2 + "]:" + pidMan.processArray[pid2].isAlive());
            pidMan.release_pid(pid2);
            pidMan.processArray[pid2].destroy();
            writer.println("After releasing  booleanArray ["+ pid2 + "]:" + pidMan.booleanArray[pid2]);
            writer.println("After releasing  processArray ["+ pid2 + "]:" + pidMan.processArray[pid2].isAlive());

            //Terminating third process
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("Process " + counter);
            counter++;
            writer.println("Before releasing  booleanArray ["+ pid3 + "]:" + pidMan.booleanArray[pid3]);
            writer.println("Before releasing  processArray ["+ pid3 + "]:" + pidMan.processArray[pid3].isAlive());
            pidMan.release_pid(pid3);
            pidMan.processArray[pid3].destroy();
            writer.println("After releasing  booleanArray ["+ pid3 + "]:" + pidMan.booleanArray[pid3]);
            writer.println("After releasing  processArray ["+ pid3 + "]:" + pidMan.processArray[pid3].isAlive());

            //Creating 100 Threads
            writer.println("\nCreating 100 threads : ");
            for(int i = 1; i <= 100 ; i++) {
                pidMan.thread = new Thread(pidMan);
                pidMan.thread.start();
                writer.println("Thread " + i + " is created");
            }

        }
        catch(Exception e)
        {
            System.out.println("Exception caught in main.");
            e.printStackTrace();
        }
        finally{
            try {
                Thread.sleep(10000);                            //Wait 10 seconds to close file so the 100 threads can finish executing.
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            writer.close();
        }
    }

}


