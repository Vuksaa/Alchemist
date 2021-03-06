package it.unibo.alchemist.model.implementations.actions

import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.Context
import it.unibo.alchemist.model.interfaces.Molecule
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.VisibleNode
import it.unibo.alchemist.model.interfaces.environments.Physics2DEnvironment
import kotlin.math.min

/**
 * Extracts the closest [VisibleNode] to the position at the given distance from the given node's position
 * in the direction of the node's heading.
 * @constructor
 * [visionMolecule] is expected to contain a list of [VisibleNode].
 * If there are no visible nodes then [targetMolecule] will be removed from [node],
 * otherwise the VisibleNode's position will be inserted in [targetMolecule].
 */
class ExtractClosestVisibleNodeAtDistance(
    node: Node<Any>,
    private val env: Physics2DEnvironment<Any>,
    private val distance: Double,
    private val visionMolecule: Molecule,
    private val targetMolecule: Molecule
) : AbstractAction<Any>(node) {
    override fun cloneAction(node: Node<Any>, reaction: Reaction<Any>) =
        ExtractClosestVisibleNodeAtDistance(node, env, distance, visionMolecule, targetMolecule)

    override fun execute() {
        if (node.contains(visionMolecule)) {
            val visibleNodes = node.getConcentration(visionMolecule)
            require(visibleNodes is List<*>) { "visionMolecule contains ${visibleNodes::class} instead of a List" }
            if (visibleNodes.isEmpty()) {
                if (node.contains(targetMolecule)) node.removeConcentration(targetMolecule)
            } else {
                val aNode = visibleNodes.first()
                require(aNode is VisibleNode<*, *>) {
                    "visionMolecule contains List<${aNode?.javaClass}> instead of a List<VisibleNode>"
                }
                require(aNode.position is Euclidean2DPosition) {
                    "The VisibleNode contained in visionMolecule is from a different environment"
                }
                @Suppress("UNCHECKED_CAST") val nodes = visibleNodes as List<VisibleNode<*, Euclidean2DPosition>>
                val myPosition = env.getPosition(node).surroundingPointAt(
                    versor = env.getHeading(node),
                    distance = distance
                )
                nodes.map { it.position }
                    .reduce { n1, n2 -> minBy(n1, n2) { it.distanceTo(myPosition) } }
                    .also { node.setConcentration(targetMolecule, it) }
            }
        }
    }

    override fun getContext() = Context.LOCAL

    private fun <T> minBy(a: T, b: T, mapper: (T) -> Double) = with(mapper(a)) {
        if (min(this, mapper(b)) < this) return b else a
    }
}
