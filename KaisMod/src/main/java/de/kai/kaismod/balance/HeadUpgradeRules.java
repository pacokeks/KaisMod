package de.kai.kaismod.balance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HeadUpgradeRules {
	private static final String RESOURCE_PATH = "/data/kaismod/balance/head_upgrade_costs.json";
	private static final Map<String, Integer> DEFAULT_COSTS = Map.of(
		"wood", 1,
		"stone", 1,
		"copper", 1,
		"iron", 1,
		"gold", 1,
		"diamond", 2,
		"netherite", 3
	);
	private static final Map<String, Integer> costs = new ConcurrentHashMap<>(DEFAULT_COSTS);

	private static volatile boolean loaded;

	private HeadUpgradeRules() {
	}

	public static int costForTargetMaterial(String materialId) {
		ensureLoaded();
		if (materialId == null || materialId.isBlank()) {
			return 1;
		}
		return Math.max(1, costs.getOrDefault(materialId.toLowerCase(Locale.ROOT), 1));
	}

	private static void ensureLoaded() {
		if (loaded) {
			return;
		}
		synchronized (HeadUpgradeRules.class) {
			if (loaded) {
				return;
			}
			loadFromResource();
			loaded = true;
		}
	}

	private static void loadFromResource() {
		try (InputStream stream = HeadUpgradeRules.class.getResourceAsStream(RESOURCE_PATH)) {
			if (stream == null) {
				return;
			}
			try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
				JsonElement root = JsonParser.parseReader(reader);
				if (!root.isJsonObject()) {
					return;
				}
				JsonObject object = root.getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
					if (!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isNumber()) {
						continue;
					}
					int value = Math.max(1, entry.getValue().getAsInt());
					costs.put(entry.getKey().toLowerCase(Locale.ROOT), value);
				}
			}
		} catch (Exception ignored) {
			// Fallback bleibt auf DEFAULT_COSTS.
		}
	}
}
