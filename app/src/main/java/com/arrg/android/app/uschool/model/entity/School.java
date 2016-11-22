package com.arrg.android.app.uschool.model.entity;

import java.io.Serializable;

/**
 * Created by Alberto on 13-Nov-16.
 */

public class School implements Serializable {

    private int nit;
    private String name;
    private byte[] logo;
    private String municipality;
    private String department;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String email;
    private String attention;
    private String emphasis;
    private String calendar;
    private String day;
    private String nature;
    private String grade;
    private int space;
    private int spaceAvailable;
    private float icfesAverage;
    private int icfesPosition;
    private float qualification;

    public School(int nit, String name, byte[] logo, String municipality, String department, String address, Double latitude, Double longitude, String phone, String email, String attention, String emphasis, String calendar, String day, String nature, String grade, int space, int spaceAvailable, float icfesAverage, int icfesPosition, float qualification) {
        this.nit = nit;
        this.name = name;
        this.logo = logo;
        this.municipality = municipality;
        this.department = department;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.attention = attention;
        this.emphasis = emphasis;
        this.calendar = calendar;
        this.day = day;
        this.nature = nature;
        this.grade = grade;
        this.space = space;
        this.spaceAvailable = spaceAvailable;
        this.icfesAverage = icfesAverage;
        this.icfesPosition = icfesPosition;
        this.qualification = qualification;
    }

    public int getNit() {
        return nit;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getEmphasis() {
        return emphasis;
    }

    public void setEmphasis(String emphasis) {
        this.emphasis = emphasis;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getSpaceAvailable() {
        return spaceAvailable;
    }

    public void setSpaceAvailable(int spaceAvailable) {
        this.spaceAvailable = spaceAvailable;
    }

    public float getIcfesAverage() {
        return icfesAverage;
    }

    public void setIcfesAverage(float icfesAverage) {
        this.icfesAverage = icfesAverage;
    }

    public int getIcfesPosition() {
        return icfesPosition;
    }

    public void setIcfesPosition(int icfesPosition) {
        this.icfesPosition = icfesPosition;
    }

    public float getQualification() {
        return qualification;
    }

    public void setQualification(float qualification) {
        this.qualification = qualification;
    }
}