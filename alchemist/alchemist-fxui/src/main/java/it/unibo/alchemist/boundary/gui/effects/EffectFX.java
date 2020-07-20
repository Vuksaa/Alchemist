package it.unibo.alchemist.boundary.gui.effects;

import it.unibo.alchemist.boundary.interfaces.DrawCommand;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Position2D;
import java.io.Serializable;
import java.util.Queue;

/**
 * Graphical visualization of something happening in the environment.
 *
 * @param <P> the position type
 */
public interface EffectFX<P extends Position2D<? extends P>> extends Serializable {

    /**
     * Computes a queue of commands to Draw something.
     *
     * @param environment the environment to gather data from
     * @param <T>         the {@link it.unibo.alchemist.model.interfaces.Concentration} type
     * @return the queue of commands that should be run to draw the effect
     */
    <T> Queue<DrawCommand<P>> computeDrawCommands(Environment<T, P> environment);

    /**
     * Gets the name of the effect.
     *
     * @return the name of the effect
     */
    String getName();

    /**
     * Sets the name of the effect.
     *
     * @param name the name of the effect to set
     */
    void setName(String name);

    /**
     * Gets the visibility of the effect.
     *
     * @return the visibility of the effect
     */
    boolean isVisible();

    /**
     * Sets the visibility of the effect.
     *
     * @param visibility the visibility of the effect to set
     */
    void setVisibility(boolean visibility);
}