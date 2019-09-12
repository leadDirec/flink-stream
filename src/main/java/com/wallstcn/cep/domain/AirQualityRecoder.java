package com.wallstcn.cep.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class AirQualityRecoder implements Serializable{
    private String id;
    private String city;
    private Integer airQuality;
    private Date emmit;
    private Long et;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(Integer airQuality) {
        this.airQuality = airQuality;
    }

    public Date getEmmit() {
        return emmit;
    }

    public void setEmmit(Date emmit) {
        this.emmit = emmit;
    }

    public Long getEt() {
        return et;
    }

    public void setEt(Long et) {
        this.et = et;
    }

    public AirQualityRecoder() {

    }

    public AirQualityRecoder(String city, Integer airQuality, Date emmit, Long et) {
        this.city = city;
        this.airQuality = airQuality;
        this.emmit = emmit;
        this.et = et;
    }

    @Override
    public String toString() {
        return "AirQualityRecoder{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", airQuality=" + airQuality +
                ", emmit=" + emmit +
                ", et=" + et +
                '}';
    }

    public static AirQualityRecoder createOne(){
        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] citys = new String[]{"天津","北京","上海","西安","深圳","广州"};
        AirQualityRecoder aqv = new AirQualityRecoder();
        Random r = new Random();
        aqv.setCity(citys[r.nextInt(6)]);
        aqv.setId(aqv.getCity());
        aqv.setAirQuality(r.nextInt(10));
        aqv.setEmmit(new Date());
        aqv.setEt(System.currentTimeMillis());
        return aqv;
    }
}
