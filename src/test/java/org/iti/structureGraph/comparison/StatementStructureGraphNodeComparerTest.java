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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.iti.structureGraph.StructureGraph;
import org.iti.structureGraph.comparison.result.StructureGraphComparisonResult;
import org.iti.structureGraph.comparison.result.Type;
import org.iti.structureGraph.nodes.IStructureElement;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StatementStructureGraphNodeComparerTest {

	private static IStructureGraphComparer comparer = new StatementStructureGraphComparer();

	private static StructureGraph structureGraph;

	private DirectedGraph<IStructureElement, DefaultEdge> statementGraph;

	private Map<String, Type> expectedModifications = new HashMap<>();

	private StructureGraphComparisonResult result;

	@BeforeClass
	public static void init() throws Exception {
		structureGraph = StatementGraphComparerTestHelper.getOriginal();
	}

	@Before
	public void setUp() throws Exception {
		statementGraph = StatementGraphComparerTestHelper.getCurrentGraph();

		expectedModifications.clear();

		result = null;
	}

	@Test
	public void detectsSubgraphIsomorphism() throws StructureGraphComparisonException {
		whenComparisonResultIsCreated();

		assertEquals(expectedModifications.size(), result.getNodeModifications().size());

		StructureGraphComparerTestHelper.assertModificationExpectations(expectedModifications, result);
	}

	@Test
	public void detectsMissingMandatoryNode() throws StructureGraphComparisonException {
		StatementGraphComparerTestHelper.givenMissingMandatoryNode(statementGraph);
		StructureGraphComparerTestHelper.givenExpectedNodeAddition(StatementGraphComparerTestHelper.cn4, structureGraph, expectedModifications);

		whenComparisonResultIsCreated();

		assertEquals(expectedModifications.size(), result.getNodeModifications().size());

		StructureGraphComparerTestHelper.assertModificationExpectations(expectedModifications, result);
	}

	@Test
	public void handlesSuperfluousNodes() throws StructureGraphComparisonException {
		StatementGraphComparerTestHelper.givenSuperfluousMandatoryNode(statementGraph);
		StructureGraphComparerTestHelper.givenExpectedNodeRemovals(StatementGraphComparerTestHelper.cn41, new StructureGraph(statementGraph), expectedModifications);

		whenComparisonResultIsCreated();

		assertEquals(expectedModifications.size(), result.getNodeModifications().size());

		StructureGraphComparerTestHelper.assertModificationExpectations(expectedModifications, result);
	}

	private void whenComparisonResultIsCreated() throws StructureGraphComparisonException {

		StructureGraph structureGraphCurrent = new StructureGraph(statementGraph);

		result = comparer.compare(structureGraphCurrent, structureGraph);

	}
}
