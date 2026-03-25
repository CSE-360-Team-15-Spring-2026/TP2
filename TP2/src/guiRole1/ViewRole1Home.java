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
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @version 2.00 2025-02-07 Updated with discussion system functionality
 */
public class ViewRole1Home {
    
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Page title and user info
    protected static Label label_PageTitle = new Label();
    protected static Label label_UserDetails = new Label();
    protected static Button button_UpdateThisUser = new Button("Account Update");
    
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
    
    // Action buttons
    protected static Button button_CreatePost = new Button("Create New Post");
    protected static Button button_ViewAllPosts = new Button("View All Posts");
    protected static Button button_ViewMyPosts = new Button("View My Posts");
    protected static Button button_SearchPosts = new Button("Search Posts");

    protected static Line line_Separator2 = new Line(20, 160, width-20, 160);
    
    // Post table
    protected static TableView<PostDisplay> table_Posts = new TableView<>();
    protected static ObservableList<PostDisplay> postData = FXCollections.observableArrayList();
    
    protected static Line line_Separator3 = new Line(20, 480, width-20, 480);
    
    // Post action buttons
    protected static Button button_ViewPost = new Button("View Post & Replies");
    protected static Button button_EditPost = new Button("Edit Post");
    protected static Button button_DeletePost = new Button("Delete Post");
    
    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
    
    // Navigation
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    private static ViewRole1Home theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    protected static Stage theStage;
    protected static Pane theRootPane;
    public static User theUser;
    private static Scene theViewRole1HomeScene = null;
    protected static final int theRole = 2;

    /**
     * Entry point to display student home page
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
     * Constructor - creates all GUI elements
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
     * Setups the TableView with columns
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
     * Populates the table with posts
     */
    protected static void populatePostTable(List<Post> posts) {
        
        postData.clear();
        for (Post post : posts) {
            if (!post.isDeleted()) {
                postData.add(new PostDisplay(post));
            } else {
            }
        }
    }
    
    /**
     * Show alert dialog
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show confirmation dialog
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
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }
    
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    
    private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y){
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);
    }
    
    /**
     * Inner class for TableView display
     * ADAPTED FOR FRIEND'S POST CLASS
     */
    public static class PostDisplay {
        private final int postId;
        private final String title;
        private final String thread;     // NEW!
        private final String author;
        private final int replyCount;
        private final String status;
        private final String timestamp;
        private final Post post;

        public PostDisplay(Post post) {
            this.post = post;
            this.postId = post.getPostID();
            this.title = post.getTitle();
            this.thread = post.getThreadName();  // NEW!
            this.author = post.getUsername();
            this.replyCount = ModelRole1Home.getReplyCount(post.getPostID());
            this.status = ModelRole1Home.isRead(post.getPostID()) ? "READ" : "UNREAD";
            this.timestamp = ModelRole1Home.getFormattedTimestamp(post);
        }

        // Getters
        public int getPostId() { return postId; }
        public String getTitle() { return title; }
        public String getThread() { return thread; }  // NEW!
        public String getAuthor() { return author; }
        public int getReplyCount() { return replyCount; }
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
        public Post getPost() { return post; }
    }
}