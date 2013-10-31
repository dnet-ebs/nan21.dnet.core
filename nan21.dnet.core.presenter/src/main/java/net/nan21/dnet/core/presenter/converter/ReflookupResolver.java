/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import net.nan21.dnet.core.api.annotation.DsField;
import net.nan21.dnet.core.api.annotation.Param;
import net.nan21.dnet.core.api.annotation.RefLookup;
import net.nan21.dnet.core.api.annotation.RefLookups;
import net.nan21.dnet.core.api.exceptions.BusinessException;
import net.nan21.dnet.core.api.model.IModelWithId;
import net.nan21.dnet.core.presenter.AbstractPresenterBase;

/**
 * Delegate class to update the entity references from the the data-source
 * model, based on the {@link RefLookup} annotations at the data-source model.
 * 
 * @author amathe
 * 
 * @param <M>
 * @param <E>
 */
public class ReflookupResolver<M, E> extends AbstractPresenterBase {

	/**
	 * Model (data-source) class
	 */
	private Class<M> modelClass;

	/**
	 * Entity class
	 */
	private Class<E> entityClass;

	public ReflookupResolver(Class<M> modelClass, Class<E> entityClass) {
		super();
		this.modelClass = modelClass;
		this.entityClass = entityClass;
	}

	public void execute(M ds, E e, boolean isInsert, EntityManager em)
			throws Exception {
		if (this.modelClass.isAnnotationPresent(RefLookups.class)) {

			RefLookup[] refLookups = this.modelClass.getAnnotation(
					RefLookups.class).value();

			for (RefLookup rl : refLookups) {
				this.doRefLookup1(ds, e, rl, isInsert, em);
			}
		}
	}

