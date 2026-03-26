package guiCreatePost;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;


/*******
 * <p> Title: GUIcreatepost Class. </p>
 * 
 * <p> Description: The Java/FX page allows users to view posts and create new posts or replies 
 * within discussion threads.It serves as a central interface for browsing and contributing to this GUI</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-20 Initial version
 *  
 */

public class ViewCreatePost {
	
	/*-******************************************************************************************
	Attributes
	 */
	// These define the application window dimensions
	
	/** Width of the GUI page */
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	/** Height of the GUI page */
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// The GUI components are organized into 3 main sections
	
	// GUI Area 1: Displays page title, current user info, and account update option
	/** Title of the page */
	protected static Label label_PageTitle = new Label();
	/** Displays the current logged-in user */
	protected static Label label_UserDetails = new Label();
	/** Button to open the Account Update page */
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	/** Separator line between sections */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: Allows the user to create a new post
	/** Label for post title input */
	protected static Label label_PostTitle = new Label("Post Title");
	/** Text field for entering the post title */
	protected static TextField text_PostTitle = new TextField();
	
	/** Label for post body input */
	protected static Label label_PostBody = new Label("Post Body");
	/** Text area for entering the post content */
	protected static TextArea text_PostBody = new TextArea();
	
	/** Label for selecting thread */
	protected static Label label_ThreadName = new Label("Thread");
	/** Dropdown for selecting thread name */
	protected static ComboBox<String> comboBox_ThreadName = new ComboBox<String>();
	
	/** Button to submit and create a post */
	protected static Button button_CreatePost = new Button("Create Post");
	/** Button to cancel and return to previous page */
	protected static Button button_Cancel = new Button("Cancel");
	
	
	/** Separator line between the footer section */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: Provides logout and application exit options
	/** Button to submit and create a post */
	protected static Button button_Logout = new Button("Logout");
	/** Button to cancel and return to previous page */
	protected static Button button_Quit = new Button("Quit");
	
	/** Singleton instance of ViewCreatePost */
	private static ViewCreatePost theView;		
	/** Connection to the database */
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	/** JavaFX Stage used to display this page */
	protected static Stage theStage;			
	/** Root pane containing all UI elements */
	protected static Pane theRootPane;			
	/** Currently logged-in user */
	protected static User theUser;				
	
	/** Scene used for this page */
	private static Scene theViewCreatePostScene;	
	/** Role identifier (Role1 = Student) */
	protected static final int theRole = 2;		

	/*-*******************************************************************************************

	Constructors
	
	 */


