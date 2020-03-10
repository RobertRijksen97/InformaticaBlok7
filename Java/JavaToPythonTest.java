package ProjectBlok7;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class JavaToPythonTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        String pythonPath = "/home/larsm/Documents/Programming/PythonScripts/Bioinformatica/Blok7/ProjectORF/TestPythonToJava.py";

        methode1(pythonPath);
//        methode2(pythonPath);
    }

    private static void methode1(String pythonPath) throws InterruptedException, IOException {
        ProcessBuilder processbuild = new ProcessBuilder().command("python", pythonPath, "Dit is arg 1?");
        Process p = processbuild.start();
        p.waitFor();

        System.out.println("Exitcode" + p.exitValue());

        String line;
        System.out.println("\nPython prints:");
        BufferedReader inputreader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
        while ((line = inputreader.readLine()) != null)
            System.out.println(line);


        BufferedReader errorreader = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
        System.out.println("\nPython errors:");
        while ((line = errorreader.readLine()) != null)
            System.out.println(line);
    }

    private static void methode2(String pythonString) throws IOException, InterruptedException {
        String cmd;
//        if (System.IS_OS_LINUX) {
            cmd = "python " + pythonString + " Dit_is_argv_1?";
//        } else {cmd = "blabla"}
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        BufferedReader inputreader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = inputreader.readLine()) != null)
            System.out.println(line);
    }


////import org.python.util.PythonInterpreter;
////import org.python.core.*;
//
//        public static void methode3(String pythonString){
//        // je moet dingen installeren
//            PythonInterpreter python = new PythonInterpreter();
//
//            int number1 = 10;
//            int number2 = 32;
//            python.set("number1", new PyInteger(number1));
//            python.set("number2", new PyInteger(number2));
//            python.exec("number3 = number1+number2");
//            PyObject number3 = python.get("number3");
//            System.out.println("val : "+number3.toString());
//        }
}




