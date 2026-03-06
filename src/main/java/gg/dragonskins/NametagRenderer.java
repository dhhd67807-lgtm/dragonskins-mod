package gg.dragonskins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NametagRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger("DragonSkins/NametagRenderer");
    private static final Map<UUID, NametagData> nametagCache = new ConcurrentHashMap<>();

    public static void init() {
        LOGGER.info("NametagRenderer initialized");
    }

    public static void renderCustomNametag(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        UUID playerUuid,
        Text displayName,
        TextRenderer textRenderer,
        int light
    ) {
        NametagData data = nametagCache.get(playerUuid);
        
        if (data != null && data.hasCustomTag()) {
            // Render custom nametag with icon/badge
            renderWithIcon(matrices, vertexConsumers, data, displayName, textRenderer, light);
        }
    }

    private static void renderWithIcon(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        NametagData data,
        Text displayName,
        TextRenderer textRenderer,
        int light
    ) {
        // TODO: Implement custom nametag rendering with icons
        // This will render badges, ranks, or custom icons next to player names
    }

    public static void setNametagData(UUID uuid, NametagData data) {
        nametagCache.put(uuid, data);
    }

    public static void clearCache() {
        nametagCache.clear();
    }

    public static class NametagData {
        private final String badge;
        private final int color;

        public NametagData(String badge, int color) {
            this.badge = badge;
            this.color = color;
        }

        public boolean hasCustomTag() {
            return badge != null && !badge.isEmpty();
        }

        public String getBadge() {
            return badge;
        }

        public int getColor() {
            return color;
        }
    }
}
