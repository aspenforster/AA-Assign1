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
public class OrderedLinkedListRQ implements Runqueue {

    //First node in the list
    private Node FirstNode;
        
    public OrderedLinkedListRQ() {
        FirstNode = null;
    }  // end of OrderedLinkedList()

    @Override
    public void enqueue(String procLabel, int vt) {
        
        Node newNode = new Node(procLabel, vt);
        
        if(FirstNode == null) {
            FirstNode = newNode;
        }
        else {
            // need to search through and compare vt and then insert
            // Processes that have the same vruntime follow the FIFO order.
            // Therefore vt needs to be less than node validating against (loop) thought all nodes and stop at null
            
            Node currentNode = FirstNode;
            Node previousNode = null;
          
            //Check the first node
            
            if (vt < currentNode.getVirtualRuntime()) {
 
                newNode.setNext(FirstNode);
                FirstNode = newNode;
            } else {
                previousNode = currentNode;
                currentNode = currentNode.getNext();
                boolean insertComplete = false;
                while (currentNode != null && insertComplete == false) {

                    if (vt < currentNode.getVirtualRuntime()){
 
                        newNode.setNext(currentNode);// insert new node, set new nodes next to current node
                        previousNode.setNext(newNode); // set previous
                        insertComplete = true;
                    }   

                    previousNode = currentNode;
                    currentNode = currentNode.getNext();

                };
                
                //If an insert hasnt taken place, add the new node to the end
                if (insertComplete == false) {
                    previousNode.setNext(newNode); 
                }
            }       
        }

    } // end of enqueue()


    @Override
    public String dequeue() {
        if(FirstNode == null) {
            return "";
        }
        else {
            String procLabel = FirstNode.getProcLabel();
            FirstNode = FirstNode.nextNode; //Original first Node will automatically delete with garbage collector
            return procLabel; 
        }
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        Node currentNode = FirstNode;
        
        while (currentNode != null) {
            if (procLabel.equals(currentNode.getProcLabel())) {
                return true;
            }
            currentNode = currentNode.getNext();
        }

        return false; 
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        Node currentNode = FirstNode;
        Node previousNode = null;
        
        while (currentNode != null) {
            
            if (procLabel.equals(currentNode.getProcLabel())) {
                if (previousNode == null) {
                    FirstNode = null;
                    return true;
                }
                else {
                    previousNode.setNext(currentNode.getNext());
                    return true;
                }
            }
            previousNode = currentNode;
            currentNode = currentNode.getNext();
        }

        return false;
    } // End of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        Node currentNode = FirstNode;
        int totalTime = 0;
        
        while (currentNode != null) {
            totalTime += currentNode.getVirtualRuntime();
            
            if (procLabel.equals(currentNode.getProcLabel())) {
                return totalTime;
            }
            currentNode = currentNode.getNext();
        }

        return -1; 
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        Node currentNode = FirstNode;
        int totalTime = 0;
        boolean nodeFound = false;
        
        while (currentNode != null) {
            
            if (procLabel.equals(currentNode.getProcLabel())) {
                nodeFound = true;
            }
            
            if (nodeFound == true){
                totalTime += currentNode.getVirtualRuntime();
            }
            currentNode = currentNode.getNext();
        }
        
        if (nodeFound == true){
            return totalTime;
        } else {
            return -1;
        }

    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        Node currentNode = FirstNode;
        String output = "";
        
        while (currentNode != null) {
            
            output = output + currentNode.getProcLabel() + " ";
            
            currentNode = currentNode.getNext();
        }
        
        System.out.println(output);

    } // end of printAllProcess()
    
    private class Node
    {
        private String procLabel;
        private int virtualRuntime;
        private Node nextNode;

        public Node(String procLabel, int virtualRuntime) {
            this.procLabel = procLabel;
            this.virtualRuntime = virtualRuntime;
            nextNode = null;
        }

        public String getProcLabel() {
            return procLabel;
        }
        
        public int getVirtualRuntime() {
            return virtualRuntime;
        }

        public Node getNext() {
            return nextNode;
        }

        public void setNext(Node nextNode) {
            this.nextNode = nextNode;
        }
    } // end of inner class Node
} // end of class OrderedLinkedListRQ
