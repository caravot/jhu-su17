package ravotta.carrie.hw5;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;

public class TodoItem {
    public final ObservableLong id = new ObservableLong();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableInt priority = new ObservableInt();
    public final ObservableField<Status> status = new ObservableField<>();

    public TodoItem() {
        this.id.set(-1L);
        this.name.set("");
        this.description.set("");
        this.priority.set(1);
        this.status.set(Status.PENDING);
    }

    public TodoItem(long id, String name, String description, int priority, Status status) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.priority.set(priority);
        this.status.set(status);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", priority=" + priority.get() +
                ", status=" + status.get() +
                '}';
    }
}
