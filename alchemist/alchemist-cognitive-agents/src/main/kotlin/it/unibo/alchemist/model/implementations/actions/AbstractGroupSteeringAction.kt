/*
 * Copyright (C) 2010-2020, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.implementations.actions

import it.unibo.alchemist.model.implementations.positions.AbstractEuclideanPosition
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.GroupSteeringAction
import it.unibo.alchemist.model.interfaces.Pedestrian
import it.unibo.alchemist.model.interfaces.Reaction

/**
 * An abstract [GroupSteeringAction].
 */
abstract class AbstractGroupSteeringAction<T, P : AbstractEuclideanPosition<P>>(
    /**
     * The environment in which this action executes.
     */
    protected open val env: Environment<T, P>,
    reaction: Reaction<T>,
    pedestrian: Pedestrian<T>
) : GroupSteeringAction<T, P>, SteeringActionImpl<T, P>(env, reaction, pedestrian) {

    /**
     * Computes the centroid of the [group] in ABSOLUTE coordinates.
     */
    protected fun centroid(): P = with(group()) {
        map { env.getPosition(it) }.reduce { acc, pos -> acc + pos } / size.toDouble()
    }
}
