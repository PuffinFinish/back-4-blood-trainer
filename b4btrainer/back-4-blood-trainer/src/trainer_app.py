import sys
import time
from .cheat_engine import CheatEngine
from pynput import keyboard

class TrainerApp:
    """Main application for Back 4 Blood trainer with hotkey support."""

    def __init__(self):
        self.engine = CheatEngine()
        self.running = False
        self.listener = None

    def on_press(self, key):
        """Handle keyboard hotkeys."""
        try:
            if key == keyboard.Key.f1:
                print("Toggling infinite health...")
                self.engine.toggle_cheat("infinite_health")
            elif key == keyboard.Key.f2:
                print("Toggling infinite ammo...")
                self.engine.toggle_cheat("infinite_ammo")
            elif key == keyboard.Key.f3:
                print("Toggling max copper...")
                self.engine.toggle_cheat("max_copper")
            elif key == keyboard.Key.esc:
                print("Exiting trainer...")
                self.stop()
        except AttributeError:
            pass

    def start(self):
        """Start the trainer application."""
        print("Back 4 Blood Trainer v1.0")
        print("Connecting to game...")
        if not self.engine.connect():
            print("Failed to connect to Back 4 Blood. Make sure the game is running.")
            sys.exit(1)
        print("Connected successfully!")
        print("Hotkeys: F1=Infinite Health, F2=Infinite Ammo, F3=Max Copper, ESC=Exit")
        self.running = True
        self.listener = keyboard.Listener(on_press=self.on_press)
        self.listener.start()

        try:
            while self.running:
                status = self.engine.get_status()
                # In a real trainer, you'd apply cheats here by writing memory
                # For demo, we just print status periodically
                print(f"Health: {status['health']}, Ammo: {status['ammo']}, Copper: {status['copper']}")
                time.sleep(5)
        except KeyboardInterrupt:
            self.stop()

    def stop(self):
        """Stop the trainer and clean up."""
        self.running = False
        if self.listener:
            self.listener.stop()
        self.engine.disconnect()
        print("Trainer stopped.")

if __name__ == "__main__":
    app = TrainerApp()
    app.start()
