package src;

import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {

    private Proc rootProc = null;
    /**
     * Constructs empty queue
     */
    public BinarySearchTreeRQ() {
        // Implement Me

    }  // end of BinarySearchTreeRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
        // Implement me
        if (rootProc == null){
            rootProc = new Proc(procLabel, vt);

            Proc testLeftProc = new Proc("left test", 100);
            Proc testRightProc = new Proc("right test", 500);

            testLeftProc.setleftProc(new Proc("l2", 50));
            testLeftProc.setRightProc(new Proc("r2", 150));
            rootProc.setleftProc(testLeftProc);
            rootProc.setRightProc(testRightProc);
            System.out.println("created root process");
        }
    } // end of enqueue()


    @Override
    public String dequeue() {
        // Implement me

        return ""; // placeholder, modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
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
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        // Implement me
        System.out.println(rootProc.printProc());
    } // end of printAllProcess()

} // end of class BinarySearchTreeRQ
