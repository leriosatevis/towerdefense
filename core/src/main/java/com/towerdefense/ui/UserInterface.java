package com.towerdefense.ui;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.towerdefense.input.InputHandler;
import com.towerdefense.input.MouseButton;
import com.towerdefense.renderer.Renderer;


public class UserInterface extends InputHandler {

    private Array<Element> elements;

    private Viewport viewport;

    public UserInterface(Viewport viewport) {
        this.viewport = viewport;
        elements = new Array<>();
    }


    public UserInterface addElement(Element element) {
        elements.add(element);
        element.setUserInterface(this);
        return this;
    }

    public UserInterface removeElement(Element element) {
        elements.removeValue(element, true);
        return this;
    }


    public Array<Element> getElements() {
        return elements;
    }


    public void update(double delta, int x, int y) {
        elements.forEach(element -> element.logic(delta));
    }

    public void render(Renderer renderer) {
        elements.forEach(element -> element.render(renderer));
    }

    private Vector2 clickStart = new Vector2();

    @Override
    public boolean onMousePressed(int x, int y, MouseButton button) {
        viewport.unproject(clickStart.set(x, y));
        // check if we've clicked on an element
        Element clicked;
        boolean processInput = false;
        // go backwards to preserve z order check
        for (int i = elements.size - 1; i >= 0; --i) {
            clicked = elements.get(i);
            if (clicked != null) {
                if (clicked.contains(clickStart)) {
                    processInput = true;
                    break;
                }
            }
        }
        // here we return the result from the check , if we've clicked on a UI element ,we don't want the event to pass on anything else.
        return processInput;
    }
}
