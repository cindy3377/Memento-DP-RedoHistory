import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Originator {
    private Color color1, color2, color3;
    private boolean checkbox;

    public void setState(Color c1, Color c2, Color c3, boolean checkbox) {
        this.color1 = c1;
        this.color2 = c2;
        this.color3 = c3;
        this.checkbox = checkbox;
    }

    public ModelMemento saveToMemento() {
        return new ModelMemento(color1, color2, color3, checkbox);
    }

    public void restoreFromMemento(ModelMemento memento) {
        setState(memento.getColor1(), memento.getColor2(), memento.getColor3(), memento.isCheckbox());
    }

    public Color getColor1() { return color1; }
    public Color getColor2() { return color2; }
    public Color getColor3() { return color3; }
    public boolean isCheckbox() { return checkbox; }
}
