package tasks;

import java.time.LocalDateTime;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean completed) {
        super(description, completed);
    }

    @Override
    public String getFileFormat() {
        return String.format("T | %s", super.getFileFormat());
    }

    @Override
    public boolean isOnDate(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return false;
    }
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}