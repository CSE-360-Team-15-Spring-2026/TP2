package guiViewPost;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import entityClasses.User;
import entityClasses.Post;
import entityClasses.Reply;
import java.util.List;
import java.util.ArrayList;

/**
 * <p> Title: ViewViewPost Class. </p>
 * 
 * <p> Description: View post with replies and add new replies </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 */
public class ViewViewPost {

    /** Width of the GUI page */
    private static double width = 700;
    /** Height of the GUI page */
    private static double height = 600;

    /** Displays the title of the selected post */
    protected static Label label_PostTitle = new Label();
    /** Displays the title of the selected post */
    protected static Label label_PostMeta = new Label();
    /** Text area showing the body of the selected post */
    protected static TextArea text_PostBody = new TextArea();

    /** Label for the replies section */
    protected static Label label_Replies = new Label("Replies:");
    /** List view displaying all replies */
    protected static ListView<String> list_Replies = new ListView<>();
    /** Observable list containing reply text for display */
    protected static ObservableList<String> replyData = FXCollections.observableArrayList();
    
    /** Stores Reply objects corresponding to the displayed replies */
    protected static List<Reply> currentReplies = new ArrayList<>();

    /** Label for the add reply section */
    protected static Label label_AddReply = new Label("Add Reply:");
    /** Text area for entering a new reply */
    protected static TextArea text_ReplyBody = new TextArea();
    /** Button to post a new reply */
    protected static Button button_PostReply = new Button("Post Reply");
    /** Button to delete the selected reply */
    protected static Button button_DeleteReply = new Button("Delete Selected Reply");
    /** Button to edit the selected reply */
    protected static Button button_EditReply = new Button("Edit Selected Reply");

    /** Button to return to the previous page */
    protected static Button button_Return = new Button("Return");

    /** Singleton instance of ViewViewPost */
    private static ViewViewPost theView;
    /** JavaFX Stage used to display this page */
    protected static Stage theStage;
    /** Root pane containing all GUI elements */
    protected static Pane theRootPane;
    /** Currently logged-in user */
    protected static User theUser;
    /** The post currently being viewed */
    protected static Post thePost;
    /** Scene used for this page */
    private static Scene theScene = null;
    /** Tracks whether the user came from the main page or search page */
    public static String previousPageType = "main"; 
    
    /**********
    * <p> Method: displayViewPost(Stage ps, User user, Post post) </p>
    * 
    * <p> Description: Displays the selected post, loads its details into the GUI,
    * marks it as read for the current user, and loads all associated replies. </p>
    * 
    * @param ps    the JavaFX Stage used to display this page
    * @param user  the currently logged-in user
    * @param post  the post to be viewed
    */

    public static void displayViewPost(Stage ps, User user, Post post) {
        theStage = ps;
        theUser = user;
        thePost = post;
        
        ModelViewPost.initialize(theUser.getUserName());
        
        if (theView == null) {
            theView = new ViewViewPost();
        }
        
        if (thePost != null && theUser != null) {
            applicationMain.FoundationsMain.database.markPostAsRead(
                theUser.getUserName(), 
                thePost.getPostID()
            );
        }
        
        label_PostTitle.setText(
            post.getTitle() == null || post.getTitle().isBlank() ? "(No Title)" : post.getTitle()
        );
        
        String formattedTimestamp = "";
        if (post.getTimestamp() != null) {
            java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedTimestamp = post.getTimestamp().format(formatter);
        }
        
        String postUser = (post.getUsername() == null || post.getUsername().isBlank())
            ? "Unknown User"
            : post.getUsername();

        String threadName = (post.getThreadName() == null || post.getThreadName().isBlank())
            ? "General"
            : post.getThreadName();
        
        label_PostMeta.setText(
            "By: " + postUser +
            " | Thread: " + threadName +
            " | " + formattedTimestamp
        );
        
        text_PostBody.setText(post.getBody() == null ? "" : post.getBody());
        text_ReplyBody.clear();
        
        if(post.isDeleted()) 
        {
    	label_PostTitle.setText("[Deleted]");
    	text_PostBody.setText("[Deleted]");
        }
        
        loadReplies();
        
        theStage.setTitle("View Post");
        theStage.setScene(theScene);
        theStage.show();
    }
    
