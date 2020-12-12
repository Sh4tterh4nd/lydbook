package ch.fhnw.webec.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudiobookUtilTest {

    @Test
    void getFileEnding_withMoreDots() {
        String filename = "ABC.Test.mp3";
        assertEquals(AudiobookUtil.getFileEnding(filename), ".mp3");
    }

    @Test
    void getFileEnding_withOneDot() {
        String filename = "Test.mp3";
        assertEquals(AudiobookUtil.getFileEnding(filename), ".mp3");
    }

    @Test
    void getFileEnding_withNoDots() {
        String filename = "Testmp3";
        assertEquals(AudiobookUtil.getFileEnding(filename), "");
    }

    @Test
    void isNumeric_withOnlyNumbers() {
        String number = "1234";
        assertTrue(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withDecimalNumber() {
        String number = "12.34";
        assertTrue(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withDecimalNumberDotInFront() {
        String number = ".1234";
        assertTrue(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withDecimalNumberDotInEnd() {
        String number = "1234.";
        assertTrue(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withOnlyLetters() {
        String number = "abc";
        assertFalse(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withLettersNumbersMixed() {
        String number = "abc123";
        assertFalse(AudiobookUtil.isNumeric(number));
    }

    @Test
    void isNumeric_withLettersNumbersPointMixed() {
        String number = "abc.123";
        assertFalse(AudiobookUtil.isNumeric(number));
    }
    @Test
    void isNumeric_withDecimalWithLetter() {
        String number = "l123.4";
        assertFalse(AudiobookUtil.isNumeric(number));
    }
    @Test
    void isNumeric_withNumberWithMultiplePoints() {
        String number = "1.23.4";
        assertFalse(AudiobookUtil.isNumeric(number));
    }

}