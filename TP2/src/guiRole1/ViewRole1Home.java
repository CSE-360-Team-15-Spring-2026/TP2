package guiRole1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Post;
import java.util.List;

/**
 * <p> Title: ViewRole1Home Class. </p>
 * 
 * <p> Description: Student Discussion System Home Page with CRUD operations for posts and replies </p>
 * <p> Includes a table which displays all posts and their name, user, thread, status, date and time </p>
 * <p> also buttons to create post, edit post, delete post, view all posts, view my posts, search posts, log out and quit. </p>
 * 
 */
public class ViewRole1Home {
    
	/** Width of the GUI page */
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    
    /** Height of the GUI page */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Page title and user info
    /** Title of the page */
    protected static Label label_PageTitle = new Label();
    /** Username and Role of the user */
    protected static Label label_UserDetails = new Label();
    /** Button to take the user to Account Update page */
    protected static Button button_UpdateThisUser = new Button("Account Update");
    
    /** Line Separator between the page title, user details and account update button and role1 home gui */
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
    
    // Action buttons
    /** Button that takes the user to the CreatePost GUI page */
    protected static Button button_CreatePost = new Button("Create New Post");
    /** Button that lets the user view all created posts */
    protected static Button button_ViewAllPosts = new Button("View All Posts");
    /** Button that displays only the posts created by the User */
    protected static Button button_ViewMyPosts = new Button("View My Posts");
    /** Button that takes the user to the SearchPosts GUI page */
    protected static Button button_SearchPosts = new Button("Search Posts");
    
    /** Line Separator between the buttons above and the Posts TableView */
    protected static Line line_Separator2 = new Line(20, 160, width-20, 160);
    
    // Post table
    /** Table that displays all Posts */
    protected static TableView<PostDisplay> table_Posts = new TableView<>();
    /** List of all the Posts to be displayed in the table */
    protected static ObservableList<PostDisplay> postData = FXCollections.observableArrayList();
    
    /** Line Separator between the table and the buttons below it */
    protected static Line line_Separator3 = new Line(20, 480, width-20, 480);
    
    // Post action buttons
    /** Button that takes the user to the ViewPost GUI page for a specific post */
    protected static Button button_ViewPost = new Button("View Post & Replies");
    /** Button that takes the user to the EditPost GUI page for a specific post */
    protected static Button button_EditPost = new Button("Edit Post");
    /** Button that allows the user to delete a post, given that it is theirs */
    protected static Button button_DeletePost = new Button("Delete Post");
    
    /** Line Separator between the buttons above and the log out and quit buttons */
    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
    
    // Navigation
    /** Button that logs the user out and takes them to the login page */
    protected static Button button_Logout = new Button("Logout");
    /** Button that terminates the program */
    protected static Button button_Quit = new Button("Quit");

    /** Instance of ViewRole1Home */
    private static ViewRole1Home theView;
    /** Connection to the Database */
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    /** The Stage that JavaFX has established for the GUI */
    protected static Stage theStage;
    /** Pane that holds all the GUI widgets */
    protected static Pane theRootPane;
    /** The current logged in User object */
    public static User theUser;
    /** Shared Scene that is populated and displayed */
    private static Scene theViewRole1HomeScene = null;
    /** Role Identifier */
    protected static final int theRole = 2;

    /**
     * <p> Entry point to display student home page. </p>
     * <p> Calls ModelRole1Home and ControllerRole1Home to get the username and posts </p>
     * <p> Sets the title and scene </p>
     * @param ps: the Stage
     * @param user: the current user
     */
    public static void displayRole1Home(Stage ps, User user) {
        theStage = ps;
        theUser = user;
        
        if (theView == null) {
            theView = new ViewRole1Home();
        }
        
        // Initialize the model with current user
        ModelRole1Home.initialize(theUser.getUserName());
        
        // Load all posts by default
        ControllerRole1Home.loadAllPosts();
        
        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;
        
        label_UserDetails.setText("User: " + theUser.getUserName());
        
        theStage.setTitle("CSE 360 Foundations: Student Discussion System");
        theStage.setScene(theViewRole1HomeScene);
        theStage.show();
    }
    
