package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.GenericIndividualFactory;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.IndividualFactory;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.selection.SelectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenAlg extends Algorithm {

    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;
    private final int populationSize;
    private final float mutationProp;

    private Object lastBestIndividual;

    private List<Individual> elitists;

    public GenAlg(float[] min, float[] max,
                  Comparator<Individual> comparator,
                  Mutation mutator,
                  ComparatorIndividual terminationCriterion,
                  float mutationProp,
                  int populationSize
                  ) {
        super(comparator, mutator);

        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.populationSize = populationSize;
        this.mutationProp = mutationProp;
    }

    @Override
    protected boolean isTerminationCondition() {
        return comparator.compare(lastBestIndividual, terminationCriterion) > 0;
    }

    @Override
    public void run() {
        initialize(indFac, populationSize);

        while (!isTerminationCondition()) {
            nextGeneration();
        }
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();

        // Sanity check
        if (population.size() != 1) {
            throw new RuntimeException("Something went very wrong...");
        }

        population.sort(comparator);

        selectElitists(); // Nehme die TOP N und übernehme sie unverändert in die nächste Gen.
        selection(); // Nehme die besten eltern
        recombination(); // Paare die Eltern und erzeuge Kinder
        mutation(); // Mutiere Eltern und Kinder, aber NICHT Elitisten
    }

    private void selectElitists() {
        elitists = population.asList().subList(0, 4); // TODO As parameter
    }

    private List<Individual> selectionists; // TODO change location

    private void selection() {
        int selectionSize = populationSize / 2 - 5; // TODO 5 is depending on selectionists

        selectionists = new ArrayList<>(selectionSize);

        for (int i = 0; i < selectionSize; i++) {
            SelectionUtils.selectNormal(population, rng, selectionists); // TODO Also consider to avoid elitists
        }
    }

    private void recombination() {

    }

    private void mutation() {

    }
}
