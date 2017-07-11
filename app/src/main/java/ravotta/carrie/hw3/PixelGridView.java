package ravotta.carrie.hw3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import static android.R.attr.strokeColor;
import static android.R.attr.x;
import static android.R.attr.y;
import static ravotta.carrie.hw3.R.dimen.shapeSize;

/**
 * Copied from https://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
 * from user https://stackoverflow.com/users/2850651/mike-m
 */

public class PixelGridView extends View {
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private boolean[][] cellChecked;
    private Thing[][] things;
    private int shapePadding;

    // four shapes
    private ShapeDrawable starDrawable;
    private ShapeDrawable heartDrawable;
    private Drawable squareDrawable;
    private Drawable circleDrawable;

    // reusable values for drawing rectangle
    private int right = 0;
    private int left = 0;
    private int top = 0;
    private int bottom = 0;

    // score
    private Score score;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        shapePadding = (int) getResources().getDimension(R.dimen.shape_padding);

        float strokeWidth = getResources().getDimension(R.dimen.strokeWidth);
        ColorStateList strokeColor = getResources().getColorStateList(R.color.stroke);
        int triangleFillColor = getResources().getColor(R.color.triangleColor);

        squareDrawable = getResources().getDrawable(R.drawable.square);
        circleDrawable = getResources().getDrawable(R.drawable.circle);
        heartDrawable = createHeart((int) strokeWidth, triangleFillColor, strokeColor);
        starDrawable = createStar((int) strokeWidth, triangleFillColor, strokeColor);

        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        score = new Score(context);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        cellChecked = new boolean[numColumns][numRows];

        things = new Thing[numRows][numColumns];

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);

        Drawable drawableToUse = null;
        int size = (int) shapeSize;
        Random random = new Random();

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (i != 0 && i != numColumns-1 && j != 0 && j != numRows-1) {
                    //if (cellChecked[i][j])
                    int r = random.nextInt(4);
                    Thing thing = new Thing(Thing.Type.Blank, null);

                    switch (r) {
                        case 0:
                            drawableToUse = squareDrawable;
                            thing.setType(Thing.Type.Square);
                            break;
                        case 1:
                            drawableToUse = circleDrawable;
                            thing.setType(Thing.Type.Circle);
                            break;
                        case 2:
                            drawableToUse = heartDrawable;
                            thing.setType(Thing.Type.Heart);
                            break;
                        case 3:
                            drawableToUse = starDrawable;
                            thing.setType(Thing.Type.Star);
                            break;
                    }

                    //thing.setBounds(new Rect(size * j, size * i, size + (size * j), size + (size * i)));
//                    left = i * cellWidth;
//                    top = j * cellHeight;
//                    right = (i + 1) * cellWidth;
//                    bottom = (j + 1) * cellHeight;
                    left = (i * cellWidth) + shapePadding;
                    top = (j * cellHeight) + shapePadding;
                    right = ((i + 1) * cellWidth) - shapePadding;
                    bottom = ((j + 1) * cellHeight) - shapePadding;
                    System.out.println(left + " | " + top + " | " + right + " | " + bottom + " = " + shapePadding);
                    // l, t, r, b
                    thing.setBounds(new Rect(left, top, right, bottom));
                    drawableToUse.setBounds(thing.getBounds());
                    drawableToUse.draw(canvas);
                    //System.out.println(thing.getType());
                    things[i][j] = thing;
                }
            }
        }

//        String text = getResources().getString(R.string.score) + ": " + score;
//
//        TextPaint textPaint = new TextPaint();
//        textPaint.setAntiAlias(true);
//        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
//        textPaint.setColor(Color.BLACK);
//        int textWidth = (int) textPaint.measureText(text);

            //canvas.drawText(text, (width/2) - (textWidth/2), cellHeight/2, textPaint);
//        StaticLayout staticLayout = new StaticLayout(text, textPaint, (int) width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        //staticLayout.draw(canvas);

//        for (int i = 1; i < numColumns; i++) {
//            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
//        }
//
//        for (int i = 1; i < numRows; i++) {
//            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            cellChecked[column][row] = !cellChecked[column][row];
            //System.out.println(column + " - " + row);
            //invalidate();
        }

        return true;
    }


    private ShapeDrawable createStar(int strokeWidth, int triangleFillColor, ColorStateList strokeColor) {
        final Star star = new Star(strokeWidth, triangleFillColor, strokeColor);
        ShapeDrawable shapeDrawable = new ShapeDrawable(star) {
            @Override
            protected boolean onStateChange(int[] stateSet) {
                star.setState(stateSet);
                return super.onStateChange(stateSet);
            }

            @Override
            public boolean isStateful() {
                return true;
            }
        };
        shapeDrawable.setIntrinsicHeight((int) shapeSize);
        shapeDrawable.setIntrinsicWidth((int) shapeSize);
        shapeDrawable.setBounds(0, 0, (int) shapeSize, (int) shapeSize);
        return shapeDrawable;
    }

    private ShapeDrawable createHeart(int strokeWidth, int triangleFillColor, ColorStateList strokeColor) {
        final Heart heart = new Heart(strokeWidth, triangleFillColor, strokeColor);
        ShapeDrawable shapeDrawable = new ShapeDrawable(heart) {
            @Override
            protected boolean onStateChange(int[] stateSet) {
                heart.setState(stateSet);
                return super.onStateChange(stateSet);
            }

            @Override
            public boolean isStateful() {
                return true;
            }
        };
        shapeDrawable.setIntrinsicHeight((int) shapeSize);
        shapeDrawable.setIntrinsicWidth((int) shapeSize);
        shapeDrawable.setBounds(0, 0, (int) shapeSize, (int) shapeSize);
        return shapeDrawable;
    }

}
