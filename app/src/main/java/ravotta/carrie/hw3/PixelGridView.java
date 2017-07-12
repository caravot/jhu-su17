package ravotta.carrie.hw3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import static android.R.attr.width;
import static ravotta.carrie.hw3.R.dimen.shapeSize;
import static ravotta.carrie.hw3.Thing.Type.Circle;
import static ravotta.carrie.hw3.Thing.Type.Square;

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
    private Thing selectedThing;
    private boolean redraw = true;

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

    // when moving save start state
    int initialColumn = -1;
    int initialRow = -1;

    // score
    private int score = 0;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        shapePadding = (int) getResources().getDimension(R.dimen.shape_padding);

        float strokeWidth = getResources().getDimension(R.dimen.strokeWidth);
        ColorStateList strokeColor = getResources().getColorStateList(R.color.stroke);
        int triangleFillColor = getResources().getColor(R.color.triangleColor);
        int starFillColor = getResources().getColor(R.color.starColor);

        squareDrawable = getResources().getDrawable(R.drawable.square);
        circleDrawable = getResources().getDrawable(R.drawable.circle);
        heartDrawable = createHeart((int) strokeWidth, triangleFillColor, strokeColor);
        starDrawable = createStar((int) strokeWidth, starFillColor, strokeColor);

        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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

        // add score
        drawScore(canvas);

        if (!redraw) {
            redrawBoard(canvas);
            return;
        }

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        redraw = false;

        // initialize board drawing
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                Thing thing = new Thing(Thing.Type.Blank, null);
                if (i != 0 && i != numColumns-1 && j != 0 && j != numRows-1) {
                    int r = random.nextInt(4);

                    switch (r) {
                        case 0:
                            drawableToUse = squareDrawable;
                            thing.setType(Square);
                            break;
                        case 1:
                            drawableToUse = circleDrawable;
                            thing.setType(Circle);
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

                    left = (i * cellWidth) + shapePadding;
                    top = (j * cellHeight) + shapePadding;
                    right = ((i + 1) * cellWidth) - shapePadding;
                    bottom = ((j + 1) * cellHeight) - shapePadding;

                    thing.setBounds(new Rect(left, top, right, bottom));
                    drawableToUse.setBounds(thing.getBounds());
                    drawableToUse.draw(canvas);
                }

                things[i][j] = thing;
            }
        }
    }

    public void drawScore(Canvas canvas) {
        String text = getResources().getString(R.string.score) + ": " + score;

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.BLACK);
        int textWidth = (int) textPaint.measureText(text);

        canvas.drawText(text, (width/2) - (textWidth/2), cellHeight/2, textPaint);
    }

    public void redrawBoard(Canvas canvas) {
        Drawable drawableToUse = null;

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (i != 0 && i != numColumns-1 && j != 0 && j != numRows-1) {
                    Thing thing = things[i][j];
                    drawableToUse = getDrawable(thing.getType());
                    drawableToUse.setBounds(thing.getBounds());
                    drawableToUse.draw(canvas);
                }
            }
        }
    }

    public boolean checkForMatches() {
        // if no match move things back to original view
        return false;
    }

    public void swapThings(Thing original, Thing target) {
//        Thing.Type targetType = things[x][y].getType();
//        Thing.Type originalType = things[initialRow][initialColumn].getType();

        // if row doesn't change, only swap if moving +/- one column
        // if column doesn't change, only swap if moving +/- one row
//        if ((x == initialRow && (y - 1 == initialColumn || y + 1 == initialColumn))
//                || (y == initialColumn && (x - 1 == initialRow || x + 1 == initialRow))
//                && (targetType != originalType)) {
            Thing targetClone = target;
            System.out.println("Original: " + original.getType() + " Target: " + targetClone.getType());
            // target
            target.setType(original.getType());

            // original
            original.setType(targetClone.getType());
//        }
    }

    public Drawable getDrawable(Thing.Type type) {
        Drawable drawableToUse = null;

        switch (type) {
            case Square:
                drawableToUse = squareDrawable;
                break;
            case Circle:
                drawableToUse = circleDrawable;
                break;
            case Heart:
                drawableToUse = heartDrawable;
                break;
            case Star:
                drawableToUse = starDrawable;
                break;
        }
        return drawableToUse;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)(event.getX() / cellWidth);
        int y = (int)(event.getY() / cellHeight);

        Thing target = things[x][y];

        if (selectedThing == null) {
            System.out.println("Init selectedThing");
            initialRow = (int)(event.getX() / cellWidth);
            initialColumn = (int)(event.getY() / cellHeight);
            selectedThing = things[x][y];
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//            cellChecked[column][row] = !cellChecked[column][row];
                return true;
            case MotionEvent.ACTION_MOVE:
                if (selectedThing != null) {
                    //System.out.println(x + " - " + initialRow + " : " + y + " - " + initialColumn);
                    // if row doesn't change, only swap if moving +/- one column
                    // if column doesn't change, only swap if moving +/- one row
                    if ((x == initialRow && (y - 1 == initialColumn || y + 1 == initialColumn))
                            || (y == initialColumn && (x - 1 == initialRow || x + 1 == initialRow))
                            && (target.getType() != selectedThing.getType())) {
                        swapThings(selectedThing, target);
                        invalidate();
                    }
//                    System.out.println("FROM: " + initialRow + " - " + initialColumn + " TO: " + row + " - " + column);
//                    swapThings(row, column);
//                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (!checkForMatches()) {
                    System.out.println("Swapping back");
                    swapThings(target, selectedThing);
                }

                selectedThing = null;
                initialColumn = -1;
                initialRow = -1;

                invalidate();
                return true;
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
