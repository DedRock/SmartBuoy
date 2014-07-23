package su.systserv.smartBuoy.buoy.entity;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Клаcc описания сущности "Метеорологический буй"
 */
@Entity
@Table(name="meteo_buoy")
public class MeteoBuoy {

    @Id
    @Column(name="mmsi")
    private int mmsi;

    @OneToOne
    @JoinColumn(name = "mmsi")
    private Buoy buoy;

    private float airTemp; //Темп возд
    private byte airHumidity; // Влажность воздуха
    private float windSpeed; // Скорость ветра
    private short windDirection; // Направление ветра
    private float waterTemp; // Темп воды
    private float waterFlowRate; // Скорость течения
    private short waterFlowDirection; // Направление течения

   //===== Constructor ================================================================
    public MeteoBuoy(){}

    public MeteoBuoy(int mmsi){
        this.mmsi = mmsi;
    }

    //===== Getters ===================================================================
    @Column(name="airHumidity")
    public byte getAirHumidity() {
        return airHumidity;
    }

    @Column(name="airTemp")
    public float getAirTemp() {
        return airTemp;
    }

    @Column(name="waterFlowDirection")
    public short getWaterFlowDirection() {
        return waterFlowDirection;
    }

    @Column(name="waterFlowRate")
    public float getWaterFlowRate() {
        return waterFlowRate;
    }

    @Column(name="waterTemp")
    public float getWaterTemp() {
        return waterTemp;
    }

    @Column(name="windDirection")
    public short getWindDirection() {
        return windDirection;
    }

    @Column(name="windSpeed")
    public float getWindSpeed() {
        return windSpeed;
    }

    //===== Setters ===================================================================

    public void setAirHumidity(byte airHumidity) {
        this.airHumidity = airHumidity;
    }

    public void setAirTemp(float airTemp) {
        this.airTemp = airTemp;
    }

    public void setWaterFlowDirection(short waterFlowDirection) {
        this.waterFlowDirection = waterFlowDirection;
    }

    public void setWaterFlowRate(float waterFlowRate) {
        this.waterFlowRate = waterFlowRate;
    }

    public void setWaterTemp(float waterTemp) {
        this.waterTemp = waterTemp;
    }

    public void setWindDirection(short windDirection) {
        this.windDirection = windDirection;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    //===== Serialization ==============================================================================================
    public JSONObject toJSON(JSONObject result) throws JSONException {
        //JSONObject result = new JSONObject();
        System.out.println("method toJSON of MeteoBuoy-Class");
        result.put("airTemp", airTemp);
        result.put("airHumidity", airHumidity);
        result.put("windSpeed", windSpeed);
        result.put("windDirection", windDirection);
        result.put("waterTemp", waterTemp);
        result.put("waterFlowRate", waterFlowRate);
        result.put("waterFlowDirection", waterFlowDirection);
        return result;
    }
}
