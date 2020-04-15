package generation;
import java.util.Date;

public class RunTimeTrials {

    public static final int[] listSizes = {10, 50};
    public static final int[] iterations = {2, 1};
    public static final int[] seeds = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static void main(String[] args) {

        int variations;
        String uniqueness = "False";
        if(args[0].equals("Both")){
            variations = 2;
        } else {
            variations = 1;
        }
        for(int k=0; k<variations; k++){
            if (k == 1){
                uniqueness = "True";
            }
        //run same inputs 3 times each to get proper average
            for(int j=0; j < 3; j++){
                for(int i=0; i < listSizes.length; i++){
                    try{
                        String[] command = {"javac", "C:\\Users\\aspen\\Desktop\\Masters\\_A+A\\Assign1-s3330837\\Assign1-s3330837"};

                        ProcessBuilder processBuilder = new ProcessBuilder(command);

                        ProcessBuilder p = processBuilder.command("java.exe", ".\\Generate", Integer.toString(listSizes[i]), Integer.toString(iterations[i]), Integer.toString(seeds[i]), "ALL", uniqueness);

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
              System.out.println("Loop " + j + " completed" + new Date());
            }
        }

    }

}
