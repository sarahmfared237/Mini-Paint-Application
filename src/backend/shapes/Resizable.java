package backend.shapes;

import java.awt.*;

public interface Resizable {
    void setDraggingPoint(Point point);
    Point getDraggingPoint();
    boolean contains(Point point);
    void resize();
}