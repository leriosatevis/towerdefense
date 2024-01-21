package com.towerdefense;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.towerdefense.entities.Turret;
import com.towerdefense.entities.WildCookoo;
import com.towerdefense.enums.LevelDifficulty;
import com.towerdefense.enums.MonsterType;
import com.towerdefense.enums.TargetSystem;
import com.towerdefense.renderer.Renderer;
import com.towerdefense.ui.UiLabel;
import com.towerdefense.ui.UserInterface;
import com.towerdefense.world.Level;

public class TowerDefense extends ApplicationAdapter {

    private OrthographicCamera camera;
    private Renderer renderer;
    private FitViewport fitViewport;
    private Level level;
    private UserInterface ui;

    @Override
    public void create() {
        // camera and renderer
        camera = new OrthographicCamera();
        fitViewport = new FitViewport(1920/2f, 1080/2f);
        fitViewport.setCamera(camera);
        renderer = new Renderer();

        // create the level and add some turrets
        level = new Level("Level 1", fitViewport, LevelDifficulty.Easy);
        level.setTurretsCanOverlap(true);

        Turret turret0 = new Turret("Turret 0", level, TargetSystem.GroundToAir, 20 , 300 , 150 , 1);
        turret0.setPosition(200, 200);

        Turret turret1 = new Turret("Turret 1", level, TargetSystem.GroundToGround, 35 , 200 , 10 , 5);
        turret1.setPosition(200, 275);

        WildCookoo chickenInferno = new WildCookoo("Wild Cookoo" , MonsterType.Air , 200000 ,20);
        chickenInferno.setPosition(600 ,450);
        level.addMinion(chickenInferno);

        level.addTurret(turret0);
        level.addTurret(turret1);

        InputMultiplexer multiplexer = new InputMultiplexer();

        // create the ui
        ui = new UserInterface(fitViewport);
        final UiLabel label = targetLabel();
        ui.addElement(label);

        // create the world input


        ui.addToMultiplexer(multiplexer);

        level.addToMultiplexer(multiplexer);


        Gdx.input.setInputProcessor(multiplexer);
    }

    private UiLabel targetLabel() {
        UiLabel label = new UiLabel("Target")
        {
            @Override
            public void render(Renderer renderer) {
                setText(level.getTurretManager().getSelectedTurret() + "");
                setVisible(getText().equalsIgnoreCase("null") ? false : true);
                setPosition(camera.viewportWidth / 2.0 - getSize().x / 2.0, camera.viewportHeight - (getSize().y + 2));
                super.render(renderer);
            }
        };
        label.setPosition(200 ,200);
        label.setSize(120 ,20);
        return label;
    }

    @Override
    public void render() {
        level.logic(Gdx.graphics.getDeltaTime());

        renderer.clearScreen(Color.DARK_GRAY)
        .setProjectionMatrix(camera.projection);


        renderer.start().enableBlending();

        level.render(renderer);
        ui.render(renderer);
        renderer.disableBlending().stop();
    }


    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        level.dispose();
    }
}
