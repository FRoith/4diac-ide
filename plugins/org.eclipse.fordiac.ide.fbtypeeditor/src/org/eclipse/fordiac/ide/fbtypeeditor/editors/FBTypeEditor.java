/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 				 2018 TU Wien/ACIN
 * 				 2020 Johannes Kepler University, Linz
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *
 *   Peter Gsellmann
 *     - incorporating simple fb
 *
 *   Daniel Lindhuber, Bianca Wiesmayr
 *     - cleanup
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.editors;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.fordiac.ide.fbtypeeditor.Activator;
import org.eclipse.fordiac.ide.fbtypeeditor.Messages;
import org.eclipse.fordiac.ide.model.Palette.AdapterTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.FBTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.PaletteEntry;
import org.eclipse.fordiac.ide.model.dataexport.AbstractBlockTypeExporter;
import org.eclipse.fordiac.ide.model.libraryElement.AdapterFBType;
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.CompositeFBType;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceInterfaceFBType;
import org.eclipse.fordiac.ide.model.libraryElement.SimpleFBType;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibrary;
import org.eclipse.fordiac.ide.systemmanagement.changelistener.IEditorFileChangeListener;
import org.eclipse.fordiac.ide.typemanagement.FBTypeEditorInput;
import org.eclipse.fordiac.ide.ui.editors.AbstractCloseAbleFormEditor;
import org.eclipse.fordiac.ide.ui.editors.EditorUtils;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class FBTypeEditor extends AbstractCloseAbleFormEditor implements ISelectionListener, CommandStackEventListener,
ITabbedPropertySheetPageContributor, IGotoMarker, IEditorFileChangeListener, INavigationLocationProvider {

	private Collection<IFBTEditorPart> editors;
	private PaletteEntry paletteEntry;
	private FBType fbType;
	private FBTypeContentOutline contentOutline = null;
	private final CommandStack commandStack = new CommandStack();

	private final Adapter adapter = new AdapterImpl() {

		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			final Object feature = notification.getFeature();
			if (null != feature) {
				if (LibraryElementPackage.LIBRARY_ELEMENT__NAME == notification.getFeatureID(feature.getClass())) {
					Display.getDefault().asyncExec(() -> {
						if (null != paletteEntry) {
							setPartName(paletteEntry.getFile().getName());
							setInput(new FileEditorInput(paletteEntry.getFile()));
						}
					});

				}
			}
		}
	};

	@Override
	public void doSave(final IProgressMonitor monitor) {
		if ((null != paletteEntry) && (checkTypeSaveAble())) {
			performPresaveHooks();
			// allow each editor to save back changes before saving to file
			editors.forEach(editorPart -> editorPart.doSave(monitor));

			getCommandStack().markSaveLocation();
			AbstractBlockTypeExporter.saveType(paletteEntry);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	private void performPresaveHooks() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IConfigurationElement[] config = registry
				.getConfigurationElementsFor("org.eclipse.fordiac.ide.fbtypeeditor.fBTEditorValidation"); //$NON-NLS-1$

		for (final IConfigurationElement e : config) {
			try {
				final Object o = e.createExecutableExtension("class"); //$NON-NLS-1$
				if (o instanceof IFBTValidation) {
					((IFBTValidation) o).invokeValidation(fbType);
				}
			} catch (final CoreException ex) {
				Activator.getDefault().logError(ex.getMessage(), ex);
			}
		}
	}

	protected boolean checkTypeSaveAble() {
		if (fbType instanceof CompositeFBType) {
			// only allow to save composite FBs if all contained FBs have types
			for (final FBNetworkElement fb : ((CompositeFBType) fbType).getFBNetwork().getNetworkElements()) {
				if (null == fb.getPaletteEntry()) {
					MessageDialog.openInformation(getSite().getShell(),
							Messages.FBTypeEditor_CompositeContainsFunctionBlockWithoutType,
							MessageFormat.format(Messages.FBTypeEditor_CheckTypeSaveAble, fb.getName()));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void doSaveAs() {
		// TODO implement a save as new type method
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks
	 * that the input is an instance of <code>FBTypeEditorInput</code>.
	 *
	 * @param site        the site
	 * @param editorInput the editor input
	 *
	 * @throws PartInitException the part init exception
	 */
	@Override
	public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {

		if (editorInput instanceof FileEditorInput) {
			final IFile fbTypeFile = ((FileEditorInput) editorInput).getFile();
			if (!fbTypeFile.exists()) {
				throw new PartInitException(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.FBTypeEditor_TypeFileDoesnotExist));
			}

			paletteEntry = TypeLibrary.getPaletteEntryForFile(fbTypeFile);
		} else if (editorInput instanceof FBTypeEditorInput) {
			paletteEntry = ((FBTypeEditorInput) editorInput).getPaletteEntry();
		}

		if (null != paletteEntry) {
			setPartName(paletteEntry.getLabel());

			fbType = getFBType(paletteEntry);
			if (null != fbType) {
				// TODO create a copy of the type here so that closing the editor without
				// saveing is better implemented
				// Attention adapters need for saveing then beeing treated special
				fbType.eAdapters().add(adapter);
			}
		}

		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		getCommandStack().addCommandStackEventListener(this);

		super.init(site, editorInput);
	}

	protected FBType getFBType(final PaletteEntry paletteEntry) {
		if (paletteEntry instanceof FBTypePaletteEntry) {
			return ((FBTypePaletteEntry) paletteEntry).getFBType();
		} else if (paletteEntry instanceof AdapterTypePaletteEntry) {
			return ((AdapterTypePaletteEntry) paletteEntry).getType().getAdapterFBType();
		}
		return null;
	}

	public FBType getFBType() {
		return fbType;
	}

	public CommandStack getCommandStack() {
		return commandStack;
	}

	@Override
	public void dispose() {
		if ((fbType != null) && fbType.eAdapters().contains(adapter)) {
			fbType.eAdapters().remove(adapter);
		}

		// get these values here before calling super dispose
		final boolean dirty = isDirty();

		if (null != getSite()) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		}

		super.dispose();

		if (dirty) {
			// purge from typelib after super.dispose() so that no notifiers
			// will be called
			if (null != paletteEntry) {
				paletteEntry.setType(null);
			}
		}

		getCommandStack().removeCommandStackEventListener(this);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void addPages() {
		final SortedMap<Integer, IFBTEditorPart> sortedEditorsMap = getEditorsSorted();

		editors = new ArrayList<>();
		final FBTypeEditorInput editorInput = getFBTypeEditorInput();

		for (final IFBTEditorPart fbtEditorPart : sortedEditorsMap.values()) {
			editors.add(fbtEditorPart);
			try {
				// setCommonCommandStack needs to be called before the editor is added as page
				fbtEditorPart.setCommonCommandStack(commandStack);
				final int index = addPage(fbtEditorPart, editorInput);
				setPageText(index, fbtEditorPart.getTitle());
				setPageImage(index, fbtEditorPart.getTitleImage());
			} catch (final PartInitException e) {
				Activator.getDefault().logError(e.getMessage(), e);
			}

		}
	}

	private SortedMap<Integer, IFBTEditorPart> getEditorsSorted() {
		final SortedMap<Integer, IFBTEditorPart> sortedEditorsMap = new TreeMap<>();

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IExtensionPoint point = registry.getExtensionPoint("org.eclipse.fordiac.ide.fbtypeeditor.fBTEditorTabs"); //$NON-NLS-1$
		final IExtension[] extensions = point.getExtensions();
		for (final IExtension extension : extensions) {
			final IConfigurationElement[] elements = extension.getConfigurationElements();
			for (final IConfigurationElement element : elements) {
				try {
					final Object obj = element.createExecutableExtension("class"); //$NON-NLS-1$
					if (obj instanceof IFBTEditorPart) {
						final String elementType = element.getAttribute("type"); //$NON-NLS-1$
						final Integer sortIndex = Integer.valueOf(element.getAttribute("sortIndex")); //$NON-NLS-1$

						if (checkTypeEditorType(fbType, elementType)) {
							sortedEditorsMap.put(sortIndex, (IFBTEditorPart) obj);
						}
					}
				} catch (final Exception e) {
					Activator.getDefault().logError(e.getMessage(), e);
				}
			}
		}
		return sortedEditorsMap;
	}

	/**
	 * Check if the given editor type is a valid editor for the given type
	 *
	 * @param fbType     type to be edited in this type editor
	 * @param editorType editor type string as defined the fBTEditorTabs.exsd
	 * @return true if the editor should be shown otherwise false
	 */
	protected boolean checkTypeEditorType(final FBType fbType, final String editorType) {
		return ((editorType.equals("ForAllTypes")) || //$NON-NLS-1$
				(editorType.equals("ForAllFBTypes")) || //$NON-NLS-1$
				(editorType.equals("ForAllNonAdapterFBTypes") && !(fbType instanceof AdapterFBType)) || //$NON-NLS-1$
				((fbType instanceof BasicFBType) && editorType.equals("basic")) || //$NON-NLS-1$
				((fbType instanceof SimpleFBType) && editorType.equals("simple")) || //$NON-NLS-1$
				((fbType instanceof ServiceInterfaceFBType) && editorType.equals("serviceInterface")) || //$NON-NLS-1$
				((fbType instanceof CompositeFBType) && editorType.equals("composite")) //$NON-NLS-1$
				);
	}

	private FBTypeEditorInput getFBTypeEditorInput() {
		return new FBTypeEditorInput(fbType, paletteEntry);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (this.equals(getSite().getPage().getActiveEditor())) {
			handleContentOutlineSelection(selection);
		}
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (adapter == IContentOutlinePage.class) {
			if (null == contentOutline) {
				contentOutline = new FBTypeContentOutline(fbType, this);
			}
			return adapter.cast(contentOutline);
		}
		if (adapter == CommandStack.class) {
			return adapter.cast(getCommandStack());
		}
		if (adapter == IPropertySheetPage.class) {
			return adapter.cast(new TabbedPropertySheetPage(this));
		}
		if (adapter == IGotoMarker.class) {
			return adapter.cast(this);
		}
		return super.getAdapter(adapter);
	}

	public void handleContentOutlineSelection(final ISelection selection) {
		if ((selection instanceof IStructuredSelection) && !selection.isEmpty()) {
			final Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
			int i = 0;
			for (final IFBTEditorPart editorPart : editors) {
				if (editorPart.outlineSelectionChanged(selectedElement)) {
					setActivePage(i);
					break;
				}
				i++;
			}
		}
	}

	public IEditorActionBarContributor getActionBarContributor() {
		return null;
	}

	@Override
	public void stackChanged(final CommandStackEvent event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public String getContributorId() {
		return "property.contributor.fb"; //$NON-NLS-1$
	}

	@Override
	public void gotoMarker(final IMarker marker) {
		int i = 0;
		for (final IFBTEditorPart editorPart : editors) {
			if (editorPart.isMarkerTarget(marker)) {
				setActivePage(i);
				editorPart.gotoMarker(marker);
				break;
			}
			i++;
		}
	}

	@Override
	public void reloadFile() {
		if ((fbType != null) && fbType.eAdapters().contains(adapter)) {
			fbType.eAdapters().remove(adapter);
		}
		fbType = (FBType) paletteEntry.getType();
		editors.stream().forEach(e -> e.reloadType(fbType));
		final IEditorPart activeEditor = getActiveEditor();
		if (activeEditor instanceof IFBTEditorPart) {
			Display.getDefault()
					.asyncExec(() -> EditorUtils.refreshPropertySheetWithSelection(this,
							activeEditor.getAdapter(GraphicalViewer.class),
							((IFBTEditorPart) activeEditor).getSelectableEditPart()));
		}
		getCommandStack().flush();
		fbType.eAdapters().add(adapter);

	}

	@Override
	public IFile getFile() {
		return paletteEntry != null ? paletteEntry.getFile() : null;
	}

	@Override
	protected void pageChange(final int newPageIndex) {
		super.pageChange(newPageIndex);
		getSite().getPage().getNavigationHistory().markLocation(this);
	}

	@Override
	public INavigationLocation createEmptyNavigationLocation() {
		return null;
	}

	@Override
	public INavigationLocation createNavigationLocation() {
		return new FBTypeNavigationLocation(this);
	}

	@Override
	public void updateEditorInput(final FileEditorInput newInput) {
		setInput(newInput);
		setTitleToolTip(newInput.getFile().getFullPath().toOSString());
	}
}
