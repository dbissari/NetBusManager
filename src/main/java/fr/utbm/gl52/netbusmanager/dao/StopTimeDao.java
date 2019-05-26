/**
 *
 */
package fr.utbm.gl52.netbusmanager.dao;

import java.util.List;

import javax.persistence.Query;

import fr.utbm.gl52.netbusmanager.entity.Stop;
import fr.utbm.gl52.netbusmanager.entity.StopTime;
import fr.utbm.gl52.netbusmanager.entity.Trip;

/**
 * @author bright
 *
 */
public class StopTimeDao extends AbstractDao<StopTime> {

    /**
     * @param trip the to find stop times
     * @return all saved stop time for the directedRoute
     */
    public List<StopTime> getAllByTrip(Trip trip) {
        Query q = entityManager.createQuery("select st from StopTime st where st.trip = :trip order by st.stopSequence asc");
        q.setParameter("trip", trip);

        return q.getResultList();
    }

    /**
     * @param trip the to find stop times
     * @return all saved stop time for the directedRoute
     */
    public List<Stop> getAllStopsByTrip(Trip trip) {
        Query q = entityManager.createQuery("select st.stop from StopTime st where st.trip = :trip order by st.stopSequence asc");
        q.setParameter("trip", trip);

        return q.getResultList();
    }

    /**
     * @param stop the to find stop times
     * @return all saved stop time for the stop
     */
    public List<StopTime> getAllByStop(Stop stop) {
        Query q = entityManager.createQuery("select st from StopTime st where st.stop = :stop");
        q.setParameter("stop", stop);

        return q.getResultList();
    }

    /**
     * @param stop the to find stop times
     * @param trip the to find stop times
     * @return stop time for the stop and the directedRoute
     */
    public StopTime getByStopAndTrip(Stop stop, Trip trip) {
        Query q = entityManager.createQuery("select st from StopTime st where st.stop = :stop and st.trip = :trip");
        q.setParameter("stop", stop);
        q.setParameter("trip", trip);

        return (StopTime) q.getSingleResult();
    }

    /**
     * @param id the id of the stop time to find
     * @return the searched stop time
     */
    public StopTime getById(Integer id) {
        return entityManager.find(StopTime.class, id);
    }

}
