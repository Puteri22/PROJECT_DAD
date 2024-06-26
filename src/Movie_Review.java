import java.awt.EventQueue;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Movie_Review {

    private JFrame frame;
    private JList<String> listMovie;
    private DefaultListModel<String> listModel;
    private Connection connection;
    private JTextArea txtAreaListReview; 
    private JTextField txtFieldMovieName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Movie_Review window = new Movie_Review();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Start the server in a new thread
        new Thread(() -> {
            try {
                MovieReviewServer.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Create the application.
     */
    public Movie_Review() {
        initialize();
        connectToDatabase();
        populateMovieList();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 632, 591);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("MOVIE REVIEW");
        lblNewLabel.setBounds(246, 24, 107, 36);
        frame.getContentPane().add(lblNewLabel);

        // Initialize the list model
        listModel = new DefaultListModel<>();

        listMovie = new JList<>(listModel);
        listMovie.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listMovie.setBounds(152, 78, 244, 116);
        frame.getContentPane().add(listMovie);

        JScrollPane scrollPane = new JScrollPane(listMovie);
        scrollPane.setBounds(149, 92, 283, 149);
        frame.getContentPane().add(scrollPane);

        JLabel lblNewLabel_1 = new JLabel("Please choose one movie:");
        lblNewLabel_1.setBounds(149, 70, 181, 13);
        frame.getContentPane().add(lblNewLabel_1);

        txtAreaListReview = new JTextArea(); // Initialize txtAreaListReview
        txtAreaListReview.setBounds(149, 369, 283, 159);
        frame.getContentPane().add(txtAreaListReview); // Add txtAreaListReview to the frame

        JButton btnViewReview = new JButton("View Review");
        btnViewReview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSelectedMovieName();
                displayReviews();
            }
        });
        btnViewReview.setBounds(210, 251, 143, 21);
        frame.getContentPane().add(btnViewReview);

        txtFieldMovieName = new JTextField();
        txtFieldMovieName.setBounds(149, 332, 204, 19);
        frame.getContentPane().add(txtFieldMovieName);
        txtFieldMovieName.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Movie Name:");
        lblNewLabel_2.setBounds(150, 309, 120, 13);
        frame.getContentPane().add(lblNewLabel_2);
    }

    private void connectToDatabase() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost:3306/movie_application";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Database connected successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateMovieList() {
        try {
            String sql = "SELECT movieName FROM movies";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            listModel.clear();
            while (resultSet.next()) {
                String name = resultSet.getString("movieName");
                listModel.addElement(name);
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayReviews() {
        String selectedMovie = listMovie.getSelectedValue();
        if (selectedMovie == null) {
            txtAreaListReview.setText("Please select a movie.");
            return;
        }

        try (Socket socket = new Socket("localhost", 12345); // Connect to the server on the same device
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(selectedMovie);

            StringBuilder reviews = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                reviews.append(line).append("\n");
            }

            txtAreaListReview.setText(reviews.toString());

        } catch (IOException e) {
            e.printStackTrace();
            txtAreaListReview.setText("Error retrieving reviews from the server.");
        }
    }

    // Method to update txtFieldMovieName with the selected movie
    private void updateSelectedMovieName() {
        String selectedMovie = listMovie.getSelectedValue();
        if (selectedMovie != null) {
            txtFieldMovieName.setText(selectedMovie);
        }
    }
}