package com.towerdefense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.towerdefense.core.Program;
import com.towerdefense.input.PanAndZoomController;
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
        fitViewport = new FitViewport(1920, 1080);
        fitViewport.setCamera(camera);
        fitViewport.apply(true);
        renderer = new Renderer();


        // create the ui
        ui = new UserInterface(fitViewport);
        UiLabel label = new UiLabel("""
            Lerio Satevis : Ascend and become one with the cosmic energy.This is a quote from one of the most powerful sorceress in the universe.""", renderer.getFont()) {
            @Override
            public void render(Renderer renderer) {
                setVisible(getText().equalsIgnoreCase("null") ? false : true);
                setPosition(camera.viewportWidth / 2.0 - getSize().x / 2.0, camera.viewportHeight / 2.0 + (getSize().y / 2.0));
                setDrawGlyphBounds(false);
                setWrap(true);
                setGlyphBoundLineThickness(camera.zoom * 0.5f);
                super.render(renderer);
            }
        };
        label.setSize(400, 80);
        label.setFontScale(0.2);
        label.setStyle(TextLayout.Left);
        label.setTextAlignment(TextAlignment.Center);

        UiLabel label1 = new UiLabel("START" , renderer.getFont());
        label1.setSize(120 , 30);
        label1.setPosition(200 , 200);
        label1.setFontScale(0.2);
        label1.setStyle(TextLayout.Left);
        label1.setTextAlignment(TextAlignment.Center);


        ui.addElement(label);
        ui.addElement(label1);

        PanAndZoomController controller = new PanAndZoomController(fitViewport);
        controller.setLerpTime(0.25f);


        addResource(renderer)
            .addInput(controller)
            .addInput(ui);
    }


    @Override
    public void update(double delta) {

    }

    GlyphLayout layout = new GlyphLayout();

    @Override
    public void render(double delta) {
        renderer.clearScreen(Color.DARK_GRAY)
            .setProjectionMatrix(camera.combined);


        renderer.start().enableBlending();


        ui.render(renderer);
        String es = "Emma Stone has the most beautiful feet on Earth.\nAnd I want to see her.";
        renderer.text(es, 400, 400, Color.WHITE, Align.left, false, -1, null, 1, 1, true, camera.zoom);


        renderer.disableBlending().stop();
    }


    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, false);
    }

}
