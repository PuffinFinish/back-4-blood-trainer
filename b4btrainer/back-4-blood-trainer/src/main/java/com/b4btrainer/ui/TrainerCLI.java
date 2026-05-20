package com.b4btrainer.ui;

import com.b4btrainer.core.TrainerEngine;

import java.util.Scanner;

/**
 * TrainerCLI provides a simple command-line interface to control the trainer.
 * It prompts the user for a process ID and then displays a menu.
 */
public class TrainerCLI {

    private final TrainerEngine engine;

    public TrainerCLI(TrainerEngine engine) {
        this.engine = engine;
    }

    /**
     * Starts the interactive menu loop.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Back 4 Blood Trainer CLI ===");
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1 - Activate All");
            System.out.println("2 - Deactivate All");
            System.out.println("3 - Toggle Infinite Health");
            System.out.println("4 - Toggle Infinite Ammo");
            System.out.println("5 - Set Max Copper");
            System.out.println("0 - Exit");
            System.out.print("Choice: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    engine.activateAll();
                    break;
                case "2":
                    engine.deactivateAll();
                    break;
                case "3":
                    if (engine.isActive()) {
                        engine.disableInfiniteHealth();
                    } else {
                        engine.enableInfiniteHealth();
                    }
                    break;
                case "4":
                    if (engine.isActive()) {
                        engine.disableInfiniteAmmo();
                    } else {
                        engine.enableInfiniteAmmo();
                    }
                    break;
                case "5":
                    engine.setMaxCopper();
                    break;
                case "0":
                    System.out.println("Shutting down trainer...");
                    engine.shutdown();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Back 4 Blood process ID: ");
        int pid = Integer.parseInt(scanner.nextLine().trim());
        TrainerEngine engine = new TrainerEngine(pid);
        TrainerCLI cli = new TrainerCLI(engine);
        cli.start();
    }
}
