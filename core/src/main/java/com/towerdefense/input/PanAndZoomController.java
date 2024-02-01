package com.towerdefense.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A pan and zoom controller for an Orthographic camera.
 *
 * @author Lerio
 */
public class PanAndZoomController extends InputHandler {


    private OrthographicCamera camera;
    private Viewport viewport;
    private float[] zoomValues = {0.003125f ,0.00625f, 0.0125f, 0.025f, 0.05f, 0.075f, 0.1f, 0.125f, 0.1875f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f, 0.875f, 1f, 1.125f, 1.25f, 1.5f, 1.75f, 2f, 2.25f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f};
    private int zoomIndex = 15;
    /**
     * I made it final because the zoom feels smoother. It feels right :).
     * I think it might work with one of the sine variations as well. Experiment and see.
     */
    private static final Interpolation interpolation = Interpolation.fastSlow;
    private float lerpTime = 0.777f;
    private float lerpProgress = 0;
    private float sourceZoomValue, destinationZoomValue;
    private MouseButton defaultPanButton = MouseButton.Left;

    public PanAndZoomController(Viewport viewport) {
        this.viewport = viewport;
        camera = (OrthographicCamera) viewport.getCamera();
    }


    private Vector2 mousePosition = new Vector2();
    private Vector2 dragStart = new Vector2(), drag = new Vector2();
    private Vector2 mposBeforeZoom = new Vector2(), mposAfterZoom = new Vector2();
    private Vector3 oldCamPos = new Vector3(), newCamPos = new Vector3();

    private boolean isPanning = false;


    public void update(double delta , int x , int y) {
        viewport.unproject(mousePosition.set(x, y));
        camera.update();
        if (isPanning) {
            if (lerpProgress > 0) {
                lerpProgress -= delta;
                float alpha = lerpProgress < 0 ? 1 : 1 - (lerpProgress / lerpTime);
                camera.zoom = interpolation.apply(sourceZoomValue, destinationZoomValue, alpha);
                camera.update();
            }
            viewport.unproject(drag.set(x, y));
            camera.translate(dragStart.x - drag.x, dragStart.y - drag.y);
            camera.update();
        } else {
            if (lerpProgress > 0) {
                lerpProgress -= delta;
                float alpha = lerpProgress < 0 ? 1 : 1 - (lerpProgress / lerpTime);
                camera.zoom = interpolation.apply(sourceZoomValue, destinationZoomValue, alpha);
                camera.position.x = interpolation.apply(oldCamPos.x, newCamPos.x, alpha);
                camera.position.y = interpolation.apply(oldCamPos.y, newCamPos.y, alpha);
            }
        }
    }


    @Override
    public boolean onMouseScroll(int x, int y, ZoomType zoom) {
        if (isPanning) {
            /** Clamp values */
            {
                if (zoom == ZoomType.In && zoomIndex == 0) return false;
                if (zoom == ZoomType.Out && zoomIndex == zoomValues.length - 1) return false;
            }
            switch (zoom) {
                case In -> {
                    decrement();
                }
                case Out -> {
                    increment();
                }
            }
            sourceZoomValue = camera.zoom;
            destinationZoomValue = zoomValues[zoomIndex];
            resetLerpProgress();
        } else {
            {
                /** Clamp values */
                {
                    if (zoom == ZoomType.In && zoomIndex == 0) return false;
                    if (zoom == ZoomType.Out && zoomIndex == zoomValues.length - 1) return false;
                }
                switch (zoom) {
                    // now zoom in can only occur if the zoom index is higher than 0
                    case In -> {
                        decrement();
                    }
                    // now zoom out can only occur if the zoom index is lower than the last index in the array
                    case Out -> {
                        increment();
                    }
                }
                sourceZoomValue = camera.zoom;
                destinationZoomValue = zoomValues[zoomIndex];

                oldCamPos.set(camera.position);
                /** Perform Virtual Zoom */
                viewport.unproject(mposBeforeZoom.set(x, y));
                camera.zoom = destinationZoomValue;
                camera.update();
                viewport.unproject(mposAfterZoom.set(x, y));
                camera.translate(mposBeforeZoom.sub(mposAfterZoom));
                camera.update();
                newCamPos.set(camera.position);
                /** Reset Camera Parameters */
                camera.zoom = sourceZoomValue;
                camera.position.set(oldCamPos);
                camera.update();
                // everytime we want to zoom in/out , we set the progress to the zoom time.
                resetLerpProgress();
            }
        }
        return false;
    }


