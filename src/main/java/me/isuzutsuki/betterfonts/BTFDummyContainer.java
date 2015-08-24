package me.isuzutsuki.betterfonts;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Collections;

public class BTFDummyContainer extends DummyModContainer {

    public BTFDummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "BetterFonts";
        meta.name = "BetterFonts";
        meta.version = "${version}";
        meta.credits = "thvortex for original codes";
        meta.authorList = Collections.singletonList("iSuzutsuki");
        meta.description = "";
        meta.url = "https://secretdataz.github.io/BetterFonts";
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";

    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
        return true;
    }

}
