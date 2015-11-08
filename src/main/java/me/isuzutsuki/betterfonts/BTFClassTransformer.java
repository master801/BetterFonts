package me.isuzutsuki.betterfonts;

import net.md_5.jbeat.PatcherIO;
import net.minecraft.launchwrapper.IClassTransformer;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class BTFClassTransformer implements IClassTransformer {

//    private static final String PATCH_FILE_PATH = "/patch/FontRenderer.java.patch";
    private static final String PATCH_FILE_PATH = "/patch/FontRenderer.java.bps";

    /**
     * @param obfClassName Obfuscated class name
     * @param className Deobfuscated name (usually the Searge class name)
     * @param bytes The class' data.
     *
     * @return The data for the class.
     */
    @Override
    public byte[] transform(String obfClassName, String className, final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;//Check if the class data is valid.
        byte[] returnData = bytes;
        if (className.equals("net.minecraft.client.gui.FontRenderer")) {
            BetterFontsCore.BETTER_FONTS_LOGGER.info("Patching class \"{}\"...", arg0);
            returnData = beatPatch(className, bytes);
        }
        if (returnData == null || returnData.length <= 0) {//Patching failed
            returnData = bytes;
            BetterFontsCore.BETTER_FONTS_LOGGER.error("Failed to patch class \"{}\".");
        }
        return returnData;
    }

    private byte[] beatPatch(final String className, final byte[] data) {
        InputStream patchFileInputStream = BTFClassTransformer.class.getResourceAsStream(BTFClassTransformer.PATCH_FILE_PATH);
        if (patchFileInputStream == null) {
            BetterFontsCore.BETTER_FONTS_LOGGER.error("Failed to find patch file \"{}\"!", BTFClassTransformer.PATCH_FILE_PATH);
            return data;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PatcherIO patcherIO = new PatcherIO(new ByteArrayInputStream(data), patchFileInputStream, byteArrayOutputStream);
        try {
            patcherIO.patch();
        } catch (IOException e) {
            BetterFontsCore.BETTER_FONTS_LOGGER.error("Caught exception \"{}\" when attempting to patch class \"{}\"!", e, className);
            return data;
        }
        BetterFontsCore.BETTER_FONTS_LOGGER.info("Patched class \"{}\"!", className);
        return byteArrayOutputStream.toByteArray();
    }

}
