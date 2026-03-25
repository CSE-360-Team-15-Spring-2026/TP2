package guiRole1;

import entityClasses.Post;
import entityClasses.Reply;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: ModelRole1Home Class </p>
 *
 * <p> Description: Model for Student Discussion System - manages all posts via database </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 2.00 2025-02-07 Updated with discussion system functionality
 */
public class ModelRole1Home {

    private static String currentUser = "";

    /**
     * Initializes the model with current user
     */
    public static void initialize(String username) {
        currentUser = username;
    }

    /**
     * Gets the current logged-in user
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets all posts FROM DATABASE
     */
    public static List<Post> getAllPosts() {
        database.Database db = applicationMain.FoundationsMain.database;
        List<Post> allPosts = db.getAllPosts();
        
        // Filter out replies (only show top-level posts)
        List<Post> topLevelPosts = new ArrayList<>();
        for (Post post : allPosts) {
            
            if (post.getParentPostID() == -1) {  // -1 means top-level post
                topLevelPosts.add(post);
            }
        }
        return topLevelPosts;
    }

    /**
     * Gets a specific post by ID FROM DATABASE
     */
    public static Post getPostById(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getPostByID(postId);
    }

    /**
     * Gets posts by current user FROM DATABASE
     */
    public static List<Post> getMyPosts() {
        List<Post> allPosts = getAllPosts();
        List<Post> myPosts = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getUsername().equals(currentUser)) {
                myPosts.add(post);
            }
        }
        return myPosts;
    }

    /**
     * Deletes a post IN DATABASE
     */
    public static boolean deletePost(int postId) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        if (!post.getUsername().equals(currentUser)) {
            return false;
        }
        
        post.changeDelete();
        return true;
    }

    /**
     * Gets replies for a specific post FROM DATABASE
     */
    public static List<Reply> getRepliesForPost(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getRepliesForPost(postId);
    }
    
    /**
     * Get reply count for a post FROM DATABASE
     */
    public static int getReplyCount(int postId) {
        List<Reply> replies = getRepliesForPost(postId);
        return replies.size();
    }
    
    /**
     * Helper: Get formatted timestamp
     */
    public static String getFormattedTimestamp(Post post) {
        if (post.getTimestamp() == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return post.getTimestamp().format(formatter);
    }

    /**
     * Check if current user has read a post
     */
    public static boolean isRead(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.isPostRead(currentUser, postId);
    }

    /**
     * Search posts by keyword, optionally filtered by thread
     */
    public static List<Post> searchPosts(String keyword, String thread) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.searchPosts(keyword, thread);
    }
}