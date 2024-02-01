package com.towerdefense.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;

public class InputHandler {


    private InputAdapter input;


    public InputHandler() {
        input = new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return onMousePressed(screenX, screenY, switch (button) {
                    case 0 ->  MouseButton.Left;
                    case 1 ->  MouseButton.Right;
                    case 2 ->  MouseButton.Middle;
                    case 3 ->  MouseButton.Back;
                    case 4 ->  MouseButton.Forward;
                    default -> throw new IllegalStateException("Unexpected value: " + button);
                });
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return onMouseReleased(screenX, screenY, switch (button) {
                    case 0 ->  MouseButton.Left;
                    case 1 ->  MouseButton.Right;
                    case 2 ->  MouseButton.Middle;
                    case 3 ->  MouseButton.Back;
                    case 4 ->  MouseButton.Forward;
                    default -> throw new IllegalStateException("Unexpected value: " + button);
                });
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return onMouseDrag(screenX, screenY);
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return onCancel(screenX, screenY, switch (button) {
                    case 0 ->  MouseButton.Left;
                    case 1 ->  MouseButton.Right;
                    case 2 ->  MouseButton.Middle;
                    case 3 ->  MouseButton.Back;
                    case 4 ->  MouseButton.Forward;
                    default -> throw new IllegalStateException("Unexpected value: " + button);
                });
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
                return onMouseScroll(Gdx.input.getX(), Gdx.input.getY() , amountY < 0 ? ZoomType.In : ZoomType.Out);
            }
        };
    }

    public void update(double delta, int x, int y) {

    }


    public boolean onMousePressed(int x, int y, MouseButton button) {
        return false;
    }

    public boolean onMouseReleased(int x, int y, MouseButton button) {
        return false;
    }

    public boolean onMouseDrag(int x, int y) {
        return false;
    }

    public boolean onCancel(int x, int y, MouseButton button) {
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

    public boolean onMouseScroll(int x, int y, ZoomType zoom) {
        return false;
    }

    public void setAsInput() {
        Gdx.input.setInputProcessor(input);
    }

    public void addToMultiplexer(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(input);
    }

    protected InputAdapter getInput() {
        return input;
    }
}
