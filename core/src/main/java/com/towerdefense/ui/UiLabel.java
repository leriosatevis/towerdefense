package com.towerdefense.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.towerdefense.renderer.Renderer;

public class UiLabel extends Element {

    private String text;
    private Color textColor;
    private Color background;
    private Color outline;
    private Vector2 fontScale, textPosition;
    private boolean wrap, truncate, renderText, drawGlyphBounds;
    private String truncation = "...";
    private BitmapFont font;
    private TextLayout style;
    private TextAlignment textAlignment;
    private Margin margin;
    private float glyphBoundLineThickness = 1.0f;
    private Color glyphBoundLineColor = new Color(Color.RED);


    public UiLabel(String text, BitmapFont font) {
        this.text = text;
        this.font = font;
        textColor = new Color(Color.WHITE);
        background = new Color(Color.valueOf("000000AA"));
        outline = new Color(Color.GRAY);
        fontScale = new Vector2(1, 1);
        margin = new Margin(1, 1, 1, 1);
        style = TextLayout.Left;
        textAlignment = TextAlignment.BottomLeft;
        textPosition = new Vector2();
    }


    public String getText() {
        return text;
    }

    public UiLabel setText(String text) {
        this.text = text;
        return this;
    }

    public Vector2 getFontScale() {
        return fontScale;
    }

    public UiLabel setFontScale(double scaleX, double scaleY) {
        this.fontScale.set((float) scaleX, (float) scaleY);
        return this;
    }

    public UiLabel setFontScale(double scale) {
        this.fontScale.set((float) scale, (float) scale);
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public UiLabel setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }


    public UiLabel setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    public UiLabel setStyle(TextLayout style) {
        this.style = style;
        return this;
    }

    @Override
    public void logic(double delta) {
        // if the font is bigger than the size of the label ,do not render the text
        renderText = true;//layout.width <= size.x && layout.height <= size.y; // reposition the text based on its alignment within the component

    }

    private Vector2 computeTextPosition(GlyphLayout layout) {
        return switch (textAlignment) {
            case TopLeft ->
                textPosition.set((float) (position.x + margin.left), (float) (position.y + size.y - margin.top));
            case TopRight ->
                textPosition.set((float) (position.x + size.x - layout.width - margin.right), (float) (position.y + size.y - margin.top));
            case TopCenter ->
                textPosition.set(position.x + size.x / 2f - layout.width / 2f, (float) (position.y + size.y - margin.top));
            case Center ->
                textPosition.set(position.x + size.x / 2f - layout.width / 2f, position.y + size.y / 2f + layout.height - layout.height / 2f);
            case CenterLeft ->
                textPosition.set((float) (position.x + margin.left), position.y + size.y / 2f + layout.height - layout.height / 2f);
            case CenterRight ->
                textPosition.set((float) (position.x + size.x - layout.width - margin.right), position.y + size.y / 2f + layout.height - layout.height / 2f);
            case BottomLeft ->
                textPosition.set((float) (position.x + margin.left), (float) (position.y + layout.height - font.getDescent() + margin.bottom));
            case BottomRight ->
                textPosition.set((float) (position.x + size.x - layout.width - margin.right), (float) (position.y + layout.height - font.getDescent() + margin.bottom));
            case BottomCenter ->
                textPosition.set(position.x + size.x / 2f - layout.width / 2f, (float) (position.y + layout.height - font.getDescent() + margin.bottom));
        };
    }


    @Override
    public void render(Renderer renderer) {
        final GlyphLayout layout = renderer.getLayout();
        final ShaderProgram oldShader = renderer.batch().getShader();
        // draw fill
        if (isVisible()) {
            renderer.setColor(background).fillRectangle(position.x, position.y, size.x, size.y, 0, 0, 0, false, scale.x, scale.y);

            final double minScale = 0.01;
            if (fontScale.x < minScale || fontScale.y < minScale)
                throw new IllegalStateException("Font scale must equal to or above " + String.valueOf(minScale) + " !");
            font.getData().setScale(fontScale.x, fontScale.y);

            float usableWidth = (float) (size.x - (margin.left + margin.right));


            layout.setText(font, text, 0, text.length(), textColor, usableWidth, style.getValue(), wrap, wrap ? null : truncation);

            if (layout.height > size.y + margin.top + margin.top) {
                renderText = false;
                System.err.println("Can't fit text in label bounds.");
            } else {
                renderText = true;
            }

            computeTextPosition(layout);

            if (renderText)
                renderer.setShader(renderer.getFontShader())
                    // draw the text
                    .setColor(textColor)
                    .text(layout, textPosition)
                    // switch back to old shader
                    .setShader(oldShader);
            // draw the glyph bounds
            if (drawGlyphBounds) renderer
                .drawGlyphPositions(layout, textPosition, fontScale.x, fontScale.y, glyphBoundLineThickness);
            // draw outline
            renderer.setColor(outline)
                .rectangle(position.x, position.y, size.x, size.y, 0, 0, 0, false, scale.x, scale.y, 0.25);
        }
    }

    public UiLabel setDrawGlyphBounds(boolean drawGlyphBounds) {
        this.drawGlyphBounds = drawGlyphBounds;
        return this;
    }

    public UiLabel setGlyphBoundLineColor(Color glyphBoundLineColor) {
        this.glyphBoundLineColor = glyphBoundLineColor;
        return this;
    }

    public UiLabel setGlyphBoundLineThickness(float glyphBoundLineThickness) {
        this.glyphBoundLineThickness = glyphBoundLineThickness;
        return this;
    }

    public UiLabel setWrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }
}
