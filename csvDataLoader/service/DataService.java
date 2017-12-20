package service;

import model.QuakesEntity;

import javax.management.Query;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//Created by Gotcha on 2017/12/20.
public class DataService {
    private BufferedReader reader = null;
    private ArrayList<QuakesEntity> quakes = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public void init() {
        try {
            File file = new File("./src/data/earthquakes.csv");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String quakeString;
            String[] attrs;
            reader.readLine();
            while ((quakeString = reader.readLine()) != null) {
                attrs = quakeString.split(",");
                quakes.add(new QuakesEntity(Integer.parseInt(attrs[0]),
                                            attrs[1],
                                            Double.parseDouble(attrs[2]),
                                            Double.parseDouble(attrs[3]),
                                            Integer.parseInt(attrs[4]),
                                            Double.parseDouble(attrs[5]),
                                            attrs[6]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<QuakesEntity> getByUTCDate(String dataString) {
        return quakes.stream().filter(e -> e.getUtcDate().equals(dataString)).collect(Collectors.toList());
    }

    public List<QuakesEntity> getByUTCDateRange(String fromDateString, String toDateString) throws ParseException {
        return getByUTCDateRange(format.parse(fromDateString), format.parse(toDateString));
    }

    public List<QuakesEntity> getByUTCDate(Date date) {
        return getByUTCDate(format.format(date));
    }

    public List<QuakesEntity> getByUTCDateRange(Date fromDate, Date toDate) {
        return quakes.stream().filter(e -> {
            boolean statisfied = false;
            try {
                Date date = format.parse(e.getUtcDate());
                statisfied = date.after(fromDate) && date.before(toDate) || date.equals(fromDate) || date.equals(toDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return statisfied;
        }).collect(Collectors.toList());
    }

    public List<QuakesEntity> getByMagnitude(double magnitude) {
        return quakes.stream().filter(e -> e.getMagnitude() == magnitude).collect(Collectors.toList());
    }

    public List<QuakesEntity> getByMagnitudeRange(double fromMagnitude, double toMagnitude) {
        return quakes.stream().filter(e -> {
            double magnitude = e.getMagnitude();
            return magnitude >= fromMagnitude && magnitude <= toMagnitude;
        }).collect(Collectors.toList());
    }

    public List<QuakesEntity> getByRegion(String region) {
        return quakes.stream().filter(e -> e.getRegion().equals(region)).collect(Collectors.toList());
    }

    public List<QuakesEntity> getAll() {
        return quakes;
    }

//    public static void main(String[] args) {
//        DataService dataService = new DataService();
//        Date date = new Date();
//        dataService.init();
//        System.out.println(dataService.getAll().size());
//        System.out.println(dataService.getByMagnitude(2.5).size());
//        System.out.println(dataService.getByMagnitudeRange(1.0, 5.0).size());
//        System.out.println(dataService.getByUTCDate(date));
//    }
}
