package src;

import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the run queue interface using an Ordered Link List.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan.
 */
public class OrderedLinkedListRQUnique implements Runqueue {

    //First node in the list
    private Proc FirstProc;
        
    public OrderedLinkedListRQUnique() {
        FirstProc = null;
    }  // end of OrderedLinkedList()

    @Override
    public void enqueue(String procLabel, int vt) {
        

        Proc newNode = new Proc(procLabel, vt);
        
        if(FirstProc == null) {
            FirstProc = newNode;
        }
        else {
            //UNIQUENESS CHECK
            if(findProcess(procLabel)){
                return;
            }
            // need to search through and compare vt and then insert
            // Processes that have the same vruntime follow the FIFO order.
            // Therefore vt needs to be less than node validating against (loop) thought all nodes and stop at null
            
            Proc currentProc = FirstProc;
            Proc previousProc = null;
          
            //Check the first node
            
            if (vt < currentProc.getVirtualRuntime()) {
 
                newNode.setNext(FirstProc);
                FirstProc = newNode;
            } else {
                previousProc = currentProc;
                currentProc = currentProc.getNext();
                boolean insertComplete = false;
                while (currentProc != null && insertComplete == false) {

                    if (vt < currentProc.getVirtualRuntime()){
 
                        newNode.setNext(currentProc);// insert new node, set new nodes next to current node
                        previousProc.setNext(newNode); // set previous
                        insertComplete = true;
                    }   

                    previousProc = currentProc;
                    currentProc = currentProc.getNext();

                };
                
                //If an insert hasnt taken place, add the new node to the end
                if (insertComplete == false) {
                    previousProc.setNext(newNode); 
                }
            }       
        }

    } // end of enqueue()


    @Override
    public String dequeue() {
        if(FirstProc == null) {
            return "";
        }
        else {
            String procLabel = FirstProc.getProcLabel();
            FirstProc = FirstProc.nextProc; //Original first Node will automatically delete with garbage collector
            return procLabel; 
        }
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        Proc currentProc = FirstProc;
        
        while (currentProc != null) {
            if (procLabel.equals(currentProc.getProcLabel())) {
                return true;
            }
            currentProc = currentProc.getNext();
        }

        return false; 
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        Proc currentProc = FirstProc;
        Proc previousProc = null;
        
        while (currentProc != null) {
            
            if (procLabel.equals(currentProc.getProcLabel())) {
                if (previousProc == null) {
                    FirstProc = null;
                    return true;
                }
                else {
                    previousProc.setNext(currentProc.getNext());
                    return true;
                }
            }
            previousProc = currentProc;
            currentProc = currentProc.getNext();
        }

        return false;
    } // End of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        Proc currentProc = FirstProc;
        int totalTime = 0;
        
        while (currentProc != null) {
            if (procLabel.equals(currentProc.getProcLabel())) {
                return totalTime;
            }

            totalTime += currentProc.getVirtualRuntime();
            
            currentProc = currentProc.getNext();
        }

        return -1; 
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        Proc currentProc = FirstProc;
        int totalTime = 0;
        boolean nodeFound = false;
        
        while (currentProc != null) {
            if (nodeFound == true){
                totalTime += currentProc.getVirtualRuntime();
            }
            
            if (procLabel.equals(currentProc.getProcLabel())) {
                nodeFound = true;
            }
            
            currentProc = currentProc.getNext();
        }
        
        if (nodeFound == true){
            return totalTime;
        } else {
            return -1;
        }

    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {

        os.flush();

        Proc currentProc = FirstProc;
        String output = "";
        
        while (currentProc != null) {
            
            output = output + currentProc.getProcLabel() + " ";
            
            currentProc = currentProc.getNext();
        }
        
        os.println(output);

    } // end of printAllProcess()
    
    private class Proc
    {
        private String procLabel;
        private int virtualRuntime;
        private Proc nextProc;

        public Proc(String procLabel, int virtualRuntime) {
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
            nextProc = null;
        }

        public String getProcLabel() {
            return procLabel;
        }
        
        public int getVirtualRuntime() {
            return virtualRuntime;
        }

        public Proc getNext() {
            return nextProc;
        }

        public void setNext(Proc nextProc) {
            this.nextProc = nextProc;
        }
    } // end of inner class Proc
} // end of class OrderedLinkedListRQ
