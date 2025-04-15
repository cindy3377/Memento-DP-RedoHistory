import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.Deque;

public class MementoApp extends Application {

    private final Originator originator = new Originator();
    private final Deque<ModelMemento> undoStack = new ArrayDeque<>();
    private final Deque<ModelMemento> redoStack = new ArrayDeque<>();
    private final ObservableList<ModelMemento> historyList = FXCollections.observableArrayList();

    private final Rectangle r1 = new Rectangle(100, 100, Color.RED);
    private final Rectangle r2 = new Rectangle(100, 100, Color.GREEN);
    private final Rectangle r3 = new Rectangle(100, 100, Color.BLUE);
    private final CheckBox checkBox = new CheckBox("Active");

    @Override
    public void start(Stage primaryStage) {
        // Initial state
        saveState();

        // Layout
        HBox rectangles = new HBox(10, r1, r2, r3);
        Button historyButton = new Button("Show History");

        VBox layout = new VBox(10, rectangles, checkBox, historyButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 300);

        // Change listeners
        checkBox.setOnAction(e -> handleChange());
        r1.setOnMouseClicked(e -> { r1.setFill(randomColor()); handleChange(); });
        r2.setOnMouseClicked(e -> { r2.setFill(randomColor()); handleChange(); });
        r3.setOnMouseClicked(e -> { r3.setFill(randomColor()); handleChange(); });

        // Key events for undo/redo
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown()) {
                if (e.getCode() == KeyCode.Z) handleUndo();
                else if (e.getCode() == KeyCode.Y) handleRedo();
            }
        });

        // History button
        historyButton.setOnAction(e -> showHistoryWindow());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Memento App with History");
        primaryStage.show();
    }

    private void handleChange() {
        saveState();
        redoStack.clear();
    }

    private void saveState() {
        originator.setState((Color) r1.getFill(), (Color) r2.getFill(), (Color) r3.getFill(), checkBox.isSelected());
        ModelMemento memento = originator.saveToMemento();
        undoStack.push(memento);
        historyList.add(memento);
    }

    private void handleUndo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(originator.saveToMemento());
            ModelMemento memento = undoStack.pop();
            originator.restoreFromMemento(memento);
            applyStateToUI();
        }
    }

    private void handleRedo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(originator.saveToMemento());
            ModelMemento memento = redoStack.pop();
            originator.restoreFromMemento(memento);
            applyStateToUI();
        }
    }

    private void applyStateToUI() {
        r1.setFill(originator.getColor1());
        r2.setFill(originator.getColor2());
        r3.setFill(originator.getColor3());
        checkBox.setSelected(originator.isCheckbox());
    }

    private void showHistoryWindow() {
        ListView<ModelMemento> listView = new ListView<>(historyList);
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ModelMemento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getSummary());
            }
        });

        listView.setOnMouseClicked(event -> {
            ModelMemento selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                originator.restoreFromMemento(selected);
                applyStateToUI();
            }
        });

        VBox layout = new VBox(10, new Label("History:"), listView);
        layout.setPadding(new Insets(10));
        Scene historyScene = new Scene(layout, 400, 300);
        Stage historyStage = new Stage();
        historyStage.setScene(historyScene);
        historyStage.setTitle("Model History");
        historyStage.show();
    }

    private Color randomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    public static void main(String[] args) {
        launch();
    }
}
