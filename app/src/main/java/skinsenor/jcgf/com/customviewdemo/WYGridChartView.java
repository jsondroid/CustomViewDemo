package skinsenor.jcgf.com.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by wenbaohe on 2018/5/25.
 */

public class WYGridChartView extends View {

    public WYGridChartView(Context context) {
        super(context);
        init();
    }

    public WYGridChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WYGridChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint x_Paint;
    private Paint y_Paint;
    private Paint g_Paint;
    private Paint block_Paint;
    private Paint notice_Paint;
    /**
     * 色块颜色值
     */
    private String colorArray[][];

    private void init() {
        x_Paint = new Paint();
        x_Paint.setAntiAlias(true);
        x_Paint.setColor(Color.BLACK);
        x_Paint.setStrokeWidth(1);
        x_Paint.setStyle(Paint.Style.STROKE);
        x_Paint.setTextSize(dp2px(15));

        y_Paint = new Paint();
        y_Paint.setAntiAlias(true);
        y_Paint.setColor(Color.BLACK);
        y_Paint.setStyle(Paint.Style.STROKE);
        y_Paint.setStrokeWidth(1);
        y_Paint.setTextSize(dp2px(15));

        g_Paint = new Paint();
        g_Paint.setAntiAlias(true);
        g_Paint.setColor(Color.GRAY);
        g_Paint.setStyle(Paint.Style.STROKE);

        block_Paint = new Paint();
        block_Paint.setAntiAlias(true);
        block_Paint.setStyle(Paint.Style.FILL);

        notice_Paint = new Paint();
        notice_Paint.setAntiAlias(true);

        colorArray = new String[][]{
                {"C8AC99", "CBAB99", "CAAB96", "CBAC95", "C9AD95", "C8A995", "C9A996", "C8A994", "C7A992", "C5A991", "C7A896"},
                {"C5A691", "C8A793", "C7A792", "C6A891", "C4A890", "C7A694", "CAA596", "C2A38E", "C3A28F", "C2A38F", "C2A48D"},
                {"C0A48D", "C3A38F", "C5A192", "C6A195", "C6A096", "C6A098", "BE9D86", "BE9C87", "BE9D88", "BC9D87", "BD9F87"},
                {"BF9F8C", "C29C8C", "C39A8D", "C39A8E", "C39990", "BB9881", "BD9983", "BA9982", "B99980", "B89A74", "BB9983"},
                {"BD9584", "C29787", "C19487", "C1958C", "B69279", "B5927B", "B49277", "B39276", "B19175", "B7927B", "B68D78"},
                {"B98D7E", "BC8D80", "BE9186", "B1886C", "AF8870", "B0896E", "AD8B6F", "AA8B6D", "B3886E", "B38672", "B58576"},
                {"B58678", "A87F64", "A98167", "A68266", "A58163", "A38264", "AB8167", "AC7E69", "AE7F6E", "AD7E71", "A0765A"},
                {"A0795F", "9B775D", "99795D", "A2785D", "A5755F", "A57461", "946C51", "986F54", "906F54", "8F7054", "976C52"},
                {"9A6B54", "996A58", "876249", "89634C", "846349", "866048", "8C624E", "895F4D", "775741", "795945", "775642"},
                {"785444", "775445", "654D3E", "644B3C", "634B3F", "664C41", "63483E", "544338", "514239", "53433A", "544239"},
        };
    }

