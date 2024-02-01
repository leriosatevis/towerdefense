package com.towerdefense.ui;

import com.badlogic.gdx.math.Vector2;
import com.towerdefense.renderer.Renderer;

public class Element {

    Vector2 position;
    Vector2 size;
    Vector2 scale;
    boolean visible;
    UserInterface userInterface;

    public Element() {
        position = new Vector2();
        size = new Vector2(1, 1);
        scale = new Vector2(1, 1);
        visible = true;
    }

    public void logic(double delta) {

    }

    public void render(Renderer renderer) {
    }

    public boolean contains(Vector2 vec) {
        // Adjust the size by the scale factor
        float scaledWidth = size.x * scale.x;
        float scaledHeight = size.y * scale.y;

        // Check if the point is within the scaled bounds of the element
        return vec.x > position.x && vec.y > position.y &&
               vec.x < position.x + scaledWidth && vec.y < position.y + scaledHeight;
    }

    public void setPosition(double x, double y) {
        position.set((float) x, (float) y);
    }

    public void setSize(double width, double height) {
        size.set((float) width, (float) height);
    }

    public void setScale(double scaleX, double scaleY) {
        scale.set((float) scaleX, (float) scaleY);
    }

    public Element setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
        return this;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getScale() {
        return scale;
    }


    public UserInterface getUserInterface() {
        return userInterface;
    }

    public Element setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }
}
