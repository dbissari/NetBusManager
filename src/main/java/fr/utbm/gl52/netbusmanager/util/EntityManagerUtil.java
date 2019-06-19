package fr.utbm.gl52.netbusmanager.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author bright
 *
 */
public class EntityManagerUtil {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("nbmPU");
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }
}
