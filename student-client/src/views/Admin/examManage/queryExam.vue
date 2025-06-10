<template>
  <div>
    <el-card>
      <el-table
          :data="tableData"
          border
          style="width: 100%">
        <el-table-column
            fixed
            prop="cno"
            label="课程编号"
            width="100">
        </el-table-column>
        <el-table-column
            prop="day"
            label="考试日期"
            width="120">
        </el-table-column>
        <el-table-column
            prop="period"
            label="考试时段"
            width="100">
        </el-table-column>
        <el-table-column
            prop="durationMin"
            label="考试时长(分钟)"
            width="120">
        </el-table-column>
        <el-table-column
            prop="grade"
            label="考试总分"
            width="100">
        </el-table-column>
        <el-table-column
            prop="location"
            label="考试地点"
            width="120">
        </el-table-column>
        <el-table-column
            prop="Req"
            label="考试要求"
            width="200">
        </el-table-column>
        <el-table-column
            label="操作"
            width="150">
          <template slot-scope="scope">
            <el-popconfirm
                confirm-button-text="删除"
                cancel-button-text="取消"
                icon="el-icon-info"
                icon-color="red"
                title="删除不可复原"
                @confirm="deleteExam(scope.row)">
              <el-button slot="reference" type="text" size="small">删除</el-button>
            </el-popconfirm>
            <el-button @click="editExam(scope.row)" type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'AdminQueryExam',
  data() {
    return {
      tableData: [
        {
          cno: 2001,
          day: '2025-06-15',
          period: 4,
          durationMin: 120,
          grade: 100,
          location: 'YF409',
          Req: '遵守考场纪律，保持安静'
        },
        {
          cno: 2002,
          day: '2025-06-15',
          period: 1,
          durationMin: 120,
          grade: 100,
          location: 'YF505',
          Req: '携带学生证和文具，禁止作弊'
        },
        {
          cno: 2003,
          day: '2025-06-15',
          period: 6,
          durationMin: 120,
          grade: 100,
          location: 'YF404',
          Req: '独立完成考试，不得交头接耳'
        },
        {
          cno: 2004,
          day: '2025-06-15',
          period: 5,
          durationMin: 120,
          grade: 100,
          location: 'YF312',
          Req: '提前 15 分钟入场，关闭手机'
        },
        {
          cno: 2005,
          day: '2025-06-15',
          period: 7,
          durationMin: 120,
          grade: 100,
          location: null,
          Req: '认真审题，规范答题'
        },
        {
          cno: 2006,
          day: '2025-12-15',
          period: 5,
          durationMin: 120,
          grade: 100,
          location: 'YF312',
          Req: '提前 15 分钟入场，关闭手机'
        }
      ]
    };
  },
  created() {
    // 预留接口调用逻辑
    // const that = this;
    // axios.get('http://localhost:10086/exam/getAllExams').then(function (resp) {
    //   that.tableData = resp.data;
    // }).catch(function (e) {
    //   that.$message({
    //     showClose: true,
    //     message: '获取考试数据失败',
    //     type: 'error'
    //   });
    // });
  },
  methods: {
    deleteExam(row) {
      // 预留删除接口调用
      // const that = this;
      // axios.get('http://localhost:10086/exam/deleteById/' + row.cno).then(function (resp) {
      //   if (resp.data === true) {
      //     that.$message({
      //       showClose: true,
      //       message: '删除成功',
      //       type: 'success'
      //     });
      //     // 重新加载数据
      //   } else {
      //     that.$message({
      //       showClose: true,
      //       message: '删除失败，请检查数据库连接',
      //       type: 'error'
      //     });
      //   }
      // }).catch(function (e) {
      //   that.$message({
      //     showClose: true,
      //     message: '删除失败，存在外键依赖',
      //     type: 'error'
      //   });
      // });
      this.$message({
        showClose: true,
        message: '删除功能待实现',
        type: 'warning'
      });
    },
    editExam(row) {
      this.$router.push({
        path: '/editorExam',
        query: {
          cno: row.cno
        }
      });
    }
  }
}
</script>

<style scoped>
</style>