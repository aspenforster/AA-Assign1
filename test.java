import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {

    public static void main(String[] args) throws IOException, InterruptedException {

        String[] command = {"javac", "C:\\Users\\aspen\\Desktop\\Masters\\_A+A\\AA-Assign1\\src\\RunqueueTester"};

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        ProcessBuilder p = processBuilder.command("java.exe", "src.RunqueueTester", "tree", "input.in", "output.out");

        System.out.println(p.command());
    

        Process process = processBuilder.start();

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

        int ret = process.waitFor();

        System.out.printf("Program exited with code: %d", ret);
    }
}