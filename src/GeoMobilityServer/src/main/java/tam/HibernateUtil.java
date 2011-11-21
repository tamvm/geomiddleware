package tam;

import java.io.File;
import java.lang.reflect.Field;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;

/**
 * @author VoMinhTam
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			return new AnnotationConfiguration().configure(new File("hibernate.cfg.xml"))
					.buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() { 
		return sessionFactory;
	}

	public static Session getSession() {
		return getSessionFactory().openSession();
	}

	public static String getSQL(Criteria criteria) {
		try {
			CriteriaImpl c = (CriteriaImpl) criteria;
			SessionImpl s = (SessionImpl) c.getSession();
			SessionFactoryImplementor factory = (SessionFactoryImplementor) s
					.getSessionFactory();
			String[] implementors = factory.getImplementors(c
					.getEntityOrClassName());
			CriteriaLoader loader = new CriteriaLoader(
					(OuterJoinLoadable) factory
							.getEntityPersister(implementors[0]), factory, c,
					implementors[0], s.getEnabledFilters());
			Field f;
			f = OuterJoinLoader.class.getDeclaredField("sql");

			f.setAccessible(true);
			String sql = (String) f.get(loader);
			return sql;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
