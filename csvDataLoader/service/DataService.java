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

/**
 * @author 黄义劲
 * The class to query the QuakesEntities by Stream operations and provide the quried QuakesEntities for Controller
 */
public class DataService {
    private BufferedReader reader = null;
    private ArrayList<QuakesEntity> quakes = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * Get the csv file and encapsulate the earthquakes to QuakesEntities.
     */
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

    /**
     * Add new date restriction to filter earthquakes
     * @param fromDateString the begin date string in format yyyy-MM-dd HH:mm:ss.S
     * @param toDateString the end date string in format yyyy-MM-dd HH:mm:ss.S
     * @param stream if stream is null then open a new stream else filter the stream
     * @return stream after filtering
     */
    public Stream<QuakesEntity> restrictByUTCDateRange(String fromDateString, String toDateString, Stream<QuakesEntity> stream) throws ParseException {
        return restrictByUTCDateRange(format.parse(fromDateString), format.parse(toDateString), stream);
    }

    /**
     * Add new date restriction to filter earthquakes
     * @param fromDate the begin date
     * @param toDate the end date
     * @param stream if stream is null then open a new stream else filter the stream
     * @return stream after filtering
     */
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

    /**
     * Add new date restriction to filter earthquakes
     * @param fromMagnitude the lower bound magnitude
     * @param toMagnitude the upper bound magnitude
     * @param stream if stream is null then open a new stream else filter the stream
     * @return stream after filtering
     */
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

    /**
     * Add new region restriction to filter earthquakes
     * @param region the region of earthquake
     * @param stream if stream is null then open a new stream else filter the stream
     * @return stream after filtering
     */
    public Stream<QuakesEntity> restrictByRegion(String region, Stream<QuakesEntity> stream) {
        if (stream == null) {
            stream = quakes.stream();
        }
        stream = stream.filter(e -> e.getRegion().equals(region));
        return stream;
    }

    /**
     * Get all earthquakes
     * @return List of earthquakes
     */
    public List<QuakesEntity> getAll() {
        return quakes;
    }

    /**
     * Generate List of earthquakes by stream after filtering
     * @param stream the stream after filtering
     * @return List of earthquakes after filtering
     */
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
