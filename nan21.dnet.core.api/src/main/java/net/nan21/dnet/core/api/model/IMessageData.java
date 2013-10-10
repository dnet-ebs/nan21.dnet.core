/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.model;

import java.util.Map;

/**
 * Message data model. Services can fire events which can be handled by
 * event-handlers within the same bundle or different ones. This events are
 * published on the publish-subscribe model. Data is passed over in such an
 * event-data instance.
 * 
 * For example an entity service can fire an event on update so that other
 * services are notified and can react accordingly. In such a case the the event
 * would contain as source the entity canonical class name and as action
 * 'update'. Any other useful information (for example id's of updated entities
 * can be added in the data map)
 * 
 * @author amathe
 * 
 */
public interface IMessageData {

	public static String PRE_INSERT_ACTION = "pre-insert";
	public static String POST_INSERT_ACTION = "post-insert";

	public static String PRE_UPDATE_ACTION = "pre-update";
	public static String POST_UPDATE_ACTION = "post-update";

	public static String PRE_DELETE_ACTION = "pre-delete";
	public static String POST_DELETE_ACTION = "post-delete";

	/**
	 * A key identifying the source of the event. Usually it is a class name,
	 * but can be whatever string.
	 * 
	 */
	public abstract String getSource();

	public abstract void setSource(String source);

	/**
	 * An optional information regarding the source of the event, specifying the
	 * action which triggered this event. For example insert or update.
	 */
	public abstract String getAction();

	public abstract void setAction(String action);

	/**
	 * Any other information which may be useful for the listeners, for example
	 * a list of ID's.
	 */
	public abstract Map<String, Object> getData();

	public abstract void setData(Map<String, Object> data);

}