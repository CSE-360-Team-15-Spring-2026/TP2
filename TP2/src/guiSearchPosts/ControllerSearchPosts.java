package guiSearchPosts;

import entityClasses.Post;
import guiRole1.ViewRole1Home;

import java.util.List;

/*******
 * <p> Title: ControllerSearchPosts Class </p>
 *
 * <p> Description: Controller for the Search Posts page. Handles user interactions from the GUI
 * and coordinates actions between the View and Model components.</p>
 *
 * <p> Note: Methods are declared as protected since they are intended to be accessed only by
 * the View and Model classes within this MVC structure.</p>
 */
public class ControllerSearchPosts {

    /*-*******************************************************************************************

    User Interface Actions for this page

    This controller is not a class that gets instantiated.  Rather, it is a collection of protected
    static methods that can be called by the View (which is a singleton instantiated object) and
    the Model is often just a stub, or will be a singleton instantiated object.

    */

    /**
     * Default constructor is not used.
     */
    public ControllerSearchPosts() {
    }

    /**********
     * <p> Title: performSearch() Method. </p>
     *
     * <p> Description: Protected method that validates the keyword input and performs a search
     * for posts matching the keyword, optionally filtered by thread name. The method saves the
     * results for back navigation and populates the results table with matching posts. If no
     * results are found, an alert is displayed to inform the user. </p>
     *
     */
    public static void performSearch() {
        // Get input from text fields
        String keyword = ViewSearchPosts.textField_Keyword.getText().trim();
        String thread = ViewSearchPosts.textField_Thread.getText().trim();

        // Validate keyword
        if (keyword.isEmpty()) {
            ViewSearchPosts.showAlert("Invalid Input", "Please enter a keyword to search.");
            return;
        }

        // If thread is empty, treat as null (search all threads)
        String threadFilter = thread.isEmpty() ? null : thread;

        // Call model to search
        List<Post> results = ModelSearchPosts.search(keyword, threadFilter);

        // Save results for back navigation
        ModelSearchPosts.setLastSearchResults(results);

        // Populate results table
        ViewSearchPosts.populateResultsTable(results);

        // Show result count
        if (results.isEmpty()) {
            ViewSearchPosts.showAlert(
                "No Results",
                "No posts found matching '" + keyword + "'" +
                (threadFilter != null ? " in thread '" + threadFilter + "'" : "")
            );
        }
    }

    /**********
     * <p> Title: viewSelectedPost() Method. </p>
     * <p> Description: Protected method that retrieves the currently selected post from the
     * results table and opens the View Post page to display the post details and associated replies.
     * The method sets the navigation context to "search" so that the Back button on the View Post
     * page returns the user to the search results instead of the main home page. </p>
     *
     */
    public static void viewSelectedPost() {
        ViewSearchPosts.PostDisplay selected =
            ViewSearchPosts.table_Results.getSelectionModel().getSelectedItem();

        if (selected == null) {
            ViewSearchPosts.showAlert("No Selection", "Please select a post to view.");
            return;
        }

        int postId = selected.getPostId();
        Post post = guiRole1.ModelRole1Home.getPostById(postId);

        if (post == null) {
            ViewSearchPosts.showAlert("Error", "Post not found.");
            return;
        }

        // Set previous page type to "search" so Back button returns to search results
        guiViewPost.ViewViewPost.previousPageType = "search";

        // Open view post dialog
        guiViewPost.ViewViewPost.displayViewPost(
            ViewSearchPosts.theStage,
            null,
            post
        );
    }

    /**********
     * <p> Title: goBack() Method. </p>
     * <p> Description: Protected method that clears the search input fields to prepare for the
     * next search operation and returns the user to the main Role1Home page. </p>
     *
     */
    public static void goBack() {
        // Clear the search fields for next time
        ViewSearchPosts.textField_Keyword.clear();
        ViewSearchPosts.textField_Thread.clear();

        // Return to Role1Home
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewSearchPosts.theStage,
            guiRole1.ViewRole1Home.theUser
        );
    }
}
