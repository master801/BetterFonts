package me.isuzutsuki.betterfonts.rendering.string;

import me.isuzutsuki.betterfonts.rendering.glyph.Glyph;

import java.lang.ref.WeakReference;

/**
 * This entry holds the layed out glyph positions for the cached string along with some relevant metadata.
 */
public final class Entry {

    /**
     * A weak reference back to the Key object in stringCache that maps to this Entry.
     */
    public WeakReference<Key> keyRef;

    /**
     * The total horizontal advance (i.e. width) for this string in pixels.
     */
    public int advance;

    /**
     * Array of fully layed out glyphs for the string. Sorted by logical order of characters (i.e. glyph.stringIndex)
     */
    public Glyph glyphs[];

    /**
     * Array of color code locations from the original string
     */
    public ColorCode colors[];

    /**
     * True if the string uses strikethrough or underlines anywhere and needs an extra pass in renderString()
     */
    public boolean specialRender;

}
