package it.unibo.alchemist.boundary.gui.effects;

import it.unibo.alchemist.model.implementations.actions.See;
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule;
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Node;
import it.unibo.alchemist.model.interfaces.Position;
import it.unibo.alchemist.model.interfaces.environments.EuclideanPhysics2DEnvironment;
import it.unibo.alchemist.model.interfaces.geometry.AwtShapeCompatible;
import it.unibo.alchemist.model.interfaces.geometry.GeometricShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

/**
 * Draws node's shapes.
 *
 * @param <T> concentration type
 * @param <P> position type
 */
public final class DrawSmartcam<T, P extends Position<P>> implements AdvancedEffect<T, P> {
    private static final long serialVersionUID = 1L;

    @Override
    public void apply(final Graphics2D g, final Node<T> node, final Environment<T, P> environment, final double zoom, final int x, final int y) {
        if (!(environment instanceof EuclideanPhysics2DEnvironment)) {
            return;
        }
        @SuppressWarnings("unchecked")
        final EuclideanPhysics2DEnvironment<T> env = (EuclideanPhysics2DEnvironment) environment;
        drawShape(g, node, env, zoom, x, y);
        drawFieldOfView(g, node, env, zoom, x, y);
    }

    @Override
    public Color getColorSummary() {
        return Color.GREEN;
    }

    private void drawShape(final Graphics2D g, final Node<T> node, final EuclideanPhysics2DEnvironment<T> env, final double zoom, final int x, final int y) {
        final GeometricShape geometricShape = node.getShape();
        if (!(geometricShape instanceof AwtShapeCompatible)) {
            return;
        }
        final AffineTransform transform = getTransform(x, y, zoom, getRotation(node, env));
        final Shape shape = transform.createTransformedShape(((AwtShapeCompatible) geometricShape).asAwtShape());
        if (node.contains(new SimpleMolecule("wanted"))) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        g.draw(shape);
    }

    private void drawFieldOfView(final Graphics2D g, final Node<T> node, final EuclideanPhysics2DEnvironment<T> env, final double zoom, final int x, final int y) {
        final AffineTransform transform = getTransform(x, y, zoom, getRotation(node, env));
        g.setColor(Color.BLUE);
        node.getReactions()
            .stream()
            .flatMap(r -> r.getActions().stream())
            .filter(a -> a instanceof See)
            .map(a -> (See) a)
            .forEach(a -> {
                final double angle = a.getAngle();
                final double startAngle = -angle / 2;
                final double d = a.getDistance();
                final Shape fov = new Arc2D.Double(-d, -d, d * 2, d * 2, startAngle, angle, Arc2D.PIE);
                g.draw(transform.createTransformedShape(fov));
            });
    }

    private double getRotation(final Node<T> node, final EuclideanPhysics2DEnvironment<T> env) {
        final Euclidean2DPosition direction = env.getHeading(node);
        return Math.atan2(direction.getY(), direction.getX());
    }

    private AffineTransform getTransform(final int x, final int y, final double zoom, final double rotation) {
        final AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(zoom, zoom);
        transform.rotate(-rotation); // invert angle because the y axis is inverted in the gui
        return transform;
    }
}
