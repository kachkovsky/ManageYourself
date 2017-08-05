package com.kachkovsky.busyhistory.controller;

import com.kachkovsky.busyhistory.component.table.DeleteCell;
import com.kachkovsky.busyhistory.component.table.EditCell;
import com.kachkovsky.busyhistory.component.table.GraphicComboBoxTableCell;
import com.kachkovsky.busyhistory.component.table.LocalDatePickerTableCell;
import com.kachkovsky.busyhistory.data.BusyItem;
import com.kachkovsky.busyhistory.db.PersistenceManager;
import com.kachkovsky.javafx.ApplyStageListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.persistence.EntityManager;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable, ApplyStageListener {
//    private static final List<String> COMBO_BOX_OPTIONS = Arrays.asList(
//            "0.15", "0.3", "0.45", "1",
//            "1.15", "1.3", "1.45", "2",
//            "2.15", "2.3", "2.45", "3",
//            "3.15", "3.3", "3.45", "4",
//            "4.15", "4.3", "4.45", "5",
//            "5.15", "5.3", "5.45", "6",
//            "6.15", "6.3", "6.45", "7",
//            "7.15", "7.3", "7.45", "8"
//    );

    private static final Double[] COMBO_BOX_DOUBLE_OPTIONS_ARR = {
            0.15, 0.3, 0.45, 1.,
            1.15, 1.3, 1.45, 2.,
            2.15, 2.3, 2.45, 3.,
            3.15, 3.3, 3.45, 4.,
            4.15, 4.3, 4.45, 5.,
            5.15, 5.3, 5.45, 6.,
            6.15, 6.3, 6.45, 7.,
            7.15, 7.3, 7.45, 8.
    };
    private static final List<Double> COMBO_BOX_DOUBLE_OPTIONS = Arrays.asList(COMBO_BOX_DOUBLE_OPTIONS_ARR);

    @FXML
    private TableView<BusyItem> tableView;

    @FXML
    private TableColumn<BusyItem, LocalDate> dateTableColumn;
    @FXML
    private TableColumn<BusyItem, Object> hoursTableColumn;
    @FXML
    private TableColumn<BusyItem, String> infoTableColumn;
    @FXML
    private TableColumn deleteRowTableColumn;

    @FXML
    private Button applyBtn;

    @FXML
    private Button addBtn;

    private EntityManager em;

    private final ObservableList<BusyItem> data =
            FXCollections.observableArrayList(busyItem -> new Observable[]{
                    busyItem.dateProperty(),
                    busyItem.hoursProperty(),
                    busyItem.infoProperty()
            });

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("XX");
        try {
            em = PersistenceManager.INSTANCE.getEntityManager();
        }catch (Throwable t){
            t.printStackTrace();
        }
        System.out.println("XX2");

//        DatePicker d;
//        d.
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<BusyItem> q = cb.createQuery(BusyItem.class);
//        Root<BusyItem> root = q.from(BusyItem.class);
//        CriteriaQuery<BusyItem> select = q.select(root);
//        List<BusyItem> resultList = em.createQuery(select).getResultList();
//        data.addAll(resultList);

        tableView.setEditable(true);
        tableView.setItems(data);
        //ObservableList<BusyItem> items = tableView.getItems();
        infoTableColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(hoursTableColumn.widthProperty()).subtract(dateTableColumn.widthProperty()).subtract(deleteRowTableColumn.widthProperty()).subtract(10));


        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        //dateTableColumn.setMinWidth(110);
        hoursTableColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
        infoTableColumn.setCellValueFactory(new PropertyValueFactory<>("info"));

//        dateTableColumn.setCellValueFactory(cellData -> {
//            Function<BusyItem, ObjectProperty<LocalDate>> dateProperty = BusyItem::dateProperty;
//            return dateProperty.apply(cellData.getValue());
//        });
//        hoursTableColumn.setCellValueFactory(cellData -> {
//            Function<BusyItem, ObservableValue> hoursProperty = BusyItem::hoursProperty;
//            return hoursProperty.apply(cellData.getValue());
//        });