    private float xy_higth;//x和y轴区域的高度
    private float viewhigth;
    private float viewwith;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewwith = w;
        viewhigth = h;
        xy_higth = (Math.min(w, h)) / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGrid(canvas);
        drawX(canvas);
        drawY(canvas);
    }

    /**
     * 绘制网格
     */
    private RectF gridRect;
    private RectF blockrect;//每一块格子的区域
    private int block_hspace = 3;//11个//横向的间隔
    private int block_vspace = 3;//10个//纵向的间隔
    private int selepoint = 0;//选中色块
    private String seleColor = "";//选中色块的颜色值
    private String noticeText = "肤色偏黄";
    private RectF noticeRect;//提示块

    private void drawGrid(Canvas canvas) {
        gridRect = new RectF();
        gridRect.left = xy_higth + block_hspace / 2;
        gridRect.right = viewwith - xy_higth / 2 + block_hspace / 2;
        gridRect.top = xy_higth / 2 - block_vspace / 2;
        gridRect.bottom = viewhigth - xy_higth - block_vspace / 2;

        float block_Width = gridRect.width() / 11;//11列划分
        float block_Higth = gridRect.height() / 10;//10行划分
        /**每次刷新都清空选中的色块*/
        noticeRect = null;
        seleColor = "";
        /**先画行再画列*/
        for (int row = 0; row < 10; row++) {

            for (int colum = 0; colum < 11; colum++) {
                blockrect = new RectF();
                blockrect.left = gridRect.left + (colum * block_Width) + block_hspace;
                blockrect.right = gridRect.left + ((colum + 1) * block_Width) - block_hspace;
                blockrect.top = gridRect.top + (row * block_Higth) + block_vspace;
                blockrect.bottom = gridRect.top + ((row + 1) * block_Higth) - block_vspace;
//
                block_Paint.setStyle(Paint.Style.FILL);
                block_Paint.setColor(Color.parseColor("#fbf4ef"));
                g_Paint.setStrokeWidth(1);
                canvas.drawRect(blockrect, block_Paint);
//
                block_Paint.setColor(Color.parseColor("#" + colorArray[row][colum]));
                canvas.drawCircle(blockrect.centerX(), blockrect.centerY(), ((Math.min(blockrect.width(), blockrect.height()) / 2.5f)), block_Paint);

                /**竖线*/
                canvas.drawLine(gridRect.left + (block_Width * (colum + 1)), gridRect.top, gridRect.left + (block_Width * (colum + 1)), gridRect.bottom, g_Paint);

                /**行线*/
                canvas.drawLine(gridRect.left, gridRect.top + (block_Higth * row), gridRect.right, gridRect.top + (block_Higth * row), g_Paint);


                int yy = (selepoint - 1) / 11;//第几行
                int temp = yy * 11;
                int xx = (selepoint - 1) - temp;//第几列
                if (row == yy && colum == xx) {
                    block_Paint.setStyle(Paint.Style.STROKE);
                    block_Paint.setColor(Color.BLACK);
                    block_Paint.setStrokeWidth(block_vspace);
                    canvas.drawRect(blockrect, block_Paint);
                    noticeRect = blockrect;
                    seleColor = colorArray[row][colum];
                }

            }

        }
        if (noticeRect != null) {
            drawNotice(canvas, noticeRect);
        }
    }

    /**
     * 绘制提示信息
     */
    private void drawNotice(Canvas canvas, RectF rect) {
        canvas.save();
        float wsize = (Math.min(gridRect.height(), gridRect.width()) / 3.5f);
        float hsize = (Math.min(gridRect.height(), gridRect.width()) / 3.5f);


        RectF textrectF = new RectF();
        textrectF.left = rect.right - (wsize / 10);
        textrectF.right = textrectF.left + wsize;
        textrectF.top = rect.bottom - (hsize / 10);
        textrectF.bottom = textrectF.top + hsize;

        /**判断是否在右边*/
        if (textrectF.right > gridRect.right) {
            textrectF.right = rect.left + (wsize / 10);
            textrectF.left = textrectF.right - wsize;
            textrectF.top = rect.bottom - (wsize / 10);
            textrectF.bottom = textrectF.top + hsize;
        }
        /**先判断是否在下边在判断是否也在右边*/
        if (textrectF.bottom > gridRect.bottom) {
            textrectF.left = rect.right - (wsize / 10);
            textrectF.right = textrectF.left + wsize;
            textrectF.bottom = rect.top + (wsize / 10);
            textrectF.top = textrectF.bottom - hsize;
            if (textrectF.right > gridRect.right) {
                textrectF.right = rect.left + (wsize / 10);
                textrectF.left = textrectF.right - wsize;
                textrectF.bottom = rect.top + (wsize / 10);
                textrectF.top = textrectF.bottom - hsize;
            }
        }
        notice_Paint.setColor(Color.parseColor("#CBAC95"));
        canvas.drawRect(textrectF, notice_Paint);

        /**绘制提示语*/
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textrectF.height() / 5f);
        StaticLayout layout = new StaticLayout(noticeText, textPaint, (int) (textrectF.width() / 2), Layout.Alignment.ALIGN_NORMAL, 1.2F, 0.0F, true);
        canvas.translate(textrectF.centerX() - layout.getWidth() / 2.6f, textrectF.centerY() - layout.getHeight() / 2);
        layout.draw(canvas);

        canvas.restore();
    }

    /**
     * 绘制xy轴
     */
    private Path xpath;
    private Rect xrect;//X轴的区域

    private void drawX(Canvas canvas) {
        xrect = new Rect();
        xrect.left = (int) xy_higth;
        xrect.right = (int) viewwith;
        xrect.top = (int) (viewhigth - xy_higth);
        xrect.bottom = (int) viewhigth;

        xpath = new Path();
        xpath.moveTo(xrect.left, xrect.top);
        xpath.lineTo(xrect.left, xrect.top);
        xpath.lineTo(xrect.right, xrect.top);
        xpath.lineTo(xrect.right - 20, xrect.top - 10);
        xpath.moveTo(xrect.right, xrect.top);
        xpath.lineTo(xrect.right - 20, xrect.top + 10);

        x_Paint.setStrokeWidth(1);
        x_Paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(xpath, x_Paint);

        /**绘制坐标刻度*/
        float xblok = gridRect.width() / 11;
        x_Paint.setStrokeWidth(1);
        x_Paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 11; i++) {
            RectF recttext = new RectF();
            recttext.left = gridRect.left + (int) (xblok * i);
            recttext.right = gridRect.left + (int) (xblok * (i + 1));
            recttext.top = gridRect.bottom;
            recttext.bottom = viewhigth;
            Rect textw = new Rect();
            String text = String.valueOf(1 + i);
            x_Paint.getTextBounds(text, 0, text.length(), textw);
            canvas.drawText(text, recttext.centerX() - (textw.width() / 2), recttext.centerY() + (textw.height() / 3), x_Paint);
        }
    }

    private Path ypath;
    private Rect yrect;//Y轴的区域

    private void drawY(Canvas canvas) {
        yrect = new Rect();
        yrect.left = 0;
        yrect.right = (int) xy_higth;
        yrect.top = 0;
        yrect.bottom = (int) (viewhigth - xy_higth);

        ypath = new Path();
        ypath.moveTo(yrect.right, yrect.bottom);
        ypath.lineTo(yrect.right, yrect.bottom);
        ypath.lineTo(yrect.right, yrect.top);
        ypath.lineTo(yrect.right - 10, yrect.top + 20);
        ypath.moveTo(yrect.right, yrect.top);
        ypath.lineTo(yrect.right + 10, yrect.top + 20);

        y_Paint.setStrokeWidth(1);
        y_Paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(ypath, y_Paint);

        /**绘制坐标刻度*/
        float yblok = gridRect.height() / 10;
        y_Paint.setStrokeWidth(1);
        y_Paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 10; i++) {
            RectF recttext = new RectF();
            recttext.left = yrect.left;
            recttext.right = gridRect.left;
            recttext.top = gridRect.top + (yblok * i);
            recttext.bottom = gridRect.top + (yblok * (i + 1));

            Rect textw = new Rect();
            String text = String.valueOf((10 - i));
            y_Paint.getTextBounds(text, 0, text.length(), textw);
            canvas.drawText(text, recttext.centerX() - (textw.width() / 2), recttext.centerY() + (textw.height() / 3), y_Paint);
        }
    }

    public void setSelepoint(int selepoint) {
        if (selepoint > (110)) {
            selepoint = 110;
        }
        this.selepoint = selepoint;
        invalidate();
    }

    public int dp2px(float dpValue) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
        return px;
    }

    public String getSeleColor() {
        return seleColor;
    }

    public synchronized String getSeleColor(int selepoint) {
        if (selepoint < 0 || selepoint > 110) {
            return "";
        }
        int yy = (selepoint - 1) / 11;//第几行
        int temp = yy * 11;
        int xx = (selepoint - 1) - temp;//第几列
        boolean isbreak = false;
        for (int row = 0; row < 10; row++) {
            for (int colum = 0; colum < 11; colum++) {
                if (row == yy && colum == xx) {
                    seleColor = colorArray[row][colum];
                    isbreak = true;
                    break;
                }
            }
            if (isbreak) {
                break;
            }
        }
        return seleColor;
    }
}
