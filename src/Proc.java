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

    public Proc recurseLeft(){
        System.out.println("recursing left -> " + this.procLabel + "- " + this.vt);
        if (leftProc.getLeftProc() == null){
            return leftProc;
        } else {
            System.out.println("break here? " + this.procLabel + "- " + this.vt);
            leftProc.recurseLeft();
        }
        return null;
    }

    public void setProc(Proc proc){
        if(proc.getVT() > vt){
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
} 