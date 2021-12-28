package com.zzt.popupwindows.util

/**
 * @author: zeting
 * @date: 2021/12/10
 * 设置item监听
 */
interface ZPopItemClickListener<T> {
    fun onItemClick(position: Int, value: T)
}