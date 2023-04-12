package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.GenericIndividualFactory;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.IndividualFactory;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.mutation.MutationOptions.KEYS;
import de.heaal.eaf.mutation.MutationScope;
import de.heaal.eaf.mutation.RandomMutation;
import de.heaal.eaf.selection.SelectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenAlg extends Algorithm {

    private final float[] min;
    private final float[] max;
    private final Combination combination;
    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;
    private final int populationSize;
    private final float mutationProp;
    private final int elitistSize;
    private final MutationScope mutationScope;

    public GenAlg(float[] min, float[] max,
                  Comparator<Individual> comparator,
                  Mutation mutator,
                  Combination combination,
                  ComparatorIndividual terminationCriterion,
                  float mutationProp,
                  int populationSize,
                  int elitistSize,
                  MutationScope mutationScope) {
        super(comparator, mutator);

        this.min = min;
        this.max = max;
        this.indFac = new GenericIndividualFactory(min, max);
        this.combination = combination;
        this.terminationCriterion = terminationCriterion;
        this.populationSize = populationSize;
        this.mutationProp = mutationProp;
        this.elitistSize = elitistSize;
        this.mutationScope = mutationScope;
    }

    @Override
    protected boolean isTerminationCondition() {
        population.sort(comparator);
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public void run() {
        initialize(indFac, populationSize);

        int numOfGenerations = 0;

        while (numOfGenerations < 1000) {
            numOfGenerations++;
            nextGeneration();
            System.out.println(population.get(0).getCache());
        }

    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();

        // Sanity check
        if (population.size() != populationSize) {
            throw new RuntimeException("Something went very wrong...");
        }

        population.sort(comparator);

        elitists.clear();
        selectionists.clear();
        childs.clear();

        selectElitists(); // Nehme die TOP N und übernehme sie unverändert in die nächste Gen.
        selection(); // Nehme die besten eltern
        recombination(); // Paare die Eltern und erzeuge Kinder
        mutation(); // Mutiere Eltern und Kinder, aber NICHT Elitisten

        List<Individual> newPopulation = new ArrayList<>(populationSize);
        newPopulation.addAll(elitists);
        newPopulation.addAll(selectionists);
        newPopulation.addAll(childs);

        population.setIndividuals(newPopulation);
    }

    private List<Individual> elitists = new ArrayList<>();
    private List<Individual> selectionists = new ArrayList<>();
    private List<Individual> childs = new ArrayList<>();

    private void selectElitists() {
        if (elitistSize == 0) {
            return;
        }

        elitists = population.asList().subList(0, elitistSize);
        population.removeFirstN(elitistSize); // TODO Maybe wrong behavior
    }

    private void selection() {
        int selectionSize = (populationSize / 2) - elitistSize;

        for (int i = 0; i < selectionSize; i++) {
            selectionists.add(SelectionUtils.selectNormal(population, rng, selectionists));
        }
    }

    private void recombination() {
        int numOfChilds = populationSize / 2;

        int selectionistSize = selectionists.size();
        for (int i = 0; i < numOfChilds; i++) {

            Individual[] parents = new Individual[2];
            parents[0] = selectionists.get(rng.nextInt(selectionistSize));
            parents[1] = selectionists.get(rng.nextInt(selectionistSize));

            childs.add(combination.combine(parents));
        }
    }

    private void mutation() {
        List<Individual> individualsToMutate = new ArrayList<>();
        switch (mutationScope) {
            case CHILDS -> {
                individualsToMutate.addAll(childs);
                break;
            }
            case CHILDS_PARENTS -> {
                individualsToMutate.addAll(childs);
                individualsToMutate.addAll(selectionists);
                break;
            }
            case PARENTS -> {
                individualsToMutate.addAll(selectionists);
                break;
            }
            default -> throw new RuntimeException("What ze fök");
        }

        MutationOptions mutOptMutProp = new MutationOptions();
        mutOptMutProp.put(KEYS.MUTATION_PROBABILITY, mutationProp);

        for (Individual ind : individualsToMutate) {
            mutator.mutate(ind, mutOptMutProp);
        }
    }
}
