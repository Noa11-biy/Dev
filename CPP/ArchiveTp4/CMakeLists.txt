cmake_minimum_required(VERSION 4.2)
project(C__)

set(CMAKE_CXX_STANDARD 20)

add_executable(C__ main.cpp
        date.cpp
        item.cpp
        item.h
        Liste.cpp
        Liste.h)
