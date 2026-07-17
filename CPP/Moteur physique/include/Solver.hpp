#pragma once
#include "Vector2.hpp"
#include <functional>

using AccelFunc = std::function<Vector2(const Vector2& r)>;

class Solver{
    public:
    virtual ~Solver() = default;
    virtual void step(Vector2& r, Vector2& v, double dt, const AccelFunc& accel) = 0;
};

class EuleurExplicit : public Solver{
    public :
    void step(Vector2& r, Vector2& v, double dt, const AccelFunc& accel) override {
        Vector2 a = accel(r);
        Vector2 rNew = r + v * dt; // On utilise l'ancienne vitesse
        v = v + a * dt;
        r = rNew;
    }
};

class EuleurSymplectic : public Solver{
    public :
    void step(Vector2& r, Vector2& v, double dt, const AccelFunc& accel) override{
        Vector2 a = accel(r);
        v = v + a * dt; // Vitesse d'abord
        r = r + v * dt; // puis utilisé pour la position
    }
};

class VelocityVerlet : public Solver {
    public :
    void step(Vector2& r, Vector2& v, double dt, const AccelFunc& accel) override{
        Vector2 a0 = accel(r);
        r = r + v * dt + a0 * (0.5 * dt * dt);
        Vector2 a1 = accel(r); // Accélération à la nouvelle position
        v = v + (a0 + a1) * (0.5 * dt);
    }
};