package com.wstxda.switchai.service;

interface IShizukuService {
    boolean launchActivity(String packageName, String activityName) = 1;
    void destroy() = 16777114;
}