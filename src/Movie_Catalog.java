import java.awt.EventQueue;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Movie_Catalog {

    private JFrame frame;
    private JList<String> listMovie;
    private DefaultListModel<String> listModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Start the server in a new thread
                    new Thread(() -> {
                        try {
                            MovieCatalogServer.main(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    
                    // Adding delay to ensure server is up
                    Thread.sleep(2000); 

                    Movie_Catalog window = new Movie_Catalog();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Movie_Catalog() {
        initialize();
        populateMovieList();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 540, 317);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("MOVIE CATALOG");
        lblNewLabel.setBounds(218, 10, 148, 37);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Movie List");
        lblNewLabel_1.setBounds(238, 47, 74, 21);
        frame.getContentPane().add(lblNewLabel_1);

        JButton btnDeleteMovie = new JButton("Delete Movie");
        btnDeleteMovie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMovie();
            }
        });
        btnDeleteMovie.setBounds(210, 233, 121, 21);
        frame.getContentPane().add(btnDeleteMovie);

        // Initialize the list model
        listModel = new DefaultListModel<>();

        listMovie = new JList<>(listModel);
        listMovie.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listMovie.setBounds(152, 78, 244, 116);
        frame.getContentPane().add(listMovie);

        JScrollPane scrollPane = new JScrollPane(listMovie);
        scrollPane.setBounds(145, 78, 251, 116);
        frame.getContentPane().add(scrollPane);
    }

    private void populateMovieList() {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET_MOVIES");

            listModel.clear();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                listModel.addElement(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving movies from the server.");
        }
    }

    private void deleteMovie() {
        String selectedMovie = listMovie.getSelectedValue();
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(frame, "Please select a movie to delete.");
            return;
        }

        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("DELETE_MOVIE " + selectedMovie);

            String response = in.readLine();
            JOptionPane.showMessageDialog(frame, response);

            if (response.equals("Movie deleted successfully.")) {
                populateMovieList();
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting the movie.");
        }
    }
}