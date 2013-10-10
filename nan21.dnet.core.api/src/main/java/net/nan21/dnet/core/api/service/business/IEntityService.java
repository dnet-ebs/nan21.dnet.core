/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.service.business;

/**
 * Base interface to be implemented by business services. A business service or
 * entity service is the entry point to the transactional business logic of the
 * application.
 * 
 * By default it is expected to be an entity service for each entity to provide
 * two types of functionalities:
 * <ul>
 * <li>Repository features: finder methods</li>
 * <li>Transactional functionality to insert, update and delete data as well as
 * various business functions.</li>
 * </ul>
 * 
 * This interface declares some of the generic methods for the aforementioned
 * ones. Specific methods should be declared in the entity specific interface
 * which should extend this.
 * 
 * 
 * 
 * @author amathe
 * 
 * @param <E>
 */
public interface IEntityService<E> extends IEntityWriteService<E> {

}