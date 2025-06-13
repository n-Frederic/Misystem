<template>
  <div>
    <el-card>
      <el-form style="width: 60%" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="课程名称" prop="cname">
          <el-input v-model="ruleForm.cname" :value="ruleForm.cname || '未知'" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="教师名" prop="tname">
          <el-input v-model="ruleForm.tname" :value="ruleForm.tname || '未知'" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="学生名" prop="sname">
          <el-input v-model="ruleForm.sname" :value="ruleForm.sname || '未知'" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="分数" prop="grade">
          <el-input v-model.number="ruleForm.grade" placeholder="请输入成绩"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="test">test</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    var checkGrade = (rule, value, callback) => {
      if (value === null || value === undefined || value === '') {
        return callback(new Error('成绩不能为空'));
      }
      if (!Number.isInteger(value)) {
        callback(new Error('请输入整数值'));
      } else {
        if (value > 100 || value < 0) {
          callback(new Error('成绩范围 [0 - 100]'));
        } else {
          callback();
        }
      }
    };
    return {
      ruleForm: {
        cno: null,
        cname: null,
        grade: null,
        sid: null,
        sname: null,
        tname: null,
        term: null
      },
      rules: {
        grade: [
          { required: true, message: '请输入成绩', trigger: 'change' },
          { type: 'number', message: '请输入数字', trigger: 'change' },
          { validator: checkGrade, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    console.log('Query 参数:', this.$route.query);
    if (!this.$route.query.cno || !this.$route.query.sid || !this.$route.query.term) {
      this.$message.error('缺少必要参数，无法加载编辑页面');
      this.$router.go(-1);
      return;
    }
    this.ruleForm.cno = parseInt(this.$route.query.cno, 10) || null;
    this.ruleForm.sid = parseInt(this.$route.query.sid, 10) || null;
    this.ruleForm.term = String(this.$route.query.term);
    this.ruleForm.cname = this.$route.query.cname || '未知';
    this.ruleForm.tname = this.$route.query.tname || '未知';
    this.ruleForm.sname = this.$route.query.sname || '未知';
    this.ruleForm.grade = null; // 用户输入新成绩
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const that = this;
          const { sid, cno, term, grade } = that.ruleForm;
          if (!sid || !cno || !term || (grade === null || grade === undefined)) {
            that.$message.error('提交失败：参数不完整');
            return;
          }
          const params = {
            sid: parseInt(sid, 10),
            cno: parseInt(cno, 10),
            term: String(term),
            grade: parseInt(grade, 10)
          };
          console.log('提交参数:', params);
          axios.get(`http://localhost:10086/SC/updateById/${encodeURIComponent(params.sid)}/${encodeURIComponent(params.cno)}/${encodeURIComponent(params.term)}/${encodeURIComponent(params.grade)}`)
              .then(function (resp) {
                console.log('提交响应:', resp.data);
                if (resp.data === true) {
                  that.$message({
                    showClose: true,
                    message: '编辑成功',
                    type: 'success'
                  });
                  if (sessionStorage.getItem('type') === 'admin') {
                    that.$router.push('/queryGradeCourse');
                  } else {
                    that.$router.push('/teacherQueryGradeCourseManage');
                  }
                } else {
                  that.$message.error('编辑失败：请检查数据库');
                }
              })
              .catch(error => {
                console.error('提交错误:', error.response?.data, error.message);
                that.$message.error(`编辑失败：${error.response?.data?.message || '请检查网络或后端服务'}`);
                that.$router.go(-1);
              });
        } else {
          console.log('校验失败');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.ruleForm = {
        cno: parseInt(this.$route.query.cno, 10) || null,
        sid: parseInt(this.$route.query.sid, 10) || null,
        term: String(this.$route.query.term),
        cname: this.$route.query.cname || '未知',
        tname: this.$route.query.tname || '未知',
        sname: this.$route.query.sname || '未知',
        grade: null
      };
      this.$refs[formName].resetFields();
    },
    test() {
      console.log(this.ruleForm);
    }
  }
};
</script>