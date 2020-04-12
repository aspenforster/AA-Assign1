import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class ProcessGenerator{

    public ArrayList<ArrayList<String>> randomLists;

    private int listSize;
    private int numList;
    private int seed;
    private String fileString;
    private String scenario;

    public ProcessGenerator(int listSize, int numList, int seed, String scenario){
        this.listSize = listSize;
        this.numList = numList;
        this.seed = seed;
        this.fileString = "./datagenfiles/size"+ listSize + "-list%d(seed" + seed + ")-" + scenario;
        this.scenario = scenario;
        randomLists = new ArrayList<ArrayList<String>>();

        Random r = new Random(seed);
        
        for(int i=0; i < numList; i++){
            int randomIndex = r.nextInt(listSize);
            ArrayList<String> tempList = new ArrayList<String>();
            for(int j=0; j < listSize; j++){
                tempList.add("EN P" + j + " " +  r.nextInt(100));
            }
            
            if(scenario.equals("DE")){
                for(int j=0; j< listSize; j++){
                    tempList.add("DE");
                }
            }

            if(scenario.equals("PT")){
                tempList.add("PT P" +randomIndex);
            }
            randomLists.add(tempList);
            saveToFile(scenario);
        }
    }

    public int getListSize(){
        return listSize;
    }

    public int getNumList(){
        return numList;
    }

    public int getSeed(){
        return seed;
    }

    public String getScenario(){
        return scenario;
    }

    public String getFileString(){
        return fileString;
    }

    public void printLists(){
        int listCount = 0;
        for(ArrayList<String> list : randomLists){
            System.out.println("list " + listCount);
            listCount++;
            for(String s : list){
                System.out.println(s);
            }
        }
    }

    public void saveToFile(String scenario){
        try {
            File f = new File("./");
            int listCount = 0;
            for(ArrayList<String> list : randomLists){
                PrintWriter p = new PrintWriter(new File(String.format(fileString, listCount) + ".in"));
                for(String s : list){
                    p.println(s);
                }

                p.println("PA");
                p.close();
                listCount++;
            }
            
            
        } catch (Exception e){

        }

        
    }
}