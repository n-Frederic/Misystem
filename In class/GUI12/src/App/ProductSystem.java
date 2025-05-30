package App;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProductSystem extends JFrame {

    private JTextField nameField;
    private JTextField priceField;
    private JTextField numberField;
    private JTextArea textArea;
    private JButton addButton;
    private JButton displayButton;
    private JButton saveToFileButton;
    private ArrayList<Product> products = new ArrayList<>();

    public ProductSystem() {
        setLayout(new BorderLayout());

        // Add components to the frame
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField = new JTextField(10));
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField = new JTextField(10));
        inputPanel.add(new JLabel("Number:"));
        inputPanel.add(numberField = new JTextField(10));

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        displayButton = new JButton("Display");
        saveToFileButton = new JButton("Save to File");
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(saveToFileButton);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add event listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProducts();
            }
        });

        saveToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("Save to File");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void addProduct() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int number = Integer.parseInt(numberField.getText());

        if (name.isEmpty() || price == 0 || number == 0) {
            textArea.append("Please fill in all fields!\n");
            return;
        }

        Product product = new Product(name, price, number);
        products.add(product);
        textArea.append("Add Product " + product.getName() + " success!\n");

        // Clear input fields
        nameField.setText("");
        priceField.setText("");
        numberField.setText("");
    }

    private void displayProducts() {
        textArea.setText("");
        for (Product product : products) {
            textArea.append(product.toString() + "\n");
        }
    }
}


