package it.unibo.alchemist.boundary.monitor;

import it.unibo.alchemist.boundary.monitor.generic.NumericLabelMonitor;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Position;
import it.unibo.alchemist.model.interfaces.Reaction;
import it.unibo.alchemist.model.interfaces.Time;

/**
 * {@code OutputMonitor} that monitors the current {@link it.unibo.alchemist.core.interfaces.Simulation#getStep() steps} of the {@code Simulation}.
 *
 * @param <T> The type which describes the {@link it.unibo.alchemist.model.interfaces.Concentration} of a molecule
 * @param <P> The position type
 */
public class FXStepMonitor<T, P extends Position<? extends P>> extends NumericLabelMonitor<Long, T, P> {
    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FXStepMonitor() {
        super(0L, "Step: ");
    }

    /**
     * @inheritDocs
     */
    @Override
    public void finished(final Environment<T, P> environment, final Time time, final long step) {
        update(step);
    }

    /**
     * @inheritDocs
     */
    @Override
    public void stepDone(final Environment<T, P> environment, final Reaction<T> reaction, final Time time, final long step) {
        update(step);
    }
}