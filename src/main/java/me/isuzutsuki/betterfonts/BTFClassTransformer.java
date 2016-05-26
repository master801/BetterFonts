package me.isuzutsuki.betterfonts;

import me.isuzutsuki.betterfonts.rendering.string.StringCache;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
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
    protected String getClassName(boolean isNameTransformed) {
        return isNameTransformed ? "bty" : "net.minecraft.client.gui.FontRenderer";
    }

    @Override
    protected void fixUp(ClassNode classNode) throws Exception {
        final int access = Opcodes.ACC_PUBLIC;
        final String[] methodNames = new String[] {
                "func_175065_a",//Obfuscated
                "drawString"//Deobfuscated
        };
        final String methodDesc = Type.getMethodDescriptor(
                Type.INT_TYPE,
                Type.getType(String.class),
                Type.FLOAT_TYPE,
                Type.FLOAT_TYPE,
                Type.INT_TYPE,
                Type.BOOLEAN_TYPE
        );

        MethodNode searching = null;
        for(MethodNode methodNode : classNode.methods) {
            if (methodNode.access == access && methodNode.desc.equals(methodDesc)) {
                for(String methodName : methodNames) {
                    if (methodName.equals(methodNode.name)) {
                        searching = methodNode;
                        break;
                    }
                }
            }
            if (searching != null) break;
        }
        //Adds a dropShadowEnabled check to the if statement in method drawString(Ljava/lang/String;FFIZ)I
        if (searching != null) {
            for(int i = 0; i < searching.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = searching.instructions.get(i);
                if (abstractInsnNode instanceof JumpInsnNode && abstractInsnNode.getOpcode() == Opcodes.IFEQ) {
                    JumpInsnNode jump = (JumpInsnNode)abstractInsnNode;
                    InsnList insert = new InsnList();
                    insert.add(new VarInsnNode(
                            Opcodes.ALOAD,
                            0
                    ));
                    insert.add(new FieldInsnNode(
                            Opcodes.GETFIELD,
                            classNode.name,
                            "dropShadowEnabled",
                            "Z"
                    ));
                    insert.add(new JumpInsnNode(
                            Opcodes.IFEQ,
                            jump.label
                    ));
                    searching.instructions.insert(
                            jump,
                            insert);
                    break;
                }
            }
        }
    }

    @Override
    protected boolean writeClassFile() {
        return true;
    }

    @Override
    public byte[] transform(String untransformedName, String transformedName, final byte[] bytes) {
        byte[] returnBytes;
        try {
            returnBytes = super.transform(bytes, untransformedName, transformedName);
        } catch(Exception e) {
            returnBytes = bytes;
            getLogger().error("Caught an exception while transforming! Transformer: \"{}\", Exception: \"{}\"", this, e);
        }
        return returnBytes;
    }

    @SuppressWarnings("unused")
    private static final class Internal {

        @SubstitutionField(value = Transcendent.ADD)
        public static boolean betterFontsEnabled;

        @SubstitutionField(value = Transcendent.ADD)
        public StringCache stringCache;

        @SubstitutionField(value = Transcendent.ADD)
        public boolean dropShadowEnabled;

        @SubstitutionField(value = Transcendent.ADD)
        public boolean enabled;

        //<editor-fold desc="Internal">
        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_78295_j", unobfuscatedName = "posX")
        protected float posX;

        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_111273_g", unobfuscatedName = "locationFontTexture")
        protected ResourceLocation locationFontTexture;

        @SubstitutionField(value = Transcendent.INVISIBLE, obfuscatedName = "field_78285_g", unobfuscatedName = "colorCode")
        private int[] colorCode;
        //</editor-fold>

        @SubstitutionMethod(value = Transcendent.MERGE)
        public Internal(GameSettings par1, ResourceLocation par2, TextureManager par3, boolean par4) {
            Substitution.startAdditionalInstructions(Insertion.BEGINNING, 1);

            Internal.betterFontsEnabled = (dropShadowEnabled = enabled = true);//Manually set variables in constructor
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
        private int renderString(String par1, float par2, float par3, int par4, boolean par5) {
            Substitution.startRemoval();
            renderStringAtPos(par1, par5);
            Substitution.endRemoval();

            Substitution.startAdditionalInstructions(Insertion.ENDING);
            if(Internal.betterFontsEnabled && stringCache != null) {
                posX += stringCache.renderString(par1, par2, par3, par4, par5);
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
