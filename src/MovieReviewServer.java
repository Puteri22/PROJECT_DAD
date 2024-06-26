import java.io.*;
import java.net.*;
import java.sql.*;

public class MovieReviewServer {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Server listens on port 12345
            System.out.println("Server started and waiting for clients...");
            connectToDatabase();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectToDatabase() {
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

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String movieName = in.readLine();
                System.out.println("Received request for movie: " + movieName);

                String response = getReviewsForMovie(movieName);
                out.println(response);

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getReviewsForMovie(String movieName) {
            StringBuilder reviews = new StringBuilder();
            try {
                String sql = "SELECT reviews.reviewerName, reviews.reviewText " +
                             "FROM reviews " +
                             "JOIN movies ON reviews.movieID = movies.movieID " +
                             "WHERE movies.movieName = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, movieName);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String reviewerName = resultSet.getString("reviewerName");
                    String reviewText = resultSet.getString("reviewText");
                    reviews.append("Name: ").append(reviewerName).append("\n");
                    reviews.append("Review: ").append(reviewText).append("\n\n");
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return reviews.toString();
        }
    }
}