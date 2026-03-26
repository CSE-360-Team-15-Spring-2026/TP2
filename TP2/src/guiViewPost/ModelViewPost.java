package guiViewPost;

import entityClasses.Post;
import entityClasses.Reply;
import database.Database;
import java.util.List;

/**
 * <p> Title: ModelViewPost Class </p>
 *
 * <p> Description: Model for View Post functionality - handles post and reply operations </p>
 */
public class ModelViewPost {

	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ModelViewPost() {}
	
	/** Establishes connection to the database */
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    /** Initializes the current username with an empty String */
    private static String currentUser = "";

    /**
     * <p> Initializes with current user </p>
     * @param username: the current user's username
     */
    public static void initialize(String username) {
        currentUser = username;
    }

    /**
     * <p> Gets the current username </p>
     * @return the username of the current user
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * <p> Gets the replies for a post from the database </p>
     * @param postId: unique id of the post that the replies are connected to
     * @return list of replies to the post
     */
    public static List<Reply> getRepliesForPost(int postId) {
        return theDatabase.getRepliesForPost(postId);
    }

    /**
     * <p> Creates a reply to a post </p>
     * @param parentPostID: post id of the post that is being replied to
     * @param replyBody: text body of the reply
     * @return false if theres no current user or reply body is blank or if the parent post doesn't exist, true if successful
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
     * <p> Deletes a reply from the post </p>
     * @param reply: the reply being deleted
     * @return false if reply fails to be deleted, true is reply is successfully deleted
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