package lav.isu.ru.setgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public  class CardView extends View {

    GameActivity.Card card;
    public CardView(Context context) {
        super(context);
    }

    public CardView(Context context, GameActivity.Card card) {
        super(context);
        this.card = card;
    }

    public CardView(Context context, AttributeSet attrs, GameActivity.Card card) {
        super(context, attrs);
        this.card = card;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        p.setColor(Color.rgb(252, 239, 186));
        canvas.drawRoundRect(10,10, canvas.getWidth()-10, canvas.getHeight()-10, 5,5,p);
        if (card.color==1) p.setColor(Color.RED);
        else if (card.color==2) p.setColor(Color.BLUE);
        else if (card.color==3) p.setColor(Color.GREEN);
        int r = 0;
        r = (card.fill==1) ? 1 : ((card.fill==2) ? 2 : 3);

        p.setStyle(Paint.Style.FILL);
        for (int i=0; i<card.count; i++) {
            if (card.shape==1)
            canvas.drawCircle(canvas.getWidth()/2,
                    (i+1)*canvas.getHeight()/(card.count+1), 10*r, p);
            else if (card.shape==2)
                canvas.drawRect(canvas.getWidth()/2-10*r,
                        (i+1)*canvas.getHeight()/(card.count+1)-10*r, canvas.getWidth()/2+10*r,
                        (i+1)*canvas.getHeight()/(card.count+1)+10*r, p);
            else if (card.shape==3)
                canvas.drawOval(canvas.getWidth()/2-15*r,
                        (i+1)*canvas.getHeight()/(card.count+1)-10*r, canvas.getWidth()/2+15*r,
                        (i+1)*canvas.getHeight()/(card.count+1)+10*r, p);
        }

    }


}
