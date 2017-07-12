package ravotta.carrie.hw3;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

public class Star extends Shape {
    private int strokeWidth;
    private final int fillColor;
    private ColorStateList strokeColor;
    private Path path;
    private Paint strokePaint;
    private Paint fillPaint;

    public Star(int strokeWidth, int fillColor, ColorStateList strokeColor) {
        this.strokeWidth = strokeWidth;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;

        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(strokeColor.getColorForState(new int[0], 0));
        this.strokePaint.setStrokeJoin(Paint.Join.MITER);
        this.strokePaint.setStrokeWidth(strokeWidth);

        this.fillPaint = new Paint();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setColor(fillColor);
    }

    public void setState(int[] stateList) {
        this.strokePaint.setColor(strokeColor.getColorForState(stateList, 0));
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(path, fillPaint);
        //canvas.drawPath(path, strokePaint);
    }

    @Override
    protected void onResize(float width, float height) {
        super.onResize(width, height);
        path = new Path();

        float mid = width / 2;
        float min = Math.min(width, height);
        float half = min / 2;
        mid = mid - half;

        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f);

        path.close();
    }
}
