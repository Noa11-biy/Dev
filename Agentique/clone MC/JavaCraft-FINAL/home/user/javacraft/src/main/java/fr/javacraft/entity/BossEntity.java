package fr.javacraft.entity;

import org.joml.Vector3f;

/**
 * Boss : Ender Dragon et Wither.
 * Combat simplifié : health bar, phases de vol, perch (Dragon),
 * et projectile (Wither).
 */
public class BossEntity extends Entity {

    public enum BossType { ENDER_DRAGON, WITHER }
    private final BossType bossType;

    // Phase pour le Dragon
    private int dragonPhase = 0;
    private int perchTimer  = 0;
    private int breatheCooldown = 0;

    // Phase pour le Wither
    private int witherPhase = 0;
    private float[] witherHeads = new float[3]; // rotation de chaque tête

    public BossEntity(BossType type, Vector3f spawn) {
        super(type == BossType.ENDER_DRAGON ? Entity.EntityType.ENDER_DRAGON : Entity.EntityType.WITHER, spawn);
        this.bossType = type;
        this.maxHealth = (type == BossType.ENDER_DRAGON) ? 200.0f : 300.0f;
        this.health = maxHealth;
        this.damage = 6.0f;
    }

    @Override
    public void tick(float dt) {
        switch (bossType) {
            case ENDER_DRAGON -> tickDragon(dt);
            case WITHER       -> tickWither(dt);
        }
        super.tick(dt);
    }

    private void tickDragon(float dt) {
        breatheCooldown = Math.max(0, breatheCooldown - 1);
        float bh = health / maxHealth;

        if (bh > 0.5f)       dragonPhase = 0; // Vol agressif
        else if (bh > 0.25f) dragonPhase = 1; // Perch attacks
        else                 dragonPhase = 2; // Furieux

        if (dragonPhase == 1 && perchTimer < 200) {
            perchTimer++;
            vel.set(0, 0, 0); // Hover en l'air
        } else {
            perchTimer = 0;
        }

        if (breatheCooldown == 0 && dragonPhase >= 1) {
            breatheCooldown = 120;
            System.out.println("[Dragon] Souffle de blaze !");
        }
    }

    private void tickWither(float dt) {
        float bh = health / maxHealth;
        if (bh < 0.66f && witherPhase == 0) witherPhase = 1;
        if (bh < 0.33f && witherPhase == 1) witherPhase = 2;

        for (int i = 0; i < 3; i++) {
            witherHeads[i] += dt * (1.0f + i * 0.3f);
        }
    }

    @Override
    public void damage(float amount) {
        super.damage(amount);
        System.out.println("[Boss:" + bossType + "] HP=" + (int)health + "/" + (int)maxHealth);
        if (!alive) {
            System.out.println("[Boss:" + bossType + "] VAINCU !");
        }
    }

    public int getPhase()       { return dragonPhase; }
    public int getWitherPhase() { return witherPhase; }
    public BossType getBossType(){ return bossType; }
}