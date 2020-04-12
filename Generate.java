import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Generate {


    private static ProcessBuilder pb;
    private static PrintWriter pw;

    private static ProcessGenerator generator;

    public static void main(String[] args){

        try{
            int listSize = Integer.parseInt(args[0]);
            int numLists = Integer.parseInt(args[1]);
            int seed = Integer.parseInt(args[2]);
            String scenario = args[3];
 
            pw = setupFileWriting();
            if(scenario.equals("ALL")){
                generator = generateInputFiles(listSize, numLists, seed, "EN");
                generateTimeRecords(generator, pw);

                generator = generateInputFiles(listSize, numLists, seed, "DE");
                generateTimeRecords(generator, pw);

                generator = generateInputFiles(listSize, numLists, seed, "PT");
                generateTimeRecords(generator, pw);

            } else {
                generator = generateInputFiles(listSize, numLists, seed, scenario);
                generateTimeRecords(generator, pw);
            }

        } catch (Exception e){
            System.out.println("Wrong arguments parsed - try again");
            System.exit(1);
        }

        pw.close();
    }

    /**
     * open/create time data csv file
     */
    public static PrintWriter setupFileWriting(){

        try {
            File timeFile = new File("timedata.csv");
            if(timeFile.exists()){                
                //the second argument here puts it in append mode (rather than override)
                FileWriter fwTime = new FileWriter(timeFile, true);
                pw = new PrintWriter(fwTime);
                //pwTime.println("test again");
                //pw.close();
            } else {
                pw = new PrintWriter(new File("timedata.csv"));
            }

        } catch (Exception e){
            System.out.println("something went wrong");
            System.exit(1);
        }

        return pw;
    }
    /**
     * Generate input files from list length, number of lists and seed provided in cmd line
     * saves in relative "inputs" folder
     */
    public static ProcessGenerator generateInputFiles(int listSize, int numLists, int seed, String scenario){
        //generate input files based on supplied arguments
        return new ProcessGenerator(listSize, numLists, seed, scenario);
    }

    /**
     * Helper function, returns the running time of RunqueueTester program
     * or -1 if something went wrong
     * @param listSize - length of list we're recording
     * @param listNum - the index of the list we're recording time for 
     * @param seed - the seed the list was generated with
     * @param dataStruct - what data structure to record (array, linkedlist or tree)
     * @return
     */
    public static double recordTime(ProcessGenerator generator, int listNum, String dataStruct){
        String filePath = String.format(generator.getFileString(), listNum);

        //set up command to actually run runqueue tester
        String[] command = {"javac", ".\\src\\RunqueueTester"};

        //make proc builder object 
        pb = new ProcessBuilder(command);

        System.out.println(filePath);
        
        pb.command("java.exe", "src/RunqueueTester", "tree", filePath + ".in", filePath + ".out");  

        long startTime = System.nanoTime();
        try {
            
            Process process = pb.start();

            int returnValue = process.waitFor();

            long endTime = System.nanoTime();

            BufferedReader reader = 
            new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }   
            String result = builder.toString();
    
            System.out.println(result);

            return ((double)(endTime - startTime)) / Math.pow(10, 9);           
        } catch (Exception e){
            return -1;
        }
    }

    public static void generateTimeRecords(ProcessGenerator generator, PrintWriter pw){
        
         //iterate over the lists, retrieving the time taken for each one
         for(int i=0; i < generator.getNumList(); i++){
            String record = "";
            record += "Seed" + generator.getSeed() + ",";
            record += "Size" + generator.getListSize() + ",";
            record += "i-" + i + ",";
            record += recordTime(generator, i, "array") + ",";
            record += recordTime(generator, i,"linkedlist") + ",";
            record += recordTime(generator, i, "tree") + ",";
            record += generator.getScenario();
            System.out.println(record);
            pw.println(record);
         }
         
    }
}