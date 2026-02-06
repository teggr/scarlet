package dev.rebelcraft.scarlet.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JGit-based implementation of GitRepositoryManager.
 */
public class JGitRepositoryManager implements GitRepositoryManager {
    
    private static final Logger logger = LoggerFactory.getLogger(JGitRepositoryManager.class);
    
    private final Path sharedParentPath;
    
    /**
     * Creates a new JGitRepositoryManager with the specified shared parent directory.
     * 
     * @param sharedParentPath the directory where all repositories will be cloned
     */
    public JGitRepositoryManager(Path sharedParentPath) {
        this.sharedParentPath = sharedParentPath;
        try {
            Files.createDirectories(sharedParentPath);
        } catch (IOException e) {
            throw new GitException("Failed to create shared parent directory: " + sharedParentPath, e);
        }
    }
    
    @Override
    public Path cloneRepository(String repositoryUrl, String repositoryName) {
        Path repositoryPath = sharedParentPath.resolve(repositoryName);
        
        if (Files.exists(repositoryPath)) {
            throw new GitException("Repository directory already exists: " + repositoryPath);
        }
        
        try {
            logger.info("Cloning repository {} into {}", repositoryUrl, repositoryPath);
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(repositoryPath.toFile())
                    .call()
                    .close();
            logger.info("Successfully cloned repository to {}", repositoryPath);
            return repositoryPath;
        } catch (GitAPIException e) {
            throw new GitException("Failed to clone repository from " + repositoryUrl, e);
        }
    }
    
    @Override
    public void openRepository(Path repositoryPath) {
        try (Git git = openGit(repositoryPath)) {
            // Just verify the repository can be opened
            logger.info("Successfully opened repository at {}", repositoryPath);
        }
    }
    
    @Override
    public List<String> listBranches(Path repositoryPath) {
        try (Git git = openGit(repositoryPath)) {
            return git.branchList()
                    .call()
                    .stream()
                    .map(Ref::getName)
                    .map(name -> name.replace("refs/heads/", ""))
                    .collect(Collectors.toList());
        } catch (GitAPIException e) {
            throw new GitException("Failed to list branches", e);
        }
    }
    
    @Override
    public void checkoutBranch(Path repositoryPath, String branchName) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Checking out branch {} in {}", branchName, repositoryPath);
            git.checkout()
                    .setName(branchName)
                    .call();
            logger.info("Successfully checked out branch {}", branchName);
        } catch (GitAPIException e) {
            throw new GitException("Failed to checkout branch: " + branchName, e);
        }
    }
    
    @Override
    public void createBranch(Path repositoryPath, String branchName) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Creating branch {} in {}", branchName, repositoryPath);
            git.branchCreate()
                    .setName(branchName)
                    .call();
            logger.info("Successfully created branch {}", branchName);
        } catch (GitAPIException e) {
            throw new GitException("Failed to create branch: " + branchName, e);
        }
    }
    
    @Override
    public GitStatus getStatus(Path repositoryPath) {
        try (Git git = openGit(repositoryPath)) {
            org.eclipse.jgit.api.Status status = git.status().call();
            String currentBranch = git.getRepository().getBranch();
            
            return new GitStatus(
                    currentBranch,
                    status.getAdded(),
                    status.getChanged(),
                    status.getRemoved(),
                    status.getUntracked(),
                    status.getConflicting(),
                    status.isClean()
            );
        } catch (GitAPIException | IOException e) {
            throw new GitException("Failed to get repository status", e);
        }
    }
    
    @Override
    public void addFiles(Path repositoryPath, String filePattern) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Adding files with pattern {} in {}", filePattern, repositoryPath);
            git.add()
                    .addFilepattern(filePattern)
                    .call();
            logger.info("Successfully added files with pattern {}", filePattern);
        } catch (GitAPIException e) {
            throw new GitException("Failed to add files with pattern: " + filePattern, e);
        }
    }
    
    @Override
    public String commit(Path repositoryPath, String message) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Committing changes in {} with message: {}", repositoryPath, message);
            var revCommit = git.commit()
                    .setMessage(message)
                    .call();
            String commitSha = revCommit.getName();
            logger.info("Successfully committed with SHA: {}", commitSha);
            return commitSha;
        } catch (GitAPIException e) {
            throw new GitException("Failed to commit changes", e);
        }
    }
    
    @Override
    public void push(Path repositoryPath) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Pushing changes in {}", repositoryPath);
            git.push().call();
            logger.info("Successfully pushed changes");
        } catch (GitAPIException e) {
            throw new GitException("Failed to push changes", e);
        }
    }
    
    @Override
    public void push(Path repositoryPath, String username, String password) {
        try (Git git = openGit(repositoryPath)) {
            logger.info("Pushing changes in {} with credentials", repositoryPath);
            git.push()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();
            logger.info("Successfully pushed changes with credentials");
        } catch (GitAPIException e) {
            throw new GitException("Failed to push changes with credentials", e);
        }
    }
    
    /**
     * Opens a Git repository at the specified path.
     */
    private Git openGit(Path repositoryPath) {
        try {
            Path gitDir = repositoryPath.resolve(".git");
            if (!Files.exists(gitDir)) {
                throw new GitException("Git repository not found at: " + repositoryPath);
            }
            
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .setGitDir(gitDir.toFile())
                    .readEnvironment()
                    .findGitDir()
                    .build();
            return new Git(repository);
        } catch (IOException e) {
            throw new GitException("Failed to open repository at: " + repositoryPath, e);
        }
    }
}
