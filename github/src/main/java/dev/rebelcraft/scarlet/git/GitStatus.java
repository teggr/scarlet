package dev.rebelcraft.scarlet.git;

import java.util.Set;

/**
 * Represents the status of a Git repository.
 */
public record GitStatus(
        String currentBranch,
        Set<String> added,
        Set<String> changed,
        Set<String> removed,
        Set<String> untracked,
        Set<String> conflicting,
        boolean clean
) {
}
