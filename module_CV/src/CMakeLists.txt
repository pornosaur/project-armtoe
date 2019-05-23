cmake_minimum_required(VERSION 3.12)
project(opencv_test)


set(CMAKE_C_COMPILER mpicc)
set(CMAKE_CXX_COMPILER mpicxx)

find_package(OpenCV REQUIRED)
find_package(OpenMP REQUIRED)
set(CMAKE_CXX_STANDARD 17)

include_directories(${OpenCV_INCLUDE_DIRS})

add_executable(opencv_test image_recognize.cpp image_recognize.h)
target_link_libraries(opencv_test ${OpenCV_LIBS} mpi )
