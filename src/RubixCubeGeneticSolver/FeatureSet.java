

import java.util.*;

class FeatureSet implements Comparable<FeatureSet>
{
    Random rnd = new Random();
    float[] genes;
    float fitness;
    int correct = 0;

    FeatureSet(int chromosomeLength)
    {
        genes = new float[chromosomeLength];

        for (int i = 0; i < chromosomeLength; i++)
            if (rnd.nextFloat() < 0.1f)
                genes[i] = rnd.nextFloat() * 4 - 2;
            else
                genes[i] = 0;
    }

    void mutate(FeatureSet p1, FeatureSet p2) {
        float crossoverRate = 0.5f; // Higher the percent, the more genes from p1
        int[] choice = new int[genes.length];

        // Set the whole gene to p1
        for (int i = 0; i < genes.length; i++) {
            genes[i] = p1.genes[i];

            // Create an array split by the crossoverRate and then shuffle the data
            if (i < genes.length * crossoverRate)
                choice[i] = 1;
            else
                choice[i] = 2;
        }

        // Shuffle
        int swapIndex, temp;
        for (int i = 0; i < choice.length; i++) {
            temp = choice[i];
            swapIndex = rnd.nextInt((choice.length - i)) + i;
            choice[i] = choice[swapIndex];
            choice[swapIndex] = temp;
        }

        // Crossover between parents
        for (int i = 0; i < choice.length; i++) {
            if (choice[i] == 1)
                genes[i] = p1.genes[i];
            else
                genes[i] = p2.genes[i];
        }
    }

    public int getFeatureSize() {
        int featureSize = 0;

        // Count how many features are used, '0.0' counts as not used
        for (int i = 0; i < genes.length; i++)
            if (genes[i] != 0)
                featureSize++;
        return featureSize;
    }

    public float fitness(ArrayList<ArrayList<Float>> input, ArrayList<Float> output)
    {
        // Reward the smaller the # of genes
        fitness = 0;
        float sizeEncouragement = 0;
        int featureSize = getFeatureSize();

        sizeEncouragement = 2.0f - (float)featureSize/(float)genes.length;

        // Check the answers
        correct = 0;
        float sum;
        for (int row = 0; row < input.size(); row++) {
            sum = 0;
            for (int column = 0; column < input.get(row).size(); column++)
                if (input.get(row).get(column) != -1)
                    sum += input.get(row).get(column) * genes[column];

            if (sum >= 0 && output.get(row) == 1)
                correct++;
             if (sum < 0 && output.get(row) == 0)
                correct++;
        }

        fitness = correct;

        if (correct == input.size())
            fitness *= sizeEncouragement;

        return fitness;
    }

    public int getCorrect() {
        return correct;
    }

    public String getFitness() {
        return Math.floor(fitness*100)/100 + "";
    }

    public String toString() {
        String output = "\t[", current;
        for (int i = 0; i < genes.length; i++) {
            current = Math.floor(genes[i]*100)/100 + "";
            if (current.equals("0.0"))
                current = "";
            while (current.length() < 5)
                current = " " + current;
            output += current;
            if (i < genes.length-1)
                output += ", ";
        }
        output += "]";

        return output;
    }

    public String getFeaturesUsed() {

        String output = "";
        for (int i = 0; i < genes.length; i++ ) {
            if (genes[i] != 0.0) {

                output = output + " , " + (i+1);
            }
        }
        return output;
    }

    @Override
    public int compareTo(FeatureSet o) {
        int out = 1;
        if (fitness > o.fitness)
            out = -1;
        return out;
    }
}
