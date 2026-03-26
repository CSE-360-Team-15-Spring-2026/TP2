package guiEditPost;

import entityClasses.Post;
import database.Database;

/**
 * <p> Title: ModelEditPost Class </p>
 *
 * <p> Description: model for editing posts </p>
 * <p> The operation in the class pulls post from databse and updates them with the newly edited version </p>
 * 
 * @version 1.00
 */
public class ModelEditPost {
    /** will allow to make calls to database and make the updates*/
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    
    /**
	 * <p> Constructor - Not utilized </p>
	 */
	public  ModelEditPost() {}
    
    /**
     * <p> Performs an update to posts in databse </p>
     * 
     * @param post    the post that will be updated 
     * @param title   the title that will replace the old one
     * @param body    the body that will replace the old one
     * @param threadName    the threadName that will replace the old one
     *
     * @return false if post, title, or false are null or if the post is failed to be saved
     */
    protected static boolean updatePost(Post post, String title, String body, String threadName) {
        if (post == null) {
            return false;
        }
        if (title == null || title.isBlank()) {
            return false;
        }
        if (body == null || body.isBlank()) {
            return false;
        }
        if (threadName == null || threadName.isBlank()) {
            threadName = "General";
        }
        
        try {
            // Update the post object fields
            post.setTitle(title.trim());
            post.setBody(body.trim());
            post.setThreadName(threadName);
            
            // CRITICAL: Save changes back to database
            return theDatabase.updatePost(post);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
