package com.team12.flightmanagement.cli;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testMainDoesNotCrash() {
        // Just runs main with no args (not a full coverage test)
        String[] args = {};
        com.team12.flightmanagement.cli.app.Main.main(args);
    }
}

