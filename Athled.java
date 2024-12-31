import javax.swing.*;
import java.awt.GridLayout;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

class Player implements Serializable {
    String name;
    String team;
    String password;

    public Player(String name, String team, String password) {
        this.name = name;
        this.team = team;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", team: " + team;
    }
}

class Match implements Serializable {
    String team1;
    String team2;
    String dateTime;
    int timeSlot;
    int groundNumber;

    public Match(String team1, String team2, String dateTime, int timeSlot, int groundNumber) {
        this.team1 = team1;
        this.team2 = team2;
        this.dateTime = dateTime;
        this.timeSlot = timeSlot;
        this.groundNumber = groundNumber;
    }

    @Override
    public String toString() {
        return team1 + " vs " + team2 + " at " + dateTime + " (Time Slot: " + timeSlot + ", Ground: " + groundNumber + ")";
    }
}

public class TournamentManagementGUI {

    private static List<Player> players = new ArrayList<>();
    private static List<Match> matches = new ArrayList<>();
    private static List<String> bookedTimeslots = new ArrayList<>();
    private static final String PLAYER_FILE = "players.txt";
    private static final String MATCH_FILE = "matches.txt";
    private static final int TOTAL_GROUNDS = 3; // Number of grounds available

    public static void main(String[] args) {
        loadPlayers();
        loadMatches();
        SwingUtilities.invokeLater(TournamentManagementGUI::createMainMenu);
    }

