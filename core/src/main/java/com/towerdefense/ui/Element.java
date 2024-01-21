package com.towerdefense.ui;

import com.badlogic.gdx.math.Vector2;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.renderer.Renderer;

public class Element implements Renderable {

    Vector2 position;
    Vector2 size;

    public Element() {
        position = new Vector2();
        size = new Vector2();
    }

    @Override
    public void render(Renderer renderer) {

    }

    public boolean contains (Vector2 vec) {
        return vec.x > position.x && vec.y > position.y && vec.x < position.x + size.x && vec.y < position.y + size.y;
    }

    public void setPosition (double x , double y) {
        position.set((float) x , (float) y);
    }

    public void setSize (double width , double height) {
        size.set((float) width , (float) height);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}
