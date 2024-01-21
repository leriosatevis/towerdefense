package com.towerdefense.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.towerdefense.enums.MonsterType;
import com.towerdefense.renderer.Renderer;

public class WildCookoo extends Minion {

    private double maxHp;
    private Color outlineColor = new Color(Color.RED);
    private Color fillColor = new Color(Color.BLACK);


    public WildCookoo(String name , MonsterType monsterType , double health, double radius) {
        this.name = name;
        this.monsterType = monsterType;
        this.health = health;
        this.maxHp = health;
        this.alive = health > 0;
        this.position = new Vector2();
        this.radius = radius;
    }

    @Override
    public void logic(double delta) {
        if (health <= 0) alive = false;
    }

    public WildCookoo setPosition (double x , double y) {
        position.set((float) x, (float) y);
        return this;
    }

    public WildCookoo setHealth (double health) {
        this.health = health;
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        if (isAlive()) {
            renderer
                // draw the turret fill color
                .setColor(outlineColor)
                .fillOval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 50)
                // draw the turret outline
                .setColor(fillColor)
                .oval(position.x, position.y, radius * 2.0, radius * 2.0, 0, 0, 0, true, 1, 1, 1, 50)
                .text(name + "\nHP[" + health + "/" + maxHp + "]" , position.x , position.y + radius + 28 , 0 , String.valueOf(name + "\nHP[" + health + "/" + maxHp + "]").length() , Align.center , false , 0 , null , 0.125 , 0.125);
        }
    }


    public MonsterType getMonsterType () {
        return monsterType;
    }

    public boolean isAlive() {
        return alive;
    }
}
