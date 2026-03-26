package guiEditPost;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Post;

/**
 * <p> Title: ViewEditPost Class </p>
 * 
 * <p> Description: Displays the the editPost page where users update posts</p>
 *
 *
 * @version 1.00
 */
public class ViewEditPost {
    /** Sets window width*/
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    /** Sets window height*/
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
    
    // GUI Area 1: Header
    /** label for page title*/
    protected static Label label_PageTitle = new Label();
    /** label for Details of User*/
    protected static Label label_UserDetails = new Label();
    /** Button for Account Update*/
    protected static Button button_UpdateThisUser = new Button("Account Update");
    /** line seperator*/
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    // GUI Area 2: Edit Form
    /** sets post title label*/
    protected static Label label_PostTitle = new Label("Post Title:");
    /** text field for post title*/
    protected static TextField text_PostTitle = new TextField();
    
    /** sets label for post body*/
    protected static Label label_PostBody = new Label("Post Body:");
    /** text area for postbody*/
    protected static TextArea text_PostBody = new TextArea();

    /** label for the thread name*/
    protected static Label label_ThreadName = new Label("Thread:");
    /** combo box for thread name*/
    protected static ComboBox<String> comboBox_ThreadName = new ComboBox<>();

    /** button made to save changes*/
    protected static Button button_SaveChanges = new Button("Save Changes");
    /** button to cancel*/
    protected static Button button_Cancel = new Button("Cancel");

    /** line seperator */
    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
    
    // GUI Area 3: Navigation
    /** button for logging out*/
    protected static Button button_Logout = new Button("Logout");
    /** button for quiting*/ 
    protected static Button button_Quit = new Button("Quit");
    
    /** ViewEditPost class*/ 
    private static ViewEditPost theView;
    /** Database class*/
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /** the Stage*/
    protected static Stage theStage;
    /** the root pane*/
    protected static Pane theRootPane;
    /** the current user*/
    protected static User theUser;
    /** the current post*/
    protected static Post thePost;  // The post being edited

    /** scene for edit post*/
    private static Scene theViewEditPostScene;
    /** the role 2*/
    protected static final int theRole = 2;

    /**
     * <p> Display Edit Post page with the post to edit </p>
     *
     * @param ps    the stage value
     * @param user  current user 
     * @param post  post to be edited
     */
    public static void displayEditPost(Stage ps, User user, Post post) {
        theStage = ps;
        theUser = user;
        thePost = post;
        
        if (theView == null) {
            theView = new ViewEditPost();
        }
        
        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;
        
        label_UserDetails.setText("User: " + theUser.getUserName());
        
        // Load existing post data
        if (thePost != null) {
            text_PostTitle.setText(thePost.getTitle() == null ? "" : thePost.getTitle());
            text_PostBody.setText(thePost.getBody() == null ? "" : thePost.getBody());
            
            // Load threads and select current thread
            loadThreads();
            if (thePost.getThreadName() != null && !thePost.getThreadName().isBlank()) {
                comboBox_ThreadName.setValue(thePost.getThreadName());
            }
        } else {
            text_PostTitle.clear();
            text_PostBody.clear();
            loadThreads();
        }
        
        theStage.setTitle("CSE 360 Foundations: Edit Post");
        theStage.setScene(theViewEditPostScene);
        theStage.show();
    }
    
    /**
     * <p> Load available threads into combo box </p>
     *
     */
    private static void loadThreads() {
        comboBox_ThreadName.getItems().clear();
        java.util.List<String> threads = theDatabase.getAllThreads();
        if (threads != null && !threads.isEmpty()) {
            comboBox_ThreadName.getItems().addAll(threads);
        }
        if (comboBox_ThreadName.getItems().isEmpty()) {
            comboBox_ThreadName.getItems().add("General");
        }
        if (comboBox_ThreadName.getValue() == null) {
            comboBox_ThreadName.setValue("General");
        }
    }
    
    /**
     * <p> Constructor - creates all GUI elements</p>
     */
    private ViewEditPost() {
        theRootPane = new Pane();
        theViewEditPostScene = new Scene(theRootPane, width, height);
        
        // GUI Area 1: Header
        label_PageTitle.setText("Edit Post");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
        
        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> {ControllerEditPost.performUpdate(); });
        
        // GUI Area 2: Edit Form
        setupLabelUI(label_PostTitle, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 115);
        
        text_PostTitle.setFont(Font.font("Arial", 16));
        text_PostTitle.setLayoutX(20);
        text_PostTitle.setLayoutY(145);
        text_PostTitle.setMinWidth(740);
        text_PostTitle.setMaxWidth(740);
        text_PostTitle.setPromptText("Enter post title...");
        
        setupLabelUI(label_PostBody, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 185);
        
        text_PostBody.setFont(Font.font("Arial", 16));
        text_PostBody.setLayoutX(20);
        text_PostBody.setLayoutY(215);
        text_PostBody.setMinWidth(740);
        text_PostBody.setMaxWidth(740);
        text_PostBody.setPrefHeight(200);
        text_PostBody.setWrapText(true);
        text_PostBody.setPromptText("Enter post body...");
        
        setupLabelUI(label_ThreadName, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 425);
        
        comboBox_ThreadName.setLayoutX(20);
        comboBox_ThreadName.setLayoutY(455);
        comboBox_ThreadName.setPrefWidth(300);
        comboBox_ThreadName.setPromptText("Select thread");
        
        setupButtonUI(button_SaveChanges, "Dialog", 16, 180, Pos.CENTER, 20, 490);
        button_SaveChanges.setOnAction((_) -> {ControllerEditPost.performSaveChanges(); });
        
        setupButtonUI(button_Cancel, "Dialog", 16, 180, Pos.CENTER, 220, 490);
        button_Cancel.setOnAction((_) -> {ControllerEditPost.performCancel(); });
        
        // GUI Area 3: Navigation
        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerEditPost.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerEditPost.performQuit(); });

        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
            label_PostTitle, text_PostTitle,
            label_PostBody, text_PostBody,
            label_ThreadName, comboBox_ThreadName,
            button_SaveChanges, button_Cancel,
            line_Separator4, button_Logout, button_Quit);
    }
    /**
     * <p> sets up ui for label</p>
     *
     * @param l    label
     * @param ff   font family
     * @param f    font size
     * @param w    width
     * @param p    alignment
     * @param x    x coordinate
     * @param y    y coordinate
     */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }
     /**
     * <p> sets up ui for button</p>
     *
     * @param b    button to be setup
     * @param ff   font family
     * @param f    font size
     * @param w    width
     * @param p    alignment
     * @param x    x coordinate
     * @param y    y coordinate
     */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    /**
     * <p> show and alert </p>
     *
     * @param title    title of pop up window
     * @param message    message of pop up
     * 
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
