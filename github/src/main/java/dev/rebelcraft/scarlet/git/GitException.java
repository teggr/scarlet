package dev.rebelcraft.scarlet.git;

/**
 * Exception thrown when Git operations fail.
 */
public class GitException extends RuntimeException {
    
    public GitException(String message) {
        super(message);
    }
    
    public GitException(String message, Throwable cause) {
        super(message, cause);
    }
}
