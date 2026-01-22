package com.clean.learningtest.archunit.application;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyService {
    MyService2 myService2;

    void run() {
        myService2 = new MyService2();
        log.info(myService2.toString());    }
}
