package me.isuzutsuki.betterfonts;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public final class BTFDummyContainer extends DummyModContainer {

    public BTFDummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "BetterFonts";
        meta.name = "BetterFonts";
        meta.version = "@MOD_VERSION@";
        meta.credits = "thvortex for original codes";
        meta.authorList.add("iSuzutsuki");
        meta.url = "https://secretdataz.github.io/BetterFonts";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
        return true;
    }

}
