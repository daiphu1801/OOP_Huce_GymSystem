package com.gym.oop_huce_gymsystem.util;

import com.gym.oop_huce_gymsystem.controller.RevenuaController.RevenueController;

public class AppContext {
    private static AppContext instance;
    private RevenueController revenueController;

    private AppContext() {
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public void setRevenueController(RevenueController controller) {
        this.revenueController = controller;
    }

    public RevenueController getRevenueController() {
        return revenueController;
    }
}