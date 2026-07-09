package fr.javacraft.render;

import java.nio.IntBuffer;
import org.lwjgl.opengl.GL33;
import static org.lwjgl.opengl.GL33.*;
import org.lwjgl.system.MemoryStack;

/**
 * Gestionnaire de shaders GLSL.
 * Version Batch 1 : supporte maintenant la normale et l'UV atlas
 * (ajout du 3e attribut dans le vertex shader).
 * Les shaders sont inline (pas de fichiers externes).
 */
public class Shader {

    private int program;

    public int getProgram() { return program; }

    public void compile() {
        int vert = compileShader(GL_VERTEX_SHADER, VERTEX_SRC);
        int frag = compileShader(GL_FRAGMENT_SHADER, FRAGMENT_SRC);

        program = glCreateProgram();
        glAttachShader(program, vert);
        glAttachShader(program, frag);
        glLinkProgram(program);

        try (MemoryStack s = MemoryStack.stackPush()) {
            IntBuffer ok = s.ints(1);
            glGetProgramiv(program, GL_LINK_STATUS, ok);
            if (ok.get(0) != GL_TRUE) {
                String log = glGetProgramInfoLog(program);
                throw new RuntimeException("[Shader] Erreur de link:\n" + log);
            }
        }

        glDeleteShader(vert);
        glDeleteShader(frag);
        System.out.println("[Shader] Compile. Program=" + program);
    }

    public void use() { glUseProgram(program); }

    public void delete() { glDeleteProgram(program); }

    // -------------------------------------------------------------------------
    // Vertex shader : x y z | u v | nx ny nz  (8 floats, locations 0, 1, 2)
    // -------------------------------------------------------------------------
    private static final String VERTEX_SRC =
        "#version 330 core\n" +
        "layout(location = 0) in vec3 position;\n" +
        "layout(location = 1) in vec2 uv;\n" +
        "layout(location = 2) in vec3 normal;\n" +

        "uniform mat4 projection;\n" +
        "uniform mat4 view;\n" +
        "uniform mat4 model;\n" +

        "out vec2 vUV;\n" +
        "out vec3 vNormal;\n" +

        "void main() {\n" +
        "    gl_Position = projection * view * model * vec4(position, 1.0);\n" +
        "    vUV = uv;\n" +
        "    vNormal = normal;\n" +
        "}\n";

    // -------------------------------------------------------------------------
    // Fragment shader : eclairage simple lambertien + couleur atlas
    // -------------------------------------------------------------------------
    private static final String FRAGMENT_SRC =
        "#version 330 core\n" +
        "in vec2 vUV;\n" +
        "in vec3 vNormal;\n" +
        "out vec4 fragColor;\n" +

        "uniform sampler2D tex;\n" +
        "uniform float alpha;\n" +

        "void main() {\n" +
        "    vec4 col = texture(tex, vUV);\n" +
        "    if (col.a < 0.01) discard;\n" +
        "    // Eclairage lambertien simple\n" +
        "    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.3));\n" +
        "    float diff = max(dot(vNormal, lightDir), 0.0);\n" +
        "    float ambient = 0.45;\n" +
        "    float lighting = ambient + diff * 0.55;\n" +
        "    fragColor = vec4(col.rgb * lighting, col.a * alpha);\n" +
        "}\n";

    private int compileShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        try (MemoryStack s = MemoryStack.stackPush()) {
            IntBuffer ok = s.ints(1);
            glGetShaderiv(shader, GL_COMPILE_STATUS, ok);
            if (ok.get(0) != GL_TRUE) {
                String log = glGetShaderInfoLog(shader);
                throw new RuntimeException("[Shader] Erreur de compilation "
                    + (type == GL_VERTEX_SHADER ? "vertex" : "fragment") + ":\n" + log);
            }
        }
        return shader;
    }
}
