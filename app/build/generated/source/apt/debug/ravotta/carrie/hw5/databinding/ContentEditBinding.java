package ravotta.carrie.hw5.databinding;
import ravotta.carrie.hw5.R;
import ravotta.carrie.hw5.BR;
import android.view.View;
public class ContentEditBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    public final android.widget.EditText description;
    private final android.widget.ScrollView mboundView0;
    public final android.widget.EditText name;
    public final android.widget.EditText priority;
    // variables
    private ravotta.carrie.hw5.TodoItem mItem;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ContentEditBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.description = (android.widget.EditText) bindings[2];
        this.description.setTag(null);
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.name = (android.widget.EditText) bindings[1];
        this.name.setTag(null);
        this.priority = (android.widget.EditText) bindings[3];
        this.priority.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean setVariable(int variableId, Object variable) {
        switch(variableId) {
            case BR.item :
                setItem((ravotta.carrie.hw5.TodoItem) variable);
                return true;
        }
        return false;
    }

    public void setItem(ravotta.carrie.hw5.TodoItem Item) {
        this.mItem = Item;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.item);
        super.requestRebind();
    }
    public ravotta.carrie.hw5.TodoItem getItem() {
        return mItem;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        ravotta.carrie.hw5.TodoItem item = mItem;
        java.lang.String itemPriorityJavaLangString = null;
        java.lang.String itemDescription = null;
        int itemPriority = 0;
        java.lang.String itemName = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (item != null) {
                    // read item.description
                    itemDescription = item.getDescription();
                    // read item.priority
                    itemPriority = item.getPriority();
                    // read item.name
                    itemName = item.getName();
                }


                // read (item.priority) + ("")
                itemPriorityJavaLangString = (itemPriority) + ("");
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.description.setText(itemDescription);
            this.name.setText(itemName);
            this.priority.setText(itemPriorityJavaLangString);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ContentEditBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ContentEditBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ContentEditBinding>inflate(inflater, ravotta.carrie.hw5.R.layout.content_edit, root, attachToRoot, bindingComponent);
    }
    public static ContentEditBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ContentEditBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ravotta.carrie.hw5.R.layout.content_edit, null, false), bindingComponent);
    }
    public static ContentEditBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ContentEditBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/content_edit_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ContentEditBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): item
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}