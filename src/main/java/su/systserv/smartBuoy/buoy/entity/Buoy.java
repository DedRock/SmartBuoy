package su.systserv.smartBuoy.buoy.entity;


import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.BuoyType;
import su.systserv.smartBuoy.buoy.annotations.BuoySetting;
import su.systserv.smartBuoy.buoy.annotations.BuoySimpleInfo;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Класс описывающий сущность "Буй"
 */
@Entity
@Table(name="buoy")
public class Buoy {

    private static final Logger log = Logger.getLogger(Buoy.class.getName());

    @Id
    @Column(name="mmsi")
    @BuoySimpleInfo
    private Integer mmsi;
    @BuoySetting
    @Column(name="name")
    @BuoySimpleInfo
    private String name;
    @BuoySetting
    @Column(name="simNumber")
    @BuoySimpleInfo
    private Long simNumber; // номер SSI-карты
    @Column(name="buoyTypeId")
    @BuoySimpleInfo
    private Byte buoyTypeId; // тип буя

    // Coordinates
    @Column(name="curLatitude")
    private String curLatitude; // текущая широта
    @Column(name="curLongtitude")
    private String curLongtitude; // текущая долгота
    @BuoySetting
    @Column(name="defaultLatitude")
    private String defaultLatitude; // заданноая широта
    @BuoySetting
    @Column(name="defailtLongtitude")
    private String defailtLongtitude; // заданноая долгота

    // Buoy's energy parameters
    @Column(name="voltage")
    private Float voltage; // текущее напряжение
    @Column(name="batteryResource")
    private Byte batteryResource; // Запас по времени работы батареи, дни
    @Column(name="batteryNeedToChange")
    private Boolean batteryNeedToChange; // Флаг требования замены батареи

    //Параметры световой сигнализаци
    @Column(name="flushDuration")
    private Byte flushDuration; // длительность вспышек
    @Column(name="flashPause")
    private Byte flashPause; //промежуток между вспышками
    @Column(name="flashNumberInSeries")
    private Byte flashNumberInSeries; // количество вспышек в серии
    @Column(name="flashStartTime")
    //@Temporal(value=TemporalType.TIME)
    private Timestamp flastStartTime; // начало серии вспышек по UTC
    @Column(name="flashSeriesInterval")
    private Byte flashSeriesInterval; // задержка между сериями
    @Column(name="flashBrightness")
    private Byte flashBrightness; // яркость вспышек

