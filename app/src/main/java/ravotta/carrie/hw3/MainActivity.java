package ravotta.carrie.hw3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // game square size
    private int GAME_BOARD_SIZE = 10;
    // four shapes
    private ShapeDrawable starDrawable;
    private ShapeDrawable heartDrawable;
    private Drawable squareDrawable;
    private Drawable circleDrawable;
    private float shapeSize;
    private float lineWidth;
    private int lineColor;
    private DrawingArea drawingArea;
    private Thing tappedThing = null;
    private Thing[][] tiles = new Thing[GAME_BOARD_SIZE][GAME_BOARD_SIZE];
    private boolean redraw = true;

    private DrawingBoard drawingBoard;

    private enum Mode {
        AddSquare, AddCircle, AddTriangle, Select
    }

    private Mode mode = null;
    private volatile boolean blink = false;

    private Runnable blinker = new Runnable() {
        @Override
        public void run() {
            try {
                blink = true;
                drawingArea.postInvalidate();
                Thread.sleep(250);
                blink = false;
                drawingArea.postInvalidate();
                Thread.sleep(250);
                blink = true;
                drawingArea.postInvalidate();
                Thread.sleep(250);
                blink = false;
                drawingArea.postInvalidate();
                tappedThing = null;
            } catch (InterruptedException e) {
                blink = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        float strokeWidth = getResources().getDimension(R.dimen.strokeWidth);
        int triangleFillColor = getResources().getColor(R.color.triangleColor);

        // get window size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        float w = screenWidth / GAME_BOARD_SIZE;
        float h = screenHeight / GAME_BOARD_SIZE;

        shapeSize = w;
        ColorStateList strokeColor = getResources().getColorStateList(R.color.stroke);

        squareDrawable = getResources().getDrawable(R.drawable.square);
        circleDrawable = getResources().getDrawable(R.drawable.circle);
        heartDrawable = createHeart((int) strokeWidth, triangleFillColor, strokeColor);
        starDrawable = createStar((int) strokeWidth, triangleFillColor, strokeColor);

        lineColor = getResources().getColor(R.color.lineColor);
        lineWidth = getResources().getDimension(R.dimen.lineWidth);

        LinearLayout mainLayout = new LinearLayout(MainActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //params.setMargins(20, 20, 20, 20);
        mainLayout.setLayoutParams(params);
        setContentView(mainLayout);

        drawingArea = new DrawingArea(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        drawingArea.setLayoutParams(layoutParams);

        assert mainLayout != null;

        //mainLayout.addView(drawingArea);

        PixelGridView pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(GAME_BOARD_SIZE);
        pixelGrid.setNumRows(GAME_BOARD_SIZE);

        setContentView(pixelGrid);
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

    private class DrawingBoard extends LinearLayout {
        private Context context;
        private Button plus;

        public DrawingBoard(Context context) {
            super(context);
            init(context);
        }
        public DrawingBoard(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }
        private void init(Context context) {
            this.context = context;
            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0);
            plus = new Button(context);
            plus.setText("Score: 00");
            plus.setLayoutParams(params);
            setOrientation(LinearLayout.VERTICAL);

            addView(plus);
        }
    }

    private class DrawingArea extends View {
        private List<Thing> things = new ArrayList<>();
        private Thing selectedThing = null;
        private Paint linePaint = new Paint();
        private Random random = new Random();
        int actionX, actionY;

        public DrawingArea(Context context) {
            super(context);
            linePaint.setColor(lineColor);
            linePaint.setStrokeWidth(lineWidth);
            linePaint.setStyle(Paint.Style.STROKE);
        }

        private Thing findThingAt(int x, int y) {
            for (int i = things.size() - 1; i >= 0; i--) {
                Thing thing = things.get(i);
                if (thing.getBounds().contains(x, y)) {
                    return thing;
                }
            }
            return null;
        }

        private Rect thingBounds(int x, int y, int size) {
            int halfSize = size / 2;
            return new Rect(x - halfSize, y - halfSize, x + halfSize, y + halfSize);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Thing thing = findThingAt(actionX, actionY);
                    System.out.println(thing.getType());
                    // AddSquare, AddCircle, AddTriangle, Select
//                    switch(mode) {
//                        case AddSquare:
//                            things.add(new Thing(Thing.Type.Square,
//                                    thingBounds((int)event.getX(), (int)event.getY(), (int)shapeSize)));
//                            break;
//                        case AddCircle:
//                            things.add(new Thing(Thing.Type.Circle,
//                                    thingBounds((int)event.getX(), (int)event.getY(), (int)shapeSize)));
//                            break;
//                        case AddTriangle:
//                            things.add(new Thing(Thing.Type.Heart,
//                                    thingBounds((int)event.getX(), (int)event.getY(), (int)shapeSize)));
//                            break;
//                        case Select:
//                            selectedThing = findThingAt((int) event.getX(), (int) event.getY());
//                            if(selectedThing != null) {
//                                tappedThing = selectedThing;
//                                things.remove(selectedThing);
//                                things.add(selectedThing);
//                                new Thread(blinker).start();
//                            }
//                            break;
//                    }
                    invalidate();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (selectedThing != null) {
                        selectedThing.setBounds(thingBounds((int) event.getX(), (int) event.getY(), (int) shapeSize));
                    }
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    selectedThing = null;
                    invalidate();
                    return true;
            }
            //return super.onTouchEvent(event);
            return false;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Drawable drawableToUse = null;
            int size = (int) shapeSize;

            // draw game board as 8x8 tiles
            if (redraw) {
                for (int i = 0; i < GAME_BOARD_SIZE; i++) {
                    for (int j = 0; j < GAME_BOARD_SIZE; j++) {
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

                        thing.setBounds(new Rect(size * j, size * i, size + (size * j), size + (size * i)));
                        drawableToUse.setBounds(thing.getBounds());
                        drawableToUse.draw(canvas);

                        things.add(thing);
                    }
                }
                redraw = false;
            }

            for (Thing thing : things) {
                switch (thing.getType()) {
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

//                if (thing == selectedThing || (blink && tappedThing != null && thing.getType() == tappedThing.getType())) {
//                    drawableToUse.setState(selectedState);
//                } else {
//                    drawableToUse.setState(unselectedState);
//                }

                drawableToUse.setBounds(thing.getBounds());
                drawableToUse.draw(canvas);
            }
        }
    }

    private static final int[] selectedState = {android.R.attr.state_selected};
    private static final int[] unselectedState = {};
}
