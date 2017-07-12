package ravotta.carrie.hw3;

import android.graphics.Rect;

public class Thing {
	public enum Type {
		Square, Circle, Heart, Star, Blank
	}
	private Type type;
	private Rect bounds;
    private boolean shown;

	public Thing(Type type, Rect bounds) {
		this.type = type;
		this.bounds = bounds;
	}

    public void setType(Type type) {
        this.type = type;
    }

	public void setBounds(Rect bounds) {
		this.bounds = bounds;
	}

	public Rect getBounds() {
		return bounds;
	}

	public Type getType() {
		return type;
	}

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
