
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


class UniformCrossOver
{
    Random rnd = new Random();
    ArrayList<Float> output = new ArrayList<Float>();
    ArrayList<ArrayList<Float>> input = new ArrayList<ArrayList<Float>>();
    int population = 30, maxRounds = 100000;
    FeatureSet[] featureSet = new FeatureSet[population];
    File dataFile = null;

    public static void main(String[] args) {

      // Input validation
      if (args.length != 1) {

          System.err.println("Invalid arguments, Program must be run in the form 'java UniformCrossOver dataSet.txt' where dataSet.txt is a text file containing the data being analised ");

      } else {

        UniformCrossOver uniformCrossOver = new UniformCrossOver(args[0]);

      }

    }

    UniformCrossOver(String fileName) {


      dataFile = new File(fileName);


        loadData();
        populate(); // Generate a random population
        run();

        // Best
        System.out.println("\n\nBest Round:");
        for (int i = 0; i < population; i++)
            System.out.println(i + ". " + featureSet[i] + "\t Fitness: " + featureSet[i].getFitness() + "\t Correct: " + featureSet[i].getCorrect() + "/" + input.size() + "\t Feature Size: " + featureSet[i].getFeatureSize());

        featureSet[0].getFitness();

        // Print any feature sets found that are 100% correct
        System.out.println("\n \n 100% correct Minimum Cardinality Feature Sets found:");

        boolean noneCorrect = true;
        String featuresUsed = "";

        for (int i = 0; i < population; i++) {

            if (featureSet[i].getCorrect() == input.size() ) {

                noneCorrect = false;
                System.out.println(i + ". Features included in set:" + featureSet[i].getFeaturesUsed() + "\t Fitness: " + featureSet[i].getFitness() + "\t Correct: " + featureSet[i].getCorrect() + "/" + input.size() + "\t Feature Size: " + featureSet[i].getFeatureSize());

            }

        }

        if (noneCorrect == true) {

            System.out.println("None :/");
        }


    }

    void populate()
    {
        for (int i = 0; i < population; i++) {
            featureSet[i] = new FeatureSet(input.get(0).size());
        }
    }

    void run()
    {
        int round = 0;
        boolean allCorrect;
        do {
            allCorrect = false;

            Arrays.sort(featureSet);
            for (int i = 0; i < population; i++) {
                if (i >= 2 && i <= 20)
                    featureSet[i].mutate(featureSet[i], featureSet[rnd.nextInt(population)]);
                if (i > 20)
                    featureSet[i] = new FeatureSet(input.get(0).size());

            }

            // Evaluate
            for (int i = 0; i < population; i++) {
                featureSet[i].fitness(input, output);
            }


            round++;

        } while (round < maxRounds && !allCorrect);

        System.out.println("Iterations: " + round);

    }

    void loadData() {

      try {



        Scanner sc = new Scanner(dataFile);

        int samples = 0;
        sc.nextLine();
        while (sc.hasNextLine() && samples < 900) {
            samples++;
            String line = sc.nextLine();
            String[] values = line.split(",");
            input.add(new ArrayList<Float>());

            for (int i = 0; i < values.length; i++) {
                if (i < values.length - 1)
                    input.get(input.size() - 1).add(Float.parseFloat(values[i]));
                else
                    output.add(Float.parseFloat(values[i]));
            }
        }
        System.out.println("Finished Reading in the sample data. Number of samples: " + samples);

      } catch(FileNotFoundException e) {


      }

    }
}
