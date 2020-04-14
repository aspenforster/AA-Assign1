import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import jdk.jfr.events.FileWriteEvent;

public class ConsolidateTimeRecords {
    
    public static void main(String[] args){
        makeTidyCSV();
    }


    public static void makeTidyCSV(){
        try {

            File file = new File("timedata.csv");

            System.out.println(file);

            File outputfile = new File("cleantimedata.csv");

            FileWriter fw = new FileWriter(outputfile);
            PrintWriter pw = new PrintWriter(fw);

            if (!file.exists()){
                System.out.println("no time data file found");
                System.exit(1);
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line = "";

                //String firstLine = br.readLine();

                while((line = br.readLine()) !=null){
                    line = line.replaceAll("\\s", "");

                    String[] record = line.split(",");

                    String op = record[0];
                    String struct = record[1];
                    String listSize = record[2];

                    for(int i=3; i < record.length; i+=5){
                        String seed = record[i];
                        String iteration = record[i+1];
                        String count = record[i+2];
                        String totalTime = record[i+3];
                        String avgTime = record[i+4];

                        String wholeString = op + "," + struct + "," + listSize + "," + seed + "," + iteration + "," + count + "," + totalTime + "," + avgTime;
                        pw.println(wholeString);
                    }
                    
                }

                br.close();
                pw.close();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}