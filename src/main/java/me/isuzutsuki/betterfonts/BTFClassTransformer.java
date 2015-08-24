package me.isuzutsuki.betterfonts;

import net.minecraft.launchwrapper.IClassTransformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BTFClassTransformer implements IClassTransformer {

    /**
     * @param arg0 Deobfuscated name (usually the Searge class name)
     * @param arg1 Obfuscated name
     * @param arg2 The class' data.
     *
     * @return The data for the class.
     */
    @Override
    public byte[] transform(String arg0, String arg1, byte[] arg2) {
        if (arg2 == null || arg2.length <= 0) return null;//Check if the class data is valid.
        if (arg0.equals("net.minecraft.client.gui.FontRenderer")) {
            BetterFontsCore.BETTER_FONTS_LOGGER.info("Transformer is about to patch class: {}", arg0);
            arg2 = patchClassInJar(arg0, arg2, arg1, BetterFontsCore.location);
        }
        return arg2;
    }

    @Deprecated
    public byte[] patchClassInJar(String name, byte[] bytes, String ObfName, File location) {
        try {
            //open the jar as zip
            ZipFile zip = new ZipFile(location);
            ZipEntry entry = zip.getEntry(name.replace('.', '/') + ".class");
            if (entry == null) {
                BetterFontsCore.BETTER_FONTS_LOGGER.error("{} not found in {}", name, location.getName());
            } else {
                //serialize the class file into the bytes array
                InputStream zin = zip.getInputStream(entry);
                int size = (int) entry.getSize();
                byte[] newbytes = new byte[size];
                int pos = 0;
                while (pos < size) {
                    final int len = zin.read(newbytes, pos, size - pos);
                    if (len == 0) throw new IOException();
                    pos += len;
                }
                if(!Arrays.equals(newbytes, bytes)) bytes = newbytes;
                zin.close();
                BetterFontsCore.BETTER_FONTS_LOGGER.info("Class {} was patched!", name);
            }
            zip.close();
        } catch (Exception e) {
            throw new RuntimeException("Error overriding " + name + " from " + location.getName(), e);
        }
        return bytes;
    }

}
