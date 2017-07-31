package ravotta.carrie.hw5.databinding;
import ravotta.carrie.hw5.R;
import ravotta.carrie.hw5.BR;
import android.view.View;
public class ActivityEditBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(3);
        sIncludes.setIncludes(0, 
            new String[] {"content_edit"},
            new int[] {1},
            new int[] {R.layout.content_edit});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 2);
    }
    // views
    public final ravotta.carrie.hw5.databinding.ContentEditBinding editContent;
    private final android.support.design.widget.CoordinatorLayout mboundView0;
    public final android.support.v7.widget.Toolbar toolbar;
    // variables
    private ravotta.carrie.hw5.TodoItem mItem;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityEditBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds);
        this.editContent = (ravotta.carrie.hw5.databinding.ContentEditBinding) bindings[1];
        setContainedBinding(this.editContent);
        this.mboundView0 = (android.support.design.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.toolbar = (android.support.v7.widget.Toolbar) bindings[2];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        editContent.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (editContent.hasPendingBindings()) {
            return true;
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
    }
    public ravotta.carrie.hw5.TodoItem getItem() {
        return mItem;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeEditContent((ravotta.carrie.hw5.databinding.ContentEditBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeEditContent(ravotta.carrie.hw5.databinding.ContentEditBinding EditContent, int fieldId) {
        switch (fieldId) {
            case BR._all: {
                synchronized(this) {
                        mDirtyFlags |= 0x1L;
                }
                return true;
            }
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
        // batch finished
        executeBindingsOn(editContent);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ActivityEditBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityEditBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityEditBinding>inflate(inflater, ravotta.carrie.hw5.R.layout.activity_edit, root, attachToRoot, bindingComponent);
    }
    public static ActivityEditBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityEditBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ravotta.carrie.hw5.R.layout.activity_edit, null, false), bindingComponent);
    }
    public static ActivityEditBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityEditBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_edit_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityEditBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): editContent
        flag 1 (0x2L): item
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}