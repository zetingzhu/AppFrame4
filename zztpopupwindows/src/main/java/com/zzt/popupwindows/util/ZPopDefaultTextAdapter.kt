package com.zzt.popupwindows.util

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: zeting
 * @date: 2021/12/10
 * 默认文本选择器
 */
class ZPopDefaultTextAdapter :
    ListAdapter<String, ZPopDefaultTextAdapter.BaseViewHolder>(UserDiffCallback()) {

    // 默认选中
    var selectPosition = 0

    /**
     * 列表监听
     */
    var itemClickListener: ZPopItemClickListener<String>? = null

    /**
     * 添加列表点击
     */
    fun addItemClickListener(listener: ZPopItemClickListener<String>?) {
        this.itemClickListener = listener
    }

    /**
     * 这里设置数据，这个data每次都必须是新的
     */
    fun setAdapterData(data: MutableList<String>) {
        submitList(data)
    }


    class UserDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            /**
             *  这里对字段比较
             */
            return false
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            /**
             * 这里对像个对象比较
             */
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        /**
         * 这里设置自己的列表
         */
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.test_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(
            holder.adapterPosition,
            selectPosition,
            getItem(holder.adapterPosition)
        )
        holder.itemView.setOnClickListener {
            var oldSelPos = selectPosition
            this.selectPosition = holder.adapterPosition
            notifyItemChanged(oldSelPos)
            notifyItemChanged(selectPosition)
            val item = getItem(holder.adapterPosition)
            it.postDelayed({
                itemClickListener?.onItemClick(selectPosition, item)
            }, 300)
        }
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * 这里对展示处理
         */
        fun bindView(
            position: Int,
            selectPosition: Int,
            str: String,
        ) {
            itemView.findViewById<TextView>(android.R.id.text1)?.apply {
                this.gravity = Gravity.CENTER
                this.setPadding(20, 20, 20, 20)
                this.text = str
                if (position == selectPosition) {
                    this.setTextColor(Color.parseColor("#3D56FF"))
                    this.setBackgroundColor(Color.parseColor("#F7F7F8"))
                } else {
                    this.setTextColor(Color.parseColor("#252C58"))
                    this.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
        }
    }
}