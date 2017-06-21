package RubixCubeGeneticSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class RubixCubeSimulation {

    private ArrayList<ArrayList> cubeData = new ArrayList<>();
    private LinkedList<int[][]> cubeStrips = new LinkedList();


    /*

    This class represents a rubix cube in a 2d array and then allows transformations to be done to it using transformCube class.
    Transformations are just complex matrix operations.
     */

    public RubixCubeSimulation(String inputName) {

        File inputFile = new File(inputName);


        //populate cubeStrips LinkedList

        /*
        Each strip represents a loop around the cube, first int identifies the side of the cube, second int, the individual tile.
         Strips ordered by the side they are adjacent to, i.e. strip 0 is adjacent to side 0
         */

        int[][] strip0 = {{1,0},{1,1},{1,2},{3,0},{3,1},{3,2},{5,8},{5,7},{5,6},{2,0},{2,1},{2,2}};
        int[][] strip1 = {{0,6},{0,7},{0,8},{3,0},{3,3},{3,6},{4,2},{4,1},{4,0},{2,8},{2,5},{2,2}};
        int[][] strip2 = {{0,0},{0,3},{0,6},{1,0},{1,3},{1,6},{4,0},{4,3},{4,6},{5,0},{5,3},{5,6}};
        int[][] strip3 = {{0,2},{0,5},{0,8},{1,2},{1,5},{1,8},{4,2},{4,5},{4,8},{5,2},{5,5},{5,8}};
        int[][] strip4 = {{1,6},{1,7},{1,8},{3,6},{3,7},{3,8},{5,2},{5,1},{5,0},{2,6},{2,7},{2,8}};
        int[][] strip5 = {{0,0},{0,1},{0,2},{3,2},{3,5},{3,8},{4,8},{4,7},{4,6},{2,6},{2,3},{2,0}};

        cubeStrips.add(strip0);
        cubeStrips.add(strip1);
        cubeStrips.add(strip2);
        cubeStrips.add(strip3);
        cubeStrips.add(strip4);
        cubeStrips.add(strip5);





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






    }

    public void transformCube(int face, int turnDirection) {



        // Perform some operations on cubeData


        cubeData = performOperationUponCube(face,turnDirection,cubeData);

        // now return

        System.out.println("End state: " + cubeData.toString());

        saveOutputToFile("output.txt");

    }


    /*

    performOperationUponCube

    Postconditions: inputCube transformed to represent cube having been turned in the direction of turnDirection.


     */

    private ArrayList<ArrayList> performOperationUponCube(int side, int turnDirection,ArrayList<ArrayList> inputCube) {


        ArrayList<ArrayList> clonedCube = deepCopyCube(inputCube);


        // rotate the facing side.

        /*
        E.g. direction = 1
        0 0 1   0 0 0
        0 1 1 = 0 1 0
        0 0 1   1 1 1

        0 1 2
        3 4 5
        6 7 8
         */
        ArrayList<Integer> currentValueFace = inputCube.get(side);



        clonedCube.get(side).set(4,currentValueFace.get(4));

        // Transform resultFace.
        if (turnDirection == -1) {

            clonedCube.get(side).set(0,currentValueFace.get(2));
            clonedCube.get(side).set(1,currentValueFace.get(5));
            clonedCube.get(side).set(2,currentValueFace.get(8));
            clonedCube.get(side).set(3,currentValueFace.get(1));

            clonedCube.get(side).set(5,currentValueFace.get(7));

            clonedCube.get(side).set(6,currentValueFace.get(0));
            clonedCube.get(side).set(7,currentValueFace.get(3));
            clonedCube.get(side).set(8,currentValueFace.get(6));

        } else {

            clonedCube.get(side).set(0,currentValueFace.get(6));
            clonedCube.get(side).set(1,currentValueFace.get(3));
            clonedCube.get(side).set(2,currentValueFace.get(0));
            clonedCube.get(side).set(3,currentValueFace.get(7));

            clonedCube.get(side).set(5,currentValueFace.get(1));
            clonedCube.get(side).set(6,currentValueFace.get(8));
            clonedCube.get(side).set(7,currentValueFace.get(5));
            clonedCube.get(side).set(8,currentValueFace.get(2));

        }


        // Now, move the adjacent pieces.

        int[][] stripToModify = cubeStrips.get(side);


        int[] tempTile = {0,7};



        for (int i = 0; i < stripToModify.length; i++ ) {


            int newValueForCurrentTile = getTileThreeSpacesAroundCube(stripToModify[i],turnDirection,inputCube,stripToModify);

            clonedCube.get(stripToModify[i][0]).set(stripToModify[i][1],newValueForCurrentTile);

        }







        return clonedCube;
    }

    private int getTileThreeSpacesAroundCube(int[] tile, int direction,ArrayList<ArrayList> cube, int[][] strip) {


        int sign = 1;

        if (direction == 1) {
            sign = -1;
        }

            int indexOfTileInStrip = searchForTileInStripArray(tile,strip);

            // Add a sprinkle of modular arithmetic.
            int number = indexOfTileInStrip + (3 * sign);
            int mod = strip.length;
            int answer = ((number % mod) + mod) % mod;

            // Now we know where in the strip the correct value is, find the value
            int indexOfTileThreeBack = answer;

            int side = strip[indexOfTileThreeBack][0];
            int tileOnSide = strip[indexOfTileThreeBack][1];

            return (int)cube.get(side).get(tileOnSide);


    }

    private int searchForTileInStripArray(int[] tile, int[][] strip) {
        /*
        Custom search function that returns the index of the found element.
        Returns -1 if not found
         */

        for (int i = 0; i < strip.length; i++) {

            if ((strip[i][0] == tile[0]) && (strip[i][1] == tile[1])) {
                // Match
                return i;
            }
        }
        return -1;
    }

    private ArrayList<ArrayList> deepCopyCube(ArrayList<ArrayList> inputCube) {

        ArrayList<ArrayList> outputCube = new ArrayList<>();

        for (ArrayList side : inputCube) {

            ArrayList newSide = new ArrayList<Integer>();
            outputCube.add(newSide);

            for (int i = 0; i < 9; i++ ) {
                newSide.add(side.get(i));
            }
        }
        return outputCube;
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
