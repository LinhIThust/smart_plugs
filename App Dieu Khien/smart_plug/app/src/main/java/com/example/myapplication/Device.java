package com.example.myapplication;

public class Device {
    private int status;
    private String idDevice;
    private String nameDevice;

    public Device() {

    }

    public Device(int status, String idDevice, String nameDevice) {
        this.status = status;
        this.idDevice = idDevice;
        this.nameDevice = nameDevice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    @Override
    public String toString() {
        return "Device{" +
                "status=" + status +
                ", idDevice='" + idDevice + '\'' +
                ", nameDevice='" + nameDevice + '\'' +
                '}';
    }
}
