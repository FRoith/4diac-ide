/*******************************************************************************
 * Copyright (c) 2017, 2023 fortiss GmbH, Johannes Kepler University,
 *                          Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl - initial API and implementation and/or initial documentation
 *               - fixed untyped subapp interface updates and according code
 *                 cleanup
 *               - allow navigation to parent by double-clicking on subapp
 *                 interface element
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.editparts;

import java.util.List;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.fordiac.ide.application.commands.ResizeGroupOrSubappCommand;
import org.eclipse.fordiac.ide.application.figures.UntypedSubappConnectorBorder;
import org.eclipse.fordiac.ide.application.policies.DeleteSubAppInterfaceElementPolicy;
import org.eclipse.fordiac.ide.gef.FixedAnchor;
import org.eclipse.fordiac.ide.gef.annotation.FordiacAnnotationUtil;
import org.eclipse.fordiac.ide.gef.draw2d.ConnectorBorder;
import org.eclipse.fordiac.ide.gef.editparts.LabelDirectEditManager;
import org.eclipse.fordiac.ide.gef.figures.ToolTipFigure;
import org.eclipse.fordiac.ide.gef.policies.INamedElementRenameEditPolicy;
import org.eclipse.fordiac.ide.model.commands.change.ChangeNameCommand;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.fordiac.ide.model.libraryElement.impl.ErrorMarkerDataTypeImpl;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.SWT;

public class UntypedSubAppInterfaceElementEditPart extends InterfaceEditPartForFBNetwork {

	private final TargetPinManager targetPinManager = new TargetPinManager(this);
	TargetInterfaceAdapter targetInteraceAdapter = null;

	private final class SubappInternalConnAnchor extends FixedAnchor {
		private SubappInternalConnAnchor(final IFigure owner, final boolean isInput) {
			super(owner, isInput);
		}

		@Override
		public Point getLocation(final Point reference) {
			final Point location = super.getLocation(reference);
			final IFigure subappFigure = ((GraphicalEditPart) getParent()).getFigure();
			final Rectangle bounds = subappFigure.getBounds().getCopy();
			subappFigure.translateToAbsolute(bounds);
			location.y = Math.max(location.y, bounds.y);
			location.y = Math.min(location.y, bounds.y + bounds.height);
			return location;
		}
	}

	public class UntypedSubappIEAdapter extends EContentAdapter {
		@Override
		public void notifyChanged(final Notification notification) {
			final Object feature = notification.getFeature();
			if (LibraryElementPackage.eINSTANCE.getIInterfaceElement_InputConnections().equals(feature)
					|| LibraryElementPackage.eINSTANCE.getIInterfaceElement_OutputConnections().equals(feature)
					|| LibraryElementPackage.eINSTANCE.getINamedElement_Name().equals(feature)
					|| LibraryElementPackage.eINSTANCE.getINamedElement_Comment().equals(feature)) {
				refresh();
			} else if (LibraryElementPackage.eINSTANCE.getIInterfaceElement_Type().equals(feature)) {
				updateConnectorBorderColor();
				refreshToolTip();
			}
			super.notifyChanged(notification);
		}

		private void updateConnectorBorderColor() {
			Border border = getFigure().getBorder();
			if (border instanceof final CompoundBorder comBorder) {
				border = comBorder.getOuterBorder();
			}
			if (border instanceof final ConnectorBorder conBorder) {
				conBorder.updateColor();
				getFigure().repaint();
			}
		}

		public UntypedSubAppInterfaceElementEditPart getUntypedSubAppInterfaceElementEditPart() {
			return UntypedSubAppInterfaceElementEditPart.this;
		}
	}

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			if (isInExpandedSubapp()) {
				targetInteraceAdapter = new TargetInterfaceAdapter(this);
			}
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			if (targetInteraceAdapter != null) {
				targetInteraceAdapter.deactivate();
			}
			super.deactivate();
		}
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new INamedElementRenameEditPolicy() {
			@Override
			protected Command getDirectEditCommand(final DirectEditRequest request) {
				if (getHost() instanceof final UntypedSubAppInterfaceElementEditPart editPart) {
					final ChangeNameCommand changeNameCmd = ChangeNameCommand.forName(getModel(),
							(String) request.getCellEditor().getValue());
					if (isInExpandedSubapp()) {
						return new ResizeGroupOrSubappCommand((GraphicalEditPart) editPart.getParent(), changeNameCmd);
					}
					return changeNameCmd;
				}
				return null;
			}
		});
		// allow delete of a subapp's interface element
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteSubAppInterfaceElementPolicy());
	}

	@Override
	public void performRequest(final Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			// REQ_DIRECT_EDIT -> first select 0.4 sec pause -> click -> edit
			createDirectEditManager().show();
		} else {
			super.performRequest(request);
		}
	}

	private DirectEditManager createDirectEditManager() {
		return new LabelDirectEditManager(this, getNameLabel());
	}

	public Label getNameLabel() {
		return (Label) getFigure();
	}

	@Override
	protected Adapter createContentAdapter() {
		return new UntypedSubappIEAdapter();
	}

	@Override
	public void refresh() {
		super.refresh();
		getNameLabel().setText(getLabelText());
		refreshToolTip();
	}

	private void refreshToolTip() {
		getFigure().setToolTip(new ToolTipFigure(getModel(), FordiacAnnotationUtil.getAnnotationModel(this)));
	}

	@Override
	protected IFigure createFigure() {
		final InterfaceFigure figure = new InterfaceFigure() {
			@Override
			public String getSubStringText() {
				return (getChildren().isEmpty()) ? super.getSubStringText() : "";
			}

			@Override
			protected void paintFigure(final Graphics graphics) {
				if (!getChildren().isEmpty()) {
					graphics.fillRoundRectangle(getBounds(), 8, 8);
				}
				super.paintFigure(graphics);
			}
		};
		figure.setToolTip(new ToolTipFigure(getModel(), FordiacAnnotationUtil.getAnnotationModel(this)));

		figure.setBorder(new UntypedSubappConnectorBorder(getModel()));
		return figure;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(final ConnectionEditPart connection) {
		if (isInput()) {
			// we are unfolded and this is an internal connection
			return new SubappInternalConnAnchor(getFigure(), !isInput());
		}
		return new FixedAnchor(getFigure(), isInput());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connection) {
		if (!isInput()) {
			// we are unfolded and this is an internal connection
			return new SubappInternalConnAnchor(getFigure(), !isInput());
		}
		return super.getTargetConnectionAnchor(connection);
	}

	@Override
	protected List getModelChildren() {
		if (isInExpandedSubapp()) {
			return targetPinManager.getModelChildren();
		}
		return super.getModelChildren();
	}

	private boolean isInExpandedSubapp() {
		return (getModel().getFBNetworkElement() instanceof final SubApp subApp) && subApp.isUnfolded();
	}

	@Override
	protected void addChildVisual(final EditPart childEditPart, final int index) {
		final var epFigure = getFigure();
		if (epFigure.getLayoutManager() == null) {
			final var tbLayout = new GridLayout(1, true);
			tbLayout.marginHeight = 2;
			tbLayout.marginWidth = 0;
			tbLayout.verticalSpacing = 2;
			tbLayout.horizontalSpacing = 0;
			epFigure.setLayoutManager(tbLayout);
			epFigure.setBackgroundColor(ColorConstants.white);
		}
		final IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		getContentPane().add(child, new GridData(SWT.FILL, SWT.BEGINNING, true, false), index);
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == UntypedSubAppInterfaceElementEditPart.class) {
			return key.cast(this);
		}
		if (key == ErrorMarkerDataTypeImpl.class) {
			final IInterfaceElement model = getModel();
			final ErrorMarkerDataTypeImpl marker = model instanceof VarDeclaration
					? (ErrorMarkerDataTypeImpl) ((VarDeclaration) model).getType()
					: null;
			return key.cast(marker);
		}

		return super.getAdapter(key);
	}

}