package src;

import java.io.PrintWriter;
import java.lang.String;


/**
 * Implementation of the Runqueue interface using an Ordered Array.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class OrderedArrayRQ implements Runqueue {

    private class Proc{

        private String procLabel;
        private int virtualRuntime;

        public Proc(String procLabel, int virtualRuntime){
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
        }

        public String getLabel(){
            return this.procLabel;
        }

        public int getVirtualRuntime(){
            return this.virtualRuntime;
        }

    }

    private Proc[] procArray;
    private int procLength = 0;
    /**
     * Constructs empty queue
     */
    public OrderedArrayRQ() {
        // Implement Me
        procArray = new Proc[1];
        System.out.println(procArray);
    }  // end of OrderedArrayRQ()


    //returns index to put new value at
    public int binarySearchIndex(Proc[] array, int leftIndex, int rightIndex, int value){

        //midpoint is the left index plus half the difference between left and right
        if (rightIndex >= leftIndex){
            
            int mid = leftIndex + (rightIndex - leftIndex)/2;
            System.out.println("checking index " + mid + " in range " + leftIndex + " " + rightIndex);
            if (value == array[mid].virtualRuntime){
                System.out.println("found at " + mid);
                return mid;
            } else if (value > array[mid].virtualRuntime){
                System.out.println("searching left half");
                return binarySearchIndex(array, leftIndex, mid -1, value);
            } else if (value < array[mid].virtualRuntime){
                System.out.println("searching right half");
                return binarySearchIndex(array, mid + 1, rightIndex, value);
            }
        }
        System.out.println("not found" + leftIndex + " " + rightIndex);
        //leftIndex at this point is the index that the new value should be put at
        //even if they're the same value (this will push a proc with the same runtime but longer on queue closer to being dequeued)
        return leftIndex;
    }

    @Override
    public void enqueue(String procLabel, int vt) {
        //array empty
        if (procLength == 0)
        {
            procArray[0] = new Proc(procLabel, vt);
            procLength++;    
        }
        
        else 
        {
            Proc[] temp = new Proc[procLength + 1];

            //index at which enqueue'd proc should be placed
            int procIndex = binarySearchIndex(procArray, 0, procLength -1, vt);

            //copy across earlier values that weren't affected by enqueue
            System.arraycopy(procArray, 0, temp, 0, procIndex);
            //put new proc at index
            temp[procIndex] = new Proc(procLabel, vt);
            //added new proc so increase actual length by 1
            procLength++;
            //copy across remaining values from original array at (original index) + 1
            System.arraycopy(procArray, procIndex, temp, procIndex + 1, procLength - 1 - procIndex);

            System.out.print("procArray = ");
            for(int i =0; i < procLength -1; i++){
                System.out.print(procArray[i].getVirtualRuntime() + " ");
            }
            System.out.println();

            System.out.print("temp = ");
            for(int i=0; i < procLength; i++){
                System.out.print(temp[i].getVirtualRuntime() + " ");
            }
            System.out.println();

            //replace old array with new one
            procArray = temp;
        }

    } // end of enqueue()


    @Override
    public String dequeue() {
        if (procLength > 0){
            String procLabel = procArray[procLength- 1].getLabel();
            procArray[procLength - 1] = null;
            procLength--;
            return procLabel;
        }
        return ""; 
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
       for (int i =0; i < procLength; i++){
           if (procArray[i].getLabel().equals(procLabel)){
              return true;
           }
       }
        return false; 
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {

        //getprocessID returns index if found, -1 if not
        int procIndex = getProcessID(procLabel);

        if (procIndex >= 0){
            System.out.println(procIndex);
            System.arraycopy(procArray, procIndex + 1, procArray, procIndex, procLength - procIndex - 1);
            procLength--;
            return true;
        }
        
        return false; 
    } // end of removeProcess()

    @Override
    public int precedingProcessTime(String procLabel) {
        
        //getprocessID returns index if found, -1 if not
        int procIndex = getProcessID(procLabel);

        int preProcTime = 0;

        if(procIndex >=0){
            for(int i=procIndex + 1; i < procLength; i++){
                preProcTime += procArray[i].getVirtualRuntime();
            }
            System.out.println(preProcTime);
            return preProcTime;
        } 
        return -1;

    }// end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        
        //getprocessID returns index if found, -1 if not
        int procIndex = getProcessID(procLabel);

        int sucProcTime = 0;
        if (procIndex >=0){
            for(int i=0; i < procIndex; i++){
                sucProcTime += procArray[i].getVirtualRuntime();
            }
            return sucProcTime;
        }
        return -1; 
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        for (int i = procLength -1; i >= 0; i--){
            System.out.println(procArray[i].getLabel() + "-" + procArray[i].getVirtualRuntime());
        }
        
    } // end of printAllProcesses()


    public int getProcessID(String procLabel){
        int procIndex = -1;
        for(int i=0; i < procLength; i++){
            if(procArray[i].getLabel().equals(procLabel)){
                procIndex = i;
            }
        }
        return procIndex;
    }
    
} // end of class OrderedArrayRQ
