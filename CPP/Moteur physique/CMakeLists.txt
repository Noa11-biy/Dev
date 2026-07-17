cmake_minimum_required(VERSION 3.20)
project(PhysicsEngine LANGUAGES CXX C)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# GLFW installé via pacman (MSYS2 UCRT64) :
#   pacman -S mingw-w64-ucrt-x86_64-glfw
find_package(glfw3 REQUIRED)
find_package(OpenGL REQUIRED)

add_executable(${PROJECT_NAME}
    src/main.cpp
    src/Renderer.cpp
    src/glad.c
)

target_include_directories(${PROJECT_NAME} PRIVATE
    include
)

target_link_libraries(${PROJECT_NAME} PRIVATE
    glfw
    OpenGL::GL
)
