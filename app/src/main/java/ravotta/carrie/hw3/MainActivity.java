package ravotta.carrie.hw3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static ravotta.carrie.hw3.R.dimen.shapeSize;
import static ravotta.carrie.hw3.Thing.Type.Blank;
import static ravotta.carrie.hw3.Thing.Type.Circle;
import static ravotta.carrie.hw3.Thing.Type.Square;

public class MainActivity extends AppCompatActivity {
    // game square size
    private int GAME_BOARD_SIZE = 10;

    // blink shape
    private volatile boolean blink = false;

    // tapped item
    private Thing tappedThing = null;

    // main board
    GameBoard gameBoard;

    private Runnable blinker = new Runnable() {
        @Override
        public void run() {
            try {
                blink = true;
                gameBoard.postInvalidate();
                Thread.sleep(425);
                blink = false;
                gameBoard.postInvalidate();
                Thread.sleep(425);
                blink = true;
                gameBoard.postInvalidate();
                Thread.sleep(425);
                blink = false;
                gameBoard.postInvalidate();
                tappedThing = null;
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                blink = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameBoard = new GameBoard(this);
        gameBoard.setNumColumns(GAME_BOARD_SIZE);
        gameBoard.setNumRows(GAME_BOARD_SIZE);

        setContentView(gameBoard);
    }

    /**
     * Copied from https://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
     * from user https://stackoverflow.com/users/2850651/mike-m
     */
    public class GameBoard extends View {
        private int numColumns, numRows;
        private int cellWidth, cellHeight;
        private Paint blackPaint = new Paint();
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

        // game paused
        private boolean gamePaused = false;

        public GameBoard(Context context) {
            this(context, null);
        }

        public GameBoard(Context context, AttributeSet attrs) {
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

            things = new Thing[numRows][numColumns];

            invalidate();
        }

        private int calculateDistance(int i) {
            int density  = (int) getResources().getDisplayMetrics().density;
            //System.out.println(density);
            return density * i;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            Drawable drawableToUse = null;
            Random random = new Random();

            // game paused, do not draw the board
            if (gamePaused) {
                drawPause(canvas);
                return;
            }

            // add score
            drawScore(canvas);

            if (!redraw) {
                redrawBoard(canvas);
                return;
            }

            // do nothing if the board is not sized correctly
            if (numColumns == 0 || numRows == 0) {
                return;
            }

            redraw = false;

            // initialize board drawing
            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    Thing thing = new Thing(Thing.Type.Blank, null);

                    if (i != 0 && i != numColumns - 1 && j != 0 && j != numRows - 1) {
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

            checkForMatches();

            // matches found so redraw board until there are none
            if (score > 0) {
                redraw = true;
                score = 0;
            }

            invalidate();
        }

        // draw the paused text in the center of the screen
        public void drawPause(Canvas canvas) {
            String text = getResources().getString(R.string.pause);

            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(32 * getResources().getDisplayMetrics().density);
            textPaint.setColor(Color.BLACK);
            int textWidth = (int) textPaint.measureText(text);

            canvas.drawText(text, (getWidth()/2) - (textWidth/2), getHeight()/2, textPaint);
        }

        // draw the score centered in the first row on the grid
        public void drawScore(Canvas canvas) {
            String text = getResources().getString(R.string.score) + ": " + score;

            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
            textPaint.setColor(Color.BLACK);
            int textWidth = (int) textPaint.measureText(text);

            canvas.drawText(text, (getWidth()/2) - (textWidth/2), cellHeight/2, textPaint);
        }

        // blanks the screen and redraws objects already in place
        public void redrawBoard(Canvas canvas) {
            Drawable drawableToUse;

            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (i != 0 && i != numColumns-1 && j != 0 && j != numRows-1) {
                        Thing thing = things[i][j];
                        // do not draw blanks
                        if (thing.getType() == Blank) {
                            break;
                        }

                        // do not show items that are hidden and are blank types
                        if (thing.isShown()) {
                            drawableToUse = getDrawable(thing.getType());

                            if ((blink && tappedThing != null && thing.getType() == tappedThing.getType())) {
                                drawableToUse.setState(selectedState);
                            } else {
                                drawableToUse.setState(unselectedState);
                            }

                            drawableToUse.setBounds(thing.getBounds());
                            drawableToUse.draw(canvas);
                        }
                        // blink/highlight matches until removal
//                        else {
//                            new Thread(blinker).start();
//
//                            synchronized (lock) {
//                                drawableToUse = getDrawable(thing.getType());
//
//                                if (blink) {
//                                    drawableToUse.setState(selectedState);
//                                } else {
//                                    drawableToUse.setState(unselectedState);
//                                    thing.setType(Blank);
//                                }
//
//                                drawableToUse.setBounds(thing.getBounds());
//                                drawableToUse.draw(canvas);
//
//                                gameBoard.postInvalidate();
//                            }
//                        }
                    }
                }
            }
        }

        // checks for any vertical or horizontal matches of 3 shapes
        // then adds to the total score
        public boolean checkForMatches() {
            Thing one;
            Thing two;
            Thing three;
            int numberOfMatches = 0;

            // find vertical matches
            for (int i = 1; i < (getNumRows() - 1); i++) {
                for (int j = 2; j < (getNumColumns() - 1); j++) {
                    one = things[i][j - 1];
                    two = things[i][j];
                    three = things[i][j + 1];

                    if (one.getType() == two.getType() && two.getType() == three.getType()
                            && one.isShown() && two.isShown() && three.isShown()) {
                        numberOfMatches++;

                        // hide shapes
                        one.setShown(false);
                        two.setShown(false);
                        three.setShown(false);
                    }
                }
            }

            // find horizontal matches
            for (int i = 2; i < (getNumRows() - 1); i++) {
                for (int j = 2; j < (getNumColumns() - 1); j++) {
                    one = things[i-1][j];
                    two = things[i][j];
                    three = things[i+1][j];

                    if (one.getType() == two.getType() && two.getType() == three.getType()
                            && one.isShown() && two.isShown() && three.isShown()) {
                        numberOfMatches++;

                        // hide shapes
                        one.setShown(false);
                        two.setShown(false);
                        three.setShown(false);
                    }
                }
            }

            // add to users score
            score += numberOfMatches;

            // drop shapes down to fill in removed matches
            //fillInGaps();

            // returns if matches were found
            return numberOfMatches == score;
        }

        // draw visible shapes into lowest row in their column
        public void fillInGaps() {
            // make a copy of the original grid
            //Thing[][] originalThings = things;
            ArrayList<Thing> newThings = new ArrayList<Thing>();

            for (int i = numColumns - 2; i > 0; i--) {
                ArrayList<Thing[]> arrayList = new ArrayList<Thing[]>();

                for (int j = numRows - 2; j > 0; j--) {
                    Thing originalThing = things[i][j];
                    // if thing is hidden move items down
                    if (!originalThing.isShown()) {
                        //newThings[i] = things[j][i];
                        //thingsNew.remove(things[i][j]);
                    }
                }
            }

//        for (int i = numColumns - 2; i > 0; i--) {
//            for (int j = numRows - 2; j > 0; j--) {
//                Thing originalThing = newThings[i][j];
//                if (originalThing != null) {
//                } else {
//                }
//            }
//        }
        }

        // swap two shapes with each other
        public void swapThings(Thing original, Thing target) {
            Thing targetClone = new Thing(target.getType(), target.getBounds());
            Thing originalClone = new Thing(original.getType(), original.getBounds());

            // target
            target.setType(originalClone.getType());

            // original
            original.setType(targetClone.getType());
        }

        // check if coordinate is in the game's border
        public boolean isOnGameBorder(int x, int y) {
            return (x == 0 || y == 0 || x == (getNumRows() - 1) || (y == getNumColumns() - 1));
        }

        // outline shapes of the same type
        public void highlightSameShapes(Thing thing) {
            Drawable drawableToUse = getDrawable(thing.getType());

            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    Thing tmpThing = things[i][j];

                    // do not show items that are hidden
                    if (thing.isShown() && tmpThing.getType() == thing.getType()) {
                        drawableToUse.setState(selectedState);
                    }
                }
            }
        }

