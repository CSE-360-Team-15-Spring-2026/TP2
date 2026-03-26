package guiSearchPosts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import entityClasses.Post;
import guiRole1.ModelRole1Home;

import java.util.List;

/*******
 * <p> Title: ViewSearchPosts Class </p>
 *
 * <p> Description: The Java/FX page allows users to search for posts within discussion threads
 * by keyword and optionally by thread name. It serves as a central interface for discovering
 * and viewing posts relevant to their interests. </p>
 *
 */
public class ViewSearchPosts {


    // Attributes

    // These define the application window dimensions
    
    
    /** Application window width from the main application settings */
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    
    /** Application window height from the main application settings */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // The GUI components are organized by their functional area

    // GUI Area 1: Search input controls

    /** Label displaying the page title "Search Posts" */
    protected static Label label_PageTitle = new Label();
    
    /** Label for the keyword search input field */
    protected static Label label_Keyword = new Label("Keyword:");

    /** Text field for user to enter search keyword */
    protected static TextField textField_Keyword = new TextField();

    /** Label for the optional thread filter input */
    protected static Label label_Thread = new Label("Thread (optional):");

    /** Text field for user to optionally filter search by thread name; null or empty searches all threads */
    protected static TextField textField_Thread = new TextField();

    /** Button to initiate the search operation with the specified keyword and thread filter */
    protected static Button button_Search = new Button("Search");

    // GUI Area 2: Results display table
    
    /** Table view displaying search results with columns for post ID, title, author, thread, reply count, read status, and timestamp */
    protected static TableView<PostDisplay> table_Results = new TableView<>();

    /** Observable list backing the results table, automatically updates the display when modified */
    protected static ObservableList<PostDisplay> resultData = FXCollections.observableArrayList();

    // GUI Area 3: Action buttons
    
    /** Button to view the selected post and its replies in detail */
    protected static Button button_ViewPost = new Button("View Post & Replies");

    /** Button to return to the previous page (Role1Home or ViewPost depending on navigation context) */
    protected static Button button_Back = new Button("Back");

    // Page management

    /** Singleton instance of ViewSearchPosts for lazy initialization pattern */
    private static ViewSearchPosts theView;

    /** The JavaFX Stage (window) for displaying this search page GUI */
    protected static Stage theStage;

    /** Root pane containing all GUI components for layout management */
    protected static Pane theRootPane;

    /** The Scene for the search posts page containing the root pane */
    private static Scene theSearchScene = null;





    /**********
     * <p> Title: displaySearchPosts(Stage ps) Method. </p>
     *
     * <p> Description: Entry point to display the Search Posts page. Initializes the view if
     * needed and sets the scene on the stage for display to the user. </p>
     *
     * @param ps              The JavaFX Stage for this GUI
     *
     */
    public static void displaySearchPosts(Stage ps) {
        theStage = ps;

        if (theView == null) {
            theView = new ViewSearchPosts();
        }

        theStage.setTitle("CSE 360 Foundations: Search Posts");
        theStage.setScene(theSearchScene);
        theStage.show();
    }

