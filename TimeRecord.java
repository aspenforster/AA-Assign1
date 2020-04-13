public class TimeRecord{

    private String type;
    private String operation;
    private int count;
    private double totalTime;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public TimeRecord(String operation, String type, int count, double totalTime){
        this.operation = operation;
        this.type = type;
        this.count = count;
        this.totalTime = totalTime;
    }

    public void addTime(double time){
        this.count++;
        this.totalTime += time;
    }

    public double getAverage(){
        return this.totalTime / this.count;
    }

    public String toString(){
        return "(" + operation+ "," + type+ "," + count + "," + totalTime + "," + getAverage() + ")";
    }

}