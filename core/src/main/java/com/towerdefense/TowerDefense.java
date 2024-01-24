package com.towerdefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.towerdefense.core.Program;
import com.towerdefense.renderer.Renderer;
import com.towerdefense.ui.TextAlignment;
import com.towerdefense.ui.TextLayout;
import com.towerdefense.ui.UiLabel;
import com.towerdefense.ui.UserInterface;

public class TowerDefense extends Program {

    private OrthographicCamera camera;
    private Renderer renderer;
    private FitViewport fitViewport;
    private UserInterface ui;

    @Override
    public void create() {
        // camera and renderer
        camera = new OrthographicCamera();
        fitViewport = new FitViewport(720, 480);
        fitViewport.setCamera(camera);
        renderer = new Renderer();


        InputMultiplexer multiplexer = new InputMultiplexer();

        // create the ui
        ui = new UserInterface(fitViewport);
        UiLabel label = new UiLabel("""
            Lerio Satevis : Ascend and become one with the cosmic energy.
            This is a quote of one of the most powerful sorceress in the universe.
            Date : Cycle 2.23265772223212156""" , renderer.getFont()) {
            @Override
            public void render(Renderer renderer) {
                setVisible(getText().equalsIgnoreCase("null") ? false : true);
                setPosition(camera.viewportWidth / 2.0 - getSize().x / 2.0, camera.viewportHeight / 2.0 + (getSize().y / 2.0));
                super.render(renderer);
            }
        };
        label.setSize(220, 50);
        label.setFontScale(0.06);
        label.setTextLayout(TextLayout.Left);
        label.setTextAlignment(TextAlignment.Center);
        ui.addElement(label);

        ui.addToMultiplexer(multiplexer);
        Gdx.input.setInputProcessor(multiplexer);

        addResource(renderer);
    }


    @Override
    public void update(double delta) {
        ui.logic(delta);
    }

    @Override
    public void render(double delta) {
        renderer.clearScreen(Color.DARK_GRAY)
            .setProjectionMatrix(camera.projection);


        renderer.start().enableBlending();


        ui.render(renderer);


        renderer.disableBlending().stop();
    }


    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

}
