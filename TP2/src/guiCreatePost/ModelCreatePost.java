package guiCreatePost;

/**
 * Model class for the create post page.
 */
public class ModelCreatePost {

/*******
 * <p> Title: ModelCreatePost Class. </p>
 * 
 * <p> Description: Handles data-related operations for the Create Post page.
 * Currently acts as a placeholder since all data interactions are managed
 * directly through the database layer.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2026-03-25 Updated description
 *  
 */
	
	/**
	 * Default constructor. This model currently does not manage its own data
	 * and relies on the database for all operations.
	 */
	public ModelCreatePost() {
	}

	/**********
 	* <p> Method: createPost(String username, String title, String body, String threadName) </p>
 	* 
 	* <p> Description: Validates input and creates a new post using the database. </p>
 	* 
 	* @param username the user creating the post
 	* @param title the post title
 	* @param body the post content
 	* @param threadName the selected thread (defaults to "General" if empty)
 	* 
 	* @return true if the post is created successfully, otherwise false
 	* 
 	*/
	protected static boolean createPost(String username, String title, String body, String threadName) {
		try {
			if (username == null || username.isBlank()) return false;
			if (title == null || title.isBlank()) return false;
			if (body == null || body.isBlank()) return false;
			if (threadName == null || threadName.isBlank()) threadName = "General";

			applicationMain.FoundationsMain.database.createPost(
					username,
					title.trim(),
					body.trim(),
					"",
					threadName
			);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
