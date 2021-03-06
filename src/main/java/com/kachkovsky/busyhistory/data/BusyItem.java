package com.kachkovsky.busyhistory.data;

import javafx.beans.property.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

@Entity
public class BusyItem {

    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty hours;
    private final SimpleStringProperty info;
    private int id;

    public BusyItem() {
        this(LocalDate.now(), 0., "");
    }

    public BusyItem(LocalDate date, double hours, String info) {
        this.date = new SimpleObjectProperty<>(date);
        this.hours = new SimpleDoubleProperty(hours);
        this.info = new SimpleStringProperty(info);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Transient
    public LocalDate getDate() {
        return date.get();
    }

    @Basic
    public long getDateMillis() {
        return getDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    public void setDateMillis(long longValue) {
        setDate(Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void setDate(LocalDate date) {
        if (Objects.equals(this.date.get(), date)) return;
        this.date.set(date);
    }

    @Basic
    public double getHours() {
        return hours.get();
    }

    public void setHours(double hours) {
        if (this.hours.get() == hours) return;
        this.hours.set(hours);
    }

    @Basic
    public String getInfo() {
        return info.get();
    }

    public void setInfo(String info) {
        if (Objects.equals(this.info.get(), info)) return;
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
