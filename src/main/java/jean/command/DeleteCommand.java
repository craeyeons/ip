package jean.command;

import jean.storage.Storage;
import jean.task.Task;
import jean.task.TaskList;
import jean.ui.Ui;

import java.io.IOException;

public class DeleteCommand extends Command {
    int taskIndex;
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        taskList.deleteTask(this.taskIndex, ui);
    }
}
