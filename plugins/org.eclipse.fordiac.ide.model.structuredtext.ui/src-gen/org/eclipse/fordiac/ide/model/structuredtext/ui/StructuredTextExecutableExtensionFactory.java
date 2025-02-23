/*
 * generated by Xtext 2.25.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.ui;

import com.google.inject.Injector;
import org.eclipse.fordiac.ide.model.structuredtext.ui.internal.StructuredtextActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class StructuredTextExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return FrameworkUtil.getBundle(StructuredtextActivator.class);
	}
	
	@Override
	protected Injector getInjector() {
		StructuredtextActivator activator = StructuredtextActivator.getInstance();
		return activator != null ? activator.getInjector(StructuredtextActivator.ORG_ECLIPSE_FORDIAC_IDE_MODEL_STRUCTUREDTEXT_STRUCTUREDTEXT) : null;
	}

}
