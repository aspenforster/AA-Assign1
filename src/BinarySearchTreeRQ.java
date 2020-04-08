package src;

import java.io.PrintWriter;
import java.lang.String;

//import src.Proc;
/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {
    
    private Proc rootProc;
        
    public BinarySearchTreeRQ() {
        rootProc = null;
    }  // end of BinarySearchTreeRQ()

    /**
     * enqueue method - just checks if root is null
     * otherwise will recurse down tree from root to find appropriate place
     * for node we want to queue
     */

    @Override
    public void enqueue(String procLabel, int vt) {

        //need to check here for uniqueness of procLabel, if it exists
        //just return as per discussion forum reply
        if (findProcess(procLabel)){
            return;
        }

        // if there's no root, make this process the root
        if (rootProc == null){
            rootProc = new Proc(procLabel, vt);

        // if a root exists, try and add new node starting from root
        } else {
            addProc(rootProc, new Proc(procLabel, vt));
        }
    } // end of enqueue()

    /**
     * Recursive method to add a new procedure to tree
     * 
     * @param currProc the existing procedure node in the tree
     * @param newProc the procedure we're trying to add to the tree
     */

    public void addProc(Proc currProc, Proc newProc){
        //if the new node's runtime is higher than (or equal to) the current node
        //we should be sending it down the right side of the tree
        if(newProc.getVirtualRuntime() >= currProc.getVirtualRuntime()){

            //if current node has no right node, can just place here
            if (currProc.getRight() == null){
                currProc.setRight(newProc);

            //otherwise, need to recurse down another step
            } else {
                addProc(currProc.getRight(), newProc);
            }
        // end up here if new node is less than current node runtime
        } else {
            //if current node has no left node, can just place here
            if (currProc.getLeft() == null){
                currProc.setLeft(newProc);

            //otherwise, need to recurse down another step
            } else {
                addProc(currProc.getLeft(), newProc);
            }
        }
    }

    /**
     * dequeue runs the returnSmallest method from the root 
     * to find the left-most node of the whole tree (which should be the node to dequeue)
     * and then runs the deleteProc() method on that node
     */

    @Override
    public String dequeue(){
        //return string to give back if no proc was dequeued
        String returnString = "no procedures to dequeue";
        //only run if rootProc exists
        if (rootProc != null){
            Proc del = returnSmallest(rootProc);
            returnString = del.getProcLabel();
            deleteProc(del);
        }
        return returnString;
    }

    /**
     * returns true if the process is in the queue somewhere
     * doesn't need to access any of the data/position of the node
     */
    @Override
    public boolean findProcess(String procLabel) {

        if (rootProc != null){
            //runs recursive "find" method starting at rootnode
            if (find(rootProc, procLabel) !=null){
                return true;
            } 
        }
        return false;
    } // end of findProcess()

    /**
     * Recursive method that will take a procedure node and a string
     * and recurse through the tree until it finds the corresponding proc
     * (otherwise just returns null)
     * 
     * @param proc - the proc object we're currently checking
     * @param procLabel - the label that the user/file was trying to find
     * @return - returns node if found, otherwise null
     */
    public Proc find(Proc proc, String procLabel){


        Proc foundProc = null;

        if (proc.getProcLabel().equals(procLabel)){
            return proc;
        }

        //check left node first (arbitrary, could check right first instead)
        if (proc.getLeft() !=null && foundProc == null){
            foundProc = find(proc.getLeft(), procLabel);
        }

        //check right node
        if (proc.getRight() !=null && foundProc == null){
            foundProc = find(proc.getRight(), procLabel);
        }
        
        return foundProc;
    }

    /**
     * Helper function to return smallest Proc of tree from given Proc object
     * @param proc - the proc object to start from
     * @return will return the smallest proc from tree (even if that the input proc)
     */
    public Proc returnSmallest(Proc proc){
        return proc.getLeft() != null ? returnSmallest(proc.getLeft()) : proc;
    }


    /**
     * helper function that sets child to left or right depending on direction given
     * will override existing children nodes
     * 
     * @param parent - parent node to have its children updated
     * @param child - child to be placed under parent
     * @param direction - put child node on left or right - left = true, right =
     * 
     */
    public void setChild(Proc parent, Proc child, Boolean direction){
        if (direction){
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }
    }

    /**
     * deleteProc does what it says on the tin - deletes a proc from the tree
     * and deals with the cleanup of nodes
     * this method keeps Proc objects in tact and just reorganises
     * the connections between them 
     * @param proc - the process you want to remove
     */
    public void deleteProc(Proc proc){

        Proc parent = proc.getParentProc();

        System.out.println(proc.getLeft() + " " + proc.getRight() + " " + parent);

        //direction just used to represent left/right - left is true
        Boolean direction = true;

        //if we're not at root, get child position from looking at parent runtime
        if(parent!=null){
            direction= parent.getVirtualRuntime() > proc.getVirtualRuntime();
        } 

        //if we have no children, can remove ref to this node from parent
        if (proc.getLeft() == null && proc.getRight() == null){

            if(parent!=null){
                setChild(parent, null, direction);
            } else {
                rootProc = null;
            }

        // if we only have a right child
        } else if (proc.getLeft() == null){
            if(parent!=null){
                System.out.println("we're here" + proc.getProcLabel());
                setChild(parent, proc.getRight(), direction);
            //at root proc
            } else {
                rootProc = proc.getRight();
                rootProc.setParentProc(null);
            }

        //if we only have a left child
        } else if (proc.getRight() == null){
            //if we're not at root proc
            if (parent!=null){
                
                setChild(parent, proc.getLeft(), direction);
            //at root proc
            } else {
                rootProc = proc.getLeft();
                rootProc.setParentProc(null);
            }

        //if we have two children :(
        } else {
            //smallest of right subtree of node
            Proc sml = returnSmallest(proc.getRight());

            //smallest (of subtree)'s parent
            Proc smlParent = sml.getParentProc();

            //smallest (of subtree)'s right nodes - if any
            Proc sRight = sml.getRight();

            //storing references to node-to-be-deleted's children
            Proc left = proc.getLeft();
            Proc right = proc.getRight();
            
            //set deleted node's parent to point at sml instead
            if(parent!=null){
                setChild(parent, sml, direction);
            //if we've got no parent, update rootProc
            } else {
                rootProc = sml;
                rootProc.setParentProc(null);
            }
            
            //give sml node the left tree from deleted node
            sml.setLeft(left);

            //if sml's original parent was the node to be deleted
            //we know sml had no left tree of it's own
            //so we're already done
            if(smlParent == proc){
                
            //otherwise, continue with steps to fully swap nodes
            } else {

                //set sml to have right tree of deleted node
                //(not necessary if sml was the direct right node child of deleted node)
                sml.setRight(right);

                //set sml's original parent to have deleted node as child
                smlParent.setLeft(proc);

                //give deleted node the original children of sml
                //left would always be null - as sml was the smallest value
                proc.setLeft(null);
                proc.setRight(sRight);

                //last step: run deleteProc on the node to be deleted
                //(which is now in sml's original location)

                deleteProc(proc);

            }
        }
    }

    /**
     * uses recursive 'find' method to search through tree
     * if the node is found, runs the deleteProc method on the node
     * otherwise returns false
     */
    @Override
    public boolean removeProcess(String procLabel) {
        
        //if a rootProc exists, search tree for proclabel
        if(rootProc!=null){
            Proc del = find(rootProc, procLabel);
           
            //if the node was found, delete it and return true
            if(del != null){
                deleteProc(del);
                return true;
            }
        }
        return false; 
    } // end of removeProcess()



    public int findPreProcs(Proc proc, Proc targetProc){

        System.out.println("visiting node " + proc.getVirtualRuntime());
        int totalPreRuntime = 0;

        //if target proc is the proc we're on (not just runtime, label as well)
        if (targetProc == proc){
            return proc.getLeft() != null ? addProcRT(proc.getLeft()) : 0;

        //if target proc has greater or equal runtime to the proc we're on
        //we should recurse right and add values we've found so far
        } else if (targetProc.getVirtualRuntime() >= proc.getVirtualRuntime()){

            //add this node's value to total
            totalPreRuntime += proc.getVirtualRuntime();
            
            //and add this node's whole left subtree values
            totalPreRuntime += proc.getLeft()!=null ? addProcRT(proc.getLeft()) : 0;
            //if this node has right subtree, keep looking there
            if(proc.getRight()!=null){
                totalPreRuntime += findPreProcs(proc.getRight(), targetProc);
            } 
        // end up here if target proc's runtime is less than current node
        // which means we should recurse left
        } else{
            totalPreRuntime += proc.getLeft()!= null ? findPreProcs(proc.getLeft(), targetProc) : 0;
        }
        System.out.println(totalPreRuntime);
        return totalPreRuntime;
    }

    public int findSucProcs(Proc proc, Proc targetProc){

        System.out.println("visiting node " + proc.getVirtualRuntime());
        int totalPreRuntime = 0;

        //if target proc is the proc we're on (not just runtime, label as well)
        if (targetProc == proc){
            return proc.getRight() != null ? addProcRT(proc.getRight()) : 0;

        //if target proc has less than or equal runtime to the proc we're on
        //we should recurse left and add values we've found so far
        } else if (targetProc.getVirtualRuntime() < proc.getVirtualRuntime()){

            //add this node's value to total
            totalPreRuntime += proc.getVirtualRuntime();
            
            //and add this node's whole right subtree values
            totalPreRuntime += proc.getRight()!=null ? addProcRT(proc.getRight()) : 0;
            //if this node has right subtree, keep looking there
            if(proc.getLeft()!=null){
                totalPreRuntime += findSucProcs(proc.getLeft(), targetProc);
            } 
        // end up here if target proc's runtime is greater than current node
        // which means we should recurse right
        } else{
            totalPreRuntime += proc.getRight()!= null ? findSucProcs(proc.getRight(), targetProc) : 0;
        }
        return totalPreRuntime;
    }

    /**
     * Recursive helper method to add up all runtimes of a given proc and its children
     * @param proc - proc to start with - is included in total
     * @return - returns value of a node and its children
     */
    public int addProcRT(Proc proc){
        
        int value = proc.getVirtualRuntime();

        value += proc.getLeft()!= null ? addProcRT(proc.getLeft()) : 0;

        value += proc.getRight()!= null ? addProcRT(proc.getRight()) : 0;

        return value;
    }   

    @Override
    public int precedingProcessTime(String procLabel){
        
        //search tree for node with procLabel
        Proc p = find(rootProc, procLabel);

        //if it exists, calculate process times before it
        if(p != null){
            //find preceeding proc times starting from root
            return findPreProcs(rootProc, p);
        }

        //if node doesn't exist
        return -1;
    }


    @Override
    public int succeedingProcessTime(String procLabel) {
        Proc p = find(rootProc, procLabel);

        if(p != null){
            //find succeeding proc times starting from root
            return findSucProcs(rootProc, p);
        }

        return -1;
    } // end of precedingProcessTime()


    /**
     * Recursive helper method to print procs and their children
     * @param proc proc to start search from - will recurse down both sides of tree
     * @return returns string of this node and it's children
     */
    public String printProc(Proc proc){
        String s = "";

        //if there's a left node, run printProc() on that first
        if (proc.getLeft() != null){
            s += printProc(proc.getLeft());
        } 
        //add the node itself to the print 
        s += proc.getProcLabel() + " ";

        //if there's a right node, run printProc() on that after printing this proc
        if (proc.getRight() != null){
            s += printProc(proc.getRight());
        } 
        return s; 
    }

    @Override
    public void printAllProcesses(PrintWriter os) {

        os.flush();

        //if a root proc exists, recursively run printProc() and add output to "output" string
        if (rootProc != null){
            String output = printProc(rootProc);
            os.print(output);
        } else {
            os.print("no procedures");
        }

        os.println();
        //os.close();
    } // end of printAllProcess()   
    
    private class Proc
    {
        private String procLabel;
        private int virtualRuntime;
        private Proc leftChild;
        private Proc rightChild;
        private Proc parentProc;

        public Proc(String procLabel, int virtualRuntime) {
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
            this.leftChild = null;
            this.rightChild = null;
            this.parentProc = null;
        }

        public String getProcLabel() {
            return procLabel;
        }

        public int getVirtualRuntime() {
            return virtualRuntime;
        }


        public Proc getLeft() {
            return leftChild;
        }
        
        public void setLeft(Proc leftChild) {
            if(leftChild!=null) leftChild.setParentProc(this);
            this.leftChild = leftChild;
        }
        
        public Proc getRight() {
            return rightChild;
        }     
        
        public void setRight(Proc rightChild) {
            if(rightChild!=null) rightChild.setParentProc(this);
            this.rightChild = rightChild;
        }
        
        public Proc getParentProc(){
            return parentProc;
        } 

        public void setParentProc(Proc parent){
            parentProc = parent;
        }
        

        
    } 
} // end of class BinarySearchTreeRQ
