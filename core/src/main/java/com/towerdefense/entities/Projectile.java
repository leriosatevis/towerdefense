package com.towerdefense.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.renderer.Renderer;

public class Projectile implements Renderable , Pool.Poolable {

    private Turret turret;
    private double size = 1;
    private double damage = 1;
    private double speed = 1;
    private double acceleration = 1;
    private Vector2 direction;
    private Vector2 destination;

    public Projectile (Turret turret) {
        this.turret = turret;
        direction = new Vector2();
        destination = new Vector2();
    }

    @Override
    public void render(Renderer renderer) {

    }

    @Override
    public void reset() {

    }
}
