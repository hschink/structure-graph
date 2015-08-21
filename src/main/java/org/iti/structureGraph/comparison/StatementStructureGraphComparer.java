package org.iti.structureGraph.comparison;

import org.iti.structureGraph.IStructureGraph;
import org.iti.structureGraph.comparison.result.IStructureModification;
import org.iti.structureGraph.comparison.result.StructureGraphComparisonResult;
import org.iti.structureGraph.comparison.result.StructurePathModification;
import org.iti.structureGraph.comparison.result.Type;
import org.iti.structureGraph.nodes.IStructureElement;

public class StatementStructureGraphComparer implements IStructureGraphComparer {

	@Override
	public StructureGraphComparisonResult compare(IStructureGraph statement, IStructureGraph structure)
			throws StructureGraphComparisonException {
		IStructureGraphComparer comparer = new SimpleStructureGraphComparer();
		StructureGraphComparisonResult result = comparer.compare(statement, structure);

		removeNonMandatoryNodeAdditions(result);

		removeOptionalListNodeAdditions(result);

		return result;
	}

	private void removeNonMandatoryNodeAdditions(StructureGraphComparisonResult result) {
		for (IStructureElement element : result.getElementsByModification(Type.NodeAdded)) {
			if (!(element.isOptionalList() || (element.isMandatory() && parentExists(result, element)))) {
				String fullIdentifier = result.getNewGraph().getIdentifier(element);
				String path = result.getNewGraph().getPath(element);

				result.removeModification(fullIdentifier);
				result.removeModification(path);

				for (IStructureModification modification : result.getPathModifications().values()) {
					if (modification.getType() == Type.PathAdded
							&& elementIsPartOfPath((StructurePathModification) modification, element)) {
						result.removeModification(modification.getIdentifier());
					}
				}
			}
		}
	}

	private void removeOptionalListNodeAdditions(StructureGraphComparisonResult result) {
		for (IStructureElement element : result.getElementsByModification(Type.NodeAdded)) {
			if (element.isOptionalList()) {
				String optionalListElementPath = result.getNewGraph().getPath(element);
				String fullIdentifier = result.getNewGraph().getIdentifier(element);

				result.removeModification(fullIdentifier);

				for (IStructureElement optionalListElement : result.getOldGraph()
						.getStructureElements(optionalListElementPath)) {
					fullIdentifier = result.getOldGraph().getIdentifier(optionalListElement);

					result.removeModification(fullIdentifier);
				}
			}
		}
	}

	private boolean parentExists(StructureGraphComparisonResult result, IStructureElement element) {
		IStructureElement parent = result.getNewGraph().getParent(element);
		String fullIdentifierParent = result.getNewGraph().getIdentifier(parent);
		IStructureElement parentInOldGraph = result.getOldGraph().getStructureElement(fullIdentifierParent);

		return parentInOldGraph != null && result.getOldGraph()
				.getStructureElement(result.getOldGraph().getIdentifier(parentInOldGraph)) != null;
	}

	private boolean elementIsPartOfPath(StructurePathModification modification, IStructureElement element) {
		return modification.getSourceElement().equals(element) || modification.getTargetElement().equals(element);
	}
}
