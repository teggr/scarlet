package dev.rebelcraft.scarlet.git;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JGitRepositoryManager.
 */
class JGitRepositoryManagerTest {
    
    @TempDir
    Path tempDir;
    
    private JGitRepositoryManager manager;
    private Path sharedParent;
    
    @BeforeEach
    void setUp() {
        sharedParent = tempDir.resolve("repositories");
        manager = new JGitRepositoryManager(sharedParent);
    }
    
    @Test
    void shouldCreateSharedParentDirectory() {
        assertTrue(Files.exists(sharedParent), "Shared parent directory should be created");
        assertTrue(Files.isDirectory(sharedParent), "Shared parent should be a directory");
    }
    
    @Test
    void shouldInitializeRepositoryInSharedParent() throws Exception {
        // Create a simple test repository manually
        Path testRepo = tempDir.resolve("test-repo");
        Files.createDirectories(testRepo);
        
        // Initialize a git repo using JGit
        org.eclipse.jgit.api.Git.init()
                .setDirectory(testRepo.toFile())
                .call()
                .close();
        
        // Create a test file and commit it
        Path testFile = testRepo.resolve("README.md");
        Files.writeString(testFile, "# Test Repository");
        
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(testRepo.toFile())) {
            git.add().addFilepattern("README.md").call();
            git.commit().setMessage("Initial commit").call();
        }
        
        // Now clone this repository using our manager
        Path clonedRepo = manager.cloneRepository(testRepo.toUri().toString(), "cloned-test-repo");
        
        assertNotNull(clonedRepo);
        assertTrue(Files.exists(clonedRepo));
        assertTrue(Files.exists(clonedRepo.resolve(".git")));
        assertTrue(Files.exists(clonedRepo.resolve("README.md")));
        assertEquals(sharedParent.resolve("cloned-test-repo"), clonedRepo);
    }
    
    @Test
    void shouldThrowExceptionWhenCloningToExistingDirectory() throws Exception {
        // Create a directory with the target name
        Path existingDir = sharedParent.resolve("existing-repo");
        Files.createDirectories(existingDir);
        
        // Attempt to clone should fail
        assertThrows(GitException.class, () -> 
            manager.cloneRepository("https://github.com/example/repo.git", "existing-repo")
        );
    }
    
    @Test
    void shouldListBranches() throws Exception {
        // Create a test repository with multiple branches
        Path testRepo = createTestRepository();
        
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(testRepo.toFile())) {
            git.branchCreate().setName("feature-branch").call();
            git.branchCreate().setName("develop").call();
        }
        
        List<String> branches = manager.listBranches(testRepo);
        
        assertNotNull(branches);
        assertTrue(branches.contains("master") || branches.contains("main"));
        assertTrue(branches.contains("feature-branch"));
        assertTrue(branches.contains("develop"));
        assertEquals(3, branches.size());
    }
    
    @Test
    void shouldCheckoutBranch() throws Exception {
        Path testRepo = createTestRepository();
        
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(testRepo.toFile())) {
            git.branchCreate().setName("new-branch").call();
        }
        
        manager.checkoutBranch(testRepo, "new-branch");
        
        GitStatus status = manager.getStatus(testRepo);
        assertEquals("new-branch", status.currentBranch());
    }
    
    @Test
    void shouldCreateBranch() throws Exception {
        Path testRepo = createTestRepository();
        
        manager.createBranch(testRepo, "test-branch");
        
        List<String> branches = manager.listBranches(testRepo);
        assertTrue(branches.contains("test-branch"));
    }
    
    @Test
    void shouldGetStatus() throws Exception {
        Path testRepo = createTestRepository();
        
        GitStatus status = manager.getStatus(testRepo);
        
        assertNotNull(status);
        assertNotNull(status.currentBranch());
        assertTrue(status.clean());
        assertTrue(status.added().isEmpty());
        assertTrue(status.changed().isEmpty());
        assertTrue(status.untracked().isEmpty());
    }
    
    @Test
    void shouldAddFilesAndCommit() throws Exception {
        Path testRepo = createTestRepository();
        
        // Create a new file
        Path newFile = testRepo.resolve("newfile.txt");
        Files.writeString(newFile, "Hello, World!");
        
        // Check that file is untracked
        GitStatus statusBefore = manager.getStatus(testRepo);
        assertFalse(statusBefore.clean());
        assertTrue(statusBefore.untracked().contains("newfile.txt"));
        
        // Add and commit the file
        manager.addFiles(testRepo, ".");
        String commitSha = manager.commit(testRepo, "Add new file");
        
        assertNotNull(commitSha);
        assertFalse(commitSha.isEmpty());
        
        // Check that repository is clean after commit
        GitStatus statusAfter = manager.getStatus(testRepo);
        assertTrue(statusAfter.clean());
    }
    
    @Test
    void shouldOpenRepository() throws Exception {
        Path testRepo = createTestRepository();
        
        // Should not throw exception
        assertDoesNotThrow(() -> manager.openRepository(testRepo));
    }
    
    @Test
    void shouldThrowExceptionWhenOpeningNonExistentRepository() {
        Path nonExistentRepo = tempDir.resolve("non-existent");
        
        assertThrows(GitException.class, () -> manager.openRepository(nonExistentRepo));
    }
    
    /**
     * Creates a test repository with an initial commit.
     */
    private Path createTestRepository() throws Exception {
        Path testRepo = tempDir.resolve("test-repo-" + System.currentTimeMillis());
        Files.createDirectories(testRepo);
        
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.init()
                .setDirectory(testRepo.toFile())
                .call()) {
            
            // Create initial file
            Path readmeFile = testRepo.resolve("README.md");
            Files.writeString(readmeFile, "# Test Repository");
            
            // Add and commit
            git.add().addFilepattern("README.md").call();
            git.commit().setMessage("Initial commit").call();
        }
        
        return testRepo;
    }
}
