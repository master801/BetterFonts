package me.isuzutsuki.betterfonts;

public final class BTFThaiUtils {

    public static boolean isThaiChar(char par1)
    {
        return BTFThaiUtils.getUnicodeFromChar(par1) >= 7009 && BTFThaiUtils.getUnicodeFromChar(par1) <= 7099;
    }

    public static int getUnicodeFromChar(char par1)
    {
        return (int)par1 + 3424;
    }

}
