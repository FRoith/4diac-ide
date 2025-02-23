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
package org.eclipse.fordiac.ide.model.Palette.impl;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.fordiac.ide.model.Activator;
import org.eclipse.fordiac.ide.model.Palette.PalettePackage;
import org.eclipse.fordiac.ide.model.Palette.SegmentTypePaletteEntry;
import org.eclipse.fordiac.ide.model.dataimport.CommonElementImporter;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.libraryElement.SegmentType;

/** <!-- begin-user-doc --> An implementation of the model object '<em><b>Segment Type Palette Entry</b></em>'. <!--
 * end-user-doc -->
 *
 * @generated */
public class SegmentTypePaletteEntryImpl extends PaletteEntryImpl implements SegmentTypePaletteEntry {
	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated */
	protected SegmentTypePaletteEntryImpl() {
		super();
	}

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated */
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.SEGMENT_TYPE_PALETTE_ENTRY;
	}

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated */
	@Override
	public SegmentType getSegmentType() {
		final LibraryElement type = getType();
		if (type instanceof SegmentType) {
			return (SegmentType) type;
		}
		return null;
	}

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated */
	@Override
	public void setType(final LibraryElement type) {
		if (type instanceof SegmentType) {
			super.setType(type);
		} else {
			super.setType(null);
			if (null != type) {
				final Status exception = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"tried to set no SegmentType as type entry for SegmentTypePaletteEntry"); //$NON-NLS-1$
				Activator.getDefault().getLog().log(exception);
			}
		}
	}

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated */
	@Override
	public CommonElementImporter getImporter() {
		return new org.eclipse.fordiac.ide.model.dataimport.SEGImporter(getFile());
	}

} // SegmentTypePaletteEntryImpl
