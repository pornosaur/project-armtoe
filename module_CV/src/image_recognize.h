//
// Created by pornosaur on 8.3.19.
//

#ifndef OPENCV_TEST_IMAGE_RECOGNIZE_H
#define OPENCV_TEST_IMAGE_RECOGNIZE_H


enum TYPE {
    RED,
    BLACK
};

void recognize_circles(cv::Mat &frame, std::vector<cv::Vec3i> &points);

void recognize_grid(cv::Mat &frame);

void recognize_haris(cv::Mat &frame, std::vector<cv::Point> &points);

void drawLine(cv::Vec2f line, cv::Mat &img, cv::Scalar rgb = CV_RGB(0,0,255));

#endif //OPENCV_TEST_IMAGE_RECOGNIZE_H
