package com.towerdefense.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Program {

    private Array<Disposable> resources;
    private final ApplicationAdapter adapter;

    public Program() {
        resources = new Array<>();
        adapter = new ApplicationAdapter() {
            @Override
            public void create() {
                Program.this.create();
            }

            @Override
            public void resize(int width, int height) {
                Program.this.resize(width, height);
            }

            @Override
            public void render() {
                Program.this.update(Gdx.graphics.getDeltaTime());
                Program.this.render(Gdx.graphics.getDeltaTime());
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


    public ApplicationAdapter getAdapter() {
        return adapter;
    }


}
