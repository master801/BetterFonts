package me.isuzutsuki.betterfonts;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Name("BetterFonts")
@MCVersion("1.8")
@TransformerExclusions("me.isuzutsuki.betterfonts.rendering.")
public final class BetterFontsCore implements IFMLLoadingPlugin {

    public static final Logger BETTER_FONTS_LOGGER = LogManager.getLogger("BetterFonts");

	@Override
    public String[] getASMTransformerClass() {
		return new String[] {
                BTFClassTransformer.class.getName()
        };
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getModContainerClass() {
        return BTFDummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
