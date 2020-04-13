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

    private static HashMap<List<Integer>, List<TimeRecord>> timeRecords;

    


    public static void main(String[] args){

        try{
            int listSize = Integer.parseInt(args[0]);
            int numLists = Integer.parseInt(args[1]);
            int seed = Integer.parseInt(args[2]);
            //operation is "EN", "DE" or "PT"
            String operation = args[3];

            //initialise the timeRecords dictionary
            timeRecords = new HashMap<List<Integer>, List<TimeRecord>>();
 
            pw = setupRecordsFile();

            if(operation.equals("ALL")){
                generator = generateInputFiles(listSize, numLists, seed, "EN");
                generateTimeRecords(generator, pw);

                generator = generateInputFiles(listSize, numLists, seed, "DE");
                generateTimeRecords(generator, pw);

                generator = generateInputFiles(listSize, numLists, seed, "PT");
                generateTimeRecords(generator, pw);

            } else {
                generator = generateInputFiles(listSize, numLists, seed, operation);
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
            File timeFile = new File("timedata.csv");
            if(timeFile.exists()){     

                BufferedReader br = new BufferedReader(new FileReader(timeFile));

                String line = "";
                while((line = br.readLine()) != null){
                    //get rid of whitespaces in csv line (if any)
                    line = line.replaceAll("\\s", "");

                    //split csv line into array of strings
                    String[] tempString = line.split(",");

                    //"key" values
                    int seed = Integer.parseInt(tempString[0]);
                    int size = Integer.parseInt(tempString[1]);
                    int index = Integer.parseInt(tempString[2]);

                    List<Integer> keyList = new ArrayList<Integer>();

                    keyList.add(seed);
                    keyList.add(size);
                    keyList.add(index);

                    //make a list to hold records for this configuration, if any
                    List<TimeRecord> recordList = new ArrayList<TimeRecord>();

                    //if tempString still has tokens, we've got time records to read in
                    if (tempString.length > 3){
                        //each 5 values constitues a time record object
                        for(int i = 3; i < tempString.length; i+=5){
                            //read in next 5 values to create time record
                            //don't need to read in average value which would be at tempString[i+4] as the timerecord object can calculate that itself
                            //TimeRecord(operation, data structure, count, totalTime, avgTime)
                            TimeRecord t = new TimeRecord(tempString[i], tempString[i+1], Integer.parseInt(tempString[i+2]), Double.parseDouble(tempString[i+3]));
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
                pw = new PrintWriter(new File("timedata.csv"));
            }

        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return pw;
    }
    /**
     * Generate input files from list length, number of lists and seed provided in cmd line
     * saves in relative "inputs" folder
     */
    public static ProcessGenerator generateInputFiles(int listSize, int numLists, int seed, String operation){
        //generate input files based on supplied arguments
        return new ProcessGenerator(listSize, numLists, seed, operation);
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
        
        pb.command("java.exe", "src/RunqueueTester", dataStruct, filePath + ".in", filePath + ".out");  

        long startTime = System.nanoTime();
        try {
            
            Process process = pb.start();

            int returnValue = process.waitFor();

            long endTime = System.nanoTime();

            double timeResult = ((double)(endTime-startTime)) / Math.pow(10, 9);

            List<Integer> keyList = new ArrayList<Integer>();

            keyList.add(generator.getSeed());
            keyList.add(generator.getListSize());
            keyList.add(listNum);

            TimeRecord t = new TimeRecord("err", "err", 0, 0);
            //if this key already exists in records
            if(timeRecords.get(keyList) != null){
                //loop over each time record associated with this key
                boolean existingRecord = false;
                for(TimeRecord tR : timeRecords.get(keyList)){
                    
                    //if the operation (EN, DE, PT) AND type (array, linkedlist, BST) match 
                    // we have an existing record for this config, grab a reference to that for further down
                    if (tR.getType().equals(dataStruct) && tR.getOperation().equals(generator.getOperation()) && !existingRecord){
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
                        t = new TimeRecord(generator.getOperation(), dataStruct, 1, timeResult);
                        timeRecords.get(keyList).add(t);
                }
            //if this key doesn't exist in timeRecords, set up new time record list and add it and it's key to timeRecords
            } else {
                List<TimeRecord> timeList = new ArrayList<TimeRecord>();
                t = new TimeRecord(generator.getOperation(), dataStruct, 1, timeResult);
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
        pw.println("seed, listSize, iteration-1, operation-1, type-1, count-1, totalTime-1, avgTime-1, iteration-2, operation-2, type-2, count-2, totalTime-2, avgTime-2, operation-3, type-3, count-3, totalTime-3, avgTime-3");

        for(Map.Entry<List<Integer>, List<TimeRecord>> entry : timeRecords.entrySet()){
            String record = entry.toString();
            record = record.replaceAll("\\[|\\]|\\s|\\(|\\)", "");
            record = record.replaceAll("\\=", ",");
            pw.println(record);
        }
        pw.close();
    }
}