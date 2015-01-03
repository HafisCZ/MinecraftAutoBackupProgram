import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class TimebackupService {

	Timer backupTimer;

	public TimebackupService(int seconds) {
		backupTimer = new Timer();
		backupTimer.schedule(new runAutomatedBackup(), seconds * 1000);
	}

	class runAutomatedBackup extends TimerTask {
		public void run() {
			try {
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd");
				java.awt.Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Make sure that autosaving is disabled\nor game is not running !", "Confirm backup creation",
						JOptionPane.PLAIN_MESSAGE);
				Start.filename = Start.path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip";
				if (new File(Start.path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip").isFile()) {
					for (int i = 1;; i++) {
						if (new File(Start.path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip").isFile()) {
							continue;
						}
						Start.filename = Start.path_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip";
						break;
					}
				}
				Start.compress(Start.compiler.ZIP, Start.path_save, Start.filename);
				Start.updateTable(Start.path_backup);
			} catch (Exception e) {

			}
			backupTimer.cancel();
			if (Start.timedBackup_running) new TimebackupService(Start.timedBackup_split);
		}
	}
}
