package com.towerdefense.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.mediators.Target;
import com.towerdefense.renderer.Renderer;

public class Projectile implements Renderable, Pool.Poolable {

    private Turret turret;
    private double radius = 1;
    private double damage = 1;
    private double speed = 1;
    private double acceleration = 1;
    private Vector2 direction;
    private Vector2 destination;
    private Vector2 position;

    public Projectile(Turret turret) {
        this.turret = turret;
        direction = new Vector2();
        destination = new Vector2();
        position = new Vector2();
    }

    @Override
    public void render(Renderer renderer) {
        renderer// draw the turret fill
            .setColor(Color.GOLD)
            .fillOval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 50)
            // draw the turret outline
            .setColor(Color.WHITE)
            .oval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 1, 50);

    }

    public void logic(double delta) {
        // Update the projectile's position along the direction vector
        Vector2 movement = new Vector2(direction).scl((float) (speed * delta));
        position.add(movement);

        // Check if the projectile reached its destination or needs to be reset
        if (position.epsilonEquals(destination, 1.0f)) {
            // Here you can handle hitting the target
            // Then reset or free the projectile
            reset();
        }
    }

    public void fly(Target target) {
        // Set the projectile's destination as the target's current position
        this.destination.set(target.getPosition());
        // Calculate the direction from the turret to the target
        this.direction.set(destination).sub(turret.getPosition()).nor();
    }

    @Override
    public void reset() {
        // Reset the projectile's properties to their initial state
        this.turret = null;
        this.radius = 1;
        this.damage = 1;
        this.speed = 1;
        this.acceleration = 1;
        this.direction.setZero();
        this.destination.setZero();
        this.position.setZero();
    }

}
