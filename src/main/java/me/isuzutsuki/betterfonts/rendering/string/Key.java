package me.isuzutsuki.betterfonts.rendering.string;

/**
 * Wraps a String and acts as the key into stringCache. The hashCode() and equals() methods consider all ASCII digits
 * to be equal when hashing and comparing Key objects together. Therefore, Strings which only differ in their digits will
 * be all hashed together into the same entry. The renderString() method will then substitute the correct digit glyph on
 * the fly. This special digit handling gives a significant speedup on the F3 debug screen.
 */
final class Key {

    /**
     * A copy of the String which this Key is indexing. A copy is used to avoid creating a strong reference to the original
     * passed into renderString(). When the original String is no longer needed by Minecraft, it will be garbage collected
     * and the WeakHashMaps in StringCache will allow this Key object and its associated Entry object to be garbage
     * collected as well.
     */
    public String str;

    /**
     * Computes a hash code on str in the same manner as the String class, except all ASCII digits hash as '0'
     *
     * @return the augmented hash code on str
     */
    @Override
    public int hashCode() {
        int code = 0, length = str.length();

        /*
        * True if a section mark character was last seen. In this case, if the next character is a digit, it must
        * not be considered equal to any other digit. This forces any string that differs in color codes only to
        * have a separate entry in the StringCache.
        */
        boolean colorCode = false;

        for(int index = 0; index < length; index++) {
            char c = str.charAt(index);
            if (c >= '0' && c <= '9' && !colorCode) {
                c = '0';
            }
            code = (code * 31) + c;
            colorCode = (c == '\u00A7');
        }

        return code;
    }

    /**
     * Compare str against another object (specifically, the object's string representation as returned by toString).
     * All ASCII digits are considered equal by this method, as long as they are at the same index within the string.
     *
     * @return true if the strings are the identical, or only differ in their ASCII digits
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        /*
        * There seems to be a timing window inside WeakHashMap itself where a null object can be passed to this
        * equals() method. Presumably it happens between computing a hash code for the weakly referenced Key object
        * while it still exists and calling its equals() method after it was garbage collected.
        */
        if (o == null) {
            return false;
        }

        /* Calling toString on a String object simply returns itself so no new object allocation is performed */
        String other = o.toString();
        int length = str.length();

        if (length != other.length()) {
            return false;
        }

        /*
        * True if a section mark character was last seen. In this case, if the next character is a digit, it must
        * not be considered equal to any other digit. This forces any string that differs in color codes only to
        * have a separate entry in the StringCache.
        */
        boolean colorCode = false;

        for(int index = 0; index < length; index++) {
            char c1 = str.charAt(index);
            char c2 = other.charAt(index);

            if (c1 != c2 && (c1 < '0' || c1 > '9' || c2 < '0' || c2 > '9' || colorCode)) {
                return false;
            }
            colorCode = (c1 == '\u00A7');
        }

        return true;
    }

    /**
     * Returns the contained String object within this Key.
     *
     * @return the str object
     */
    @Override
    public String toString() {
        return str;
    }
}
