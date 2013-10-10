package net.nan21.dnet.core.domain.eventhandler;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;

public class DefaultEventHandler extends DescriptorEventAdapter implements
		DescriptorCustomizer {

	public void customize(ClassDescriptor descriptor) {
	}

}
