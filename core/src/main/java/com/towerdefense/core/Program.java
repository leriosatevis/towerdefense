package com.towerdefense.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.towerdefense.input.InputHandler;
import com.towerdefense.input.Multiplexer;

public class Program {

    private final Array<Disposable> resources;
    private final Multiplexer multiplexer;
    private final ApplicationAdapter adapter;

    public Program() {
        resources = new Array<>();
        multiplexer = new Multiplexer();
        adapter = new ApplicationAdapter() {
            @Override
            public void create() {
                Program.this.create();
                Gdx.input.setInputProcessor(multiplexer.get());
            }

            @Override
            public void resize(int width, int height) {
                Program.this.resize(width, height);
            }

            @Override
            public void render() {
                double delta = Gdx.graphics.getDeltaTime();
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();

                multiplexer.update(delta, x, y);
                Program.this.update(delta);
                Program.this.render(delta);
            }

            @Override
            public void pause() {
                super.pause();
            }

            @Override
            public void resume() {
                super.resume();
            }

            @Override
            public void dispose() {
                resources.forEach(resource -> {
                    if (resource != null) {
                        resource.dispose();
                        //System.err.println("Disposed of " + resource);
                    }
                });
                Program.this.dispose();
            }
        };
    }

    public void create() {

    }

    public void update(double delta) {

    }

    public void render(double delta) {

    }

    public void resize(int width, int height) {

    }

    public void dispose() {

    }


    public Program addResource(Disposable resource) {
        if (!resources.contains(resource, true))
            resources.add(resource);
        return this;
    }

    public Program addInput(InputHandler input) {
        multiplexer.addInput(input);
        return this;
    }

    public ApplicationAdapter getAdapter() {
        return adapter;
    }


}
