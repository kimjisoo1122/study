package com.study.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageHandlerTest {

    @Test
    void pageHandler() {
        PageHandler pageHandler = new PageHandler(2, 125);
        assertEquals(1, pageHandler.getBeginPage());
        assertEquals(13, pageHandler.getMaxPage());
        assertEquals(10, pageHandler.getEndPage());
        assertFalse(pageHandler.isPrevious());
        assertTrue(pageHandler.isNext());
        assertEquals(10, pageHandler.getOffset());

        PageHandler pageHandler2 = new PageHandler(12, 125);
        assertEquals(11, pageHandler2.getBeginPage());
        assertEquals(13, pageHandler2.getMaxPage());
        assertEquals(13, pageHandler2.getEndPage());
        assertTrue(pageHandler2.isPrevious());
        assertFalse(pageHandler2.isNext());
        assertEquals(110, pageHandler2.getOffset());
    }
}