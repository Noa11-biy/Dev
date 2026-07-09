package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringTest {
    private String start;

    @BeforeEach
    void init(){
        start = "Bonjour les amis";
    }

    @Test
    void toUpperCase(){
        String expected = "BONJOUR LES AMIS";
        String result = start.toUpperCase();
        assertEquals(expected,result);
        assertFalse(start.toUpperCase().equals(start));
    }


    @Test
    void compareTo(){

    }

    @Test
    void toCharArray(){

    }


    @Test
    void testSubString_leveeException_avecIndNega_ou_Trop_Gros(){
        assertThrows(IndexOutOfBoundsException.class,
                () -> start.substring(2,5));
    }

}
