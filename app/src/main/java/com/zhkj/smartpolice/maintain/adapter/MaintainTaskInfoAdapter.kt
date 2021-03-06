package com.zhkj.smartpolice.maintain.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.sunny.zy.base.BaseRecycleAdapter
import com.sunny.zy.base.BaseRecycleViewHolder
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.app.UrlConstant
import com.zhkj.smartpolice.maintain.bean.MaintainTaskBean
import kotlinx.android.synthetic.main.item_maintain_audit.view.*
import java.text.SimpleDateFormat

class MaintainTaskInfoAdapter(info: ArrayList<MaintainTaskBean>, var isType: Boolean) : BaseRecycleAdapter<MaintainTaskBean>(info) {

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_maintain_audit, parent, false)

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tv_apply_date.text = getDate(getData(position).applyDate)
        holder.itemView.tv_dept_name.text = getData(position).deptName
        getData(position).shopGoodsEntityList?.let {
            holder.itemView.tv_goods.text = it[0].goodsName
            Glide.with(context)
                .load(UrlConstant.LOAD_IMAGE_PATH_URL + it[0].imageId)
                .dontAnimate()
                .placeholder(R.drawable.svg_default_image)
                .into(holder.itemView.iv_maintain_img)
        }
        holder.itemView.tv_style_font_black_small.text = getData(position).applyContent

        if (isType) {
            holder.itemView.tv_audit_status.text = "已维修"
        } else {
            holder.itemView.tv_audit_status.text = "待维修"
            holder.itemView.tv_audit_status.setBackgroundResource(R.drawable.sel_maintain_audit_type_background)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(str: String?): String {
        val formatter: java.text.SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(str))
    }

}