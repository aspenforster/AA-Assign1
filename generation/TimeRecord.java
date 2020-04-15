package generation;

public class TimeRecord{

    private final int seed;
    private final int iteration;
    private int count;
    private double totalTime;

    public int getSeed(){
        return seed;
    }

    public int getIteration(){
        return iteration;
    }

    public TimeRecord(int seed, int iteration, int count, double totalTime){
        this.seed = seed;
        this.iteration = iteration;
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
        return "(" + seed+ "," + iteration+ "," + count + "," + totalTime + "," + getAverage() + ")";
    }

}