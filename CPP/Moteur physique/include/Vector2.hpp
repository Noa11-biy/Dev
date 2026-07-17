#pragma once
#include <cmath>

struct Vector2{
    double x = 0.0, y = 0.0;

    Vector2 operator+(const Vector2& o) const { return {x + o.x, y + o.y}; }
    Vector2 operator-(const Vector2& o) const { return {x - o.x, y - o.y}; }
    Vector2 operator*(double s) const { return {x * s, y * s}; }

    double lengthSquared() const { return x * x + y * y; }
    double length() const { return std::sqrt(lengthSquared()); }
};