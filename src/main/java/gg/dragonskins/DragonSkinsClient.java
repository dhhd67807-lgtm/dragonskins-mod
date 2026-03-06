package gg.dragonskins;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonSkinsClient implements ClientModInitializer {
    public static final String MOD_ID = "dragonskins";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("DragonSkins initialized!");
        
        // Initialize skin loader
        SkinLoader.init();
        
        // Initialize cape loader
        CapeLoader.init();
        
        // Initialize nametag renderer
        NametagRenderer.init();
    }
}
