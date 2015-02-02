import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.event.*;


/**
*    This class is the main GUI - for application running aparently
*      because of fucked up JApplet status bullshit
*   Lukas Saul   Jan 2003
*
*/
public class JSphereChess extends JFrame implements ActionListener{
	// add new variables to bottom of list
	public String user_dir = System.getProperty("user.dir");
	public String file_sep = System.getProperty("file.separator");
	public String save_dir = user_dir; // for now...
	public String CRLF = System.getProperty("line.separator");
	public String backupFileName = ".backup";

	// Chess Objects
	private SphereChess sc;  // most of the action

	// GUI Objects...
	private Container contentPane;
	private JPanel buttonPanel, goodsPanel;
	private JButton newButton, serverButton, undoButton, piecesButton;
	private SphereChessComponent idx3dPanel;

	/**
	* the constructor for the GUI.  Constructing this object starts the program.
	*
	*/
	public JSphereChess () {
		if (user_dir == null) user_dir = "";
		if (save_dir == null) save_dir = "";

		o(save_dir);

		setTitle("JSphereChess");
		contentPane = getContentPane();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//saveToFile();
				System.exit(0);
			}
		});


		//This code is to override the system.out.println to logfile.txt
		/*
		System.out.println("Trying to convert output to log file...");
		try {
			FileOutputStream str = new FileOutputStream(new File(user_dir + file_sep + ".log.txt"));
		    System.setOut(new PrintStream(str));
	        System.setErr(new PrintStream(str));
        }
        catch (Exception ex) { ex.printStackTrace(); }
        */
        //System.out.println("Output log for ASCIIDump.java");
		//**** --------------------- Done Initialization----------------------------------------****/


		// Create GUI Objects...

		buttonPanel = new JPanel();
		newButton = new JButton("New Game");
		newButton.addActionListener(this);
		buttonPanel.add(newButton);
		serverButton = new JButton("Server Options");
		serverButton.addActionListener(this);
		buttonPanel.add(serverButton);
		undoButton = new JButton("Undo");
		undoButton.addActionListener(this);
		buttonPanel.add(undoButton);
		piecesButton = new JButton("Pieces");
		piecesButton.addActionListener(this);
		buttonPanel.add(piecesButton);


		sc=new SphereChess();
		idx3dPanel=new SphereChessComponent(sc);
		//idx3dPanel.setSize(200,300);
		//goodsPanel = new JPanel();
		//goodsPanel.setSize(200,300);
		//goodsPanel.setPreferredSize(200,300);
		//goodsPanel.add(idx3dPanel);

		contentPane.add(idx3dPanel,"Center");
		contentPane.add(buttonPanel, "South");

		//fileBase = new FileBase(rootDir);
		setSize(500,500);
		//pack();
		show();
	}

	/**
	*   Here's where all the button presses and actions go...
	*/
	public void actionPerformed(ActionEvent e) {
		Object source = e.getActionCommand();
		//System.out.println(e+"");

		if (source == "New Game") {
			o("new game dialog?");
		}
		else if (source=="Undo") {
		}

	}


	public static void main(String[] args) {
		JSphereChess  jsc = new JSphereChess();
	}

	public static void o(String s) {
		System.out.println(s);
	}

}