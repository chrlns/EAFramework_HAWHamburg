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
}
