package ravotta.carrie.hw3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Score extends View {
    // score
    private int score = 0;
    private String scoreText = "";

    public Score(Context context) {
        this(context, null);

        scoreText = getResources().getString(R.string.score);
    }

    public Score(Context context, AttributeSet attrs) {
        super(context, attrs);

        scoreText = getResources().getString(R.string.score);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        String text = scoreText + ": " + score;

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.BLACK);
        int textWidth = (int) textPaint.measureText(text);

        canvas.drawText(text, (width/2) - (textWidth/2), 15, textPaint);
    }
}
