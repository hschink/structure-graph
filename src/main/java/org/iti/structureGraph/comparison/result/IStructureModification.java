package org.iti.structureGraph.comparison.result;

public interface IStructureModification {

	String getIdentifier();
	Type getType();
	IModificationDetail getModificationDetail();
}