    /**********
    * <p> Method: loadReplies() </p>
    * 
    * <p> Loads all replies associated with the current post and
    * displays them in the replies list. Deleted replies are shown with a
    * placeholder message. </p>
    */
    protected static void loadReplies() {
        replyData.clear();
        currentReplies.clear();
        
        if (thePost == null) {
            return;
        }
        
        if (thePost.isDeleted()) {
        	Reply temp = new Reply();
            replyData.add("Original post has been deleted.");
            currentReplies.add(temp);
        }
        
        List<Reply> replies = new ArrayList<>();
        
        try {
            // Student side
            if (applicationMain.FoundationsMain.activeHomePage == 2) {
                replies = guiRole1.ModelRole1Home.getRepliesForPost(thePost.getPostID());
            }
            // Staff side
            else if (applicationMain.FoundationsMain.activeHomePage == 3) {
                replies = applicationMain.FoundationsMain.database.getRepliesForPost(thePost.getPostID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (replies == null) {
            return;
        }
        
        for (Reply reply : replies) {
            if (reply == null) {
                continue;
            }
            
            String timestamp = "";
            if (reply.getTimestamp() != null) {
                java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                timestamp = reply.getTimestamp().format(formatter);
            }
            
            String replyBody = reply.getBody() == null ? "" : reply.getBody();
            String replyUser = (reply.getUsername() == null || reply.getUsername().isBlank())
                ? "Unknown User"
                : reply.getUsername();
            
            if (reply.isDeleted()) {
                replyBody = "[Deleted Reply]";
            }
            
            String replyText = String.format("[%s] %s: %s",
                timestamp,
                replyUser,
                replyBody);
            
            replyData.add(replyText);
            currentReplies.add(reply);
        }
    }
    /**********
    * <p> Constructor: ViewViewPost() </p>
    * 
    * <p> Description: Builds and initializes all GUI components for the View Post page.
    * Sets up the layout, styles, and event handlers for post details, replies,
    * reply actions, and navigation buttons. </p>
    */
    private ViewViewPost() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);
        
        // Post title (bold)
        label_PostTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label_PostTitle.setLayoutX(20);
        label_PostTitle.setLayoutY(20);
        label_PostTitle.setPrefWidth(660);
        
        // Post metadata (gray)
        setupLabelUI(label_PostMeta, "Arial", 14, 660, Pos.BASELINE_LEFT, 20, 50);
        label_PostMeta.setTextFill(Color.GRAY);
        
        // Post body (read-only)
        text_PostBody.setFont(Font.font("Arial", 14));
        text_PostBody.setLayoutX(20);
        text_PostBody.setLayoutY(80);
        text_PostBody.setPrefWidth(660);
        text_PostBody.setPrefHeight(120);
        text_PostBody.setWrapText(true);
        text_PostBody.setEditable(false);
        
        // Replies section
        setupLabelUI(label_Replies, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 215);
        
        list_Replies.setItems(replyData);
        list_Replies.setLayoutX(20);
        list_Replies.setLayoutY(245);
        list_Replies.setPrefWidth(660);
        list_Replies.setPrefHeight(150);
        
        // Add reply section
        setupLabelUI(label_AddReply, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 410);
        
        text_ReplyBody.setFont(Font.font("Arial", 14));
        text_ReplyBody.setLayoutX(20);
        text_ReplyBody.setLayoutY(440);
        text_ReplyBody.setPrefWidth(660);
        text_ReplyBody.setPrefHeight(60);
        text_ReplyBody.setWrapText(true);
        text_ReplyBody.setPromptText("Type your reply here...");
        
        setupButtonUI(button_EditReply, "Dialog", 14, 200, Pos.CENTER, 20, 515);
        button_EditReply.setOnAction((e) -> { ControllerViewPost.performEditReply(); });
        
        setupButtonUI(button_PostReply, "Dialog", 14, 150, Pos.CENTER, 225, 515);
        button_PostReply.setOnAction((e) -> { ControllerViewPost.performPostReply(); });
        
        setupButtonUI(button_DeleteReply, "Dialog", 14, 200, Pos.CENTER, 425, 515);
        button_DeleteReply.setOnAction((e) -> { ControllerViewPost.performDeleteReply(); });
        
        setupButtonUI(button_Return, "Dialog", 16, 150, Pos.CENTER, 275, 555);
        button_Return.setOnAction((e) -> { ControllerViewPost.performReturn(); });
        
        theRootPane.getChildren().addAll(
            label_PostTitle, label_PostMeta, text_PostBody,
            label_Replies, list_Replies,
            label_AddReply, text_ReplyBody,
            button_PostReply, button_EditReply, button_DeleteReply,
            button_Return
        );
    }

    /**********
    * Private local method to initialize the standard fields for a label
    *
    * @param l   the Label to configure
    * @param ff  the font family
    * @param f   the font size
    * @param w   the width of the label
    * @param p   the alignment
    * @param x   the x-coordinate (horizontal position)
    * @param y   the y-coordinate (vertical position)
    */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }

    /**********
    * Private local method to initialize the standard fields for a button
    *
    * @param b   the Button to configure
    * @param ff  the font family
    * @param f   the font size
    * @param w   the width of the button
    * @param p   the alignment
    * @param x   the x-coordinate (horizontal position)
    * @param y   the y-coordinate (vertical position)
    */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    
    /**********
    * <p> Method: showConfirmation(String title, String message) </p>
    *
    * <p> Description: Displays a confirmation dialog and returns true if the user
    * selects the OK option, otherwise returns false. </p>
    *
    * @param title   the title of the dialog window
    * @param message the message displayed in the dialog
    *
    * @return true if the user confirms, false otherwise
    */
    protected static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        java.util.Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
