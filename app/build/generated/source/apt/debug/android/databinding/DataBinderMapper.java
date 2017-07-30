
package android.databinding;
import ravotta.carrie.hw5.BR;
class DataBinderMapper  {
    final static int TARGET_MIN_SDK = 19;
    public DataBinderMapper() {
    }
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case ravotta.carrie.hw5.R.layout.todo_row:
                    return ravotta.carrie.hw5.databinding.TodoRowBinding.bind(view, bindingComponent);
                case ravotta.carrie.hw5.R.layout.content_todo_list:
                    return ravotta.carrie.hw5.databinding.ContentTodoListBinding.bind(view, bindingComponent);
        }
        return null;
    }
    android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -478163497: {
                if(tag.equals("layout/todo_row_0")) {
                    return ravotta.carrie.hw5.R.layout.todo_row;
                }
                break;
            }
            case -1471043139: {
                if(tag.equals("layout/content_todo_list_0")) {
                    return ravotta.carrie.hw5.R.layout.content_todo_list;
                }
                break;
            }
        }
        return 0;
    }
    String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"item"
            ,"model"
            ,"position"};
    }
}