/*
 *  Copyright 1999 Hagen Schink <hagen.schink@gmail.com>
 *
 *  This file is part of sql-schema-comparer.
 *
 *  sql-schema-comparer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  sql-schema-comparer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with sql-schema-comparer.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.iti.structureGraph.comparison;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iti.structureGraph.IStructureGraph;
import org.iti.structureGraph.comparison.result.IModificationDetail;
import org.iti.structureGraph.comparison.result.IStructureModification;
import org.iti.structureGraph.comparison.result.OriginalStructureElement;
import org.iti.structureGraph.comparison.result.StructureElementModification;
import org.iti.structureGraph.comparison.result.StructureGraphComparisonResult;
import org.iti.structureGraph.comparison.result.StructurePathModification;
import org.iti.structureGraph.comparison.result.Type;
import org.iti.structureGraph.nodes.IStructureElement;
import org.jgrapht.graph.DefaultEdge;

public class StructureGraphComparer implements IStructureGraphComparer {

	public static interface IAmbiguousException {
		StructureGraphComparisonResult getPreviousResult();
	}

	public static class AmbiguousRenameException extends StructureGraphComparisonException
			implements IAmbiguousException {
		private static final long serialVersionUID = -3176377321899125075L;

		private StructureGraphComparisonResult previousResult;

		public AmbiguousRenameException(StructureGraphComparisonResult previousResult) {
			this.previousResult = previousResult;
		}

		@Override
		public StructureGraphComparisonResult getPreviousResult() {
			return previousResult;
		}
	}

	public static class AmbiguousMoveException extends StructureGraphComparisonException
			implements IAmbiguousException {
		private static final long serialVersionUID = -3666070878704536627L;

		private StructureGraphComparisonResult previousResult;

		public AmbiguousMoveException(StructureGraphComparisonResult previousResult) {
			this.previousResult = previousResult;
		}

		@Override
		public StructureGraphComparisonResult getPreviousResult() {
			return previousResult;
		}

	}

	private StructureGraphComparisonResult result;

	private IStructureGraph oldGraph;
	private IStructureGraph newGraph;

	private Map<String, List<IStructureElement>> removedNodesByPath;
	private Map<String, List<IStructureElement>> addedNodesByPath;

	@Override
	public StructureGraphComparisonResult compare(IStructureGraph oldGraph, IStructureGraph newGraph)
			throws StructureGraphComparisonException {
		SimpleStructureGraphComparer simpleStructureGraphComparer = new SimpleStructureGraphComparer();

		result = simpleStructureGraphComparer.compare(oldGraph, newGraph);

		this.oldGraph = oldGraph;
		this.newGraph = newGraph;

		groupAddedAndRemovedNodesByPath();

		setRenamedNodes();
		setMovedNodes();
		setRenamedPathes();

		return result;
	}

	private void groupAddedAndRemovedNodesByPath() {
		Collection<IStructureElement> removedNodes = result.getElementsByModification(Type.NodeDeleted);
		Collection<IStructureElement> addedNodes = result.getElementsByModification(Type.NodeAdded);

		removedNodesByPath = getNodesByPath(oldGraph, removedNodes);
		addedNodesByPath = getNodesByPath(newGraph, addedNodes);
	}

	private static Map<String, List<IStructureElement>> getNodesByPath(IStructureGraph graph,
			Collection<IStructureElement> elements) {
		Map<String, List<IStructureElement>> missingNodesByPath = new HashMap<>();

		for (IStructureElement element : elements) {
			String path = graph.getPath(element);

			if (!missingNodesByPath.containsKey(path)) {
				missingNodesByPath.put(path, new ArrayList<IStructureElement>());
			}

			missingNodesByPath.get(path).add(element);
		}

		return missingNodesByPath;
	}

	private void setRenamedNodes() throws AmbiguousRenameException {
		for (Entry<String, List<IStructureElement>> removedInPath : removedNodesByPath.entrySet()) {
			for (IStructureElement removedElement : removedInPath.getValue()) {
				IStructureElement renamedElement = findRenamedElement(removedInPath.getKey(), removedElement);

				exchangeNode(removedElement, renamedElement, Type.NodeRenamed);
			}
		}
	}

	private IStructureElement findRenamedElement(String path, IStructureElement removedElement)
			throws AmbiguousRenameException {
		List<IStructureElement> addedElementsInPath = addedNodesByPath.get(path);

		if (addedElementsInPath != null) {
			switch (addedElementsInPath.size()) {
			case 0:
				return null;
			case 1:
				IStructureElement addedElement = addedElementsInPath.get(0);

				return addedElement;
			default:
				throw new AmbiguousRenameException(result);
			}
		}

		return null;
	}

	private void exchangeNode(IStructureElement removedElement, IStructureElement addedElement, Type type) {
		if (addedElement != null) {
			String fullIdentifier = result.getNewGraph().getIdentifier(addedElement);
			IModificationDetail detail = getModificationDetail(result.getOldGraph(), removedElement);
			StructureElementModification modification = getModification(result.getNewGraph(), addedElement, type,
					detail);

			result.removeModification(oldGraph.getIdentifier(removedElement));
			result.removeModification(newGraph.getIdentifier(addedElement));

			result.addModification(fullIdentifier, modification);
		}
	}

	private IModificationDetail getModificationDetail(IStructureGraph graph, IStructureElement element) {
		String fullIdentifier = graph.getIdentifier(element);

		return new OriginalStructureElement(fullIdentifier);
	}

	private StructureElementModification getModification(IStructureGraph graph, IStructureElement element, Type type,
			IModificationDetail detail) {
		String path = graph.getPath(element);

		return new StructureElementModification(path, element.getName(), type, detail);
	}

	private void setMovedNodes() throws AmbiguousMoveException {
		List<String> paths = new ArrayList<>(removedNodesByPath.keySet());

		paths.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (String path : paths) {
			List<IStructureElement> removedInPath = removedNodesByPath.get(path);

			for (IStructureElement removedElement : removedInPath) {
				IStructureElement movedElement = findMovedElement(removedElement);

				exchangeNode(removedElement, movedElement, Type.NodeMoved);
			}
		}
	}

	private IStructureElement findMovedElement(IStructureElement element) throws AmbiguousMoveException {
		Collection<IStructureElement> addedElements = result.getElementsByName(element.getName(), Type.NodeAdded);
		IStructureElement oldParent = oldGraph.getParent(element);

		if (oldParent != null) {
			IStructureElement movedParent = getMovedParent(oldParent);

			for (IStructureElement addedElement : addedElements) {
				if (newGraph.getParent(addedElement).equals(movedParent)) {
					return addedElement;
				}
			}
		}

		switch (addedElements.size()) {
		case 0:
			return null;

		case 1:
			IStructureElement addedElement = addedElements.iterator().next();

			return addedElement;

		default:
			throw new AmbiguousMoveException(result);
		}
	}

	private IStructureElement getMovedParent(IStructureElement oldParent) {
		String parentIdentifier = oldGraph.getIdentifier(oldParent);

		for (Entry<String, IStructureModification> m : result.getNodeModifications().entrySet()) {
			if ((m.getValue().getType().equals(Type.NodeMoved) || m.getValue().getType().equals(Type.NodeRenamed))
					&& m.getValue().getModificationDetail().getIdentifier().equals(parentIdentifier)) {
				return newGraph.getStructureElement(m.getKey());
			}
		}

		return null;
	}

	private void setRenamedPathes() {
		for (IStructureElement element : result.getElementsByModification(Type.NodeMoved)) {
			String newPath = getPath(element, result.getNewGraph());
			String oldPath = getPath(getMovedElement(element), result.getOldGraph());

			exchangePaths(oldPath, newPath, Type.PathRenamed);
		}
	}

	private IStructureElement getMovedElement(IStructureElement element) {
		String identifier = result.getNewGraph().getIdentifier(element);
		IStructureModification modification = result.getModifications().get(identifier);
		IModificationDetail detail = modification.getModificationDetail();

		return result.getOldGraph().getStructureElement(detail.getIdentifier());
	}

	private String getPath(IStructureElement element, IStructureGraph graph) {
		return graph.getPath(element, true);
	}

	private void exchangePaths(String removedPath, String addedPath, Type type) {
		if (addedPath != null) {
			DefaultEdge edge = newGraph.getEdge(addedPath);
			IStructureElement source = newGraph.getSourceElement(edge);
			IStructureElement target = newGraph.getSourceElement(edge);
			IModificationDetail detail = new OriginalStructureElement(removedPath);
			IStructureModification modification = new StructurePathModification(addedPath, edge, source, target, type,
					detail);

			result.removeModification(addedPath);
			result.removeModification(removedPath);

			result.addModification(addedPath, modification);
		}
	}
}