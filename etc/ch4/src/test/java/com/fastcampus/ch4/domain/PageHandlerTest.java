package com.fastcampus.ch4.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class PageHandlerTest {

    @Test
    public void pageTest() {
        PageHandler pageHandler = new PageHandler(256, 15);
        System.out.println("pageHandler = " + pageHandler);
        pageHandler.print();
    }

}