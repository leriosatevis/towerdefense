package com.towerdefense.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class Renderer implements Disposable {


    private Matrix4 projectionMatrix;
    private Texture pixelTexture;
    private TextureRegion whitePixel;
    private SpriteBatch batch;
    private Color color = new Color(1, 1, 1, 1);
    // default font is consola with hinting set to auto-medium and size of 100
    private BitmapFont font;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private Texture fontTexture;
    private ShaderProgram shader;
    private ShaderProgram fontShader;


    public Renderer() {
        shader = defaultShader();
        fontShader = defaultFontShader();
        batch = new SpriteBatch(8191, shader);
        // create a white pixel texture
        final Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixelTexture = new Texture(pixmap);
        whitePixel = new TextureRegion(pixelTexture);
        pixmap.dispose();
        // set default font
//        fontGenerator = new FreeTypeFontGenerator(new FileHandle("C:/windows/fonts/segoeui.ttf"));
//        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        fontParameter.size = 100;
//        fontParameter.renderCount = 2;
//        fontParameter.gamma = 1.5f;
//        fontParameter.color = color;
//        fontParameter.magFilter = Texture.TextureFilter.Nearest;
//        fontParameter.minFilter = Texture.TextureFilter.Linear;
//        fontParameter.hinting = FreeTypeFontGenerator.Hinting.AutoMedium;
//        fontParameter.kerning = true;
//        font = fontGenerator.generateFont(fontParameter);
//        font.setUseIntegerPositions(false);
//        fontGenerator.dispose();
        fontTexture = new Texture(Gdx.files.internal("verdana.png"), true); // true enables mipmaps
        fontTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        font = new BitmapFont(Gdx.files.internal("verdana.fnt"), new TextureRegion(fontTexture), false);
        font.setUseIntegerPositions(false);
        System.out.println(font.getCapHeight());
    }


    public Renderer clearScreen(double r, double g, double b, double a) {
        Gdx.gl.glClearColor((float) r, (float) g, (float) b, (float) a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        return this;
    }

    public Renderer clearScreen(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        return this;
    }

    public Renderer setColor(Color color) {
        this.color.set(color);
        return this;
    }

    public Renderer setColor(double r, double g, double b, double a) {
        this.color.set((float) r, (float) g, (float) b, (float) a);
        return this;
    }

    public Renderer setColor(String hexColor) {
        this.color.set(Color.valueOf(hexColor));
        return this;
    }


    // DRAW METHODS
    public Renderer line(double x1, double y1, double x2, double y2, double lineThickness, String hexColor) {
        if (!batch.isDrawing()) return this;

        color.set(Color.valueOf(hexColor));
        double halfThickness = lineThickness / 2.0;

        // Direction from A to C
        double dirX = x2 - x1;
        double dirY = y2 - y1;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        double normalizedDirX = dirX / length;
        double normalizedDirY = dirY / length;

        // Perpendicular direction (parallel to A and C)
        double perpX = normalizedDirY * halfThickness;
        double perpY = -normalizedDirX * halfThickness;

        // Vertex coordinates
        double vertexAX = x1 - perpX;
        double vertexAY = y1 - perpY;
        double vertexBX = x1 + perpX;
        double vertexBY = y1 + perpY;
        double vertexCX = x2 + perpX;
        double vertexCY = y2 + perpY;
        double vertexDX = x2 - perpX;
        double vertexDY = y2 - perpY;

        float[] vertices = new float[20];

        // vertex A
        vertices[0] = (float) vertexAX;
        vertices[1] = (float) vertexAY;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) vertexBX;
        vertices[6] = (float) vertexBY;
        vertices[7] = color.toFloatBits();
        vertices[8] = 1;
        vertices[9] = 0;
        // vertex C
        vertices[10] = (float) vertexCX;
        vertices[11] = (float) vertexCY;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex D
        vertices[15] = (float) vertexDX;
        vertices[16] = (float) vertexDY;
        vertices[17] = color.toFloatBits();
        vertices[18] = 0;
        vertices[19] = 1;

        return drawArray(vertices);
    }

    public Renderer line(double x1, double y1, double x2, double y2, double lineThickness, double r, double g, double b, double a) {
        if (!batch.isDrawing()) return this;

        color.set((float) r, (float) g, (float) b, (float) a);
        double halfThickness = lineThickness / 2.0;

        // Direction from A to C
        double dirX = x2 - x1;
        double dirY = y2 - y1;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        double normalizedDirX = dirX / length;
        double normalizedDirY = dirY / length;

        // Perpendicular direction (parallel to A and C)
        double perpX = normalizedDirY * halfThickness;
        double perpY = -normalizedDirX * halfThickness;

        // Vertex coordinates
        double vertexAX = x1 - perpX;
        double vertexAY = y1 - perpY;
        double vertexBX = x1 + perpX;
        double vertexBY = y1 + perpY;
        double vertexCX = x2 + perpX;
        double vertexCY = y2 + perpY;
        double vertexDX = x2 - perpX;
        double vertexDY = y2 - perpY;

        float[] vertices = new float[20];

        // vertex A
        vertices[0] = (float) vertexAX;
        vertices[1] = (float) vertexAY;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) vertexBX;
        vertices[6] = (float) vertexBY;
        vertices[7] = color.toFloatBits();
        vertices[8] = 1;
        vertices[9] = 0;
        // vertex C
        vertices[10] = (float) vertexCX;
        vertices[11] = (float) vertexCY;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex D
        vertices[15] = (float) vertexDX;
        vertices[16] = (float) vertexDY;
        vertices[17] = color.toFloatBits();
        vertices[18] = 0;
        vertices[19] = 1;

        return drawArray(vertices);
    }

    public Renderer line(double x1, double y1, double x2, double y2, double lineThickness, Color color) {
        if (!batch.isDrawing()) return this;

        double halfThickness = lineThickness / 2.0;

        // Direction from A to C
        double dirX = x2 - x1;
        double dirY = y2 - y1;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        double normalizedDirX = dirX / length;
        double normalizedDirY = dirY / length;

        // Perpendicular direction (parallel to A and C)
        double perpX = normalizedDirY * halfThickness;
        double perpY = -normalizedDirX * halfThickness;

        // Vertex coordinates
        double vertexAX = x1 - perpX;
        double vertexAY = y1 - perpY;
        double vertexBX = x1 + perpX;
        double vertexBY = y1 + perpY;
        double vertexCX = x2 + perpX;
        double vertexCY = y2 + perpY;
        double vertexDX = x2 - perpX;
        double vertexDY = y2 - perpY;

        float[] vertices = new float[20];

        // vertex A
        vertices[0] = (float) vertexAX;
        vertices[1] = (float) vertexAY;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) vertexBX;
        vertices[6] = (float) vertexBY;
        vertices[7] = color.toFloatBits();
        vertices[8] = 1;
        vertices[9] = 0;
        // vertex C
        vertices[10] = (float) vertexCX;
        vertices[11] = (float) vertexCY;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex D
        vertices[15] = (float) vertexDX;
        vertices[16] = (float) vertexDY;
        vertices[17] = color.toFloatBits();
        vertices[18] = 0;
        vertices[19] = 1;

        return drawArray(vertices);
    }

    public Renderer triangle(double x1, double y1, double x2, double y2, double x3, double y3, double lineThickness, Color color) {
        return line(x1, y1, x2, y2, lineThickness, color).line(x2, y2, x3, y3, lineThickness, color).line(x3, y3, x1, y1, lineThickness, color);
    }

    public Renderer triangle(double x1, double y1, double x2, double y2, double x3, double y3, double lineThickness, String hexColor) {
        return line(x1, y1, x2, y2, lineThickness, hexColor).line(x2, y2, x3, y3, lineThickness, hexColor).line(x3, y3, x1, y1, lineThickness, hexColor);
    }

    public Renderer triangle(double x1, double y1, double x2, double y2, double x3, double y3, double lineThickness, double r, double g, double b, double a) {
        return line(x1, y1, x2, y2, lineThickness, r, g, b, a).line(x2, y2, x3, y3, lineThickness, r, g, b, a).line(x3, y3, x1, y1, lineThickness, r, g, b, a);
    }

    public Renderer rectangle(double x, double y, double width, double height, double pivotX, double pivotY, double angleOfRotation, boolean clockwise, double scaleX, double scaleY, double lineThickness) {

        if (scaleX <= 0 || scaleY <= 0) throw new IllegalStateException("Scale must be above 0");
        // scale
        double scaledWidth = width * scaleX;
        double scaledHeight = height * scaleY;

        // scale the pivot too
        pivotX *= scaleX;
        pivotY *= scaleY;

        // un-rotated, scaled vertex positions
        double ax = x;
        double ay = y;
        double bx = ax;
        double by = ay + scaledHeight;
        double cx = ax + scaledWidth;
        double cy = by;
        double dx = cx;
        double dy = ay;

        // rotation
        double angle = Math.toRadians(angleOfRotation);
        if (clockwise) {
            angle = -angle;
        }
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // true pivot (origin)
        double originX = x + pivotX;
        double originY = y + pivotY;

        // rotate and translate back
        double rax = originX + (ax - originX) * cos - (ay - originY) * sin;
        double ray = originY + (ax - originX) * sin + (ay - originY) * cos;
        double rbx = originX + (bx - originX) * cos - (by - originY) * sin;
        double rby = originY + (bx - originX) * sin + (by - originY) * cos;
        double rcx = originX + (cx - originX) * cos - (cy - originY) * sin;
        double rcy = originY + (cx - originX) * sin + (cy - originY) * cos;
        double rdx = originX + (dx - originX) * cos - (dy - originY) * sin;
        double rdy = originY + (dx - originX) * sin + (dy - originY) * cos;

        return
            // A
            line(rax, ray, rbx, rby, lineThickness, color)
                // B
                .line(rbx, rby, rcx, rcy, lineThickness, color)
                // C
                .line(rcx, rcy, rdx, rdy, lineThickness, color)
                // D
                .line(rdx, rdy, rax, ray, lineThickness, color);
    }

    public Renderer oval(double x, double y, double width, double height, double pivotX, double pivotY, double angleOfRotation, boolean clockwise, double scaleX, double scaleY, double lineThickness, int segments) {
        if (scaleX <= 0 || scaleY <= 0) throw new IllegalStateException("Scale must be above 0");
        if (segments < 5)
            throw new IllegalStateException("The minimum number of segments to create an ellipse is 5...");

        // scale the ellipse
        double scaledWidth = width * scaleX;
        double scaledHeight = height * scaleY;

        // compute the base angle of rotation , this will later be used to rotate all points and rotate them around the pivot
        double baseAngleOfRotation = Math.toRadians(clockwise ? -angleOfRotation : angleOfRotation);
        double baseSine = Math.sin(baseAngleOfRotation);
        double baseCosine = Math.cos(baseAngleOfRotation);

        // compute the pivot that is relative to the position of the object
        double originX = x + pivotX;
        double originY = y + pivotY;

        // first we find the angle difference between each segment
        double angleDifference = -Math.toRadians(360.0 / segments);
        for (int i = 0; i < segments; i++) {

            double angle = i * angleDifference;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            // we compute the coordinates for the first point which will be positioned at the top
            // the x and y are the center of the ellipse
            double x1 = (sin * scaledWidth / 2.0) + x;
            double y1 = (cos * scaledHeight / 2.0) + y;

            // now rotate the first point by the given angle around the pivot point

            double rx1 = originX + (x1 - originX) * baseCosine - (y1 - originY) * baseSine;
            double ry1 = originY + (x1 - originX) * baseSine + (y1 - originY) * baseCosine;


            sin = Math.sin(angle + angleDifference);
            cos = Math.cos(angle + angleDifference);

            // now we compute the coordinates for the second point
            double x2 = (sin * scaledWidth / 2.0) + x;
            double y2 = (cos * scaledHeight / 2.0) + y;

            // now rotate the first point by the given angle around the pivot point
            double rx2 = originX + (x2 - originX) * baseCosine - (y2 - originY) * baseSine;
            double ry2 = originY + (x2 - originX) * baseSine + (y2 - originY) * baseCosine;


            line(rx1, ry1, rx2, ry2, lineThickness, color);
        }

        return this;
    }


    public Renderer fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        if (!batch.isDrawing()) return this;
        float[] vertices = new float[20];
        // vertex A
        vertices[0] = (float) x1;
        vertices[1] = (float) y1;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) x2;
        vertices[6] = (float) y2;
        vertices[7] = color.toFloatBits();
        vertices[8] = 0;
        vertices[9] = 1;
        // vertex C
        vertices[10] = (float) x3;
        vertices[11] = (float) y3;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex C copy
        vertices[15] = (float) x3;
        vertices[16] = (float) y3;
        vertices[17] = color.toFloatBits();
        vertices[18] = 1;
        vertices[19] = 0;

        return drawArray(vertices);
    }

    public Renderer fillRectangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (!batch.isDrawing()) return this;
        float[] vertices = new float[20];
        // vertex A
        vertices[0] = (float) x1;
        vertices[1] = (float) y1;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) x2;
        vertices[6] = (float) y2;
        vertices[7] = color.toFloatBits();
        vertices[8] = 0;
        vertices[9] = 1;
        // vertex C
        vertices[10] = (float) x3;
        vertices[11] = (float) y3;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex D
        vertices[15] = (float) x4;
        vertices[16] = (float) y4;
        vertices[17] = color.toFloatBits();
        vertices[18] = 1;
        vertices[19] = 0;

        return drawArray(vertices);
    }

    public Renderer fillRectangle(double x, double y, double width, double height, double pivotX, double pivotY, double angleOfRotation, boolean clockwise, double scaleX, double scaleY) {

        if (scaleX <= 0 || scaleY <= 0) throw new IllegalStateException("Scale must be above 0");
        // scale
        double scaledWidth = width * scaleX;
        double scaledHeight = height * scaleY;

        // scale the pivot too
        pivotX *= scaleX;
        pivotY *= scaleY;

        // un-rotated, scaled vertex positions
        double ax = x;
        double ay = y;
        double bx = ax;
        double by = ay + scaledHeight;
        double cx = ax + scaledWidth;
        double cy = by;
        double dx = cx;
        double dy = ay;

        // rotation
        double angle = Math.toRadians(angleOfRotation);
        if (clockwise) {
            angle = -angle;
        }
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // true pivot (origin)
        double originX = x + pivotX;
        double originY = y + pivotY;

        // rotate and translate back
        double rax = originX + (ax - originX) * cos - (ay - originY) * sin;
        double ray = originY + (ax - originX) * sin + (ay - originY) * cos;
        double rbx = originX + (bx - originX) * cos - (by - originY) * sin;
        double rby = originY + (bx - originX) * sin + (by - originY) * cos;
        double rcx = originX + (cx - originX) * cos - (cy - originY) * sin;
        double rcy = originY + (cx - originX) * sin + (cy - originY) * cos;
        double rdx = originX + (dx - originX) * cos - (dy - originY) * sin;
        double rdy = originY + (dx - originX) * sin + (dy - originY) * cos;

        float[] vertices = new float[20];
        // vertex A
        vertices[0] = (float) rax;
        vertices[1] = (float) ray;
        vertices[2] = color.toFloatBits();
        vertices[3] = 0;
        vertices[4] = 0;
        // vertex B
        vertices[5] = (float) rbx;
        vertices[6] = (float) rby;
        vertices[7] = color.toFloatBits();
        vertices[8] = 0;
        vertices[9] = 1;
        // vertex C
        vertices[10] = (float) rcx;
        vertices[11] = (float) rcy;
        vertices[12] = color.toFloatBits();
        vertices[13] = 1;
        vertices[14] = 1;
        // vertex D
        vertices[15] = (float) rdx;
        vertices[16] = (float) rdy;
        vertices[17] = color.toFloatBits();
        vertices[18] = 1;
        vertices[19] = 0;

        return drawArray(vertices);
    }


    public Renderer fillOval(double x, double y, double width, double height, double pivotX, double pivotY, double angleOfRotation, boolean clockwise, double scaleX, double scaleY, int segments) {
        if (!isRendering()) throw new IllegalStateException("You are currently not rendering. Call start first.");
        if (scaleX <= 0 || scaleY <= 0) throw new IllegalStateException("Scale must be above 0");
        if (segments < 5)
            throw new IllegalStateException("The minimum number of segments to create an ellipse is 5...");

        double scaledWidth = width * scaleX / 2.0;
        double scaledHeight = height * scaleY / 2.0;

        double originX = pivotX + x;
        double originY = pivotY + y;

        double shapeRotationAngle = Math.toRadians(clockwise ? -angleOfRotation : angleOfRotation);
        double baseSin = Math.sin(shapeRotationAngle);
        double baseCos = Math.cos(shapeRotationAngle);

        // arc angle
        double arc = Math.toRadians(360.0 / segments);
        for (int i = 0; i < segments; i++) {
            // we form a triangle with the center of the shape
            double angle = arc * i;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = (sin * scaledWidth) + x;
            double y1 = (cos * scaledHeight) + y;

            // apply rotation relative to the shape's position


            sin = Math.sin(angle + arc);
            cos = Math.cos(angle + arc);

            double x2 = (sin * scaledWidth) + x;
            double y2 = (cos * scaledHeight) + y;


            double rax = originX + (x - originX) * baseCos - (y - originY) * baseSin;
            double ray = originY + (x - originX) * baseSin + (y - originY) * baseCos;
            double rbx = originX + (x1 - originX) * baseCos - (y1 - originY) * baseSin;
            double rby = originY + (x1 - originX) * baseSin + (y1 - originY) * baseCos;
            double rcx = originX + (x2 - originX) * baseCos - (y2 - originY) * baseSin;
            double rcy = originY + (x2 - originX) * baseSin + (y2 - originY) * baseCos;


            fillTriangle(rax, ray, rbx, rby, rcx, rcy);
        }

        return this;
    }

    // draw fonts
    GlyphLayout layout = new GlyphLayout();

    public Renderer text(String text, double x, double y, int start, int end, int alignment, boolean wrap, double wrapWidth, String truncate, double scaleX, double scaleY) {
        final double minScale = 0.05;
        if (scaleX < minScale || scaleY < minScale)
            throw new IllegalStateException("Font scale must equal to or above " + String.valueOf(minScale) + " !");
        final ShaderProgram oldShader = batch.getShader();
        batch.setShader(fontShader);
        font.getData().setScale((float) scaleX, (float) scaleY);
        layout.setText(font, text);
        font.draw(batch, text, (float) x, (float) y, start, end, (float) wrapWidth, alignment, wrap, truncate);
        batch.setShader(oldShader);
        return this;
    }

    // core method for shapes
    private Renderer drawArray(float[] vertices) {
        batch.draw(pixelTexture, vertices, 0, vertices.length);
        return this;
    }


    // essential
    public Renderer start() {
        batch.begin();
        return this;
    }

    public Renderer stop() {
        batch.end();
        return this;
    }

    public Renderer flush() {
        batch.flush();
        return this;
    }

    // getters / setters

    public Renderer enableBlending() {
        batch.enableBlending();
        return this;
    }

    public Renderer disableBlending() {
        batch.disableBlending();
        return this;
    }


    public boolean isRendering() {
        return batch.isDrawing();
    }

    public Renderer setProjectionMatrix(Matrix4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        return this;
    }

    public SpriteBatch batch() {
        return batch;
    }

    public Renderer setFont(BitmapFont font) {
        this.font = font;
        return this;
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
        fontShader.dispose();
        pixelTexture.dispose();
        font.dispose();
    }


    private ShaderProgram defaultShader() {
        String vertexShader = """
            attribute vec4 a_position;
            attribute vec4 a_color;
            attribute vec2 a_texCoord0;
            uniform mat4 u_projTrans;
            varying vec4 v_color;
            varying vec2 v_texCoords;

            void main()
            {
               v_color = a_color;
               v_color.a = v_color.a * (255.0/254.0);
               v_texCoords = a_texCoord0;
               gl_Position =  u_projTrans * a_position;
            }
            """;
        String fragmentShader = """
            #ifdef GL_ES
            #define LOWP lowp
            precision mediump float;
            #else
            #define LOWP
            #endif
            varying LOWP vec4 v_color;
            varying vec2 v_texCoords;
            uniform sampler2D u_texture;
            void main()
            {
              gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
            }
            """;
        return new ShaderProgram(vertexShader, fragmentShader);
    }

    private ShaderProgram defaultFontShader() {
        String vertex = """
            attribute vec4 a_position;
            attribute vec4 a_color;
            attribute vec2 a_texCoord0;
            uniform mat4 u_projTrans;
            varying vec4 v_color;
            varying vec2 v_texCoords;

            void main()
            {
               v_color = a_color;
               v_color.a = v_color.a * (255.0/254.0);
               v_texCoords = a_texCoord0;
               gl_Position =  u_projTrans * a_position;
            }
            """;
        String fragment = """
            #ifdef GL_ES
            precision mediump float;
            #endif

            uniform sampler2D u_texture;

            varying vec4 v_color;
            varying vec2 v_texCoord;

            const float smoothing = 1.0/20.0;

            void main() {
                float distance = texture2D(u_texture, v_texCoord).a;
                float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
                gl_FragColor = vec4(v_color.rgb, v_color.a * alpha);
            }

            """;
        String msdfFragmentShader = """
            #version 120
            #ifdef GL_ES
            precision mediump float;
            #endif

            uniform sampler2D u_texture;
            varying vec4 v_color;
            varying vec2 v_texCoords;
            uniform float u_smoothing = 25;
            uniform float u_weight = 0.01;

            float median(float r, float g, float b) {
                return max(min(r, g), min(max(r, g), b));
            }
            float linearstep(float a, float b, float x) {
                return clamp((x - a) / (b - a), 0.0, 1.0);
            }
            void main() {
                vec4 msdf = texture2D(u_texture, v_texCoords);
                float distance = u_smoothing * (median(msdf.r, msdf.g, msdf.b) + u_weight - 0.5);
                float glyphAlpha = clamp(distance + 0.5, 0.0, 1.0);
                gl_FragColor = vec4(v_color.rgb, glyphAlpha * v_color.a);
            }
            """;
        return new ShaderProgram(vertex, msdfFragmentShader);
    }
}
