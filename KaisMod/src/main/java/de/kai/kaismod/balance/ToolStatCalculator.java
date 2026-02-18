package de.kai.kaismod.balance;

import de.kai.kaismod.data.ToolState;
import de.kai.kaismod.data.ToolType;

public final class ToolStatCalculator {
	private ToolStatCalculator() {
	}

	public static double attackDamage(ToolType type, ToolState state) {
		double base = switch (type) {
			case SWORD -> 8.0;
			case AXE -> 10.0;
			case PICKAXE -> 4.0;
			case SPEAR -> 9.0;
			case MACE -> 12.0;
		};
		return base * headDamageFactor(state.headMaterial());
	}

	public static double attackSpeed(ToolType type, ToolState state) {
		double base = switch (type) {
			case SWORD -> 1.6;
			case AXE -> 1.0;
			case PICKAXE -> 1.2;
			case SPEAR -> 1.1;
			case MACE -> 0.8;
		};
		return base * headSpeedFactor(state.headMaterial());
	}

	public static float miningSpeed(ToolType type, ToolState state) {
		float base = switch (type) {
			case PICKAXE -> 10.0f;
			case AXE -> 4.0f;
			case SWORD -> 1.5f;
			case SPEAR -> 1.0f;
			case MACE -> 1.0f;
		};
		return (float) (base * headSpeedFactor(state.headMaterial()));
	}

	public static int durabilityLossOnHit(ToolType type) {
		return switch (type) {
			case MACE, AXE -> 2;
			default -> 1;
		};
	}

	public static int durabilityLossOnMine(ToolType type) {
		return switch (type) {
			case PICKAXE, AXE -> 1;
			case MACE -> 2;
			default -> 1;
		};
	}

	private static double headDamageFactor(String headMaterial) {
		return switch (headMaterial) {
			case "wood" -> 0.85;
			case "stone" -> 1.00;
			case "copper" -> 1.05;
			case "iron" -> 1.15;
			case "gold" -> 1.00;
			case "diamond" -> 1.35;
			case "netherite" -> 1.50;
			default -> 1.00;
		};
	}

	private static double headSpeedFactor(String headMaterial) {
		return switch (headMaterial) {
			case "wood" -> 0.90;
			case "stone" -> 1.00;
			case "copper" -> 1.10;
			case "iron" -> 1.15;
			case "gold" -> 1.35;
			case "diamond" -> 1.25;
			case "netherite" -> 1.20;
			default -> 1.00;
		};
	}
}
