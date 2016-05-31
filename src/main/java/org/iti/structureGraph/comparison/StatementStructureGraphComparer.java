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
		return compare(statement, structure, false);
	}

	public StructureGraphComparisonResult compare(IStructureGraph statement, IStructureGraph structure,
			boolean ignoreMandatoryNodes) throws StructureGraphComparisonException {
		IStructureGraphComparer comparer = new SimpleStructureGraphComparer();
		StructureGraphComparisonResult result = comparer.compare(statement, structure);

		removeNonMandatoryNodeAdditions(result, ignoreMandatoryNodes);

		removeOptionalListNodeAdditions(result);

		return result;
	}

	private void removeNonMandatoryNodeAdditions(StructureGraphComparisonResult result, boolean ignoreMandatoryNodes) {
		boolean detectMandatoryNodes = !ignoreMandatoryNodes;

		for (IStructureElement element : result.getElementsByModification(Type.NodeAdded)) {
			if (element.isOptionalList() || (detectMandatoryNodes && isNodeRequiredByStatement(result, element))) {
				continue;
			} else {
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

	private boolean isNodeRequiredByStatement(StructureGraphComparisonResult result, IStructureElement element) {
		return element.isMandatory() && parentExistsInStatementGraph(result, element);
	}

	private void removeOptionalListNodeAdditions(StructureGraphComparisonResult result) {
		for (IStructureElement element : result.getElementsByModification(Type.NodeAdded)) {
			if (element.isOptionalList()) {
				String optionalListElementsPath = result.getNewGraph().getPath(element);
				String fullIdentifier = result.getNewGraph().getIdentifier(element);

				result.removeModification(fullIdentifier);

				for (IStructureElement optionalListElement : result.getOldGraph()
						.getStructureElements(optionalListElementsPath)) {
					String elementIdentifier = result.getOldGraph().getIdentifier(optionalListElement);
					IStructureElement elementInStructure = result.getNewGraph().getStructureElement(elementIdentifier);

					if (elementInStructure == null || !elementInStructure.isMandatory()) {
						fullIdentifier = result.getOldGraph().getIdentifier(optionalListElement);

						result.removeModification(fullIdentifier);
					}
				}
			}
		}
	}

	private boolean parentExistsInStatementGraph(StructureGraphComparisonResult result, IStructureElement element) {
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
