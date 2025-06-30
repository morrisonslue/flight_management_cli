package com.team12.flightmanagement.cli;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testMainDoesNotCrash() {
        // super basic test we have here
        String[] args = {};
        com.team12.flightmanagement.cli.app.Main.main(args);
    }
}

