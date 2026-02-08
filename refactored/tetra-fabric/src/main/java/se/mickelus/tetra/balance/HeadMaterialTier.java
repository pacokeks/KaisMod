package se.mickelus.tetra.balance;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HeadMaterialTier {
	WOOD("wood"),
	STONE("stone"),
	COPPER("copper"),
	IRON("iron"),
	GOLD("gold"),
	DIAMOND("diamond"),
	NETHERITE("netherite");

	private static final Map<String, HeadMaterialTier> BY_ID = Arrays.stream(values())
		.collect(Collectors.toUnmodifiableMap(HeadMaterialTier::id, Function.identity()));

	private final String id;

	HeadMaterialTier(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	public Optional<HeadMaterialTier> next() {
		int nextIndex = ordinal() + 1;
		if (nextIndex >= values().length) {
			return Optional.empty();
		}
		return Optional.of(values()[nextIndex]);
	}

	public boolean canUpgradeTo(HeadMaterialTier nextTier) {
		return next().map(nextTier::equals).orElse(false);
	}

	public static Optional<HeadMaterialTier> fromId(String id) {
		if (id == null || id.isBlank()) {
			return Optional.empty();
		}
		return Optional.ofNullable(BY_ID.get(id.toLowerCase(Locale.ROOT)));
	}
}
