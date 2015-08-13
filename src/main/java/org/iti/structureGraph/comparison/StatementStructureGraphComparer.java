package org.iti.structureGraph.comparison;

import org.iti.structureGraph.IStructureGraph;
import org.iti.structureGraph.comparison.result.StructureElementModification;
import org.iti.structureGraph.comparison.result.StructureGraphComparisonResult;
import org.iti.structureGraph.comparison.result.Type;
import org.iti.structureGraph.nodes.IStructureElement;
import org.jgrapht.graph.DefaultEdge;

public class StatementStructureGraphComparer implements IStructureGraphComparer {

	@Override
	public StructureGraphComparisonResult compare(IStructureGraph statement, IStructureGraph structure)
			throws StructureGraphComparisonException {
		IStructureGraphComparer comparer = new SimpleStructureGraphComparer();
		StructureGraphComparisonResult result = comparer.compare(statement, structure);

		addMissingMandatoryNodesToResult(result);

		return result;
	}

	private void addMissingMandatoryNodesToResult(StructureGraphComparisonResult result) {
		for (IStructureElement element : result.getElementsByModification(Type.NodeAdded)) {
			if (element.isMandatory() && parentExists(result, element)) {
				String fullIdentifier = result.getNewGraph().getIdentifier(element);
				String path = result.getNewGraph().getPath(element);
				StructureElementModification modification = new StructureElementModification(path, element.getIdentifier(), Type.NodeDeleted);

				result.removeModification(fullIdentifier);
				result.addModification(fullIdentifier, modification);
			}
		}
	}

	private boolean parentExists(StructureGraphComparisonResult result, IStructureElement element) {
		DefaultEdge edge = result.getNewGraph().getEdge(result.getNewGraph().getPath(element, false));
		IStructureElement parent = result.getNewGraph().getSourceElement(edge);

		return parent != null && result.getOldGraph().containsElementWithPath(result.getNewGraph().getIdentifier(parent));
	}

}
