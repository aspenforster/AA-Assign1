package src;

public class Proc {
    private String procLabel;
    private int vt;
    private Proc leftProc;
    private Proc rightProc;


    public Proc(String procLabel, int vt){
        this.procLabel = procLabel;
        this.vt = vt;
    }


    public String getLabel(){
        return procLabel;
    }

    public int getVT(){
        return vt;
    }

    public Proc getLeftProc(){
        return leftProc;
    }

    public Proc getRightProc(){
        return rightProc;
    }

    public void setLeftProc(Proc proc){
        this.leftProc = proc;
    }

    public void setRightProc(Proc proc){
        this.rightProc = proc;
    }

    public void setProc(Proc proc){
        if(proc.getVT() >= vt){
            if (this.rightProc != null){
                rightProc.setProc(proc);
            } else {
                setRightProc(proc);
            }
        } else {
            if (this.leftProc != null){
                leftProc.setProc(proc);
            } else {
                setLeftProc(proc);
            }
        }
    }

    public boolean checkChildren(String procLabel){

        //if this node has the right label, return true
        if (this.getLabel().equals(procLabel)){
            return true;
        }

        Boolean isLabelFound = false;

        if (leftProc != null){
            isLabelFound = leftProc.checkChildren(procLabel);
        }

        //if it has a right child AND 'labelFound' is still false
        if (rightProc!= null && !isLabelFound){
            isLabelFound = rightProc.checkChildren(procLabel);
        }
        return isLabelFound;
    }

    public String printProc(){
        String s = "";
        
        if (leftProc != null){
            s += leftProc.printProc();
        } else {
            s += ".l";
        }

        s += " " + procLabel + "-" + vt + " ";

        if (rightProc != null){
            s += rightProc.printProc();
        } else {
            s += ".r";
        }
        return s;
    }

    public String printP(){
        return procLabel;
    }
} 