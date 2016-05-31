package org.iti.structureGraph.comparison;

import org.iti.structureGraph.helper.Edge2;
import org.iti.structureGraph.helper.Edge3;
import org.iti.structureGraph.helper.Element;
import org.iti.structureGraph.nodes.IStructureElement;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class StatementGraphComparerTestHelper {

	static Element cn1 = new Element("cn1");
	static Element cn10 = new Element("cn10");
	static Element cn11 = new Element("cn11", true, false);
	static Element cn12 = new Element("cn12", true, false);
	static Element cn13 = new Element("cn13", true, false);

	static Element cn2 = new Element("cn2");
	static Element cn20 = new Element("cn20");
	static Element cn21 = new Element("cn21", true, false);
	static Element cn22 = new Element("cn22", true, false);

	static Element cn3 = new Element("cn3");
	static Element cn30 = new Element("cn30");
	static Element cn31 = new Element("cn31", true, false);
	static Element cn310 = new Element("cn31", false, false);
	static Element cn32 = new Element("cn32", true, false);
	static Element cn320 = new Element("cn32", false, false);
	static Element cn33 = new Element("cn33", false, true);
	static Element cn330 = new Element("cn330", false, false);
	static Element cn331 = new Element("cn331", false, false);
	static Element cn332 = new Element("cn332", false, false);

	static DirectedGraph<IStructureElement, DefaultEdge> getStructureGraph() {
		DirectedGraph<IStructureElement, DefaultEdge> structureGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		structureGraph.addVertex(cn1);
		structureGraph.addVertex(cn10);
		structureGraph.addVertex(cn11);
		structureGraph.addVertex(cn12);

		structureGraph.addVertex(cn2);
		structureGraph.addVertex(cn20);
		structureGraph.addVertex(cn21);
		structureGraph.addVertex(cn22);

		structureGraph.addVertex(cn3);
		structureGraph.addVertex(cn30);
		structureGraph.addVertex(cn31);
		structureGraph.addVertex(cn32);
		structureGraph.addVertex(cn33);

		structureGraph.addEdge(cn1, cn10, new Edge2());
		structureGraph.addEdge(cn10, cn11, new Edge3());
		structureGraph.addEdge(cn10, cn12, new Edge3());

		structureGraph.addEdge(cn2, cn20, new Edge2());
		structureGraph.addEdge(cn20, cn21, new Edge3());
		structureGraph.addEdge(cn20, cn22, new Edge3());

		structureGraph.addEdge(cn3, cn30, new Edge2());
		structureGraph.addEdge(cn30, cn31, new Edge3());
		structureGraph.addEdge(cn30, cn32, new Edge3());
		structureGraph.addEdge(cn30, cn33, new Edge3());

		return structureGraph;
	}

	static SimpleDirectedGraph<IStructureElement, DefaultEdge> getStatementGraph() {
		SimpleDirectedGraph<IStructureElement, DefaultEdge> statementGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		statementGraph.addVertex(cn1);
		statementGraph.addVertex(cn10);
		statementGraph.addVertex(cn11);
		statementGraph.addVertex(cn12);

		statementGraph.addVertex(cn3);
		statementGraph.addVertex(cn30);
		statementGraph.addVertex(cn310);
		statementGraph.addVertex(cn320);
		statementGraph.addVertex(cn330);
		statementGraph.addVertex(cn331);
		statementGraph.addVertex(cn332);

		statementGraph.addEdge(cn1, cn10, new Edge2());
		statementGraph.addEdge(cn10, cn11, new Edge3());
		statementGraph.addEdge(cn10, cn12, new Edge3());

		statementGraph.addEdge(cn3, cn30, new Edge2());
		statementGraph.addEdge(cn30, cn310, new Edge3());
		statementGraph.addEdge(cn30, cn320, new Edge3());
		statementGraph.addEdge(cn30, cn330, new Edge3());
		statementGraph.addEdge(cn30, cn331, new Edge3());
		statementGraph.addEdge(cn30, cn332, new Edge3());

		return statementGraph;
	}

	public static void givenMissingMandatoryNode(DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.removeVertex(cn12);
	}

	public static void givenSuperfluousMandatoryNode(DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.addVertex(cn13);

		graph.addEdge(cn10, cn13, new Edge3());
	}

	public static void givenMissingMandatoryNodeNextToOptionalListNode(
			DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.removeVertex(cn320);
	}

	public static void givenParentNodeWithOptionalListChildIsMissing(DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.removeVertex(cn30);
		graph.removeVertex(cn310);
		graph.removeVertex(cn320);
		graph.removeVertex(cn330);
		graph.removeVertex(cn331);
		graph.removeVertex(cn332);
	}
}
