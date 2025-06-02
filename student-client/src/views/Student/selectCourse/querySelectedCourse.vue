<template>
  <div>
    <el-card>
      <el-table
          :data="tableData"
          border
          style="width: 100%">
        <el-table-column
            fixed
            prop="cid"
            label="课号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="cname"
            label="课程号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="tid"
            label="教师号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="tname"
            label="教师名称"
            width="150">
        </el-table-column>
        <el-table-column
            prop="ccredit"
            label="学分"
            width="150">
        </el-table-column>
        <el-table-column
            label="操作"
            width="100">

          <template slot-scope="scope">
            <el-popconfirm
                confirm-button-text='退课'
                cancel-button-text='取消'
                icon="el-icon-info"
                title="确定退课？"
                @confirm="deleteSCT(scope.row)"
            >
              <el-button slot="reference" type="text" size="small">退课</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          @current-change="changePage"
      >
      </el-pagination>
    </el-card>
<!--    &lt;!&ndash; 新增时间表 &ndash;&gt;-->
<!--    <el-card class="timetable-card" shadow="hover" style="margin-top: 20px;">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span>课程时间表</span>-->
<!--      </div>-->
<!--      <table border="1" cellspacing="0" cellpadding="5" style="width: 100%; text-align: center; border-collapse: collapse;">-->
<!--        <thead>-->
<!--        <tr>-->
<!--          <th>节次/星期</th>-->
<!--          <th v-for="dayName in dayNames" :key="dayName">{{ dayName }}</th>-->
<!--        </tr>-->
<!--        </thead>-->
<!--        <tbody>-->
<!--        <tr v-for="period in periods" :key="period">-->
<!--          <td>{{ period }}节</td>-->
<!--          <td v-for="day in days" :key="day">-->
<!--            <div v-if="getCourse(day, period)">-->
<!--              <strong>{{ getCourse(day, period).cname }}</strong><br>-->
<!--              {{ getCourse(day, period).location }}-->
<!--            </div>-->
<!--            <div v-else> </div>-->
<!--          </td>-->
<!--        </tr>-->
<!--        </tbody>-->
<!--      </table>-->
<!--    </el-card>-->
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: null,
      pageSize: 10,
      total: null,
      tmpList: null,
      type: sessionStorage.getItem('type'),

      dayNames: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      days: [1, 2, 3, 4, 5, 6, 7],       // 星期数字
      periods: [1, 2, 3, 4, 5, 6, 7],  // 节次数字

      courses: [],  // 课程数组，用于课表显示
    }
  },
  methods: {
    deleteSCT(row) {
      const cid = row.cid
      const tid = row.tid
      const sid = sessionStorage.getItem('sid')
      const term = sessionStorage.getItem('currentTerm')
      const sct = { cid, tid, sid, term }

      const that = this
      axios.post('http://localhost:10086/SCT/deleteBySCT', sct).then(function (resp) {
        if (resp.data === true) {
          that.$message({ showClose: true, message: '退课成功', type: 'success' })
          window.location.reload()
        } else {
          that.$message({ showClose: true, message: '退课失败，请联系管理员', type: 'error' })
        }
      })
    },
    changePage(page) {
      page = page - 1
      const that = this
      let start = page * that.pageSize, end = that.pageSize * (page + 1)
      let length = that.tmpList.length
      let ans = (end < length) ? end : length
      that.tableData = that.tmpList.slice(start, ans)
    },
    getCourse(day, period) {
      // 查找当天第period节课，注意day和period都是数字
      return this.courses.find(c => c.day === day && c.period === period) || null
    }
  },
  created() {
    const sid = sessionStorage.getItem('sid')
    const term = sessionStorage.getItem('currentTerm')
    const that = this

    // 获取学生选课表
    axios.get('http://localhost:10086/SCT/findBySid/' + sid + '/' + term).then(function (resp) {
      that.tmpList = resp.data
      that.total = resp.data.length
      let start = 0, end = that.pageSize
      let length = that.tmpList.length
      let ans = (end < length) ? end : length
      that.tableData = that.tmpList.slice(start, ans)

    })

  }
}
</script>
