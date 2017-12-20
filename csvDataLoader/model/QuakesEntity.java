package model;

//Created by Gotcha on 2017/12/20.
public class QuakesEntity {
    private int id;
    private String utcDate;
    private double latitude;
    private double longitude;
    private int depth;
    private double magnitude;
    private String region;

    public QuakesEntity(int id,
                        String utcDate,
                        double latitude,
                        double longitude,
                        int depth,
                        double magnitude,
                        String region) {
        this.id = id;
        this.utcDate = utcDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.magnitude = magnitude;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
        return result;
    }
}
