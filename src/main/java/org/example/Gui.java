package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui extends Application {
    private Controller controller;
    private ColorBox colorBox1, colorBox2, colorBox3;
    private CheckBox checkBox;

    public void start(Stage stage) {
        controller = new Controller(this);

        Insets insets = new Insets(10);
        colorBox1 = new ColorBox(1, controller);
        colorBox2 = new ColorBox(2, controller);
        colorBox3 = new ColorBox(3, controller);

        checkBox = new CheckBox("Click me!");
        checkBox.setPadding(insets);
        checkBox.setOnAction(event -> controller.setIsSelected(checkBox.isSelected()));

        HBox hBox = new HBox(colorBox1.getRectangle(), colorBox2.getRectangle(), colorBox3.getRectangle());
        hBox.setSpacing(10);
        hBox.setMargin(colorBox1.getRectangle(), insets);
        hBox.setMargin(colorBox2.getRectangle(), insets);
        hBox.setMargin(colorBox3.getRectangle(), insets);

        Label label = new Label("Press Ctrl-Z to undo, Ctrl-Y to redo.");
        Button historyButton = new Button("Show History");
        historyButton.setOnAction(e -> openHistoryWindow());

        VBox vBox = new VBox(hBox, checkBox, label, historyButton);
        Scene scene = new Scene(vBox);

        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                controller.undo();
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                controller.redo();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Memento Pattern Example");
        stage.show();
    }

    private void openHistoryWindow() {
        Stage historyStage = new Stage();
        ListView<IMemento> listView = new ListView<>();
        ObservableList<IMemento> observableHistory = FXCollections.observableArrayList(controller.getHistory());
        listView.setItems(observableHistory);

        listView.setOnMouseClicked(e -> {
            IMemento selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.restoreState(selected);
            }
        });

        VBox layout = new VBox(listView);
        historyStage.setScene(new Scene(layout, 250, 400));
        historyStage.setTitle("History");
        historyStage.show();
    }

    public void updateGui() {
        colorBox1.setColor(controller.getOption(1));
        colorBox2.setColor(controller.getOption(2));
        colorBox3.setColor(controller.getOption(3));
        checkBox.setSelected(controller.getIsSelected());
    }
}
