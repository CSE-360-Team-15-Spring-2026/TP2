package guiEditPost;

/*******
 * <p> Title: ControllerEditPost Class </p>
 *
 * <p> Description: A class under the guiEditPost package which holds the funcionality needed to allow students to edit posts.</p>
 * <p> The operations in the class save the changes that students would like to make that were saved into database.</p>
 * <p> All the attributes and functionality was decided and implemented using the Student User Stories as well as the Staff EPICS</p>
 * 
 * @version 1.00    
 */
public class ControllerEditPost {
	
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public  ControllerEditPost() {}
	
    /*****
     * <p> Save changes made to the post into the database: if title and body are not empty.</p>
     * <p> Thread name defaults to "General" if empty.</p>
     */
    protected static void performSaveChanges() {
        String title = ViewEditPost.text_PostTitle.getText();
        String body = ViewEditPost.text_PostBody.getText();
        String threadName = ViewEditPost.comboBox_ThreadName.getValue();
        
        if (title == null || title.trim().isEmpty()) {
            ViewEditPost.showAlert("Validation Error", "Post title cannot be empty.");
            return;
        }
        
        if (body == null || body.trim().isEmpty()) {
            ViewEditPost.showAlert("Validation Error", "Post body cannot be empty.");
            return;
        }
        
        if (threadName == null || threadName.isBlank()) {
            threadName = "General";
        }
        
        boolean success = ModelEditPost.updatePost(
            ViewEditPost.thePost,
            title,
            body,
            threadName
        );
        
        if (success) {
            ViewEditPost.showAlert("Success", "Post updated successfully!");
            performCancel();  // Return to home
        } else {
            ViewEditPost.showAlert("Error", "Failed to update post.");
        }
    }
    
    /*****
     * <p> Cancel editing and return to home depending on role of user.</p>
     */
    protected static void performCancel() {
        if (applicationMain.FoundationsMain.activeHomePage == 2) {
            guiRole1.ViewRole1Home.displayRole1Home(
                ViewEditPost.theStage,
                ViewEditPost.theUser
            );
        } else {
            guiRole2.ViewRole2Home.displayRole2Home(
                ViewEditPost.theStage,
                ViewEditPost.theUser
            );
        }
    }
    /*****
     * <p> Updates Display.</p>
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
            ViewEditPost.theStage,
            ViewEditPost.theUser
        );
    }
    /*****
     * <p> logout current User.</p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewEditPost.theStage);
    }
    /*****
     * <p> Will shutdown program.</p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}
