package skinsenor.jcgf.com.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wenbaohe on 2017/12/8.
 */

public class SkinGridView extends View {

    public SkinGridView(Context context) {
        super(context);
    }

    public SkinGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);


        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, dp2px(200));
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, dp2px(200));
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, dp2px(200));
        }
        initPaint();
    }

    private Paint drawXYlinePaint;


    /**
     * 色块颜色值
     */
    private String colorArray[][];

    private void initPaint() {
        drawXYlinePaint = new Paint();
        drawXYlinePaint.setAntiAlias(true);
//        drawXYlinePaint.setTypeface(TypefaceHelper.get(getContext(), "fonts/font_w3.otf"));
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
        rigthrect = new ArrayList<>();
//        rigthrect.add(110-10);
//        rigthrect.add(110-11);
//        rigthrect.add(110-21);
//        rigthrect.add(110-22);
//        rigthrect.add(110-32);
//        rigthrect.add(110-33);
//        rigthrect.add(110-43);
//        rigthrect.add(110-44);
//        rigthrect.add(110-54);
//        rigthrect.add(110-55);
//        rigthrect.add(110-65);
//        rigthrect.add(110-66);
//        rigthrect.add(110-76);
//        rigthrect.add(110-77);

        rigthrect.add(10);
        rigthrect.add(11);
        rigthrect.add(21);
        rigthrect.add(22);
        rigthrect.add(32);
        rigthrect.add(33);
        rigthrect.add(43);
        rigthrect.add(44);
        rigthrect.add(54);
        rigthrect.add(55);
        rigthrect.add(65);
        rigthrect.add(66);
        rigthrect.add(76);
        rigthrect.add(77);
    }


    private int number = 1;//11列
    private String tipText = "";//提示文字
    private int num2 = -1;//10行
    private int block_hspace = dp2px(1f);//11个//横向的间隔
    private int block_vspace = dp2px(1f);//10个//纵向的间隔

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float bw = getWidth() / 12f;
        float bh = getHeight() / 11f;
        RectF rectF = new RectF(bw, bh / 2, getWidth() - bw / 2, getHeight() - bh);
        drawXYlinePaint.setColor(Color.parseColor("#c0c0c0"));
        canvas.drawRect(rectF, drawXYlinePaint);

        float block_Width = rectF.width() / 11 - block_hspace / 11;
        float block_Higth = rectF.height() / 10 - block_vspace / 10;


        for (int i = 0; i < 10; i++)//行
        {
            for (int j = 0; j < 11; j++)//列
            {
                /**画格子部分*/
                Paint barbgpaint = new Paint();
                drawXYlinePaint.setColor(Color.parseColor("#fbf4ef"));

                barbgpaint.setAntiAlias(true);
                RectF gridblock = new RectF();
                gridblock.left = j * block_Width + block_hspace + bw;
                gridblock.right = (j + 1) * block_Width - block_hspace + bw;
                gridblock.top = i * block_Higth + block_vspace + bh / 2;
                gridblock.bottom = (i + 1) * block_Higth - block_vspace + bh / 2;
                canvas.drawRoundRect(gridblock, 0, 0, drawXYlinePaint);//开始绘制

                barbgpaint.setColor(Color.parseColor("#" + colorArray[i][j]));
                canvas.drawCircle(gridblock.centerX(), gridblock.centerY(), gridblock.height() / 3f, barbgpaint);




                 /*右下角到左上角 1-110*/
//                int yy = 9 - (number - 1) / 11;
//                int temp = yy * 11;
//                int xx = ((110 - number) - temp);

                /*左上角到右下角1-110*/
                int yy = (number - 1) / 11;
                int temp = yy * 11;
                int xx = (number - 1) - temp;

                if ((i == yy) && (j == xx)) {//加黑框(左下角原点坐标计算)
                    barbgpaint.setStyle(Paint.Style.STROKE);
                    barbgpaint.setStrokeWidth(3);
                    barbgpaint.setColor(Color.GRAY);
                    canvas.drawRoundRect(gridblock, 0, 0, barbgpaint);//开始绘制
                    checkRect = gridblock;
                }

                /**左边坐标数字*/
                drawXYlinePaint.setColor(Color.parseColor("#888888"));
                drawXYlinePaint.setAntiAlias(true);
//                float y = (i + 1) * block_Higth+ bh/2 - block_vspace;
//                canvas.drawText(""+(10-i), bw/2, y, drawXYlinePaint);
                RectF fText = new RectF(0, bh / 2, bw, rectF.bottom);
                float y = fText.height() / 10;
//                canvas.drawText(""+(10-i), bw/2, y*i+bh, drawXYlinePaint);
                drawtext(canvas, "" + (10 - i), bw / 2, y * i + bh, fText.width() / 2f);
                /**下边坐标数字*/
                float x = (j + 1) * block_Width + bw / 2 - block_hspace;
//                canvas.drawText(""+(j+1), x, getHeight()-bh/2, drawXYlinePaint);
                drawtext(canvas, "" + (j + 1), x, getHeight() - bh / 2, fText.width() / 2f);
            }
        }

        drawXYlinePaint.setColor(Color.BLACK);
        drawXline(canvas);
        drawYline(canvas);
        if (checkRect != null && tipText != null) {
            draTipText(canvas, checkRect);
        }

    }

    private void drawtext(Canvas canvas, String str, float x, float y, float textSize) {
        drawXYlinePaint.setTextAlign(Paint.Align.CENTER);
        drawXYlinePaint.setTextSize(textSize);
        Paint.FontMetrics metrics = drawXYlinePaint.getFontMetrics();
        //文字的y轴坐标
        float basey = y + (Math.abs(metrics.ascent) - metrics.descent) / 2;
        canvas.drawText(str, x, basey, drawXYlinePaint);
    }

    private RectF textrectF;
    private RectF checkRect;
    private ArrayList<Integer> rigthrect;//右区域的位置变换提示信息框的位置

    private void draTipText(Canvas canvas, RectF rectF) {
        float size_w = rectF.width() * 3;
        float size_h = rectF.height() * 3;

        float x = rectF.right - rectF.width() / 4;
        float y = rectF.top + rectF.height() / 4;
            /*在顶部*/
        if (number < 9 || (number > 12 && number < 20)) {
            x = rectF.right - rectF.width() / 4;
            y = rectF.bottom - rectF.height() / 4 + size_h;
        }
        if (rigthrect.contains(number)) {//右边区域
            x = rectF.left + rectF.width() / 4 - size_w;
            y = rectF.bottom - rectF.height() / 4 + size_h;
        }
        /*右下角*/
        if (number == 87 || number == 88 || number == 98 || number == 99 || number == 109 || number == 110) {
            x = rectF.left + rectF.width() / 4 - size_w;
            y = rectF.top + rectF.height() / 4;
        }

        textrectF = new RectF();
        textrectF.left = x;
        textrectF.top = y - size_h;
        textrectF.right = x + size_w;
        textrectF.bottom = y;
        drawXYlinePaint.setColor(Color.parseColor("#a57452"));
        canvas.drawRect(textrectF, drawXYlinePaint);

        drawXYlinePaint.setColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textrectF.height() / 5f);
        StaticLayout layout = new StaticLayout(tipText, textPaint, (int) size_w / 2, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.translate(textrectF.centerX() - layout.getWidth() / 2, textrectF.centerY() - layout.getHeight() / 2);
        layout.draw(canvas);
    }

    /**
     * 按宽的12等比分
     */
    private void drawXline(Canvas canvas) {
        float bw = getWidth() / 12f;
        float bh = getHeight() / 11f;
        canvas.drawLine(getWidth(), getHeight() - bh, getWidth() - dp2px(6), (getHeight() - bh) + dp2px(3), drawXYlinePaint);
        canvas.drawLine(getWidth(), getHeight() - bh, getWidth() - dp2px(6), (getHeight() - bh) - dp2px(3), drawXYlinePaint);//箭头
        canvas.drawLine(bw, getHeight() - bh, getWidth(), getHeight() - bh, drawXYlinePaint);//轴
    }

    private void drawYline(Canvas canvas) {
        float bw = getWidth() / 12f;
        float bh = getHeight() / 11f;

        canvas.drawLine(bw, 0, bw + dp2px(3), dp2px(6), drawXYlinePaint);//箭头
        canvas.drawLine(bw, 0, bw - dp2px(3), dp2px(6), drawXYlinePaint);

        canvas.drawLine(bw, getHeight() - bh, bw, 0, drawXYlinePaint);//轴
    }

    public int dp2px(float dpValue) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
        return px;
    }

    public void setNumber(int number) {
        if (number <= 0) {
            number = 1;
        }
        if (number > 110) {
            number = 110;
        }
        this.number = number;
        invalidate();
    }

    public void setTipText(String tipText) {
//        if (TextUtils.isEmpty(tipText)) {
//            return;
//        }
        this.tipText = tipText;
        invalidate();
    }
}
