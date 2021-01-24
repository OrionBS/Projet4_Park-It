package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class InteractiveShellTest {

    private static InteractiveShell interactiveShell;
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        interactiveShell = new InteractiveShell();
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    public void loadInterfaceTest() {

    }

}
