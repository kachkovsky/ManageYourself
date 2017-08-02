package com.kachkovsky.busyhistory.data;

import javafx.beans.property.*;

import java.time.LocalDate;

public class BusyItem {

    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty hours;
    private final SimpleStringProperty info;

    public BusyItem(LocalDate date, double hours, String info) {
        this.date = new SimpleObjectProperty<>(date);
        this.hours = new SimpleDoubleProperty(hours);
        this.info =   new SimpleStringProperty(info);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public double getHours() {
        return hours.get();
    }

    public void setHours(double hours) {
        this.hours.set(hours);
    }

    public String getInfo() {
        return info.get();
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public DoubleProperty hoursProperty() {
        return hours;
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }
}
