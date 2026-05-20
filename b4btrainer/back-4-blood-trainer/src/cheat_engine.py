from .memory_reader import MemoryReader
from typing import Dict, Any

class CheatEngine:
    """Core trainer logic for Back 4 Blood."""

    # Example static addresses (would be dynamic in real trainer via pointer scans)
    HEALTH_OFFSET = 0x00A1B2C3
    AMMO_OFFSET = 0x00D4E5F6
    COPPER_OFFSET = 0x00G7H8I9

    def __init__(self):
        self.memory = MemoryReader()
        self.active_cheats: Dict[str, bool] = {
            "infinite_health": False,
            "infinite_ammo": False,
            "max_copper": False
        }

    def connect(self) -> bool:
        """Attempt to connect to the game process."""
        return self.memory.open_process()

    def get_health(self) -> int:
        """Read current health value."""
        val = self.memory.read_int(self.HEALTH_OFFSET)
        return val if val is not None else 0

    def get_ammo(self) -> int:
        """Read current ammo value."""
        val = self.memory.read_int(self.AMMO_OFFSET)
        return val if val is not None else 0

    def get_copper(self) -> int:
        """Read current copper amount."""
        val = self.memory.read_int(self.COPPER_OFFSET)
        return val if val is not None else 0

    def toggle_cheat(self, cheat_name: str) -> bool:
        """Toggle a cheat on/off."""
        if cheat_name in self.active_cheats:
            self.active_cheats[cheat_name] = not self.active_cheats[cheat_name]
            return self.active_cheats[cheat_name]
        return False

    def get_status(self) -> Dict[str, Any]:
        """Return current game status and cheat states."""
        return {
            "connected": self.memory.process_handle is not None,
            "health": self.get_health(),
            "ammo": self.get_ammo(),
            "copper": self.get_copper(),
            "cheats": self.active_cheats
        }

    def disconnect(self):
        """Clean up memory reader."""
        self.memory.close()