    /**********
     * <p> Title: ViewSearchPosts() Constructor. </p>
     *
     * <p> Description: Initializes all GUI components, including layout, styling, and event handlers.
     * This constructor runs only once (singleton pattern), establishing the visual structure of
     * the search page. Dynamic data is updated separately when the page is displayed. </p>
     *
     */
    private ViewSearchPosts() {
        theRootPane = new Pane();
        theSearchScene = new Scene(theRootPane, width, height);

        // Page title
        label_PageTitle.setText("Search Posts");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        // Search section
        setupLabelUI(label_Keyword, "Arial", 14, 80, Pos.BASELINE_LEFT, 20, 60);
        setupTextFieldUI(textField_Keyword, "Arial", 14, 200, Pos.BASELINE_LEFT, 100, 55);

        setupLabelUI(label_Thread, "Arial", 14, 150, Pos.BASELINE_LEFT, 330, 60);
        setupTextFieldUI(textField_Thread, "Arial", 14, 200, Pos.BASELINE_LEFT, 480, 55);

        setupButtonUI(button_Search, "Dialog", 14, 100, Pos.CENTER, 700, 50);
        button_Search.setOnAction((_) -> {
            ControllerSearchPosts.performSearch();
        });

        // Results table
        setupTableView();

        // Action buttons
        setupButtonUI(button_ViewPost, "Dialog", 16, 180, Pos.CENTER, 20, 495);
        button_ViewPost.setOnAction((_) -> {
            ControllerSearchPosts.viewSelectedPost();
        });

        setupButtonUI(button_Back, "Dialog", 16, 180, Pos.CENTER, 220, 495);
        button_Back.setOnAction((_) -> {
            ControllerSearchPosts.goBack();
        });

        theRootPane.getChildren().addAll(
            label_PageTitle,
            label_Keyword, textField_Keyword,
            label_Thread, textField_Thread, button_Search,
            table_Results,
            button_ViewPost, button_Back
        );
    }


    //Helper methods for UI setup

    /**********
     * <p>
     *
     * Title: setupTableView() Method. </p>
     *
     * <p> Description: Private method that configures the results table with columns for
     * displaying post information. The table includes columns for ID, Title, Author, Thread,
     * Reply Count, Read Status, and Timestamp to provide comprehensive post information to the user. </p>
     *
     */
    private void setupTableView() {
        TableColumn<PostDisplay, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("postId"));
        idCol.setPrefWidth(50);

        TableColumn<PostDisplay, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(250);

        TableColumn<PostDisplay, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(100);

        TableColumn<PostDisplay, String> threadCol = new TableColumn<>("Thread");
        threadCol.setCellValueFactory(new PropertyValueFactory<>("thread"));
        threadCol.setPrefWidth(80);

        TableColumn<PostDisplay, Integer> repliesCol = new TableColumn<>("Replies");
        repliesCol.setCellValueFactory(new PropertyValueFactory<>("replyCount"));
        repliesCol.setPrefWidth(60);

        TableColumn<PostDisplay, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        TableColumn<PostDisplay, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        dateCol.setPrefWidth(140);

        table_Results.getColumns().addAll(
            idCol, titleCol, authorCol, threadCol, repliesCol, statusCol, dateCol
        );

