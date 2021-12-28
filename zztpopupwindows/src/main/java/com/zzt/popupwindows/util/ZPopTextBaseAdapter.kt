package com.zzt.popupwindows.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: zeting
 * @date: 2021/12/10
 * 所有列表选择器的简单适配标准模板
 */
class ZPopTextBaseAdapter<T> :
    ListAdapter<T, ZPopTextBaseAdapter.BaseViewHolder<T>>(UserDiffCallback()) {
    /** 列表监听 */
    var itemClickListener: ZPopItemClickListener<T>? = null

    /** 添加列表点击  */
    fun addItemClickListener(listener: ZPopItemClickListener<T>?) {
        this.itemClickListener = listener
    }

    /** 这里设置数据，这个data每次都必须是新的 */
    fun setAdapterData(data: MutableList<T>) {
        submitList(data)
    }

    class UserDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            /** 这里对字段比较 */
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            /** 这里对像个对象比较  */
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        /** 这里设置自己的列表 */
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.test_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindView(holder.adapterPosition, getItem(holder.adapterPosition), itemClickListener)
    }

    class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 这里对展示处理  */
        fun bindView(position: Int, t: T, listener: ZPopItemClickListener<T>?) {
            itemView.setOnClickListener { listener?.onItemClick(position, t) }
        }
    }
}