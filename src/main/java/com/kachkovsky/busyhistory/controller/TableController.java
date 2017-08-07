package com.kachkovsky.busyhistory.controller;

import com.kachkovsky.busyhistory.component.table.DeleteCell;
import com.kachkovsky.busyhistory.component.table.EditCell;
import com.kachkovsky.busyhistory.component.table.GraphicComboBoxTableCell;
import com.kachkovsky.busyhistory.component.table.LocalDatePickerTableCell;
import com.kachkovsky.busyhistory.data.BusyItem;
import com.kachkovsky.busyhistory.data.Project;
import com.kachkovsky.busyhistory.db.PersistenceManager;
import com.kachkovsky.busyhistory.db.hibernate.BusyItemRepository;
import com.kachkovsky.busyhistory.db.transaction.TransactionCallable;
import com.kachkovsky.busyhistory.db.transaction.TransactionRunnable;
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
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.persistence.EntityManager;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable, ApplyStageListener {
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

    @FXML
    private TableView<BusyItem> tableView;

    @FXML
    private TableColumn<BusyItem, LocalDate> dateTableColumn;
    @FXML
    private TableColumn<BusyItem, Object> hoursTableColumn;
    @FXML
    private TableColumn<BusyItem, Project> projectTableColumn;
    @FXML
    private TableColumn<BusyItem, String> infoTableColumn;
    @FXML
    private TableColumn deleteRowTableColumn;

    @FXML
    private Button applyBtn;

    @FXML
    private Button addBtn;

    private EntityManager em;
    private BusyItemRepository repository;
    private final ObservableList<BusyItem> data =
            FXCollections.observableArrayList(busyItem -> new Observable[]{
                    busyItem.dateProperty(),
                    busyItem.hoursProperty(),
                    busyItem.infoProperty()
            });

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Connect");
        em = PersistenceManager.INSTANCE.getEntityManager();
        repository = new BusyItemRepository(em);
        System.out.println("Connected");
        List<BusyItem> list = new TransactionCallable<List<BusyItem>>(em) {
            @Override
            protected List<BusyItem> doTransaction(EntityManager em) {
                return repository.list();
            }
        }.call();
        System.out.println("Loaded " + list.size());
        data.addAll(list);

        tableView.setEditable(true);
        tableView.setItems(data);
        infoTableColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(hoursTableColumn.widthProperty()).subtract(dateTableColumn.widthProperty()).subtract(deleteRowTableColumn.widthProperty()).subtract(10));


        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        hoursTableColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
        infoTableColumn.setCellValueFactory(new PropertyValueFactory<>("info"));


        dateTableColumn.setCellFactory(LocalDatePickerTableCell::new);
        dateTableColumn.setOnEditCommit(t -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setDate(t.getNewValue());
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
        projectTableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn());

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

        infoTableColumn.setCellFactory(column -> EditCell.createStringEditCell());
        infoTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setInfo(t.getNewValue()));

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
                try {
                    while (c.next()) {

                        System.out.println(String.format("%s %s %s %s", c.getAddedSize(), c.getFrom(), c.getTo(), c.getRemovedSize()));
                        System.out.println(String.format("%s %s %s %s %s", c.wasAdded(), c.wasPermutated(), c.wasRemoved(), c.wasReplaced(), c.wasUpdated()));
                        if (c.wasAdded()) {
                            for (BusyItem b : c.getAddedSubList()) {
                                new TransactionRunnable(em) {
                                    @Override
                                    protected void doTransaction(EntityManager em) {
                                        em.persist(b);
                                    }
                                }.run();
                            }
                        }
                        if (c.wasUpdated()) {
                            BusyItem busyItem = c.getList().get(c.getFrom());
                            new TransactionRunnable(em) {
                                @Override
                                protected void doTransaction(EntityManager em) {
                                    em.merge(busyItem);
                                }
                            }.run();
                        }
                        if (c.wasRemoved()) {
                            for (BusyItem b : c.getRemoved()) {
                                new TransactionRunnable(em) {
                                    @Override
                                    protected void doTransaction(EntityManager em) {
                                        em.remove(b);
                                    }
                                }.run();
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
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
