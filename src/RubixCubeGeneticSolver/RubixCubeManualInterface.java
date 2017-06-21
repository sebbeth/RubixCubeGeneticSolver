package RubixCubeGeneticSolver;

import java.util.Scanner;

/**
 * Created by seb on 21/6/17.
 */
public class RubixCubeManualInterface {

    static RubixCubeSimulation rubixCubeSimulation;

    public static void main(String args[]) {


        System.out.println("Rubix Cube Genetic solver");

        rubixCubeSimulation = new RubixCubeSimulation(args[0]);

        if (args.length == 2) { // take input string from from input argument

            System.out.println("End state: " + rubixCubeSimulation.transformCubeFromCommandString(args[1]));

            return;
        }


        while (true) {

            Scanner scanner = new Scanner(System.in);

            int input = scanner.nextInt();

            switch (input) {

                case 1:
                    rubixCubeSimulation.transformCube(0,1);
                    break;
                case 2:
                    rubixCubeSimulation.transformCube(1,1);
                    break;
                case 3:
                    rubixCubeSimulation.transformCube(2,1);
                    break;
                case 4:
                    rubixCubeSimulation.transformCube(3,1);
                    break;
                case 5:
                    rubixCubeSimulation.transformCube(4,1);
                    break;
                case 6:
                    rubixCubeSimulation.transformCube(5,1);
                    break;
                case -1:
                    rubixCubeSimulation.transformCube(0,-1);
                    break;
                case -2:
                    rubixCubeSimulation.transformCube(1,-1);
                    break;
                case -3:
                    rubixCubeSimulation.transformCube(2,-1);
                    break;
                case -4:
                    rubixCubeSimulation.transformCube(3,-1);
                    break;
                case -5:
                    rubixCubeSimulation.transformCube(4,-1);
                    break;
                case -6:
                    rubixCubeSimulation.transformCube(5,-1);
                    break;
                default:
                    System.out.println("Invalid");

            }
        }
    }
}
