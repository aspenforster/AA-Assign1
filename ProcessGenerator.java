import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ProcessGenerator{

    public ArrayList<ArrayList<String>> randomLists;

    private int listSize;
    private int numList;
    private int seed;

    public ProcessGenerator(int listSize, int numList, int seed){
        this.listSize = listSize;
        this.numList = numList;
        this.seed = seed;
        randomLists = new ArrayList<ArrayList<String>>();

        Random r = new Random(seed);
        for(int i=0; i < numList; i++){
            ArrayList<String> tempList = new ArrayList<String>();
            for(int j=0; j < listSize; j++){
                tempList.add("EN P" + j + " " +  r.nextInt(100));
            }
            //Collections.shuffle(tempList);

            randomLists.add(tempList);
        }
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

    public void saveToFile(){
        try {
            File f = new File("./");
            System.out.println(f.getAbsolutePath());
            int listCount = 0;
            for(ArrayList<String> list : randomLists){
                PrintWriter p = new PrintWriter(new File("./datagenfiles/size"+ listSize + "-list" + listCount + "(seed" + seed + ").in"));
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