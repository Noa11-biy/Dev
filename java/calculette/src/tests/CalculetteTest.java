package tests;
import calculette.Calculette;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculetteTest {
    private Calculette calc;

    @BeforeEach
    void setUp() {
        calc = new Calculette();
    }

    @Test
    void addition() {
        // ce qui est attendu :
        int expected = 13;
        // test de la fonction :
        int result = calc.addition(5,8);
        // Assertion : compare le résultat à ce qui est attendu
        assertEquals(expected,result);
    }
}