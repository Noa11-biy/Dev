package test;

import mesClasses.Adherent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AdherentTest {
    Adherent paul;

    @BeforeEach
    void setUp() {
        paul = new Adherent(36, "DURAND", "Paul");
    }
    /**
     * Test of getNom, getPrenom and getAnnNaiss() methods, of class Adherent.
     */
    @Test
    @DisplayName("Test de récupération des caractéristiques d'un adhérent")
    void test_des_get() {
        String nom_expected = "DURAND";
        String prenom_expected = "Paul";
        int age_expected = 36;

        String nom_result = paul.getNom();
        String prenom_result = paul.getPrenom();
        int age_result = paul.getAge();

        assertEquals(nom_expected,nom_result);
        assertEquals(prenom_expected,prenom_result);
        assertEquals(age_expected,age_result);
    }

    @Disabled
    @Test
    public void testGetIdentifiant() {
        fail("Test à implémenter plus tard");
    }

    /**
     * Test of setNom, setPrenom and setAnnNaiss() methods, of class Adherent.
     */
    @Test
    @DisplayName("Test de définition des caractéristiques d'un adhérent")
    public void test_des_set() {
        // TODO
    }

    /**
     * Test the equals() method on Adherents. Two members are told 'equal' if they
     * have the same name, surname and birth year.
     * Here we test 'paul' with another member who has the same characteristics,
     * and others who have only one different characteristic.
     */
    @Test
    @DisplayName("Test de l'égalité de 2 adhérents")
    void testEquals() {

    }


}