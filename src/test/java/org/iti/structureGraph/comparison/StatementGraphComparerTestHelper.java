package org.iti.structureGraph.comparison;

import org.iti.structureGraph.StructureGraph;
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
	static Element cn32 = new Element("cn32", true, false);
	static Element cn33 = new Element("cn33", true, true);
	static Element cn330 = new Element("cn330", true, false);
	static Element cn331 = new Element("cn331", true, false);

	static StructureGraph getOriginal() {
		DirectedGraph<IStructureElement, DefaultEdge> originalGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		originalGraph.addVertex(cn1);
		originalGraph.addVertex(cn10);
		originalGraph.addVertex(cn11);
		originalGraph.addVertex(cn12);

		originalGraph.addVertex(cn2);
		originalGraph.addVertex(cn20);
		originalGraph.addVertex(cn21);
		originalGraph.addVertex(cn22);

		originalGraph.addVertex(cn3);
		originalGraph.addVertex(cn30);
		originalGraph.addVertex(cn31);
		originalGraph.addVertex(cn32);
		originalGraph.addVertex(cn33);

		originalGraph.addEdge(cn1, cn10, new Edge2());
		originalGraph.addEdge(cn10, cn11, new Edge3());
		originalGraph.addEdge(cn10, cn12, new Edge3());

		originalGraph.addEdge(cn2, cn20, new Edge2());
		originalGraph.addEdge(cn20, cn21, new Edge3());
		originalGraph.addEdge(cn20, cn22, new Edge3());

		originalGraph.addEdge(cn3, cn30, new Edge2());
		originalGraph.addEdge(cn30, cn31, new Edge3());
		originalGraph.addEdge(cn30, cn32, new Edge3());
		originalGraph.addEdge(cn30, cn33, new Edge3());

		return new StructureGraph(originalGraph);
	}

	static SimpleDirectedGraph<IStructureElement, DefaultEdge> getCurrentGraph() {
		SimpleDirectedGraph<IStructureElement, DefaultEdge> currentGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		currentGraph.addVertex(cn1);
		currentGraph.addVertex(cn10);
		currentGraph.addVertex(cn11);
		currentGraph.addVertex(cn12);

		currentGraph.addVertex(cn3);
		currentGraph.addVertex(cn30);
		currentGraph.addVertex(cn31);
		currentGraph.addVertex(cn32);
		currentGraph.addVertex(cn330);
		currentGraph.addVertex(cn331);

		currentGraph.addEdge(cn1, cn10, new Edge2());
		currentGraph.addEdge(cn10, cn11, new Edge3());
		currentGraph.addEdge(cn10, cn12, new Edge3());

		currentGraph.addEdge(cn3, cn30, new Edge2());
		currentGraph.addEdge(cn30, cn31, new Edge3());
		currentGraph.addEdge(cn30, cn32, new Edge3());
		currentGraph.addEdge(cn30, cn330, new Edge3());
		currentGraph.addEdge(cn30, cn331, new Edge3());

		return currentGraph;
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
		graph.removeVertex(cn32);
	}
}
