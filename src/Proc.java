public class Proc{
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

    public void setleftProc(Proc proc){
        if (leftProc == null){
            leftProc = proc;
        } else {

        }
    }

    public void setRightProc(Proc proc){
        rightProc = proc;
    }

    public void setProc(Proc proc){
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