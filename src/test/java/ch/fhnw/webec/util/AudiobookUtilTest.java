package ch.fhnw.webec.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudiobookUtilTest {

    @Test
    void getFileEnding_withMoreDots() {
        String filename ="Test.mp3";
        assertEquals(AudiobookUtil.getFileEnding(filename),".mp3");
    }
}