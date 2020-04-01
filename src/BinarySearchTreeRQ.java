package src;

import java.io.PrintWriter;
import java.lang.String;

import src.Proc;
/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {
    
    Object nodeData;
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
        } else {
            rootProc.setProc(new Proc(procLabel, vt));
        }
    } // end of enqueue()


    @Override
    public String dequeue() {
        // start at root node
        Proc currProc = rootProc;
        // prevProc will keep track of the parent node
        Proc prevProc = null;
        
        //check if root node exists
        if (currProc != null){
            //while the node we're currently at still has a left child (smaller VT)
            //go to that child node instead
            while (currProc.getLeftProc()!= null){
                prevProc = currProc;
                currProc = currProc.getLeftProc();
            }

            //once we're at left-most node, check if it has a right child node
            //if no right node, no child nodes so can safely null the current node
            if (currProc.getRightProc()== null){
                if (prevProc != null){
                    prevProc.setLeftProc(null);
                } else {
                    rootProc = null;
                } 
            //if there is a right child - replace current note with it's own right node (by setting prevProc's left node)
            }else {
                prevProc.setLeftProc(currProc.getRightProc());
            }
            //return the label of deleted node
            return currProc.getLabel();
        } else {
            return "no procs to dequeue";
        }
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        return rootProc.checkChildren(procLabel);
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

        Proc currProc = rootProc;
        if (currProc != null){
            
            //System.out.println(rootProc.printProc());
        }else {
            System.out.println("no procs");
        }
    } // end of printAllProcess()
    
    
    // private class Proc
    // {
    //     private String procLabel;
    //     private int virtualRuntime;
    //     private Proc previousNode;
    //     private Proc currentNode;
    //     private Proc leftChild;
    //     private Proc rightChild;

    //     public Proc(String procLabel, int virtualRuntime, Proc currentNode) {
    //         this.procLabel = procLabel;
    //         this.virtualRuntime = virtualRuntime;
    //         leftChild = null;
    //         rightChild = null;;
    //     }

    //     public String getProcLabel() {
    //         return procLabel;
    //     }
        
    //     public int getVirtualRuntime() {
    //         return virtualRuntime;
    //     }

    //     public Proc getNext() {
    //         return previousNode;
    //     }

    //     public void setNext(Proc nextProc) {
    //         this.previousNode = nextProc;
    //     }
    // } // end of inner class Node
} // end of class BinarySearchTreeRQ
