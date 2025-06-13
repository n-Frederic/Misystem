<template>
  <div>
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%">
      <el-table-column
          fixed
          prop="cno"
          label="课程号"
          width="150">
      </el-table-column>
      <el-table-column
          prop="cname"
          label="课程名"
          width="150">
      </el-table-column>
      <el-table-column
          prop="tname"
          label="教师名"
          width="100">
      </el-table-column>
      <el-table-column
          fixed
          prop="sid"
          label="学号"
          width="100">
      </el-table-column>
      <el-table-column
          prop="sname"
          label="学生名"
          width="100">
      </el-table-column>
      <el-table-column
          prop="daily_grade"
          label="成绩"
          width="100">
      </el-table-column>
      <el-table-column
          prop="term"
          label="学期"
          width="100">
      </el-table-column>
      <el-table-column
          label="操作"
          width="100">
        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="删除不可复原"
              @confirm="deleteGrade(scope.row)"
          >
            <el-button slot="reference" type="text" size="small">删除</el-button>
          </el-popconfirm>
          <el-button @click="editor(scope.row)" type="text" size="small">编辑</el-button>
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
  </div>
</template>

<script>
import axios from 'axios';

export default {
  methods: {
    select(row) {
      console.log(row);
    },
    deleteGrade(row) {
      const that = this;
      const sid = parseInt(row.sid); // 确保整数
      const cno = parseInt(row.cno);

      // 验证参数
      if (!sid || !cno || isNaN(sid) || isNaN(cno)) {
        that.$message.error('缺少或无效的学号/课程号');
        console.error('Invalid parameters:', { sid, cno });
        return;
      }

      // 删除确认
      axios.get(`http://localhost:10086/exams/deletegrade/${encodeURIComponent(sid)}/${encodeURIComponent(cno)}`)
          .then(resp => {
            console.log('deletegrade response:', resp.data);
            if (resp.data === true) {
              that.$message({ showClose: true, message: '删除成功', type: 'success' });
              that.refreshTable();
            } else {
              that.$message.error('删除失败，请检查数据库');
            }
          })
          .catch(error => {
            console.error('deletegrade error:', {
              message: error.message,
              response: error.response,
              request: error.request
            });
            that.$message.error(`删除失败：${error.message || '网络错误'}`);
          });
    },
    changePage(page) {
      page = page - 1;
      const that = this;
      let start = page * that.pageSize, end = that.pageSize * (page + 1);
      let length = that.tmpList.length;
      let ans = (end < length) ? end : length;
      that.tableData = that.tmpList.slice(start, ans);
    },
    editor(row) {
      this.$router.push({
        path: '/editorGradeCourse',
        query: {
          cno: row.cno,
          sid: row.sid,
          term: row.term,
          cname: row.cname,
          tname: row.tname,
          sname: row.sname
        }
      });
    },
    refreshTable() {
      const that = this;
      axios.post('http://localhost:10086/SC/findBySearch', this.ruleForm)
          .then(resp => {
            console.log('findBySearch response:', resp.data);
            that.tmpList = resp.data;
            that.total = resp.data.length;
            let start = 0, end = that.pageSize;
            let length = that.tmpList.length;
            let ans = (end < length) ? end : length;
            that.tableData = that.tmpList.slice(start, ans);
          })
          .catch(error => {
            console.error('findBySearch error:', error);
            that.$message.error(`查询失败：${error.message || '网络错误'}`);
          });
    }
  },
  data() {
    return {
      tableData: null,
      pageSize: 10,
      total: null,
      tmpList: null
    };
  },
  props: {
    ruleForm: Object
  },
  watch: {
    ruleForm: {
      handler(newRuleForm) {
        console.log('组件监听 form:', newRuleForm);
        const that = this;
        that.tmpList = null;
        that.total = null;
        that.tableData = null;
        axios.post('http://localhost:10086/SC/findBySearch', newRuleForm)
            .then(resp => {
              console.log('查询结果:', resp.data);
              that.tmpList = resp.data;
              that.total = resp.data.length;
              let start = 0, end = that.pageSize;
              let length = that.tmpList.length;
              let ans = (end < length) ? end : length;
              that.tableData = that.tmpList.slice(start, ans);
            })
            .catch(error => {
              console.error('findBySearch error:', error);
              that.$message.error(`查询失败：${error.message || '网络错误'}`);
            });
      },
      deep: true,
      immediate: true
    }
  }
};
</script>
