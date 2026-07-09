cmake_minimum_required(VERSION 4.2)
project(TP5)

set(CMAKE_CXX_STANDARD 20)

add_executable(TP5 main.cpp
        Image.cpp
        Image.h
        date.cpp
        date.h
        Album.cpp
        Album.h
)
