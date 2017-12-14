package model;

import javax.persistence.*;

//Created by Gotcha on 2017/12/11.
@Entity
@Table(name = "quakes", schema = "main", catalog = "")
public class QuakesEntity {
    private int id;
    private String utcDate;
    private double latitude;
    private double longitude;
    private int depth;
    private double magnitude;
    private String region;
    private int areaId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UTC_date", nullable = false)
    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    @Basic
    @Column(name = "latitude", nullable = false, precision = 0)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = false, precision = 0)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "depth", nullable = false)
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Basic
    @Column(name = "magnitude", nullable = false, precision = 0)
    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    @Basic
    @Column(name = "region", nullable = true, length = 100)
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Basic
    @Column(name = "area_id", nullable = true)
    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

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
        if (areaId != that.areaId) return false;
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
        result = 31 * result + areaId;
        return result;
    }
}
