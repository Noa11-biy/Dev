#include "Renderer.hpp"
#include <glad/gl.h>
#include <GLFW/glfw3.h>
#include <iostream>
#include <stdexcept>

namespace {

const char* kVertexSrc = R"(
#version 330 core
layout (location = 0) in vec2 aPos;
uniform float uInvExtent;
void main() {
    gl_Position = vec4(aPos * uInvExtent, 0.0, 1.0);
    gl_PointSize = 8.0;
}
)";

const char* kFragmentSrc = R"(
#version 330 core
out vec4 FragColor;
uniform vec3 uColor;
void main() {
    FragColor = vec4(uColor, 1.0);
}
)";

} // namespace

Renderer::Renderer(int width, int height, const char* title, double worldExtent)
    : invExtent_(static_cast<float>(1.0 / worldExtent))
{
    if (!glfwInit()) {
        throw std::runtime_error("Echec glfwInit");
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    window_ = glfwCreateWindow(width, height, title, nullptr, nullptr);
    if (!window_) {
        glfwTerminate();
        throw std::runtime_error("Echec glfwCreateWindow");
    }

    glfwMakeContextCurrent(window_);
    glfwSwapInterval(1); // vsync

    if (!gladLoadGL((GLADloadfunc)glfwGetProcAddress)) {
        throw std::runtime_error("Echec gladLoadGL");
    }

    initGL();
}

Renderer::~Renderer() {
    if (vbo_) glDeleteBuffers(1, &vbo_);
    if (vao_) glDeleteVertexArrays(1, &vao_);
    if (shaderProgram_) glDeleteProgram(shaderProgram_);
    if (window_) glfwDestroyWindow(window_);
    glfwTerminate();
}

unsigned int Renderer::compileShader(unsigned int type, const char* src) const {
    unsigned int shader = glCreateShader(type);
    glShaderSource(shader, 1, &src, nullptr);
    glCompileShader(shader);

    int success = 0;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &success);
    if (!success) {
        char log[512];
        glGetShaderInfoLog(shader, sizeof(log), nullptr, log);
        std::cerr << "Erreur compilation shader: " << log << "\n";
        throw std::runtime_error("Compilation shader echouee");
    }
    return shader;
}

void Renderer::initGL() {
    glEnable(GL_PROGRAM_POINT_SIZE);
    glClearColor(0.05f, 0.05f, 0.08f, 1.0f);

    unsigned int vs = compileShader(GL_VERTEX_SHADER, kVertexSrc);
    unsigned int fs = compileShader(GL_FRAGMENT_SHADER, kFragmentSrc);

    shaderProgram_ = glCreateProgram();
    glAttachShader(shaderProgram_, vs);
    glAttachShader(shaderProgram_, fs);
    glLinkProgram(shaderProgram_);

    int success = 0;
    glGetProgramiv(shaderProgram_, GL_LINK_STATUS, &success);
    if (!success) {
        char log[512];
        glGetProgramInfoLog(shaderProgram_, sizeof(log), nullptr, log);
        std::cerr << "Erreur link shader: " << log << "\n";
        throw std::runtime_error("Link shader echoue");
    }

    glDeleteShader(vs);
    glDeleteShader(fs);

    glGenVertexArrays(1, &vao_);
    glGenBuffers(1, &vbo_);

    glBindVertexArray(vao_);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_);
    glBufferData(GL_ARRAY_BUFFER, 2 * sizeof(float), nullptr, GL_DYNAMIC_DRAW);
    glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);
    glBindVertexArray(0);
}

bool Renderer::shouldClose() const {
    return glfwWindowShouldClose(window_);
}

void Renderer::pollEvents() {
    glfwPollEvents();
}

void Renderer::beginFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(shaderProgram_);
    glUniform1f(glGetUniformLocation(shaderProgram_, "uInvExtent"), invExtent_);
}

void Renderer::drawPoint(const Vector2& worldPos, float r, float g, float b) {
    float vertex[2] = { static_cast<float>(worldPos.x), static_cast<float>(worldPos.y) };

    glBindBuffer(GL_ARRAY_BUFFER, vbo_);
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(vertex), vertex);

    glUniform3f(glGetUniformLocation(shaderProgram_, "uColor"), r, g, b);

    glBindVertexArray(vao_);
    glDrawArrays(GL_POINTS, 0, 1);
}

void Renderer::endFrame() {
    glfwSwapBuffers(window_);
}
