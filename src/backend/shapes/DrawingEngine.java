package backend.shapes;


import java.awt.*;

public interface DrawingEngine {
    /* Manage shapes objects */
    void addShape(Shape shape);
    void removeShape(Shape shape);
    /* Return the created shapes objects */
    Shape[] getShapes();
    /* Redraw all shapes on the canvas */
    void refresh(Graphics canvas);
}
