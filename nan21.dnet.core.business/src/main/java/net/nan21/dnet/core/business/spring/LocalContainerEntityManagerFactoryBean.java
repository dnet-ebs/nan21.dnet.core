/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.business.spring;

public class LocalContainerEntityManagerFactoryBean extends
		org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean {
//
//	public LocalContainerEntityManagerFactoryBean() {
//		try {
//			if (Validator.isNotNull(PropsValues.JPA_LOAD_TIME_WEAVER)) {
//				Class<?> loadTimeWeaverClass = Class
//						.forName(PropsValues.JPA_LOAD_TIME_WEAVER);
//
//				LoadTimeWeaver loadTimeWeaver = (LoadTimeWeaver) loadTimeWeaverClass
//						.newInstance();
//
//				setLoadTimeWeaver(loadTimeWeaver);
//			}
//		} catch (Exception e) {
//			_log.error(e, e);
//
//			throw new RuntimeException(e);
//		}
//
//		setPersistenceXmlLocation("classpath*:META-INF/persistence-custom.xml");
//
//		PersistenceUnitPostProcessor[] persistenceUnitPostProcessors = { new LiferayPersistenceUnitPostProcessor() };
//
//		setPersistenceUnitPostProcessors(persistenceUnitPostProcessors);
//	}
//
//	@Override
//	public void setDataSource(DataSource dataSource) {
//		Database database = DatabaseDetector.determineDatabase(dataSource);
//
//		AbstractJpaVendorAdapter jpaVendorAdapter = null;
//
//		String provider = PropsValues.JPA_PROVIDER;
//
//		try {
//			Class<?> providerClass = getProviderClass(provider);
//
//			if (_log.isInfoEnabled()) {
//				_log.info("Using provider class " + providerClass.getName());
//			}
//
//			jpaVendorAdapter = (AbstractJpaVendorAdapter) providerClass
//					.newInstance();
//		} catch (Exception e) {
//			_log.error(e, e);
//
//			return;
//		}
//
//		String databasePlatform = PropsValues.JPA_DATABASE_PLATFORM;
//
//		if (provider.equalsIgnoreCase("eclipselink")
//				|| provider.equalsIgnoreCase("toplink")) {
//
//			if (databasePlatform == null) {
//				databasePlatform = getDatabasePlatform(database);
//			}
//
//			if (_log.isInfoEnabled()) {
//				_log.info("Using database platform " + databasePlatform);
//			}
//
//			jpaVendorAdapter.setDatabasePlatform(databasePlatform);
//		} else {
//			if (databasePlatform == null) {
//				jpaVendorAdapter.setDatabase(database);
//
//				if (_log.isInfoEnabled()) {
//					_log.info("Using database name " + database.toString());
//				}
//			} else {
//				jpaVendorAdapter
//						.setDatabase(Database.valueOf(databasePlatform));
//
//				if (_log.isInfoEnabled()) {
//					_log.info("Using database name " + databasePlatform);
//				}
//			}
//		}
//
//		setJpaVendorAdapter(jpaVendorAdapter);
//
//		super.setDataSource(dataSource);
//	}
//
//	protected String getDatabasePlatform(Database database) {
//		String databasePlatform = null;
//
//		String packageName = null;
//
//		boolean eclipseLink = false;
//
//		if (PropsValues.JPA_PROVIDER.equalsIgnoreCase("eclipselink")) {
//			packageName = "org.eclipse.persistence.platform.database.";
//
//			eclipseLink = true;
//		} else {
//			packageName = "oracle.toplink.essentials.platform.database.";
//		}
//
//		if (database.equals(Database.DB2)) {
//			databasePlatform = packageName + "DB2Platform";
//		} else if (database.equals(Database.DERBY)) {
//			databasePlatform = packageName + "DerbyPlatform";
//		} else if (database.equals(Database.HSQL)) {
//			databasePlatform = packageName + "HSQLPlatform";
//		} else if (database.equals(Database.INFORMIX)) {
//			databasePlatform = packageName + "InformixPlatform";
//		} else if (database.equals(Database.MYSQL)) {
//			if (eclipseLink) {
//				databasePlatform = packageName + "MySQLPlatform";
//			} else {
//				databasePlatform = packageName + "MySQL4Platform";
//			}
//		} else if (database.equals(Database.ORACLE)) {
//			if (eclipseLink) {
//				databasePlatform = packageName + "OraclePlatform";
//			} else {
//				databasePlatform = packageName + "oracle.OraclePlatform";
//			}
//		} else if (database.equals(Database.POSTGRESQL)) {
//			databasePlatform = packageName + "PostgreSQLPlatform";
//		} else if (database.equals(Database.SQL_SERVER)) {
//			databasePlatform = packageName + "SQLServerPlatform";
//		} else if (database.equals(Database.SYBASE)) {
//			databasePlatform = packageName + "SybasePlatform";
//		} else {
//			_log.error("Unable to detect database platform for \""
//					+ database.toString() + "\". Override by configuring the "
//					+ "\"jpa.database.platform\" property.");
//		}
//
//		return databasePlatform;
//	}
//
//	protected Class<?> getProviderClass(String provider) throws Exception {
//		if (provider.equalsIgnoreCase("eclipselink")) {
//			return EclipseLinkJpaVendorAdapter.class;
//		} else if (provider.equalsIgnoreCase("hibernate")) {
//			return HibernateJpaVendorAdapter.class;
//		} else if (provider.equalsIgnoreCase("openjpa")) {
//			return OpenJpaVendorAdapter.class;
//		} else if (provider.equalsIgnoreCase("toplink")) {
//			return TopLinkJpaVendorAdapter.class;
//		}
//
//		return null;
//	}
//
//	private static Log _log = LogFactoryUtil
//			.getLog(LocalContainerEntityManagerFactoryBean.class);

}