package com.kachkovsky.busyhistory.component.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;


public class GraphicComboBoxTableCell<S, T> extends TableCell<S, T> {
    private final static StringConverter<?> defaultStringConverter = new StringConverter<Object>() {
        @Override
        public String toString(Object t) {
            return t == null ? null : t.toString();
        }

        @Override
        public Object fromString(String string) {
            return (Object) string;
        }
    };

    static <T> ComboBox<T> createComboBox(final Cell<T> cell,
                                          final ObservableList<T> items,
                                          final ObjectProperty<StringConverter<T>> converter,
                                          T value) {
        ComboBox<T> comboBox = new ComboBox<T>(items);
        if (value != null) {
            comboBox.setValue(value);
        }
        comboBox.converterProperty().bind(converter);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (cell.isEditing()) {
                cell.commitEdit(newValue);
            }
        });
        return comboBox;
    }

    static <T> void updateItem(final Cell<T> cell,
                               final ComboBox<T> comboBox) {
        cell.setText(null);
        if (!cell.isEmpty()) {
            if (comboBox != null && cell.getItem() != null) {
                comboBox.setValue(cell.getItem());
            }
            cell.setGraphic(comboBox);
        }
    }


    private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
        return converter == null ?
                cell.getItem() == null ? "" : cell.getItem().toString() :
                converter.toString(cell.getItem());
    }


    private final ObservableList<T> items;

    private ComboBox<T> comboBox;


    /***************************************************************************
     * *
     * Constructors                                                            *
     * *
     **************************************************************************/

    public GraphicComboBoxTableCell(TableColumn<S, T> column) {
        this(FXCollections.<T>observableArrayList(), column);
    }

    @SafeVarargs
    public GraphicComboBoxTableCell(TableColumn<S, T> column, T... items) {
        this(FXCollections.observableArrayList(items), column);
    }

    @SafeVarargs
    public GraphicComboBoxTableCell(StringConverter<T> converter, TableColumn<S, T> column, T... items) {
        this(converter, column, FXCollections.observableArrayList(items));
    }


    public GraphicComboBoxTableCell(ObservableList<T> items, TableColumn<S, T> column) {
        this(null, column, items);
    }

    public GraphicComboBoxTableCell(StringConverter<T> converter, TableColumn<S, T> column, ObservableList<T> items) {
        this.getStyleClass().add("combo-box-table-cell");
        this.items = items;
        if (comboBox == null) {

            comboBox = createComboBox(this, items, converterProperty(), getItem());
            comboBox.editableProperty().bind(comboBoxEditableProperty());
            this.comboBox.disableProperty().bind(comboBoxEditableProperty().not());
        }
        this.comboBox.setOnShowing(event -> {
            final TableView<S> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setConverter(converter != null ? converter : (StringConverter<T>) defaultStringConverter);
    }


    /***************************************************************************
     * *
     * Properties                                                              *
     * *
     **************************************************************************/

    // --- converter
    private ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter");

    /**
     * The {@link StringConverter} property.
     */
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    /**
     * Sets the {@link StringConverter} to be used in this cell.
     */
    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    /**
     * Returns the {@link StringConverter} used in this cell.
     */
    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }


    // --- comboBox editable
    private BooleanProperty comboBoxEditable =
            new SimpleBooleanProperty(this, "comboBoxEditable");

    /**
     * A property representing whether the ComboBox, when shown to the user,
     * is editable or not.
     */
    public final BooleanProperty comboBoxEditableProperty() {
        return comboBoxEditable;
    }

    /**
     * Configures the ComboBox to be editable (to allow user input outside of the
     * options provide in the dropdown list).
     */
    public final void setComboBoxEditable(boolean value) {
        comboBoxEditableProperty().set(value);
    }

    /**
     * Returns true if the ComboBox is editable.
     */
    public final boolean isComboBoxEditable() {
        return comboBoxEditableProperty().get();
    }


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Returns the items to be displayed in the ChoiceBox when it is showing.
     */
    public ObservableList<T> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEdit() {
        //System.out.println("startEdit");
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        //System.out.println("startEdit1");
        comboBox.getSelectionModel().select(getItem());

        super.startEdit();
        //setText(null);
        setGraphic(comboBox);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        //setText(getConverter().toString(getItem()));
        setGraphic(comboBox);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        comboBox.setValue(item);
        updateItem(this, comboBox);
    }
}
