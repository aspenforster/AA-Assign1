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
     * dequeue function - could try and rewrite to be recursive?
     */
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
            while (currProc.getLeft()!= null){
                prevProc = currProc;
                currProc = currProc.getLeft();
            }

            //once we're at left-most node, check if it has a right child node
            //if no right node, no child nodes so can safely null the current node
            if (currProc.getRight()== null){
                if (prevProc != null){
                    prevProc.setLeft(null);
                } else {
                    rootProc = null;
                }
            //if there is a right child - replace current note with it's own right node (by setting prevProc's left node)
            }else {
                prevProc.setLeft(currProc.getRight());
            }
            //return the label of deleted node
            return currProc.getProcLabel();
        } else {
            return "no procs to dequeue";
        }
    } // end of dequeue()

    /**
     * find method that will take a procedure node and a string
     * and recurse through the tree until it finds the string
     * (otherwise just returns false for not found anywhere)
     * 
     * @param proc - the procedure we're currently checking
     * @param procLabel - the label that the user/file was trying to find
     * @return - returns false if not found anywhere, true if found somewhere
     */

    public boolean find(Proc proc, String procLabel){

        //bool to check if we've already found it
        boolean isLabelFound = false;

        //check left node first (arbitrary, could check right first instead)
        if (proc.getLeft() !=null && !isLabelFound){
            isLabelFound = find(proc.getLeft(), procLabel);
        }

        //check right node
        if (proc.getRight() !=null  && !isLabelFound){
            isLabelFound = find(proc.getRight(), procLabel);
        }
        
        return isLabelFound;
    }

    /**
     * returns true if the process is in the queue somewhere
     * doesn't need to access any of the data/position of the node
     */
    @Override
    public boolean findProcess(String procLabel) {

        //runs recursive "find" method starting at rootnode
        if (rootProc != null){
            return find(rootProc, procLabel);
        } else {
            return false;
        }
    
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        Proc currentProc = rootProc; // currentProc = Parent
        Proc lastNode = null;
        Boolean endOfTree = false;
        boolean leftFlag = false;
        int totalTime = 0;

        if (currentProc == null) {
            return -1;
        } else {
            
            do {
                    //got to the bottom left hand branch
                if (currentProc.getLeft() != null && leftFlag == false) {
                    currentProc = currentProc.getLeft();

                } else {

                    if (currentProc.getProcLabel().equals(procLabel)){
                        return totalTime;
                    }
                    
                    totalTime = totalTime + currentProc.getVirtualRuntime();

                    leftFlag = true;

                    if (currentProc.getRight() != null) {
                        currentProc = currentProc.getRight();
                        leftFlag = false;
                    } else {
                        if (currentProc == rootProc && currentProc.getRight() == null){
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == rootProc){
                                        endOfTree = true;
                                    } else {
                                        lastNode = currentProc;                        
                                        currentProc = currentProc.getPreviousProc();
                                    }
                                } 
                            } 
                        } 
                    }   
                }  
            } while (endOfTree == false);
        }

        return -1; 
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        Proc currentProc = rootProc; // currentProc = Parent
        Proc lastNode = null;
        Boolean endOfTree = false;
        boolean leftFlag = false;
        int totalTime = 0;
        boolean procFound = false;

        if (currentProc == null) {
            return -1;
        } else {
            
            do {
                    //got to the bottom left hand branch
                if (currentProc.getLeft() != null && leftFlag == false) {
                    currentProc = currentProc.getLeft();

                } else {
                    
                    if (procFound == true) {
                        totalTime = totalTime + currentProc.getVirtualRuntime();
                    }

                    if (currentProc.getProcLabel().equals(procLabel)){
                        procFound = true;
                    }

                    leftFlag = true;

                    if (currentProc.getRight() != null) {
                        currentProc = currentProc.getRight();
                        leftFlag = false;
                    } else {
                        if (currentProc == rootProc && currentProc.getRight() == null){
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == rootProc){
                                        endOfTree = true;
                                    } else {
                                        lastNode = currentProc;                        
                                        currentProc = currentProc.getPreviousProc();
                                    }
                                } 
                            } 
                        } 
                    }   
                }  
            } while (endOfTree == false);
        }
        if (procFound == true) {
            return totalTime;
        } else {
            return -1; 
        }
    } // end of precedingProcessTime()


    //recursive function for printing a procedure
    public String printProc(Proc proc){
        String s = "";
        
        //if there's a left node, run printProc() on that first
        if (proc.getLeft() != null){
            s += printProc(proc.getLeft());
        } 
        //TO-DO: Comment out runtime print for submission!!
        s += proc.getProcLabel() + " " + "-" + proc.getVirtualRuntime() + " ";


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
        os.close();
    } // end of printAllProcess()   
    
    private class Proc
    {
        private String procLabel;
        private int virtualRuntime;
        private Proc previousProc;
        private Proc currentProc;
        private Proc leftChild;
        private Proc rightChild;

        public Proc(String procLabel, int virtualRuntime) {
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
            leftChild = null;
            rightChild = null;;
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
            this.leftChild = leftChild;
        }
        
        public Proc getRight() {
            return rightChild;
        }     
        
        public void setRight(Proc rightChild) {
            this.rightChild = rightChild;
        }
        
        public Proc getPreviousProc() {
            return previousProc;
        }     
        
        public void setPreviousProc(Proc previousProc) {
            this.previousProc = previousProc;
        }
        
    } 
} // end of class BinarySearchTreeRQ
