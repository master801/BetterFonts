package me.isuzutsuki.betterfonts;

public final class BTFThaiUtils {

    public static boolean isThaiChar(char character)
    {
        return BTFThaiUtils.getUnicodeFromChar(character) >= 7009 && BTFThaiUtils.getUnicodeFromChar(character) <= 7099;
    }

    public static int getUnicodeFromChar(char character)
    {
        return (int)character + 3424;
    }

}
