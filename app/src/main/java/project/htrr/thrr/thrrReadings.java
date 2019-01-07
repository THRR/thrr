package project.htrr.thrr;

import java.security.Timestamp;
import java.util.Date;

public class thrrReadings {
    private double temperature = -273;
    private int hr = -1;
    private long timestamp;
    private Date tm;

    thrrReadings() {

    }

    thrrReadings(long timestamp, double temperature, int hr) {

        this.temperature = temperature;
        this.hr = hr;
        this.tm = new Date((long) timestamp);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setTimestamp(long timestamp) {
        this.tm = new Date((long) timestamp);
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getHr() {
        return this.hr;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String PrintMe() {
        return "temperature: " + this.temperature + "hr:" + this.hr +
                "timestamp: " + this.tm + "  ";
    }

    @Override
    public String toString() {

        if (this.hr == -1) {
            return "temperature: " + this.temperature + "\n" + "timestamp: " + this.tm + "  ";

        } else if (this.temperature == -273) {
            return "hr:" + this.hr +
                    "\n" + "timestamp: " + this.tm + "  ";
        } else {

            return "temperature: " + this.temperature + "\n" + "hr:" + this.hr +
                    "\n" + "timestamp: " + this.tm + "  ";
        }
    }
}