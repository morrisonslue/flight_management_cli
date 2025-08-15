package com.team12.flightmanagement.cli;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testMainDoesNotCrash() {
        System.setProperty("cli.test", "true");
        String[] args = {};
        com.team12.flightmanagement.cli.app.Main.main(args);
    }
}


