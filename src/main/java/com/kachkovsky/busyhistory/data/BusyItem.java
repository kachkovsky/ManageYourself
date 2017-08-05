package com.kachkovsky.busyhistory.data;

import com.kachkovsky.busyhistory.data.DecSequence.Holder;
import javafx.beans.property.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Entity
public class BusyItem {

    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty hours;
    private final SimpleStringProperty info;
    private int id;

    public BusyItem(LocalDate date, double hours, String info) {
        //this.id = Holder.INSTANCE.next();
        this.date = new SimpleObjectProperty<>(date);
        this.hours = new SimpleDoubleProperty(hours);
        this.info = new SimpleStringProperty(info);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date.get();
    }

    @Basic
    public long getDateMillis() {
        return getDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    public void setDateMillis(long longValue) {
        Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    @Basic
    public double getHours() {
        return hours.get();
    }

    public void setHours(double hours) {
        this.hours.set(hours);
    }

    @Basic
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
