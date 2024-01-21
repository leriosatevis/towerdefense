package com.towerdefense.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.towerdefense.enums.TargetSystem;
import com.towerdefense.enums.TurretState;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.renderer.Renderer;
import com.towerdefense.world.Level;

public class Turret implements Renderable {

    private double range = 400;
    private String name;
    private Vector2 position;
    private Color turretColor = new Color(Color.RED);
    private Color turretOutlineColor = new Color(Color.WHITE);
    private Color turretRangeColor = new Color(Color.BLUE);
    private TargetSystem targetSystem;
    private Level level;
    private Minion target;
    private double radius = 1;
    private TurretState turretState;
    private double turretBaseDamage;
    // times per second
    private double attackRate;
    private Pool<Projectile> projectilePool;


    public Turret(String name, Level level, TargetSystem targetSystem, double radius, double range, double baseDamage, double attackRate) {
        this.name = name;
        this.position = new Vector2();
        this.level = level;
        this.targetSystem = targetSystem;
        this.radius = radius;
        this.range = range;
        this.turretBaseDamage = baseDamage;
        this.attackRate = attackRate;
        this.turretState = TurretState.Searching;
        this.projectilePool = new Pool<Projectile>() {
            @Override
            protected Projectile newObject() {
                return new Projectile(Turret.this);
            }
        };
    }


    private void target(Minion target) {
        if (target != null) this.target = target;
    }


    private double t;

    public void logic(double delta) {
        switch (turretState) {
            case Searching -> seek();
            case TargetAcquired -> {
                // continuously check if target is within range
                if (hasTarget() && isInRange(target)) {
                    double attackSpeed = 1.0 / attackRate;
                    if (t < attackSpeed) {
                        t += delta;
                    } else {
                        t = 0;
                        performAttack();
                        System.out.println(name + " Performing an attack on enemy " + target.name + " at rate " + attackRate + " attacks per second");
                    }
                } else {
                    turretState = TurretState.Searching;
                }
            }
        }
    }

    private void performAttack() {
        if (isInRange(target)) {
            target.health -= turretBaseDamage;
            Projectile projectile = projectilePool.obtain();
            projectile.fly(target);
        }
    }

    private void seek() {
        // we can only seek if we are in the searching state
        if (turretState == TurretState.Searching)
            // the purpose of seek is to acquire a target
            for (int i = 0; i < level.getMinions().size; i++) {
                final Minion current = level.getMinions().get(i);
                if (current != null && isInRange(current)) {
                    target(current);
                    turretState = TurretState.TargetAcquired;
                    break;
                }
            }
    }

    @Override
    public void render(Renderer renderer) {
        renderer
            // draw the turret range
            .setColor(turretRangeColor)
            .oval(position.x, position.y, range * 2.0, range * 2.0, 0, 0, 0, true, 1, 1, 1, 100)
            // draw the turret fill
            .setColor(turretOutlineColor)
            .fillOval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 50)
            // draw the turret outline
            .setColor(turretColor)
            .oval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 1, 50);

    }


    public Turret setPosition(double x, double y) {
        this.position.set((float) x, (float) y);
        return this;
    }

    public boolean contains(Vector2 vector2) {
        return position.dst(vector2) <= radius; // Assuming size is the diameter
    }

    public boolean overlaps(Turret turret) {
        return position.dst(turret.position) <= (radius + turret.radius); // Sum of radii
    }

    /**
     * This checks if the target (minion/monster) is in a valid range of the turret<br>
     * which is the distance from the center of the turret (x , y) to the outer shell of the minion.
     *
     * @return true if the minion/monster is within a valid range and attackable by this turret
     * */
    public boolean isInRange(Minion minion) {
        return position.dst(minion.position) <= range + minion.radius;
    }

    public Turret setRange(double range) {
        this.range = range;
        return this;
    }

    public double getRange() {
        return range;
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * This simply checks if the target is not NULL
     *
     * @return true if the target is not null
     * */
    public boolean hasTarget() {
        return target != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
