package de.kai.kaismod.data;

import java.util.Objects;
import java.util.Optional;

public record ToolStateOperationResult(
	ToolState state,
	Optional<ToolStateError> error
) {
	public ToolStateOperationResult {
		Objects.requireNonNull(state, "state");
		Objects.requireNonNull(error, "error");
	}

	public static ToolStateOperationResult success(ToolState state) {
		return new ToolStateOperationResult(state, Optional.empty());
	}

	public static ToolStateOperationResult failure(ToolState state, ToolStateError error) {
		return new ToolStateOperationResult(state, Optional.of(error));
	}

	public boolean isSuccess() {
		return error.isEmpty();
	}
}
