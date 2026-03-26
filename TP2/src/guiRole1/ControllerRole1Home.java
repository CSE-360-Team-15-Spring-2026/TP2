package guiRole1;

import entityClasses.Post;
import java.util.List;

/**
 * <p> Title: ControllerRole1Home Class. </p>
 * 
 * <p> Description: Controller for Student Discussion System - handles all user actions </p>
 * 
 */
public class ControllerRole1Home {
    
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public  ControllerRole1Home() {}
	
    /**
     * <p> Loads all the posts into the table </p>
     */
    public static void loadAllPosts() {
        List<Post> posts = ModelRole1Home.getAllPosts();
        ViewRole1Home.populatePostTable(posts);
    }
    
    /**
     * <p> Loads only the current user's posts into the table </p>
     */
    protected static void loadMyPosts() {
        List<Post> posts = ModelRole1Home.getMyPosts();
        ViewRole1Home.populatePostTable(posts);
        
        if (posts.isEmpty()) {
            ViewRole1Home.showAlert("No Posts", "You have not created any posts yet.");
        }
    }
    
    /**
     * <p> Call the displayCreatePost function in guiCreatePost </p>
     */
    protected static void createNewPost() {
        guiCreatePost.ViewCreatePost.displayCreatePost(ViewRole1Home.theStage, 
            ViewRole1Home.theUser);
    }
    
    /**
     * <p> Views the selected post by calling the guiViewPost package </p>
     * <p> Shows an alert message if no post is selected </p>
     */
    protected static void viewPost() {
        ViewRole1Home.PostDisplay selected = 
            ViewRole1Home.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            ViewRole1Home.showAlert("No Selection", 
                "Please select a post to view.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelRole1Home.getPostById(postId);
        
        if (post == null) {
            ViewRole1Home.showAlert("Error", "Post not found.");
            return;
        }
        
        
        // Open view post dialog
        guiViewPost.ViewViewPost.displayViewPost(ViewRole1Home.theStage, 
            ViewRole1Home.theUser, post);
    }
    
    /**
     * <p> Calls guiEditPost to edit the selected post </p>
     * <p> Shows an alert if no post is selected and if the post is not authored by the current user </p>
     */
    public static void editPost() {
        ViewRole1Home.PostDisplay selected = ViewRole1Home.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            ViewRole1Home.showAlert("No Selection", "Please select a post to edit.");
            return;
        }
        
        Post post = selected.getPost();
        
        // Check ownership
        if (!post.getUsername().equals(ModelRole1Home.getCurrentUser())) {
            ViewRole1Home.showAlert("Unauthorized", "You can only edit your own posts.");
            return;
        }
        
        // Check if it's deleted
        if (post.isDeleted()) {
        	ViewRole1Home.showAlert("Error", "Post was deleted");
        	return;
        }
        
        // Open edit page
        guiEditPost.ViewEditPost.displayEditPost(
            ViewRole1Home.theStage,
            ViewRole1Home.theUser,
            post
        );
    }
    
    /**
     * <p> Deletes the selected post with confirmation </p>
     */
    protected static void deletePost() {
        ViewRole1Home.PostDisplay selected = 
            ViewRole1Home.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            ViewRole1Home.showAlert("No Selection", 
                "Please select a post to delete.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelRole1Home.getPostById(postId);
        
        if (post == null) {
            ViewRole1Home.showAlert("Error", "Post not found.");
            return;
        }
        
        // Check if user is the author (using getUsername instead of getAuthor)
        if (!post.getUsername().equals(ModelRole1Home.getCurrentUser())) {
            ViewRole1Home.showAlert("Unauthorized", 
                "You can only delete your own posts.");
            return;
        }
        
        if (post.isDeleted()) {
            ViewRole1Home.showAlert("Error", "Post already deleted.");
            return;
        }
        
        // Confirmation dialog
        boolean confirmed = ViewRole1Home.showConfirmation("Confirm Delete", 
            "Are you sure you want to delete this post?\n\n" +
            "Title: " + post.getTitle() + "\n\n" +
            "Note: Replies to this post will remain visible.");
        
        if (confirmed) {
            boolean success = ModelRole1Home.deletePost(postId);
            
            if (success) {
                ViewRole1Home.showAlert("Success", "Post deleted successfully.");
                loadAllPosts(); // Refreshes the table
            } else {
                ViewRole1Home.showAlert("Error", "Failed to delete post.");
            }
        }
    }
    
    /**
     * <p> Logs the user out and returns them to login page </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1Home.theStage);
    }
    
    /**
     * <p> Calls the guiSearchPosts package </p>
     */
    protected static void searchPosts() {
        guiSearchPosts.ViewSearchPosts.displaySearchPosts(ViewRole1Home.theStage);
    }

    /**
     * <p> Terminates the program </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}