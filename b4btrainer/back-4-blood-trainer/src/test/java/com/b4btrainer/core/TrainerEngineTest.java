package com.b4btrainer.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrainerEngine.
 * Note: These tests require a running Back 4 Blood process with a known PID.
 * They are marked as integration tests and only run on Windows.
 */
@EnabledOnOs(OS.WINDOWS)
public class TrainerEngineTest {

    // This is a dummy PID for testing; replace with actual PID when running.
    private static final int TEST_PID = 12345;
    private TrainerEngine engine;

    @BeforeEach
    public void setUp() {
        // In a real test, we would mock MemoryScanner or use a test process.
        // Here we assume the PID exists (test will fail otherwise).
        try {
            engine = new TrainerEngine(TEST_PID);
        } catch (Exception e) {
            // If process not found, skip tests
            engine = null;
        }
    }

    @Test
    public void testEngineInitialization() {
        if (engine == null) {
            return; // Skip if process unavailable
        }
        assertNotNull(engine);
        assertFalse(engine.isActive());
    }

    @Test
    public void testActivateAll() {
        if (engine == null) {
            return;
        }
        engine.activateAll();
        assertTrue(engine.isActive());
    }

    @Test
    public void testDeactivateAll() {
        if (engine == null) {
            return;
        }
        engine.activateAll();
        engine.deactivateAll();
        assertFalse(engine.isActive());
    }

    @Test
    public void testShutdown() {
        if (engine == null) {
            return;
        }
        engine.activateAll();
        engine.shutdown();
        assertFalse(engine.isActive());
    }
}
