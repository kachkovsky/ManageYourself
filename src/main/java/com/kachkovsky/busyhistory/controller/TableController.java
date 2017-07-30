package com.kachkovsky.busyhistory.controller;

import com.kachkovsky.busyhistory.component.table.DatePickerCell;
import com.kachkovsky.busyhistory.data.BusyItem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable {
    private static final List<String> COMBO_BOX_OPTIONS = Arrays.asList(
            "0.15", "0.3", "0.45", "1",
            "1.15", "1.3", "1.45", "2",
            "2.15", "2.3", "2.45", "3",
            "3.15", "3.3", "3.45", "4",
            "4.15", "4.3", "4.45", "5",
            "5.15", "5.3", "5.45", "6",
            "6.15", "6.3", "6.45", "7",
            "7.15", "7.3", "7.45", "8"
    );
    private static final List<Double> COMBO_BOX_DOUBLE_OPTIONS = Arrays.asList(
            0.15, 0.3, 0.45, 1.,
            1.15, 1.3, 1.45, 2.,
            2.15, 2.3, 2.45, 3.,
            3.15, 3.3, 3.45, 4.,
            4.15, 4.3, 4.45, 5.,
            5.15, 5.3, 5.45, 6.,
            6.15, 6.3, 6.45, 7.,
            7.15, 7.3, 7.45, 8.
    );
    @FXML
    private TableView<BusyItem> tableView;

    @FXML
    private TableColumn<BusyItem, LocalDate> dateTableColumn;
    @FXML
    private TableColumn hoursTableColumn;
    @FXML
    private TableColumn infoTableColumn;

    private final ObservableList<BusyItem> data =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        DatePicker d;
//        d.
        //tableView.setEditable(true);
        ObservableList<BusyItem> items = tableView.getItems();
        infoTableColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(hoursTableColumn.widthProperty()).subtract(dateTableColumn.widthProperty()).subtract(10));
        dateTableColumn.setResizable(false);
        //dateTableColumn.setEditable(true);
        dateTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("date"));
        hoursTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("hours"));
        infoTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("info"));
        //infoTableColumn.setEditable(true);
        hoursTableColumn.setCellFactory(col -> {
            TableCell<TableView<BusyItem>, Property<Double>> c = new TableCell<>();
            final ComboBox<Double> comboBox = new ComboBox<>();
            comboBox.setEditable(true);
            comboBox.getItems().addAll(COMBO_BOX_DOUBLE_OPTIONS);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;
        });
        dateTableColumn.setCellFactory(new Callback<TableColumn<BusyItem, LocalDate>, TableCell<BusyItem, LocalDate>>() {
            @Override
            public TableCell<BusyItem, LocalDate> call(TableColumn<BusyItem, LocalDate> param) {
                DatePickerCell<BusyItem> datePick = new DatePickerCell<>(items, ((BusyItem x) -> x.date));
                return datePick;
            }
        });

        //not working
        tableView.getItems().add(new BusyItem(LocalDate.now(), 8., "sfgnadgi"));
//
//        tableView.setItems(data);
//        data.add(new BusyItem(LocalDate.now(), 8., "sfgnadgi"));
    }
}
