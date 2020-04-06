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
  */

    @Override
    public String dequeue(){
        //return string to give back if no proc was dequeued
        String returnString = "no procedures to dequeue";
        //only run if rootProc exists
        if (rootProc != null){

            //get left-most-node's parent
            Proc parent = returnSmallestParent(rootProc);

            // if parent returned is root node we need to be careful
            if (parent == rootProc){
                //get root's label
                returnString = rootProc.getProcLabel();

                //if root has no children, safe to null it
                if (rootProc.getRight() == null && rootProc.getLeft() == null){
                    rootProc = null;
                //if root has right child only
                } else if (rootProc.getLeft() == null){
                    rootProc = rootProc.getRight();
                //if root only has left child delete it
                } else {
                    returnString = rootProc.getLeft().getProcLabel();
                    rootProc.setLeft(null);
                }
            // if parent node returned is not root, we know there is a left-er node to delete instead        
            } else {
                returnString = parent.getLeft().getProcLabel();
                deleteChildProc(parent, true);
            }
        }
        return returnString;

    }

    /**
     * returns true if the process is in the queue somewhere
     * doesn't need to access any of the data/position of the node
     */
    @Override
    public boolean findProcess(String procLabel) {
        //runs recursive "find" method starting at rootnode
        return rootProc != null ? find(rootProc, procLabel) : false;
    } // end of findProcess()

    /**
     * Recursive method that will take a procedure node and a string
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
     * Recursive method that will take a procedure node and a string
     * and recurse through the tree until it finds the corresponding proc
     * (otherwise just returns null)
     * 
     * @param proc - the procedure we're currently checking
     * @param procLabel - the label that the user/file was trying to find
     * @return - returns node if found, otherwise null
     */
    public Proc findProc(Proc proc, String procLabel){

        Proc foundProc = null;

        if (proc.getProcLabel() == procLabel){
            foundProc = proc;
        }

        //check left node first (arbitrary, could check right first instead)
        if (proc.getLeft() !=null && foundProc==null){
            foundProc = findProc(proc.getLeft(), procLabel);
        }

        //check right node
        if (proc.getRight() !=null  && foundProc==null){
            foundProc = findProc(proc.getRight(), procLabel);
        }
        
        return foundProc;
    }

    /**
     * Helper function to return smallest Proc of tree from given Proc object
     * @param proc - the proc object to start from
     * @return will return the smallest proc (even if that the input proc)
     */
    public Proc returnSmallest(Proc proc){
        return proc.getLeft() != null ? returnSmallest(proc.getLeft()) : proc;
    }

    /**
     * Helper function that returns the -parent- of the smallest left leaf node
     * @param proc
     * @return
     */
    public Proc returnSmallestParent(Proc proc){
        Proc left = proc.getLeft();
        if (left != null){
            if (left.getLeft() == null){
                return proc;
            } else {
                return returnSmallestParent(left);
            }
        }
        return proc;
    }


    public void deleteChildProc(Proc parent, boolean isLeft){

        //if "isLeft" is true, get the left child of parent - otherwise right
        Proc child = isLeft ? parent.getLeft() : parent.getRight();

        System.out.println(child.getVirtualRuntime());

        // no children, safe to delete
        if (child.getLeft() == null && child.getRight() == null){

            if(isLeft){
                parent.setLeft(null); 
            }
            else {
                parent.setRight(null);
            }
            
        // only right child
        } else if (child.getLeft() == null){

            if(isLeft){
                parent.setLeft(child.getRight()); 
            }
            else {
                parent.setRight(child.getRight());
            }

        // only left child
        } else if (child.getRight() == null){

            if(isLeft){
                parent.setLeft(child.getLeft()); 
            }
            else {
                parent.setRight(child.getLeft());
            }
        // two children - uh oh
        } else {
            System.out.println("two children uh oh");

            //store ref to smallest node from child subtree
            Proc sml = returnSmallest(child.getRight());
            System.out.println(sml.getVirtualRuntime());
            //and it's parent
            Proc smlParent = returnSmallestParent(child.getRight());

            System.out.println(smlParent.getVirtualRuntime());

            //and any right children it may have (can't have left because it's smallest)
            Proc sRight = sml.getRight();

            //store ref to node-to-be-deleted's children nodes
            Proc cRight = child.getRight();
            Proc cLeft = child.getLeft();
            
            //replace reference to deleted node with reference to smallest child
            if(isLeft){
                parent.setLeft(sml);
            } else {
                parent.setRight(sml);
            }
            //smallest node is now in place, set it's left children to what deleted node had
            sml.setLeft(cLeft);
        
            //if smlParent is the same as small, we're already done and don't need to do any of this
            if (smlParent != sml){
                //set smallest node's right child to node-to-be-deleted's right children
                sml.setRight(cRight);
                //put the node-to-be-deleted in the place the smallest node used to occupy
                smlParent.setLeft(child);
                //set node-to-be-deleted's children to those the smallest node used to have
                child.setLeft(null);
                child.setRight(sRight);
                //recursively run this function again on node to be deleted
                deleteChildProc(smlParent, true);
            }
        }
    }


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        rootProc = new Proc("root", 9);

        addProc(rootProc, new Proc("t1", 15));

        addProc(rootProc, new Proc("t2", 10));

        addProc(rootProc, new Proc("t3", 13));

        addProc(rootProc, new Proc("t4", 23));

        addProc(rootProc, new Proc("t5", 19));
        
        addProc(rootProc, new Proc("t6", 5));

        addProc(rootProc, new Proc("t7", 9));

        addProc(rootProc, new Proc("t8", 11));

        addProc(rootProc, new Proc("t9", 40));

        System.out.println(printProc(rootProc));

        deleteChildProc(rootProc.getRight(), false);

        // System.out.println(printProc(rootProc));

        //System.out.println(findProc(rootProc, "t2").getProcLabel());

        //System.out.println(rootProc.getLeft().getProcLabel());

        //System.out.println("sml parent = " + returnSmallestParent(rootProc).getVirtualRuntime());
        //switchDeleteRight(rootProc);

        //System.out.println(returnSmallest(rootProc).getProcLabel());

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
        //os.close();
    } // end of printAllProcess()   
    
    private class Proc
    {
        private String procLabel;
        private int virtualRuntime;
        private Proc previousProc;
        private Proc leftChild;
        private Proc rightChild;

        public Proc(String procLabel, int virtualRuntime) {
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
            this.leftChild = null;
            this.rightChild = null;
        }

        public String getProcLabel() {
            return procLabel;
        }

        public void setProcLabel(String label){
            this.procLabel = label;
        }

        public int getVirtualRuntime() {
            return virtualRuntime;
        }

        public void setVirtualRuntime(int runtime){
            this.virtualRuntime = runtime;
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
        

        
    } 
} // end of class BinarySearchTreeRQ
