/*******************************************************************************
 * Copyright (c) 2008, 2016, 2018 Profactor GmbH, fortiss GmbH, Johannes Kepler University
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.policies;

import org.eclipse.fordiac.ide.gef.editparts.InterfaceEditPart;
import org.eclipse.fordiac.ide.model.commands.change.ReconnectEventConnectionCommand;
import org.eclipse.fordiac.ide.model.commands.create.EventConnectionCreateCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * An EditPolicy which allows drawing Connections between EventInterfaces.
 */
public class EventNodeEditPolicy extends InterfaceElementEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCreateCommand(final CreateConnectionRequest request) {
		EventConnectionCreateCommand cmd = new EventConnectionCreateCommand(getParentNetwork());
		cmd.setSource(((InterfaceEditPart) getHost()).getModel());
		request.setStartCommand(cmd);
		return new EventConnectionCreateCommand(getParentNetwork());
	}

	@Override
	protected Command createReconnectCommand(ReconnectRequest request) {
		return new ReconnectEventConnectionCommand(request, getParentNetwork());
	}
}
