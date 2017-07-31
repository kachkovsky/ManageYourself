package com.kachkovsky.busyhistory.component.table;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;

import java.time.LocalDate;

/**
 * https://stackoverflow.com/questions/23075139/datepicker-in-javafx-tablecell
 * @param <T>
 */
public class LocalDatePickerTableCell<T> extends TableCell<T, LocalDate> {
    private final DatePicker picker;

    public LocalDatePickerTableCell(TableColumn<T, LocalDate> column) {
        this.picker = new DatePicker();
        this.picker.editableProperty().bind(column.editableProperty());
        this.picker.disableProperty().bind(column.editableProperty().not());
        this.picker.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });
//        this.picker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("----");
//            System.out.println(oldValue);
//            System.out.println(newValue);
//            if (isEditing()) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        System.out.println("editing");
//
//                        commitEdit(oldValue);
//                    }
//                });
//
//            }
//        });
        this.picker.setOnAction(evt -> {
            //System.out.println("xxx");
            LocalDate value = this.picker.getValue();
            //System.out.println(value);
                    if (isEditing()) {
                        Platform.runLater(() -> {
                            //System.out.println("xxx1");
                            commitEdit(value);
                        });
                    }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

//    @Override
//    public void commitEdit(LocalDate item) {
//
//        // This block is necessary to support commit on losing focus, because the baked-in mechanism
//        // sets our editing state to false before we can intercept the loss of focus.
//        // The default commitEdit(...) method simply bails if we are not editing...
//        if (! isEditing() && ! item.equals(getItem())) {
//            TableView<T> table = getTableView();
//            if (table != null) {
//                TableColumn<T, LocalDate> column = getTableColumn();
//                CellEditEvent<T, LocalDate> event = new CellEditEvent<>(table,
//                        new TablePosition<T,LocalDate>(table, getIndex(), column),
//                        TableColumn.editCommitEvent(), item);
//                Event.fireEvent(column, event);
//            }
//        }
//
//        super.commitEdit(item);
//
//        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            this.picker.setValue(item);
            this.setGraphic(this.picker);
        }
    }
}