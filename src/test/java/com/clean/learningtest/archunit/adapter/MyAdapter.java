package com.clean.learningtest.archunit.adapter;

import com.clean.learningtest.archunit.application.MyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAdapter {
    MyService myService;

    void run () {
        myService = new MyService();
        log.info(myService.toString());
    }
}
