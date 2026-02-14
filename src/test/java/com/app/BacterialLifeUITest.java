package com.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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

}
