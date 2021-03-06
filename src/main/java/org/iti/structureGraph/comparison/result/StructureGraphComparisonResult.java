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

package org.iti.structureGraph.comparison.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iti.structureGraph.IStructureGraph;
import org.iti.structureGraph.nodes.IStructureElement;

public class StructureGraphComparisonResult {

	private IStructureGraph oldGraph;

	public IStructureGraph getOldGraph() {
		return oldGraph;
	}

	private IStructureGraph newGraph;

	public IStructureGraph getNewGraph() {
		return newGraph;
	}

	private Map<String, IStructureModification> modifications = new HashMap<>();

	public Map<String, IStructureModification> getModifications() {
		return modifications;
	}

	public Map<String, IStructureModification> getNodeModifications() {
		return getModifications(StructureElementModification.class);
	}

	public Map<String, IStructureModification> getPathModifications() {
		return getModifications(StructurePathModification.class);
	}

	private Map<String, IStructureModification> getModifications(
			Class<?> class1) {
		Map<String, IStructureModification> nodeModifications = new HashMap<>();

		for (Entry<String, IStructureModification> e : modifications.entrySet()) {
			if (class1.isInstance(e.getValue())) {
				nodeModifications.put(e.getKey(), e.getValue());
			}
		}

		return nodeModifications;
	}

	public void addModification(String path, IStructureModification modification) {
		modifications.put(path, modification);
	}

	public void removeModification(String fullIdentifier) {
		modifications.remove(fullIdentifier);
	}

	public StructureGraphComparisonResult(IStructureGraph oldGraph,
			IStructureGraph newGraph) {
		this.oldGraph = oldGraph;
		this.newGraph = newGraph;
	}

	public Collection<IStructureElement> getElementsByModification(Type type) {
		List<IStructureElement> elements = new ArrayList<>();

		for (Entry<String, IStructureModification> m : modifications.entrySet()) {
			if (m.getValue().getType() == type) {
				elements.add(getElementByName(m.getKey()));
			}
		}

		return elements;
	}

	private IStructureElement getElementByName(String name) {
		IStructureElement element = oldGraph.getStructureElement(name);

		return (element != null) ? element : newGraph.getStructureElement(name);
	}

	public Collection<IStructureElement> getElementsByName(String name,
			Type type) {
		List<IStructureElement> elements = new ArrayList<>();

		for (Entry<String, IStructureModification> m : modifications.entrySet()) {
			IStructureElement element = getElementByName(m.getKey());

			if (m.getValue().getType() == type && element.getName().equals(name)) {
				elements.add(getElementByName(m.getKey()));
			}
		}

		return elements;
	}
}