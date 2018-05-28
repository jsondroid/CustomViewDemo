package skinsenor.jcgf.com.customviewdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wenbaohe on 2018/5/28.
 */

public class CircChartView extends ViewGroup {

    public CircChartView(Context context) {
        super(context);
        init();
    }

    public CircChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int dp2px(float dpValue) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
        return px;
    }


    private Paint cirlPaint;//画圆形的画笔
    private Paint scalePaint;//画刻度的画笔
    private Paint coverPaint;//画覆盖层的画笔
    private Paint linePaint;//画半径线的画笔
    private Paint textPaint;//

    private void init() {
        cirlPaint = new Paint();
        cirlPaint.setAntiAlias(true);
        cirlPaint.setStyle(Paint.Style.FILL);
        cirlPaint.setColor(Color.WHITE);

        scalePaint = new Paint();
        scalePaint.setAntiAlias(true);
        scalePaint.setStyle(Paint.Style.FILL);
        scalePaint.setColor(Color.BLACK);

        coverPaint = new Paint();
        coverPaint.setAntiAlias(true);
        coverPaint.setStyle(Paint.Style.FILL);
        coverPaint.setColor(Color.GRAY);
        coverPaint.setAlpha(50);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.GRAY);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.GRAY);

        colors = new int[]{Color.parseColor("#FFC01CCF"), Color.GRAY, Color.GRAY};

    }

    private float centerX;
    private float centerY;
    private float radius;
    private float scaleLeng = 0;//刻度值的长度
    private int rotate = 0;//旋转角度

    private float maxPer = 100;//最大百分比（100分之一）
    private int CountY = 3;//Y轴个数（即圆分3等分）
    private int CountX = 6;//X轴个数（即圆的半径分6等分）
    private ArrayList<Integer> datas;//数据源
    private ArrayList<String> datalables;//数据源文字
    private int[] colors;//数据源文字
    private Path path;//绘制覆盖层


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = (Math.min(w, h)) / 3;
        scaleLeng = dp2px(3);
    }


    /**
     * 自定义布局的使用
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawCirl(canvas);
        drawLine(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        CountY = getChildCount();
        drawText();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
    }

    /**
     * 自定义view的使用
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 画底部的圆
     */
    private void drawCirl(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, cirlPaint);
    }

    /**
     * 画半径直线
     */
    private void drawLine(Canvas canvas) {

        int diress = 360 / CountY;
        path = new Path();
        for (int i = 0; i < CountY; i++) {
            /**计算圆上的坐标点*/
            int angle = (i * diress + rotate);
            double rpi = 2 * Math.PI / 360 * angle;
            float x = (float) (centerX + Math.sin(rpi) * radius);
            float y = (float) (centerY - Math.cos(rpi) * radius);
            canvas.drawLine(centerX, centerY, x, y, linePaint);

//            /**覆盖层计算*/
//            float rp = (new Random().nextInt(100));
//            /**绘制刻度*/
//            drawScale(canvas, i * diress);
//
//            float x1 = (float) (centerX + Math.sin(rpi) * (radius / maxPer * rp));
//            float y1 = (float) (centerY - Math.cos(rpi) * (radius / maxPer * rp));
//            if (i == 0) {
//                path.moveTo(x1, y1);
//            }
//            path.lineTo(x1, y1);

            /**绘制刻度*/
            drawScale(canvas, i * diress);
            if (datas != null && !datas.isEmpty()) {
                float data = datas.get(i);
                float x1 = (float) (centerX + Math.sin(rpi) * (radius / maxPer * data));
                float y1 = (float) (centerY - Math.cos(rpi) * (radius / maxPer * data));
                if (i == 0) {
                    path.moveTo(x1, y1);
                }
                path.lineTo(x1, y1);
            }


        }

//        path.close();
//        canvas.drawPath(path, coverPaint);

        if (datas != null && !datas.isEmpty()) {
            path.close();
            canvas.drawPath(path, coverPaint);
        }
    }

    /**
     * 画半径上的刻度
     * distance: 那一条半径的度数
     */
    private void drawScale(Canvas canvas, float distance) {
        float xper = radius / CountX;//将半径分CountX等分
        for (int i = 0; i < CountX; i++) {
            float f = xper * i;//每等分的长度
            float nrpi = (float) Math.atan(scaleLeng / f);

            /**分段的点*/
            double lrpi = 2 * Math.PI / 360 * ((distance) + rotate);

            float nx = (float) (centerX + Math.sin(lrpi + nrpi) * (xper * i));
            float ny = (float) (centerY - Math.cos(lrpi + nrpi) * (xper * i));

            float nx1 = (float) (centerX + Math.sin(lrpi - nrpi) * (xper * i));
            float ny1 = (float) (centerY - Math.cos(lrpi - nrpi) * (xper * i));
            canvas.drawLine(nx, ny, nx1, ny1, scalePaint);
        }
    }

    private int left = 0;

    private int rigth = 0;

    private int top = 0;

    private int bottom = 0;

    private void drawText() {
        int diress = 360 / CountY;
        for (int i = 0; i < CountY; i++) {
            /**计算圆上的坐标点*/
            int angle = (i * diress + rotate);
            double rpi = 2 * Math.PI / 360 * angle;
            float x = (float) (centerX + Math.sin(rpi) * radius);
            float y = (float) (centerY - Math.cos(rpi) * radius);

            TextView textView = (TextView) getChildAt(i);
            if (textView != null && (datalables != null && !datalables.isEmpty() && datas != null && !datas.isEmpty())) {
                textView.setText(setTextstyle1(colors[i], datalables.get(i), String.valueOf(datas.get(i))));
                float textw = radius * 0.8f;
                float texth = radius / 3;

                if (angle == 0 || (angle > 270 && angle < 90)) {
                    left = (int) (x - textw / 2);

                    rigth = (int) (left + textw);

                    top = (int) (y - texth);

                    bottom = (int) (top + texth);
                }

                if (angle == 120 || (angle > 90 && angle < 180)) {
                    left = (int) (x - textw / 2);

                    rigth = (int) (left + textw);

                    top = (int) (y + texth * 0.8f);

                    bottom = (int) (top + texth);
                }


                if (angle == 240 || (angle > 180 && angle < 270)) {
                    left = (int) (x - textw / 2);

                    rigth = (int) (left + textw);

                    top = (int) (y + texth * 0.8f);
                    bottom = (int) (top + texth);
                }

                textView.layout(left, top, rigth, bottom);
            }

        }


    }
    /**
     * 设置文字样式
     */
    protected Spannable setTextstyle1(int color, String count1, String count2) {
        String mtext = count1 + count2;
        Spannable span = new SpannableString(mtext);
        span.setSpan(new RelativeSizeSpan(2), count1.length(), count1.length() + count2.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.5f), mtext.length() - 1, mtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(color), 2, count1.length() + count2.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(color), mtext.length() - count2.length(), mtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public void setDatas(ArrayList<Integer> datas) {
        this.datas = datas;
    }

    public void setDatalables(ArrayList<String> datalables) {
        this.datalables = datalables;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setMaxPer(float maxPer) {
        this.maxPer = maxPer;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setCountY(int countY) {
        CountY = countY;
    }

    public void setCountX(int countX) {
        CountX = countX;
    }
}
