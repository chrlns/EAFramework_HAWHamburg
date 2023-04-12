/*
 * This file is part of a Körber Pharma Software GmbH project.
 *
 * Copyright (c)
 *    Körber Pharma Software GmbH
 *    All rights reserved.
 *
 * This source file may be managed in different Java package structures,
 * depending on actual usage of the source file by the Copyright holders:
 *
 * for Werum:  com.werum.* or any other Körber Pharma Software owned Internet domain
 *
 * Any use of this file as part of a software system by none Copyright holders
 * is subject to license terms.
 *
 */
package de.heaal.eaf.crossover;

import de.heaal.eaf.base.Individual;

import java.util.Random;

/**
 * TODO Description of class...
 *
 * @author gerrik_heitmann
 * @company Körber Pharma Software GmbH
 * @created 12.04.2023
 * @since TODO
 */
public class AverageCombination<T extends Individual> implements Combination<T> {
    private Random rng;

    public AverageCombination() {
        this.rng = new Random();
    }

    @Override
    public void setRandom(Random rng) {
        this.rng = rng;
    }

    @Override
    public T combine(Individual[] parents) {
        if (parents.length != 2) {
            throw new RuntimeException("parents.length has to be == 2!");
        }

        Individual motherInd = parents[0];
        Individual fatherInd = parents[1];

        float[] motherGenomeArr = motherInd.getGenome().array();
        float[] fatherGenomeArr = fatherInd.getGenome().array();
        int arrSize = motherGenomeArr.length;

        T child = (T)parents[0].copy();

        for (int i = 0; i < arrSize; i++) {
            float avgOfTwo = (motherGenomeArr[i] + fatherGenomeArr[i]) / 2;
            child.getGenome().array()[i] = avgOfTwo;
        }

        return child;
    }
}
