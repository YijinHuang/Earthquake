package Service;

import model.QuakesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Created by Gotcha on 2017/12/11.
public class DataService {
    Configuration config = null;
    SessionFactory sessionFactory = null;
    Session session = null;
    Transaction tx = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public void init(){
        config = new Configuration().configure("./hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }

    public QuakesEntity getById(int id) {
        QuakesEntity earthquake = session.get(QuakesEntity.class, id);
        return earthquake;
    }

    public List<QuakesEntity> getByAreaId(int areaId) {
        Query query = session.createQuery("from QuakesEntity where areaId=?");
        query.setParameter(0, areaId);
        return query.list();
    }

    public List<QuakesEntity> getByUTCDate(String dataString) {
        Query query = session.createQuery("from QuakesEntity where utcDate=?");
        query.setParameter(0, dataString);
        return query.list();
    }

    public List<QuakesEntity> getByUTCDate(Date date) {
        return getByUTCDate(format.format(date));
    }

    public List<QuakesEntity> getByMagnitude(double magnitude) {
        Query query = session.createQuery("from QuakesEntity where magnitude=?");
        query.setParameter(0, magnitude);
        return query.list();
    }

    public List<QuakesEntity> getByRegion(String region) {
        Query query = session.createQuery("from QuakesEntity where region=?");
        query.setParameter(0, region);
        return query.list();
    }

    public List<QuakesEntity> getAll() {
        Query query = session.createQuery("from QuakesEntity");
        return query.list();
    }

    public void close() {
        session.close();
    }
}
