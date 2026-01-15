package com.termux.zerocore.loader

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.xh_lib.utils.UUtils
import com.lcw.library.imagepicker.utils.ImageLoader
import com.termux.R

class ZeroTermuxImageLoader :  ImageLoader{

    private val mOptions: RequestOptions = RequestOptions()
        .centerCrop()
        .format(DecodeFormat.PREFER_RGB_565)
        .placeholder(R.mipmap.icon_image_default)
       // .error(R.mipmap.icon_image_error)

    private val mPreOptions: RequestOptions = RequestOptions()
        .skipMemoryCache(true)
       // .error(R.mipmap.icon_image_error)

    override fun loadImage(imageView: ImageView, imagePath: String?) {
        Glide.with(imageView.context).load(imagePath).apply(mOptions).into(imageView)
    }

    override fun loadPreImage(imageView: ImageView, imagePath: String?) {
        Glide.with(imageView.context).load(imagePath).apply(mPreOptions).into(imageView)
    }

    override fun clearMemoryCache() {
        Glide.get(UUtils.getContext()).clearMemory()
    }
}
