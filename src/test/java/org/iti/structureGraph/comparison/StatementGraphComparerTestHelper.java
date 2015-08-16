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
	static Element cn2 = new Element("cn2");
	static Element cn3 = new Element("cn3", true);
	static Element cn4 = new Element("cn4", true);
	static Element cn41 = new Element("cn41", true);
	static Element cn5 = new Element("cn5");
	static Element cn6 = new Element("cn6");
	static Element cn7 = new Element("cn7", true);
	static Element cn8 = new Element("cn8", true);

	static StructureGraph getOriginal() {
		DirectedGraph<IStructureElement, DefaultEdge> originalGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		originalGraph.addVertex(cn1);
		originalGraph.addVertex(cn2);
		originalGraph.addVertex(cn3);
		originalGraph.addVertex(cn4);
		originalGraph.addVertex(cn5);
		originalGraph.addVertex(cn6);
		originalGraph.addVertex(cn7);
		originalGraph.addVertex(cn8);

		originalGraph.addEdge(cn1, cn2, new Edge2());
		originalGraph.addEdge(cn2, cn3, new Edge3());
		originalGraph.addEdge(cn2, cn4, new Edge3());

		originalGraph.addEdge(cn5, cn6, new Edge2());
		originalGraph.addEdge(cn6, cn7, new Edge3());
		originalGraph.addEdge(cn6, cn8, new Edge3());

		return new StructureGraph(originalGraph);
	}

	static SimpleDirectedGraph<IStructureElement, DefaultEdge> getCurrentGraph() {
		SimpleDirectedGraph<IStructureElement, DefaultEdge> currentGraph = new SimpleDirectedGraph<IStructureElement, DefaultEdge>(DefaultEdge.class);

		currentGraph.addVertex(cn1);
		currentGraph.addVertex(cn2);
		currentGraph.addVertex(cn3);
		currentGraph.addVertex(cn4);

		currentGraph.addEdge(cn1, cn2, new Edge2());
		currentGraph.addEdge(cn2, cn3, new Edge3());
		currentGraph.addEdge(cn2, cn4, new Edge3());

		return currentGraph;
	}

	public static void givenMissingMandatoryNode(DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.removeVertex(cn4);
	}

	public static void givenSuperfluousMandatoryNode(DirectedGraph<IStructureElement, DefaultEdge> graph) {
		graph.addVertex(cn41);

		graph.addEdge(cn2, cn41, new Edge3());
	}
}
