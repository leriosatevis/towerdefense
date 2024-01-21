package com.towerdefense.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.towerdefense.renderer.Renderer;

public class UiLabel extends Element {

    private String text;
    private Color textColor;
    private Color background;
    private Color outline;
    private GlyphLayout layout;
    private boolean visible = true;


    public UiLabel(String text) {
        this.text = text;
        textColor = new Color(Color.WHITE);
        background = new Color(Color.valueOf("000000A5"));
        outline = new Color(Color.GOLD);
        layout = new GlyphLayout();
    }


    public String getText() {
        return text;
    }

    public UiLabel setText(String text) {
        this.text = text;
        return this;
    }


    public boolean isVisible() {
        return visible;
    }

    public UiLabel setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        float scaleX = 0.125f;
        float scaleY = 0.125f;
        renderer.getFont().getData().setScale(scaleX , scaleY);
        layout.setText(renderer.getFont() , text);

        if (visible)
        renderer
            .setColor(background)
            .fillRectangle(position.x, position.y,  size.x ,size.y , 0 , 0 ,0 , false , 1 , 1)
            .setColor(outline)
            .rectangle(position.x, position.y,  size.x ,size.y , 0 , 0 ,0 , false , 1 , 1,0.5)
            .setColor(textColor)
            .text(text , position.x + (size.x - layout.width) / 2f, position.y + layout.height + (size.y - layout.height) / 2f, 0 , text.length() , Align.center , false , layout.width , null , scaleX , scaleY);

    }
}
