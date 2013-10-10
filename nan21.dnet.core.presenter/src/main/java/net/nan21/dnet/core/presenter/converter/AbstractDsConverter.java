/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.presenter.converter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import net.nan21.dnet.core.api.action.result.IDsConverter;
import net.nan21.dnet.core.api.annotation.RefLookups;
import net.nan21.dnet.core.presenter.AbstractPresenterBase;
import net.nan21.dnet.core.presenter.descriptor.DsDescriptor;

public abstract class AbstractDsConverter<M, E> extends AbstractPresenterBase
		implements IDsConverter<M, E> {

	final static Logger logger = LoggerFactory
			.getLogger(AbstractDsConverter.class);

	/**
	 * Parser to resolve data-source model field -> entity path expressions.
	 */
	private ExpressionParser parser;

	/**
	 * Data-source model descriptor
	 */
	private DsDescriptor<M> descriptor;

	/**
	 * Reference lookups resolver for the model to entity conversion.
	 */
	private ReflookupResolver<M, E> resolver;

	private Class<M> modelClass;

	private Class<E> entityClass;

	public AbstractDsConverter() {
		this.parser = new SpelExpressionParser();
	}

	/**
	 * Populate model fields from entity according to mappings.
	 */
	@Override
	public void entityToModel(E e, M m, EntityManager em) throws Exception {

		StandardEvaluationContext context = new StandardEvaluationContext(e);
		Map<String, String> refpaths = this.descriptor.getE2mConv();
		Method[] methods = this.getModelClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")
					&& !method.getName().equals("set__clientRecordId__")) {
				String fn = StringUtils.uncapitalize(method.getName()
						.substring(3));
				try {
					method.invoke(m, parser.parseExpression(refpaths.get(fn))
							.getValue(context));
				} catch (Exception exc) {

				}
			}
		}
	}

	/**
	 * Update entity fields with values from the model according to mappings.
	 */
	@Override
	public void modelToEntity(M m, E e, boolean isInsert, EntityManager em)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Model-to-entity conversion: {} -> {} ",
					new String[] { this.modelClass.getSimpleName(),
							this.entityClass.getSimpleName() });
		}

		this.modelToEntityAttributes(m, e, isInsert, em);
		this.modelToEntityReferences(m, e, isInsert, em);
	}

	/**
	 * Update entity attribute fields.
	 * 
	 * @param m
	 * @param e
	 * @param isInsert
	 * @param entityService
	 * @throws Exception
	 */
	protected void modelToEntityAttributes(M m, E e, boolean isInsert,
			EntityManager em) throws Exception {
		StandardEvaluationContext context = new StandardEvaluationContext(m);
		Map<String, String> attrmap = this.descriptor.getM2eConv();
		Method[] methods = this.getEntityClass().getMethods();

		List<String> noInserts = this.descriptor.getNoInserts();
		List<String> noUpdates = this.descriptor.getNoUpdates();

		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				String fn = StringUtils.uncapitalize(method.getName()
						.substring(3));
				boolean doit = true;
				if (attrmap.containsKey(fn)) {
					String dsf = attrmap.get(fn);
					if (isInsert && noInserts.contains(fn)) {
						doit = false;
					}
					if (!isInsert && noUpdates.contains(fn)) {
						doit = false;
					}

					try {
						if (doit) {
							method.invoke(e, parser.parseExpression(dsf)
									.getValue(context));
						}
					} catch (Exception exc) {

					}
				}
			}
		}
	}

	/**
	 * Update entity reference fields.
	 * 
	 * @param m
	 * @param e
	 * @param isInsert
	 * @param entityService
	 * @throws Exception
	 */
	protected void modelToEntityReferences(M m, E e, boolean isInsert,
			EntityManager em) throws Exception {
		if (this.getModelClass().isAnnotationPresent(RefLookups.class)) {

			if (this.resolver == null) {
				this.resolver = new ReflookupResolver<M, E>(this.modelClass,
						this.entityClass);
			}

			this.resolver.setApplicationContext(this.getApplicationContext());
			this.resolver.execute(m, e, isInsert, em);
		}
	}

	/**
	 * Get the data-source class.
	 * 
	 * @return
	 */
	public Class<M> getModelClass() {
		return this.modelClass;
	}

	/**
	 * Set the data-source class.
	 * 
	 * @param clazz
	 */
	public void setModelClass(Class<M> clazz) {
		this.modelClass = clazz;
	}

	/**
	 * Get the entity class.
	 * 
	 * @return
	 */
	public Class<E> getEntityClass() {
		return entityClass;
	}

	/**
	 * Set the entity class.
	 * 
	 * @param entityClass
	 */
	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Set the data-source descriptor.
	 * 
	 * @param descriptor
	 */
	public void setDescriptor(DsDescriptor<M> descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Get the data-source descriptor.
	 * 
	 * @return
	 */
	public DsDescriptor<M> getDescriptor() {
		return descriptor;
	}

}
