package fr.utbm.gl52.netbusmanager.dao;

import java.util.List;

import javax.persistence.Query;

import fr.utbm.gl52.netbusmanager.entity.Route;
import fr.utbm.gl52.netbusmanager.entity.Trip;

/**
 * @author bright
 *
 */
public class TripDao extends AbstractDao<Trip> {

    /**
     * @return all saved trips for the route
     */
    public List<Trip> getAll() {
        return entityManager.createQuery("select t from Trip t order by t.route.identifier, t.direction").getResultList();
    }

    /**
     * @param route the to find trips
     * @return all saved trips for the route
     */
    public List<Trip> getAllByRoute(Route route) {
        Query q = entityManager.createQuery("select t from Trip t where t.route = :route order by t.direction");
        q.setParameter("route", route);

        return q.getResultList();
    }

    /**
     * @param id the id of the trip to find
     * @return the searched trip
     */
    public Trip getById(Integer id) {
        return entityManager.find(Trip.class, id);
    }

}
