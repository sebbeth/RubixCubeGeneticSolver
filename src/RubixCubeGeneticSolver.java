import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class RubixCubeGeneticSolver {

    private ArrayList<ArrayList> cubeData = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Rubix Cube Genetic solver");

        new RubixCubeGeneticSolver(args[0]);
    }


    public RubixCubeGeneticSolver(String inputName) {

        File inputFile = new File(inputName);

        // Parse input from file and store in cubeData

        try {

            Scanner scanner = new Scanner(inputFile);

            scanner.useDelimiter(" ");

            int i = 0;
            int j = 0;
            cubeData.add(new ArrayList<Integer>());

            while (scanner.hasNext()) {

                cubeData.get(j).add(Integer.parseInt(scanner.next()));
                i++;
                if (i >= 9) {

                    if (scanner.hasNext()) {
                        cubeData.add(new ArrayList<Integer>());
                        i = 0;
                        j++;
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Start state of cube now stored.

        System.out.println("Start state: " + cubeData.toString());


        // Perform some operations on cubeData


        cubeData = performOperationUponCube(0,-1,cubeData);

        // now return

        System.out.println("End state: " + cubeData.toString());

        saveOutputToFile("output.txt");


    }


    /*

    performOperationUponCube

    Postconditions: inputCube transformed to represent cube having been turned in the direction of turnDirection.


     */

    private ArrayList<ArrayList> performOperationUponCube(int side, int turnDirection,ArrayList<ArrayList> inputCube) {



        // rotate the facing side.

        /*
        E.g. direction = 1
        0 0 1   0 0 0
        0 1 1 = 0 1 0
        0 0 1   1 1 1

        1 1 1
        0 1 0
        0 0 0

        0 1 2
        3 4 5
        6 7 8
         */
        ArrayList<Integer> resultFace = new ArrayList<>();
        ArrayList<Integer> currentValueFace = inputCube.get(side);
        // fill with default values
        for (int i=0; i < 9;i++) {
            resultFace.add(-1);
        }

        resultFace.set(4,currentValueFace.get(4));

        // Transform resultFace.
        if (turnDirection == -1) {

                resultFace.set(0,currentValueFace.get(2));
                resultFace.set(1,currentValueFace.get(5));
                resultFace.set(2,currentValueFace.get(8));
                resultFace.set(3,currentValueFace.get(1));

                resultFace.set(5,currentValueFace.get(7));

                resultFace.set(6,currentValueFace.get(0));
                resultFace.set(7,currentValueFace.get(3));
                resultFace.set(8,currentValueFace.get(6));

        } else {

                resultFace.set(0,currentValueFace.get(6));
                resultFace.set(1,currentValueFace.get(3));
                resultFace.set(2,currentValueFace.get(0));
                resultFace.set(3,currentValueFace.get(7));

                resultFace.set(5,currentValueFace.get(1));
                resultFace.set(6,currentValueFace.get(8));
                resultFace.set(7,currentValueFace.get(5));
                resultFace.set(8,currentValueFace.get(2));

        }

        inputCube.set(side,resultFace);


        return inputCube;
    }

    private void saveOutputToFile(String outputFileName) {

        String output = "";

        for (ArrayList side : cubeData) {
            for (Object x : side) {
                if (output.length() == 0) {
                    output = output + x.toString();
                } else {
                    output = output + " " + x.toString();
                }
            }
        }

        try {
            writeToFile(output,outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void writeToFile(String text, String targetFilePath) throws IOException
    {
        Path targetPath = Paths.get(targetFilePath);
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        Files.write(targetPath, bytes, StandardOpenOption.CREATE);
    }
}
