//
// Created by pornosaur on 8.3.19.
//

#include <iostream>
#include <opencv2/opencv.hpp>
#include <string>
#include <thread>


#include "image_recognize.h"

int main() {
    cv::VideoCapture cap;
    if (!cap.open(0))
        return 0;

    unsigned long i = 0;
 
    std::vector<cv::Point> points_grid;
    while (i != 80) {
        cv::Mat color, grey;
        cap >> color;

        points_grid.clear();
        recognize_haris(color, points_grid);

        imshow("Frame", color);

        if (cv::waitKey(200) == 27) break;

        i = points_grid.size();
    }

    cv::Point smallp = points_grid[0];

    int x_gap = 60;
    int y_gap = 60;

    for (;;) {
        cv::Mat color, grey;
        cap >> color;

        /*for (auto &vec : points_grid) {
            if (smallp.x > vec.x) {
                smallp.x = vec.x;
            }
            if (smallp.y > vec.y) {
                smallp.y = vec.y;
            }
            cv::circle(color, vec, 2, {0, 255, 0});
        }

        cv::circle(color, smallp, 10, {255, 0, 0});*/

        if (color.empty()) {
         //   std::cout << "End of video stream!" << std::endl;
            break;
        }

        std::vector<cv::Vec3i> points;
        points.clear();
        recognize_circles(color, points);
       /* int board[9][7] = {0};

        for (auto &v : points) {
            int x, y;

            x = cv::abs(v[0] - smallp.x) / x_gap;
            y = cv::abs(v[1] - smallp.y) / y_gap;
         //   std::cout << "X: " << x << " y:" << y << std::endl;

            board[x][y] = v[2];
        }

        std::string board_str;
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                //std::cout << " " << j[i];
                board_str += std::to_string(board[i][j]) + ",";
            }
         //   std::cout << "" << std::endl;
        }
        std::cout << board_str << std::endl;
     //   std::cout << "------------------------------" << std::endl;
*/
        imshow("Frame", color);

        if (cv::waitKey(10) == 27) break; // stop capturing by pressing ESC
    }

    return 0;
}

void recognize_haris(cv::Mat &frame, std::vector<cv::Point> &points) {
    cv::Mat grey, circles;
    frame.copyTo(grey);
    cv::cvtColor(grey, grey, cv::COLOR_BGR2GRAY);
    cv::GaussianBlur(grey, grey, {7, 7}, 0);

    grey.convertTo(grey, CV_32F);


    cv::cornerHarris(grey, grey, 3, 3, 0.13);

    cv::dilate(grey, grey, cv::Mat());
    cv::dilate(grey, grey, cv::Mat());
    cv::GaussianBlur(grey, grey, {3, 3}, 0);

    cv::threshold(grey, grey, 165, 255, cv::THRESH_BINARY);


    std::vector<std::vector<cv::Point>> contours;
    std::vector<cv::Vec4i> hierarchy;
    grey.convertTo(grey, CV_8UC1);

    cv::findContours(grey, contours, hierarchy, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE);

    for (const auto &contour : contours) {
        double area = cv::contourArea(contour);
        cv::Scalar color = cv::Scalar(rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255));
        if (area > 50 && area < 185) {
            auto m = cv::moments(contour);
            double cx = m.m10 / m.m00;
            double cy = m.m01 / m.m00;
            points.emplace_back((int) cx, (int) cy);
            cv::circle(frame, {(int) cx, (int) cy}, 5, {0, 255, 0});
        }
    }

    grey.convertTo(grey, CV_8U);
}

void recognize_grid(cv::Mat &frame) {

}

void recognize_circles(cv::Mat &frame, std::vector<cv::Vec3i> &points) {
    cv::Mat grey, hsv, m1, m2;

    cv::cvtColor(frame, hsv, cv::COLOR_BGR2HSV);
    cv::cvtColor(frame, grey, cv::COLOR_BGR2GRAY);
    cv::medianBlur(grey, grey, 3);
    cv::Canny(grey, grey, 120, 200);

    //cv::adaptiveThreshold(grey, grey, 255, cv::ADAPTIVE_THRESH_MEAN_C, cv::THRESH_BINARY_INV, 31, 5);
    cv::threshold(grey, grey, 80, 255, cv::THRESH_BINARY | cv::THRESH_OTSU);

    std::vector<cv::Vec3f> circles;
    cv::Mat kernel = cv::getStructuringElement(cv::MORPH_CROSS, {3, 3});
    cv::morphologyEx(grey, grey, cv::MORPH_CLOSE, kernel, {-1, -1}, 2);

    std::vector<std::vector<cv::Point>> contours;
    std::vector<cv::Vec4i> hierarchy;
    cv::findContours(grey, contours, hierarchy, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE);

    for (int i = 1; i < contours.size(); i++) {
        if (contours[i].size() >= 5) {
            auto rect = cv::fitEllipse(contours[i]);

            double ecc = rect.size.height / rect.size.width;
            //std::cout << "ECC " << ecc <<std::endl;
            if (ecc <= 1.2 && rect.size.area() <= 2000 && rect.size.area() >= 1200) {
                 cv::ellipse(frame, rect, {255, 0, 0}, 2);


              //  std::cout << rect.center << std::endl;
                if (rect.center.x >= 0 && rect.center.y >= 0 && !isnan(rect.center.x) && !isnan(rect.center.y)) {
                    auto color = hsv.at<cv::Vec3b>(rect.center);

                   // std::cout << "COLOR: " << color << std::endl;
                    if (color[1] >= 120 && color[2] >= 160) {
                       // std::cout << "RED" << std::endl;
                        points.push_back({(int) rect.center.x, (int) rect.center.y, 1});
                    } else {
                        points.push_back({(int) rect.center.x, (int) rect.center.y, 2});
                    }
                }

                //cv::circle(frame, {(int) cx, (int) cy}, 5, {0, 255, 255});
            }
        }

        //frame = grey;
    }
}
