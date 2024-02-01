package com.towerdefense.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.towerdefense.renderer.Renderer;

public class UiLabel extends Element {

    private String text;
    private Color textColor;
    private Color background;
    private Color outline;
    private GlyphLayout layout;
    private Vector2 fontScale, textPosition;
    private boolean wrap, truncate, renderText , drawGlyphBounds;
    private String truncation = "...";
    private BitmapFont font;
    private TextLayout textLayout;
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
        layout = new GlyphLayout();
        fontScale = new Vector2(1, 1);
        margin = new Margin(1, 1, 1, 1);
        textLayout = TextLayout.Left;
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

    public UiLabel setTextLayout(TextLayout textLayout) {
        this.textLayout = textLayout;
        return this;
    }

    @Override
    public void logic(double delta) {
        // if the font is bigger than the size of the label ,do not render the text
        renderText = true;//layout.width <= size.x && layout.height <= size.y; // reposition the text based on its alignment within the component

    }

    private Vector2 computeTextOffset() {
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
        if (isVisible())
            renderer
                .setColor(background)
                .fillRectangle(position.x, position.y, size.x, size.y, 0, 0, 0, false, scale.x, scale.y)
                .setColor(outline)
                .rectangle(position.x, position.y, size.x, size.y, 0, 0, 0, false, scale.x, scale.y, 0.25)
                .setColor(textColor);

        final double minScale = 0.05;
        if (fontScale.x < minScale || fontScale.y < minScale)
            throw new IllegalStateException("Font scale must equal to or above " + String.valueOf(minScale) + " !");
        font.getData().setScale(fontScale.x, fontScale.y);
        layout.setText(font, text, 0, text.length(), textColor, layout.width, textLayout.getValue(), wrap, null);
        computeTextOffset();
        font.draw(renderer.batch(), layout, textPosition.x, textPosition.y);

        if (drawGlyphBounds)
            for (GlyphLayout.GlyphRun run : layout.runs) {
                double glyphX = textPosition.x + run.x;
                final double baseline = textPosition.y + run.y;
                for (int i = 0; i < run.glyphs.size; i++) {
                    final BitmapFont.Glyph glyph = run.glyphs.get(i);
                    final double glyphY = baseline + ((glyph.height + glyph.yoffset) * fontScale.y);
                    glyphX += run.xAdvances.get(i);
                    if ((char) glyph.id != ' ')
                       renderer.setColor(glyphBoundLineColor).rectangle(glyphX + glyph.xoffset * fontScale.x, glyphY + font.getAscent(), glyph.width, -glyph.height, 0, 0, 0, false, fontScale.x, fontScale.y, glyphBoundLineThickness);
                }
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
}
