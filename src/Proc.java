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
} 