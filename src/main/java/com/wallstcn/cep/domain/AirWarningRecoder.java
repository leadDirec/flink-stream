package com.wallstcn.cep.domain;

//一个用于存放前后数据的对比记录
public class AirWarningRecoder {
    private String city;
    private AirQualityRecoder first;
    private AirQualityRecoder second;
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AirQualityRecoder getFirst() {
        return first;
    }

    public void setFirst(AirQualityRecoder first) {
        this.first = first;
    }

    public AirQualityRecoder getSecond() {
        return second;
    }

    public void setSecond(AirQualityRecoder second) {
        this.second = second;
    }

    public AirWarningRecoder(AirQualityRecoder first, AirQualityRecoder second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "AirWarningRecoder{" +
                "city='" + city + '\'' +
                ", first=" + first +
                ", second=" + second +
                '}';
    }

    public AirWarningRecoder(String city, AirQualityRecoder first, AirQualityRecoder second) {
        this.city = city;
        this.first = first;
        this.second = second;
    }

}
