package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GenAlg;
import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.RandomMutation;

import java.util.Comparator;
import java.util.function.Function;

public class TestGenAlg {

    public static void main(String[] args) {
        double res = ackley20Dims(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});

        System.out.println(res);
    }

    private static void runAckley() {
        float[] min = {-40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f, -40.0f};
        float[] max = {+40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f, +40.0f};

        Function<Individual, Float> evalAckleyFunc = ind -> {
            float[] arr = ind.getGenome().array();
            return (float) ackley20Dims(arr);
        };

        Comparator comparator = new MinimizeFunctionComparator(evalAckleyFunc);

        var algo = new GenAlg(min, max,
                comparator, new RandomMutation(min, max), new ComparatorIndividual(0.0000001f),0.1f, 1000);
        algo.run();
    }

    public static float ackley20Dims(float[] x) {
        // Sanity check
        if (x.length != 20) {
            throw new RuntimeException("You need to me with 20 parameters");
        }

        double sum1 = 0.0;
        double sum2 = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum1 += Math.pow(x[i], 2);
            sum2 += (Math.cos(2 * Math.PI * x[i]));
        }

        return (float) (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / ((double) x.length))) + 20
                - Math.exp(sum2 / ((double) x.length)) + Math.exp(1.0));
    }
}