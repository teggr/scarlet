# GitHub Module

A utility module for managing GitHub repositories locally using JGit.

## Features

The GitHub module provides a facade (`GitRepositoryManager`) for managing the lifecycle of Git repositories:

- **Clone repositories** into a shared parent directory
- **Create and switch branches**
- **Get repository status** (tracked, modified, untracked files)
- **Stage files** for commit
- **Commit changes** with commit messages
- **Push changes** to remote repositories (with or without credentials)

## Usage

### Basic Setup

```java
import dev.rebelcraft.scarlet.git.JGitRepositoryManager;
import dev.rebelcraft.scarlet.git.GitStatus;
import java.nio.file.Path;
import java.nio.file.Paths;

// Create a manager with a shared parent directory for all repositories
Path sharedParent = Paths.get("/path/to/repositories");
JGitRepositoryManager gitManager = new JGitRepositoryManager(sharedParent);
```

### Clone a Repository

```java
// Clone a repository from a URL
String repositoryUrl = "https://github.com/example/repo.git";
String repoName = "my-repo";
Path repoPath = gitManager.cloneRepository(repositoryUrl, repoName);
// Repository will be cloned to: /path/to/repositories/my-repo
```

### Work with Branches

```java
// List all branches
List<String> branches = gitManager.listBranches(repoPath);
branches.forEach(System.out::println);

// Create a new branch
gitManager.createBranch(repoPath, "feature-branch");

// Switch to a branch
gitManager.checkoutBranch(repoPath, "feature-branch");
```

### Check Repository Status

```java
// Get the current status
GitStatus status = gitManager.getStatus(repoPath);

System.out.println("Current branch: " + status.currentBranch());
System.out.println("Is clean: " + status.clean());
System.out.println("Modified files: " + status.changed());
System.out.println("Untracked files: " + status.untracked());
```

### Stage, Commit and Push Changes

```java
// Add all files to staging area
gitManager.addFiles(repoPath, ".");

// Or add specific files/patterns
gitManager.addFiles(repoPath, "src/main/java/**/*.java");

// Commit the staged changes
String commitSha = gitManager.commit(repoPath, "Add new feature");
System.out.println("Committed with SHA: " + commitSha);

// Push to remote (without credentials)
gitManager.push(repoPath);

// Or push with credentials (e.g., GitHub token)
String username = "github-username";
String token = "github-personal-access-token";
gitManager.push(repoPath, username, token);
```

### Complete Example

```java
import dev.rebelcraft.scarlet.git.JGitRepositoryManager;
import dev.rebelcraft.scarlet.git.GitStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitExample {
    public static void main(String[] args) {
        // Setup
        Path sharedParent = Paths.get("./repositories");
        JGitRepositoryManager manager = new JGitRepositoryManager(sharedParent);
        
        // Clone repository
        Path repoPath = manager.cloneRepository(
            "https://github.com/example/repo.git", 
            "example-repo"
        );
        
        // Create and switch to feature branch
        manager.createBranch(repoPath, "feature/new-feature");
        manager.checkoutBranch(repoPath, "feature/new-feature");
        
        // Make changes (example: create a file)
        Path newFile = repoPath.resolve("README.md");
        Files.writeString(newFile, "# My Project\n\nThis is a new file.");
        
        // Check status
        GitStatus status = manager.getStatus(repoPath);
        System.out.println("Untracked files: " + status.untracked());
        
        // Stage, commit and push
        manager.addFiles(repoPath, ".");
        String sha = manager.commit(repoPath, "Add README");
        manager.push(repoPath, "username", "token");
        
        System.out.println("Successfully committed and pushed: " + sha);
    }
}
```

## Error Handling

All operations throw `GitException` (a runtime exception) when they fail. You can catch these exceptions to handle errors gracefully:

```java
try {
    Path repoPath = gitManager.cloneRepository(url, name);
} catch (GitException e) {
    System.err.println("Failed to clone repository: " + e.getMessage());
    e.printStackTrace();
}
```

## Dependencies

The module uses:
- **JGit** (7.1.0.202411261347-r) - Pure Java implementation of Git
- **SLF4J** - Logging facade
- **JUnit Jupiter** - For testing

## Testing

Run tests with Maven:

```bash
cd github
mvn test
```

The test suite includes comprehensive tests for:
- Repository cloning
- Branch management
- Status checking
- File staging and committing
- Error handling
