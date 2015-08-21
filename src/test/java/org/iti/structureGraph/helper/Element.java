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

package org.iti.structureGraph.helper;

import org.iti.structureGraph.nodes.IStructureElement;

public class Element implements IStructureElement {

	private String name = "";
	private boolean mandatory = false;
	private boolean optionalList = false;

	public Element(String name) {
		this.name = name;
	}

	public Element(String name, boolean mandatory, boolean optionalList) {
		this(name);

		this.mandatory = mandatory;
		this.optionalList = optionalList;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public boolean isOptionalList() {
		return optionalList;
	}
}
