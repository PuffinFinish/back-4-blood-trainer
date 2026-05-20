import unittest
from unittest.mock import patch, MagicMock
from src.cheat_engine import CheatEngine

class TestCheatEngine(unittest.TestCase):
    """Test suite for CheatEngine class."""

    def setUp(self):
        self.engine = CheatEngine()

    def test_initial_cheat_states_off(self):
        """All cheats should start disabled."""
        for cheat, state in self.engine.active_cheats.items():
            with self.subTest(cheat=cheat):
                self.assertFalse(state, f"{cheat} should be False initially")

    def test_toggle_cheat_returns_new_state(self):
        """Toggling a cheat should return its new state."""
        result = self.engine.toggle_cheat("infinite_health")
        self.assertTrue(result)
        result = self.engine.toggle_cheat("infinite_health")
        self.assertFalse(result)

    def test_toggle_invalid_cheat_returns_false(self):
        """Toggling a non-existent cheat should return False."""
        result = self.engine.toggle_cheat("invalid_cheat")
        self.assertFalse(result)

    @patch('src.cheat_engine.MemoryReader')
    def test_get_status_returns_dict(self, mock_memory):
        """get_status should return a dictionary with expected keys."""
        mock_instance = MagicMock()
        mock_instance.process_handle = 12345
        mock_instance.read_int.return_value = 100
        mock_memory.return_value = mock_instance
        self.engine.memory = mock_instance
        status = self.engine.get_status()
        self.assertIn("connected", status)
        self.assertIn("health", status)
        self.assertIn("ammo", status)
        self.assertIn("copper", status)
        self.assertIn("cheats", status)
        self.assertEqual(status["health"], 100)
        self.assertEqual(status["ammo"], 100)
        self.assertEqual(status["copper"], 100)

    def test_connect_calls_open_process(self):
        """connect should call memory.open_process."""
        with patch.object(self.engine.memory, 'open_process', return_value=True) as mock_open:
            result = self.engine.connect()
            self.assertTrue(result)
            mock_open.assert_called_once()

    def test_disconnect_calls_close(self):
        """disconnect should call memory.close."""
        with patch.object(self.engine.memory, 'close') as mock_close:
            self.engine.disconnect()
            mock_close.assert_called_once()

if __name__ == '__main__':
    unittest.main()
