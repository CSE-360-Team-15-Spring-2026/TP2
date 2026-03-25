package guiViewPost;

import entityClasses.Post;
import entityClasses.Reply;
import database.Database;
import java.util.List;

/**
 * <p> Title: ModelViewPost Class </p>
 *
 * <p> Description: Model for View Post functionality - handles post and reply operations </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @version 2.00 2026-03-25 Complete reply management
 */
public class ModelViewPost {

    private static Database theDatabase = applicationMain.FoundationsMain.database;
    private static String currentUser = "";

    /**
     * Initialize with current user
     */
    public static void initialize(String username) {
        currentUser = username;
    }

    /**
     * Get current username
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * Get replies for a post FROM DATABASE
     */
    public static List<Reply> getRepliesForPost(int postId) {
        return theDatabase.getRepliesForPost(postId);
    }

    /**
     * Create a reply to a post
     */
    public static boolean createReply(int parentPostID, String replyBody) {
        if (currentUser == null || currentUser.isBlank()) {
            return false;
        }
        if (replyBody == null || replyBody.isBlank()) {
            return false;
        }
        
        try {
            // Get the thread name from the parent post
            Post parentPost = theDatabase.getPostByID(parentPostID);
            if (parentPost == null) {
                return false;
            }
            
            String threadName = parentPost.getThreadName();
            if (threadName == null || threadName.isBlank()) {
                threadName = "General";
            }
            
            theDatabase.createReply(currentUser, replyBody.trim(), "", threadName, parentPostID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a reply (soft delete)
     */
    public static boolean deleteReply(Reply reply) {
        if (reply == null) {
            return false;
        }
        if (!reply.getUsername().equals(currentUser)) {
            return false;
        }
        
        try {
            return theDatabase.deleteReply(reply);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}