//        dateTableColumn.setEditable(true);
//        hoursTableColumn.setEditable(true);
//        infoTableColumn.setEditable(true);

        //hoursTableColumn.setCellFactory(GraphicComboBoxTableCell.forTableColumn(COMBO_BOX_DOUBLE_OPTIONS_ARR));

        dateTableColumn.setCellFactory(LocalDatePickerTableCell::new);
        dateTableColumn.setOnEditCommit(t -> {
            // System.out.println("Commit");
            //System.out.println(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDate());
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setDate(t.getNewValue());
            // System.out.println(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDate());
            //((TableColumn)t.getSource()).getTableView().requestLayout();
            //t.consume();
        });

        hoursTableColumn.setCellFactory(col -> {
            GraphicComboBoxTableCell<BusyItem, Object> comboBoxTableCell = new GraphicComboBoxTableCell<>(new StringConverter<Object>() {

                @Override
                public String toString(Object object) {
                    if (object == null) {
                        return null;
                    }
                    return object.toString();
                }

                @Override
                public Object fromString(String string) {
                    if (string == null) {
                        return 0.;
                    }
                    return Double.parseDouble(string);
                }
            },
                    hoursTableColumn, (Object[]) COMBO_BOX_DOUBLE_OPTIONS_ARR);
            comboBoxTableCell.setComboBoxEditable(true);
            return comboBoxTableCell;
        });
        hoursTableColumn.setOnEditCommit((CellEditEvent<BusyItem, Object> t) -> {
            Double newValue = null;
            Object newValueObj = t.getNewValue();
            if (newValueObj instanceof Double) {
                newValue = (Double) newValueObj;
            } else if (newValueObj != null) {
                newValue = Double.parseDouble(newValueObj.toString());
            }
            if (newValue != null) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setHours(newValue);
            }
        });

        deleteRowTableColumn.setCellFactory(column -> new DeleteCell());

//        hoursTableColumn.setCellFactory(col -> {
//            TableCell<TableView<BusyItem>, Property<Double>> c = new TableCell<>();
//            final ComboBox<Double> comboBox = new ComboBox<>();
//            comboBox.setEditable(true);
//            comboBox.getItems().addAll(COMBO_BOX_DOUBLE_OPTIONS);
//            c.itemProperty().addListener((observable, oldValue, newValue) -> {
//                if (oldValue != null) {
//                    comboBox.valueProperty().unbindBidirectional(oldValue);
//                }
//                if (newValue != null) {
//                    comboBox.valueProperty().bindBidirectional(newValue);
//                }
//            });
//            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
//            return c;
//        });
        //infoTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        infoTableColumn.setCellFactory(column -> EditCell.createStringEditCell());
        infoTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setInfo(t.getNewValue()));

//        dateTableColumn.setCellFactory(new Callback<TableColumn<BusyItem, LocalDate>, TableCell<BusyItem, LocalDate>>() {
//            @Override
//            public TableCell<BusyItem, LocalDate> call(TableColumn<BusyItem, LocalDate> param) {
//                DatePickerCell<BusyItem> datePick = new DatePickerCell<>(items, ((BusyItem x) -> x.date));
//                return datePick;
//            }
//        });

        //not working
        tableView.getItems().add(new BusyItem(LocalDate.now(), 8., "sfgnadgi"));
        tableView.getItems().add(new BusyItem(LocalDate.now(), 4., "asdasfgnadgi"));
        //dateTableColumn.setResizable(false);
        //GUIUtils.autoFitTable(tableView);
//
//        tableView.setItems(data);
//        data.add(new BusyItem(LocalDate.now(), 8., "sfgnadgi"));

        applyBtn.setOnAction(event -> {
            for (BusyItem item : tableView.getItems()) {
                System.out.println(String.format("%s %s %s", item.getDate(), item.getHours(), item.getInfo()));
            }
        });

        addBtn.setOnAction(e -> {
            tableView.getItems().add(new BusyItem(LocalDate.now(), 2., ""));
        });

        tableView.getItems().addListener(new ListChangeListener<BusyItem>() {
            @Override
            public void onChanged(Change<? extends BusyItem> c) {
                while (c.next()) {
                    System.out.println(String.format("%s %s %s %s", c.getAddedSize(), c.getFrom(), c.getTo(), c.getRemovedSize()));
                    System.out.println(String.format("%s %s %s %s %s", c.wasAdded(), c.wasPermutated(), c.wasRemoved(), c.wasReplaced(), c.wasUpdated()));
                }
            }
        });

    }

    @Override
    public void applyStageActions(Stage stage) {
        stage.setOnHidden(e -> {
            em.close();
        });
    }
}
