package com.towerdefense.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Path {

    private Array<Vector2> points;

    public Path () {
        points = new Array<>();
    }


    public Array<Vector2> getPoints() {
        return points;
    }
}
