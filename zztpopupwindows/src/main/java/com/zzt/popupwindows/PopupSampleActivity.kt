package com.zzt.popupwindows

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import com.zzt.popupwindows.library.ZNormalPopup
import com.zzt.popupwindows.util.ZPopHintBubble
import com.zzt.popupwindows.library.ZDirection
import com.zzt.popupwindows.util.ZPopItemClickListener
import com.zzt.popupwindows.util.ZPopListBubble
import com.zzt.popupwindows.util.ZPopTextListDefaultBubble
import java.util.ArrayList

class PopupSampleActivity : AppCompatActivity() {
    val TAG = PopupSampleActivity::class.java.simpleName
    var tv_text1: TextView? = null
    var tv_text2: TextView? = null
    var tv_text3: TextView? = null
    var tv_text4: TextView? = null
    var tv_text5: TextView? = null
    var rv_list_data: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_sample)
        initView()
    }

    var padding = 50
    var zNormalPopup: ZNormalPopup? = null
    fun initView() {
        rv_list_data = findViewById(R.id.rv_list_data)
        val mList: MutableList<StartActivityDao> = ArrayList()
        mList.add(StartActivityDao("弄个通用的列表悬浮", "", "1"))
        StartActivityRecyclerAdapter.setAdapterData(
            rv_list_data,
            RecyclerView.VERTICAL,
            mList
        ) { itemView: View?, position: Int, data: StartActivityDao ->
            when (data.arouter) {
                "1" -> {
                    if (itemView != null) {
                        var mList = mutableListOf<String>()
                        mList.add("zzzzzzzzzz")
                        mList.add("tt")
                        mList.add("ggggggggg")
                        mList.add("uuuuuu")
                        var zpopBubble = ZPopTextListDefaultBubble(this@PopupSampleActivity)
                        zpopBubble.setListDataDefault(list = mList,
                            selPos = 2,
                            listener = object : ZPopItemClickListener<String> {
                                override fun onItemClick(position: Int, value: String) {
                                    Log.d(TAG, "点击 选中了什么内容：$value")

                                    zpopBubble.dismiss()
                                }
                            })
                        zpopBubble.show(itemView)
                    }
                }
                "2" -> {
                }
            }
        }
        zNormalPopup = ZNormalPopup(
            this,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv_text1 = findViewById(R.id.tv_text1)
        tv_text2 = findViewById(R.id.tv_text2)
        tv_text3 = findViewById(R.id.tv_text3)
        tv_text4 = findViewById(R.id.tv_text4)
        tv_text5 = findViewById(R.id.tv_text5)
        tv_text5?.setOnClickListener(View.OnClickListener { v ->
            ZPopHintBubble(
                v.context,
                "getResources().getString(R.string.s6_50)",
                ZDirection.DIRECTION_BOTTOM
            ).show(v)
        })
        tv_text4?.setOnClickListener(View.OnClickListener { anchor ->
            val textList: MutableList<String> = ArrayList()
            textList.add("resources.getString(R.string.s13_241)")
            textList.add("resources.getString(R.string.s27_217)")
            textList.add("resources.getString(R.string.s27_228)")
            textList.add("resources.getString(R.string.s27_219)")
            val zPopListBubble = ZPopListBubble(
                anchor.context, textList, 0,
                R.drawable.select_item_ffffff_f7f7f8,
                R.color.selector_text_colors_252c58_3d56ff,
                14, resources.getDimensionPixelOffset(R.dimen.margin_13dp)
            ) { position, value -> }
            zPopListBubble.show(anchor)
        })
        tv_text1?.setOnClickListener(View.OnClickListener { v: View ->

            //region 这个可以的
            val textView = TextView(this@PopupSampleActivity)
            textView.setPadding(padding, padding, padding, padding)
            textView.text = "通过 dimAmount() 设置背景遮罩"
            textView.setTextColor(
                resources.getColor(R.color.salmon)
            )
            zNormalPopup!!.preferredDirection(ZDirection.DIRECTION_BOTTOM)
                .view(textView)
                .bgColor(resources.getColor(R.color.qmui_config_color_white))
                .bgColorGradient(
                    intArrayOf(
                        ContextCompat.getColor(v.context, R.color.color_FFD333),
                        ContextCompat.getColor(v.context, R.color.color_FF8702)
                    )
                )
                .borderColor(resources.getColor(R.color.magenta))
                .borderWidth(0)
                .radius(10)
                .arrow(true)
                .arrowSize(dp2px(v.context, 30f), dp2px(v.context, 30f))
                .dimAmount(0.6f)
                .show(v)
        })
        tv_text2?.setOnClickListener(View.OnClickListener { v: View ->
            //region 这个可以的
            val textView = TextView(this@PopupSampleActivity)
            textView.setPadding(padding, padding, padding, padding)
            textView.text = "通过 dimAmount() 设置背景遮罩"
            textView.setTextColor(
                resources.getColor(R.color.salmon)
            )
            zNormalPopup!!.preferredDirection(ZDirection.DIRECTION_TOP)
                .view(textView)
                .bgColor(resources.getColor(R.color.qmui_config_color_white))
                .bgColorGradient(
                    intArrayOf(
                        ContextCompat.getColor(v.context, R.color.color_FFD333),
                        ContextCompat.getColor(v.context, R.color.color_FF8702)
                    )
                )
                .borderColor(resources.getColor(R.color.magenta))
                .borderWidth(0)
                .radius(dp2px(v.context, 10f))
                .arrow(true)
                .arrowSize(dp2px(v.context, 20f), dp2px(v.context, 20f))
                .offsetYIfBottom(dp2px(v.context, 10f))
                .dimAmount(0.6f)
                .show(v)
        })
        tv_text3?.setOnClickListener(View.OnClickListener { v: View? ->

            //region 这个可以的
            val textView = TextView(this@PopupSampleActivity)
            textView.setPadding(padding, padding, padding, padding)
            textView.text = "通过 dimAmount() 设置背景遮罩"
            textView.setTextColor(
                resources.getColor(R.color.salmon)
            )
            zNormalPopup!!.preferredDirection(ZDirection.DIRECTION_CENTER_IN_SCREEN)
                .view(textView)
                .bgColor(resources.getColor(R.color.qmui_config_color_white))
                .borderColor(resources.getColor(R.color.magenta))
                .borderWidth(5)
                .radius(10)
                .dimAmount(0.6f)
                .onDismiss { }
                .show(v!!)
        })
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }
}