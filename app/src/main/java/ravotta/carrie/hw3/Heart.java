package ravotta.carrie.hw3;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

public class Heart extends Shape {
    private int strokeWidth;
    private final int fillColor;
    private ColorStateList strokeColor;
    private Path path;
    private Paint strokePaint;
    private Paint fillPaint;


    public Heart(int strokeWidth, int fillColor, ColorStateList strokeColor) {
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
        canvas.drawPath(path, strokePaint);
    }

    @Override
    protected void onResize(float width, float height) {
        super.onResize(width, height);
        path = new Path();

        // Starting point
        path.moveTo(width / 2, height / 5);

        // Upper left path
        path.cubicTo(5 * width / 14, 0,
                0, height / 15,
                width / 28, 2 * height / 5);

        // Lower left path
        path.cubicTo(width / 14, 2 * height / 3,
                3 * width / 7, 5 * height / 6,
                width / 2, height);

        // Lower right path
        path.cubicTo(4 * width / 7, 5 * height / 6,
                13 * width / 14, 2 * height / 3,
                27 * width / 28, 2 * height / 5);

        // Upper right path
        path.cubicTo(width, height / 15,
                9 * width / 14, 0,
                width / 2, height / 5);

        path.close();
    }
}

