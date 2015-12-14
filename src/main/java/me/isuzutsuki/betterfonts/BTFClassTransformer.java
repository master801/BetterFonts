package me.isuzutsuki.betterfonts;

import me.isuzutsuki.betterfonts.betterfonts.ConfigParser;
import me.isuzutsuki.betterfonts.betterfonts.StringCache;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import org.slave.lib.api.asm.substitution.Substitution;
import org.slave.lib.api.asm.substitution.Substitution.Insertion;
import org.slave.lib.api.asm.substitution.SubstitutionAnnotations.SubstitutionField;
import org.slave.lib.api.asm.substitution.SubstitutionAnnotations.SubstitutionMethod;
import org.slave.lib.api.asm.substitution.SubstitutionAnnotations.Transcendent;
import org.slave.lib.asm.transformers.substitution.SubstitutionTransformer;

public final class BTFClassTransformer extends SubstitutionTransformer implements IClassTransformer {

    public BTFClassTransformer() {
        super(BetterFontsCore.BETTER_FONTS_LOGGER);
    }

    @Override
    protected String getSubstitutionClassPath() {
        return Internal.class.getName();
    }

    @Override
    protected String getClassName(boolean transformed) {
        return transformed ? "bty" : "net.minecraft.client.gui.FontRenderer";
    }

    @Override
    protected boolean writeAsmFile() {
        return true;
    }

    @Override
    public byte[] transform(String s, String s1, final byte[] bytes) {
        byte[] returnBytes;
        try {
            returnBytes = super.transform(bytes, s, s1);
        } catch(Exception e) {
            returnBytes = bytes;
            getLogger().error("Caught an exception while transforming! Transformer: \"{}\", Exception: \"{}\"", this, e);
        }
        return returnBytes;
    }

    @SuppressWarnings("unused")
    private static final class Internal {

        @SubstitutionField(value = Transcendent.ADD, obfuscatedName = "", unobfuscatedName = "")
        public static boolean betterFontsEnabled = true;

        @SubstitutionField(value = Transcendent.ADD, obfuscatedName = "", unobfuscatedName = "")
        public StringCache stringCache;

        @SubstitutionField(value = Transcendent.ADD, obfuscatedName = "", unobfuscatedName = "")
        public boolean dropShadowEnabled = true;

        @SubstitutionField(value = Transcendent.ADD, obfuscatedName = "", unobfuscatedName = "")
        public boolean enabled = true;

        //<editor-fold desc="Internal">
        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_78295_j", unobfuscatedName = "posX")
        private int posX;

        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_111273_g", unobfuscatedName = "locationFontTexture")
        ResourceLocation locationFontTexture;

        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_78285_g", unobfuscatedName = "colorCode")
        int[] colorCode;
        //</editor-fold>

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "", unobfuscatedName = "")
        public Internal(GameSettings par1, ResourceLocation par2, TextureManager par3, boolean par4) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING, -1);
            BetterFontsCore.BETTER_FONTS_LOGGER.info("Starting BetterFonts...");
            if (locationFontTexture.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && stringCache == null) {
                //Read optional config file to override the default font name/size
                ConfigParser config = new ConfigParser();
                int fontSize = 18;
                boolean antiAlias = false;
                String fontName = "SansSerif";
                if (config.loadConfig("/config/BetterFonts.cfg")) {
                    fontName = config.getFontName("SansSerif");
                    fontSize = config.getFontSize(18);
                    antiAlias = config.getBoolean("font.antialias", false);
                    dropShadowEnabled = config.getBoolean("font.dropshadow", true);
                    BetterFontsCore.BETTER_FONTS_LOGGER.info("Loaded configuration...");
                }
                stringCache = new StringCache(colorCode);
                stringCache.setDefaultFont(fontName, fontSize, antiAlias);
            }
            Substitution.endAdditionalInstructions();
        }

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "func_147647_b", unobfuscatedName = "bidiReorder")
        private String bidiReorder(String par1) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return par1;
            Substitution.endAdditionalInstructions();
            return null;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "func_180455_b", unobfuscatedName = "renderString")
        private int renderString(String par1, float par2, float par3, int oar4, boolean par5) {
            Substitution.startRemoval();
            renderStringAtPos(par1, par5);
            Substitution.endRemoval();

            Substitution.startAdditionalInstructions(Insertion.ENDING);
            if(betterFontsEnabled && stringCache != null) {
                posX += stringCache.renderString(par1, par2, par3, oar4, par5);
            } else {
                renderStringAtPos(par1,par5);
            }
            Substitution.endAdditionalInstructions();
            return -1;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "func_78256_a", unobfuscatedName = "getStringWidth")
        public int getStringWidth(String par1) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.getStringWidth(par1);//First index
            Substitution.endAdditionalInstructions();
            return -1;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "func_78262_a", unobfuscatedName = "trimStringToWidth")
        public String trimStringToWidth(String par1, int par2, boolean par3) {
            Substitution.startAdditionalInstructions(Insertion.ENDING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.trimStringToWidth(par1, par2, par3);//First index
            Substitution.endAdditionalInstructions();
            return null;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, obfuscatedName = "func_78259_e", unobfuscatedName = "sizeStringToWidth")
        private int sizeStringToWidth(String par1, int par2) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.sizeStringToWidth(par1, par2);//First index
            Substitution.endAdditionalInstructions();
            return -1;
        }

        //<editor-fold desc="Internal">
        @SubstitutionMethod(value = Transcendent.INVISIBLE, obfuscatedName = "func_78255_a", unobfuscatedName = "renderStringAtPos")
        private void renderStringAtPos(String par1, boolean par2) {
        }
        //</editor-fold>

    }

}
