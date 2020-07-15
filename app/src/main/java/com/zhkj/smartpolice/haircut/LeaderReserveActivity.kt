package com.zhkj.smartpolice.haircut

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.zy.base.BaseModel
import com.sunny.zy.utils.GlideApp
import com.sunny.zy.utils.ToastUtil
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.app.UrlConstant
import com.zhkj.smartpolice.base.UserManager
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveRecordAdapter
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveTimeAdapter
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveWeekAdapter
import com.zhkj.smartpolice.haircut.bean.ManageBean
import com.zhkj.smartpolice.haircut.bean.MerchantTime
import com.zhkj.smartpolice.merchant.model.MerchantContract
import com.zhkj.smartpolice.mine.bean.ReserveRecordBean
import com.zhkj.smartpolice.mine.model.ReserveContract
import com.zhkj.smartpolice.mine.model.ReservePresenter
import kotlinx.android.synthetic.main.act_leader_reserve.*
import kotlinx.android.synthetic.main.act_leader_reserve.btn_sure
import kotlinx.android.synthetic.main.act_leader_reserve.recycler_date
import kotlinx.android.synthetic.main.act_receive_time.recycler_time

class LeaderReserveActivity : HaircutOrderTimeActivity(), MerchantContract.IReserveView, ReserveContract.IReverseRecordView {

    val bean: ManageBean by lazy {
        (intent.getSerializableExtra("bean") as ManageBean)
    }

    private var endDate = ""

    private val recordPresenter: ReservePresenter by lazy {
        ReservePresenter(this)
    }

    private val leaderReserveRecordAdapter = LeaderReserveRecordAdapter()


    override fun setLayout(): Int = R.layout.act_leader_reserve

    override fun initView() {

        resourceId = bean.resourceId

        weekAdapter = LeaderReserveWeekAdapter().apply {
            setOnItemClickListener { _, position ->
                index = position
                timeAdapter.clearData()
                timeAdapter.notifyDataSetChanged()

                leaderReserveRecordAdapter.clearData()
                leaderReserveRecordAdapter.notifyDataSetChanged()

                notifyDataSetChanged()
                endDate = getEndData(getData(position).day)
                presenter.loadReserveTime(endDate, shopId, resourceId)
            }

        }
        timeAdapter = LeaderReserveTimeAdapter().apply {
            setOnItemClickListener { _, position ->
                index = position
                notifyDataSetChanged()

            }
        }

        tv_name.text = bean.resourceName
        tv_desc.text = bean.resourceContext

        GlideApp.with(this).load(UrlConstant.LOAD_IMAGE_PATH_URL + bean.imageId)
            .placeholder(R.drawable.svg_default_head)
            .into(iv_head)

        recycler_date.addItemDecoration(ItemDecoration(true))
        super.initView()
        recycler_time.addItemDecoration(ItemDecoration(false))

        recycler_time.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recycler_record.layoutManager = LinearLayoutManager(this)
        recycler_record.adapter = leaderReserveRecordAdapter

    }


    override fun onClickEvent(view: View) {
        when (view.id) {
            btn_sure.id -> {
                (timeAdapter as LeaderReserveTimeAdapter).let {
                    presenter.commitReserve(
                        UserManager.getUserBean().nickName ?: "",
                        UserManager.getUserBean().mobile ?: "",
                        it.getData(it.index).beginTime ?: "",
                        it.getData(it.index).endTime ?: "",
                        it.getData(it.index).manageId.toString(),
                        "1",
                        shopId,
                        bean
                    )
                }
            }
        }
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

    override fun showReserveTime(data: ArrayList<MerchantTime>) {
        super.showReserveTime(data)
        val manageId = data[(timeAdapter as LeaderReserveTimeAdapter).index].manageId
        recordPresenter.loadReverseRecord("1", "", manageId.toString(), endDate)
    }

    override fun reserveResult(data: BaseModel<Any>) {
        ToastUtil.show("预约成功！")
        finish()
    }

    override fun showReverseRecord(data: ArrayList<ReserveRecordBean>) {
        leaderReserveRecordAdapter.clearData()
        leaderReserveRecordAdapter.addData(data)
        leaderReserveRecordAdapter.notifyDataSetChanged()
    }
}