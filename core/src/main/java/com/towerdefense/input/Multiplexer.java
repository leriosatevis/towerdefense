package com.towerdefense.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Array;


public class Multiplexer {


    private InputMultiplexer multiplexer;
    private Array<InputHandler> inputs;


    public Multiplexer() {
        multiplexer = new InputMultiplexer();
        inputs = new Array<>();
    }


    public Multiplexer addInput(InputHandler input) {
        multiplexer.addProcessor(input.getInput());
        inputs.add(input);
        return this;
    }

    public void update(double delta, int x, int y) {
        inputs.forEach(inputHandler -> inputHandler.update(delta, x, y));
    }

    public InputMultiplexer get() {
        return multiplexer;
    }

}
