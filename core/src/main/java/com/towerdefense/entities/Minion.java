package com.towerdefense.entities;

import com.badlogic.gdx.math.Vector2;
import com.towerdefense.enums.MonsterType;
import com.towerdefense.mediators.Renderable;
import com.towerdefense.mediators.Target;
import com.towerdefense.renderer.Renderer;

public class Minion implements Target, Renderable {

    String name;
    double health;
    double speed;
    boolean alive;
    Vector2 position;
    double radius;
    MonsterType monsterType;

    public void logic (double delta) {

    }

    @Override
    public void render(Renderer renderer) {

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}
