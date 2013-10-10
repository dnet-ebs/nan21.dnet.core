/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.model;

public abstract class AbstractDsModel<E> {

	/**
	 * Required when inserting a list of objects to be able to identify the
	 * entity as there is no id yet.
	 */
	private E _entity_;

	/**
	 * Field to transport fake ID's generated on client-side (user interface).
	 * If more than one new record is sent to server to be inserted, the client
	 * doesn't know how to match the records received in response with those
	 * sent. This field is used to identify the result.
	 */
	private String __clientRecordId__;

	public AbstractDsModel() {
	}

	public AbstractDsModel(E e) {
		this._entity_ = e;
	}

	public E _getEntity_() {
		return this._entity_;
	}

	public void _setEntity_(E entity) {
		this._entity_ = entity;
	}

	public String get__clientRecordId__() {
		return __clientRecordId__;
	}

	public void set__clientRecordId__(String clientRecordId) {
		__clientRecordId__ = clientRecordId;
	}

}
