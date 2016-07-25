package com.hony.sportview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hony.sportview.R;


/**
 * Created by Hony on 2016/7/20.
 */
public class SportView extends View{

    private int mWidth,mHeight;//view的宽高
    private int mradio = 16;//圆弧的半径
    private int mLightCircleColor;//明亮的背景色
    private int mDarkCircleColor;//暗的背景色
    private int mTextSize;//字体大小
    private int mTextColor;//字体颜色
    private int mTitleColor;//标题颜色
    private int progress = 100;//进度

    private int minDistViewSize;
    private int maxDistViewSize;
    private int traslationX;
    private int traslationY;
    private RectF wheelBoundsRectF;

    private Paint lightPaint;//亮圆
    private Paint darkPaint;//暗圆
    private Paint textPaint;//文字
    private Paint itemEndPointsPaint;//半圆
    private Paint centerTextPaint;//中心字
    private Paint orderTextPaint;//排名

    private float START_ANGLE = -220;//起始角度
    private float END_ANGLE = 40;//结束角度

    private boolean isShowAnimation = false;//是否正在执行动画
    private float avg = 100;//默认平均步数100
    private int currentAngle = 10;//默认角度
    private int number = 10;//默认排名第10

    public SportView(Context context) {
        super(context,null);
    }

    public SportView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        initData();
    }

    public SportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setRadio(int size){
        this.mradio = size;
    }
    /**
     * 设置排名数
     * @param number
     */
    public void setNumber(int number) throws Exception {
        if(number<0)
            throw new Exception("值超过有效范围");
        this.number = number;
    }
    /**
     * 设置步数
     * @param progress
     * @throws Exception
     */
    public void setProgress(int progress) throws Exception {
        if(progress<0)
            throw new Exception("值超过有效范围");
        this.progress = progress;
        resumeProgress();
    }

    /**
     * 重新计数进度
     */
    private void resumeProgress(){
        float var = progress/avg;
        float main = Math.abs(END_ANGLE - START_ANGLE);
        this.currentAngle = (int) (var*main);
        if(this.currentAngle > Math.abs(END_ANGLE - START_ANGLE)){
            this.currentAngle = (int) Math.abs(END_ANGLE - START_ANGLE);
        }
    }

    private void setCurrentAngle(int currentAngle){
        this.currentAngle = currentAngle;
    }
    /**
     * 设置平均步数
     * @param avg
     */
    public void setAvg(int avg) throws Exception {
        if(avg<0)
            throw new Exception("值超过有效范围");
        this.avg = avg;
        resumeProgress();
    }
    /**
     * 修改起始角度
     * @param startAngle
     * @throws Exception
     */
    public void setStartAngle(float startAngle) throws Exception {
        if(Math.abs(startAngle) > 360f)
            throw new Exception("起始角度值超过有效范围");
        this.START_ANGLE = startAngle;
        resumeProgress();
    }

    /**
     * 修改结束角度
     * @param endAngle
     * @throws Exception
     */
    public void setEndAngle(float endAngle) throws Exception {
        if(Math.abs(endAngle) > 360f)
            throw new Exception("结束角度值超过有效范围");
        this.END_ANGLE = endAngle;
        resumeProgress();
    }

    /**
     * 设置圆环的背景颜色
     * @param color
     */
    public void setCircleBackgroudColor(int color){
        this.mDarkCircleColor = color;
    }

    /**
     * 设置圆环的颜色
     * @param color
     */
    public void setCircleProgressColor(int color){
        this.mLightCircleColor = color;
    }
    private void initData(){

        mLightCircleColor = getResources().getColor(R.color.light_blue_color);
        mDarkCircleColor = getResources().getColor(R.color.dark_blue_color);
        mTextColor = getResources().getColor(R.color.gray_color);
        mTitleColor = getResources().getColor(R.color.light_blue_color);
        mTextSize = dip2px(getContext(),14);

        lightPaint = new Paint();
        lightPaint.setAntiAlias(true);
        lightPaint.setStyle(Paint.Style.STROKE);
        lightPaint.setStrokeWidth(mradio * 2);
        lightPaint.setColor(mLightCircleColor);

        darkPaint = new Paint();
        darkPaint.setAntiAlias(true);
        darkPaint.setStyle(Paint.Style.STROKE);
        darkPaint.setStrokeWidth(mradio * 2);
        darkPaint.setColor(mDarkCircleColor);

        itemEndPointsPaint = new Paint();
        itemEndPointsPaint.setColor(mDarkCircleColor);
        itemEndPointsPaint.setAntiAlias(true);

        centerTextPaint = new Paint();
        centerTextPaint.setColor(mLightCircleColor);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setTextSize(dip2px(this.getContext(),48));

        orderTextPaint = new Paint();
        orderTextPaint.setColor(mLightCircleColor);
        orderTextPaint.setAntiAlias(true);
        orderTextPaint.setTextSize(dip2px(this.getContext(),24));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mTextSize);
        textPaint.setColor(mTextColor);
    }

    /**
     * dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    private  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 当View大小改变的时候调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
        @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
            this.mWidth = getMeasuredWidth();
            this.mHeight = getMeasuredHeight();

            this.minDistViewSize = Math
                    .min(getMeasuredWidth(), getMeasuredHeight());
            this.maxDistViewSize = Math
                    .max(getMeasuredWidth(), getMeasuredHeight());

            if (mWidth <= mHeight) {
                this.traslationX = 0;
                this.traslationY = (maxDistViewSize - minDistViewSize) / 2;
            } else {
                this.traslationX = (maxDistViewSize - minDistViewSize) / 2;
                this.traslationY = 0;
            }
            // Adding artificial padding, depending on line width
            wheelBoundsRectF = new RectF(0 + mradio, 0 + mradio,
                    minDistViewSize - mradio, minDistViewSize
                    - mradio);
    }



    /**
     * 当View绘制内容的时候调用
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(traslationX, traslationY);
        lightPaint.setColor(mLightCircleColor);
        canvas.drawArc(wheelBoundsRectF,START_ANGLE,Math.abs((END_ANGLE - START_ANGLE)),false,darkPaint);
        drawableCricleArc(canvas,START_ANGLE,END_ANGLE,mDarkCircleColor);
        canvas.drawArc(wheelBoundsRectF,START_ANGLE,currentAngle,false,lightPaint);
        drawableCricleArc(canvas,START_ANGLE,(START_ANGLE+currentAngle),mLightCircleColor);
        drawableText(canvas);
    }

    /**
     * 画文字
     */
    private void drawableText(Canvas canvas){

        //中心文字的位置
        float tx = (this.minDistViewSize - getFontlength(centerTextPaint,""+progress))/2;//
        float ty = (this.minDistViewSize - getFontHeight(centerTextPaint))/2+getFontHeight(centerTextPaint)/2;
        canvas.drawText(""+progress,tx,ty,centerTextPaint);
        //上面的截止时间
        float topX = (this.minDistViewSize - getFontlength(textPaint,"截止22:16已走"))/2;
        float topY = ty - dip2px(getContext(),24) - getFontHeight(centerTextPaint)/2;
        canvas.drawText("截止22:16已走",topX,topY,textPaint);
        //下面好友平均值
        float bottomX = (this.minDistViewSize - getFontlength(textPaint,"好友平均"+(int)avg+"步"))/2;
        float bottomY = ty + getFontHeight(centerTextPaint)/2+dip2px(getContext(),4);
        canvas.drawText("好友平均"+(int)avg+"步",bottomX,bottomY,textPaint);
        //底部排名
        float x = (this.minDistViewSize - getFontlength(orderTextPaint,"89"))/2;
        float y = this.minDistViewSize - getFontHeight(orderTextPaint)/2;
        canvas.drawText(""+number,x,y,orderTextPaint);
        float x1 = x - getFontlength(textPaint,"第")- dip2px(getContext(),4);
        float y1 = this.minDistViewSize - getFontHeight(orderTextPaint)/2 - (getFontHeight(orderTextPaint) - getFontHeight(textPaint))/2;
        canvas.drawText("第",x1,y1,textPaint);
        float x2 = x + getFontlength(orderTextPaint,""+number)+ dip2px(getContext(),4);
        float y2 = this.minDistViewSize - getFontHeight(orderTextPaint)/2 - (getFontHeight(orderTextPaint) - getFontHeight(textPaint))/2;
        canvas.drawText("名",x2,y2,textPaint);
    }
    /**
     * 画圆弧两头的小圆
     * @param canvas
     * @param startAngle
     * @param endAngle
     * @param color
     */
    private void drawableCricleArc(Canvas canvas,float startAngle,float endAngle,int color){
        itemEndPointsPaint.setColor(color);
        int topPosition = minDistViewSize / 2 - mradio;
        canvas.drawCircle(
                (float) (Math.cos(Math.toRadians(startAngle)) *
                        topPosition + topPosition + mradio),
                (float) (Math.sin(Math.toRadians(startAngle)) *
                        topPosition + topPosition + mradio), mradio,
                itemEndPointsPaint);

        canvas.drawCircle(
                (float) (Math.cos(Math.toRadians(endAngle)) *
                        topPosition + topPosition + mradio),
                (float) (Math.sin(Math.toRadians(endAngle)) *
                        topPosition + topPosition + mradio), mradio,
                itemEndPointsPaint);
    }

    public void startAnimation(){
        if(!isShowAnimation){

            ObjectAnimator animator = new ObjectAnimator().ofInt(this,"currentAngle",0,currentAngle);
            animator.setDuration(1200);
            animator.setInterpolator(new AccelerateDecelerateInterpolator(){
                @Override
                public float getInterpolation(float input) {
                    currentAngle = (int) input;
                    postInvalidate();
                    return super.getInterpolation(input);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isShowAnimation = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isShowAnimation = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.play(animator);
            animationSet.start();
        }


    }

    /**
     * @return 返回字符串的长度
     */
    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @return 返回文字高度
     */
    public static float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
}
