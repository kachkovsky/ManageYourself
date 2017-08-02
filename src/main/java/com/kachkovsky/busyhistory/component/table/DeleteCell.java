package com.kachkovsky.busyhistory.component.table;

import com.kachkovsky.busyhistory.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DeleteCell<S, T> extends TableCell<S, T> {

    static class Holder {
        static final Image IMAGE_DELETE = new Image(Main.class.getClassLoader().getResource("pics/ic_delete.png").toString());
    }

    private Button deleteButton;

    public DeleteCell() {
        super();
        if (!isEmpty()) {
            createButton();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    public void createButton() {
        if (deleteButton == null) {
            this.deleteButton = new Button("", new ImageView(Holder.IMAGE_DELETE));
            this.deleteButton.setPadding(Insets.EMPTY);
            this.setGraphic(deleteButton);
        }
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            createButton();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}