package ravotta.carrie.hw5.databinding;
import ravotta.carrie.hw5.R;
import ravotta.carrie.hw5.BR;
import android.view.View;
public class TodoRowBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    private final android.widget.LinearLayout mboundView0;
    private final android.widget.TextView mboundView1;
    private final android.widget.TextView mboundView2;
    // variables
    private ravotta.carrie.hw5.TodoItem mItem;
    private ravotta.carrie.hw5.Model mModel;
    private int mPosition;
    private final android.view.View.OnClickListener mCallback2;
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public TodoRowBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback1 = new android.databinding.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
            case BR.model :
                setModel((ravotta.carrie.hw5.Model) variable);
                return true;
            case BR.position :
                setPosition((int) variable);
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
    public void setModel(ravotta.carrie.hw5.Model Model) {
        this.mModel = Model;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.model);
        super.requestRebind();
    }
    public ravotta.carrie.hw5.Model getModel() {
        return mModel;
    }
    public void setPosition(int Position) {
        this.mPosition = Position;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.position);
        super.requestRebind();
    }
    public int getPosition() {
        return mPosition;
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
        ravotta.carrie.hw5.Model model = mModel;
        int itemPriority = 0;
        int position = mPosition;
        java.lang.String itemName = null;

        if ((dirtyFlags & 0x9L) != 0) {



                if (item != null) {
                    // read item.priority
                    itemPriority = item.getPriority();
                    // read item.name
                    itemName = item.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            this.mboundView1.setOnClickListener(mCallback1);
            this.mboundView2.setOnClickListener(mCallback2);
        }
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, itemName);
            ravotta.carrie.hw5.BindingAdapters.setIntText(this.mboundView2, itemPriority);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // model
                ravotta.carrie.hw5.Model model = mModel;
                // model != null
                boolean modelJavaLangObjectNull = false;
                // item
                ravotta.carrie.hw5.TodoItem item = mItem;
                // position
                int position = mPosition;



                modelJavaLangObjectNull = (model) != (null);
                if (modelJavaLangObjectNull) {




                    model.itemPrioritySelected(item, position);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // model
                ravotta.carrie.hw5.Model model = mModel;
                // model != null
                boolean modelJavaLangObjectNull = false;
                // item
                ravotta.carrie.hw5.TodoItem item = mItem;
                // position
                int position = mPosition;



                modelJavaLangObjectNull = (model) != (null);
                if (modelJavaLangObjectNull) {




                    model.itemNameSelected(item, position);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static TodoRowBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static TodoRowBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<TodoRowBinding>inflate(inflater, ravotta.carrie.hw5.R.layout.todo_row, root, attachToRoot, bindingComponent);
    }
    public static TodoRowBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static TodoRowBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ravotta.carrie.hw5.R.layout.todo_row, null, false), bindingComponent);
    }
    public static TodoRowBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static TodoRowBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/todo_row_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new TodoRowBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): item
        flag 1 (0x2L): model
        flag 2 (0x3L): position
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}