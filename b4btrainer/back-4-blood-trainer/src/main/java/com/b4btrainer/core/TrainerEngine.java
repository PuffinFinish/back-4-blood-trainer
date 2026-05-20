package com.b4btrainer.core;

import java.util.HashMap;
import java.util.Map;

/**
 * TrainerEngine manages the activation and deactivation of cheats
 * by reading/writing specific memory addresses for Back 4 Blood.
 * Addresses are based on common offsets (example values, not real).
 */
public class TrainerEngine {

    private final MemoryScanner scanner;
    private final Map<String, Long> cheatAddresses;
    private boolean active;

    /**
     * Creates a TrainerEngine bound to a specific process.
     *
     * @param processId The PID of Back 4 Blood.
     */
    public TrainerEngine(int processId) {
        this.scanner = new MemoryScanner(processId);
        this.cheatAddresses = new HashMap<>();
        this.active = false;
        initializeAddresses();
    }

    /**
     * Initializes example memory addresses for cheats.
     * In a real trainer, these would be dynamically resolved via pattern scanning.
     */
    private void initializeAddresses() {
        // Base address offsets (illustrative only)
        long base = 0x00400000L; // Example base
        cheatAddresses.put("infiniteHealth", base + 0x1A2B3C);
        cheatAddresses.put("infiniteAmmo", base + 0x4D5E6F);
        cheatAddresses.put("maxCopper", base + 0x7A8B9C);
    }

    /**
     * Enables infinite health by writing a large value to the health address.
     */
    public void enableInfiniteHealth() {
        long addr = cheatAddresses.get("infiniteHealth");
        scanner.writeInt(addr, 999999);
        System.out.println("[Trainer] Infinite health enabled at 0x" + Long.toHexString(addr));
    }

    /**
     * Disables infinite health by restoring a default value (example).
     */
    public void disableInfiniteHealth() {
        long addr = cheatAddresses.get("infiniteHealth");
        scanner.writeInt(addr, 100);
        System.out.println("[Trainer] Infinite health disabled.");
    }

    /**
     * Enables infinite ammo by writing a large value to the ammo address.
     */
    public void enableInfiniteAmmo() {
        long addr = cheatAddresses.get("infiniteAmmo");
        scanner.writeInt(addr, 9999);
        System.out.println("[Trainer] Infinite ammo enabled.");
    }

    /**
     * Disables infinite ammo.
     */
    public void disableInfiniteAmmo() {
        long addr = cheatAddresses.get("infiniteAmmo");
        scanner.writeInt(addr, 30);
        System.out.println("[Trainer] Infinite ammo disabled.");
    }

    /**
     * Sets copper (in-game currency) to a maximum value.
     */
    public void setMaxCopper() {
        long addr = cheatAddresses.get("maxCopper");
        scanner.writeInt(addr, 99999);
        System.out.println("[Trainer] Copper set to max.");
    }

    /**
     * Activates all cheats.
     */
    public void activateAll() {
        active = true;
        enableInfiniteHealth();
        enableInfiniteAmmo();
        setMaxCopper();
    }

    /**
     * Deactivates all cheats.
     */
    public void deactivateAll() {
        active = false;
        disableInfiniteHealth();
        disableInfiniteAmmo();
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Releases the process handle.
     */
    public void shutdown() {
        deactivateAll();
        scanner.close();
    }
}
