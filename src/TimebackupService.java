import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

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
				ZipFile zipFile;
				zipFile = new ZipFile(Start.filename);
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				zipFile.createZipFileFromFolder(Start.path_save, parameters, false, 10485760);
				Start.updateTable(Start.path_backup);
			} catch (Exception e) {

			}
			backupTimer.cancel();
			if (Start.timedBackup_running) new TimebackupService(Start.timedBackup_split);
		}
	}
}
