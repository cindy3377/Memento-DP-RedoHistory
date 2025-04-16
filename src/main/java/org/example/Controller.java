package org.example;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Model model;
    private Gui gui;
    private List<IMemento> history;
    private List<IMemento> redoStack;

    public Controller(Gui gui) {
        this.model = new Model();
        this.gui = gui;
        this.history = new ArrayList<>();
        this.redoStack = new ArrayList<>();
    }

    public void setOption(int optionNumber, int choice) {
        saveToHistory();
        redoStack.clear();
        model.setOption(optionNumber, choice);
    }

    public int getOption(int optionNumber) {
        return model.getOption(optionNumber);
    }

    public void setIsSelected(boolean isSelected) {
        saveToHistory();
        redoStack.clear();
        model.setIsSelected(isSelected);
    }

    public boolean getIsSelected() {
        return model.getIsSelected();
    }

    public void undo() {
        if (!history.isEmpty()) {
            IMemento currentState = model.createMemento();
            redoStack.add(currentState);

            IMemento previousState = history.remove(history.size() - 1);
            model.restoreState(previousState);
            gui.updateGui();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            IMemento currentState = model.createMemento();
            history.add(currentState);

            IMemento nextState = redoStack.remove(redoStack.size() - 1);
            model.restoreState(nextState);
            gui.updateGui();
        }
    }

    public void restoreState(IMemento memento) {
        saveToHistory();
        redoStack.clear();
        model.restoreState(memento);
        gui.updateGui();
    }

    private void saveToHistory() {
        IMemento currentState = model.createMemento();
        history.add(currentState);
    }

    public List<IMemento> getHistory() {
        return history;
    }
}
