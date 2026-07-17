#include "Vector2.hpp"
#include "Solver.hpp"
#include "Renderer.hpp"
#include <iostream>
#include <memory>
#include <cmath>

int main(){
    const double G = 6.674e-11;
    const double m1 = 5.972e24; // Terre
    const double m2 = 7.348e22; // Lune
    const double GM = G * (m1 + m2);

    const double r0 = 3.884e8;              // Distance initiale (m)
    const double v0 = std::sqrt(GM / r0);   // vitesse analytique (orbite circulaire)
    const double T = 2.0 * M_PI * std::sqrt(r0 * r0 * r0 / GM); // periode analytique

    Vector2 r{r0, 0.0};
    Vector2 v{0.0, v0};

    AccelFunc gravity = [GM](const Vector2& r) -> Vector2{
        double dist = r.length();
        return r * (-GM / (dist * dist * dist));
    };

    std::unique_ptr<Solver> solver = std::make_unique<VelocityVerlet>();

    const double dt = T / 10000.0;
    // On avance plusieurs pas physiques par frame rendue : dt reste petit
    // (precision numerique inchangee), mais on "voit" l'orbite en quelques
    // secondes reelles au lieu d'attendre 10000 frames a 60 fps.
    const int stepsPerFrame = 20;

    Renderer renderer(800, 800, "Systeme Terre-Lune (reduit)", r0 * 1.2);

    while (!renderer.shouldClose()) {
        renderer.pollEvents();

        for (int i = 0; i < stepsPerFrame; ++i) {
            solver->step(r, v, dt, gravity);
        }

        renderer.beginFrame();
        renderer.drawPoint(Vector2{0.0, 0.0}, 0.3f, 0.5f, 1.0f); // origine = referentiel reduit (Terre)
        renderer.drawPoint(r, 0.7f, 0.7f, 0.7f);                  // Lune (position relative r)
        renderer.endFrame();
    }

    return 0;
}
