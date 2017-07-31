package com.kachkovsky.busyhistory.component.table;

import javafx.scene.control.*;

import java.time.LocalDate;

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
        this.picker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(isEditing()) {
                commitEdit(newValue);
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            this.picker.setValue(item);
            this.setGraphic(this.picker);
        }
    }
}