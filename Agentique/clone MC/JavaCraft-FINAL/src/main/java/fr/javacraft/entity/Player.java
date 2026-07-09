package fr.javacraft.entity;

import org.joml.Vector3f;

/**
 * Le joueur (entité joueur).
 * Gère la vie, l'inventaire, les dégâts, et l'interaction avec le monde.
 */
public class Player extends Entity {

    public Player() {
        super(EntityType.PLAYER, new Vector3f(8, 80, 8));
        this.health = 20.0f;
        this.maxHealth = 20.0f;
        this.damage = 1.0f;
    }

    private final float[] position = new float[3];

    @Override
    public void tick(float dt) {
        position[0] = pos.x;
        position[1] = pos.y;
        position[2] = pos.z;
    }

    public void setPosition(float x, float y, float z) {
        pos.set(x, y, z);
    }

    public float getX() { return pos.x; }
    public float getY() { return pos.y; }
    public float getZ() { return pos.z; }

    public float[] getPosition() { return new float[]{pos.x, pos.y, pos.z}; }

    @Override
    public void damage(float amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            alive = false;
            System.out.println("[Player] Mort ! Respawn...");
            respawn();
        }
    }

    private void respawn() {
        health = maxHealth;
        pos.set(8, 80, 8);
        alive = true;
    }
}