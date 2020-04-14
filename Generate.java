import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Generate {

    private static ProcessBuilder pb;
    private static PrintWriter pw;

    private static ProcessGenerator generator;

    private static HashMap<List<String>, List<TimeRecord>> timeRecords;

    private static boolean unique;


    public static void main(String[] args){

        try{
            int listSize = Integer.parseInt(args[0]);
            int numLists = Integer.parseInt(args[1]);
            int seed = Integer.parseInt(args[2]);
            //operation is "EN", "DE" or "PT"
            String operation = args[3];
            unique = Boolean.parseBoolean(args[4]);

            //initialise the timeRecords dictionary
            timeRecords = new HashMap<List<String>, List<TimeRecord>>();
 
            pw = setupRecordsFile();

            List<String> keyList = new ArrayList<String>();

            keyList.add(operation);
            keyList.add("tree");
            keyList.add(Integer.toString(listSize));

            boolean writeToFile = false;

            //if all was input, make processes for EN, DE and PT
            if(operation.equals("ALL")){

                if (timeRecords.get(keyList) == null){
                    writeToFile = true;
                }

                generator = new ProcessGenerator(listSize, numLists, seed, "EN", writeToFile);
            
                generateTimeRecords(generator, pw);

                generator = new ProcessGenerator(listSize, numLists, seed, "DE", writeToFile);
            
                generateTimeRecords(generator, pw);

                generator = new ProcessGenerator(listSize, numLists, seed, "PT", writeToFile);
            
                generateTimeRecords(generator, pw);

            } else {
                generator = new ProcessGenerator(listSize, numLists, seed, operation, writeToFile);
            
                generateTimeRecords(generator, pw);
            }

            saveTimeRecords(pw);

        } catch (Exception e){
            System.out.println("Wrong arguments parsed - try again");
            System.exit(1);
        }

    }

    /**
     * open/create time data csv file
     * read in previous records if any
     */
    public static PrintWriter setupRecordsFile(){

        try {
            String fileName = "";
            if(unique){
                fileName = "timedata_unique.csv";
            } else {
                fileName = "timedata.csv";
            }
            File timeFile = new File(fileName);
            if(timeFile.exists()){     

                BufferedReader br = new BufferedReader(new FileReader(timeFile));

                String line = "";

                //dump first line in csv that stores headers
                br.readLine();
                
                while((line = br.readLine()) != null){
                    //get rid of whitespaces in csv line (if any)
                    line = line.replaceAll("\\s", "");

                    //split csv line into array of strings
                    String[] tempString = line.split(",");

                    List<String> keyList = new ArrayList<String>();

                    keyList.add(tempString[0]);
                    keyList.add(tempString[1]);
                    keyList.add(tempString[2]);

                    //make a list to hold records for this configuration, if any
                    List<TimeRecord> recordList = new ArrayList<TimeRecord>();

                    //if tempString still has tokens, we've got time records to read in
                    if (tempString.length > 3){
                        //each 5 values constitues a time record object
                        for(int i = 3; i < tempString.length; i+=5){
                            //read in next 5 values to create time record
                            //don't need to read in average value which would be at tempString[i+4] as the timerecord object can calculate that itself

                            //public TimeRecord(int seed, int iteration, int count, double totalTime){
                            int seed = Integer.parseInt(tempString[i]);
                            int iteration = Integer.parseInt(tempString[i+1]);
                            int count = Integer.parseInt(tempString[i+2]);
                            double totalTime = Double.parseDouble(tempString[i+3]);
                            //double avgTime = Double.parseDouble(tempString[i+4]);

                            TimeRecord t = new TimeRecord(seed, iteration, count, totalTime);

                            recordList.add(t);
                        }
                    }
                    //finally, add time record objects to dictionary
                    timeRecords.put(keyList, recordList);  
                }

                br.close();

                //set up printwriter to point to existing timedata.csv file
                //will save over old records at end - maybe not perfect solution
                FileWriter fwTime = new FileWriter(timeFile);
                pw = new PrintWriter(fwTime);
            } else {
                //make timedata.csv if it didn't exist
                pw = new PrintWriter(new File(fileName));
            }

        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return pw;
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

        String cmdString;
        if (unique){
            cmdString = ".\\src\\RunqueueTesterUnique";
        } else {
            cmdString = ".\\src\\RunqueueTester";
        }
        //set up command to actually run runqueue tester
        String[] command = {"javac", cmdString};

        //make proc builder object 
        pb = new ProcessBuilder(command);
        
        pb.command("java.exe", cmdString, dataStruct, filePath + ".in", filePath + ".out");  

        long startTime = System.nanoTime();
        try {
            
            Process process = pb.start();

            int returnValue = process.waitFor();

            long endTime = System.nanoTime();

            double timeResult = ((double)(endTime-startTime)) / Math.pow(10, 9);

            List<String> keyList = new ArrayList<String>();

            keyList.add(generator.getOperation());
            keyList.add(dataStruct);
            keyList.add(Integer.toString(generator.getListSize()));

            TimeRecord t = new TimeRecord(0, 0, 0, 0.0);
            //if this key already exists in records
            if(timeRecords.get(keyList) != null){
                //loop over each time record associated with this key
                boolean existingRecord = false;
                for(TimeRecord tR : timeRecords.get(keyList)){
                    
                    //if the seed and iteration number are the same, we've already run this config
                    //and should add new time to exisiting record
                    if (tR.getSeed() == generator.getSeed() && tR.getIteration() == listNum && !existingRecord){
                        t = tR;
                        existingRecord = true;   
                    }
                }
                //add this result to the existing time record for this config, rather than making a new time record
                if (existingRecord){
                    t.addTime(timeResult);
                
                // if there was no existing record, make a new one
                } else {
                        //public TimeRecord(String operation, String type, int count, double totalTime)
                        t = new TimeRecord(generator.getSeed(), listNum, 1, timeResult);
                        timeRecords.get(keyList).add(t);
                }
            //if this key doesn't exist in timeRecords, set up new time record list and add it and it's key to timeRecords
            } else {
                List<TimeRecord> timeList = new ArrayList<TimeRecord>();
                t = new TimeRecord(generator.getSeed(), listNum, 1, timeResult);
                timeList.add(t);
                timeRecords.put(keyList, timeList);
            }

            return timeResult;       
        } catch (Exception e){
            return -1;
        }
    }

    public static void generateTimeRecords(ProcessGenerator generator, PrintWriter pw){
         //iterate over the lists, retrieving the time taken for each one
         for(int i=0; i < generator.getNumList(); i++){
            recordTime(generator, i, "array");
            recordTime(generator, i,"linkedlist");
            recordTime(generator, i, "tree");
         } 
    }




    public static void saveTimeRecords(PrintWriter pw){
        //private static HashMap<List<Integer>, List<TimeRecord>> timeRecords;

        //write headers here
        pw.println("operation, data structure, listSize, seed, iteration, run count, total time, avg time");

        for(Map.Entry<List<String>, List<TimeRecord>> entry : timeRecords.entrySet()){
            String record = entry.toString();
            record = record.replaceAll("\\[|\\]|\\s|\\(|\\)", "");
            record = record.replaceAll("\\=", ",");
            pw.println(record);
        }
        pw.close();
    }
}