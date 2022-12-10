package backend.shapes;

import java.awt.*;

public interface Resizable {
    int BOX_WIDTH = 10, BOX_HEIGHT = 10;
    void drawSelected(Graphics canvas);
    void resize(Point point);
    Shape containResizePoint(Point point);
}