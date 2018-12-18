/*Name:Victor Li
  Last modified: 4/20/18
  Class: Operating Systems(CISC. 3320-ET6)
  Assignment 3                         */

/*
    Programming Exercise 3.20 required you to design a PID manager that allocated a unique process
    identifier to each process. Exercise 4.20 required you to modify your solution to Exercise 3.20 by writing
    a program that created a number of threads that requested and released process identifiers. Now
    modify your solution to Exercise 4.20 by ensuring that the data structure used to represent the
    availability of process identifiers is safe from race conditions. Use Pthreads mutex locks.
    Please note.
    If you used mutex locks in your solution to HW #2, please resubmit to get credit for HW#3 */

//NOTE: ALL OUTPUT IS PRINTED IN "VictorLi_output.txt"
//NOTE: GIVE THE PROGRAM 20-30 SECONDS TO FINISH ITS EXECUTION.
//NOTE: CODE WAS COMPILED ON INTELLIJ IDEA.

/*This program is a modified version of Assignment 2 program.
  First 3 notepad process and requests for a pid. After a random period of seconds it releases its pid and terminates its process.
  Subsequently, 10 threads are created and requests for a pid. After a random period of seconds it releases its pid.
  Mutex lock is implemented into the run method to prevent race conditions.*/


import sun.awt.Mutex;
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
    static Mutex m = new Mutex();


    public void run()
    {
        m.lock();
        writer.println("\nTHREAD " + numThread + " EXECUTING: " +  "Locking.");
        try {
            Random r = new Random();
            int randomPid = allocate_pid();
            writer.println("\nTHREAD " + numThread + " EXECUTING: " + "Requested pid: " + randomPid );
            int randomTime = r.nextInt(2001) + 1000;                  //Generates random second from 1 to 3 seconds.
            long sleep = (long) randomTime;
            thread.sleep(sleep);
            writer.println("THREAD " + numThread + " EXECUTING: " + "Before thread "+ numThread +  " with pid " + randomPid+": " + booleanArray[randomPid]);
            writer.print("THREAD " + numThread + " EXECUTING: " );
            release_pid(randomPid);
            writer.println("THREAD " + numThread + " EXECUTING: " +  "After thread " + numThread + " with pid " + randomPid+ ": " + booleanArray[randomPid]);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            writer.println("\nTHREAD " + numThread + " EXECUTING: " + " Unlocking.\n");
            writer.println("===================================================================");
            numThread++;
            m.unlock();
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

    public static void main(String[] args) {
        try {

            PidManager pidMan = new PidManager();

            writer = new PrintWriter("VictorLi_output.txt");

            //Initializing 2 arrays in allocate_map.
            if (pidMan.allocate_map() == 1)
                writer.println("MAIN THREAD EXECUTING: " +"Successful allocation.\n");
            else
                writer.println("MAIN THREAD EXECUTING: " +"Failed allocation.\n");
            ProcessBuilder processbuilder = new ProcessBuilder("notepad.exe");

            //Creating first process
            int pid1 = pidMan.allocate_pid();
            if (pid1 == -1)
                writer.println("MAIN THREAD EXECUTING: " +"Allocation has failed. No available pid at the moment.");
            else {
                writer.println("MAIN THREAD EXECUTING: " +"Creating a notepad process with pid " + pid1);
                ;
                Process process1 = processbuilder.start();
                pidMan.processArray[pid1] = process1;
            }

            //Creating second process
            int pid2 = pidMan.allocate_pid();
            if (pid2 == -1)
                writer.println("MAIN THREAD EXECUTING: " +"Allocation has failed. No available pid at the moment.");
            else {
                writer.println("MAIN THREAD EXECUTING: " +"Creating a notepad process with pid " + pid2);
                Process process2 = processbuilder.start();
                pidMan.processArray[pid2] = process2;
            }

            //Creating third process
            int pid3 = pidMan.allocate_pid();
            if (pid3 == -1)
                writer.println("MAIN THREAD EXECUTING: " +"Allocation has failed. No available pid at the moment.");
            else {
                writer.println("MAIN THREAD EXECUTING: " +"Creating a notepad process with pid " + pid3);
                Process process3 = processbuilder.start();
                pidMan.processArray[pid3] = process3;
            }

            //Terminating first process
            int counter = 1;
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("MAIN THREAD EXECUTING: Process " + counter);
            counter++;
            writer.println("MAIN THREAD EXECUTING: " +"Before releasing  booleanArray [" + pid1 + "]:" + pidMan.booleanArray[pid1]);
            writer.println("MAIN THREAD EXECUTING: " +"Before releasing  processArray [" + pid1 + "]:" + pidMan.processArray[pid1].isAlive());
            pidMan.release_pid(pid1);
            pidMan.processArray[pid1].destroy();
            writer.println("MAIN THREAD EXECUTING: " +"After releasing  booleanArray [" + pid1 + "]:" + pidMan.booleanArray[pid1]);
            writer.println("MAIN THREAD EXECUTING: " +"After releasing  processArray [" + pid1 + "]:" + pidMan.processArray[pid1].isAlive());

            //Terminating second process
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("MAIN THREAD EXECUTING: Process " + counter);
            counter++;
            writer.println("MAIN THREAD EXECUTING: " +"Before releasing  booleanArray [" + pid2 + "]:" + pidMan.booleanArray[pid2]);
            writer.println("MAIN THREAD EXECUTING: " +"Before releasing  processArray [" + pid2 + "]:" + pidMan.processArray[pid2].isAlive());
            pidMan.release_pid(pid2);
            pidMan.processArray[pid2].destroy();
            writer.println("MAIN THREAD EXECUTING: " +"After releasing  booleanArray [" + pid2 + "]:" + pidMan.booleanArray[pid2]);
            writer.println("MAIN THREAD EXECUTING: " +"After releasing  processArray [" + pid2 + "]:" + pidMan.processArray[pid2].isAlive());

            //Terminating third process
            writer.println("\n");
            Thread.sleep(3000);
            writer.println("MAIN THREAD EXECUTING: Process " + counter);
            counter++;
            writer.println("MAIN THREAD EXECUTING: " +"Before releasing  booleanArray [" + pid3 + "]:" + pidMan.booleanArray[pid3]);
            writer.println("MAIN THREAD EXECUTING: " + "Before releasing  processArray [" + pid3 + "]:" + pidMan.processArray[pid3].isAlive());
            pidMan.release_pid(pid3);
            pidMan.processArray[pid3].destroy();
            writer.println("MAIN THREAD EXECUTING: " + "After releasing  booleanArray [" + pid3 + "]:" + pidMan.booleanArray[pid3]);
            writer.println("MAIN THREAD EXECUTING: " + "After releasing  processArray [" + pid3 + "]:" + pidMan.processArray[pid3].isAlive());

            //Creating 10 Threads
            writer.println("\nMAIN THREAD EXECUTION: Creating 10 threads : ");
            for (int i = 1; i <= 10; i++) {
                pidMan.thread = new Thread(pidMan);
                pidMan.thread.start();
                writer.println("MAIN THREAD EXECUTING: "+"Thread " + i + " is created");
            }

        } catch (Exception e) {
            System.out.println("Exception caught in main.");
            e.printStackTrace();
        }


        //The code below waits until all threads finish execution to close the file.
        while( numThread < 10)
        {

        }
        if( numThread == 10)
        {
            try {
                Thread.sleep(3000);
            }
            catch(Exception e )
            {
                e.printStackTrace();
            }
            writer.close();
        }

    }

}


