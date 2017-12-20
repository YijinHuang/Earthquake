package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

//Created by Gotcha on 2017/12/18.
public class EarthQuakeCrawler extends Thread {
    private Document doc = null;
    private BufferedWriter writer;
    int pageNum;

    public EarthQuakeCrawler(int pageNum, BufferedWriter writer) {
        this.pageNum = pageNum;
        this.writer = writer;
    }

    @Override
    public void run() {
        Element tbody;
        Elements trs;
        Elements tabev1, tabev2;
        String id, time, latitude, longtitude, depth, magnitude, region, direction;

        try {
            doc = Jsoup.connect("https://www.emsc-csem.org/Earthquake/?view=" + pageNum).get();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        tbody = doc.select("#tbody").get(0);
        trs = tbody.select("tr");
        for (Element tr : trs) {
            id = tr.id();
            if (!id.contains("tr")) {
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

                try {
                    writer.write(id + ",");
                    writer.write(time + ",");
                    writer.write(latitude + ",");
                    writer.write(longtitude + ",");
                    writer.write(depth + ",");
                    writer.write(magnitude + ",");
                    writer.write(region + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        try {
            writer.flush();
            System.out.println("Finish page " + pageNum + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("./src/data/earthquakes.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("id,UTC_date,latitude,longitude,depth,magnitude,region\n");
            writer.flush();
            int pageAmount = 100;
            Thread[] threads = new Thread[pageAmount];
            for (int i = 1; i <= pageAmount; i++) {
                Thread thread = new EarthQuakeCrawler(i, writer);
                threads[i-1] = thread;
                thread.start();
                Thread.sleep(200);
            }
            for (Thread thread : threads) {
                thread.join();
            }
            System.out.println("Finish");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
