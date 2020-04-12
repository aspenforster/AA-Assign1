import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Generate {


    private static int listSize;
    private static int numLists;
    private static int seed;

    private static ProcessBuilder pb;
    private static PrintWriter pw;

    private static ArrayList<double[]> timeRecords;

    public static void main(String[] args){

        try{
            listSize = Integer.parseInt(args[0]);
            numLists = Integer.parseInt(args[1]);
            seed = Integer.parseInt(args[2]);

        } catch (Exception e){
            System.out.println("Wrong arguments parsed - try again");
            System.exit(1);
        }

        timeRecords = new ArrayList<double[]>();

        populateTimeRecords();


        //check for existing record in csv, if it exists don't run again
        Boolean foundRecord = false;

        for(double[] record : timeRecords){
            if (listSize == record[0] && numLists <= record[1] && seed == record[2]){
                foundRecord = true;
            }
        }

        if(!foundRecord){
            generateInputFiles(listSize, numLists, seed);
            //these double arrays are only lists of run times
            generateTimeRecords(listSize, numLists, seed);
            pw.close();

        } else {
            System.out.println("this record already exists in timedata.csv");
        }

        

    }

    /**
     * Get previous time records in csv file
     * if no previous records, make new file
     * if previous records, populate timeRecords list
     * and put printwriter in append mode
     */
    public static void populateTimeRecords(){

        try {
            File timeFile = new File("timedata.csv");
            if(timeFile.exists()){

                BufferedReader br = new BufferedReader(new FileReader(timeFile));

                String line = "";
                while((line = br.readLine()) != null){
                    String[] tempString = line.split(",");
                    double[] tempDouble = new double[tempString.length]; 
            
                    for(int i =0; i < tempString.length; i++){
                        
                        tempDouble[i] = Double.parseDouble(tempString[i]);
                    }
                    
                    timeRecords.add(tempDouble);
                }

                br.close();
                
                //the second argument here puts it in append mode (rather than override)
                FileWriter fwTime = new FileWriter(timeFile, true);
                PrintWriter pw = new PrintWriter(fwTime);
                //pwTime.println("test again");
                //pw.close();
            } else {
                pw = new PrintWriter(new File("timedata.csv"));
            }

        } catch (Exception e){
            System.out.println("something went wrong");
            System.exit(1);
        }
    }
    /**
     * Generate input files from list length, number of lists and seed provided in cmd line
     * saves in relative "inputs" folder
     */
    public static void generateInputFiles(int listSize, int numLists, int seed){
        //generate input files based on supplied arguments
        ProcessGenerator procGenerator = new ProcessGenerator(listSize, numLists, seed);
        procGenerator.saveToFile();
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
    public static double recordTime(int listSize, int listNum, int seed, String dataStruct){
        String filePath = ".\\datagenfiles\\size"+listSize +"-list" + listNum + "(seed" + seed + ")";

        pb.command("java.exe", "src.RunqueueTester", dataStruct, filePath + ".in", filePath + ".out");  
        
        long startTime = System.nanoTime();
        try {
            
            Process process = pb.start();

            int returnValue = process.waitFor();

            long endTime = System.nanoTime();

            return ((double)(endTime - startTime)) / Math.pow(10, 9);           
        } catch (Exception e){
            return -1;
        }
    }

    public static void generateTimeRecords(int listSize, int numLists, int seed){
         //set up command to actually run runqueue tester
         String[] command = {"javac", ".\\src\\RunqueueTester"};

         //make proc builder object 
         pb = new ProcessBuilder(command);
        
        
         //iterate over the lists, retrieving the time taken for each one
         for(int i=0; i < numLists; i++){
            String record = "";
            record += listSize + ",";
            record += i + ",";
            record += seed + ",";
            record += recordTime(listSize, i, seed, "array") + ",";
            record += recordTime(listSize, i, seed, "linkedlist") + ",";
            record += recordTime(listSize, i, seed, "tree");

            pw.println(record);
         }
 
    }

    public static void printAllRecords(){
        for(double[] record : timeRecords){
            for(int i=0; i < record.length; i++){
                System.out.print(record[i] + ",");
            }
            System.out.println();
        }
    }
}