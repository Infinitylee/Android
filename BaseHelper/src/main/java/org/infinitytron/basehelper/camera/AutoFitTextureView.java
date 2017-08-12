/**
 * @fileName AutoFitTextureView
 * @describe 自适应内容流显示控件
 * @author 李培铭
 * @time 2017-08-10
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

public class AutoFitTextureView extends TextureView {

    private int width = 0;
    private int height = 0;

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("控件大小异常");
        }
        this.width = width;
        this.height = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (this.width == 0 || this.height == 0) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * this.width / this.height) {
                setMeasuredDimension(width, width * this.height / this.width);
            } else {
                setMeasuredDimension(height * this.width / this.height, height);
            }
        }
    }
}
