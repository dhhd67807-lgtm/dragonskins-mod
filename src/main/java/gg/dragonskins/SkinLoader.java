package gg.dragonskins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkinLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("DragonSkins/SkinLoader");
    private static final String API_URL = "https://dragonclient.dev/api/skins/";
    private static final Map<UUID, Identifier> skinCache = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void init() {
        LOGGER.info("SkinLoader initialized");
    }

    public static Identifier loadSkin(GameProfile profile) {
        UUID uuid = profile.getId();
        
        // Check cache first
        if (skinCache.containsKey(uuid)) {
            return skinCache.get(uuid);
        }

        // Load asynchronously
        executor.submit(() -> {
            try {
                URL url = new URL(API_URL + uuid.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    try (InputStream stream = connection.getInputStream()) {
                        NativeImage image = NativeImage.read(stream);
                        Identifier skinId = Identifier.of("dragonskins", "skins/" + uuid);
                        
                        // Register texture on main thread
                        net.minecraft.client.MinecraftClient.getInstance().execute(() -> {
                            net.minecraft.client.MinecraftClient.getInstance()
                                .getTextureManager()
                                .registerTexture(skinId, new NativeImageBackedTexture(image));
                            skinCache.put(uuid, skinId);
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Failed to load skin for {}: {}", uuid, e.getMessage());
            }
        });

        return null; // Will use default skin until loaded
    }

    public static void clearCache() {
        skinCache.clear();
    }
}
