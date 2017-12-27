package service;

import model.QuakesEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 *  @author 黄义劲
 *  The class to update sqlite by crawler
 */
public class UpdateService{
    private final String URL_PREFIX = "https://www.emsc-csem.org/Earthquake/?view=";

    private int latestId;
    private Transaction transaction;
    private Session session;
    private int count = 0;

    /**
     * fetch a session and get the lastest earthquake id
     * @param session the session from DataService
     */
    public UpdateService(Session session) {
        this.session = session;
        Query query = session.createQuery("select max(quake.id) from QuakesEntity as quake");
        this.latestId = (int) query.list().get(0);
    }

    /**
     * Get the pages which contain new earthquake need to update and scrape these pages
     */
    public void update() {
        int i = 0;
        count = 0;
        System.out.println("begin update!");
        while (true) {
            i++;
            if (!check(i)) {
                Query query = session.createQuery("select max(quake.id) from QuakesEntity as quake");
                this.latestId = (int) query.list().get(0);

                System.out.println("updated " + count + " earthquakes, latest id is " + latestId + ".");
                break;
            }
        }
    }

    /**
     * Check if this page contains new earthquake
     * @param pageNum the number of page
     * @return if need to scrape
     */
    private boolean check(int pageNum) {
        Document doc;
        Element tbody;
        Elements trs;
        String id;
        try {
            doc = Jsoup.connect(URL_PREFIX + pageNum).get();
            tbody = doc.select("#tbody").get(0);
            trs = tbody.select("tr");
            for (Element tr : trs) {
                id = tr.id();
                if (!id.contains("tr")) {
                    if (Integer.parseInt(id) > latestId) {
                        crawler(doc);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * scrape the earthquakes in this page and update to the sqlite database
     * @param doc the content of page
     */
    private void crawler(Document doc) {
        Element tbody;
        Elements trs;
        Elements tabev1, tabev2;
        String id, time, latitude, longtitude, depth, magnitude, region, direction;

        transaction = session.beginTransaction();

        tbody = doc.select("#tbody").get(0);
        trs = tbody.select("tr");
        for (Element tr : trs) {
            id = tr.id();
            if (!id.contains("tr")) {
                if (Integer.parseInt(id) > latestId) {
                    QuakesEntity quake = new QuakesEntity();
                    tabev1 = tr.select(".tabev1");
                    tabev2 = tr.select(".tabev2");

                    time = tr.select(".tabev6 b a").get(0).text();

                    latitude = tabev1.get(0).text();
                    direction = tabev2.get(0).text();
                    if (!direction.equals("N")) {
                        latitude = "-" + latitude;
                    }

                    longtitude = tabev1.get(1).text();
                    direction = tabev2.get(1).text();
                    if (!direction.equals("E")) {
                        longtitude = "-" + longtitude;
                    }

                    depth = tr.select(".tabev3").get(0).text();

                    magnitude = tabev2.get(2).text();

                    region = tr.select(".tb_region").get(0).text();

                    quake.setId(Integer.parseInt(id));
                    quake.setUtcDate(time);
                    quake.setLatitude(Double.parseDouble(latitude));
                    quake.setLongitude(Double.parseDouble(longtitude));
                    quake.setDepth(Integer.parseInt(depth));
                    quake.setMagnitude(Double.parseDouble(magnitude));
                    quake.setRegion(region);

                    try {
                        session.save(quake);
                        count++;
                    } catch (org.hibernate.NonUniqueObjectException e) {
                        System.out.println(quake.getId() + " already exists!");
                    }
                } else {
                    break;
                }
            }
        }
        transaction.commit();
    }
}
