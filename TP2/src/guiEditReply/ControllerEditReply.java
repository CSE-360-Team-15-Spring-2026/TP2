package guiEditReply;

/**
 * <p> Title: ControllerEditReply Class </p>
 *
 * <p> Description: Controller for Edit Reply page </p>
 */
public class ControllerEditReply {

	/**
	 * <p> Constructor - Not Utilized </p>
	 */
	public ControllerEditReply() {}
    /**
     * <p> Saves the changes to the reply </p>
     */
    protected static void performSaveChanges() {
        String body = ViewEditReply.text_ReplyBody.getText();
        
        if (body == null || body.trim().isEmpty()) {
            ViewEditReply.showAlert("Validation Error", "Reply body cannot be empty.");
            return;
        }
        
        boolean success = ModelEditReply.updateReply(
            ViewEditReply.theReply,
            body
        );
        
        if (success) {
            ViewEditReply.showAlert("Success", "Reply updated successfully!");
            performCancel();  // Return to view post
        } else {
            ViewEditReply.showAlert("Error", "Failed to update reply.");
        }
    }
    
    /**
     * <p> Cancels editing and calls the guiViewPost package </p>
     */
    protected static void performCancel() {
        guiViewPost.ViewViewPost.displayViewPost(
            ViewEditReply.theStage,
            ViewEditReply.theUser,
            ViewEditReply.thePost
        );
    }
    
    /**
     * <p> Calls the guiUpdateUser package </p>
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
            ViewEditReply.theStage,
            ViewEditReply.theUser
        );
    }

    /**
     * <p> Logs the user out and returns them to the login GUI page </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewEditReply.theStage);
    }

    /**
     * <p> Terminates the program </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}