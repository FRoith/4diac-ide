/**
 * *******************************************************************************
 * Copyright (c) 2008 - 2018 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger, Martin Jobst
 *      - initial API and implementation and/or initial documentation
 * *******************************************************************************
 */
package org.eclipse.fordiac.ide.model.Palette;

import org.eclipse.fordiac.ide.model.dataimport.CommonElementImporter;

import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.libraryElement.ResourceType;

/** <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Type Entry</b></em>'. <!--
 * end-user-doc -->
 *
 *
 * @see org.eclipse.fordiac.ide.model.Palette.PalettePackage#getResourceTypeEntry()
 * @model
 * @generated */
public interface ResourceTypeEntry extends PaletteEntry {
	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated */
	ResourceType getResourceType();

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model
	 * @generated */
	@Override
	void setType(LibraryElement type);

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.fordiac.ide.model.Palette.CommonElementImporter"
	 * @generated */
	@Override
	CommonElementImporter getImporter();

} // ResourceTypeEntry
