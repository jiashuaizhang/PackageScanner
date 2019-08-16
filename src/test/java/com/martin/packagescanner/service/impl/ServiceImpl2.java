package com.martin.packagescanner.service.impl;

import com.martin.packagescanner.service.BaseService;

public class ServiceImpl2 implements BaseService {

    @Override
    public void execute() {
        System.out.println(getClass().getName());
    }
}