	/**
	 * Get the ds-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Field _getDsField(String fieldName) throws Exception {
		return ReflectionUtils.findField(this.modelClass, fieldName);
	}

	/**
	 * Get the getter for the ds-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Method _getDsGetter(String fieldName) throws Exception {
		return ReflectionUtils.findMethod(this.modelClass,
				"get" + StringUtils.capitalize(fieldName));
	}

	/**
	 * Get the setter for the ds-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Method _getDsSetter(String fieldName) throws Exception {
		return this.modelClass.getMethod(
				"set" + StringUtils.capitalize(fieldName),
				this._getDsField(fieldName).getType());
	}

	/**
	 * Get the value of the ds-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Object _getDsFieldValue(String fieldName, M ds) throws Exception {
		Method getter = this._getDsGetter(fieldName);
		return getter.invoke(ds, (Object[]) null);
	}

	/**
	 * Get the entity-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Field _getEntityField(String fieldName) throws Exception {
		return ReflectionUtils.findField(this.entityClass, fieldName);
	}

	/**
	 * Get the getter for the entity-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Method _getEntityGetter(String fieldName) throws Exception {
		return this.entityClass.getMethod("get"
				+ StringUtils.capitalize(fieldName));
	}

	/**
	 * Get the setter for the entity field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Method _getEntitySetter(String fieldName) throws Exception {
		return this.entityClass.getMethod(
				"set" + StringUtils.capitalize(fieldName), this
						._getEntityField(fieldName).getType());
	}

	/**
	 * Get the value of the entity-field with the given name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private Object _getEntityFieldValue(String fieldName, E e) throws Exception {
		Method getter = this._getEntityGetter(fieldName);
		return getter.invoke(e, (Object[]) null);
	}

	/**
	 * Do the actual work, update the reference field in the entity based on the
	 * ref-lookup rule.
	 * 
	 * @param m
	 * @param e
	 * @param refLookup
	 * @param isInsert
	 * @throws Exception
	 */
	private void doRefLookup1(M ds, E e, RefLookup refLookup, boolean isInsert,
			EntityManager em) throws Exception {

		String refIdDsFieldName = refLookup.refId();
		Object refIdDsFieldValue = this._getDsFieldValue(refIdDsFieldName, ds);
		String refEntityFieldName = null;
		Field refIdDsField = this._getDsField(refIdDsFieldName);

		if (!refIdDsField.isAnnotationPresent(DsField.class)) {
			throw new Exception(
					"Field "
							+ refIdDsFieldName
							+ " cannot be used as value for refId in @RefLookup annotation as it is not marked as @DsField ");
		} else {
			DsField dsFieldAnnotation = refIdDsField
					.getAnnotation(DsField.class);

			/* Obey the noInsert and noUpdate rules. */

			if ((isInsert && dsFieldAnnotation.noInsert())
					|| (!isInsert && dsFieldAnnotation.noUpdate())) {
				return;
			}

			String path = dsFieldAnnotation.path();
			if (path.indexOf('.') > 0) {
				// TODO: handle the deep references also ( a.b.c.id )
				refEntityFieldName = path.substring(0, path.indexOf('.'));
			} else {
				throw new Exception(
						"Field "
								+ refIdDsFieldName
								+ " cannot be used as value for refId in @RefLookup annotation as its path(`"
								+ path
								+ "`) in @DsField is not a reference path.");
			}
		}

		Class<?> refClass = this._getEntityField(refEntityFieldName).getType();
		Object ref = this._getEntityFieldValue(refEntityFieldName, e);
		Method setter = this._getEntitySetter(refEntityFieldName);

		if (refIdDsFieldValue != null && !"".equals(refIdDsFieldValue)) {
			/*
			 * if there is an ID now in DS which points to a different reference
			 * as the one in the original entity then update it to the new one
			 */
			if (ref == null
					|| !((IModelWithId) ref).getId().equals(refIdDsFieldValue)) {
				setter.invoke(e, em.find(refClass, refIdDsFieldValue));
			}
		} else {

			/*
			 * If there is no ID given in DS for the reference, try to lookup an
			 * entity based on the given named query which must be a query based
			 * on an unique-key. The given fields as parameters for the
			 * named-query must uniquely identify an entity.
			 */

			boolean shouldTryToFindReference = true;
			String namedQueryName = refLookup.namedQuery();

			Map<String, Object> values = new HashMap<String, Object>();
			Map<String, Object> namedQueryParams = new HashMap<String, Object>();

			if (namedQueryName == null || namedQueryName.equals("")) {
				shouldTryToFindReference = false;
			} else {
				for (Param p : refLookup.params()) {
					String paramName = p.name();
					String fieldName = p.field();
					String staticValue = p.value();

					Object fieldValue = null;
					if (staticValue != null && !"".equals(staticValue)) {
						fieldValue = staticValue;
					} else {
						fieldValue = this._getDsFieldValue(fieldName, ds);
					}

					if (fieldValue == null
							|| (fieldValue instanceof String && ((String) fieldValue)
									.equals(""))) {
						shouldTryToFindReference = false;
						break;
					} else {
						values.put(fieldName, fieldValue);
						namedQueryParams.put(paramName, fieldValue);
					}
				}
			}

			if (shouldTryToFindReference) {

				Object theReference = null;
				try {
					theReference = (findEntityService(refClass)).findByUk(
							namedQueryName, namedQueryParams);
				} catch (javax.persistence.NoResultException exception) {

					StringBuffer sb = new StringBuffer();

					for (Map.Entry<String, Object> entry : values.entrySet()) {
						sb.append(" `" + entry.getKey() + "` = `"
								+ entry.getValue() + "`");
					}

					throw new BusinessException("Cannot find  `"
							+ refClass.getSimpleName() + "` reference using "
							+ sb.toString() + " for data-source `"
							+ ds.getClass().getSimpleName() + "`");
				}
				setter.invoke(e, theReference);
				Method refIdFieldInDsSetter = this
						._getDsSetter(refIdDsFieldName);
				refIdFieldInDsSetter.invoke(ds,
						((IModelWithId) theReference).getId());
			} else {
				setter.invoke(e, (Object) null);
			}
		}
	}

}
