package com.martin.packagescanner.service.impl;

import com.martin.packagescanner.service.BaseService;

import java.io.Serializable;

public class OtherServiceImpl implements Serializable {

    private static final long serialVersionUID = 8217774531916588744L;

    public void execute() {
        System.out.println(getClass().getName());
    }
}