    @OneToOne(mappedBy = "buoy" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MeteoBuoy meteoBuoy;

    //===== Constaructors ============================================================
    public Buoy() {}
    public Buoy(int mmsi, String name, Long simNumber, byte buoyTypeId) {
        this.mmsi = mmsi;
        this.name = name;
        this.simNumber = simNumber;
        this.buoyTypeId = buoyTypeId;
    }

    //===== Getters =================================================================

    public Boolean getBatteryNeedToChange() {
        return batteryNeedToChange;
    }

    public Byte getBatteryResource() {
        return batteryResource;
    }

    public byte getBuoyTypeId() {
        return buoyTypeId;
    }

    public Byte getFlashBrightness() {
        return flashBrightness;
    }

    public Byte getFlashNumberInSeries() {
        return flashNumberInSeries;
    }

    public Byte getFlashPause() {
        return flashPause;
    }

    public Byte getFlashSeriesInterval() {
        return flashSeriesInterval;
    }

    public Timestamp getFlastStartTime() {
        return flastStartTime;
    }

    public Byte getFlushDuration() {
        return flushDuration;
    }

    public String getCurLatitude() {
        return curLatitude;
    }

    public String getCurLongtitude() {
        return curLongtitude;
    }

    public Long getSimNumber() {
        return simNumber;
    }

    public Float getVoltage() {
        return voltage;
    }

    public String getDefailtLongtitude() {
        return defailtLongtitude;
    }

    public String getDefaultLatitude() {
        return defaultLatitude;
    }

    public MeteoBuoy getMeteoBuoy() {
        return meteoBuoy;
    }

    public int getMMSI() {
        return mmsi;
    }

    public String getName() {
        return name;
    }

    //===== Setters =========================================================

    public void setBatteryNeedToChange(Boolean batteryNeedToChange) {
        this.batteryNeedToChange = batteryNeedToChange;
    }

    public void setBatteryResource(Byte batteryResource) {
        this.batteryResource = batteryResource;
    }

    public void setBouyType(byte buoyTypeId) {
        this.buoyTypeId = buoyTypeId;
    }

    public void setFlashBrightness(Byte flashBrightness) {
        this.flashBrightness = flashBrightness;
    }

    public void setFlashNumberInSeries(Byte flashNumberInSeries) {
        this.flashNumberInSeries = flashNumberInSeries;
    }

    public void setFlashPause(Byte flashPause) {
        this.flashPause = flashPause;
    }

    public void setFlashSeriesInterval(Byte flashSeriesInterval) {
        this.flashSeriesInterval = flashSeriesInterval;
    }

    public void setFlastStartTime(Timestamp flastStartTime) {
        this.flastStartTime = flastStartTime;
    }

    public void setFlushDuration(Byte flushDuration) {
        this.flushDuration = flushDuration;
    }

    public void setCurLatitude(String curLatitude) {
        this.curLatitude = curLatitude;
    }

    public void setCurLongtitude(String curLongtitude) {
        this.curLongtitude = curLongtitude;
    }

    public void setSimNumber(Long simNumber) {
        this.simNumber = simNumber;
    }

    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }

    public void setDefailtLongtitude(String defailtLongtitude) {
        this.defailtLongtitude = defailtLongtitude;
    }

    public void setDefaultLatitude(String defaultLatitude) {
        this.defaultLatitude = defaultLatitude;
    }

    public void setMeteoBuoy(MeteoBuoy meteoBuoy) {
        this.meteoBuoy = meteoBuoy;
    }

    public void setMMSI(int MMSI) {
        this.mmsi = MMSI;
    }

    public void setName(String name) {
        this.name = name;
    }



    //===== Serialization ==============================================================================================
    public JSONObject toJSON() throws JSONException{
        JSONObject result = new JSONObject();
        result = getSimpleInfo();
        log.debug("curLatitude = " + curLatitude);
        result.put("curLatitude", curLatitude);
        result.put("curLongtitude", curLongtitude);
        result.put("defaultLatitude", defaultLatitude);
        result.put("defailtLongtitude", defailtLongtitude);
        result.put("voltage", voltage);
        result.put("batteryResource", batteryResource);
        result.put("batteryNeedToChange", batteryNeedToChange);
        result.put("flushDuration", flushDuration);
        result.put("flashPause", flashPause);
        result.put("flashNumberInSeries", flashNumberInSeries);
        result.put("flashStartTime", flastStartTime);
        result.put("flashSeriesInterval", flashSeriesInterval);
        result.put("flashBrightness", flashBrightness);

        if ( buoyTypeId == BuoyType.METEO.getId() && meteoBuoy != null){
            result = meteoBuoy.toJSON(result);
        }
        log.info("toJSON" + result);
        return result;
    }

    /**
     * Получить список настраиваемых параметров + текущие значения
     * @return
     * @throws JSONException
     */
    public JSONObject getSettings() throws JSONException{
        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("defaultLatitude", defaultLatitude);
        result.put("defailtLongtitude", defailtLongtitude);




        return result;
    }

    /**
     * Получить основную информацию о буе в формате JSON
     * @return
     * @throws JSONException
     */
    public JSONObject getSimpleInfo() throws JSONException{
        JSONObject result = new JSONObject();
        result.put("mmsi", mmsi);
        result.put("name", name);
        result.put("simNumber", simNumber);
        result.put("buoyTypeId", buoyTypeId);
        log.info("getSimpleInfo" + result);
        return result;
    }
}
