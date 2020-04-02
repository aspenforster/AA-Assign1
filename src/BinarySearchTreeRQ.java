//package src;

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
    
   
     //First node in the list
    private Proc FirstProc;
        
    public BinarySearchTreeRQ() {
        FirstProc = null;
    }  // end of BinarySearchTreeRQ()

    @Override
    public void enqueue(String procLabel, int vt) {

        Proc newNode = new Proc(procLabel, vt);
        boolean complete = false;
        
        //Check the first node
        if(FirstProc == null) {
            FirstProc = newNode;
        }
        else {
            
            Proc currentProc = FirstProc;
            //Proc previousProc = null; might need to assign need to confirm if tree has to be balanced
            
            while (currentProc != null && complete == false) {
                if (vt < currentProc.getVirtualRuntime()) {
                    if(currentProc.getLeft() == null){
                        currentProc.setLeft(newNode);
                        newNode.setPreviousProc(currentProc);
                        complete = true;
                    } 
                    currentProc = currentProc.getLeft();
                    
                } else {
                    if(currentProc.getRight() == null){
                        currentProc.setRight(newNode);
                        newNode.setPreviousProc(currentProc);
                        complete = true;
                    } 
                    currentProc = currentProc.getRight();
                }
            }    

        }
 
    } // end of enqueue()


    @Override
    public String dequeue() {
        //find the first node

        return "";
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {

        Proc currentProc = FirstProc; // currentProc = Parent
        Proc lastNode = null;
        Boolean endOfTree = false;
        boolean leftFlag = false;

        if (currentProc == null) {
            return false;
        } else {
            
            do {
                    //got to the bottom left hand branch
                if (currentProc.getLeft() != null && leftFlag == false) {
                    currentProc = currentProc.getLeft();

                } else {
                    
                    if (currentProc.getProcLabel().equals(procLabel)){
                        return true;
                    }

                    leftFlag = true;

                    if (currentProc.getRight() != null) {
                        currentProc = currentProc.getRight();
                        leftFlag = false;
                    } else {
                        if (currentProc == FirstProc && currentProc.getRight() == null){
                            
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == FirstProc){
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

        return false; 

    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        Proc currentProc = FirstProc; // currentProc = Parent
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
                        if (currentProc == FirstProc && currentProc.getRight() == null){
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == FirstProc){
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
        Proc currentProc = FirstProc; // currentProc = Parent
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
                        if (currentProc == FirstProc && currentProc.getRight() == null){
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == FirstProc){
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


    @Override
    public void printAllProcesses(PrintWriter os) {

        Proc currentProc = FirstProc; // currentProc = Parent
        Proc lastNode = null;
        Boolean endOfTree = false;
        boolean leftFlag = false;
        String output = "";

        if (currentProc == null) {
            os.print(output);
        } else { 
            
            do {
                    //got to the bottom left hand branch
                if (currentProc.getLeft() != null && leftFlag == false) {
                    currentProc = currentProc.getLeft();

                } else {
                    
                    output = currentProc.getProcLabel() + " ";
                    leftFlag = true;

                    if (currentProc.getRight() != null) {
                        currentProc = currentProc.getRight();
                        leftFlag = false;
                    } else {
                        if (currentProc == FirstProc && currentProc.getRight() == null){
                            
                            endOfTree = true;
                        } else {
                            lastNode = currentProc;                        
                            currentProc = currentProc.getPreviousProc();


                            if (currentProc.getLeft() == lastNode) {
                                leftFlag = true;
                            } else {
                                while (currentProc.getRight() == lastNode && endOfTree != true) {
                                    if (currentProc == FirstProc){
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
            
            os.print(output);
        }
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
