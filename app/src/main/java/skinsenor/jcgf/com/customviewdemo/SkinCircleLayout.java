package skinsenor.jcgf.com.customviewdemo;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JCGF-OY on 2017/7/7.
 */

public class SkinCircleLayout extends ViewGroup {
    public SkinCircleLayout(Context context) {
        super(context);
    }

    public SkinCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 获取此ViewGroup上级容器为其推荐计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 获取此ViewGroup上级容器为其推荐的宽和高
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算出所有childView的宽和高，（通过ViewGroup的measureChildren方法为其所有的孩子设置宽和高，此行执行完成后，childView的宽和高都已经正确的计算过了）
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * ViewGroup内子控件的宽度和高度
         */
        int widthContent = 0;
        int heightContent = 0;

        int itemHeight =getChildAt(0).getMeasuredHeight();//单个childView的高度
        int itemWidth =getChildAt(0).getMeasuredWidth();//单个childView宽度

        /**
         * 测量ViewGroup的宽高，如果为wrap_content就按照内容计算得到的宽高
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : widthContent, (heightMode == MeasureSpec.EXACTLY) ? heightSize : heightContent);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int rigth, int bottom) {
        drawInCircle();
    }

    /**进行控件圆形排列*/
    private void drawInCircle(){
        int viewgrouWidth=getWidth();
        int viewgrouHeight=getHeight();
        int childCounts = getChildCount();
        drawBigCircle(viewgrouWidth,viewgrouHeight);


        for (int i=1;i<childCounts;i++){
            View view1 = getChildAt(i);
            drawSmallCircles(view1,(int)i*(360/8)+20);
        }
    }

    /**画大的圆*/
    private float bigRadius;//大圆的半径就是view的宽
    private int circleCentreX;//圆心点坐标
    private int circleCentreY;
    private void drawBigCircle(int viewgrouWidth,int viewgrouHeight){
        View view = getChildAt(0);
        bigRadius=view.getMeasuredWidth()/2;
        circleCentreX=viewgrouWidth/2-view.getMeasuredWidth()/3;
        circleCentreY=viewgrouHeight/2+view.getMeasuredHeight()/4+this.getPaddingTop();
        int left=circleCentreX;
        int top=circleCentreY-view.getMeasuredHeight();
        int rigth =left+view.getMeasuredWidth();
        int bottom=top+view.getMeasuredHeight();
        view.layout(left,top,rigth,bottom);

        Rect rect=new Rect(left,top,rigth,bottom);
        circleCentreX=rect.centerX();
        circleCentreY=rect.centerY();

    }

    /**画小的圆*/
    private float smallRadius;//小圆的半径就是view的宽
    private int smallcircleCentreX;//圆心点坐标
    private int smallcircleCentreY;
    private int spaceradiu=80;//间隔大小
    private void drawSmallCircles(View view,int rd){
        smallRadius=view.getMeasuredWidth()/2;
        Point point=getXYPoint(circleCentreX,circleCentreY,(int)(bigRadius+smallRadius+spaceradiu),rd);

        smallcircleCentreX=point.x;
        smallcircleCentreY=point.y;

        int left=smallcircleCentreX-view.getMeasuredWidth()/2;
        int top=smallcircleCentreY-view.getMeasuredHeight()/2;
        int rigth =smallcircleCentreX+view.getMeasuredWidth()/2;
        int bottom=smallcircleCentreY+view.getMeasuredHeight()/2;
        view.layout(left,top,rigth,bottom);
    }

    /**
     * 以原点为圆点，以radius维半径画圆，通过弧度o,获得坐标
     * @param radius 半径
     * @param o 弧度
     * @return
     */
    public static Point getXYPoint(int circleCentreX,int circleCentreY, int radius, float o){
        return new Point((int) (radius*Math.cos(o* 3.14 /180) + circleCentreX),(int) (radius*Math.sin(o * 3.14 /180) + circleCentreY));
    }
}
