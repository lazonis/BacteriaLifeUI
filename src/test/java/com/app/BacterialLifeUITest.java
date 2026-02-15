package com.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BacterialLifeUITest {

    @Mock
    BacteriaLifeLogic mockLogic;

    @Spy
    BacteriaLifeUI spyBacteriaLifeUI;

    @BeforeEach
    public void setUp() {
        int[][] emptyTable = new int[30][30];
        when(mockLogic.generateInitialGen()).thenReturn(emptyTable);

        BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);
        spyBacteriaLifeUI= spy(ui);
        spyBacteriaLifeUI.mainFrame.setVisible(false);
    }

    @Test
    void testInitialPainting(){
        // GIVEN
        int [][] emptyTable = new int[30][30];
        emptyTable[0][0] = 1;
        emptyTable[0][1] = 0;

        spyBacteriaLifeUI.bacteriaGen = emptyTable;

        //WHEN
        spyBacteriaLifeUI.refreshGenPanel();

        //THEN
        BacteriaLifeUI.Circle c1 = (BacteriaLifeUI.Circle) spyBacteriaLifeUI.genPanel.getComponent(0);
        BacteriaLifeUI.Circle c2 = (BacteriaLifeUI.Circle) spyBacteriaLifeUI.genPanel.getComponent(1);

        assertEquals(Color.BLACK, c1.getColor(), "La bacteria viva (1) debe ser negra");
        assertEquals(Color.WHITE, c2.getColor(), "La celda muerta (0) debe ser blanca");

    }

    @Test
    void testStartTimer() {
        //Given
        int[][] table = new int[30][30];
        spyBacteriaLifeUI.bacteriaGen = table;

        // Simulamos que la lógica dice que la generación es estable
        when(mockLogic.generateNewGen(any(int[][].class))).thenReturn(table);
        //checkStableGen recibe newGen y oldGen (any,any)
        when(mockLogic.checkStableGen(any(int[][].class), any(int[][].class))).thenReturn(true);

        // When
        spyBacteriaLifeUI.startButton.doClick();

        // Then
        // Verificamos que se llamó a checkStableGen
        verify(mockLogic, timeout(500)).checkStableGen(any(int[][].class), any(int[][].class));
    }

    @Test
    void testCircleComponent() {
        // GIVEN
        // instancia de la clase Circle
        BacteriaLifeUI.Circle circle = new BacteriaLifeUI.Circle(Color.RED);
        // Un mock de Graphics
        Graphics graphicsMock = mock(Graphics.class);

        //WHEN
        // Obtenemos el tamaño y forzamos el pintado
        Dimension dim = circle.getPreferredSize();
        circle.paintComponent(graphicsMock);

        // THEN
        // tamaño debe ser 10x10 (BACTERIA_SIZE)
        assertEquals(10, dim.width);
        assertEquals(10, dim.height);

        //objeto Graphics para pintar un óvalo rojo
        verify(graphicsMock).setColor(Color.RED);
        verify(graphicsMock).fillOval(0, 0, 10, 10);

    }

    @Test
    void testSetCircleColor() {
        // GIVEN
        BacteriaLifeUI.Circle circle = new BacteriaLifeUI.Circle(Color.WHITE);

        BacteriaLifeUI.Circle spyCircle = spy(circle);

        //WHEN
        spyCircle.setCircleColor(Color.BLACK);

        //THEN
        assertEquals(Color.BLACK, spyCircle.getColor());
        verify(spyCircle).repaint();
    }

    @Test
    void testDeepCopy_NullCase() {
        // GIVEN
        int[][] inputNull = null;

        // WHEN
        int[][] resultado = spyBacteriaLifeUI.deepCopy(null);

        // THEN
        assertNull(resultado);
    }

    @Test
    void testDeepCopy_Equals() {
        // GIVEN
        int[][] original = {
                {1, 0},
                {0, 1}
        };

        // WHEN
        int[][] copia = spyBacteriaLifeUI.deepCopy(original);

        //THEN
        assertArrayEquals(original, copia);

        assertNotSame(original, copia);

    }

    @Test
    void testDeepCopy_NotSameInMemory(){
        // GIVEN
        int[][] original = {
                {1, 0},
                {0, 1}
        };

        // WHEN
        int[][] copia = spyBacteriaLifeUI.deepCopy(original);

        //When
        copia[0][0] = 9;

        //Then
        assertEquals(1, original[0][0]);
        assertEquals(9, copia[0][0]);
    }

}
