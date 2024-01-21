package com.towerdefense.world;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.towerdefense.entities.Minion;
import com.towerdefense.entities.Turret;
import com.towerdefense.enums.LevelDifficulty;
import com.towerdefense.managers.TurretManager;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.renderer.Renderer;

public class Level implements Renderable, Disposable {

    private Viewport viewport;
    private World world;
    private String name;
    private Array<Turret> turrets;
    private Array<Minion> minions;
    private Array<Path> paths;
    private LevelDifficulty difficulty;
    private Vector2 worldPosition;
    private Vector2 worldSize;
    private Color worldDebugDrawColor = new Color(Color.GREEN);
    private boolean turretsCanOverlap = false;
    private TurretManager turretManager;

    public Level(String name,Viewport viewport, LevelDifficulty difficulty) {
        this.viewport = viewport;
        world = new World(new Vector2(0, 0), false);
        this.turrets = new Array<>();
        this.minions = new Array<>();
        this.worldPosition = new Vector2(0, 0);
        this.worldSize = new Vector2(viewport.getWorldWidth() , viewport.getWorldHeight());
        this.name = name;
        paths = new Array<>();
        this.difficulty = difficulty;
        this.turretManager = new TurretManager(this.viewport , this);
    }


    public void addTurret(Turret turret) {
        if (turretsCanOverlap) {
            turrets.add(turret);
            return;
        }
        for (int i = turrets.size - 1; i >= 0; i--) {
            Turret current = turrets.get(i);
            if (current != null && turret != null) {
                if (current.overlaps(turret)) {
                    throw new IllegalStateException("This place is taken by another turret ,please please somewhere else.");
                }
            }
        }
        turrets.add(turret);
    }

    public void addMinion(Minion minion) {
        this.minions.add(minion);
    }

    public void removeMinion(Minion minion) {
        this.minions.removeValue(minion, true);
    }

    public void logic(double delta) {
        world.step(1.0f / (float) delta, 4, 4);
        turrets.forEach(turret -> turret.logic(delta));
        minions.forEach(minion -> minion.logic(delta));
    }

    public Vector2 getWorldSize() {
        return worldSize;
    }

    public Array<Turret> getTurrets() {
        return turrets;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setColor(worldDebugDrawColor).rectangle(0, 0, worldSize.x, worldSize.y, 0, 0, 0, true, 1, 1, 1);
        turrets.forEach(turret -> turret.render(renderer));
        minions.forEach(minion -> minion.render(renderer));
    }

    public Level setTurretsCanOverlap(boolean turretsCanOverlap) {
        this.turretsCanOverlap = turretsCanOverlap;
        return this;
    }

    public void addToMultiplexer (InputMultiplexer multiplexer) {
        turretManager.addToMultiplexer(multiplexer);
    }

    public TurretManager getTurretManager() {
        return turretManager;
    }

    public boolean overlapAllowed() {
        return turretsCanOverlap;
    }

    public Array<Minion> getMinions() {
        return minions;
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
