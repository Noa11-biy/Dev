package fr.javacraft.entity;

import org.joml.Vector3f;

/**
 * Entité générique : personnage, mob, animal, projectile...
 * Gère position, rotation, vie, et comportement IA.
 */
public class Entity {

    public enum EntityType {
        PLAYER, PIG, COW, SHEEP, ZOMBIE, SKELETON, SPIDER,
        ENDER_DRAGON, WITHER, ARROW
    }

    public final EntityType type;
    protected Vector3f pos = new Vector3f();
    protected Vector3f vel = new Vector3f();
    protected float yaw, pitch;
    protected float health, maxHealth = 20.0f;
    protected float damage = 1.0f;
    protected int entityId;
    protected boolean alive = true;

    public Entity(EntityType type, Vector3f spawn) {
        this.type = type;
        this.pos.set(spawn);
        this.maxHealth = switch(type) {
            case ENDER_DRAGON -> 200.0f;
            case WITHER       -> 300.0f;
            case SKELETON, ZOMBIE, SPIDER -> 20.0f;
            case COW, PIG, SHEEP -> 10.0f;
            default -> 20.0f;
        };
        this.health = maxHealth;
    }

    public void tick(float dt) {
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;
        pos.z += vel.z * dt;
    }

    public void damage(float amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public Vector3f pos()  { return pos; }
    public float health()  { return health; }
    public float maxHealth(){ return maxHealth; }
    public boolean alive() { return alive; }
}