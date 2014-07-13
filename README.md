Minecraft AutoBackup Program

Small program written in Java which provides simple and automatic creation of backups of Minecraft saves.
This program is using zip4j library to zip/unzip stored backups.

Notice: 
I don't care about any corrupted worlds caused by this program.
- All stable versions are marked by 'vx' where 'x' is number of update and those versions are recommended 
and usable without any danger.
- All versions with known errors are marked by 'snapshotx' where 'x' is number of update.
- All stable versions with all errors repaired and ready to use are marked by 'fixx' where second 'x' means version,
which this fix fixes.


HOW TO: BACKUP
- You can backup whole folder with all the save folders or specify only one save to backup. For first variant,
you need to choose or write path to '.minecraft\saves' folder or for second variant, 
simply choose or write path to your save folder like '.minecraft\saves\myworld'.
- Now you need to specify a path to your output folder which must exist - like 'C:\Backups'. There will be stored all
your backup files.
- If you want to keep those upper pathes for next uses, click on 'Save' button. This function will write upper choosed 
pathes to file 'data.nfo' placed in the same directory as actual program.
- Now you can backup your world/worlds via clicking on 'Backup' button. 
It will create a '.zip' file with name like 'backup_14_07_11'.

HOW TO: RETRIEVE
- You can easily retrieve the save/saves with one click from the backup file.
- Simply choose or write the path to your save folder as i written up there if you didn't do that before.
- Now click on red 'Load' button and choose desired backup file which from you want to retrieve the save/saves.

You can also list through all created backups and delete them too. The list will provide a view of all there placed backups, their last date of modify and also a size of all displayed backups.
If you need, you can also change the source for backups, if you want to load from other folder that the output folder.