    /**
     * <p> Constructor - creates all GUI elements, such as the Labels, Buttons and Table </p>
     */
    private ViewRole1Home() {
        theRootPane = new Pane();
        theViewRole1HomeScene = new Scene(theRootPane, width, height);
        
        // Page title
        label_PageTitle.setText("Student Discussion System");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
        
        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> 
            {guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });
        
        // Action buttons
        setupButtonUI(button_CreatePost, "Dialog", 16, 150, Pos.CENTER, 20, 110);
        button_CreatePost.setOnAction((_) -> {ControllerRole1Home.createNewPost(); });
        
        setupButtonUI(button_ViewAllPosts, "Dialog", 16, 150, Pos.CENTER, 190, 110);
        button_ViewAllPosts.setOnAction((_) -> {ControllerRole1Home.loadAllPosts(); });
        
        setupButtonUI(button_ViewMyPosts, "Dialog", 16, 150, Pos.CENTER, 360, 110);
        button_ViewMyPosts.setOnAction((_) -> {ControllerRole1Home.loadMyPosts(); });

        setupButtonUI(button_SearchPosts, "Dialog", 16, 150, Pos.CENTER, 530, 110);
        button_SearchPosts.setOnAction((_) -> {ControllerRole1Home.searchPosts(); });

        // Table
        setupTableView();
        
        // Post actions
        setupButtonUI(button_ViewPost, "Dialog", 16, 180, Pos.CENTER, 20, 495);
        button_ViewPost.setOnAction((_) -> {ControllerRole1Home.viewPost(); });
        
        setupButtonUI(button_EditPost, "Dialog", 16, 150, Pos.CENTER, 220, 495);
        button_EditPost.setOnAction((_) -> {ControllerRole1Home.editPost(); });
        
        setupButtonUI(button_DeletePost, "Dialog", 16, 150, Pos.CENTER, 390, 495);
        button_DeletePost.setOnAction((_) -> {ControllerRole1Home.deletePost(); });
        