    private void resetLerpProgress() {
        lerpProgress = lerpTime;
    }

    @Override
    public boolean onMousePressed(int x, int y, MouseButton button) {
        if (button == defaultPanButton) {
            isPanning = true;
            dragStart.set(x, y);
            viewport.unproject(dragStart.set(Gdx.input.getX(), Gdx.input.getY()));
        }
        return false;
    }


    @Override
    public boolean onMouseReleased(int x, int y, MouseButton button) {
        isPanning = false;
        lerpProgress = 0;
        return false;
    }

    /**
     * Increment the zoom index by 1 and clamp it to the index of last element in the zoom values array.
     */
    void increment() {
        zoomIndex++;
    }

    /**
     * Decrement the zoom index by 1 and clamp it to the index of first element in the zoom values array.
     */
    void decrement() {
        zoomIndex--;
    }

    float viewportWidthRatio() {
        return viewport.getWorldWidth() / viewport.getScreenWidth();
    }

    float viewportHeightRatio() {
        return viewport.getWorldHeight() / viewport.getScreenHeight();
    }

    /**
     * The lerp time is the time it takes for the camera to interpolate to a next zoom value / camera position.
     *
     * @default value is 0.777seconds
     * @info clamped to a range 0.05 to 10 seconds.
     */
    public void setLerpTime(float lerpTime) {
        if (lerpTime < 0.05f) lerpTime = 0.05f;
        if (lerpTime > 10f) lerpTime = 10f;
        this.lerpTime = lerpTime;
    }

    public void setDefaultPanButton(MouseButton defaultPanButton) {
        this.defaultPanButton = defaultPanButton;
    }

    /**
     * Finds the closest number in the array that is larger than the given number (n).
     * If a number equal to n exists in the array, returns the number that meets the search criterion.
     * If no larger number is found, returns the original number (n).
     *
     * @param n     the reference number
     * @param array the array of float numbers to search in
     * @return the closest larger number, the number equal to n if it meets the criterion, or the original number (n) if no larger number is found
     */
    public static float findClosestGreaterThan(float n, float[] array) {
        float closestLargerNumber = Float.MAX_VALUE;
        boolean foundLargerNumber = false;
        boolean foundEqualToN = false;

        for (float num : array) {
            if (num == n) {
                foundEqualToN = true;
            } else if (num > n && num < closestLargerNumber) {
                closestLargerNumber = num;
                foundLargerNumber = true;
            }
        }

        if (foundLargerNumber && foundEqualToN && closestLargerNumber != Float.MAX_VALUE) {
            return closestLargerNumber;
        } else {
            return foundLargerNumber ? closestLargerNumber : n;
        }
    }

    /**
     * Finds the closest number in the array that is smaller than the given number (n).
     * If a number equal to n exists in the array, returns the number that meets the search criterion.
     * If no smaller number is found, returns the original number (n).
     *
     * @param n     the reference number
     * @param array the array of float numbers to search in
     * @return the closest smaller number, the number equal to n if it meets the criterion, or the original number (n) if no smaller number is found
     */
    public static float findClosestLesserThan(float n, float[] array) {
        float closestSmallerNumber = Float.MIN_VALUE;
        boolean foundSmallerNumber = false;
        boolean foundEqualToN = false;

        for (float num : array) {
            if (num == n) {
                foundEqualToN = true;
            } else if (num < n && num > closestSmallerNumber) {
                closestSmallerNumber = num;
                foundSmallerNumber = true;
            }
        }

        if (foundSmallerNumber && foundEqualToN && closestSmallerNumber != Float.MIN_VALUE) {
            return closestSmallerNumber;
        } else {
            return foundSmallerNumber ? closestSmallerNumber : n;
        }
    }

}
