package com.tech.analysis.entity;

/**
 * Created by Administrator on 2018/3/24 0024.
 */
public class AddressTemp {

    private String uid;
    private String organization;
    private String suborganization;
    private String full_address;
    private String city;
    private String country;
    private String zip;
    private int addr_no;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSuborganization() {
        return suborganization;
    }

    public void setSuborganization(String suborganization) {
        this.suborganization = suborganization;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getAddr_no() {
        return addr_no;
    }

    public void setAddr_no(int addr_no) {
        this.addr_no = addr_no;
    }

    @Override
    public String toString() {
        return "AddressTemp{" +
                "uid='" + uid + '\'' +
                ", organization='" + organization + '\'' +
                ", suborganization='" + suborganization + '\'' +
                ", full_address='" + full_address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                ", addr_no=" + addr_no +
                '}';
    }
}
