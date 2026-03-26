package guiEditReply;

import entityClasses.Reply;
import database.Database;

/**
 * <p> Title: ModelEditReply Class </p>
 *
 * <p> Description: Model for editing replies </p>
 */
public class ModelEditReply {
	
	/**
	 * <p> Constructor - Not Utilized </p>
	 */
	public ModelEditReply() {}

	/** Establishes connection to the database */
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**
     * <p> Updates an existing reply </p>
     * @param reply: the reply to be updated
     * @param body: the text to be changed in the reply
     * @return false if the body or reply is blank, true if the reply is successfully updated
     */
    protected static boolean updateReply(Reply reply, String body) {
        if (reply == null) {
            return false;
        }
        if (body == null || body.isBlank()) {
            return false;
        }
        
        try {
            // Update the reply object
            reply.setBody(body.trim());
            
            // Use the existing updatePost method from Database
            // (works for replies too since Reply extends Post)
            return theDatabase.updatePost(reply);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}