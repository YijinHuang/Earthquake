package service;

import model.QuakesEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 黄义劲
 * The class to query the QuakesEntities by QBC and provide the quried QuakesEntities for Controller
 */
@SuppressWarnings("deprecation")
public class DataService {
    private Configuration config = null;
    private SessionFactory sessionFactory = null;
    private Session session = null;
    private Transaction tx = null;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * Get the configuration file of hibernate and open the global session to offer services
     */
    public void init() {
        config = new Configuration().configure("./hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    /**
     * Call the UpdateService to update sqlite data by crawler
     */
    public void update() {
        UpdateService updateService = new UpdateService(session);
        updateService.update();
    }

    /**
     * @param id earthquake unique id
     * @return unique earthquake
     */
    public QuakesEntity getById(int id) {
        return session.get(QuakesEntity.class, id);
    }

    /**
     * Add new date restriction to filter earthquakes
     * @param fromDateString the begin date string in format yyyy-MM-dd HH:mm:ss.S
     * @param toDateString the end date string in format yyyy-MM-dd HH:mm:ss.S
     * @param criteria if ctiteria is null then open a new criteria else append new restriction to the criteria
     * @return criteria with restrictions
     */
    public Criteria restrictByUTCDateRange(String fromDateString, String toDateString, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.gt("utcDate", fromDateString))
                .add(Restrictions.lt("utcDate", toDateString));
        return criteria;
    }

    /**
     * Add new date restriction to filter earthquakes
     * @param fromDate the begin date
     * @param toDate the end date
     * @param criteria if ctiteria is null then open a new criteria else append new restriction to the criteria
     * @return criteria with restrictions
     */
    public Criteria restrictByUTCDateRange(Date fromDate, Date toDate, Criteria criteria) {
        return restrictByUTCDateRange(format.format(fromDate), format.format(toDate), criteria);
    }

    /**
     * Add new magnitude restriction to filter earthquakes
     * @param fromMagnitude the lower bound magnitude
     * @param toMagnitude the upper bound magnitude
     * @param criteria if ctiteria is null then open a new criteria else append new restriction to the criteria
     * @return criteria with restrictions
     */
    public Criteria restrictByMagnitudeRange(double fromMagnitude, double toMagnitude, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.gt("magnitude", fromMagnitude))
                .add(Restrictions.lt("magnitude", toMagnitude));
        return criteria;
    }

    /**
     * Add new region restriction to filter earthquakes
     * @param region the region of earthquake
     * @param criteria if ctiteria is null then open a new criteria else append new restriction to the criteria
     * @return criteria with restrictions
     */
    public Criteria restrictByRegion(String region, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.eq("region", region));
        return criteria;
    }

    /**
     * Get all earthquakes
     * @return List of earthquakes
     */
    public List<QuakesEntity> getAll() {
        Query query = session.createQuery("from QuakesEntity");
        return query.list();
    }

    /**
     * Get earthquakes after filtering
     * @param criteria the restrictions to filter
     * @return List of earthquakes after filtering
     */
    public List<QuakesEntity> query(Criteria criteria) {
        if (criteria == null) {
            return getAll();
        } else {
            return criteria.list();
        }
    }

    /**
     * Close the session
     */
    public void close() {
        session.close();
    }

//    public static void main(String[] args) {
//        DataService dataService = new DataService();
//        Date from = null;
//        Date to = null;
//        try {
//            to = dataService.format.parse("2017-10-14 13:07:58.3");
//            from = dataService.format.parse("2017-10-02 11:20:26.0");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        dataService.init();
//        Criteria criteria = dataService.restrictByUTCDateRange(from, to, null);
//        List<QuakesEntity> list = dataService.query(dataService.restrictByMagnitudeRange(2, 3, criteria));
//        System.out.println(list.size());
//        dataService.close();
//    }
}