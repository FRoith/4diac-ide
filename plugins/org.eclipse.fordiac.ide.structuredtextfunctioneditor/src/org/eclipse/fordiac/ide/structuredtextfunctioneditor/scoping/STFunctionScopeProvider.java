/*******************************************************************************
 * Copyright (c) 2022 Primetals Technologies GmbH
 *               2022 Martin Erich Jobst
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Martin Melik Merkumians
 *       - initial API and implementation and/or initial documentation
 *   Martin Jobst
 *       - add scope for function return values
 *******************************************************************************/
package org.eclipse.fordiac.ide.structuredtextfunctioneditor.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.fordiac.ide.model.typelibrary.DataTypeLibrary;
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.sTFunction.STFunctionPackage;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.SimpleScope;

/**
 * This class contains custom scoping description.
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class STFunctionScopeProvider extends AbstractSTFunctionScopeProvider {
	@Override
	public IScope getScope(final EObject context, final EReference reference) {
		if (reference == STFunctionPackage.Literals.FUNCTION_DEFINITION__RETURN_TYPE) {
			final IScope globalScope = super.getScope(context, reference);
			return new SimpleScope(globalScope, Scopes.scopedElementsFor(DataTypeLibrary.getNonUserDefinedDataTypes()),
					true);
		}
		return super.getScope(context, reference);
	}
}
