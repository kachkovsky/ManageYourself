package com.kachkovsky.busyhistory.component.table;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.function.Function;

/**
 * https://stackoverflow.com/questions/23075139/datepicker-in-javafx-tablecell
 *
 * @param <T>
 */
public class DatePickerCell<T> extends TableCell<T, LocalDate> {

    private DatePicker datePicker;
    private ObservableList<T> observableDate;
    private Function<T, ObjectProperty<LocalDate>> datePropertyFunction;

    public DatePickerCell(ObservableList<T> observableDate, Function<T, ObjectProperty<LocalDate>> datePropertyFunction) {

        super();
        this.datePropertyFunction = datePropertyFunction;
        this.observableDate = observableDate;

        if (datePicker == null) {
            createDatePicker();
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
            }
        });
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {

        super.updateItem(item, empty);

        SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");

        if (null == this.datePicker) {
            System.out.println("datePicker is NULL");
        }

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {

            if (isEditing()) {
                setContentDisplay(ContentDisplay.TEXT_ONLY);

            } else {
                datePicker.setValue(item);
                setText(smp.format(item));
                setGraphic(this.datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
        }
    }

    private void createDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setPromptText("jj/mm/aaaa");
        datePicker.setEditable(true);

        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                int index = getIndex();
                if (null != observableDate) {
                    datePropertyFunction.apply(observableDate.get(index)).set(datePicker.getValue());
                }
            }
        });

        setAlignment(Pos.CENTER);
    }

    @Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

//    public ObservableList<T> getDate() {
//        return date;
//    }
//
//    public void setDate(ObservableList<T> date) {
//        this.date = date;
//    }

//    public DatePicker getDatePicker() {
//        return datePicker;
//    }
//
//    public void setDatePicker(DatePicker datePicker) {
//        this.datePicker = datePicker;
//    }

}
