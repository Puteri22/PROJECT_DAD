import java.io.*;
import java.net.*;
import java.sql.*;

public class MovieCatalogServer {

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
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request.startsWith("GET_MOVIES")) {
                    out.println(getMovies());
                } else if (request.startsWith("DELETE_MOVIE")) {
                    String movieName = request.substring("DELETE_MOVIE ".length());
                    out.println(deleteMovie(movieName));
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getMovies() {
            StringBuilder movies = new StringBuilder();
            try {
                String sql = "SELECT movieName FROM movies";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString("movieName");
                    movies.append(name).append("\n");
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return movies.toString();
        }

        private String deleteMovie(String movieName) {
            try {
                String sql = "DELETE FROM movies WHERE movieName = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, movieName);

                int rowsDeleted = statement.executeUpdate();
                statement.close();
                if (rowsDeleted > 0) {
                    return "Movie deleted successfully.";
                } else {
                    return "Failed to delete the movie.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error occurred while deleting the movie.";
            }
        }
    }
}