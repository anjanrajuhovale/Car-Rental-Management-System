import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

// Car class
class Car {
    private int id;
    private String model;
    private String brand;
    private boolean available;

    public Car(int id, String model, String brand) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.available = true;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean status) {
        this.available = status;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s | Status: %s",
                id, brand, model, available ? "Available" : "Rented");
    }
}

// Customer class
class Customer {
    private int id;
    private String name;
    private String licenseNumber;

    public Customer(int id, String name, String licenseNumber) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | License: %s", id, name, licenseNumber);
    }
}

// Rental class
class Rental {
    private Customer customer;
    private Car car;
    private String rentalDate;
    private String returnDate;

    public Rental(Customer customer, Car car, String rentalDate) {
        this.customer = customer;
        this.car = car;
        this.rentalDate = rentalDate;
        this.returnDate = null;
    }

    // Getters and setters
    public Customer getCustomer() {
        return customer;
    }

    public Car getCar() {
        return car;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}

// Main Car Rental System class
class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Car> getAvailableCars() {
        List<Car> available = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                available.add(car);
            }
        }
        return available;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Car> getAllCars() {
        return cars;
    }

    // Enhanced method with duplicate ID checking
    public boolean addCarWithValidation(Car car) {
        for (Car existingCar : cars) {
            if (existingCar.getId() == car.getId()) {
                return false; // Duplicate ID found
            }
        }
        cars.add(car);
        return true;
    }

    // Enhanced method with duplicate ID checking
    public boolean addCustomerWithValidation(Customer customer) {
        for (Customer existingCustomer : customers) {
            if (existingCustomer.getId() == customer.getId()) {
                return false; // Duplicate ID found
            }
        }
        customers.add(customer);
        return true;
    }

    public boolean rentCar(int carId, int customerId) {
        Car carToRent = null;
        Customer customerRenting = null;

        // Find car
        for (Car car : cars) {
            if (car.getId() == carId && car.isAvailable()) {
                carToRent = car;
                break;
            }
        }

        // Find customer
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                customerRenting = customer;
                break;
            }
        }

        if (carToRent != null && customerRenting != null) {
            carToRent.setAvailable(false);
            rentals.add(new Rental(customerRenting, carToRent, new Date().toString()));
            return true;
        }
        return false;
    }

    public boolean returnCar(int carId) {
        for (Car car : cars) {
            if (car.getId() == carId && !car.isAvailable()) {
                car.setAvailable(true);
                // Update rental record
                for (Rental rental : rentals) {
                    if (rental.getCar().getId() == carId && rental.getReturnDate() == null) {
                        rental.setReturnDate(new Date().toString());
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }
}

// Main GUI class with clean, simple design
public class CarRentalGUI extends JFrame {
    private CarRentalSystem rentalSystem;
    private JTextArea outputArea;
    private Font defaultFont = new Font("Arial", Font.PLAIN, 14);
    private Font titleFont = new Font("Arial", Font.BOLD, 16);

    public CarRentalGUI() {
        rentalSystem = new CarRentalSystem();
        initializeGUI();
        addSampleData();
    }

    private void initializeGUI() {
        setTitle("Car Rental Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = createTitlePanel();

        // Create button panel with clean design
        JPanel buttonPanel = createButtonPanel();

        // Create output area with clean styling
        JPanel outputPanel = createOutputPanel();

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(outputPanel, BorderLayout.CENTER);

        add(mainPanel);

        outputArea.setText("Welcome to Car Rental Management System\n" +
                "═══════════════════════════════════════\n\n" +
                "• Select an operation from the menu on the left\n" +
                "• Sample data has been loaded for testing\n" +
                "• All operations will display results here\n\n");
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Car Rental Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));

        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Operations",
                        0, 0, titleFont),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        buttonPanel.setPreferredSize(new Dimension(200, 0));

        // Create clean, simple buttons
        JButton addCarBtn = createCleanButton("Add New Car");
        JButton addCustomerBtn = createCleanButton("Add Customer");
        JButton rentCarBtn = createCleanButton("Rent Car");
        JButton returnCarBtn = createCleanButton("Return Car");

        buttonPanel.add(createSectionLabel("Vehicle Management"));
        buttonPanel.add(addCarBtn);
        buttonPanel.add(Box.createVerticalStrut(5));

        buttonPanel.add(createSectionLabel("Customer Management"));
        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(Box.createVerticalStrut(5));

        buttonPanel.add(createSectionLabel("Rental Operations"));
        buttonPanel.add(rentCarBtn);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(returnCarBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.add(createSectionLabel("Reports"));
        JButton showCustomersBtn = createCleanButton("View All Customers");
        JButton showAvailableCarsBtn = createCleanButton("View Available Cars");
        JButton showAllCarsBtn = createCleanButton("View All Cars");

        buttonPanel.add(showCustomersBtn);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(showAvailableCarsBtn);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(showAllCarsBtn);

        buttonPanel.add(Box.createVerticalGlue());

        JButton exitBtn = createCleanButton("Exit System");
        exitBtn.setForeground(new Color(139, 0, 0));
        buttonPanel.add(exitBtn);

        // Add action listeners
        addCarBtn.addActionListener(e -> addCar());
        rentCarBtn.addActionListener(e -> rentCar());
        returnCarBtn.addActionListener(e -> returnCar());
        addCustomerBtn.addActionListener(e -> addCustomer());
        showCustomersBtn.addActionListener(e -> showCustomers());
        showAvailableCarsBtn.addActionListener(e -> showAvailableCars());
        showAllCarsBtn.addActionListener(e -> showAllCars());
        exitBtn.addActionListener(e -> exitApplication());

        return buttonPanel;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(70, 70, 70));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return label;
    }

    private JButton createCleanButton(String text) {
        JButton button = new JButton(text);
        button.setFont(defaultFont);
        button.setPreferredSize(new Dimension(180, 35));
        button.setMaximumSize(new Dimension(180, 35));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(51, 51, 51));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "System Output",
                0, 0, titleFont));

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(250, 250, 250));
        outputArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        outputPanel.add(scrollPane, BorderLayout.CENTER);
        return outputPanel;
    }

    private void addSampleData() {
        // Add sample cars
        rentalSystem.addCar(new Car(1, "Camry", "Toyota"));
        rentalSystem.addCar(new Car(2, "Civic", "Honda"));
        rentalSystem.addCar(new Car(3, "Model 3", "Tesla"));
        rentalSystem.addCar(new Car(4, "Accord", "Honda"));
        rentalSystem.addCar(new Car(5, "Corolla", "Toyota"));

        // Add sample customers
        rentalSystem.addCustomer(new Customer(1, "John Doe", "DL123456"));
        rentalSystem.addCustomer(new Customer(2, "Jane Smith", "DL789012"));
        rentalSystem.addCustomer(new Customer(3, "Mike Johnson", "DL345678"));
    }

    private void addCar() {
        try {
            String idStr = showInputDialog("Add New Car", "Enter Car ID:");
            if (idStr == null || idStr.trim().isEmpty())
                return;

            String brand = showInputDialog("Add New Car", "Enter Car Brand:");
            if (brand == null || brand.trim().isEmpty())
                return;

            String model = showInputDialog("Add New Car", "Enter Car Model:");
            if (model == null || model.trim().isEmpty())
                return;

            int id = Integer.parseInt(idStr.trim());

            // Check for duplicate ID
            boolean added = rentalSystem.addCarWithValidation(new Car(id, model.trim(), brand.trim()));

            if (added) {
                displayOutput("✓ CAR ADDED SUCCESSFULLY\n" +
                        "═══════════════════════\n" +
                        "Car ID: " + id + "\n" +
                        "Brand: " + brand.trim() + "\n" +
                        "Model: " + model.trim() + "\n" +
                        "Status: Available\n\n");
            } else {
                displayOutput("✗ CAR ADDITION FAILED\n" +
                        "════════════════════\n" +
                        "Error: Car ID " + id + " already exists!\n" +
                        "Please use a different ID.\n\n");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid car ID (number)!");
        }
    }

    private void rentCar() {
        try {
            String carIdStr = showInputDialog("Rent Car", "Enter Car ID to rent:");
            if (carIdStr == null || carIdStr.trim().isEmpty())
                return;

            String custIdStr = showInputDialog("Rent Car", "Enter Customer ID:");
            if (custIdStr == null || custIdStr.trim().isEmpty())
                return;

            int carId = Integer.parseInt(carIdStr.trim());
            int custId = Integer.parseInt(custIdStr.trim());

            boolean success = rentalSystem.rentCar(carId, custId);
            if (success) {
                displayOutput("✓ CAR RENTAL SUCCESSFUL\n" +
                        "═══════════════════════\n" +
                        "Car ID: " + carId + "\n" +
                        "Customer ID: " + custId + "\n" +
                        "Rental Date: " + new Date() + "\n\n");
            } else {
                displayOutput("✗ RENTAL FAILED\n" +
                        "═══════════════\n" +
                        "Possible reasons:\n" +
                        "• Car is not available\n" +
                        "• Car ID doesn't exist\n" +
                        "• Customer ID doesn't exist\n\n");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter valid IDs (numbers)!");
        }
    }

    private void returnCar() {
        try {
            String carIdStr = showInputDialog("Return Car", "Enter Car ID to return:");
            if (carIdStr == null || carIdStr.trim().isEmpty())
                return;

            int carId = Integer.parseInt(carIdStr.trim());
            boolean success = rentalSystem.returnCar(carId);

            if (success) {
                displayOutput("✓ CAR RETURN SUCCESSFUL\n" +
                        "══════════════════════\n" +
                        "Car ID: " + carId + "\n" +
                        "Return Date: " + new Date() + "\n" +
                        "Status: Available for rent\n\n");
            } else {
                displayOutput("✗ RETURN FAILED\n" +
                        "═══════════════\n" +
                        "Possible reasons:\n" +
                        "• Car is not currently rented\n" +
                        "• Car ID doesn't exist\n\n");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid car ID (number)!");
        }
    }

    private void addCustomer() {
        try {
            String idStr = showInputDialog("Add Customer", "Enter Customer ID:");
            if (idStr == null || idStr.trim().isEmpty())
                return;

            String name = showInputDialog("Add Customer", "Enter Customer Name:");
            if (name == null || name.trim().isEmpty())
                return;

            String license = showInputDialog("Add Customer", "Enter License Number:");
            if (license == null || license.trim().isEmpty())
                return;

            int id = Integer.parseInt(idStr.trim());

            // Check for duplicate ID
            boolean added = rentalSystem.addCustomerWithValidation(new Customer(id, name.trim(), license.trim()));

            if (added) {
                displayOutput("✓ CUSTOMER ADDED SUCCESSFULLY\n" +
                        "════════════════════════════\n" +
                        "Customer ID: " + id + "\n" +
                        "Name: " + name.trim() + "\n" +
                        "License: " + license.trim() + "\n\n");
            } else {
                displayOutput("✗ CUSTOMER ADDITION FAILED\n" +
                        "═════════════════════════\n" +
                        "Error: Customer ID " + id + " already exists!\n" +
                        "Please use a different ID.\n\n");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid customer ID (number)!");
        }
    }

    private void showCustomers() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 ALL CUSTOMERS\n");
        sb.append("═══════════════════════════════════════════════════════\n");

        List<Customer> customers = rentalSystem.getCustomers();

        if (customers.isEmpty()) {
            sb.append("No customers found in the system.\n");
        } else {
            for (int i = 0; i < customers.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, customers.get(i)));
            }
        }
        sb.append("\nTotal Customers: ").append(customers.size()).append("\n\n");
        displayOutput(sb.toString());
    }

    private void showAvailableCars() {
        StringBuilder sb = new StringBuilder();
        sb.append("🚗 AVAILABLE CARS FOR RENT\n");
        sb.append("═══════════════════════════════════════════════════════\n");

        List<Car> availableCars = rentalSystem.getAvailableCars();

        if (availableCars.isEmpty()) {
            sb.append("No cars currently available for rent.\n");
        } else {
            for (int i = 0; i < availableCars.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, availableCars.get(i)));
            }
        }
        sb.append("\nTotal Available: ").append(availableCars.size()).append("\n\n");
        displayOutput(sb.toString());
    }

    private void showAllCars() {
        StringBuilder sb = new StringBuilder();
        sb.append("🚙 ALL CARS IN SYSTEM\n");
        sb.append("═══════════════════════════════════════════════════════\n");

        List<Car> allCars = rentalSystem.getAllCars();

        if (allCars.isEmpty()) {
            sb.append("No cars in the system.\n");
        } else {
            for (int i = 0; i < allCars.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, allCars.get(i)));
            }
        }
        sb.append("\nTotal Cars: ").append(allCars.size()).append("\n\n");
        displayOutput(sb.toString());
    }

    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the Car Rental System?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private String showInputDialog(String title, String message) {
        return JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void displayOutput(String text) {
        outputArea.setText(text);
        outputArea.setCaretPosition(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CarRentalGUI().setVisible(true);
        });
    }
}
