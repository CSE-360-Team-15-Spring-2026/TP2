package guiRole1;

import entityClasses.Post;
import entityClasses.Reply;
import java.util.ArrayList;
import java.util.List;
import database.Database;

/**
 * <p> Title: ModelRole1Home Class </p>
 *
 * <p> Description: Model for Student Discussion System, manages all posts via the database </p>
 */
public class ModelRole1Home {
	
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ModelRole1Home() {}

	/** An empty string variable for the current username */
    private static String currentUser = "";

    /**
     * <p> Initializes the model with current user </p>
     * @param username: the username of the current user
     */
    public static void initialize(String username) {
        currentUser = username;
    }

    /**
     * <p> Gets the current user </p>
     * @return username of the current user
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * <p> Gets all the posts from the database </p>
     * @return the ArrayList of posts
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
     * <p> Gets a specific post by PostID from the database </p> 
     * @param postId: the unique id of the post to be found
     * @return the post that the database was searched through for
     */
    public static Post getPostById(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getPostByID(postId);
    }

    /**
     * <p> Gets posts by current user from the database </p>
     * @return a list of the posts by the current user
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
     * <p> Deletes a post in the database </p>
     * @param postId: unique id of the post to be deleted
     * @return true if the post gets deleted successfully, false if it fails to delete or is cancelled
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
        return applicationMain.FoundationsMain.database.deletePost(post);
    }

    /**
     * <p> Gets replies for a specific post from database </p>
     * @param postId: unique id of the post and its replies
     * @return list of the post's replies
     */
    public static List<Reply> getRepliesForPost(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getRepliesForPost(postId);
    }
    
    /**
     * <p> Gets the reply count for a post from the database </p>
     * @param postId: unique id of the post and its replies
     * @return the number of replies under the post
     */
    public static int getReplyCount(int postId) {
        List<Reply> replies = getRepliesForPost(postId);
        return replies.size();
    }
    
    /**
     * <p> Helper function to get a formatted timestamp </p>
     * @param post: the object of the post
     * @return the formatted timestamp or if no timestamp exists, an empty string 
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
     * <p> Checks if the current user has read a post </p>
     * @param postId: unique id of the post being checked
     * @return true if the post has been read, false if the post is unread
     */
    public static boolean isRead(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.isPostRead(currentUser, postId);
    }

    /**
     * <p> Searches posts by keyword, optionally filtered by thread </p>
     * @param keyword: the keyword to be found in any post
     * @param thread: optional parameter, limits the result to the specific thread
     * @return list of posts that contain the keyword and are a part of the specified thread
     */
    public static List<Post> searchPosts(String keyword, String thread) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.searchPosts(keyword, thread);
    }
}