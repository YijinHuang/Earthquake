package model;

import javax.persistence.*;

/**
 *  @author 黄义劲
 *  The class to represents the earthquakes table in the sqlite database
 */
@Entity
@Table(name = "quakes", schema = "main")
public class QuakesEntity {
    private int id;
    private String utcDate;
    private double latitude;
    private double longitude;
    private int depth;
    private double magnitude;
    private String region;
//    private int areaId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return UTC Date String of earthquake in format yyyy-MM-dd HH:mm:ss.S
     */
    @Basic
    @Column(name = "UTC_date", nullable = false)
    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    /**
     * @return latitude of earthquake
     */
    @Basic
    @Column(name = "latitude", nullable = false, precision = 0)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return longtitude of earthquake
     */
    @Basic
    @Column(name = "longitude", nullable = false, precision = 0)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return depth of earthquake
     */
    @Basic
    @Column(name = "depth", nullable = false)
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return magnitude of earthquake
     */
    @Basic
    @Column(name = "magnitude", nullable = false, precision = 0)
    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * @return region of earthquake
     */
    @Basic
    @Column(name = "region", nullable = true, length = 100)
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

//    @Basic
//    @Column(name = "area_id", nullable = true)
//    public int getAreaId() {
//        return areaId;
//    }
//
//    public void setAreaId(int areaId) {
//        this.areaId = areaId;
//    }

    /**
     * judge if two earthquakes are the same
     * @param o QuakesEntity
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuakesEntity that = (QuakesEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (depth != that.depth) return false;
        if (Double.compare(that.magnitude, magnitude) != 0) return false;
//        if (areaId != that.areaId) return false;
        if (utcDate != null ? !utcDate.equals(that.utcDate) : that.utcDate != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (utcDate != null ? utcDate.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + depth;
        temp = Double.doubleToLongBits(magnitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (region != null ? region.hashCode() : 0);
//        result = 31 * result + areaId;
        return result;
    }
}
