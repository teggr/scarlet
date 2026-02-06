package dev.rebelcraft.scarlet.git.examples;

import dev.rebelcraft.scarlet.git.GitException;
import dev.rebelcraft.scarlet.git.GitStatus;
import dev.rebelcraft.scarlet.git.JGitRepositoryManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Example demonstrating how to use the GitRepositoryManager facade.
 */
public class GitRepositoryExample {
    
    public static void main(String[] args) {
        // Setup the repository manager with a shared parent directory
        Path sharedParent = Paths.get("./example-repositories");
        JGitRepositoryManager manager = new JGitRepositoryManager(sharedParent);
        
        try {
            // Example 1: Clone a repository
            System.out.println("=== Cloning Repository ===");
            String repositoryUrl = "https://github.com/example/demo-repo.git";
            String repoName = "demo-repo";
            
            // Note: This will fail if the repository doesn't exist or network is unavailable
            // Path repoPath = manager.cloneRepository(repositoryUrl, repoName);
            // System.out.println("Cloned to: " + repoPath);
            
            // For this example, we'll create a local test repository instead
            Path testRepoPath = createLocalTestRepository(sharedParent);
            System.out.println("Created test repository at: " + testRepoPath);
            
            // Example 2: List branches
            System.out.println("\n=== Listing Branches ===");
            List<String> branches = manager.listBranches(testRepoPath);
            branches.forEach(branch -> System.out.println("  - " + branch));
            
            // Example 3: Create and switch to a new branch
            System.out.println("\n=== Creating New Branch ===");
            String newBranch = "feature/example";
            manager.createBranch(testRepoPath, newBranch);
            manager.checkoutBranch(testRepoPath, newBranch);
            System.out.println("Created and checked out branch: " + newBranch);
            
            // Example 4: Check repository status
            System.out.println("\n=== Repository Status ===");
            GitStatus status = manager.getStatus(testRepoPath);
            System.out.println("Current branch: " + status.currentBranch());
            System.out.println("Is clean: " + status.clean());
            
            // Example 5: Make changes and commit
            System.out.println("\n=== Making Changes ===");
            Path newFile = testRepoPath.resolve("example.txt");
            Files.writeString(newFile, "This is an example file created by the demo.");
            System.out.println("Created file: example.txt");
            
            // Check status again
            status = manager.getStatus(testRepoPath);
            System.out.println("Untracked files: " + status.untracked());
            
            // Stage the changes
            manager.addFiles(testRepoPath, ".");
            System.out.println("Staged all changes");
            
            // Commit
            String commitSha = manager.commit(testRepoPath, "Add example file");
            System.out.println("Committed changes with SHA: " + commitSha);
            
            // Check status after commit
            status = manager.getStatus(testRepoPath);
            System.out.println("Repository is now clean: " + status.clean());
            
            // Example 6: Push (commented out as it requires remote setup)
            // System.out.println("\n=== Pushing Changes ===");
            // manager.push(testRepoPath, "username", "token");
            // System.out.println("Pushed changes to remote");
            
            System.out.println("\n=== Example Complete ===");
            
        } catch (GitException e) {
            System.err.println("Git operation failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a local test repository for demonstration purposes.
     */
    private static Path createLocalTestRepository(Path parent) throws Exception {
        Path testRepo = parent.resolve("test-repo-" + System.currentTimeMillis());
        Files.createDirectories(testRepo);
        
        // Initialize a git repository using JGit
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.init()
                .setDirectory(testRepo.toFile())
                .call()) {
            
            // Create initial file
            Path readmeFile = testRepo.resolve("README.md");
            Files.writeString(readmeFile, "# Test Repository\n\nThis is a test repository.");
            
            // Add and commit
            git.add().addFilepattern("README.md").call();
            git.commit().setMessage("Initial commit").call();
        }
        
        return testRepo;
    }
}
