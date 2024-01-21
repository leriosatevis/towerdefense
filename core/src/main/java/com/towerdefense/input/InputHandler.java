package com.towerdefense.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;

public class InputHandler {


    private InputAdapter input;


    public InputHandler() {
        input = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return onMousePressed(screenX, screenY, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return onMouseReleased(screenX, screenY, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return onMouseDrag(screenX, screenY);
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return onCancel(screenX, screenY, button);
            }

            @Override
            public boolean keyDown(int keycode) {
                return onKeyPressed(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                return onKeyReleased(keycode);
            }

            @Override
            public boolean keyTyped(char character) {
                return onCharEntered(character);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return onMouseMove(screenX, screenY);
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return onMouseScroll(amountY);
            }
        };
    }


    public boolean onMousePressed(int x, int y, int button) {
        return false;
    }

    public boolean onMouseReleased(int x, int y, int button) {
        return false;
    }

    public boolean onMouseDrag(int x, int y) {
        return false;
    }

    public boolean onCancel(int x, int y, int button) {
        return false;
    }

    public boolean onKeyPressed(int key) {
        return false;
    }

    public boolean onKeyReleased(int key) {
        return false;
    }

    public boolean onCharEntered(Character character) {
        return false;
    }

    public boolean onMouseMove(int x, int y) {
        return false;
    }

    public boolean onMouseScroll (double scrollAmount) {
        return false;
    }

    public void setAsInput () {
        Gdx.input.setInputProcessor(input);
    }

    public void addToMultiplexer(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(input);
    }
}