        // get the shape drawable
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

            // get slop padding
            int touch_slop = (int) getResources().getDimension(R.dimen.touch_slop);

            Thing target = things[x][y];

            // nothing is currently held as the selected shape
            if (selectedThing == null) {
                initialRow = (int)(event.getX() / cellWidth);
                initialColumn = (int)(event.getY() / cellHeight);
                selectedThing = things[x][y];
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // prevent touch slop issues
//                    if (calculateDistance(Math.abs(x-initialRow)) <= touch_slop || calculateDistance(y) <= touch_slop) {
//                        return true;
//                    }

                    // if thing dragged outside grid redraw grid and subtract 10 points from score
                    if (isOnGameBorder(x, y)) {
                        redraw = true;
                        score -= 10;
                        invalidate();
                    }

                    if (selectedThing != null) {
                        // if row doesn't change, only swap if moving +/- one column
                        // if column doesn't change, only swap if moving +/- one row
                        if (((x == initialRow && (y - 1 == initialColumn || y + 1 == initialColumn)) || (y == initialColumn && (x - 1 == initialRow || x + 1 == initialRow)))
                                && (target.getType() != selectedThing.getType())
                                && !selectedThing.equals(target)) {
                            swapThings(selectedThing, target);
                        }
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    // pause the game if user clicks the border
                    // un-pause the game if the user clicks anywhere and the game is already paused
                    if (isOnGameBorder(x, y) || gamePaused) {
                        gamePaused = !gamePaused;
                        invalidate();

                        selectedThing = null;
                        initialColumn = -1;
                        initialRow = -1;

                        return true;
                    }
                    // highlight shapes of same
                    else if (x == initialRow && y == initialColumn) {
                        tappedThing = selectedThing;
                        highlightSameShapes(tappedThing);

                        // clear out working variables
                        selectedThing = null;
                        initialColumn = -1;
                        initialRow = -1;

                        new Thread(blinker).start();
                    }

                    // if there are no matches or both items are the same shape swap back to original
                    // save the original score before redrawing and checking for matches
                    int originalScore = score;

                    checkForMatches();

                    // if no matches re-swap items
                    if (originalScore == score && selectedThing != null && target != null) {
                        swapThings(target, selectedThing);
                    }

                    // clear out working variables
                    selectedThing = null;
                    initialColumn = -1;
                    initialRow = -1;

                    // redraw the board
                    invalidate();

                    return true;
            }

            return true;
        }

        // create a star shape to draw onto the canvas
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

        // create a heart shape to draw onto the canvas
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

    private static final int[] selectedState = {android.R.attr.state_selected};
    private static final int[] unselectedState = {};
    private static Object lock = new Object();
}
