package service;

import model.QuakesEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Created by Gotcha on 2017/12/11.
@SuppressWarnings("deprecation")
public class DataService {
    private Configuration config = null;
    private SessionFactory sessionFactory = null;
    private Session session = null;
    private Transaction tx = null;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public void init() {
        config = new Configuration().configure("./hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }

    public void update() {
        UpdateService updateService = new UpdateService(session);
        updateService.update();
    }

    public QuakesEntity getById(int id) {
        return session.get(QuakesEntity.class, id);
    }

    public Criteria restrictByUTCDateRange(String fromDateString, String toDateString, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.gt("utcDate", fromDateString))
                .add(Restrictions.lt("utcDate", toDateString));
        return criteria;
    }

    public Criteria restrictByUTCDateRange(Date fromDate, Date toDate, Criteria criteria) {
        return restrictByUTCDateRange(format.format(fromDate), format.format(toDate), criteria);
    }

    public Criteria restrictByMagnitudeRange(double fromMagnitude, double toMagnitude, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.gt("magnitude", fromMagnitude))
                .add(Restrictions.lt("magnitude", toMagnitude));
        return criteria;
    }

    public Criteria restrictByRegion(String region, Criteria criteria) {
        if (criteria == null) {
            criteria = session.createCriteria(QuakesEntity.class);
        }
        criteria.add(Restrictions.eq("region", region));
        return criteria;
    }

    public List<QuakesEntity> getAll() {
        Query query = session.createQuery("from QuakesEntity");
        return query.list();
    }

    public List<QuakesEntity> query(Criteria criteria) {
        return criteria.list();
    }

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