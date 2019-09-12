package com.wallstcn.cep.domain;

//另一个用于存放，经过空气预警类型。
public class AirWarningTypeRecoder {
    private String city;
    private String wtype;
    private Integer first;
    private Integer second;
    @Override
    public String toString() {
        return "AirWarningTypeRecoder{" +
                "city='" + city + '\'' +
                ", wtype='" + wtype + '\'' +
                ", first=" + first +
                ", second=" + second +
                '}';
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWtype() {
        return wtype;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype;
    }
}
