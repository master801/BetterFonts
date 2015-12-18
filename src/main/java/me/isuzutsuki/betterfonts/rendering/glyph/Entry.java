package me.isuzutsuki.betterfonts.rendering.glyph;

/**
 * This class holds information for a glyph about its pre-rendered image in an OpenGL texture. The texture coordinates in
 * this class are normalized in the standard 0.0 - 1.0 OpenGL range.
 */
public class Entry {

    /** The OpenGL texture ID that contains this glyph image. */
    public int textureName;

    /** The width in pixels of the glyph image. */
    public int width;

    /** The height in pixels of the glyph image. */
    public int height;

    /** The horizontal texture coordinate of the upper-left corner. */
    public float u1;

    /** The vertical texture coordinate of the upper-left corner. */
    public float v1;

    /** The horizontal texture coordinate of the lower-right corner. */
    public float u2;

    /** The vertical texture coordinate of the lower-right corner. */
    public float v2;

}