    private static void savePlayers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE))) {
            for (Player player : players) {
                writer.write(player.name + "," + player.team + "," + player.password);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving players: " + e.getMessage());
        }
    }

    private static void loadPlayers() {
        players.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String team = parts[1];
                    String password = parts[2];
                    players.add(new Player(name, team, password));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Player file not found. Starting with empty list.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading players: " + e.getMessage());
        }
    }

    private static void saveMatches() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MATCH_FILE))) {
            for (Match match : matches) {
                writer.write(match.team1 + "," + match.team2 + "," + match.dateTime + "," + match.timeSlot + "," + match.groundNumber);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving matches: " + e.getMessage());
        }
    }

    private static void loadMatches() {
        matches.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(MATCH_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String team1 = parts[0];
                    String team2 = parts[1];
                    String dateTime = parts[2];
                    int timeSlot = Integer.parseInt(parts[3]);
                    int groundNumber = Integer.parseInt(parts[4]);
                    matches.add(new Match(team1, team2, dateTime, timeSlot, groundNumber));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Match file not found. Starting with empty list.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading matches: " + e.getMessage());
        }
    }

    private static void createMainMenu() {
        JFrame frame = new JFrame("Tournament Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton playerLoginButton = new JButton("Player Login");
        JButton ownerLoginButton = new JButton("Tournament Owner");
        JButton adminLoginButton = new JButton("Admin");
        JButton exitButton = new JButton("Exit");

        playerLoginButton.addActionListener(e -> {
            frame.dispose();
            createPlayerMenu();
        });

        ownerLoginButton.addActionListener(e -> {
            frame.dispose();
            createOwnerMenu();
        });

        adminLoginButton.addActionListener(e -> {
            frame.dispose();
            createAdminMenu();
        });

        exitButton.addActionListener(e -> {
            savePlayers();
            saveMatches();
            System.exit(0);
        });

        panel.add(playerLoginButton);
        panel.add(ownerLoginButton);
        panel.add(adminLoginButton);
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createPlayerMenu() {
        JFrame frame = new JFrame("Player Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        registerButton.addActionListener(e -> {
            frame.dispose();
            createRegisterForm();
        });

        loginButton.addActionListener(e -> {
            frame.dispose();
            createLoginForm();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createMainMenu();
        });

        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createRegisterForm() {
        JFrame frame = new JFrame("Player Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel teamLabel = new JLabel("Team:");
        JTextField teamField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton submitButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String team = teamField.getText();
            String password = new String(passwordField.getPassword());

            players.add(new Player(name, team, password));
            savePlayers();
            JOptionPane.showMessageDialog(frame, "Player registered successfully!");
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createPlayerMenu();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(teamLabel);
        panel.add(teamField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createOwnerMenu() {
        JFrame frame = new JFrame("Owner Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton createMatchButton = new JButton("Create Matches");
        JButton backButton = new JButton("Back");

        createMatchButton.addActionListener(e -> {
            frame.dispose();
            createRoundRobinSchedule();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createMainMenu();
        });

        panel.add(createMatchButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createRoundRobinSchedule() {
        if (players.size() < 2) {
            JOptionPane.showMessageDialog(null, "Not enough players to create matches.");
            return;
        }

        // Generate matches using round-robin scheduling
        List<String> teams = new ArrayList<>();
        for (Player player : players) {
            if (!teams.contains(player.team)) {
                teams.add(player.team);
            }
        }

        // Create matches
        int numTeams = teams.size();
        int numRounds = numTeams - 1;
        int numMatchesPerRound = numTeams / 2;

        for (int round = 0; round < numRounds; round++) {
            for (int match = 0; match < numMatchesPerRound; match++) {
                int team1Index = (round + match) % (numTeams - 1);
                int team2Index = (numTeams - 1 - match + round) % (numTeams - 1);

                if (match == 0) {
                    team2Index = numTeams - 1;
                }

                String team1 = teams.get(team1Index);
                String team2 = teams.get(team2Index);

                scheduleMatch(team1, team2, round, match);
            }
        }

        saveMatches();
        JOptionPane.showMessageDialog(null, "Matches created successfully!");
    }

    private static void scheduleMatch(String team1, String team2, int round, int match) {
        String date = "2025-10-" + (round + 1); // Example date, you can modify this
        String time = String.format("%02d:00", 18 + (match % 6)); // Example time, you can modify this
        String dateTimeStr = date + " " + time;

        int timeSlot = calculateTimeSlot(time);
        int groundNumber = (match % TOTAL_GROUNDS) + 1; // Cycle through grounds

        // Check for conflicts
        if (!isTimeSlotBooked(dateTimeStr, timeSlot)) {
            matches.add(new Match(team1, team2, dateTimeStr, timeSlot, groundNumber));
            bookedTimeslots.add(dateTimeStr);
        }
    }

    private static boolean isTimeSlotBooked(String dateTimeStr, int timeSlot) {
        for (String bookedDateTime : bookedTimeslots) {
            int bookedTimeSlot = calculateTimeSlot(bookedDateTime.split(" ")[1]);
            if (bookedDateTime.equals(dateTimeStr) && bookedTimeSlot == timeSlot) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidDateTimeFormat(String dateTimeStr) {
        String[] parts = dateTimeStr.split(" ");
        if (parts.length != 2) {
            return false;
        }
        try {
            String[] dateParts = parts[0].split("-");
            if (dateParts.length != 3) {
                return false;
            }
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            String[] timeParts = parts[1].split(":");
            if (timeParts.length != 2) {
                return false;
            }
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            return month >= 1 && month <= 12 && day >= 1 && day <= 31 && year >= 2023 && hour >= 18 && hour < 24 && minute >= 0 && minute < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int calculateTimeSlot(String timeStr) {
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int timeSlot = ((hour - 18) * 60 + minute) / 30 + 1;
        return timeSlot;
    }

    private static void createAdminMenu() {
        JFrame frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel passwordLabel = new JLabel("Admin Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (password.equals("admin123")) {
                frame.dispose();
                createAdminDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid admin password. Try again.");
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createMainMenu();
        });

        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createAdminDashboard() {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton viewMatchesButton = new JButton("View All Matches");
        JButton backButton = new JButton("Back");

        viewMatchesButton.addActionListener(e -> {
            StringBuilder matchDetails = new StringBuilder();
            for (Match match : matches) {
                matchDetails.append(match.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, matchDetails.toString(), "All Matches", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createMainMenu();
        });

        panel.add(viewMatchesButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createLoginForm() {
        JFrame frame = new JFrame("Login Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel teamLabel = new JLabel("Team:");
        JTextField teamField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.addActionListener(e -> {
            String team = teamField.getText();
            String password = new String(passwordField.getPassword());

            boolean isAuthenticated = false;
            Player loggedInPlayer = null;
            for (Player player : players) {
                if (player.team.equals(team) && player.password.equals(password)) {
                    isAuthenticated = true;
                    loggedInPlayer = player;
                    break;
                }
            }

            if (isAuthenticated) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                createPlayerDashboard(loggedInPlayer);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again.");
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createPlayerMenu();
        });

        panel.add(teamLabel);
        panel.add(teamField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createPlayerDashboard(Player player) {
        JFrame frame = new JFrame("Player Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + player.name + "!");
        JButton viewMatchesButton = new JButton("View Match Schedule");
        JButton backButton = new JButton("Back");

        viewMatchesButton.addActionListener(e -> {
            StringBuilder matchDetails = new StringBuilder();
            for (Match match : matches) {
                // Check if the match involves the player's team
                if (match.team1.equals(player.team) || match.team2.equals(player.team)) {
                    matchDetails.append(match.toString()).append("\n");
                }
            }
            if (matchDetails.length() == 0) {
                matchDetails.append("No matches found for your team.");
            }
            JOptionPane.showMessageDialog(frame, matchDetails.toString(), "Match Schedule", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            createPlayerMenu();
        });

        panel.add(welcomeLabel);
        panel.add(viewMatchesButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}