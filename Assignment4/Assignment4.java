/*
  Name: Victor Li
  Last modified: 12/10/18
  Class: Operating Systems(CISC. 3320-ET6)
  Assignment 4



Write a program that implements the following disk-scheduling algorithms: (Pick three)

a. FCFS

b. SSTF

c. SCAN

d. C-SCAN

e, LOOK

f. C-LOOK

Your program will service a disk with 5,000 cylinders numbered 0 to 4,999.
The program will generate a random series of 50 requests and service them according to each of the algorithms you chose.
The program will be passed the initial position of the disk head as a parameter on the command line and report the total amount of head movement required for each algorithm.
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Assignment4 {

    static PrintWriter writer;

    public static void main (String [] args) throws IOException
    {
       writer= new PrintWriter("VictorLi_output.txt");


        int head = 1000;
        boolean [] cylinder  = new boolean [5000];
        int [] random50 = new int [50];
        Random random = new Random();

        //Generate 50 random numbers ranging from 0 - 4999
        for(int i = 0; i < 50; i++)
        {
            random50[i]= random.nextInt(5000);
        }

        // True = requested
        for(int i = 0 ; i < 50; i++)
        {
            cylinder[ random50[i] ] = true;
        }

        for (int i = 0; i <50 ; i++)
        {
            writer.println("Requesting position: " + random50[i]);
        }


        writer.println("\t");
        int fcfsMoves = FCFCS(random50,head);
        writer.println("FCFS total movement: " + fcfsMoves);
        int scanMoves = SCAN(cylinder, head);
        writer.println("SCAN total movement: " + scanMoves);
        int C_ScanMoves = C_SCAN(cylinder,head);
        writer.println("C_SCAN total movement: " + C_ScanMoves);
        writer.close();

    }

    public static int FCFCS( int[] rand50, int h)
    {
        writer.println("Using FCFS algorithm");
        int totalMovement = 0;
        int currentHead = h;


        for( int i = 0;  i < 50 ; i++)
        {
           totalMovement += Math.abs( currentHead - rand50[i]) ;
           currentHead = rand50[i];
        }
        return totalMovement;
    }

    public static int SCAN(boolean [] cylinder, int h) {
        int totalMovement = 0;
        int currentHead = h;
        boolean [] copyCylinder  = new boolean[5000];
        writer.println("Using SCAN algorithm");


        //Make a copy of cylinder array
        for(int i = 0; i < 4999; i++)
        {
            copyCylinder [i] = cylinder[i];
        }

        //Goes to the lower bound of the cylinder
        for (int i = currentHead; i > 0; i--) {
            if( copyCylinder[i] == true)
            {
                totalMovement += Math.abs(currentHead - i);
                currentHead = i;

                //Marking it false so no repeats when scan goes back around.
                copyCylinder[currentHead] = false;
            }
        }

        //Goes to the upper bound of the cylinder
        for (int i = currentHead; i < 5000; i++) {
            if( copyCylinder[i] == true)
            {
                totalMovement += Math.abs(currentHead - i);
                currentHead = i;

                copyCylinder[currentHead] = false;
            }
        }
        return totalMovement;
    }



    public static int C_SCAN(boolean [] cylinder, int h) {
        int totalMovement = 0;
        int currentHead = h;
        boolean [] copyCylinder  = new boolean[5000];
        writer.println("Using C_SCAN algorithm");

        //Make a copy of cylinder array
        for(int i = 0; i < 4999; i++)
        {
            copyCylinder [i] = cylinder[i];
        }


        //Goes to the lower bound of the cylinder
        for (int i = currentHead; i > 0; i--) {
            if( copyCylinder[i] == true)
            {
                totalMovement += Math.abs(currentHead - i);
                currentHead = i;

                //Marking it false so no repeats when scan goes back around.
                copyCylinder[currentHead] = false;
            }
        }
        totalMovement += 4999;
        currentHead = 4999;

        //Goes to the upper bound of the cylinder
        for (int i = currentHead; i >0 ; i--) {
            if( copyCylinder[i] == true)
            {
                totalMovement += Math.abs(currentHead - i);
                currentHead = i;

                copyCylinder[currentHead] = false;
            }
        }
        return totalMovement;
    }

}
