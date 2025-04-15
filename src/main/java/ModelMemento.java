import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModelMemento implements Metadata {
    private final Color color1;
    private final Color color2;
    private final Color color3;
    private final boolean checkbox;
    private final LocalDateTime timestamp;

    public ModelMemento(Color c1, Color c2, Color c3, boolean checkbox) {
        this.color1 = c1;
        this.color2 = c2;
        this.color3 = c3;
        this.checkbox = checkbox;
        this.timestamp = LocalDateTime.now();
    }

    public Color getColor1() { return color1; }
    public Color getColor2() { return color2; }
    public Color getColor3() { return color3; }
    public boolean isCheckbox() { return checkbox; }

    @Override
    public String getTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String getSummary() {
        return String.format("[%s] Rect1: %s, Rect2: %s, Rect3: %s, Checked: %b",
                getTimestamp(), color1, color2, color3, checkbox);
    }
}