        table_Results.setItems(resultData);
        table_Results.setLayoutX(20);
        table_Results.setLayoutY(120);
        table_Results.setPrefWidth(760);
        table_Results.setPrefHeight(350);
    }

    /**********
     * <p> Title: populateResultsTable Method. </p>
     * 
     * <p> Description: Protected method that populates the results table with the provided list
     * of posts, displaying only non-deleted posts with their metadata including title, author,
     * thread, reply count, and read status. </p>
     *
     * @param posts: The list of posts to display in the table
     *
     */
    public static void populateResultsTable(List<Post> posts) {
        resultData.clear();
        for (Post post : posts) {
            if (!post.isDeleted()) {
                resultData.add(new PostDisplay(post));
            }
        }
    }

    /**********
     * <p> Title: showAlert(String title, String message) Method. </p>
     *
     * <p> Description: Protected method that displays an information alert dialog with the
     * specified title and message to communicate important information to the user. </p>
     *
     * @param title           The alert dialog title
     * @param message         The alert message content
     *
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**********
     * <p> Title: setupLabelUI() Method. </p>
     *
     * <p> Description: Private local method to initialize the standard fields for a label. </p>
     *
     * @param l               The Label to configure
     * @param ff              The font family
     * @param f               The font size
     * @param w               The width of the label
     * @param p               The alignment (left, center, right)
     * @param x               The x-coordinate (horizontal position)
     * @param y               The y-coordinate (vertical position)
     */
    private static void setupLabelUI(Label l, String ff, double f, double w,
                                     Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**********
     * <p> Title: setupButtonUI() Method. </p>
     * 
     * <p> Description: Private local method to initialize the standard fields for a button. </p>
     *
     * @param b               The Button to configure
     * @param ff              The font family
     * @param f               The font size
     * @param w               The button width
     * @param p               The alignment (left, center, right)
     * @param x               The x-coordinate (horizontal position)
     * @param y               The y-coordinate (vertical position)
     */
    private static void setupButtonUI(Button b, String ff, double f, double w,
                                      Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**********
     * <p> Title: setupTextFieldUI() Method. </p>
     * <p> Description: Private local method to initialize the standard fields for a text field. </p>
     *
     * @param t               The TextField to configure
     * @param ff              The font family
     * @param f               The font size
     * @param w               The width of the text field
     * @param p               The alignment (left, center, right)
     * @param x               The x-coordinate (horizontal position)
     * @param y               The y-coordinate (vertical position)
     */
    private static void setupTextFieldUI(TextField t, String ff, double f,
                                         double w, Pos p, double x, double y) {
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);
    }

    /**********
     * <p> Title: PostDisplay Inner Class. </p>
     * 
     * <p> Description: A wrapper class that adapts Post objects for display in the results
     * TableView, using JavaFX properties for automatic table updates and refreshes. This class
     * provides a structured representation of post data suitable for TableView display. </p>
     *
     */
    public static class PostDisplay {
    	/** Unique ID of the post */
        private final javafx.beans.property.SimpleIntegerProperty postId;
        /** Title of the post */
        private final javafx.beans.property.SimpleStringProperty title;
        /** Username of the author */
        private final javafx.beans.property.SimpleStringProperty author;
        /** Thread that the post is a part of */
        private final javafx.beans.property.SimpleStringProperty thread;
        /** Number of replies on the post */
        private final javafx.beans.property.SimpleIntegerProperty replyCount;
        /** Status of the post */
        private final javafx.beans.property.SimpleStringProperty status;
        /** Date and time the post was posted */
        private final javafx.beans.property.SimpleStringProperty timestamp;

        /**
         * Constructs a PostDisplay from a Post object, extracting all relevant fields for table
         * display including read status and reply count from the associated post.
         *
         * @param post            The Post object to wrap for display
         */
        public PostDisplay(Post post) {
            this.postId = new javafx.beans.property.SimpleIntegerProperty(post.getPostID());
            this.title = new javafx.beans.property.SimpleStringProperty(post.getTitle());
            this.author = new javafx.beans.property.SimpleStringProperty(post.getUsername());
            this.thread = new javafx.beans.property.SimpleStringProperty(post.getThreadName());
            this.replyCount = new javafx.beans.property.SimpleIntegerProperty(
                ModelRole1Home.getReplyCount(post.getPostID())
            );
            this.status = new javafx.beans.property.SimpleStringProperty(
                ModelRole1Home.isRead(post.getPostID()) ? "READ" : "UNREAD"
            );
            this.timestamp = new javafx.beans.property.SimpleStringProperty(
                ModelRole1Home.getFormattedTimestamp(post)
            );
        }

        /** 
         * <p> Gets the unique id of the post </p>
         * @return postId
         */
        public int getPostId() { return postId.get(); }
        /**
         * <p> Gets the title of the post </p>
         * @return title of the post
         */
        public String getTitle() { return title.get(); }
        /**
         * <p> Gets the username of the author </p>
         * @return username of the author
         */
        public String getAuthor() { return author.get(); }
        /**
         * <p> Gets the thread </p>
         * @return thread name
         */
        public String getThread() { return thread.get(); }
        /**
         * <p> Gets the number of replies on the post </p>
         * @return number of replies
         */
        public int getReplyCount() { return replyCount.get(); }
        /**
         * <p> Gets the status of the post relative to the user </p>
         * @return status: read or unread
         */
        public String getStatus() { return status.get(); }
        /**
         * <p> Gets the date and time of posting </p>
         * @return date and time when the post was posted
         */
        public String getTimestamp() { return timestamp.get(); }
    }
}