        // Navigation
        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerRole1Home.performLogout(); });
    
        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerRole1Home.performQuit(); });
        
        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
            button_CreatePost, button_ViewAllPosts, button_ViewMyPosts, button_SearchPosts,
            line_Separator2,
            table_Posts, line_Separator3,
            button_ViewPost, button_EditPost, button_DeletePost, line_Separator4,
            button_Logout, button_Quit);
    }

    /**
     * <p> Setups the TableView with columns: ID, Title, Thread, Author, Replies, Status and Date. </p>
     */
    private void setupTableView() {
    	// ID Column (50px)
    	TableColumn<PostDisplay, Integer> colId = new TableColumn<>("ID");
    	colId.setCellValueFactory(new PropertyValueFactory<>("postId"));
    	colId.setPrefWidth(50);
    	colId.setMaxWidth(50);
    	colId.setMinWidth(50);

    	// Title Column (350px -> reduce this to make room for Thread)
    	TableColumn<PostDisplay, String> colTitle = new TableColumn<>("Title");
    	colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    	colTitle.setPrefWidth(250);  // CHANGED from 350 to 250

    	// Thread Column (100px) - NEW!
    	TableColumn<PostDisplay, String> colThread = new TableColumn<>("Thread");
    	colThread.setCellValueFactory(new PropertyValueFactory<>("thread"));
    	colThread.setPrefWidth(100);

    	// Author Column (120px)
    	TableColumn<PostDisplay, String> colAuthor = new TableColumn<>("Author");
    	colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    	colAuthor.setPrefWidth(120);

    	// Replies Column (80px)
    	TableColumn<PostDisplay, Integer> colReplies = new TableColumn<>("Replies");
    	colReplies.setCellValueFactory(new PropertyValueFactory<>("replyCount"));
    	colReplies.setPrefWidth(80);

    	// Status Column (80px)
    	TableColumn<PostDisplay, String> colStatus = new TableColumn<>("Status");
    	colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    	colStatus.setPrefWidth(80);

    	// Date Column (160px)
    	TableColumn<PostDisplay, String> colDate = new TableColumn<>("Date");
    	colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    	colDate.setPrefWidth(160);

    	// Add all columns to the table
    	table_Posts.getColumns().addAll(colId, colTitle, colThread, colAuthor, colReplies, colStatus, colDate);
        table_Posts.setItems(postData);
        
        table_Posts.setLayoutX(20);
        table_Posts.setLayoutY(175);
        table_Posts.setPrefWidth(760);
        table_Posts.setPrefHeight(290);
    }
    
    /**
     * <p> Populates the table with all the posts. </p>
     * @param posts: ArrayList of Posts to be displayed
     */
    protected static void populatePostTable(List<Post> posts) {
        
        postData.clear();
        for (Post post : posts) {
               postData.add(new PostDisplay(post));
        }
    }
    
    /**
     * <p> Shows alert dialogs </p>
     * @param title: text displayed at the top of the dialog window
     * @param message: the text in the body of the dialog
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * <p> Shows confirmation dialogs. </p>
     * 
     * @param title: the text displayed at the top of the dialog window
     * @param message: the text in the body of the dialog
     * @return true if the user click OK, false if the user clicks Cancel or closes the dialog window
     */
    protected static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        java.util.Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Helper methods
    /**
     * <p> Helps setup Labels </p>
     * @param l: label name
     * @param ff: font name
     * @param f: font size
     * @param w: minimum width
     * @param p: alignment position
     * @param x: location on the x axis
     * @param y: location on the y axis
     */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }
    
    /**
     * <p> Helps setup Buttons </p>
     * @param b: button name
     * @param ff: font name
     * @param f: font size
     * @param w: minimum width
     * @param p: alignment position
     * @param x: location on the x axis
     * @param y: location on the y axis
     */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    
    /**
     * Inner class for TableView display
     * 
     */
    public static class PostDisplay {
    	/** Unique ID for the post */
        private final int postId;
        /** Title of the post */
        private String title;
        /** Thread the post is a part of */
        private final String thread;    
        /** Username of the author of the post */
        private final String author;
        /** Number of replies to the post */
        private final int replyCount;
        /** Status of the post relative to the user viewing it */
        private final String status;
        /** Time and date the post was posted on */
        private final String timestamp;
        /** The Post object */
        private final Post post;

        /**
         * <p> Gets all the details of the Posts for the TableVie>w </p>
         * @param post: object of the current post
         */
        public PostDisplay(Post post) {
            this.post = post;
            this.postId = post.getPostID();
            this.thread = post.getThreadName();  // NEW!
            if(post.isDeleted()) this.title = "[Deleted]";
            else 				 this.title = post.getTitle();
            this.author = post.getUsername();
            this.replyCount = ModelRole1Home.getReplyCount(post.getPostID());
            this.status = ModelRole1Home.isRead(post.getPostID()) ? "READ" : "UNREAD";
            this.timestamp = ModelRole1Home.getFormattedTimestamp(post);
        }

        // Getters
        /** 
         * <p> Gets PostID </p> 
         * @return PostID
         */
        public int getPostId() { return postId; }
        /** 
         * <p> Gets Post Title </p> 
         * @return Title
         */
        public String getTitle() { return title; }
        /** 
         * <p> Gets the Thread </p> 
         * @return thread name
         */
        public String getThread() { return thread; } 
        /** 
         * <p> Gets the Username of the author </p> 
         * @return username of the author
         */
        public String getAuthor() { return author; }
        /** 
         * <p> Gets the number of replies </p> 
         * @return number of replies
         */
        public int getReplyCount() { return replyCount; }
        /** 
         * <p> Gets status: if read or unread </p> 
         * @return if the post has been read or not
         */
        public String getStatus() { return status; }
        /** 
         * <p> Gets Date and time of posting </p> 
         * @return date and time of posting
         */
        public String getTimestamp() { return timestamp; }
        /** 
         * <p> Gets the current Post's object </p> 
         * @return the post object
         */
        public Post getPost() { return post; }
    }
}