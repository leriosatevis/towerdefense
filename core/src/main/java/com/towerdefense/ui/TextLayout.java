package com.towerdefense.ui;

import com.badlogic.gdx.utils.Align;

public enum TextLayout {
    Center(Align.center),
    Top(Align.top),
    Bottom(Align.bottom),
    Left(Align.left),
    Right(Align.right),
    TopLeft(Align.topLeft),
    TopRight(Align.topRight),
    BottomLeft(Align.bottomLeft),
    BottomRight(Align.bottomRight);


    private int value;

    TextLayout(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
