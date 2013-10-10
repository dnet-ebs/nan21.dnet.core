/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.descriptor;

import java.util.HashMap;
import java.util.Map;

public final class ViewModelDescriptorManager {

	private static Map<String, AbstractViewModelDescriptor<?>> store = new HashMap<String, AbstractViewModelDescriptor<?>>();

	@SuppressWarnings("unchecked")
	public static <M> DsDescriptor<M> getDsDescriptor(Class<M> modelClass,
			boolean useCache) throws Exception {

		if (useCache) {
			String key = modelClass.getCanonicalName();
			if (!store.containsKey(key)) {
				store.put(key, new DsDescriptor<M>(modelClass));
			}
			return (DsDescriptor<M>) store.get(key);
		} else {
			return new DsDescriptor<M>(modelClass);
		}

	}

	@SuppressWarnings("unchecked")
	public static <M> AsgnDescriptor<M> getAsgnDescriptor(Class<M> modelClass,
			boolean useCache) throws Exception {
		if (useCache) {
			String key = modelClass.getCanonicalName();
			if (!store.containsKey(key)) {
				store.put(key, new AsgnDescriptor<M>(modelClass));
			}
			return (AsgnDescriptor<M>) store.get(key);
		} else {
			return new AsgnDescriptor<M>(modelClass);
		}

	}

	public static void remove(AbstractViewModelDescriptor<?> descriptor) {
		store.remove(descriptor.getModelClass().getCanonicalName());
	}

}
