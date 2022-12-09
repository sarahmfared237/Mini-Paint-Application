package backend.shapes;

import java.awt.*;

public interface Resizable {
    int BOX_WIDTH = 7, BOX_HEIGHT = 7;
    void drawSelected(Graphics canvas);
    void resize(Point point);
    Shape containResizePoint(Point point);
}