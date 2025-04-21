package divinejr.linalg.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.ejml.equation.Equation;
import org.ejml.simple.ConstMatrix;
import org.ejml.simple.SimpleMatrix;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // File testFile = new File("src/main/resources/currents.m");
        File realFile = new File("src/main/resources/UTF-8currents9V.m");

        // System.out.println("testFile: " + testFile.exists()); //For debug purposes

        // SimpleMatrix theMatrix = readFile(testFile);
        SimpleMatrix theMatrix = readFile(realFile);

        // SimpleMatrix testMatrix = new SimpleMatrix(new double[][] {{1.0, 4.0, 9.0, 9.0}, {6.0, 2.0, 5.0, 7.0}, {8.0, 7.0, 3.0, 5.0}});
        SimpleMatrix rrefMatrix = doRREF(theMatrix);
        printMatrix(rrefMatrix);
        // interpretMatrixAbs(rrefMatrix); //With absolute values on the final currents
        interpretMatrix(rrefMatrix); //With not so absolute vales on the final currents
    }

    public static SimpleMatrix doRREF(SimpleMatrix input) {
        Equation eq = new Equation();
        eq.alias(input, "A");
        eq.process("B = rref(A)");

        SimpleMatrix out = eq.lookupSimple("B");

        return out;
    }

    public static void printMatrix(SimpleMatrix input) {
        for(int i = 0; i < input.getNumRows(); i++) {
            SimpleMatrix r = input.getRow(i);
            for(int j =0; j < r.getNumCols(); j++) {
                System.out.print(r.get(j)+ " ");
            }
            System.out.println();
        }

    }

    public static void interpretMatrixAbs(SimpleMatrix input) {
        for(int i = 0; i < input.getNumRows(); i++) {
            System.out.println("I" + (i + 1) + ": " + Math.abs(input.get(i, input.getNumCols() - 1)));
        }
    }

    public static void interpretMatrix(SimpleMatrix input) {
        for(int i = 0; i < input.getNumRows(); i++) {
            System.out.println("I" + (i + 1) + ": " + input.get(i, input.getNumCols() - 1));
        }
    }

    public static SimpleMatrix readFile(File inputFile) {
        try{
            Scanner input = new Scanner(inputFile);
            String entireFile = "";
            
            //Read from file
            while(input.hasNextLine()) {
                entireFile = entireFile + input.nextLine();
            }
            
            input.close();

            //Format file data to be actually useful
            String actualData = entireFile.substring(entireFile.indexOf("[") + 1, entireFile.indexOf("]"));
            actualData = actualData.replaceAll(";...", "\n");
            actualData = actualData.replaceAll(",", "");

            //Turn useful data into SimpleMatrix object
            input = new Scanner(actualData);
            ArrayList<Double[]> matrixList = new ArrayList<Double[]>();

            while(input.hasNextLine()) { //Takes in the useful data line by line
                String currentRow = input.nextLine();
                Scanner line = new Scanner(currentRow); //I like scanners, sue me
                ArrayList<Double> currentRowList = new ArrayList<Double>();

                while(line.hasNext()) {
                    currentRowList.add(line.nextDouble()); //Add numbers from line into list
                }

                matrixList.add(currentRowList.toArray(new Double[] {})); //Turn them into a Double[] and then put it in the full list
            }

            // Turn full matrix into Double[][] then into a SimpleMatrix!
            Double[][] matrixArrayBad = matrixList.toArray(new Double[][] {{}});
            double[][] matrixArray = new double[matrixArrayBad.length][matrixArrayBad[0].length];
            
            

            for(int i = 0; i < matrixArray.length; i++) {
                for(int j = 0; j < matrixArray[i].length; j++) {
                    matrixArray[i][j] = matrixArrayBad[i][j].doubleValue();
                }
            }
            return new SimpleMatrix(matrixArray); //have to typecase from wrapper class to primitive type
            

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
