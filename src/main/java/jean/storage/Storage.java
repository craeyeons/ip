package jean.storage;

import jean.exception.JeanException;
import jean.task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class which handles the saving and loading of data from data files.
 */
public class Storage {
    private String filepath;

    /**
     * Constructs a Storage object with the corresponding filepath.
     *
     * @param filepath Filepath of the data.
     */
    public Storage (String filepath) {
        this.filepath = filepath;
    }

    /**
     * Loads memory from the memory file into an ArrayList of Tasks.
     *
     * @return Returns the ArrayList of Tasks from memory.
     * @throws JeanException When the file is corrupted.
     * @throws IOException When the designated filepath cannot be found.
     */
    public ArrayList<Task> load() throws JeanException, IOException{
        File f = new File(this.filepath);
        ArrayList<Task> taskList = new ArrayList<>();

        if (!f.createNewFile()) {
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String[] info = s.nextLine().split("\\|");
                Task temp = null;

                switch (info[0]) {
                case "T":
                    temp = new Todo(info[2]);
                    break;
                case "D":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                    LocalDateTime dateTime = LocalDateTime.parse(info[3], formatter);
                    temp = new Deadline(info[2], dateTime);
                    break;
                case "E":
                    temp = new Event(info[2], info[3]);
                    break;
                }

                if (temp == null) {
                    throw new JeanException("File is corrupted!"
                            + "\nLe fichier est corrompu!");
                }

                temp.setIsDone(info[1].equals("1"));
                taskList.add(temp);
            }
        }

        return taskList;
    }

    /**
     * Saves the ArrayList to the designated filepath.
     *
     * @param taskList The TaskList containing Tasks to be saved.
     * @throws IOException When the designated filepath cannot be found.
     */
    public void saveFile(TaskList taskList) throws IOException {
        FileWriter fw = new FileWriter(this.filepath);
        for (Task x : taskList.getTaskList()) {
            fw.write(x.getSaveData());
            fw.write("\n");
        }
        fw.close();
    }
}
