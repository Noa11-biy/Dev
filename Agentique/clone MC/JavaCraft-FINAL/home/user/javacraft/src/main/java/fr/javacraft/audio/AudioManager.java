package fr.javacraft.audio;

import org.lwjgl.openal.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Audio via OpenAL (LWJGL).
 * TOUT OpenAL se fait sur le thread main (appelee apres glfwMakeContextCurrent).
 * Fail-safe : si OpenAL echoue, le jeu continue sans audio.
 */
public class AudioManager {
    private static final AudioManager INSTANCE = new AudioManager();
    public static AudioManager getInstance() { return INSTANCE; }

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicReference<String> error = new AtomicReference<>(null);
    private int masterGain = 0;
    private final Map<String, Integer> soundBuffers = new HashMap<>();
    private final List<Integer> activeSources = new ArrayList<>();
    private long device = 0;
    private long context = 0;

    public enum Sound {
        BLOCK_BREAK("cassure"), BLOCK_PLACE("placer"),
        STEP("pas"), AMBIENT("ambiance"),
        HIT("coup"), DRAGON_GROWL("dragon"),
        WITHER_SPAWN("wither");

        public final String key;
        Sound(String k) { this.key = k; }
    }

    private AudioManager() {}

    /**
     * Init OpenAL. APPELER depuis le thread main APRES glfwMakeContextCurrent.
     * Ne jette jamais d'exception.
     */
    public void init() {
        if (initialized.getAndSet(true)) return;
        try {
            // Recupere le contexte GL courant (devrait etre celui de la fenetre)
            long currentContext = ALC10.alcGetCurrentContext();
            if (currentContext == 0) {
                System.err.println("[Audio] Aucun contexte OpenGL courant — audio desactive.");
                return;
            }

            long currentDevice = ALC10.alcGetContextsDevice(currentContext);
            if (currentDevice == 0) {
                System.err.println("[Audio] Aucun appareil audio — audio desactive.");
                return;
            }

            ALCCapabilities deviceCaps = ALC.createCapabilities(currentDevice);
            AL.createCapabilities(deviceCaps);
            this.device = currentDevice;
            this.context = currentContext;

            AL10.alListenerf(AL10.AL_GAIN, 0.7f);
            System.out.println("[Audio] OpenAL active. Appareil: " + AL10.alGetString(AL10.AL_RENDERER));
            generateAllSounds();
        } catch (Throwable t) {
            error.set(t.getMessage());
            System.err.println("[Audio] OpenAL non disponible : " + t.getMessage());
            initialized.set(false);
        }
    }

    private void generateAllSounds() {
        generateBlockSound(Sound.BLOCK_BREAK.key,   200, 800, 0.8f, true);
        generateBlockSound(Sound.BLOCK_PLACE.key,   150, 500, 0.5f, false);
        generateBlockSound(Sound.STEP.key,            80,  200, 0.3f, true);
        generateAmbientSound(Sound.AMBIENT.key);
        generateBlockSound(Sound.HIT.key,           100,  300, 0.6f, true);
        generateBlockSound(Sound.DRAGON_GROWL.key,   40,  150, 1.0f, false);
        generateBlockSound(Sound.WITHER_SPAWN.key,   60,  200, 1.0f, false);
    }

    private void generateBlockSound(String key, int lowFreq, int highFreq, float duration, boolean noise) {
        if (!initialized.get()) return;
        try {
            int sampleRate = 44100;
            int numSamples = (int)(sampleRate * duration);
            ByteBuffer buffer = ByteBuffer.allocate(numSamples * 2);
            for (int i = 0; i < numSamples; i++) {
                float t = (float)i / sampleRate;
                float envelope = (float)Math.exp(-t * 8.0f);
                float sample;
                if (noise) {
                    sample = (float)(Math.random() * 2.0 - 1.0) * envelope;
                } else {
                    float freq = lowFreq + (highFreq - lowFreq) * envelope;
                    sample = (float)(Math.sin(2.0 * Math.PI * freq * t)) * envelope;
                }
                buffer.putShort((short)(sample * Short.MAX_VALUE * 0.7f));
            }
            buffer.flip();
            int buf = AL10.alGenBuffers();
            AL10.alBufferData(buf, AL10.AL_FORMAT_MONO16, buffer, sampleRate);
            soundBuffers.put(key, buf);
        } catch (Throwable t) { System.err.println("[Audio] Son " + key + " echec : " + t.getMessage()); }
    }

    private void generateAmbientSound(String key) {
        if (!initialized.get()) return;
        try {
            int sampleRate = 44100;
            float duration = 3.0f;
            int numSamples = (int)(sampleRate * duration);
            ByteBuffer buffer = ByteBuffer.allocate(numSamples * 2);
            for (int i = 0; i < numSamples; i++) {
                float t = (float)i / sampleRate;
                float ambient = (float)Math.sin(2*Math.PI*55*t)*0.3f
                             + (float)Math.sin(2*Math.PI*110*t)*0.15f
                             + (float)Math.sin(2*Math.PI*220*t)*0.08f
                             + (float)(Math.random()*2.0-1.0)*0.05f;
                float loop = (float)Math.sin(2*Math.PI*0.2*t)*0.1f;
                buffer.putShort((short)((ambient+loop) * Short.MAX_VALUE * 0.4f));
            }
            buffer.flip();
            int buf = AL10.alGenBuffers();
            AL10.alBufferData(buf, AL10.AL_FORMAT_MONO16, buffer, sampleRate);
            soundBuffers.put(key, buf);
        } catch (Throwable t) { System.err.println("[Audio] Son ambiance echec : " + t.getMessage()); }
    }

    public void play(Sound sound) { play(sound, 1.0f, 0.8f); }

    public void play(Sound sound, float pitch, float gain) {
        if (!initialized.get()) return;
        try {
            Integer buf = soundBuffers.get(sound.key);
            if (buf == null) return;
            int src = AL10.alGenSources();
            AL10.alSourcei(src, AL10.AL_BUFFER, buf);
            AL10.alSourcef(src, AL10.AL_PITCH, pitch);
            AL10.alSourcef(src, AL10.AL_GAIN, gain);
            AL10.alSourcePlay(src);
            activeSources.add(src);
        } catch (Throwable t) {}
    }

    /** Tick audio — thread main uniquement */
    public void tick() {
        if (!initialized.get()) return;
        try {
            for (int i = activeSources.size() - 1; i >= 0; i--) {
                int src = activeSources.get(i);
                int state = AL10.alGetSourcei(src, AL10.AL_SOURCE_STATE);
                if (state == AL10.AL_STOPPED) {
                    AL10.alDeleteSources(src);
                    activeSources.remove(i);
                }
            }
        } catch (Throwable t) {}
    }

    public void shutdown() {
        if (!initialized.getAndSet(false)) return;
        try {
            for (int src : activeSources) { AL10.alDeleteSources(src); }
        } catch (Throwable t) {}
        activeSources.clear();
        try {
            for (int buf : soundBuffers.values()) { AL10.alDeleteBuffers(buf); }
        } catch (Throwable t) {}
        soundBuffers.clear();
        System.out.println("[Audio] Arrete.");
    }

    public boolean isAvailable() { return initialized.get(); }
}
