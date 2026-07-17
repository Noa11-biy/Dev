#pragma once
#include "Vector2.hpp"

struct GLFWwindow;

// Couche de rendu OpenGL. Ne connait rien du Solver ni de la physique :
// elle prend des positions (Vector2, en unites du monde physique, ex. metres)
// et les affiche. Le moteur physique n'inclut jamais ce header.
class Renderer {
public:
    // worldExtent = demi-etendue du monde (en memes unites que les positions)
    // qui doit tenir dans la fenetre, ex. 1.2 * r0 pour voir l'orbite entiere.
    Renderer(int width, int height, const char* title, double worldExtent);
    ~Renderer();

    Renderer(const Renderer&) = delete;
    Renderer& operator=(const Renderer&) = delete;

    bool shouldClose() const;
    void pollEvents();

    void beginFrame();
    void drawPoint(const Vector2& worldPos, float r, float g, float b);
    void endFrame();

private:
    GLFWwindow* window_ = nullptr;
    unsigned int shaderProgram_ = 0;
    unsigned int vao_ = 0;
    unsigned int vbo_ = 0;
    float invExtent_ = 1.0f;

    unsigned int compileShader(unsigned int type, const char* src) const;
    void initGL();
};
