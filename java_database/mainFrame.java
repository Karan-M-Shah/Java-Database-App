
package java_database;
import java.sql. *;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class mainFrame extends JFrame implements ActionListener{
    
    static final String DEFAULT_QUERY = "";
    
    private Connection connection;
    private ResultSetTableModel tableModel;
    
    private static JButton connectionButton;
    private static JButton executeButton;
    private static JButton clearButton;
    private static JButton clearResult;
    
    private static JLabel connectionLabel;
    private static JLabel driverLabel;
    private static JLabel URLLabel;
    private static JLabel userLabel;
    private static JLabel passwordLabel;
    private static JLabel statusLabel;
    private static JLabel queryLabel;
    private static JLabel resultLabel;
    
    private static JComboBox driverBox;
    private static JComboBox URLBox;
    
    private static JTextField userText;
    
    private static JPasswordField passwordText;
    
    private static JTextArea queryText;
    
    private static JScrollPane scrollPane;
    
    private static JTable resultArea;
    
    private boolean isConnected = false;
    
    public mainFrame() throws SQLException{
        
        //WindowListener to disconnect from the database upon exit
        
        WindowListener wl = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(isConnected == false)
                {
                    dispose();
                    System.exit(0);
                }
                else try {
                    connection.close();
                    dispose();
                    System.exit(0);
                } catch (Exception ex) {JOptionPane.showMessageDialog(null, ex);}
            }
        };
        
        //Creating the JFrame
        setTitle("SQL GUI Client");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(wl);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
        //JFrame layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,0,0,0);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.LINE_START;
        
        //CONNECTION PANEL SECTION
        
        createConnectionPanel(gbc);
        
        //QUERY PANEL SECTION
        
        createQueryPanel(gbc);
        
        //RESULT PANEL SECTION
        
        createResultPanel(gbc);
        
        setVisible(true);
    } 
    
    private void createConnectionPanel(GridBagConstraints gbc)
    {
        //GridBagConstraints formatting
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        
        //Panel initialization
        JPanel connectionPanel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        System.out.println(connectionPanel.getSize());
        connectionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //Components of connection panel
        
        connectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(0,20,0,0);
        pgbc.anchor = GridBagConstraints.CENTER;
        pgbc.weightx = 1;
        pgbc.weighty = 1;
        
        pgbc.gridwidth = 2;
        pgbc.gridx = 0;
        pgbc.gridy = 0;
        
        connectionLabel = new JLabel("Enter Database Information");
        connectionLabel.setFont(new Font("Serif", Font.BOLD, 28));
        connectionPanel.add(connectionLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_END;
        pgbc.gridwidth = 1;
        pgbc.gridy = 1;
        
        driverLabel = new JLabel("JDBC Driver");
        driverLabel.setFont(new Font("Serif", Font.BOLD, 18));
        connectionPanel.add(driverLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_START;
        pgbc.gridx = 1;
        
        driverBox = new JComboBox();
        driverBox.addItem("com.mysql.cj.jdbc.Driver");
        connectionPanel.add(driverBox, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_END;
        pgbc.gridx = 0;
        pgbc.gridy = 2;
        
        URLLabel = new JLabel("Database URL");
        URLLabel.setFont(new Font("Serif", Font.BOLD, 18));
        connectionPanel.add(URLLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_START;
        pgbc.gridx = 1;
        
        URLBox = new JComboBox();
        URLBox.addItem("jdbc:mysql://localhost:3312/project3");
        connectionPanel.add(URLBox, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_END;
        pgbc.gridx = 0;
        pgbc.gridy = 3;
        
        userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Serif", Font.BOLD, 18));
        connectionPanel.add(userLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_START;
        pgbc.gridx = 1;
        
        userText = new JTextField(15);
        connectionPanel.add(userText, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_END;
        pgbc.gridx = 0;
        pgbc.gridy = 4;
        
        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Serif", Font.BOLD, 18));
        connectionPanel.add(passwordLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_START;
        pgbc.gridx = 1;
        
        passwordText = new JPasswordField(15);
        connectionPanel.add(passwordText, pgbc);
        
        pgbc.gridx = 1;
        pgbc.gridy = 5;
        
        statusLabel = new JLabel("No Connection");
        statusLabel.setFont(new Font("Serif", Font.BOLD, 18));
        connectionPanel.add(statusLabel, pgbc);
        
        pgbc.anchor = GridBagConstraints.LINE_END;
        pgbc.gridx = 0;
        
        connectionButton = new JButton("Connect to Database");
        connectionButton.setFont(new Font("Serif", Font.BOLD, 18));
        connectionButton.addActionListener(this);
        connectionPanel.add(connectionButton, pgbc);
        
        //Adding connection panel to the frame
        add(connectionPanel, gbc);
    }
    
    private void createQueryPanel(GridBagConstraints gbc) {
        //GridBagConstraints formatting
        gbc.gridy = 1;
        
        //Panel initialization
        JPanel queryPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        queryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //Components of query panel
        queryLabel = new JLabel("Enter An SQL Command");
        queryLabel.setFont(new Font("Serif", Font.BOLD, 28));
        queryPanel.add(queryLabel);
        
        queryText = new JTextArea(10, 35);
        queryText.setEnabled(false);
        queryText.setLineWrap(true);
        queryText.setWrapStyleWord(true);
        queryText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        queryText.setFont(new Font("Serif", Font.PLAIN, 20));
        
        scrollPane = new JScrollPane(queryText,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        queryPanel.add(scrollPane);
        
        executeButton = new JButton("Execute SQL Command");
        executeButton.setFont(new Font("Serif", Font.BOLD, 18));
        executeButton.addActionListener(this);
        executeButton.setEnabled(false);
        queryPanel.add(executeButton);
        
        clearButton = new JButton("Clear SQL Command");
        clearButton.setFont(new Font("Serif", Font.BOLD, 18));
        clearButton.addActionListener(this);
        queryPanel.add(clearButton);
        
        //Adding query panel to the frame
        add(queryPanel, gbc);
    }
    
    private void createResultPanel(GridBagConstraints gbc) {
         //GridBagConstraints formatting
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        
        //Panel initialization
        JPanel resultPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //Components of result panel
        
        resultPanel.add(Box.createRigidArea(new Dimension(0,10)));
        
        resultLabel = new JLabel("SQL Execution Result Window");
        resultLabel.setAlignmentX(CENTER_ALIGNMENT);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 28));
        resultPanel.add(resultLabel);
        
        resultPanel.add(Box.createRigidArea(new Dimension(0,60)));
        
        resultArea = new JTable(tableModel);
        resultArea.setMinimumSize(new Dimension(550, 550));
        resultArea.setMaximumSize(new Dimension(550, 550));
        resultArea.setFont(new Font("Serif", Font.PLAIN, 20));
        resultArea.setAlignmentX(CENTER_ALIGNMENT);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        scrollPane = new JScrollPane(resultArea);
        
        resultPanel.add(scrollPane);
        
        resultPanel.add(Box.createRigidArea(new Dimension(0,50)));
        
        clearResult = new JButton("Clear Result Window");
        clearResult.setAlignmentX(CENTER_ALIGNMENT);
        clearResult.setFont(new Font("Serif", Font.BOLD, 18));
        clearResult.addActionListener(this);
        resultPanel.add(clearResult);
        
        //Adding result panel to the frame
        add(resultPanel, gbc);
    }
    
    //Action listener for buttons
    
    @Override
    public void actionPerformed(ActionEvent event) {
     
        if(event.getSource().equals(connectionButton)) { //For database connection
            try {
                connectDataBase();
            } catch (SQLException ex) {
                statusLabel.setText("Connection Failed");
            }
        }
        
        else if(event.getSource().equals(executeButton)) { //For query execution
            executeStatement();
        }
        else if (event.getSource().equals(clearButton)) //For clearing a query
            queryText.setText("");
        else
            resultArea.setModel(new DefaultTableModel()); //For clearing a table
    }

    //Etablishes a connection
    private void connectDataBase() throws SQLException {
        connection = DriverManager.getConnection(String.valueOf(URLBox.getSelectedItem()),userText.getText(), String.valueOf(passwordText.getPassword()));
        
        statusLabel.setText("Connected to " + driverBox.getSelectedItem()); 
        isConnected = true;
        queryText.setEnabled(true);
        executeButton.setEnabled(true);
    }
    
    //Executes statement based on input. Either query or update
    private void executeStatement() {
        
        try {
            tableModel = new ResultSetTableModel(connection);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        String command = queryText.getText().split(" ")[0];
        
        if(command.toUpperCase().equals("SELECT"))
        {
            try {
                tableModel.setQuery(queryText.getText());
            } catch (SQLException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            resultArea.setModel(tableModel);
        }
        else
        {
            try {
                tableModel.setUpdate(queryText.getText());
            } catch (SQLException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            resultArea.setModel(tableModel);
        }
    }
 }