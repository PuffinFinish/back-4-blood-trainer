import ctypes
import ctypes.wintypes
from typing import Optional, List

class MemoryReader:
    """Reads process memory for Back 4 Blood trainer."""
    PROCESS_VM_READ = 0x0010
    PROCESS_QUERY_INFORMATION = 0x0400

    def __init__(self, process_name: str = "Back4Blood.exe"):
        self.process_name = process_name
        self.process_handle: Optional[int] = None
        self.kernel32 = ctypes.windll.kernel32

    def open_process(self) -> bool:
        """Open handle to the target process."""
        import psutil
        for proc in psutil.process_iter(['pid', 'name']):
            if proc.info['name'] == self.process_name:
                pid = proc.info['pid']
                self.process_handle = self.kernel32.OpenProcess(
                    self.PROCESS_VM_READ | self.PROCESS_QUERY_INFORMATION,
                    False,
                    pid
                )
                return self.process_handle is not None and self.process_handle > 0
        return False

    def read_int(self, address: int) -> Optional[int]:
        """Read a 4-byte integer from process memory."""
        if not self.process_handle:
            return None
        buffer = ctypes.c_int(0)
        bytes_read = ctypes.c_size_t(0)
        success = self.kernel32.ReadProcessMemory(
            self.process_handle,
            ctypes.c_void_p(address),
            ctypes.byref(buffer),
            ctypes.sizeof(buffer),
            ctypes.byref(bytes_read)
        )
        return buffer.value if success else None

    def read_float(self, address: int) -> Optional[float]:
        """Read a 4-byte float from process memory."""
        if not self.process_handle:
            return None
        buffer = ctypes.c_float(0.0)
        bytes_read = ctypes.c_size_t(0)
        success = self.kernel32.ReadProcessMemory(
            self.process_handle,
            ctypes.c_void_p(address),
            ctypes.byref(buffer),
            ctypes.sizeof(buffer),
            ctypes.byref(bytes_read)
        )
        return buffer.value if success else None

    def close(self):
        """Close the process handle."""
        if self.process_handle:
            self.kernel32.CloseHandle(self.process_handle)
            self.process_handle = None
