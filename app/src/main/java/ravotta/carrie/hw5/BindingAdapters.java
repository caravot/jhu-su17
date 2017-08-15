package ravotta.carrie.hw5;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import java.util.WeakHashMap;

public class BindingAdapters {
    private static WeakHashMap<TextView, Long> lastSetLongValues = new WeakHashMap<>();
	private static WeakHashMap<TextView, Integer> lastSetIntValues = new WeakHashMap<>();
	private static WeakHashMap<TextView, Status> lastSetStatusValues = new WeakHashMap<>();

    @BindingAdapter("android:relativeDateTime")
    public static void setRelativeDateTime(TextView textView, long value) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a z");
        textView.setText(sdf.format(value));
    }

    @BindingAdapter("android:text")
    public static void setLongText(TextView textView, long value) {
        String oldValue = textView.getText().toString();
        String newValue = String.valueOf(value);

        if (!oldValue.equals(newValue)) {
            textView.setText(newValue);
            lastSetLongValues.put(textView, value);
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static long getLongText(TextView textView) {
        try {
            long value = Long.parseLong(textView.getText().toString());
            lastSetLongValues.put(textView, value);

            return value;
        } catch (NumberFormatException e) {
            Long value = lastSetLongValues.get(textView);

            if (value != null) {
                return value;
            }

            return 0L;
        }
    }

	@BindingAdapter("android:text")
	public static void setIntText(TextView textView, int value) {
		String oldValue = textView.getText().toString();
		String newValue = String.valueOf(value);

		if (!oldValue.equals(newValue)) {
			textView.setText(newValue);
            lastSetIntValues.put(textView, value);
		}
	}

	@InverseBindingAdapter(attribute = "android:text")
	public static int getIntText(TextView textView) {
		try {
			int value = Integer.parseInt(textView.getText().toString());
            lastSetIntValues.put(textView, value);

            return value;
		} catch (NumberFormatException e) {
			Integer value = lastSetIntValues.get(textView);

			if (value != null) {
                return value;
            }

			return 0;
		}
	}

	@BindingAdapter("android:text")
	public static void setStatusText(TextView textView, Status value) {
		String oldValue = textView.getText().toString();
		String newValue = value.name();

		if (!oldValue.equals(newValue)) {
			textView.setText(newValue);
			lastSetStatusValues.put(textView, value);
		}
	}

	@InverseBindingAdapter(attribute = "android:text")
	public static Status getStatusText(TextView textView) {
		try {
			Status value = Status.valueOf(textView.getText().toString());
			lastSetStatusValues.put(textView, value);

			return value;
		} catch (IllegalArgumentException e) {
			Status value = lastSetStatusValues.get(textView);

			if (value != null) {
				return value;
			}

			return Status.values()[0];
		}
	}
}