	/**********
 * <p> Method: displayCreatePost(Stage ps, User user) </p>
 * 
 * <p> Description: Entry point to display the Create Post page. Initializes the view if needed,
 * updates user-specific data, and sets the scene on the stage.</p>
 * 
 * @param ps the JavaFX Stage for this GUI
 * @param user the current logged-in User
 * 
 */
	public static void displayCreatePost(Stage ps, User user) {
		
		// Set the current stage and user
		theStage = ps;
		theUser = user;
		
		// Initialize the view only once 
		if (theView == null) theView = new ViewCreatePost();		// Instantiate singleton if needed
		
		// Load user data and update application status
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;

		// Display current user info on the page
		label_UserDetails.setText("User: " + theUser.getUserName());
		
		// Reset input fields for creating a new post
		text_PostTitle.setText("");
		text_PostBody.setText("");
		loadThreads();
				
		// Set up and display the scene
		theStage.setTitle("Student Home Page");
		theStage.setScene(theViewCreatePostScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: ViewCreatePost() </p>
	 * 
	 * <p> Description: Initializes all GUI components, including layout, styling, and event handlers.
     * This constructor runs only once (singleton pattern), while dynamic data is updated separately
     * when the page is displayed.</p>
	 * 
	 */
	private ViewCreatePost() {

		// Initialize the main layout container and sceneInitialize the main layout container and scene
		theRootPane = new Pane();
		theViewCreatePostScene = new Scene(theRootPane, width, height);	// Create the scene
	
		
		// GUI Area 1:Page title and user info section
		label_PageTitle.setText("Student Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerCreatePost.performUpdate(); });
		
		// GUI Area 2: Post creation form
		setupLabelUI(label_PostTitle, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 120);
		setupTextFieldUI(text_PostTitle, "Arial", 16, 500, Pos.BASELINE_LEFT, 20, 150);
		
		setupLabelUI(label_ThreadName, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 195);
		setupComboBoxUI(comboBox_ThreadName, "Arial", 16, 250, 20, 225);
		
		setupLabelUI(label_PostBody, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 270);
		setupTextAreaUI(text_PostBody, "Arial", 16, 740, 180, 20, 300);
		
		setupButtonUI(button_CreatePost, "Dialog", 18, 170, Pos.CENTER, 20, 490);
		button_CreatePost.setOnAction((_) -> {ControllerCreatePost.performCreatePost(); });
		
		setupButtonUI(button_Cancel, "Dialog", 18, 170, Pos.CENTER, 210, 490);
		button_Cancel.setOnAction((_) -> {ControllerCreatePost.performCancel(); });
		
		
		// GUI Area 3: Logout and exit controls
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerCreatePost.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerCreatePost.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Add all UI elements to the root pane
         theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_PostTitle, text_PostTitle,
			label_ThreadName, comboBox_ThreadName,
			label_PostBody, text_PostBody,
			button_CreatePost, button_Cancel,
	        line_Separator4, button_Logout, button_Quit);
}
	
	
	/*-********************************************************************************************

	Helper methods for UI setup

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l   the Label to configure
	 * @param ff  the font family
	 * @param f   the font size
	 * @param w   the width of the label
	 * @param p   the alignment (left, center, right)
	 * @param x   the x-coordinate (horizontal position)
	 * @param y   the y-coordinate (vertical position)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
			double y){
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
    * @param w   the button width
    * @param p   the alignment (left, center, right)
    * @param x   the x-coordinate (horizontal position)
    * @param y   the y-coordinate (vertical position)
    */
	
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);	
	}
		
	/**********
    * Private local method to initialize the standard fields for a text field
	*
    * @param t   the TextField to configure
    * @param ff  the font family
    * @param f   the font size
    * @param w   the width of the text field
    * @param p   the alignment
    * @param x   the x-coordinate (horizontal position)
    * @param y   the y-coordinate (vertical position)
    */
	
	private static void setupTextFieldUI(TextField t, String ff, double f, double w, Pos p, double x,
			double y){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
	}
	
	/**********
    * Private local method to initialize the standard fields for a text area
    *
    * @param t   the TextArea to configure
    * @param ff  the font family
    * @param f   the font size
    * @param w   the width of the text area
    * @param h   the height of the text area
    * @param x   the x-coordinate (horizontal position)
    * @param y   the y-coordinate (vertical position)
    */
	private static void setupTextAreaUI(TextArea t, String ff, double f, double w, double h, double x,
			double y){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setMinHeight(h);
		t.setMaxHeight(h);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setWrapText(true);
	}
	
	/**********
 	* Private local method to initialize the standard fields for a combo box
 	*
 	* @param c   the ComboBox to configure
 	* @param ff  the font family
 	* @param f   the font size
 	* @param w   the width of the combo box
 	* @param x   the x-coordinate (horizontal position)
 	* @param y   the y-coordinate (vertical position)
 	*/
	private static void setupComboBoxUI(ComboBox<String> c, String ff, double f, double w, double x,
			double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
	
	/**********
	 * Loads the available thread names into the GUI.
	 */
	protected static void loadThreads() {
		comboBox_ThreadName.getItems().clear();
		
		try {
			java.util.List<String> threads = applicationMain.FoundationsMain.database.getAllThreads();
			if (threads != null && !threads.isEmpty()) {
				comboBox_ThreadName.getItems().addAll(threads);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (comboBox_ThreadName.getItems().isEmpty()) {
			comboBox_ThreadName.getItems().add("General");
		}
		
		comboBox_ThreadName.getSelectionModel().selectFirst();
	}
	
	/**********
	 * Displays an alert dialog to the user.
	 * @param title   the title of the alert dialog
 	 * @param message the message content displayed in the alert
	 */
	protected static void showAlert(String title, String message) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
				javafx.scene.control.Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
