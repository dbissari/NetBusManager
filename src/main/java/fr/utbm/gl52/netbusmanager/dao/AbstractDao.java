package fr.utbm.gl52.netbusmanager.dao;

import javax.persistence.EntityManager;

import fr.utbm.gl52.netbusmanager.util.EntityManagerUtil;

/**
 * @author bright
 *
 */
abstract class AbstractDao<T> {

    protected EntityManager entityManager;

    public AbstractDao() {
        entityManager = EntityManagerUtil.getEntityManager();
    }

    /**
     * @param route the object to save
     */
    public void save(T t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    /**
     * @param t the object to update, its id should be already in database
     */
    public void update(T t) {
        entityManager.getTransaction().begin();
        entityManager.merge(t);
        entityManager.getTransaction().commit();
    }

}
