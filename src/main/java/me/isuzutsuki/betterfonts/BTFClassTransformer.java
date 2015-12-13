package me.isuzutsuki.betterfonts;

import net.minecraft.launchwrapper.IClassTransformer;
import org.slave.lib.asm.transformers.substitution.SubstitutionTransformer;

public final class BTFClassTransformer extends SubstitutionTransformer implements IClassTransformer {

    public BTFClassTransformer() {
        super(BetterFontsCore.BETTER_FONTS_LOGGER);
    }

    @Override
    protected String getSubstitutionClassPath() {
        return Substitution.class.getName();
    }

    @Override
    protected String getClassName(TransformationType transformationType) {
        switch(transformationType) {
            case UNTRANSFORMED:
                return "bty";
            case TRANSFORMED:
            default:
                return "net.minecraft.client.gui.FontRenderer";
        }
    }

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        return super.transform(bytes, s, s1);
    }

    private static final class Substitution {
    }

}
