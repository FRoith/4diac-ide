/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 * 				 2019 - 2020 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl - initial API and implementation and/or initial documentation
 *   			 - added the option that also within a subapp flatten can be
 *                 invoked, this included clean-up and the use of a compound
 *                 command for a better undo redo behavior
 *               - added checking and opening of parent editors
 *  Lukas Wais	 - added setEnabled and getSelectedSubApp
 *  			   from ToggleSubAppRepresentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.handlers;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.fordiac.ide.application.commands.FlattenSubAppCommand;
import org.eclipse.fordiac.ide.application.editparts.SubAppForFBNetworkEditPart;
import org.eclipse.fordiac.ide.application.editparts.UISubAppNetworkEditPart;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.ui.editors.HandlerHelper;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

public class FlattenSubApplication extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IEditorPart editor = HandlerUtil.getActiveEditor(event);

		final CompoundCommand mainCmd = checkSelection(event, editor);
		if (!mainCmd.isEmpty()) {
			editor.getAdapter(CommandStack.class).execute(mainCmd);
		}
		return Status.OK_STATUS;
	}

	private static CompoundCommand checkSelection(final ExecutionEvent event, final IEditorPart editor) {
		final CompoundCommand mainCmd = new CompoundCommand();
		for (final Object currentElement : getSelectionList(event)) {
			final SubApp subApp = getSubApp(currentElement);
			if (null != subApp) {
				checkCurrentEditor(subApp, editor);
				final FlattenSubAppCommand cmd = new FlattenSubAppCommand(subApp);
				if (cmd.canExecute()) {
					mainCmd.add(cmd);
				}
			}
		}
		return mainCmd;
	}

	@Override
	public void setEnabled(final Object evaluationContext) {
		final Object selection = HandlerUtil.getVariable(evaluationContext, ISources.ACTIVE_CURRENT_SELECTION_NAME);
		final SubApp subApp = getSelectedSubApp(selection);

		setBaseEnabled(HandlerHelper.isEditableSubApp(subApp));
	}

	private static SubApp getSubApp(final Object currentElement) {
		if (currentElement instanceof SubApp) {
			return (SubApp) currentElement;
		} else if (currentElement instanceof SubAppForFBNetworkEditPart) {
			return ((SubAppForFBNetworkEditPart) currentElement).getModel();
		} else if (currentElement instanceof UISubAppNetworkEditPart) {
			return (SubApp) ((UISubAppNetworkEditPart) currentElement).getModel().eContainer();
		}
		return null;
	}

	private static SubApp getSelectedSubApp(final Object selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structSel = ((IStructuredSelection) selection);
			if (!structSel.isEmpty() && (structSel.size() == 1)) {
				return getSubApp(structSel.getFirstElement());
			}
		}
		return null;
	}

	private static List<Object> getSelectionList(final ExecutionEvent event) {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof StructuredSelection) {
			return ((StructuredSelection) selection).toList();
		}
		return Collections.emptyList();
	}

	private static void checkCurrentEditor(final SubApp subApp, final IEditorPart editor) {
		if (editor.getAdapter(FBNetwork.class).equals(subApp.getSubAppNetwork())) {
			// we are invoking the method from within the subapp, switch to the parent
			// editor
			HandlerHelper.openEditor(subApp.getFbNetwork().eContainer());
		}
	}

}
