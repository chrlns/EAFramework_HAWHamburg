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

package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.mutation.RandomMutation;

import java.util.Comparator;

/**
 * Implementation of the Hill Climbing algorithm.
 *
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class HillClimbingAlgorithm extends Algorithm {

    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;

    public HillClimbingAlgorithm(float[] min, float[] max,
                                 Comparator<Individual> comparator, Mutation mutator,
                                 ComparatorIndividual terminationCriterion) {
        super(comparator, mutator);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
    }

    @Override
    public void nextGeneration() {
        super.nextGeneration();

        // Sanity check
        if (population.size() != 1) {
            throw new RuntimeException("Something went very wrong...");
        }

        MutationOptions mutationOptions = new MutationOptions();
        mutationOptions.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 1.0f);
        // Not needed. By default the method chooses a random index
        // mutationOptions.put(MutationOptions.KEYS.FEATURE_INDEX, ...);

        // x0
        Individual x0 = population.get(0);
        // x1 <- x0
        Individual x1 = x0.copy();
        // q <- randomly chosen solution feature index E [0, n]
        // Replace the q-th solution feature of x1 with a random mutation
        mutator.mutate(x1, mutationOptions);

        // If f(x1) > f(x0) then
        //     x0 <- x1
        // End if
        if (comparator.compare(x1, x0) > 0) {
            population.set(0, x1);
        }
    }

    @Override
    public boolean isTerminationCondition() {
        // Because we only have a population of 1 individual we know that
        // this individual is our current best.
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public void run() {
        // x0 <- randomly generated individual
        initialize(indFac, 1);

        int numOfGens = 0;

        // While not(termination criterion)
        // Compute the fitness f(x0) of x0
        // while (!isTerminationCondition()) {
        while (numOfGens < 1000) {
            numOfGens++;
            nextGeneration();
            System.out.println(population.get(0).getCache());
        }

        // System.out.printf("\n%.10f,%d", population.get(0).getCache(), numOfGens);
    }

}
