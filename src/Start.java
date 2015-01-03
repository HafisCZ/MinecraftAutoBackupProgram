/**
 * Created by HafisCZ aka MAR21. http://hafiscz.github.io/
 * This work is licenced under Creative Commons.
 * To view license terms, please visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

import it.sauronsoftware.ftp4j.FTPFile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import cloud.FTPService;

public class Start extends JPanel {

	/**
	 * More Info at http://hafiscz.github.io/
	 */

	private static final long serialVersionUID = 1L;

	public static String filename;
	public static float labelFont = 18.0f;

	// Upper Bar
	public static JButton window_createBackup;
	public static JButton window_playGame;

	// Log Area
	public static DefaultStyledDocument logAreaDoc;
	public static JTextPane logArea;
	public static JButton log_clean;

	// Tables
	public static JTable table;
	public static JTable table2;
	public static String[] table_columns = { "#", "Name", "Modify Time", "Size", "Description" };
	public static String[] table2_columns = { "#", "Name", "Modify Time", "Size" };
	public static String[] date_formats = { "dd/MM/yy HH:mm:ss", "dd-MM-yy HH:mm:ss", "dd.MM.yy HH:mm:ss", "MM/dd/yy HH:mm:ss", "MM-dd-yy HH:mm:ss",
			"MM.dd.yy HH:mm:ss" };

	// Local
	public static JButton manager_loadOther;
	public static JButton manager_loadSelected;
	public static JButton manager_remove;
	public static JButton manager_rename;
	public static JButton manager_description;
	public static boolean manager_isSelected = false;

	// Timed Backup
	public static boolean timedBackup_enabled = false;
	public static boolean timedBackup_running = false;
	public static Integer timedBackup_split = 1;
	public static JCheckBox time_checkboxEnable;
	public static JSpinner time_spinnerSeconds;
	public static JSpinner time_spinnerMinutes;
	public static JSpinner time_spinnerHours;
	public static JButton time_buttonStart;

	// Paths
	public static boolean isGameSpecified = false;
	public static boolean isPathSpecified = false;
	public static String path_save = "";
	public static String path_backup = "";
	public static String path_game = "";
	public static JButton path_saveBrowse;
	public static JButton path_backupBrowse;
	public static JButton path_gameBrowse;
	public static JButton path_savePaths;
	public static JTextField path_saveField;
	public static JTextField path_backupField;
	public static JTextField path_gameField;

	// Date Formats
	public static JComboBox<Object> date_formatting;
	public static int date_formatChoosen = 0;

	// Cloud
	public static boolean cloud_enabled = false;
	public static boolean cloud_isSelected = false;
	public static JButton cloud_upload;
	public static JButton cloud_remove;
	public static JButton cloud_download;
	public static JButton cloud_refresh;
	public static JButton cloud_save;
	public static JCheckBox cloud_checkboxEnable;
	public static JTextField cloud_serverField;
	public static JFormattedTextField cloud_portField;
	public static JTextField cloud_usernameField;
	public static JTextField cloud_passwordField;
	public static String cloud_server = "";
	public static Integer cloud_port = 21;
	public static String cloud_username = "";
	public static String cloud_password = "";

	// Listeners
	public static DocumentListener changeListener = new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {
			update();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}

		public void insertUpdate(DocumentEvent e) {
			update();
		}
	};

	public enum compiler {
		ZIP, RAR
	}

	public Start() {
		this.setFocusable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		int cloud_info = 0;
		int path_info = 0;
		// Load saved settings
		try {
			if (new File("data.ini").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("data.ini")));
				String[] data = datas.split("\n");
				if (data.length >= 2) date_formatChoosen = Integer.parseInt(data[1].toString());
				if (data.length >= 4) path_save = data[3].toString();
				if (data.length >= 5) path_backup = data[4].toString();
				if (data.length >= 6) path_game = data[5].toString();
				if (data.length >= 8) {
					timedBackup_enabled = Integer.parseInt(data[7]) == 1 ? true : false;
					timedBackup_split = Integer.parseInt(data[8]);
					if (timedBackup_split.equals(0)) timedBackup_split++;
				}
				path_info = data.length;
			}
			if (new File("cloud.ini").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("cloud.ini")));
				String[] data = datas.split("\n");
				if (data.length >= 2) cloud_enabled = Integer.parseInt(data[1].toString()) == 1 ? true : false;
				if (data.length >= 3) cloud_server = data[2].toString();
				if (data.length >= 4) cloud_port = Integer.parseInt(data[3].toString());
				if (data.length >= 5) cloud_username = data[4].toString();
				if (data.length >= 6) cloud_password = data[5].toString();
				cloud_info = data.length;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isPathSpecified = (!path_save.equals("") && !path_backup.equals("")) ? true : false;
		isGameSpecified = (!path_game.equals("")) ? true : false;
		JPanel subPanel = new JPanel(new FlowLayout());
		{
			// Play button will launch the Game if path to .exe file is
			// specified
			window_playGame = new JButton("Play");
			window_playGame.setFont(getFont().deriveFont(15.0f));
			window_playGame.setForeground(Color.BLACK);
			window_playGame.setEnabled(isGameSpecified);
			window_playGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (new File(path_game).isFile()) {
							new ProcessBuilder(path_game).start();
						}
					} catch (Exception f) {
						tText("\n[Play ERROR] Operation Failed !", Color.RED);
					}
				}
			});
			subPanel.add(window_playGame);
			// Create button creates a new backup if path to save file is
			// specified
			window_createBackup = new JButton("Create Backup");
			window_createBackup.setFont(getFont().deriveFont(15.0f));
			window_createBackup.setForeground(new Color(0, 200, 10));
			window_createBackup.setEnabled(isPathSpecified);
			window_createBackup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Calendar cal = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd");
						filename = path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip";
						if (new File(path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip").isFile()) {
							for (int i = 1;; i++) {
								if (new File(path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip").isFile()) {
									continue;
								}
								filename = path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip";
								break;
							}
						}
						compress(compiler.ZIP, path_save, filename);
						updateTable(path_backup);
						tText("\n[Local Manager] Backup ", Color.BLACK);
						tText(filename.toString(), Color.ORANGE);
						tText(" was created", Color.BLACK);
						JOptionPane.showMessageDialog(null, "Backup file created :\n" + filename, "Backup completed", JOptionPane.PLAIN_MESSAGE);
					} catch (Exception j) {
						tText("\n[Local Manager ERROR] Operation Failed !", Color.RED);
					}
				}
			});
			subPanel.add(window_createBackup);
			// Starts or stops the automatic backup cycle
			time_buttonStart = new JButton("Start");
			time_buttonStart.setFont(getFont().deriveFont(15.0f));
			time_buttonStart.setForeground(Color.BLACK);
			time_buttonStart.setEnabled(timedBackup_enabled && isPathSpecified);
			time_buttonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					timedBackup_running = !timedBackup_running;
					if (timedBackup_running == true) {
						new TimebackupService(timedBackup_split);
						time_buttonStart.setText("Stop");
						tText("\n[Timed Backup] Operation stopped !", Color.cyan);
					} else {
						time_buttonStart.setText("Start");
						tText("\n[Timed Backup] Operation started !", Color.cyan);
					}
				}
			});
			subPanel.add(time_buttonStart);
		}
		this.add(subPanel);
		// Creates a Tabbed pane
		JTabbedPane mainWindow = new JTabbedPane();
		mainWindow.addTab("Local Backups", tabLocalBackups());
		mainWindow.addTab("Cloud Backups", tabCloudBackups());
		mainWindow.addTab("Global Settings", tabGlobalSettings());
		mainWindow.addTab("Backup Settings", tabBackupSettings());
		mainWindow.addTab("Cloud Setup", tabCloudSettings());
		mainWindow.addTab("Log Page", tabLog());
		tText("\n[Settings] ", Color.BLACK);
		tText("" + path_info, Color.MAGENTA);
		tText(" path variables loaded", Color.BLACK);
		tText("\n[Settings] ", Color.BLACK);
		tText("" + cloud_info, Color.MAGENTA);
		tText(" cloud variables loaded", Color.BLACK);
		update();
		this.add(mainWindow);
	}

	public static void showInfo(String name, String message) {
		JOptionPane.showMessageDialog(null, message, name, JOptionPane.PLAIN_MESSAGE);
	}

	public static void updateTable(String backups) {
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		dm.getDataVector().removeAllElements();
		dm.fireTableDataChanged();
		table.setModel(new DefaultTableModel(getLocalDatabase(backups), table_columns));
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
	}

	public static void updateManager() {
		manager_isSelected = (table.getSelectedColumn() != -1) ? true : false;
		manager_loadSelected.setEnabled(manager_isSelected);
		manager_remove.setEnabled(manager_isSelected);
		manager_rename.setEnabled(manager_isSelected);
		manager_description.setEnabled(manager_isSelected);
		cloud_upload.setEnabled(cloud_enabled && manager_isSelected);
	}

	public static void updateTable2() {
		try {
			FTPService ftp = new FTPService(cloud_server, cloud_port);
			tText("\n[FTP Service] Connected to ", Color.BLACK);
			tText(cloud_server, Color.BLUE);
			tText(" @ ", Color.BLACK);
			tText(cloud_port.toString(), Color.BLUE);
			ftp.authorize(cloud_username, cloud_password).def();
			tText("\n[FTP Service] Authorized as ", Color.BLACK);
			tText(cloud_username, Color.BLUE);
			DefaultTableModel dm = (DefaultTableModel) table2.getModel();
			dm.getDataVector().removeAllElements();
			dm.fireTableDataChanged();
			table2.setModel(new DefaultTableModel(getCloudDatabase(ftp.getFiles(ftp.currentDirectory())), table2_columns));
			table2.getColumnModel().getColumn(0).setPreferredWidth(1);
			tText("\n[Cloud Manager] File list received", Color.BLACK);
			ftp.close();
			tText("\n[Cloud Manager] Connection closed", Color.BLACK);
		} catch (Exception e) {
			tText("\n[Cloud Manager ERROR] Operation Failed !", Color.RED);
		}
	}

	private static Style setColor(Color c) {
		StyleContext context = new StyleContext();
		Style style = context.addStyle("test", null);
		StyleConstants.setForeground(style, c);
		return style;
	}

	public JPanel tabLog() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		logAreaDoc = new DefaultStyledDocument();
		logArea = new JTextPane(logAreaDoc);
		logArea.setFont(getFont().deriveFont(13.0f));
		JScrollPane scrollPane = new JScrollPane(logArea);
		logArea.setEditable(false);
		pane.add(scrollPane);
		log_clean = new JButton("Clear");
		pane.add(log_clean);
		log_clean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					logArea.setText("");
					logAreaDoc.insertString(0, "Terminal output:", setColor(Color.BLACK));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		logArea.setText("Terminal output:");
		return pane;
	}

	public JPanel tabCloudBackups() { // TODO PANE_CLOUDLIST
		JPanel pane = new JPanel();
		try {
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
			JPanel listing = new JPanel();
			pane.add(listing);
			table2 = new JTable();
			table2.setModel(new DefaultTableModel(new Object[][] {}, table2_columns));
			table2.setPreferredScrollableViewportSize(new Dimension(570, 150));
			table2.setFillsViewportHeight(true);
			table2.getColumnModel().getColumn(0).setPreferredWidth(1);
			table2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					updateCloudManager();
				}
			});
			JScrollPane slr2 = new JScrollPane(table2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listing.add(slr2);
			JPanel buttons1 = new JPanel();
			buttons1.setLayout(new FlowLayout());
			buttons1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Cloud control"));
			cloud_remove = new JButton("Remove");
			cloud_remove.setEnabled(cloud_enabled);
			buttons1.add(cloud_remove);
			cloud_download = new JButton("Download");
			cloud_download.setEnabled(cloud_enabled);
			buttons1.add(cloud_download);
			cloud_refresh = new JButton("Refresh");
			cloud_refresh.setEnabled(cloud_enabled);
			buttons1.add(cloud_refresh);
			pane.add(buttons1);
			cloud_refresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateTable2();
				}
			});
			cloud_remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (isAnyCellSelected(table2)) {
							String selected = new String(table2.getModel().getValueAt(table2.getSelectedRow(), 1).toString());
							FTPService ftp = new FTPService(cloud_server, cloud_port);
							tText("\n[Cloud Manager] Connected to ", Color.BLACK);
							tText(cloud_server, Color.BLUE);
							tText(" @ ", Color.BLACK);
							tText(cloud_port.toString(), Color.BLUE);
							ftp.authorize(cloud_username, cloud_password).def();
							tText("\n[Cloud Manager] Authorized as ", Color.BLACK);
							tText(cloud_username, Color.BLUE);
							ftp.removeFile(selected);
							tText("\n[Cloud Manager] File ", Color.BLACK);
							tText(selected.toString(), Color.ORANGE);
							tText(" was removed", Color.BLACK);
							ftp.close();
							tText("\n[Cloud Manager] Connection closed", Color.BLACK);
							updateTable2();
							showInfo("Cloud Manager", "File removed succesfully !");
						}
					} catch (Exception e) {
						tText("\n[Cloud Manager] Operation Failed !", Color.RED);
					}
				}
			});
			cloud_download.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (isAnyCellSelected(table2)) {
							String selected = new String(table2.getModel().getValueAt(table2.getSelectedRow(), 1).toString());
							FTPService ftp = new FTPService(cloud_server, cloud_port);
							tText("\n[Cloud Manager] Connected to ", Color.BLACK);
							tText(cloud_server, Color.BLUE);
							tText(" @ ", Color.BLACK);
							tText(cloud_port.toString(), Color.BLUE);
							ftp.authorize(cloud_username, cloud_password).def();
							tText("\n[Cloud Manager] Authorized as ", Color.BLACK);
							tText(cloud_username, Color.BLUE);
							ftp.download(path_backup + "\\" + selected, selected);
							tText("\n[Cloud Manager] File ", Color.BLACK);
							tText(selected.toString(), Color.ORANGE);
							tText(" was downloaded", Color.BLACK);
							ftp.close();
							tText("\n[Cloud Manager] Connection closed", Color.BLACK);
							update();
							showInfo("Cloud Manager", "File downloaded succesfully !");
						}
					} catch (Exception e) {
						tText("\n[FTPService ERROR] Operation Failed !", Color.RED);
					}
				}
			});
		} catch (Exception e) {

		}
		return pane;
	}

	public JPanel tabCloudSettings() { // TODO PANE_CLOUD
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		JLabel cloud_l0 = new JLabel("Currently supporting only FTP transfer, Google Drive access / others will be added in future.");
		cloud_l0.setAlignmentX(CENTER_ALIGNMENT);
		pane.add(cloud_l0);
		JPanel fieldPanel = new JPanel(new FlowLayout());
		fieldPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "FTP Settings"));
		JPanel labelBox = new JPanel();
		labelBox.setLayout(new BoxLayout(labelBox, BoxLayout.PAGE_AXIS));
		fieldPanel.add(labelBox);
		JPanel fieldBox = new JPanel();
		fieldBox.setLayout(new BoxLayout(fieldBox, BoxLayout.PAGE_AXIS));
		fieldPanel.add(fieldBox);
		JLabel cloud0 = new JLabel("Server");
		cloud0.setFont(getFont().deriveFont(labelFont));
		labelBox.add(cloud0);
		JLabel cloud1 = new JLabel("Port");
		cloud1.setFont(getFont().deriveFont(labelFont));
		labelBox.add(cloud1);
		JLabel cloud2 = new JLabel("Username");
		cloud2.setFont(getFont().deriveFont(labelFont));
		labelBox.add(cloud2);
		JLabel cloud3 = new JLabel("Password");
		cloud3.setFont(getFont().deriveFont(labelFont));
		labelBox.add(cloud3);
		cloud_serverField = new JTextField();
		cloud_serverField.setColumns(30);
		cloud_serverField.setFont(getFont().deriveFont(14.0f));
		cloud_serverField.setText(cloud_server);
		fieldBox.add(cloud_serverField);
		NumberFormat numbersOnly = NumberFormat.getNumberInstance();
		numbersOnly.setGroupingUsed(false);
		cloud_portField = new JFormattedTextField(numbersOnly);
		cloud_portField.setColumns(30);
		cloud_portField.setFont(getFont().deriveFont(14.0f));
		cloud_portField.setText(cloud_port.toString());
		fieldBox.add(cloud_portField);
		cloud_usernameField = new JTextField();
		cloud_usernameField.setColumns(30);
		cloud_usernameField.setFont(getFont().deriveFont(14.0f));
		cloud_usernameField.setText(cloud_username);
		fieldBox.add(cloud_usernameField);
		cloud_passwordField = new JTextField();
		cloud_passwordField.setColumns(30);
		cloud_passwordField.setFont(getFont().deriveFont(14.0f));
		cloud_passwordField.setText(cloud_password);
		fieldBox.add(cloud_passwordField);
		pane.add(fieldPanel);
		JPanel outBox = new JPanel();
		outBox.setLayout(new FlowLayout());
		pane.add(outBox);
		cloud_checkboxEnable = new JCheckBox();
		cloud_checkboxEnable.setText("Enabled");
		cloud_checkboxEnable.setFont(getFont().deriveFont(17.0f));
		cloud_checkboxEnable.setSelected(cloud_enabled);
		cloud_checkboxEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update();
			}
		});
		outBox.add(cloud_checkboxEnable);
		cloud_save = new JButton("Save");
		cloud_save.setFont(getFont().deriveFont(14.0f));
		cloud_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					update();
					BufferedWriter w = new BufferedWriter(new FileWriter("cloud.ini"));
					w.write("[cloud]\n");
					w.write((cloud_enabled ? 1 : 0) + "\n");
					w.write(cloud_serverField.getText() + "\n");
					w.write(cloud_portField.getText() + "\n");
					w.write(cloud_usernameField.getText() + "\n");
					w.write(cloud_passwordField.getText());
					w.close();
					tText("\n[Settings] Cloud settings saved", Color.BLACK);
				} catch (Exception j) {
					tText("\n[Settings ERROR] Operation Failed !", Color.RED);
				}
			}
		});
		outBox.add(cloud_save);

		return pane;
	}

	public JPanel tabBackupSettings() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		JPanel subpanel1 = new JPanel();
		subpanel1.setLayout(new FlowLayout());
		subpanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Time stamp"));

		pane.add(subpanel1);

		return pane;
	}

	public JPanel tabGlobalSettings() { // TODO PANE_SETTINGS
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		JPanel pathes = new JPanel();
		pathes.setLayout(new FlowLayout());
		pathes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Paths"));
		{
			JPanel sub1 = new JPanel();
			sub1.setLayout(new BoxLayout(sub1, BoxLayout.PAGE_AXIS));
			{
				JLabel lb1 = new JLabel("Save Folder:");
				lb1.setFont(getFont().deriveFont(labelFont));
				sub1.add(lb1);
				JLabel lb2 = new JLabel("Backup Folder:");
				lb2.setFont(getFont().deriveFont(labelFont));
				sub1.add(lb2);
				JLabel lb3 = new JLabel("Game:");
				lb3.setFont(getFont().deriveFont(labelFont));
				sub1.add(lb3);
			}
			pathes.add(sub1);

			JPanel sub2 = new JPanel();
			sub2.setLayout(new BoxLayout(sub2, BoxLayout.PAGE_AXIS));
			{
				path_saveField = new JTextField();
				path_saveField.setText(path_save);
				path_saveField.setColumns(30);
				path_saveField.setFont(getFont().deriveFont(14.0f));
				sub2.add(path_saveField);
				path_backupField = new JTextField();
				path_backupField.setText(path_backup);
				path_backupField.setColumns(30);
				path_backupField.setFont(getFont().deriveFont(14.0f));
				sub2.add(path_backupField);
				path_gameField = new JTextField();
				path_gameField.setText(path_game);
				path_gameField.setColumns(30);
				path_gameField.setFont(getFont().deriveFont(14.0f));
				sub2.add(path_gameField);
			}
			pathes.add(sub2);

			JPanel sub3 = new JPanel();
			sub3.setLayout(new BoxLayout(sub3, BoxLayout.PAGE_AXIS));
			{
				path_saveBrowse = new JButton("...");
				sub3.add(path_saveBrowse);
				path_backupBrowse = new JButton("...");
				sub3.add(path_backupBrowse);
				path_gameBrowse = new JButton("...");
				sub3.add(path_gameBrowse);
			}
			pathes.add(sub3);

		}
		pane.add(pathes);

		pane.add(new JLabel(""));

		JPanel sub4 = new JPanel();
		sub4.setLayout(new FlowLayout());
		sub4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timed notifications"));
		{
			time_checkboxEnable = new JCheckBox();
			time_checkboxEnable.setText("Enabled");
			time_checkboxEnable.setFont(getFont().deriveFont(17.0f));
			time_checkboxEnable.setSelected(timedBackup_enabled);
			time_checkboxEnable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					update();
				}
			});
			sub4.add(time_checkboxEnable);
			JLabel l1 = new JLabel("Split: ");
			l1.setFont(getFont().deriveFont(labelFont));
			sub4.add(l1);
			sub4.add(new JLabel("Hours:"));
			time_spinnerHours = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			time_spinnerHours.setEnabled(timedBackup_enabled);
			sub4.add(time_spinnerHours);
			sub4.add(new JLabel("Mins:"));
			time_spinnerMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 95, 1));
			time_spinnerMinutes.setEnabled(timedBackup_enabled);
			sub4.add(time_spinnerMinutes);
			sub4.add(new JLabel("Secs:"));
			time_spinnerSeconds = new JSpinner(new SpinnerNumberModel(1, 1, 59, 1));
			time_spinnerSeconds.setEnabled(timedBackup_enabled);
			sub4.add(time_spinnerSeconds);
		}
		pane.add(sub4);

		JPanel sub5 = new JPanel();
		sub5.setLayout(new FlowLayout());
		sub5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Time format"));
		{
			JLabel date_formattingLabel = new JLabel("Time format: ");
			date_formattingLabel.setFont(getFont().deriveFont(labelFont));
			sub5.add(date_formattingLabel);
			date_formatting = new JComboBox<Object>(date_formats);

			date_formatting.setSelectedIndex(date_formatChoosen);
			date_formatting.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					date_formatChoosen = date_formatting.getSelectedIndex();
				}
			});
			sub5.add(date_formatting);
		}
		pane.add(sub5);

		path_saveField.setText(path_save);
		path_backupField.setText(path_backup);
		path_gameField.setText(path_game);

		time_spinnerSeconds.setValue((int) (timedBackup_split % 60));
		time_spinnerMinutes.setValue((int) ((timedBackup_split % 3600) / 60));
		time_spinnerHours.setValue((int) (timedBackup_split / 3600));

		path_saveField.getDocument().addDocumentListener(changeListener);
		path_backupField.getDocument().addDocumentListener(changeListener);
		path_gameField.getDocument().addDocumentListener(changeListener);

		path_saveBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose save folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setFileHidingEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					path_save = (chooser.getSelectedFile()).toString();
					path_saveField.setText(path_save);
					update();
				}
			}
		});

		path_backupBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose folder for backups");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setFileHidingEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					path_backup = (chooser.getSelectedFile()).toString();
					path_backupField.setText(path_backup);
					update();
				}
			}
		});

		path_gameBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose Minecraft launcher");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileHidingEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					path_game = (chooser.getSelectedFile()).toString();
					path_gameField.setText(path_game);
					update();
				}
			}
		});

		path_savePaths = new JButton("Save settings");
		path_savePaths.setFont(getFont().deriveFont(15.0f));
		path_savePaths.setForeground(Color.BLACK);
		path_savePaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					update();
					BufferedWriter w = new BufferedWriter(new FileWriter("data.ini"));
					w.write("[dateformat]\n");
					w.write(date_formatChoosen + "\n");
					w.write("[path]\n");
					w.write(path_save + "\n");
					w.write(path_backup + "\n");
					w.write(path_game + "\n");
					w.write("[timebackup]\n");
					w.write((timedBackup_enabled == true ? 1 : 0) + "\n");
					w.write(timedBackup_split + "");
					w.close();
					tText("[Settings] Path settings saved", Color.BLACK);
				} catch (Exception j) {
					tText("\n[Settings ERROR] Operation Failed !", Color.RED);
				}
			}
		});
		pane.add(path_savePaths);

		return pane;
	}

	public JPanel tabLocalBackups() { // TODO PANE_BACKUPS
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		JPanel listing = new JPanel();
		pane.add(listing);
		table = new JTable();
		table.setModel(new DefaultTableModel(getLocalDatabase(path_backup), table_columns));
		table.setPreferredScrollableViewportSize(new Dimension(570, 150));
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				updateManager();
			}
		});
		JScrollPane slr = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listing.add(slr);

		// Upper Buttons
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Local"));

		manager_loadOther = new JButton("External Load");
		manager_loadSelected = new JButton("Load");
		manager_rename = new JButton("Rename");
		manager_remove = new JButton("Remove");
		manager_description = new JButton("Description");

		buttons.add(manager_loadOther);
		buttons.add(manager_loadSelected);
		buttons.add(manager_rename);
		buttons.add(manager_remove);
		buttons.add(manager_description);

		pane.add(buttons);

		// Lower Buttons
		JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttons2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Cloud"));
		cloud_upload = new JButton("Upload");
		cloud_upload.setEnabled(cloud_enabled);
		buttons2.add(cloud_upload);
		pane.add(buttons2);

		updateManager();

		manager_loadOther.addActionListener(new ActionListener() {// TODO loadExternal
					public void actionPerformed(ActionEvent e) {
						try {
							JFileChooser chooser = new JFileChooser();
							chooser.setCurrentDirectory(new java.io.File("."));
							chooser.setDialogTitle("Choose backup to load");
							chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							chooser.setFileHidingEnabled(false);
							chooser.setAcceptAllFileFilterUsed(false);
							if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
								String path = chooser.getSelectedFile().getCanonicalPath();
								if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
										+ "\nThis will remove all current files in specified path" + "\nand place there all files from :\n" + path,
										"Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
									delete(new File(path_save));
									tText("\n[Local Manager] Save at ", Color.BLACK);
									tText(path_save, Color.ORANGE);
									tText(" was removed", Color.BLACK);
									ZipFile zipFile = new ZipFile(path);
									tText("\n[Local Manager] Extracting files ...", Color.BLACK);
									zipFile.extractAll(path_save.substring(0, path_save.lastIndexOf("\\")));
									JOptionPane.showMessageDialog(null, "Backup file loaded :\n" + path, "Load completed", JOptionPane.PLAIN_MESSAGE);
									tText("\n[Local Manager] Backup file ", Color.BLACK);
									tText(chooser.getSelectedFile().getName(), Color.ORANGE);
									tText(" was loaded", Color.BLACK);
								}
							}
						} catch (Exception f) {
							tText("\n[Local Manager] Operation Failed !", Color.RED);
						}
					}
				});

		manager_remove.addActionListener(new ActionListener() {// TODO removeLocal
					public void actionPerformed(ActionEvent e) {
						if (isAnyCellSelected(table)) {
							Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
							String deletepath = path_backup + "\\" + selected;
							delete(new File(deletepath));
							String descpath = deletepath.substring(0, deletepath.length() - 4) + ".decr";
							if (new File(descpath).exists()) {
								delete(new File(descpath));
							}
							updateTable(path_backup);
							tText("\n[Local Manager] File ", Color.BLACK);
							tText(selected.toString(), Color.ORANGE);
							tText(" was removed", Color.BLACK);
						}
					}
				});

		manager_loadSelected.addActionListener(new ActionListener() { // TODO loadLocal
					public void actionPerformed(ActionEvent e) {
						try {
							if (isAnyCellSelected(table)) {
								Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
								String path = path_backup + "\\" + selected;
								if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
										+ "\nThis will remove all current files in specified path" + "\nand place there all files from :\n" + path,
										"Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
									delete(new File(path_save));
									tText("\n[Local Manager] Save at ", Color.BLACK);
									tText(path_save, Color.ORANGE);
									tText(" was removed", Color.BLACK);
									ZipFile zipFile = new ZipFile(path);
									tText("\n[Local Manager] Extracting files ...", Color.BLACK);
									zipFile.extractAll(path_save.substring(0, path_save.lastIndexOf("\\")));
									JOptionPane.showMessageDialog(null, "Backup file loaded :\n" + path, "Load completed", JOptionPane.PLAIN_MESSAGE);
									tText("\n[Local Manager] Backup file ", Color.BLACK);
									tText(selected.toString(), Color.ORANGE);
									tText(" was loaded", Color.BLACK);
								}
							}
						} catch (Exception j) {
							tText("\n[Local Manager ERROR] Operation Failed !", Color.RED);
						}
					}
				});

		manager_rename.addActionListener(new ActionListener() { // TODO renameLocal
					public void actionPerformed(ActionEvent e) {
						if (isAnyCellSelected(table)) {
							Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
							String path = path_backup + "\\" + selected;
							String newName = (String) JOptionPane.showInputDialog(null, "Enter new name for file: ", "Rename file: " + selected,
									JOptionPane.PLAIN_MESSAGE, null, null, selected.toString().substring(0, selected.toString().length() - 4));
							if ((newName != null) && (newName.length() > 0)) {
								File sel = new File(path);
								String filepath = path_backup + "\\" + newName;
								sel.renameTo(new File(filepath + ".zip"));
								if (new File(path.substring(0, path.length() - 4) + ".decr").exists()) {
									File description = new File(path.substring(0, path.length() - 4) + ".decr");
									description.renameTo(new File(filepath + ".decr"));
								}
								tText("\n[Local Manager] File ", Color.BLACK);
								tText(selected.toString(), Color.ORANGE);
								tText(" was renamed to ", Color.BLACK);
								tText(newName, Color.ORANGE);
							} else {
								tText("\n[Local Manager ERROR] Operation Failed !", Color.RED);
							}
							updateTable(path_backup);
						}
					}
				});

		manager_description.addActionListener(new ActionListener() { // TODO descriptionLocal
					public void actionPerformed(ActionEvent e) {
						try {
							if (isAnyCellSelected(table)) {
								Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
								String path = path_backup + "\\" + selected;
								String description_text = null;
								if (new File(new File(path)
										.getCanonicalPath()
										.toString()
										.replace(new File(path).getName(),
												new File(path).getName().substring(0, new File(path).getName().length() - 3) + "decr")).exists())
									description_text = new String(Files.readAllBytes(Paths.get(new File(new File(path)
											.getCanonicalPath()
											.toString()
											.replace(new File(path).getName(),
													new File(path).getName().substring(0, new File(path).getName().length() - 3) + "decr"))
											.getCanonicalPath())));
								description_text = (String) JOptionPane.showInputDialog(null,
										"Enter a description: \n-Leave blank to remove description", "File description for " + selected,
										JOptionPane.PLAIN_MESSAGE, null, null, description_text);
								if (description_text != (null) && description_text.length() > 0) {
									BufferedWriter w = new BufferedWriter(new FileWriter(new File(new File(path)
											.getCanonicalPath()
											.toString()
											.replace(new File(path).getName(),
													new File(path).getName().substring(0, new File(path).getName().length() - 3) + "decr"))
											.getCanonicalPath()));
									w.write(description_text);
									w.close();
									tText("\n[Local Manager] Description of ", Color.BLACK);
									tText(selected.toString(), Color.ORANGE);
									tText(" was created", Color.BLACK);
								} else if (description_text == null) {
									// Do nothing if returned NULL (CANCEL BUTTON)
								} else {
									if (new File(new File(path)
											.getCanonicalPath()
											.toString()
											.replace(new File(path).getName(),
													new File(path).getName().substring(0, new File(path).getName().length() - 3) + "decr")).exists())
										new File(new File(path)
												.getCanonicalPath()
												.toString()
												.replace(new File(path).getName(),
														new File(path).getName().substring(0, new File(path).getName().length() - 3) + "decr"))
												.delete();
									tText("\n[Local Manager] Description of ", Color.BLACK);
									tText(selected.toString(), Color.ORANGE);
									tText(" was removed", Color.BLACK);
								}
								update();
							}
						} catch (Exception f) {
							tText("\n[Local Manager ERROR] Operation Failed !", Color.RED);
						}
					}
				});

		cloud_upload.addActionListener(new ActionListener() {// TODO uploadToCloud
					public void actionPerformed(ActionEvent e) {
						try {
							if (isAnyCellSelected(table)) {
								Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
								String path = path_backup + "\\" + selected;
								FTPService ftp = new FTPService(cloud_server, cloud_port);
								tText("\n[Cloud Manager] Connected to ", Color.BLACK);
								tText(cloud_server, Color.BLUE);
								tText(" @ ", Color.BLACK);
								tText(cloud_port.toString(), Color.BLUE);
								ftp.authorize(cloud_username, cloud_password).def();
								tText("\n[Cloud Manager] Authorized as ", Color.BLACK);
								tText(cloud_username, Color.BLUE);
								ftp.upload(path, "");
								tText("\n[Cloud Manager] File ", Color.BLACK);
								tText(selected.toString(), Color.ORANGE);
								tText(" uploaded", Color.BLACK);
								ftp.close();
								tText("\n[Cloud Manager] Connection closed", Color.BLACK);
							}
						} catch (Exception f) {
							tText("\n[Cloud Manager ERROR] Operation Failed !", Color.RED);
						}
					}
				});

		return pane;
	}

	// FNC

	public static void tText(String text, Color color) {
		try {
			logAreaDoc.insertString(logAreaDoc.getLength(), text, setColor(color));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static void updateCloudManager() {
		cloud_isSelected = (table2.getSelectedColumn() != -1) ? true : false;
		cloud_enabled = cloud_checkboxEnable.isSelected();
		cloud_download.setEnabled(cloud_enabled && cloud_isSelected);
		cloud_remove.setEnabled(cloud_enabled && cloud_isSelected);
	}

	public static void update() {// TODO update
		path_save = path_saveField.getText();
		path_backup = path_backupField.getText();
		path_game = path_gameField.getText();
		cloud_enabled = cloud_checkboxEnable.isSelected();
		cloud_upload.setEnabled(cloud_enabled && manager_isSelected);
		cloud_refresh.setEnabled(cloud_enabled);
		updateCloudManager();
		timedBackup_split = (Integer) time_spinnerSeconds.getValue() + (Integer) time_spinnerMinutes.getValue() * 60
				+ (Integer) time_spinnerHours.getValue() * 3600;
		timedBackup_enabled = time_checkboxEnable.isSelected();
		time_buttonStart.setEnabled(timedBackup_enabled && isPathSpecified);
		time_spinnerHours.setEnabled(timedBackup_enabled);
		time_spinnerMinutes.setEnabled(timedBackup_enabled);
		time_spinnerSeconds.setEnabled(timedBackup_enabled);
		isPathSpecified = (!path_save.equals("") && !path_backup.equals("")) ? true : false;
		isGameSpecified = (!path_game.equals("")) ? true : false;
		window_createBackup.setEnabled(isPathSpecified);
		window_playGame.setEnabled(isGameSpecified);
		updateTable(path_backup);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}

	public static Object[][] getLocalDatabase(String location) { // TODO getLocalDatabase
		if (!(new File(location).exists())) return new Object[][] {};
		DateFormat dateFormat = new SimpleDateFormat(date_formats[date_formatChoosen]);
		File[] files = getFileList(location, ".zip");
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			names[i] = files[i].getName();
			dates[i] = dateFormat.format(files[i].lastModified());
			size[i] = files[i].length();
		}
		Object[][] mix = new Object[files.length][5];
		for (int i = 0; i < files.length; i++) {
			mix[i][0] = i + 1;
			mix[i][1] = names[i];
			mix[i][2] = dates[i];
			mix[i][3] = ((size[i] / 1024) < 1) ? size[i] + " B" : (((size[i] / 1048576) < 1) ? size[i] / 1024 + ","
					+ (int) Math.ceil(((size[i] % 1024) * 1000) / 1000) + " kB" : size[i] / 1048576 + ","
					+ (int) Math.ceil(((size[i] / 1024) * 1000) / 1000) + " MB");
			try {
				String pr_fileName = files[i].getName();
				File pr_file = new File(files[i].getCanonicalPath().toString()
						.replace(pr_fileName, pr_fileName.substring(0, pr_fileName.length() - 3) + "decr"));
				if (pr_file.exists()) {
					mix[i][4] = new String(Files.readAllBytes(Paths.get(pr_file.getCanonicalPath())));
					tText("\n[Local Manager] Description for ", Color.BLACK);
					tText(files[i].getName(), Color.ORANGE);
					tText(" was loaded", Color.black);
				}
			} catch (Exception e) {
			}
		}
		return mix;
	}

	public static Object[][] getCloudDatabase(FTPFile[] raw_files) { // TODO getCloudDatabase
		ArrayList<FTPFile> zipOnly = new ArrayList<FTPFile>();
		for (FTPFile f : raw_files) {
			String f_name = f.getName();
			if (f_name.substring(f_name.length() - 4, f_name.length()).equals(".zip")) zipOnly.add(f);
		}
		FTPFile[] files = new FTPFile[zipOnly.size()];
		files = zipOnly.toArray(files);
		DateFormat dateFormat = new SimpleDateFormat(date_formats[date_formatChoosen]);
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = new String[files.length];
		for (int i = 0; i < files.length; i++)
			names[i] = files[i].getName();
		for (int i = 0; i < files.length; i++) {
			dates[i] = dateFormat.format(files[i].getModifiedDate());
			size[i] = files[i].getSize();
		}
		Object[][] mix = new Object[files.length][4];
		for (int i = 0; i < files.length; i++) {
			mix[i][0] = i + 1;
			mix[i][1] = names[i];
			mix[i][2] = dates[i];
			mix[i][3] = ((size[i] / 1024) < 1) ? size[i] + " B" : (((size[i] / 1048576) < 1) ? size[i] / 1024 + ","
					+ (int) Math.ceil(((size[i] % 1024) * 1000) / 1000) + " kB" : size[i] / 1048576 + ","
					+ (int) Math.ceil(((size[i] / 1024) * 1000) / 1000) + " MB");
		}
		return mix;
	}

	public static void setupGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JFrame Frame = new JFrame("Minecraft Backup Utility | CC-BY-NC-ND by mar21");
		Frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Start.class.getClass().getResource("/icon16.png")));
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setResizable(false);
		Start Content = new Start();
		Content.setOpaque(true);
		Frame.setContentPane(Content);
		Frame.pack();
		Frame.setVisible(true);
	}

	public static boolean delete(File file) {
		File[] list;
		if (file.equals(null)) return false;
		if (file.isFile()) return file.delete();
		if (!file.isDirectory()) return false;
		list = file.listFiles();
		if (list != null && list.length > 0) {
			for (File f : list)
				if (!delete(f)) return false;
		}
		return file.delete();
	}

	public static String[] getFileNameList(String path) {
		return new File(path).isDirectory() ? new File(path).list() : null;
	}

	public static File[] getFileList(String path, String doReturn) {
		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] temp = folder.listFiles();
			ArrayList<File> validFiles = new ArrayList<File>();
			for (File f : temp) {
				String f_name = f.getName();
				if (f_name.substring(f_name.length() - 4, f_name.length()).equals(doReturn)) validFiles.add(f);
			}
			File[] returnArray = new File[validFiles.size()];
			returnArray = validFiles.toArray(returnArray);
			return returnArray;
		}
		return null;
	}

	public static boolean isAnyCellSelected(JTable table) {
		for (int i = 0; i < table.getModel().getRowCount(); i++) {
			for (int y = 0; y < table.getModel().getColumnCount(); y++) {
				if (table.isCellSelected(i, y)) return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param type
	 *            Which compiler use
	 * @param source
	 *            Which folder use
	 * @param location
	 *            Where place new file
	 */

	public static boolean compress(compiler type, String input, String output) { // XXX Complete even with
		try {
			if (type == compiler.ZIP) {
				ZipFile zipFile = new ZipFile(output);
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				zipFile.createZipFileFromFolder(input, parameters, false, 10485760);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @param compFile
	 *            Compressed file used to extract
	 * @param output
	 *            Location where extract files from compFile
	 */

	public static boolean decompress(String compFile, String output) {
		return false;
	}

}