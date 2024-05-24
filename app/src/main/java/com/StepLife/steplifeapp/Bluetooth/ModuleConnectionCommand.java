package com.StepLife.steplifeapp.Bluetooth;

//Команды для получения информации с модуля
public final class ModuleConnectionCommand {
    public static final byte START_BYTE = 0x3A;
    public static final byte END_BYTE = 0x0A;
    public static final byte ERROR_BYTE = (byte) 0xFF;
    public static final byte[] CommandToK3M = {0x3A,0x10,0x14,0x03, (byte) 0xD6,0x0A,0x0D};
    public static final byte[] CommandToK3MB = {0x3A,0x10,0x12,0x01, (byte) 0xD6,0x0A,0x0D};
    public static final byte[] CommandToK3MBatteryCharge = {0x3A,0x10,0x16,0x04, (byte) 0xD6,0x0A,0x0D};
    public static final byte[] CommandToCheckConnection = {0x3A,0x10,0x11, (byte) 0xDF,0x0A,0x0D};


}
