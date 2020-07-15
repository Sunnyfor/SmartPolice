package com.zhkj.smartpolice.stadium

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.zy.base.BaseActivity
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveTimeAdapter
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveWeekAdapter
import com.zhkj.smartpolice.haircut.bean.ManageBean
import com.zhkj.smartpolice.haircut.bean.MerchantTime
import com.zhkj.smartpolice.haircut.bean.WeekDayBean
import com.zhkj.smartpolice.merchant.model.MerchantContract
import com.zhkj.smartpolice.merchant.model.MerchantPresenter
import com.zhkj.smartpolice.stadium.adapter.StadiumResourceAdapter
import kotlinx.android.synthetic.main.act_stadium_detail.*
import java.util.*

class StadiumDetailActivity : BaseActivity(), MerchantContract.IReserveTimeView {

    val presenter by lazy {
        MerchantPresenter(this)
    }

    val shopId: String by lazy {
        intent.getStringExtra("shopId")
    }

    private var endDate = ""

    var resourceId: String? = null

    private var weekAdapter: LeaderReserveWeekAdapter? = null

    private var timeAdapter: LeaderReserveTimeAdapter? = null

    private var resourceAdapter = StadiumResourceAdapter().apply {
        setOnItemClickListener { _, position ->
            index = position
            notifyDataSetChanged()
        }
    }

    private val calendar = Calendar.getInstance(Locale.CHINA)
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    private val defaultWeeks = arrayListOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
    override fun setLayout(): Int = R.layout.act_stadium_detail


    override fun initView() {
        defaultTitle("运动场")

        weekAdapter = LeaderReserveWeekAdapter().apply {
            setOnItemClickListener { _, position ->
                index = position
                timeAdapter?.clearData()
                timeAdapter?.notifyDataSetChanged()

                resourceAdapter.clearData()
                resourceAdapter.notifyDataSetChanged()
                notifyDataSetChanged()
                endDate = getEndData(getData(position).day)
                presenter.loadReserveTime(endDate, shopId, resourceId)
            }

        }
        timeAdapter = LeaderReserveTimeAdapter().apply {
            setOnItemClickListener { _, position ->
                index = position
                notifyDataSetChanged()

                resourceAdapter.clearData()

                list.groupBy { it.manageTime }[getData(position).manageTime]?.let { list ->
                    resourceAdapter.addData(list as ArrayList<MerchantTime>)
                }
                resourceAdapter.notifyDataSetChanged()
            }
        }

        val weekDayList = arrayListOf<WeekDayBean>()

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in currentDay..maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val week = defaultWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            weekDayList.add(WeekDayBean(week, i))
        }

        weekAdapter?.addData(weekDayList)

        recycler_date.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_date.adapter = weekAdapter
        recycler_date.addItemDecoration(ItemDecoration(true))

        recycler_time.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_time.adapter = timeAdapter
        recycler_time.addItemDecoration(ItemDecoration(false))

        recycler_resource.layoutManager = LinearLayoutManager(this)
        recycler_resource.adapter = resourceAdapter

        setOnClickListener(btn_reserve)
    }

    override fun onClickEvent(view: View) {
        when (view.id) {
            btn_reserve.id -> {
                resourceAdapter.getData(resourceAdapter.index).let {
                    presenter.commitReserve(
                        edit_name.text.toString(),
                        edit_phone.text.toString(),
                        it.beginTime ?: "",
                        it.endTime ?: "",
                        it.manageId.toString(),
                        "3",
                        shopId,
                        ManageBean(it.resourceId)
                    )
                }
            }
        }
    }

    override fun loadData() {
        intent.getStringExtra("shopId")?.let {
            presenter.loadReserveTime(getEndData(currentDay), shopId, resourceId)
        }
    }

    override fun close() {

    }


    override fun showReserveTime(data: ArrayList<MerchantTime>) {
        val index = data.indexOf(data.find { it.setNumber - it.reserveNumber > 0 })
        timeAdapter?.index = index
        timeAdapter?.clearData()
        timeAdapter?.addData(data)
        timeAdapter?.notifyDataSetChanged()

        resourceAdapter.clearData()
        data.groupBy { it.manageTime }.let {
            resourceAdapter.addData(it[data[0].manageTime] as ArrayList<MerchantTime>)
        }
        resourceAdapter.notifyDataSetChanged()
    }


    private fun getEndData(day: Int): String {
        val month = calendar.get(Calendar.MONTH) + 1

        return "${calendar.get(Calendar.YEAR)}-${if (month < 10) "0$month" else month}-$day"
    }


    inner class ItemDecoration(var boolean: Boolean) : RecyclerView.ItemDecoration() {
        private var borderMargin = resources.getDimension(R.dimen.dp_18).toInt()
        private var margin = resources.getDimension(R.dimen.dp_9).toInt()

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (boolean) {
                view.layoutParams.width = resources.getDimension(R.dimen.dp_44).toInt()
            }

            if (position == (parent.adapter?.itemCount ?: 0 - 1)) {
                outRect.right = borderMargin
            } else {
                outRect.right = margin
            }


            if (position == 0) {
                outRect.left = borderMargin
            } else {
                outRect.left = margin
            }
        }
    }
}