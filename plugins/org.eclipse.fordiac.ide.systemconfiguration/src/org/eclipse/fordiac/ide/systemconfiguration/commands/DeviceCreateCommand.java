/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GbmH, TU Wien ACIN, fortiss GmbH
 * 				 2019 Johannes Keppler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Gerd Kainz, Monika Wenger, Kiril Dorofeev
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - removed editor check from canUndo
 *******************************************************************************/
package org.eclipse.fordiac.ide.systemconfiguration.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.fordiac.ide.model.NameRepository;
import org.eclipse.fordiac.ide.model.Palette.DeviceTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.ResourceTypeEntry;
import org.eclipse.fordiac.ide.model.dataimport.CommonElementImporter;
import org.eclipse.fordiac.ide.model.libraryElement.Attribute;
import org.eclipse.fordiac.ide.model.libraryElement.AttributeDeclaration;
import org.eclipse.fordiac.ide.model.libraryElement.Color;
import org.eclipse.fordiac.ide.model.libraryElement.Device;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.libraryElement.Resource;
import org.eclipse.fordiac.ide.model.libraryElement.SystemConfiguration;
import org.eclipse.fordiac.ide.systemconfiguration.Messages;
import org.eclipse.fordiac.ide.systemmanagement.SystemManager;
import org.eclipse.fordiac.ide.ui.UIPlugin;
import org.eclipse.fordiac.ide.ui.preferences.PreferenceConstants;
import org.eclipse.fordiac.ide.util.ColorHelper;
import org.eclipse.fordiac.ide.util.YUV;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.RGB;

public class DeviceCreateCommand extends Command {
	private static final String CREATE_DEVICE_LABEL = Messages.DeviceCreateCommand_LABEL_CreateDevice;
	private final DeviceTypePaletteEntry entry;
	private final SystemConfiguration parent;
	private final Rectangle bounds;
	private Device device;

	public Device getDevice() {
		return device;
	}

	public DeviceCreateCommand(final DeviceTypePaletteEntry entry, final SystemConfiguration parent,
			final Rectangle bounds) {
		this.entry = entry;
		this.parent = parent;
		this.bounds = bounds;
		setLabel(CREATE_DEVICE_LABEL);
	}

	@Override
	public boolean canExecute() {
		return (entry != null) && (bounds != null) && (parent != null);
	}

	@Override
	public void execute() {
		createDevice();
		device.setPaletteEntry(entry);
		CommonElementImporter.createParamters(device);
		setDeviceProfile();
		device.updatePosition(bounds.getTopLeft());
		parent.getDevices().add(device);
		// the name needs to be set after the device is added to the network
		// so that name checking works correctly
		device.setName(NameRepository.createUniqueName(device, entry.getDeviceType().getName()));
		setDeviceAttributes();
		createResource();
		SystemManager.INSTANCE.notifyListeners();
	}

	private void setDeviceAttributes() {
		for (final AttributeDeclaration attributeDeclaration : entry.getDeviceType().getAttributeDeclarations()) {
			final Attribute attribute = LibraryElementFactory.eINSTANCE.createAttribute();
			attribute.setName(attributeDeclaration.getName());
			attribute.setComment(attributeDeclaration.getComment());
			attribute.setValue(attributeDeclaration.getInitialValue());
			attribute.setAttributeDeclaration(attributeDeclaration);
			device.getAttributes().add(attribute);
		}
	}

	private void setDeviceProfile() {
		String profile;
		if ((null != device.getType().getProfile()) && !"".equals(device.getType().getProfile())) { //$NON-NLS-1$
			profile = device.getType().getProfile();
		} else {
			profile = UIPlugin.getDefault().getPreferenceStore()
					.getString(PreferenceConstants.P_DEFAULT_COMPLIANCE_PROFILE);
		}
		device.setProfile(profile);
	}

	protected void createDevice() {
		device = LibraryElementFactory.eINSTANCE.createDevice();
		device.setColor(createRandomDeviceColor());
	}

	private void createResource() {
		for (final Resource res : entry.getDeviceType().getResource()) {
			ResourceCreateCommand cmd = null;
			if (res.getPaletteEntry() != null) {
				cmd = new ResourceCreateCommand((ResourceTypeEntry) res.getPaletteEntry(), device, true);
				cmd.execute();
				final Resource copy = cmd.getResource();
				copy.setName(res.getName());
			} else {
				org.eclipse.fordiac.ide.systemconfiguration.Activator.getDefault().logInfo("Referenced Resource Type: " //$NON-NLS-1$
						+ (res.getName() != null ? res.getName() : "N/A") //$NON-NLS-1$
						+ (res.getPaletteEntry() != null ? " (" + res.getTypeName() + ") " : "(N/A)") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ " not found. Please check whether your palette contains that type and add it manually to your device!"); //$NON-NLS-1$
			}
		}
		createDefaultResource();
	}

	private void createDefaultResource() {
		ResourceTypeEntry type = null;
		if (device.getType().getName().contains("FBRT") //$NON-NLS-1$
				|| device.getType().getName().contains("FRAME")) { //$NON-NLS-1$
			type = getResourceType("PANEL_RESOURCE"); //$NON-NLS-1$
		} else {
			type = getResourceType("EMB_RES"); //$NON-NLS-1$
		}
		if (null != type) {
			final ResourceCreateCommand cmd = new ResourceCreateCommand(type, device, false);
			cmd.execute();
		}
	}

	private ResourceTypeEntry getResourceType(final String resTypeName) {
		return device.getPaletteEntry().getPalette().getResourceTypeEntry(resTypeName);
	}

	private Color createRandomDeviceColor() {
		Color randomColor;
		boolean exist;
		final List<YUV> existingColors = new ArrayList<>();
		for (final Device dev : parent.getDevices()) {
			final Color devcolor = dev.getColor();
			existingColors.add(new YUV(new RGB(devcolor.getRed(), devcolor.getGreen(), devcolor.getBlue())));
		}
		if (existingColors.isEmpty()) {
			return ColorHelper.getStartingColor();
		}
		do {
			randomColor = ColorHelper.createRandomColor();
			final YUV randYUV = new YUV(new RGB(randomColor.getRed(), randomColor.getGreen(), randomColor.getBlue()));
			exist = false;
			for (final YUV yuv : existingColors) {
				if (randYUV.nearbyColor(yuv)) {
					exist = true;
					break;
				}
			}
		} while (exist);
		return randomColor;
	}

	@Override
	public void redo() {
		if (parent != null) {
			parent.getDevices().add(device);
			SystemManager.INSTANCE.notifyListeners();
		}
	}

	@Override
	public void undo() {
		if (parent != null) {
			parent.getDevices().remove(device);
			SystemManager.INSTANCE.notifyListeners();
		}
	}
}