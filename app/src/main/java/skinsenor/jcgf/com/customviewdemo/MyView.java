package skinsenor.jcgf.com.customviewdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import java.util.Random;

/**
 * Created by wenbaohe on 2018/5/21.
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint paint;
    EmbossMaskFilter emboss;

    private void init() {
//        setLayerType(LAYER_TYPE_SOFTWARE, null);//硬件加速
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setTextSize(30);

        float[] direction = new float[]{1, 1, 1};
        // 设置环境光亮度
        float light = 0.1f;
        // 选择要应用的反射等级
        float specular = 8;
        // 向mask应用一定级别的模糊
        float blur = 10;
        emboss = new EmbossMaskFilter(direction, light, specular, blur);
//        paint.setMaskFilter(emboss);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx1 = getWidth() * 0.25f;
        cy1 = getHeight() / 2 + (getHeight() / 2) * 0.3f;

        cx2 = getWidth() * 0.75f;
//        cy2 = getHeight() / 2 - (getHeight() / 2) * 0.3f;

        cy2 = getHeight() / 2;

        ValueAnimator animat = ValueAnimator.ofFloat(0, cy2);
        animat.setDuration(2000);
        animat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                h3D = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animat.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw02(canvas);
    }

    /**
     * 绘制直线
     */
    int count = 3;
    float centerX = 0;
    float centerY = 0;
    float angle = 270;

    private void drawLines(Canvas canvas) {
        radius = getWidth() / 3;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        canvas.drawCircle(centerX, centerY, radius, paint);

        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.sin(angle * i));
            float y = (float) (centerY + radius * Math.cos(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, paint);
        }
    }

    private float w3D;
    private float h3D;
    private float cx3D;
    private float cy3D;

    private Path path_left;
    private Path path_rigth;
    private Path path_top;
    private Path path_botom;

    private void draw3D3(Canvas canvas) {
        w3D = getWidth() / 4;
//        h3D = getHeight() / 2;

        cx3D = getWidth() / 2;
        cy3D = getHeight() / 2;
        RectF rectF = new RectF();
        rectF.left = cx3D - w3D / 2;
        rectF.right = rectF.left + w3D;
        rectF.top = cy3D - h3D / 2;
        rectF.bottom = rectF.top + h3D;

        float wrt = rectF.width() * 0.68f;//右边顶部的角
        float wlb = rectF.width() * 0.32f;//左边底部的角
        double rpi = 2 * Math.PI / 360 * (88);//切斜度
        double lpi = 2 * Math.PI / 360 * (100);//切斜度

        /**计算得到右边顶点的坐标*/
        float rtx = (float) (rectF.left + Math.sin(rpi) * wrt);
        float rty = (float) (rectF.top - Math.cos(rpi) * wrt);
        /**计算得到左边底部顶点的坐标*/
        float ltx = (float) (rectF.left + Math.sin(lpi) * wlb);
        float lty = (float) (rectF.bottom - Math.cos(lpi) * wlb);

//        canvas.drawRect(rectF,paint);

        path_left = new Path();
        path_rigth = new Path();
        path_top = new Path();
        path_botom = new Path();

        /**绘制顶部*/
        path_top.moveTo(rectF.left, rectF.top);
        path_top.lineTo(rtx, rty);
        path_top.lineTo(rectF.right, rectF.top);
        path_top.lineTo(ltx, lty - rectF.height());
        path_top.close();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(220);
        canvas.drawPath(path_top, paint);


        /**绘制底部*/
        path_botom.moveTo(rectF.left, rectF.bottom);
        path_botom.lineTo(rtx, rty + rectF.height());
        path_botom.lineTo(rectF.right, rectF.bottom);
        path_botom.lineTo(ltx, lty);
        path_botom.close();

        paint.setColor(Color.RED);
        paint.setAlpha(180);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path_botom, paint);

        /**绘制左侧*/
        path_left.moveTo(rectF.left, rectF.top);
        path_left.lineTo(ltx, lty - rectF.height());
        path_left.lineTo(ltx, lty);
        path_left.lineTo(rectF.left, rectF.bottom);
        path_left.close();

        paint.setColor(Color.RED);
        paint.setAlpha(180);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path_left, paint);


        /**绘制右侧*/
        path_rigth.moveTo(ltx, lty - rectF.height());
        path_rigth.lineTo(rectF.right, rectF.top);
        path_rigth.lineTo(rectF.right, rectF.bottom);
        path_rigth.lineTo(ltx, lty);
        path_rigth.close();

        paint.setColor(Color.RED);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path_rigth, paint);

    }


    private void draw3D2(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
//        paint.setAlpha(180);
        RectF rectF = new RectF();
        rectF.left = getWidth() / 2 - 150;
        rectF.right = getWidth() / 2 + 150;
        rectF.top = getHeight() / 4;
        rectF.bottom = getHeight() - getHeight() / 4;
        canvas.drawRect(rectF, paint);

        float w = rectF.width() * 0.68f;
        double rpi = 2 * Math.PI / 360 * (80);
        float x = (float) (rectF.left + Math.sin(rpi) * w);
        float y = (float) (rectF.top - Math.cos(rpi) * w);

        double rpi2 = 2 * Math.PI / 360 * (110);
        float w2 = rectF.width() * 0.32f;
        float x2 = (float) (rectF.left + Math.sin(rpi2) * w2);
        float y2 = (float) (rectF.top - Math.cos(rpi2) * w2);

        double rpi3 = 2 * Math.PI / 360 * (110);
        float w3 = rectF.width() * 0.32f;
        float x3 = (float) (rectF.left + Math.sin(rpi3) * w3);
        float y3 = (float) (rectF.bottom - Math.cos(rpi3) * w3);


        /**绘制顶部*/
        path = new Path();
        path.moveTo(rectF.left, rectF.top);
        path.lineTo(x, y);
        path.lineTo(rectF.right, rectF.top);
        path.lineTo(x2, y2);
        path.close();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawPath(path, paint);

        Path path2 = new Path();
        path2.moveTo(x2, y2);
        path2.lineTo(rectF.right, rectF.top);
        path2.lineTo(rectF.right, rectF.bottom);
        path2.lineTo(x3, y3);
        path2.lineTo(x2, y2);
        path2.moveTo(rectF.left, rectF.bottom);
        path2.lineTo(x3, y3);
        canvas.drawPath(path2, paint);

    }

    /**
     * 绘制3d矩形
     */
    private void draw3D1(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setAlpha(180);
        RectF rectF = new RectF();
        rectF.left = getWidth() / 2 - 50;
        rectF.right = getWidth() / 2 + 50;
        rectF.top = getHeight() / 4;
        rectF.bottom = getHeight() - getHeight() / 4;
        canvas.drawRect(rectF, paint);


        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(220);
        double rpi = 2 * Math.PI / 360 * (70);

        float w = (rectF.width() / 2);
        float x = (float) (rectF.left + Math.sin(rpi) * w);
        float y = (float) (rectF.top - Math.cos(rpi) * w);

        float x2 = (float) (rectF.right + Math.sin(rpi) * w);
        float y2 = (float) (rectF.top - Math.cos(rpi) * w);

        float x3 = (float) (rectF.right + Math.sin(rpi) * w);
        float y3 = (float) (rectF.bottom - Math.cos(rpi) * w);

        path = new Path();
        path.moveTo(rectF.right, rectF.top);
        path.lineTo(rectF.left, rectF.top);
        path.lineTo(x, y);
        path.lineTo(x2, y2);
        path.close();

        canvas.drawPath(path, paint);

        Path path2 = new Path();

        path2.moveTo(rectF.right, rectF.top);
        path2.lineTo(x2, y2);
        path2.lineTo(x3, y3);
        path2.lineTo(rectF.right, rectF.bottom);
        path2.close();


        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(200);
        canvas.drawPath(path2, paint);

    }


    /**
     * 计算圆上坐标点的闭合线连接
     */
    private float radius;
    private float rcx, rcy;

    private int datasize = 3;
    private int rotate = 120;
    private float rp = 0.2f;//半径的百分比

    private Path path2;

    private void draw02(Canvas canvas) {
        float size = Math.min(getHeight(), getWidth());
        radius = size / 3;
        rcx = getWidth() / 2;
        rcy = getHeight() / 2;
        canvas.drawCircle(rcx, rcy, radius, paint);

        int diress = 360 / datasize;

        path2 = new Path();

        for (int i = 0; i < 3; i++) {
            /**计算圆上的坐标点*/
            double rpi = 2 * Math.PI / 360 * (i * diress + rotate);
            float x = (float) (rcx + Math.sin(rpi) * radius);
            float y = (float) (rcy - Math.cos(rpi) * radius);

            canvas.drawLine(rcx, rcy, x, y, paint);

            drawkedu(canvas, i * diress);

            rp = (new Random().nextInt(100));
            float x1 = (float) (rcx + Math.sin(rpi) * (radius / 100 * rp));
            float y1 = (float) (rcy - Math.cos(rpi) * (radius / 100 * rp));
            if (i == 0) {
                path2.moveTo(x1, y1);
            }
            path2.lineTo(x1, y1);
        }
        path2.close();
        canvas.drawPath(path2, paint);
    }

    private void drawkedu(Canvas canvas, int distance) {

       float per= radius/6f;
        for(int i=0;i<6;i++){
            float f = per*i;
            float leng = 10;
            float nrpi = (float) Math.atan(leng/f);

            /**分段的点*/
            double lrpi = 2 * Math.PI / 360 * ((distance) + rotate);
            float lx = (float) (rcx + Math.sin(lrpi) * ( per*i));
            float ly = (float) (rcy - Math.cos(lrpi) * (per*i));

//        double nrpi = 2 * Math.PI / 360 * ((distance - ff) + rotate);
            float nx = (float) (rcx + Math.sin(lrpi+nrpi) * (per*i));
            float ny = (float) (rcy - Math.cos(lrpi+nrpi) * (per*i));

//        double nrpi1 = 2 * Math.PI / 360 * ((distance + ff) + rotate);
            float nx1 = (float) (rcx + Math.sin(lrpi-nrpi) * (per*i));
            float ny1 = (float) (rcy - Math.cos(lrpi-nrpi) * (per*i));
            canvas.drawLine(nx, ny, nx1, ny1, paint);
        }





//        for (int i = 0; i < 6; i++) {
//            float ff = (float) Math.atan(f / leng);
//
//            /**分段的点*/
//            double lrpi = 2 * Math.PI / 360 * ((distance) + rotate);
//            float lx = (float) (rcx + Math.sin(lrpi) * (radius * (i * 0.2f)));
//            float ly = (float) (rcy - Math.cos(lrpi) * (radius * (i * 0.2f)));
//
//            double nrpi = 2 * Math.PI / 360 * ((distance - ff) + rotate);
//            float nx = (float) (rcx + Math.sin(nrpi) * (radius * (i * 0.2f)));
//            float ny = (float) (rcy - Math.cos(nrpi) * (radius * (i * 0.2f)));
//
//            double nrpi1 = 2 * Math.PI / 360 * ((distance + ff) + rotate);
//            float nx1 = (float) (rcx + Math.sin(nrpi1) * (radius * (i * 0.2f)));
//            float ny1 = (float) (rcy - Math.cos(nrpi1) * (radius * (i * 0.2f)));
//            canvas.drawLine(nx, ny, nx1, ny1, paint);
//
//        }

    }


    private Path patha;
    private Path patha1;

    private void draw01(Canvas canvas) {

        canvas.translate(getWidth() / 2, getHeight() / 2);
        patha = new Path();
        patha.addCircle(0, 0, getMeasuredWidth() / 3, Path.Direction.CW);
        canvas.drawPath(patha, paint);
        canvas.clipPath(patha);

        for (int i = 0; i < 120; i++) {
            canvas.rotate(3, 0, 0);
            canvas.drawLine(0, 0 - (getMeasuredWidth() / 3), 0, 0 - (getMeasuredWidth() / 3) * 0.9f, paint);
            if (i % 8 == 0) {
                canvas.drawLine(0, 0 - (getMeasuredWidth() / 3), 0, 0, paint);
                float p = (getMeasuredWidth() / 3) / 5;
                for (int j = 1; j <= 5; j++) {
                    float jp = j * p;
                    float cy = jp - (getMeasuredWidth() / 3);
                    canvas.drawLine(-10, cy, 10, cy, paint);

                    patha1 = new Path();
                    patha1.lineTo(0, cy);
                    patha1.close();
                    canvas.drawPath(patha1, paint);
                }
                canvas.drawText("" + i, 0, 0 - (getMeasuredWidth() / 3) * 0.9f + 40, paint);
            }
        }
    }


    private Path path;
    private float cx1 = 0;
    private float cx2 = 0;
    private float cy1 = 0;
    private float cy2 = 0;

    private boolean isp = false;

    private float pos[] = new float[2];
    private float tan[] = new float[2];

    private float distance = 0;

    private void drawp(Canvas canvas) {


        path = new Path();
        path.moveTo(40, (getHeight() / 2) - 40);
        path.cubicTo(cx1, cy1 - 40, cx2, cy2 - 40, getWidth() - 40, (getHeight() / 2) - 40);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawPath(path, paint);

        Path path2 = new Path();
        path2.moveTo(40, getHeight() / 2);
        path2.cubicTo(cx1, cy1, cx2, cy2, getWidth() - 40, getHeight() / 2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawPath(path2, paint);

        PathMeasure measure = new PathMeasure(path, false);
        float length = distance * measure.getLength();
        measure.getPosTan(length, pos, tan);

        canvas.translate(pos[0], pos[1]);
        //获取旋转角度
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        canvas.rotate(degree);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawRect(new RectF(-20, -20, 20, 20), paint);

        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (distance <= 1) {
            distance += 0.01f;
        } else {
            distance = 0;
        }

    }


    /**
     * 绘制水珠
     */
    private Path pathdrops;
    private float p = 0.3f;

    private void drawPathDrops(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);//移动原点坐标到View中心点

        pathdrops = new Path();
        pathdrops.moveTo(0, -140);
        pathdrops.cubicTo(-230, 140, 230, 140, 0, -140);

        Path dst = new Path();
        PathMeasure measure = new PathMeasure(pathdrops, true);
        measure.getSegment((measure.getLength() * 0.5f) - (measure.getLength() * 0.5f) * p, measure.getLength() * 0.5f + (measure.getLength() * 0.5f) * p, dst, true);

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(dst, paint);

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(pathdrops, paint);


        Path path = new Path();
        path.addCircle(0, 0, 100, Path.Direction.CW);
        canvas.clipPath(path);
    }
}
