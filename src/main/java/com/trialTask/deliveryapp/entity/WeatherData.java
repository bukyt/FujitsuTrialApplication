package com.trialTask.deliveryapp.entity;
import jakarta.persistence.*;

import java.sql.Timestamp;

/**
 * Entity for Weather table entries
 * ID - the id and primary key of the entries
 * NAME - The name of the station location
 * WMOCODE - The code of the Station
 * AIR - The temperature of the air at the location
 * WIND - The speed of the wind at the location
 * QUERYTIME - The timestamp of when the query was inserted into the database
 * PHENOMENON - The phenomenon present at the location when created.
 */
@Entity
public class WeatherData {
    @Id
    @Column(name="ID")
    int ID;
    @Column(name="NAME")
    String NAME;
    @Column(name="WMOCODE")
    int WMOCODE;
    @Column(name="AIR")
    float AIR;
    @Column(name="WIND")
    float WIND;
    @Column(name="QUERYTIME")
    Timestamp QUERYTIME;
    @Column(name="PHENOMENON")
    String PHENOMENON;

    public WeatherData(String NAME, int WMOCODE, float AIR, float WIND, Timestamp QUERYTIME, String PHENOMENON) {
        this.NAME = NAME;
        this.WMOCODE = WMOCODE;
        this.AIR = AIR;
        this.WIND = WIND;
        this.QUERYTIME = QUERYTIME;
        this.PHENOMENON = PHENOMENON;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "ID=" + ID +
                ", NAME='" + NAME + '\'' +
                ", WMOCODE=" + WMOCODE +
                ", AIR=" + AIR +
                ", WIND=" + WIND +
                ", QUERYTIME=" + QUERYTIME +
                ", PHENOMENON='" + PHENOMENON + '\'' +
                '}';
    }
    //Getter and setter methods for each of the entries

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getWMOCODE() {
        return WMOCODE;
    }

    public void setWMOCODE(int WMOCODE) {
        this.WMOCODE = WMOCODE;
    }

    public float getAIR() {
        return AIR;
    }

    public void setAIR(float AIR) {
        this.AIR = AIR;
    }

    public float getWIND() {
        return WIND;
    }

    public void setWIND(float WIND) {
        this.WIND = WIND;
    }

    public Timestamp getQUERYTIME() {
        return QUERYTIME;
    }

    public void setQUERYTIME(Timestamp QUERYTIME) {
        this.QUERYTIME = QUERYTIME;
    }

    public String getPHENOMENON() {
        return PHENOMENON;
    }

    public void setPHENOMENON(String PHENOMENON) {
        this.PHENOMENON = PHENOMENON;
    }
}
