package ravotta.carrie.hw5;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import java.util.WeakHashMap;

public class BindingAdapters {
	private static WeakHashMap<TextView, Integer> lastSetValues = new WeakHashMap<>();
	private static WeakHashMap<TextView, Status> lastSetStatusValues = new WeakHashMap<>();

	@BindingAdapter("android:text")
	public static void setIntText(TextView textView, int value) {
		String oldValue = textView.getText().toString();
		String newValue = String.valueOf(value);

		if (!oldValue.equals(newValue)) {
			textView.setText(newValue);
			lastSetValues.put(textView, value);
		}
	}

	@InverseBindingAdapter(attribute = "android:text")
	public static int getIntText(TextView textView) {
		try {
			int value = Integer.parseInt(textView.getText().toString());
			lastSetValues.put(textView, value);

            return value;
		} catch (NumberFormatException e) {
			Integer value = lastSetValues.get(textView);

			if (value != null) {
                return value;
            }
			return 0;
		}
	}
	@BindingAdapter("android:text")
	public static void setStatesText(TextView textView, Status value) {
		String oldValue = textView.getText().toString();
		String newValue = value.name();

		if (!oldValue.equals(newValue)) {
			textView.setText(newValue);
			lastSetStatusValues.put(textView, value);
		}
	}

	@InverseBindingAdapter(attribute = "android:text")
	public static Status getStatesText(TextView textView) {
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
