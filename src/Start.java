/**
 * This work is licenced under Creative Commons Attribution-Noncommercial-No Derivative Works 4.0 International. 
 * To view license terms, please visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

import it.sauronsoftware.ftp4j.FTPFile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

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

	// WINDOW VARIABLES
	public static JTextArea logArea;
	public static JButton window_createBackup;
	public static JButton window_playGame;

	// TABLE VARIABLES
	public static String[] table_columns = { "#", "Name", "Date", "Size" };
	public static JTable table;
	public static JTable table2;

	// BACKUP MANAGER VARIABLES
	public static JButton manager_loadOther;
	public static JButton manager_use;
	public static JButton manager_remove;
	public static JButton manager_rename;

	// TIMEBACKUP VARIABLES
	public static boolean time_enabled = false;
	public static boolean time_running = false;
	public static Integer time_split = 1;
	public static JCheckBox time_checkboxEnable;
	public static JSpinner time_spinnerSeconds;
	public static JSpinner time_spinnerMinutes;
	public static JSpinner time_spinnerHours;
	public static JButton time_buttonStart;

	// PATH VARIABLES
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

	// CLOUD VARIABLES
	public static boolean cloud_enabled = false; // TODO MAKE FALSE xD
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

	// LISTENERS
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

	public Start() {
		this.setFocusable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// Load saved settings
		try {
			if (new File("data.nfo").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("data.nfo")));
				String[] data = datas.split("\n");
				if (data.length >= 2) path_save = data[1].toString().replace(";", "");
				if (data.length >= 3) path_backup = data[2].toString().replace(";", "");
				if (data.length >= 4) path_game = data[3].toString().replace(";", "");
				if (data.length >= 6) {
					time_enabled = Integer.parseInt(data[5].replace(";", "")) == 1 ? true : false;
					time_split = Integer.parseInt(data[6].replace(";", ""));
					if (time_split.equals(0)) time_split++;
				}
			}
			if (new File("cloud.ini").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("cloud.ini")));
				String[] data = datas.split("\n");
				if (data.length >= 2) cloud_enabled = Integer.parseInt(data[1].toString().replace(";", "")) == 1 ? true : false;
				if (data.length >= 3) cloud_server = data[2].toString().replace(";", "");
				if (data.length >= 4) cloud_port = Integer.parseInt(data[3].toString().replace(";", ""));
				if (data.length >= 5) cloud_username = data[4].toString().replace(";", "");
				if (data.length >= 6) cloud_password = data[5].toString().replace(";", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPanel subPanel = new JPanel(new FlowLayout());
		{
			// Play button will launch the Game if path to .exe file is specified
			window_playGame = new JButton("Play");
			window_playGame.setFont(getFont().deriveFont(15.0f));
			window_playGame.setForeground(Color.BLACK);
			window_playGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (new File(path_game).isFile()) {
							new ProcessBuilder(path_game).start();
						}
					} catch (Exception f) {
						f.printStackTrace();
					}
				}
			});
			subPanel.add(window_playGame);
			// Create button creates a new backup if path to save file is specified
			window_createBackup = new JButton("Create Backup");
			window_createBackup.setFont(getFont().deriveFont(15.0f));
			window_createBackup.setForeground(new Color(0, 200, 10));
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
						ZipFile zipFile = new ZipFile(filename);
						ZipParameters parameters = new ZipParameters();
						parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
						parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
						zipFile.createZipFileFromFolder(path_save, parameters, false, 10485760);
						updateTable(path_backup);
						JOptionPane.showMessageDialog(null, "Backup file created :\n" + filename, "Backup completed", JOptionPane.PLAIN_MESSAGE);
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			subPanel.add(window_createBackup);
			// Starts or stops the automatic backup cycle
			time_buttonStart = new JButton("Start");
			time_buttonStart.setFont(getFont().deriveFont(15.0f));
			time_buttonStart.setForeground(Color.BLACK);
			time_buttonStart.setEnabled(time_enabled);
			time_buttonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					time_running = !time_running;
					if (time_running == true) {
						new TimebackupService(time_split);
						time_buttonStart.setText("Stop");
					} else {
						time_buttonStart.setText("Start");
					}
				}
			});
			subPanel.add(time_buttonStart);
		}
		this.add(subPanel);
		// Creates a Tabbed pane
		JTabbedPane mainWindow = new JTabbedPane();
		mainWindow.addTab("Local Backups", setupP1());
		mainWindow.addTab("Cloud Backups", setupP1B());
		mainWindow.addTab("Settings", setupP2());
		mainWindow.addTab("Cloud Connection", setupP3());
		mainWindow.addTab("Log Page", setupLog());
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
		table.setModel(new DefaultTableModel(getDatabase(backups), table_columns));
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
	}

	public static void updateTable2() {
		try {
			FTPService ftp = new FTPService(cloud_server, cloud_port);
			ftp.authorize(cloud_username, cloud_password);
			DefaultTableModel dm = (DefaultTableModel) table2.getModel();
			dm.getDataVector().removeAllElements();
			dm.fireTableDataChanged();
			table2.setModel(new DefaultTableModel(getCloudDatabase(ftp.getFiles("")), table_columns));
			table2.getColumnModel().getColumn(0).setPreferredWidth(1);
			ftp.close();
		} catch (Exception e) {
		}
	}

	public JPanel setupLog() { // TODO LOG LIST - RECEIVE ALL OPERATION STATUSES & DATA CHANGES & UPDATES
		JPanel pane = new JPanel();
		logArea = new JTextArea(15, 53);
		logArea.setFont(getFont().deriveFont(13.0f));
		JScrollPane scrollPane = new JScrollPane(logArea);
		logArea.setEditable(false);
		pane.add(scrollPane);
		logArea.append("[MBU] Minecraft Backup Utility | CC-NY-NC-ND by mar21");
		return pane;
	}

	public JPanel setupP1B() { // TODO PANE_CLOUDLIST
		JPanel pane = new JPanel();
		try {
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
			JPanel listing = new JPanel();
			pane.add(listing);
			table2 = new JTable();
			table2.setModel(new DefaultTableModel(new Object[][] {}, table_columns));
			table2.setPreferredScrollableViewportSize(new Dimension(570, 150));
			table2.setFillsViewportHeight(true);
			table2.getColumnModel().getColumn(0).setPreferredWidth(1);
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
					try {
						updateTable2();
					} catch (Exception e) {
					}
				}
			});
			cloud_remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (isAnyCellSelected(table2)) {
							String selected = new String(table2.getModel().getValueAt(table2.getSelectedRow(), 1).toString());
							FTPService ftp = new FTPService(cloud_server, cloud_port);
							ftp.authorize(cloud_username, cloud_password);
							ftp.removeFile(selected);
							ftp.close();
							updateTable2();
							showInfo("Cloud Service", "File removed succesfully !");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			cloud_download.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (isAnyCellSelected(table2)) {
							String selected = new String(table2.getModel().getValueAt(table2.getSelectedRow(), 1).toString());
							FTPService ftp = new FTPService(cloud_server, cloud_port);
							ftp.authorize(cloud_username, cloud_password);
							ftp.download(path_backup + "\\" + selected, selected);
							ftp.close();
							update();
							showInfo("Cloud Service", "File downloaded succesfully !");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			updateTable2();
		} catch (Exception e) {

		}
		return pane;
	}

	public JPanel setupP3() { // TODO PANE_CLOUD
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
					w.write((cloud_enabled ? 1 : 0) + ";\n");
					w.write(cloud_serverField.getText() + ";\n");
					w.write(cloud_portField.getText() + ";\n");
					w.write(cloud_usernameField.getText() + ";\n");
					w.write(cloud_passwordField.getText() + ";");
					w.close();
				} catch (Exception j) {
					j.printStackTrace();
				}
			}
		});
		outBox.add(cloud_save);

		return pane;
	}

	public JPanel setupP2() { // TODO PANE_SETTINGS
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
		sub4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Auto-Backup"));
		{
			time_checkboxEnable = new JCheckBox();
			time_checkboxEnable.setText("Enabled");
			time_checkboxEnable.setFont(getFont().deriveFont(17.0f));
			time_checkboxEnable.setSelected(time_enabled);
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
			time_spinnerHours.setEnabled(time_enabled);
			sub4.add(time_spinnerHours);
			sub4.add(new JLabel("Mins:"));
			time_spinnerMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 95, 1));
			time_spinnerMinutes.setEnabled(time_enabled);
			sub4.add(time_spinnerMinutes);
			sub4.add(new JLabel("Secs:"));
			time_spinnerSeconds = new JSpinner(new SpinnerNumberModel(1, 1, 59, 1));
			time_spinnerSeconds.setEnabled(time_enabled);
			sub4.add(time_spinnerSeconds);
		}
		pane.add(sub4);

		path_saveField.setText(path_save);
		path_backupField.setText(path_backup);
		path_gameField.setText(path_game);

		time_spinnerSeconds.setValue((int) (time_split % 60));
		time_spinnerMinutes.setValue((int) ((time_split % 3600) / 60));
		time_spinnerHours.setValue((int) (time_split / 3600));

		path_saveField.getDocument().addDocumentListener(changeListener);
		path_backupField.getDocument().addDocumentListener(changeListener);
		path_gameField.getDocument().addDocumentListener(changeListener);

		path_saveBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose save folder");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						path_save = (chooser.getSelectedFile()).toString();
						path_saveField.setText(path_save);
					}

				} catch (Exception j) {
				}
			}
		});

		path_backupBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose folder for backups");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						path_backup = (chooser.getSelectedFile()).toString();
						path_backupField.setText(path_backup);
					}
				} catch (Exception j) {
				}
			}
		});

		path_gameBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose Minecraft launcher");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						path_game = (chooser.getSelectedFile()).toString();
						path_gameField.setText(path_game);
					}
				} catch (Exception j) {
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
					BufferedWriter w = new BufferedWriter(new FileWriter("data.nfo"));
					w.write("[path]\n");
					w.write(path_save + ";\n");
					w.write(path_backup + ";\n");
					w.write(path_game + ";\n");
					w.write("[timebackup]\n");
					w.write((time_enabled == true ? 1 : 0) + ";\n");
					w.write(time_split + ";");
					w.close();
				} catch (Exception j) {
					j.printStackTrace();
				}
			}
		});
		pane.add(path_savePaths);

		return pane;
	}

	public JPanel setupP1() { // TODO PANE_BACKUPS
		JPanel pane = new JPanel();
		try {
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
			JPanel listing = new JPanel();
			pane.add(listing);
			table = new JTable();
			table.setModel(new DefaultTableModel(getDatabase(path_backup), table_columns));
			table.setPreferredScrollableViewportSize(new Dimension(570, 150));
			table.setFillsViewportHeight(true);
			table.getColumnModel().getColumn(0).setPreferredWidth(1);
			JScrollPane slr = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listing.add(slr);
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Local"));
			pane.add(buttons);
			manager_loadOther = new JButton("External Load");
			buttons.add(manager_loadOther);
			manager_use = new JButton("Load");
			buttons.add(manager_use);
			manager_rename = new JButton("Rename");
			buttons.add(manager_rename);
			manager_remove = new JButton("Remove");
			buttons.add(manager_remove);
			JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttons2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Cloud"));
			pane.add(buttons2);
			cloud_upload = new JButton("Upload");
			cloud_upload.setEnabled(cloud_enabled);
			buttons2.add(cloud_upload);
			manager_loadOther.addActionListener(new ActionListener() {
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
								ZipFile zipFile = new ZipFile(path);
								zipFile.extractAll(path_save.substring(0, path_save.lastIndexOf("\\")));
								JOptionPane.showMessageDialog(null, "Backup file loaded :\n" + path, "Load completed", JOptionPane.PLAIN_MESSAGE);
							}
						}
					} catch (Exception f) {
						f.printStackTrace();
					}
				}
			});
			// Removes selected backup file
			manager_remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (isAnyCellSelected(table)) {
							Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
							String deletepath = path_backup + "\\" + selected;
							delete(new File(deletepath));
							updateTable(path_backup);
						}
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			// Loads selected backup file
			manager_use.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (isAnyCellSelected(table)) {
							Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
							String path = path_backup + "\\" + selected;
							if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
									+ "\nThis will remove all current files in specified path" + "\nand place there all files from :\n" + path,
									"Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
								delete(new File(path_save));
								ZipFile zipFile = new ZipFile(path);
								zipFile.extractAll(path_save.substring(0, path_save.lastIndexOf("\\")));
								JOptionPane.showMessageDialog(null, "Backup file loaded :\n" + path, "Load completed", JOptionPane.PLAIN_MESSAGE);
							}
						}
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			// Changes name of selected file
			manager_rename.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (isAnyCellSelected(table)) {
							Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
							String path = path_backup + "\\" + selected;
							String newName = JOptionPane.showInputDialog(null, "Enter new name for file: ", "Rename file: " + selected,
									JOptionPane.PLAIN_MESSAGE);
							if (newName != "") {
								File sel = new File(path);
								sel.renameTo(new File(path_backup + "\\" + newName + ".zip"));
							}
							updateTable(path_backup);
						}
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			// Cloud
			cloud_upload.addActionListener(new ActionListener() {// TODO CLOUD
						public void actionPerformed(ActionEvent e) {
							try {
								if (isAnyCellSelected(table)) {
									String path = path_backup + "\\" + table.getModel().getValueAt(table.getSelectedRow(), 1);
									FTPService ftp = new FTPService(cloud_server, cloud_port);
									ftp.authorize(cloud_username, cloud_password);
									ftp.upload(path, "");
									ftp.close();
									showInfo("Cloud Service", "File uploaded succesfully !");
								}
							} catch (Exception f) {
								f.printStackTrace();
							}
						}
					});
		} catch (Exception j) {
			j.printStackTrace();
		}
		return pane;
	}

	public static void update() {
		path_save = path_saveField.getText();
		path_backup = path_backupField.getText();
		path_game = path_gameField.getText();
		time_split = (Integer) time_spinnerSeconds.getValue() + (Integer) time_spinnerMinutes.getValue() * 60
				+ (Integer) time_spinnerHours.getValue() * 3600;
		time_enabled = time_checkboxEnable.isSelected();
		cloud_enabled = cloud_checkboxEnable.isSelected();
		time_buttonStart.setEnabled(time_enabled);
		time_spinnerHours.setEnabled(time_enabled);
		time_spinnerMinutes.setEnabled(time_enabled);
		time_spinnerSeconds.setEnabled(time_enabled);
		cloud_download.setEnabled(cloud_enabled);
		cloud_upload.setEnabled(cloud_enabled);
		cloud_remove.setEnabled(cloud_enabled);
		cloud_refresh.setEnabled(cloud_enabled);
		updateTable(path_backup);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}

	public static Object[][] getDatabase(String location) {
		if (!(new File(location).exists())) return new Object[][] {};
		DateFormat dateFormat = new SimpleDateFormat("dd MM yy HH:mm:ss");
		File[] files = getFileList(location);
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = getFileNameList(location);
		for (int i = 0; i < files.length; i++) {
			dates[i] = dateFormat.format(files[i].lastModified());
			size[i] = files[i].length();
		}
		Object[][] mix = new Object[names.length][4];
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

	public static Object[][] getCloudDatabase(FTPFile[] files) {
		DateFormat dateFormat = new SimpleDateFormat("dd MM yy HH:mm:ss");
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = new String[files.length];
		for (int i = 0; i < files.length; i++)
			names[i] = files[i].getName();
		for (int i = 0; i < files.length; i++) {
			dates[i] = dateFormat.format(files[i].getModifiedDate());
			size[i] = files[i].getSize();
		}
		Object[][] mix = new Object[names.length][4];
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
		JFrame Frame = new JFrame("Minecraft Backup Utility | CC-NY-NC-ND by mar21");
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setResizable(false);
		Start Content = new Start();
		Content.setOpaque(true);
		Frame.setContentPane(Content);
		Frame.pack();
		Frame.setVisible(true);
		logArea.append("\n[MBU] Size [" + Frame.getWidth() + ";" + Frame.getHeight() + "]");
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

	public static File[] getFileList(String path) {
		File folder = new File(path);
		if (folder.isDirectory()) return folder.listFiles();
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

}