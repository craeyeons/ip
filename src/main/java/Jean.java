import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Jean {

    private static void bye() {
        System.out.println("\tGoodbye! See you soon!"
                           + "\n\tAu revoir! À tout à l'heure!");
    }

    private static void checkTodo(String input, ArrayList<Task> taskList) {
        try {
            if (input.trim().length() == 4) {
                throw new JeanException("The description must not be empty!"
                                        + "\nVous devez donner une description!");
            } else {
                add(new Todo(input.substring(5)), taskList);
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkDeadline(String input, ArrayList<Task> taskList) {
        int sep = input.indexOf("/by");
        try {
            if (sep == 9 || input.trim().length() == 8) {
                throw new JeanException("The description must not be empty!"
                                        + "\nVous devez donner une description!");
            } else if (sep == -1) {
                throw new JeanException("You must give a deadline!"
                                        + "\nVous devez donner un délai!");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                LocalDateTime dateTime = LocalDateTime.parse(input.substring(sep + 4), formatter);
                add(new Deadline(input.substring(9, sep), dateTime),
                        taskList);
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Please give a valid deadline in the format of yyyy-MM-dd HHmm!"
                               + "\nVeuillez indiquer une date limite valide au format yyyy-MM-dd HHmm!");
        }
    }

    private static void checkEvent(String input, ArrayList<Task> taskList) {
        int sep = input.indexOf("/at");
        try {
            if (sep == 6 || input.trim().length() == 5) {
                throw new JeanException("The description must not be empty!"
                                        + "\nVous devez donner une description!");
            } else if (sep == -1) {
                throw new JeanException("You must give a time!"
                                        + "\nVous devez donner une heure!");
            } else {
                add(new Event(input.substring(6, sep), input.substring(sep + 4)),
                        taskList);
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void add(Task newTask, ArrayList<Task> taskList) {
        taskList.add(newTask);
        System.out.println("\tadded / ajouté:"
                           + "\n\t\t" + newTask.toString());
        System.out.println("\tYou now have " + Task.numberOfTasks + " task(s)!"
                           + "\n\tVous avez " + Task.numberOfTasks + " tâche(s)!");
    }

    private static void list(ArrayList<Task> taskList) {
        for (int i = 0; i < Task.numberOfTasks; i++) {
            System.out.println("\t" + (i+1) + ".\t " + taskList.get(i).toString());
        }
    }

    private static void checkMark(String input, ArrayList<Task> taskList) {
        try {
            if (input.trim().length() == 4 || Integer.parseInt(input.substring(5)) < 1) {
                throw new JeanException("You must name a valid task to mark!"
                                        + "\nNom d'un valable tâche à marqué comme fait!");
            } else if (Integer.parseInt(input.substring(5)) > Task.numberOfTasks) {
                throw new JeanException("There are only " + Task.numberOfTasks + " task(s)!"
                                        + "\nIl y a seulement " + Task.numberOfTasks + " tâche(s)!");
            } else if (taskList.get(Integer.parseInt(input.substring(5)) - 1).isDone){
                throw new JeanException("It is already marked!"
                                        + "\nC'est déjaà fini!");
            } else {
                mark(taskList, Integer.parseInt(input.substring(5)));
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void mark(ArrayList<Task> taskList, int taskIndex) {
        Task curr = taskList.get(taskIndex - 1);
        curr.setIsDone(true);
        System.out.println("\tI have marked it as done:"
                           + "\n\tJe l'ai marqué comme fait:"
                           + "\n\t" + curr.toString());
    }

    private static void checkUnmark(String input, ArrayList<Task> taskList) {
        try {
            if (input.trim().length() == 6 || Integer.parseInt(input.substring(7))  < 1) {
                throw new JeanException("You must name a valid task to unmark!"
                                        + "\nNom d'une valable tâche à marqué comme défait!");
            } else if (Integer.parseInt(input.substring(7)) > Task.numberOfTasks) {
                throw new JeanException("There are only " + Task.numberOfTasks + " task(s)!"
                                        + "\nIl y a seulement " + Task.numberOfTasks + " tâche(s)!");
            } else if (!taskList.get(Integer.parseInt(input.substring(7)) - 1).isDone){
                throw new JeanException("It is not marked!"
                                        + "\nCe n'est pas encore fini!");
            } else {
                unmark(taskList, Integer.parseInt(input.substring(7)));
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void unmark(ArrayList<Task> taskList, int taskIndex) {
        Task curr = taskList.get(taskIndex - 1);
        curr.setIsDone(false);
        System.out.println("\tI have marked it as undone:"
                           + "\n\tJe l'ai marqué comme défait:"
                           + "\n\t" + curr.toString());
    }

    private static void checkDelete(String input, ArrayList<Task> taskList) {
        try {
            if (input.trim().length() == 6) {
                throw new JeanException("You must name a task to delete!"
                                        + "\nNom d'une tâche à supprimer!");
            } else if (Integer.parseInt(input.substring(7)) < 0 || Integer.parseInt(input.substring(7)) > Task.numberOfTasks) {
                throw new JeanException("It is not possible!"
                                        + "\nC'est impossible!");
            } else {
                delete(taskList, Integer.parseInt(input.substring(7)) - 1);
            }
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void delete(ArrayList<Task> taskList, int taskIndex) {
        Task curr = taskList.get(taskIndex);
        Task.numberOfTasks -= 1;
        taskList.remove(taskIndex);
        System.out.println("\tI have deleted the task:"
                           + "\n\tJe l'ai supprimé:"
                           + "\n\t" + curr.toString()
                           + "\n\tYou now have " + Task.numberOfTasks + " tasks remaining!"
                           + "\n\tIl vous reste maintenant " + Task.numberOfTasks + " tâches!");
    }

    public static void initialiseStartup(ArrayList<Task> taskList) {
        System.out.println("Hello! I'm Jean"
                           + "\nHow can I help you?"
                           + "\nBonjour! Je m'appelle Jean"
                           + "\nVous désirez?");

        File f = new File("data/list.txt");

        try {
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
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage()
                               + "\nQuelque chose a mal tourné: " + e.getMessage());
        } catch (JeanException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveFile(ArrayList<Task> taskList) throws IOException {
        FileWriter fw = new FileWriter("data/list.txt");
        for (Task x : taskList) {
            fw.write(x.getSaveData());
            fw.write("\n");
        }
        fw.close();
    }

    public static boolean processInput(String input, ArrayList<Task> taskList) {
        if (input.equals("bye")) {
            bye();
            try {
                saveFile(taskList);
            } catch (IOException e) {
                System.out.println("Something went wrong: " + e.getMessage()
                                   + "\nQuelque chose a mal tourné: " + e.getMessage());
            }
            return false;
        } else if (input.equals("list")) {
            list(taskList);
        } else if (input.startsWith("mark")) {
            checkMark(input, taskList);
        } else if (input.startsWith("unmark")) {
            checkUnmark(input, taskList);
        } else if (input.startsWith("todo")) {
            checkTodo(input, taskList);
        } else if (input.startsWith("deadline")) {
            checkDeadline(input, taskList);
        } else if (input.startsWith("event")) {
            checkEvent(input, taskList);
        } else if (input.startsWith("delete")) {
            checkDelete(input, taskList);
        } else {
            System.out.println("No such command!");
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayList<Task> taskList = new ArrayList<>();
        initialiseStartup(taskList);

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            input = scanner.nextLine();
        } while (processInput(input.trim(), taskList));
    }
}
