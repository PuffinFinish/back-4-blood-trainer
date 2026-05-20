package com.b4btrainer.core;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 * MemoryScanner provides low-level memory operations for the Back 4 Blood process.
 * It uses JNA to read and write process memory via Win32 API.
 */
public class MemoryScanner {

    private final Pointer processHandle;
    private final int processId;

    /**
     * Constructs a MemoryScanner for a given process ID.
     * Opens a handle to the process with necessary permissions.
     *
     * @param processId The PID of the Back 4 Blood process.
     */
    public MemoryScanner(int processId) {
        this.processId = processId;
        this.processHandle = Kernel32.INSTANCE.OpenProcess(
                WinNT.PROCESS_VM_READ | WinNT.PROCESS_VM_WRITE | WinNT.PROCESS_VM_OPERATION,
                false,
                processId
        );
        if (processHandle == null) {
            throw new RuntimeException("Failed to open process handle. Error: " + Native.getLastError());
        }
    }

    /**
     * Reads an integer value from a specified memory address.
     *
     * @param address The memory address to read from.
     * @return The integer value at that address.
     */
    public int readInt(long address) {
        byte[] buffer = new byte[4];
        IntByReference bytesRead = new IntByReference();
        boolean success = Kernel32.INSTANCE.ReadProcessMemory(
                processHandle,
                new Pointer(address),
                buffer,
                buffer.length,
                bytesRead
        );
        if (!success || bytesRead.getValue() != 4) {
            throw new RuntimeException("Failed to read memory at 0x" + Long.toHexString(address));
        }
        // Little-endian to int
        return (buffer[0] & 0xFF) | ((buffer[1] & 0xFF) << 8) |
               ((buffer[2] & 0xFF) << 16) | ((buffer[3] & 0xFF) << 24);
    }

    /**
     * Writes an integer value to a specified memory address.
     *
     * @param address The memory address to write to.
     * @param value   The integer value to write.
     */
    public void writeInt(long address, int value) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (value & 0xFF);
        buffer[1] = (byte) ((value >> 8) & 0xFF);
        buffer[2] = (byte) ((value >> 16) & 0xFF);
        buffer[3] = (byte) ((value >> 24) & 0xFF);
        IntByReference bytesWritten = new IntByReference();
        boolean success = Kernel32.INSTANCE.WriteProcessMemory(
                processHandle,
                new Pointer(address),
                buffer,
                buffer.length,
                bytesWritten
        );
        if (!success || bytesWritten.getValue() != 4) {
            throw new RuntimeException("Failed to write memory at 0x" + Long.toHexString(address));
        }
    }

    /**
     * Closes the process handle.
     */
    public void close() {
        if (processHandle != null) {
            Kernel32.INSTANCE.CloseHandle(processHandle);
        }
    }

    public int getProcessId() {
        return processId;
    }
}
