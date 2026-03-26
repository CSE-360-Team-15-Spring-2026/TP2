package guiSearchPosts;
import entityClasses.Post;
import guiRole1.ModelRole1Home;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ModelSearchPosts Class </p>
 *
 * <p> Description: Handles data-related operations for the Search Posts page.
 * Manages search functionality and stores search results for back navigation. </p>
 *
 */
public class ModelSearchPosts {
	/** List of the last search results */
	private static List<Post> lastSearchResults = new ArrayList<>();

	/**
	 * Default constructor is not used.
	 */
	public ModelSearchPosts() {
	}

	/**********
	 * <p> Title: search(String keyword, String thread) Method. </p>
	 *
	 * <p> Description: Protected method that delegates to the Model layer to search for posts
	 * matching the given keyword, optionally filtered by thread name. Returns a list of all
	 * matching posts that meet the search criteria. </p>
	 *
	 * @param keyword         The search keyword to match against post content
	 * @param thread          The optional thread name filter (null to search all threads)
	 *
	 * @return                A List of Post objects matching the search criteria
	 *
	 */
    public static List<Post> search(String keyword, String thread) {
         return ModelRole1Home.searchPosts(keyword, thread);                  
    }          
    /**********
     * <p> Title: isPostRead(int postId) Method. </p>
     *
     * <p> Description: Protected method that checks whether the current user has marked the
     * specified post as read. This is used to display the read status in the search results table
     * and elsewhere in the GUI. </p>
     *
     * @param postId          The ID of the post to check
     *
     * @return                true if the post has been read by the current user, false otherwise
     *
     */
    public static boolean isPostRead(int postId) {
        return ModelRole1Home.isRead(postId);
    }

    /**********
     * <p> Title: setLastSearchResults Method. </p>
	 *
     * <p> Description: Protected method that caches the most recent search results to enable
     * proper navigation when the user returns from viewing a post detail page. This allows the
     * search results table to be restored without requiring a new search operation. </p>
     *
     * @param results         The list of posts returned from the most recent search
     *
     */
    public static void setLastSearchResults(List<Post> results) {
        lastSearchResults = new ArrayList<>(results);
    }

    /**********
     * <p> Title: getLastSearchResults() Method. </p>
     *
     * <p> Description: Protected method that retrieves the cached search results from the most
     * recent search operation. This is used to restore the search results table when the user
     * returns from viewing a post detail page, providing a seamless navigation experience. </p>
     *
     * @return                A copy of the cached list of posts from the last search
     *
     */
    public static List<Post> getLastSearchResults() {
        return new ArrayList<>(lastSearchResults);
    }
}                                                                            

