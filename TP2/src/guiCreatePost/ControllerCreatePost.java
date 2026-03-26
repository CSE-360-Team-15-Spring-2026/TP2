package guiCreatePost;


/*******
 * <p> Title: ControllerCreatePost Class. </p>
 * 
 * <p> Description: Controller for the Create Post page. Handles user interactions from the GUI
 * and coordinates actions between the View and Model components.</p>
 * 
 * <p> Note: Methods are declared as protected since they are intended to be accessed only by
 * the View and Model classes within this MVC structure.</p>
 * 
 */

public class ControllerCreatePost {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not meant to be instantiated. Instead, it provides a set of
	protected static methods that are called by the View (a singleton) and, when needed,
	the Model to handle user interactions.
	 */

	/**
	 * Default constructor is not used as this class only contains static methods.
	 */
	public ControllerCreatePost() {
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Redirects the user to the User Update page to modify their account details. </p>
	 * 
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewCreatePost.theStage, ViewCreatePost.theUser);
	}	

	/**********
	 * <p> Method: performCreatePost() </p>
	 * 
	 * <p> Validates and creates a new post. </p>
	 * 
	 */
	protected static void performCreatePost() {
		if (ViewCreatePost.theUser == null || ViewCreatePost.theUser.getUserName() == null
				|| ViewCreatePost.theUser.getUserName().isBlank()) {
			ViewCreatePost.showAlert("Error", "No valid user is logged in.");
			return;
		}

		String title = ViewCreatePost.text_PostTitle.getText();
		String body = ViewCreatePost.text_PostBody.getText();
		String threadName = null;

		if (ViewCreatePost.comboBox_ThreadName != null) {
			threadName = ViewCreatePost.comboBox_ThreadName.getValue();
		}

		if (title == null || title.trim().isEmpty()) {
			ViewCreatePost.showAlert("Validation Error", "Post title cannot be empty.");
			return;
		}

		if (body == null || body.trim().isEmpty()) {
			ViewCreatePost.showAlert("Validation Error", "Post body cannot be empty.");
			return;
		}

		if (threadName == null || threadName.isBlank()) {
			threadName = "General";
		}

		boolean success = ModelCreatePost.createPost(
				ViewCreatePost.theUser.getUserName(),
				title.trim(),
				body.trim(),
				threadName
		);

		if (success) {
			ViewCreatePost.showAlert("Success", "Post created successfully.");

			if (applicationMain.FoundationsMain.activeHomePage == 3) {
				guiRole2.ViewRole2Home.displayRole2Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
			} else {
				guiRole1.ViewRole1Home.displayRole1Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
			}
		} else {
			ViewCreatePost.showAlert("Error", "Failed to create post.");
		}
	}

	/**********
	 * <p> Method: performCancel() </p>
	 * 
	 * <p> Cancels post creation and returns to the correct home page. </p>
	 * 
	 */
	protected static void performCancel() {
		if (applicationMain.FoundationsMain.activeHomePage == 3) {
			guiRole2.ViewRole2Home.displayRole2Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
		} else {
			guiRole1.ViewRole1Home.displayRole1Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
		}
	}

	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: Logs out the current user and redirects to the login page, 
	 * where existing users can sign in and new users can begin account setup using an invitation code. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreatePost.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Terminates the application and ensures the database remains in a state 
	 * where the login page is shown on restart. </p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
}
