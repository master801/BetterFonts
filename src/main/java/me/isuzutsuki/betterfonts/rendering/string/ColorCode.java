package me.isuzutsuki.betterfonts.rendering.string;

/**
 * Identifies the location and value of a single color code in the original string
 */
final class ColorCode implements Comparable<Integer> {

    /**
     * Bit flag used with renderStyle to request the underline style
     */
    public static final byte UNDERLINE = 1;

    /**
     * Bit flag used with renderStyle to request the strikethrough style
     */
    public static final byte STRIKETHROUGH = 2;

    /**
     * The index into the original string (i.e. with color codes) for the location of this color code.
     */
    public int stringIndex;

    /**
     * The index into the stripped string (i.e. with no color codes) of where this color code would have appeared
     */
    public int stripIndex;

    /**
     * The numeric color code (i.e. index into the colorCode[] array); -1 to reset default color
     */
    public byte colorCode;

    /**
     * Combination of Font.PLAIN, Font.BOLD, and Font.ITALIC specifying font specific syles
     */
    public byte fontStyle;

    /**
     * Combination of UNDERLINE and STRIKETHROUGH flags specifying effects performed by renderString()
     */
    public byte renderStyle;

    /**
     * Performs numeric comparison on stripIndex. Allows binary search on ColorCode arrays in layoutStyle.
     *
     * @param i the Integer object being compared
     *
     * @return either -1, 0, or 1 if this < other, this == other, or this > other
     */
    @Override
    public int compareTo(Integer i) {
        return (stringIndex == i.intValue()) ? 0 : (stringIndex < i.intValue()) ? -1 : 1;
    }

}
