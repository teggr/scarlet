package dev.rebelcraft.scarlet.git;

import java.nio.file.Path;
import java.util.List;

/**
 * Facade for managing Git repository lifecycle.
 * Provides operations to clone, branch, commit, and push repositories.
 */
public interface GitRepositoryManager {
    
    /**
     * Clones a Git repository from the given URL into a directory within the shared parent.
     * 
     * @param repositoryUrl the URL of the Git repository to clone
     * @param repositoryName the name to use for the local directory
     * @return the path to the cloned repository
     * @throws GitException if the clone operation fails
     */
    Path cloneRepository(String repositoryUrl, String repositoryName);
    
    /**
     * Opens an existing repository at the given path.
     * 
     * @param repositoryPath the path to the repository
     * @throws GitException if the repository cannot be opened
     */
    void openRepository(Path repositoryPath);
    
    /**
     * Lists all branches in the repository.
     * 
     * @param repositoryPath the path to the repository
     * @return list of branch names
     * @throws GitException if the operation fails
     */
    List<String> listBranches(Path repositoryPath);
    
    /**
     * Switches to the specified branch in the repository.
     * 
     * @param repositoryPath the path to the repository
     * @param branchName the name of the branch to switch to
     * @throws GitException if the checkout operation fails
     */
    void checkoutBranch(Path repositoryPath, String branchName);
    
    /**
     * Creates a new branch in the repository.
     * 
     * @param repositoryPath the path to the repository
     * @param branchName the name of the new branch
     * @throws GitException if the branch creation fails
     */
    void createBranch(Path repositoryPath, String branchName);
    
    /**
     * Gets the current status of the repository.
     * 
     * @param repositoryPath the path to the repository
     * @return the status of the repository
     * @throws GitException if the status operation fails
     */
    GitStatus getStatus(Path repositoryPath);
    
    /**
     * Adds files to the staging area.
     * 
     * @param repositoryPath the path to the repository
     * @param filePattern the file pattern to add (e.g., "." for all files)
     * @throws GitException if the add operation fails
     */
    void addFiles(Path repositoryPath, String filePattern);
    
    /**
     * Commits staged changes with a commit message.
     * 
     * @param repositoryPath the path to the repository
     * @param message the commit message
     * @return the commit SHA
     * @throws GitException if the commit operation fails
     */
    String commit(Path repositoryPath, String message);
    
    /**
     * Pushes commits to the remote repository.
     * 
     * @param repositoryPath the path to the repository
     * @throws GitException if the push operation fails
     */
    void push(Path repositoryPath);
    
    /**
     * Pushes commits to the remote repository with credentials.
     * 
     * @param repositoryPath the path to the repository
     * @param username the username for authentication
     * @param password the password or token for authentication
     * @throws GitException if the push operation fails
     */
    void push(Path repositoryPath, String username, String password);
}
