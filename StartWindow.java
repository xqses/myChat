
package mychat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

class StartWindow extends JFrame implements ActionListener {
    
    private final JButton button;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    JComboBox contactBox;
    JComboBox contactFolderBox;

    StartWindow() throws IOException {
        setTitle("Start Window");
        setSize(new Dimension(400, 300));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        northPanel = new JPanel();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        JComboBox contactFolderBox = new JComboBox();
        // JComboBox contactBox = new JComboBox();
        button = new JButton("Add new contact");
        northPanel.add(contactFolderBox);
      //  centerPanel.add(contactBox);
        southPanel.add(button);
        String folderName = "MyContacts";
        contactBox = new JComboBox(createContactList(folderName));
        centerPanel.add(contactBox);
        // centerPanel.add(connectButton, BorderLayout.SOUTH);
        contactFolderBox.addActionListener(this);
        contactBox.addActionListener(this);
        button.addActionListener(this);
        setVisible(true);
        pack();
    }
    //  String[] createContactFolderList() {

    // }
   @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() instanceof JFileChooser) {
            String folderName = (String) ae.toString();
            System.out.println(folderName);
            try {
                contactBox = new JComboBox(createContactList(folderName));
            } catch (IOException ex) {
                Logger.getLogger(StartWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            JButton connectButton = new JButton();
            connectButton.addActionListener(this);
            centerPanel.add(contactBox);
            centerPanel.add(connectButton, BorderLayout.SOUTH);
            
        }
        else if (ae.getSource() instanceof JComboBox) {
            JComboBox cb = (JComboBox) ae.getSource();
            contactPicked = (Contact) cb.getSelectedItem();
            
            
        } else if (ae.getSource() instanceof JButton) {
            if (((JButton) (ae.getSource())).getText().equals("Add new contact")) {
            NewContact newContact = new NewContact();
            newContact.setVisible(true);
            this.dispose();
            }
            else if (((JButton) (ae.getSource())).getText().equals("Connect")) {
                if (contactPicked != null) {
                    String IP = contactPicked.CONTACT_NAME;
                    int PORT = contactPicked.CONTACT_PORT;
                    Client newClient = new Client(IP, PORT);
                }
            }
            else if (((JButton) (ae.getSource())).getText().equals("Choose contact folder")) {
                JDialog chooseDialog = new JDialog();
                chooseDialog.setSize(400,300);
                JFileChooser contactFolderChooser = new JFileChooser();
                chooseDialog.add(contactFolderChooser);
               // int returnVal = contactFolderChooser.showOpenDialog(this);
                chooseDialog.setVisible(true);
                contactFolderChooser.addActionListener(this);
            }
        }
    }

    Contact[] createContactList(String fileName) throws IOException {
        String textFileName = fileName + ".txt";
        File file = new File("Contacts/", textFileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            lines.add(stringBuffer.toString());
            stringBuffer.setLength(0);
        }
        ArrayList<String> NAMES = new ArrayList<>();
        ArrayList<String> IP_ADDRESSES = new ArrayList<>();
        ArrayList<Integer> PORTS = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String string = lines.get(i);
            String[] splitString = string.split("\\s+");
            NAMES.add(splitString[0]);
            IP_ADDRESSES.add(splitString[1]);
            PORTS.add(Integer.parseInt(splitString[2]));
        }
        Contact[] contactList = new Contact[lines.size()];
        int i = 0;
        while (i < contactList.length) {
            contactList[i] = new Contact(NAMES.get(i), IP_ADDRESSES.get(i), PORTS.get(i));
            i++;
        }
        return contactList;
    }
    
}