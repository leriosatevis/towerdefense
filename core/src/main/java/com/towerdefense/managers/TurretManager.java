package com.towerdefense.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.towerdefense.entities.Turret;
import com.towerdefense.input.InputHandler;
import com.towerdefense.world.Level;

public class TurretManager {


    private InputHandler gameInput;
    private Turret selectedTurret;

    public TurretManager (Viewport viewport , Level level) {
        gameInput = new InputHandler() {
            private Vector2 clickOffset = new Vector2();
            private Vector2 clickStart = new Vector2();
            private Turret currentlyDraggedTurret;
            private boolean canDrag = false;

            @Override
            public boolean onMousePressed(int x, int y, int button) {
                if (button == Input.Buttons.LEFT) {
                    canDrag = true;
                    viewport.unproject(clickStart.set(x, y));
                    selectedTurret = null;
                    for (int i = level.getTurrets().size - 1; i >= 0; i--) {
                        Turret turret = level.getTurrets().get(i);
                        if (turret != null && turret.contains(clickStart)) {
                            level.getTurrets().removeValue(turret, true);
                            level.addTurret(turret);
                            System.out.println("Clicked on: " + turret);
                            selectedTurret = turret;
                            currentlyDraggedTurret = turret;
                            // Calculate and store the offset from the turret's center to the click position
                            clickOffset.set(clickStart.x - turret.getPosition().x, clickStart.y - turret.getPosition().y);
                            break;
                        }
                    }
                }
                return true;
            }


            @Override
            public boolean onMouseDrag(int x, int y) {
                if (canDrag && currentlyDraggedTurret != null) {
                    Vector2 newWorldPos = viewport.unproject(new Vector2(x, y));
                    // Adjust the turret's position with the offset
                    currentlyDraggedTurret.setPosition(newWorldPos.x - clickOffset.x, newWorldPos.y - clickOffset.y);
                }
                return false;
            }


            @Override
            public boolean onMouseReleased(int x, int y, int button) {
                canDrag = false;
                if (button == Input.Buttons.LEFT) {
                    if (currentlyDraggedTurret != null) {
                        Vector2 newWorldPos = viewport.unproject(new Vector2(x, y));
                        boolean isOverlapping = false;

                        for (Turret turret : level.getTurrets()) {
                            if (turret != currentlyDraggedTurret && turret.overlaps(currentlyDraggedTurret)) {
                                isOverlapping = true;
                                break;
                            }
                        }

                        if (isOverlapping && !level.overlapAllowed()) {
                            System.out.println("This place is taken by another turret, please place somewhere else.");
                            currentlyDraggedTurret.setPosition(clickStart.x - clickOffset.x, clickStart.y - clickOffset.y); // Reset to original position
                        } else {
                            currentlyDraggedTurret.setPosition(newWorldPos.x - clickOffset.x, newWorldPos.y - clickOffset.y); // Set new position
                        }

                        currentlyDraggedTurret = null;
                        clickOffset.setZero();
                    }
                }
                return false;
            }
        };
    }

    public Turret getSelectedTurret() {
        return selectedTurret;
    }

    public void addToMultiplexer (InputMultiplexer multiplexer) {
        gameInput.addToMultiplexer(multiplexer);
    }
}
