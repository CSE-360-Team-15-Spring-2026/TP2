package guiEditReply;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Reply;
import entityClasses.Post;

/**
 * <p> Title: ViewEditReply Class </p>
 * 
 * <p> Description: Edit an existing reply </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 */
public class ViewEditReply {

    /** Width of the GUI page */
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    /** Height of the GUI page */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // GUI Area 1: Header
    /** Title of the page */
    protected static Label label_PageTitle = new Label();
    /** Displays the current logged-in user */
    protected static Label label_UserDetails = new Label();
    /** Button to open the Account Update page */
    protected static Button button_UpdateThisUser = new Button("Account Update");
    
    /** Separator between header and main content */
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    // GUI Area 2: Edit Form
    /** Label for original post section */
    protected static Label label_OriginalPost = new Label("Original Post:");
    /** Displays the original post content */
    protected static Label label_OriginalPostText = new Label();

    /** Label for reply body input */
    protected static Label label_ReplyBody = new Label("Reply Body:");
    /** Text area for editing the reply */
    protected static TextArea text_ReplyBody = new TextArea();

    /** Button to save updated reply */
    protected static Button button_SaveChanges = new Button("Save Changes");
    /** Button to cancel editing and return */
    protected static Button button_Cancel = new Button("Cancel");

    /** Separator before footer section */
    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
    
    // GUI Area 3: Navigation
    /** Button that logs the user out */
    protected static Button button_Logout = new Button("Logout");
    /** Button that exits the application */
    protected static Button button_Quit = new Button("Quit");

    /** Singleton instance of ViewEditReply */
    private static ViewEditReply theView;
    /** Connection to the database */
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /** JavaFX Stage used to display this page */
    protected static Stage theStage;
    /** Root pane containing all UI elements */
    protected static Pane theRootPane;
    /** Currently logged-in user */
    protected static User theUser;
    /** The reply currently being edited */
    protected static Reply theReply;
    /** The parent post associated with the reply */
    protected static Post thePost;    

    /** Scene used for this page */
    private static Scene theViewEditReplyScene;
    /** Role identifier (Role1 = Student) */
    protected static final int theRole = 2;

    /**********
    * <p> Method: displayEditReply(Stage ps, User user, Reply reply, Post post) </p>
    * 
    * <p> Description: Displays the Edit Reply page. Initializes the stage, user, 
    * reply, and post context, and populates the UI with existing reply data.</p>
    * 
    * @param ps     the JavaFX Stage used to display this page
    * @param user   the currently logged-in user
    * @param reply  the reply being edited
    * @param post   the parent post associated with the reply
    */
    public static void displayEditReply(Stage ps, User user, Reply reply, Post post) {
        theStage = ps;
        theUser = user;
        theReply = reply;
        thePost = post;
        
        if (theView == null) {
            theView = new ViewEditReply();
        }
        
        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;
        
        label_UserDetails.setText("User: " + theUser.getUserName());
        
        // Show original post context
        if (thePost != null) {
            String postTitle = thePost.getTitle() == null ? "(No Title)" : thePost.getTitle();
            label_OriginalPostText.setText("Replying to: " + postTitle);
        }
        
        // Load existing reply data
        if (theReply != null) {
            text_ReplyBody.setText(theReply.getBody() == null ? "" : theReply.getBody());
        } else {
            text_ReplyBody.clear();
        }
        
        theStage.setTitle("CSE 360 Foundations: Edit Reply");
        theStage.setScene(theViewEditReplyScene);
        theStage.show();
    }
    
    /**********
    * <p> Constructor: ViewEditReply() </p>
    * 
    * <p> Description: Builds and initializes all GUI components for the Edit Reply page.
    * Sets layout, styling, and event handlers for all UI elements. This constructor 
    * follows the singleton pattern and is only executed once.</p>
    */
    private ViewEditReply() {
        theRootPane = new Pane();
        theViewEditReplyScene = new Scene(theRootPane, width, height);
        
        // GUI Area 1: Header
        label_PageTitle.setText("Edit Reply");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
        
        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> {ControllerEditReply.performUpdate(); });
        
        // GUI Area 2: Edit Form
        setupLabelUI(label_OriginalPost, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 115);
        
        label_OriginalPostText.setFont(Font.font("Arial", 14));
        label_OriginalPostText.setLayoutX(20);
        label_OriginalPostText.setLayoutY(145);
        label_OriginalPostText.setPrefWidth(740);
        label_OriginalPostText.setWrapText(true);
        
        setupLabelUI(label_ReplyBody, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 185);
        
        text_ReplyBody.setFont(Font.font("Arial", 16));
        text_ReplyBody.setLayoutX(20);
        text_ReplyBody.setLayoutY(215);
        text_ReplyBody.setMinWidth(740);
        text_ReplyBody.setMaxWidth(740);
        text_ReplyBody.setPrefHeight(250);
        text_ReplyBody.setWrapText(true);
        text_ReplyBody.setPromptText("Enter reply body...");
        
        setupButtonUI(button_SaveChanges, "Dialog", 16, 180, Pos.CENTER, 20, 480);
        button_SaveChanges.setOnAction((_) -> {ControllerEditReply.performSaveChanges(); });
        
        setupButtonUI(button_Cancel, "Dialog", 16, 180, Pos.CENTER, 220, 480);
        button_Cancel.setOnAction((_) -> {ControllerEditReply.performCancel(); });
        
        // GUI Area 3: Navigation
        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerEditReply.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerEditReply.performQuit(); });

        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
            label_OriginalPost, label_OriginalPostText,
            label_ReplyBody, text_ReplyBody,
            button_SaveChanges, button_Cancel,
            line_Separator4, button_Logout, button_Quit);
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
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
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
    
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }

    /**********
    * Displays an informational alert dialog
    *
    * @param title   the title of the alert window
    * @param message the message displayed in the alert
    */ 
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
