//package src;

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
    /**
     * Constructs empty queue
     */
    public OrderedArrayRQ() {
        // Implement Me
        procArray = new Proc[1];
        System.out.println(procArray);
    }  // end of OrderedArrayRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
        // Implement me

    } // end of enqueue()


    @Override
    public String dequeue() {
        // Implement me

        return ""; // placeholder,modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
        // Implement me

        return false; // placeholder, modify this
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    }// end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me

    } // end of printAllProcesses()
    
    
    private Object resize (Object oldObj, int newsize) {
        int oldSize = java.lang.reflect.Array.getLength(oldObj);
        Class elementType = oldObj.getClass().getComponentType();
        Object newObj = java.lang.reflect.Array.newInstance(elementType, newsize);

        int preserveLength = Math.min(oldSize,  newsize);

        if (preserveLength > 0) {
            System.arraycopy(oldObj, 0, newObj, 0, preserveLength);
        }

        return newObj;
    }
    
} // end of class OrderedArrayRQ
