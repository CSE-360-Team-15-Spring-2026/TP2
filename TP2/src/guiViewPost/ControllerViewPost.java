package guiViewPost;

import guiRole1.ControllerRole1Home;
import guiRole1.ViewRole1Home;
import guiSearchPosts.ViewSearchPosts;
import guiSearchPosts.ModelSearchPosts;
import entityClasses.Post;
import entityClasses.Reply;

import java.util.List;

/**
 * <p> Title: ControllerViewPost class </p>
 * 
 * <p> Description: Controller for View Post functionality (Replies + Navigation) </p>
 * 
 */
public class ControllerViewPost {

	/**
	 * <p> Constructor - Not utilized </p> 
	 */
	public ControllerViewPost() {}
    /**
     * <p> Calls the previous GUI page that the user viewed </p>
     */
    public static void goBack() {
        if (ViewViewPost.previousPageType.equals("search")) {
            List<Post> savedResults = ModelSearchPosts.getLastSearchResults();
            ViewSearchPosts.populateResultsTable(savedResults);
            ViewSearchPosts.displaySearchPosts(ViewViewPost.theStage);
        } else {
            ViewRole1Home.displayRole1Home(ViewViewPost.theStage, ViewViewPost.theUser);
        }
    }

    /**
     * <p> Replies to a post </p>
     */
    protected static void performPostReply() {

        String replyBody = ViewViewPost.text_ReplyBody.getText();

        if (replyBody == null || replyBody.trim().isEmpty()) {
            showAlert("Validation Error", "Reply body cannot be empty.");
            return;
        }

        if (ViewViewPost.thePost == null) {
            showAlert("Error", "No post selected.");
            return;
        }

        if (ViewViewPost.theUser == null ||
            ViewViewPost.theUser.getUserName() == null ||
            ViewViewPost.theUser.getUserName().isBlank()) {
            showAlert("Error", "No valid user logged in.");
            return;
        }

        // Uses ModelViewPost for all reply operations
        boolean success = ModelViewPost.createReply(
            ViewViewPost.thePost.getPostID(),
            replyBody.trim()
        );

        if (success) {
            showAlert("Success", "Reply posted!");
            ViewViewPost.text_ReplyBody.clear();
            ViewViewPost.loadReplies();
        } else {
            showAlert("Error", "Failed to post reply.");
        }
    }

    /**
     * <p> Deletes the reply from the Post </p>
     */
    protected static void performDeleteReply() {

        int selectedIndex = ViewViewPost.list_Replies.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showAlert("No Selection", "Select a reply first.");
            return;
        }

        if (selectedIndex >= ViewViewPost.currentReplies.size()) {
            showAlert("Error", "Reply not found.");
            return;
        }

        Reply selectedReply = ViewViewPost.currentReplies.get(selectedIndex);
        String currentUser = ViewViewPost.theUser.getUserName();

        if (!selectedReply.getUsername().equals(currentUser)) {
            showAlert("Unauthorized", "You can only delete your own replies.");
            return;
        }

        boolean confirmed = ViewViewPost.showConfirmation(
            "Confirm Delete",
            "Delete this reply?"
        );

        if (!confirmed) return;

        // Use ModelViewPost for deletion
        boolean success = ModelViewPost.deleteReply(selectedReply);

        if (success) {
            showAlert("Success", "Reply deleted.");
            ViewViewPost.loadReplies();
        } else {
            showAlert("Error", "Delete failed.");
        }
    }

    /**
     * <p> Returns back to the Role1 home page </p>
     */
    protected static void performReturn() {

        if (applicationMain.FoundationsMain.activeHomePage == 3) {
            guiRole2.ViewRole2Home.displayRole2Home(
                ViewViewPost.theStage,
                ViewViewPost.theUser
            );
        } else {
            ViewRole1Home.displayRole1Home(
                ViewViewPost.theStage,
                ViewViewPost.theUser
            );
            ControllerRole1Home.loadAllPosts();
        }
    }
    
    /**
     * <p> Calls the guiEditReply package </p>
     */
    protected static void performEditReply() {

        int selectedIndex = ViewViewPost.list_Replies.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showAlert("No Selection", "Select a reply first.");
            return;
        }

        if (selectedIndex >= ViewViewPost.currentReplies.size()) {
            showAlert("Error", "Reply not found.");
            return;
        }

        Reply selectedReply = ViewViewPost.currentReplies.get(selectedIndex);
        String currentUser = ViewViewPost.theUser.getUserName();

        if (!selectedReply.getUsername().equals(currentUser)) {
            showAlert("Unauthorized", "You can only edit your own replies.");
            return;
        }
        
        if (selectedReply.isDeleted()) {
        	showAlert("Error", "Reply was deleted");
        	return;
        }

        // Open edit reply page
        guiEditReply.ViewEditReply.displayEditReply(
            ViewViewPost.theStage,
            ViewViewPost.theUser,
            selectedReply,
            ViewViewPost.thePost
        );
    }

    /**
     * <p> Helper function to show alerts </p>
     * @param title: text at the top of the dialog window
     * @param message: text in the body of the dialog
     */
    private static void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}