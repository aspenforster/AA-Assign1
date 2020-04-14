public class RunTimeTrials {

    public static final int[] listSizes = {10, 50, 100, 500, 1000, 5000, 10000};
    public static final int[] iterations = {200, 200, 100, 100, 50, 25, 10};
    public static final int[] seeds = {123, 69, 420, 666, 333, 1137, 1};
    public static void main(String[] args) {

        //run same inputs 5 times each to get proper average
        for(int j=0; j < 5; j++){
            for(int i=0; i < seeds.length; i++){
                try{
                    String[] command = {"javac", "C:\\Users\\aspen\\Desktop\\Masters\\_A+A\\AA-Assign1"};
        
                    ProcessBuilder processBuilder = new ProcessBuilder(command);
            
                    ProcessBuilder p = processBuilder.command("java.exe", "Generate", Integer.toString(listSizes[i]), Integer.toString(iterations[i]), Integer.toString(seeds[i]), "ALL");
            
                    System.out.println(p.command());
            
                    Process process = processBuilder.start();
            
                    int ret = process.waitFor();
            
                    System.out.printf("Program exited with code: %d", ret);
                    System.out.println();
                } catch (Exception e){
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
       
    }

}