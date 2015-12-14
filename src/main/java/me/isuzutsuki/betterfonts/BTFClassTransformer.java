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
    protected boolean writeAsmFile() {
        return true;
    }

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        return super.transform(bytes, s, s1);
    }

    @SuppressWarnings("unused")
    private static final class Internal {

        @SubstitutionField(value = Transcendent.ADD, names = {}, desc = boolean.class)
        public static boolean betterFontsEnabled = true;

        @SubstitutionField(value = Transcendent.ADD, names = {}, desc = StringCache.class)
        public StringCache stringCache;

        @SubstitutionField(value = Transcendent.ADD, names = {}, desc = boolean.class)
        public boolean dropShadowEnabled = true;

        @SubstitutionField(value = Transcendent.ADD, names = {}, desc = boolean.class)
        public boolean enabled = true;

        //<editor-fold desc="Internal">
        @SubstitutionField(value = Transcendent.INVISIBLE, names = {
                "field_78295_j",
                "posX"
        }, desc = int.class)
        private int posX;

        @SubstitutionField(value = Transcendent.INVISIBLE, names = {
                "field_111273_g",
                "locationFontTexture"
        }, desc = ResourceLocation.class)
        ResourceLocation locationFontTexture;

        @SubstitutionField(value = Transcendent.INVISIBLE, names = {
                "field_78285_g",
                "colorCode"
        }, desc = int[].class)
        int[] colorCode;
        //</editor-fold>

        @SubstitutionMethod(value = Transcendent.MERGE, names = {}, desc = {
                GameSettings.class,
                ResourceLocation.class,
                TextureManager.class,
                boolean.class
        })
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

        @SubstitutionMethod(value = Transcendent.MERGE, names = {
                "func_147647_b",
                "bidiReorder"
        }, desc = String.class)
        private String bidiReorder(String par1) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return par1;
            Substitution.endAdditionalInstructions();
            return null;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, names = {
                "func_180455_b",
                "renderString"
        }, desc = {
                String.class,
                float.class,
                float.class,
                int.class,
                boolean.class
        })
        private int renderString(String p_180455_1_, float p_180455_2_, float p_180455_3_, int p_180455_4_, boolean p_180455_5_) {
            Substitution.startRemoval();
            renderStringAtPos(p_180455_1_, p_180455_5_);
            Substitution.endRemoval();

            Substitution.startAdditionalInstructions(Insertion.ENDING);
            if(betterFontsEnabled && stringCache != null) {
                posX += stringCache.renderString(p_180455_1_, p_180455_2_, p_180455_3_, p_180455_4_, p_180455_5_);
            } else {
                renderStringAtPos(p_180455_1_,p_180455_5_);
            }
            Substitution.endAdditionalInstructions();
            return -1;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, names = {
                "func_78256_a",
                "getStringWidth"
        }, desc = String.class)
        public int getStringWidth(String par1) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.getStringWidth(par1);//First index
            Substitution.endAdditionalInstructions();
            return -1;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, names = {
                "func_78262_a",
                "trimStringToWidth"
        }, desc = {
                String.class,
                int.class,
                boolean.class
        })
        public String trimStringToWidth(String par1, int par2, boolean par3) {
            Substitution.startAdditionalInstructions(Insertion.ENDING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.trimStringToWidth(par1, par2, par3);//First index
            Substitution.endAdditionalInstructions();
            return null;
        }

        @SubstitutionMethod(value = Transcendent.MERGE, names = {
                "func_78259_e",
                "sizeStringToWidth"
        }, desc = {
                String.class,
                int.class
        })
        private int sizeStringToWidth(String par1, int par2) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING);
            if (Internal.betterFontsEnabled && stringCache != null) return stringCache.sizeStringToWidth(par1, par2);//First index
            Substitution.endAdditionalInstructions();
            return -1;
        }

        //<editor-fold desc="Internal">
        @SubstitutionMethod(value = Transcendent.INVISIBLE, names = {
                "func_78255_a",
                "renderStringAtPos"
        }, desc = {
                String.class,
                boolean.class
        })
        private void renderStringAtPos(String par1, boolean par2) {
        }
        //</editor-fold>

    }

}
