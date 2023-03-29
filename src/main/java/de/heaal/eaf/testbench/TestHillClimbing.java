/*
 * Evolutionary Algorithms Framework
 *
 * Copyright (c) 2023 Christian Lins <christian.lins@haw-hamburg.de>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.RandomMutation;

import java.util.function.Function;

/**
 * Test bench for the Hill Climbing algorithm.
 *
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class TestHillClimbing {
    public static void main(String[] args) {
        run2DSphereFuncWithStats(20);
        //runAckleyFuncWithStats(5);
    }

    private static void runAckleyFuncWithStats(int numOfRuns) {
        float[] min = {-40.0f, -40.0f};
        float[] max = {+40.0f, +40.0f};

        // Ackley Function n = 2
        Function<Individual, Float> evalAckleyFunc =
                (ind) -> {
                    float x = ind.getGenome().array()[0];
                    float y = ind.getGenome().array()[1];
                    return ackleyFunction(x, y);
                };

        var comparator = new MinimizeFunctionComparator(evalAckleyFunc);

        for (int i = 0; i < numOfRuns; i++) {
            var algo = new HillClimbingAlgorithm(min, max,
                    comparator, new RandomMutation(min, max), new ComparatorIndividual(0.0000001f));
            algo.run();
        }
    }

    // 0,0000000000,8998596
    // 0,0000000000,38544517
    // 0,0000000000,121254444
    // 0,0000000000,135786846
    // 0,0000000000,29403764
    // 0,0000000000,26719353
    // 0,0000000000,40686103
    // 0,0000000000,5911209
    // 0,0000000000,10750037
    // 0,0000000000,30447584
    // 0,0000000000,85864273
    // 0,0000000000,18515709
    // 0,0000000000,6034080
    // 0,0000000000,86339180
    // 0,0000000000,30551176
    // 0,0000000000,25174340
    // 0,0000000000,35266371
    // 0,0000000000,24649732
    // 0,0000000000,38821108
    // 0,0000000000,41081462

    private static float ackleyFunction(double x, double y) {
        double firstTerm = -20.0 * Math.exp(-0.2 * Math.sqrt(0.5 * (x * x + y * y)));
        double secondTerm = -Math.exp(0.5 * (Math.cos(2.0 * Math.PI * x) + Math.cos(2.0 * Math.PI * y)));
        double thirdTerm = Math.E + 20.0;
        return (float) (firstTerm + secondTerm + thirdTerm);
    }

    private static void run2DSphereFuncWithStats(int numOfRuns) {
        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};

        // Sphere Function n = 2
        Function<Individual, Float> evalSphereFunc2D =
                (ind) -> {
                    float x0 = ind.getGenome().array()[0];
                    float x1 = ind.getGenome().array()[1];
                    return x0 * x0 + x1 * x1;
                };

        var comparator = new MinimizeFunctionComparator(evalSphereFunc2D);

        for (int i = 0; i < numOfRuns; i++) {
            var algo = new HillClimbingAlgorithm(min, max,
                    comparator, new RandomMutation(min, max), new ComparatorIndividual(0.00001f));
            algo.run();
        }
    }

    // 0,0000056309,813
    // 0,0000088868,2525
    // 0,0000064093,2191
    // 0,0000087424,3272
    // 0,0000081308,5418
    // 0,0000073593,12403
    // 0,0000069606,2849
    // 0,0000028132,2149
    // 0,0000080395,836
    // 0,0000002472,4128
    // 0,0000007859,4740
    // 0,0000072877,798
    // 0,0000063392,6910
    // 0,0000029988,1212
    // 0,0000075014,2138
    // 0,0000037267,2846
    // 0,0000074448,3066
    // 0,0000080912,12398
    // 0,0000075933,4809
    // 0,0000081158,7598
}
