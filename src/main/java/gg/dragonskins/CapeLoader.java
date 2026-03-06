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

public class CapeLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("DragonSkins/CapeLoader");
    private static final String API_URL = "https://dragonclient.dev/api/capes/";
    private static final Map<UUID, Identifier> capeCache = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void init() {
        LOGGER.info("CapeLoader initialized");
    }

    public static Identifier loadCape(GameProfile profile) {
        UUID uuid = profile.getId();
        
        // Check cache first
        if (capeCache.containsKey(uuid)) {
            return capeCache.get(uuid);
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
                        Identifier capeId = Identifier.of("dragonskins", "capes/" + uuid);
                        
                        // Register texture on main thread
                        net.minecraft.client.MinecraftClient.getInstance().execute(() -> {
                            net.minecraft.client.MinecraftClient.getInstance()
                                .getTextureManager()
                                .registerTexture(capeId, new NativeImageBackedTexture(image));
                            capeCache.put(uuid, capeId);
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Failed to load cape for {}: {}", uuid, e.getMessage());
            }
        });

        return null; // No cape until loaded
    }

    public static boolean hasCape(UUID uuid) {
        return capeCache.containsKey(uuid);
    }

    public static void clearCache() {
        capeCache.clear();
    }
}
