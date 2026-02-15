package com.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BacterialLifeLogicTest {

    // Dimensión controlada para el test
    private final int TEST_DIMENSION = 30;

    //Instanciamos un spy
    @Spy
    BacteriaLifeLogic logic;

    @Test
    void testDimensionVariable(){
        //GIVEN
        logic = new BacteriaLifeLogic(TEST_DIMENSION);
        //WHEN
        int[][] table = logic.generateInitialGen();
        //THEN
        assertEquals(TEST_DIMENSION,table.length); //alto tablero
        assertEquals(TEST_DIMENSION,table[0].length); //ancho tablero
    }

    @Test
    void testRoundVariable(){
        //Given
        logic = new BacteriaLifeLogic(TEST_DIMENSION);
        assertEquals(0,logic.getRound(), "Round 0");

        int [][] mockTable = new int [TEST_DIMENSION][TEST_DIMENSION];
        //When
        logic.generateNewGen(mockTable); //round 1
        logic.generateNewGen(mockTable); //Round 2

        //then
        assertEquals(2, logic.getRound(), "If round doesnt increment, there's a logic bug");

    }

    @Test
    void testLimitMaxRounds(){
        //Given
        logic = new BacteriaLifeLogic(TEST_DIMENSION);
        int [][] table = new int[TEST_DIMENSION][TEST_DIMENSION];

        for (int i = 0;i < 300;i++){
            logic.generateNewGen(table);
        }

        assertEquals(300,logic.getRound());

        //WHEN
        int[][] resultado = logic.generateNewGen(table);

        //Then
        assertSame(table,resultado, "Si supera ronda maxima no debe cambiar mas la matriz");
    }

    @Test
    void testCorrectDirections(){
        //Given
        logic = new BacteriaLifeLogic(TEST_DIMENSION);
        int[][] table = {
                {1,1,1},
                {1,0,1},
                {1,1,1}
        };
        //When
        int neighbours = logic.checkNeighbours(table,1,1);
        //Then
        assertEquals(8,neighbours);
    }

    @Test
    void  testApplyRules_Nacimiento(){
        //Given
        int[][] table = {
                {0, 1, 0},
                {1, 0, 1},
                {0, 0, 0}
        };
        logic = new BacteriaLifeLogic(3);

        //When
        int[][] secondTable = logic.generateNewGen(table);
        //Then
            //Comprobamos que la celda central ha revivido
        assertEquals(1,secondTable[1][1],"Celda central tiene que revivir -> 3 vecinos vivos" );
    }

    @Test
    void  testApplyRules_LonelinessDeath(){
        //Given
        int[][] table = {
                {1, 0, 0},
                {0, 1, 0}, // El centro es 1
                {0, 0, 0}
        };
        logic = new BacteriaLifeLogic(3);

        //When
        int[][] secondTable = logic.generateNewGen(table);
        //Then
        assertEquals(0, secondTable[1][1], "Celda central debe morir (0) -> tiene menos de 2 vecinos vivos");
    }

    @Test
    void testApplyRules_ZeroNeighbours(){
        //Given
        int[][] table = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        logic = new BacteriaLifeLogic(3);
        //When
        int[][] secondTable = logic.generateNewGen(table);
        //THEN
        assertEquals(0,secondTable[1][1],"Celda central debe morir(0) por falta de vecinos");
    }

    @Test
    void  testApplyRules_OvercrowdingDeath(){
        //Given
        int[][] table = {
                {0, 1, 0},
                {1, 1, 1}, // El centro es 1 y tiene vecinos en Arriba, Izq, Der, Abajo
                {0, 1, 0}
        };
        logic = new BacteriaLifeLogic(3);

        //When
        int[][] secondTable = logic.generateNewGen(table);
        //Then
        assertEquals(0,secondTable[1][1], "Celda central debe morir -> demasiados vecinos");
    }

    @Test
    void  testApplyRules_Survival(){
        //Given
        int[][] table = {
                {1, 0, 0},
                {0, 1, 0}, // El centro es 1
                {0, 0, 1}
        };
        logic = new BacteriaLifeLogic(3);

        //When
        int[][] secondTable = logic.generateNewGen(table);
        //Then
        assertEquals(1,secondTable[1][1], "Celda central con 2 o 3 vecinos -> Sobrevive (1)");
    }

    @Test
    void testCheckStableGen_IsStable(){
        //Given -> Hardcodeamos para evitar el random
        int[][] gen1 = {
                {1, 0},
                {0, 1}
        };
        // Creamos una copia exacta manual
        int[][] gen2 = {
                {1, 0},
                {0, 1}
        };
        //When
        boolean stableGen = logic.checkStableGen(gen1,gen2);
        //Then
        assertTrue(stableGen);
    }

    @Test
    void testCheckStableGen_IsNotStable(){
        int[][] gen1 = {
                {0, 1},
                {1, 0}
        };

        int[][] gen2 = {
                {1, 0},
                {0, 1}
        };
        //When
        boolean stableGen = logic.checkStableGen(gen1,gen2);
        //Then
        assertFalse(stableGen);
    }
}
