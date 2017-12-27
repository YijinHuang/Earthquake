package service;

import model.QuakesEntity;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                                            attrs[1].replace("\"", ""),
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

    public Stream<QuakesEntity> restrictByUTCDateRange(String fromDateString, String toDateString, Stream<QuakesEntity> stream) throws ParseException {
        return restrictByUTCDateRange(format.parse(fromDateString), format.parse(toDateString), stream);
    }

    public Stream<QuakesEntity> restrictByUTCDateRange(Date fromDate, Date toDate, Stream<QuakesEntity> stream) {
        if (stream == null) {
            stream = quakes.stream();
        }
        stream = stream.filter(e -> {
            boolean statisfied = false;
            try {
                Date date = format.parse(e.getUtcDate());
                statisfied = date.after(fromDate) && date.before(toDate) || date.equals(fromDate) || date.equals(toDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return statisfied;
        });
        return stream;
    }

    public Stream<QuakesEntity> restrictByMagnitudeRange(double fromMagnitude, double toMagnitude, Stream<QuakesEntity> stream) {
        if (stream == null) {
            stream = quakes.stream();
        }
        stream = stream.filter(e -> {
            double magnitude = e.getMagnitude();
            return magnitude >= fromMagnitude && magnitude <= toMagnitude;
        });
        return stream;
    }

    public Stream<QuakesEntity> restrictByRegion(String region, Stream<QuakesEntity> stream) {
        if (stream == null) {
            stream = quakes.stream();
        }
        stream = stream.filter(e -> e.getRegion().equals(region));
        return stream;
    }

    public List<QuakesEntity> getAll() {
        return quakes;
    }

    public List<QuakesEntity> query(Stream<QuakesEntity> stream) {
        return stream.collect(Collectors.toList());
    }

//    public static void main(String[] args) {
//        DataService dataService = new DataService();
//        Date from = null;
//        Date to = null;
//        try {
//            from = dataService.format.parse("2017-10-15 00:53:27.0");
//            to = dataService.format.parse("2017-10-15 06:36:20.2");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        dataService.init();
//        System.out.println(dataService.getAll().size());
//        System.out.println(dataService.query(dataService.restrictByMagnitudeRange(1.0, 4.0, null)).size());
//        Stream stream = dataService.restrictByMagnitudeRange(1.0, 4.0, null);
//        Stream stream1 = dataService.restrictByUTCDateRange(from, to, stream);
//        System.out.println(dataService.query(stream1).size());
//    }
}
