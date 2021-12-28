package com.zzt.popupwindows.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzt.popupwindows.R
import com.zzt.popupwindows.library.ZDirection
import com.zzt.popupwindows.library.ZNormalPopup

/**
 * @author: zeting
 * @date: 2021/11/19
 * 带有一个list 礼包的气泡
 */
class ZPopTextListDefaultBubble(context: Context) : ZNormalPopup(
    context, context.resources.getDimensionPixelOffset(
        R.dimen.margin_124dp
    ), ViewGroup.LayoutParams.WRAP_CONTENT
) {

    var recyclerView: RecyclerView? = null

    init {
        initView(context, ZDirection.DIRECTION_BOTTOM)
    }

    /**
     * 设置列表选择适配器
     *
     * @param list     列表数据
     * @param listener 选择监听
     */
    fun setListDataDefault(
        list: MutableList<String>,
        selPos: Int = 0,
        listener: ZPopItemClickListener<String>?
    ) {
        if (list.isNotEmpty()) {
            var textListAdapter = ZPopDefaultTextAdapter()
            textListAdapter.setAdapterData(list)
            textListAdapter.selectPosition = selPos
            textListAdapter.addItemClickListener(listener)
            recyclerView?.adapter = textListAdapter
        } else {
            throw RuntimeException("数据为空，请检查代码")
        }
    }

    /**
     * 设置列表选择适配器
     *
     * @param adapter 继承ListAdapter 推荐 ZPopTextBaseAdapter 这个格式，
     */
    fun setListDataAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter != null) {
            recyclerView?.adapter = adapter
        } else {
            throw RuntimeException("Adapter为空，请检查代码")
        }
    }

    /**
     * 处理展示细节
     */
    fun initView(context: Context, @ZDirection preferredDirection: Int) {
        recyclerView = RecyclerView(context)
        // 设置列表默认为线性列表
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        view(recyclerView)
        offsetYIfBottom(-context.resources.getDimensionPixelOffset(R.dimen.margin_10dp))
        radius(context.resources.getDimensionPixelOffset(R.dimen.margin_3dp))
        arrow(false)
        preferredDirection(preferredDirection)
    }

    override fun show(anchor: View) {
        super.show(anchor)
    }
}