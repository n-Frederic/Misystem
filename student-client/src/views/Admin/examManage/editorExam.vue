<template>
  <div>
    <el-card>
      <el-form style="width: 60%" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="课程编号" prop="cno">
          <el-input v-model="ruleForm.cno" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="考试日期" prop="day">
          <el-date-picker v-model="ruleForm.day" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="考试时段" prop="period">
          <el-input v-model.number="ruleForm.period"></el-input>
        </el-form-item>
        <el-form-item label="考试时长" prop="durationMin">
          <el-input v-model.number="ruleForm.durationMin"></el-input>
        </el-form-item>
        <el-form-item label="考试总分" prop="grade">
          <el-input v-model.number="ruleForm.grade"></el-input>
        </el-form-item>
        <el-form-item label="考试地点" prop="location">
          <el-input v-model="ruleForm.location"></el-input>
        </el-form-item>
        <el-form-item label="考试要求" prop="Req">
          <el-input v-model="ruleForm.Req" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="$router.push('/queryExam')">返回查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'EditorExam',
  data() {
    const checkNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入值'));
      }
      if (!Number.isInteger(value)) {
        callback(new Error('请输入整数'));
      } else {
        callback();
      }
    };
    const checkGrade = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入考试总分'));
      }
      if (!Number.isInteger(value) || value < 0 || value > 100) {
        callback(new Error('总分必须是 0-100 的整数'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        cno: null,
        day: null,
        period: null,
        durationMin: null,
        grade: null,
        location: null,
        Req: null
      },
      rules: {
        day: [{ required: true, message: '请选择考试日期', trigger: 'change' }],
        period: [
          { required: true, message: '请输入考试时段', trigger: 'change' },
          { validator: checkNumber, trigger: 'blur' }
        ],
        durationMin: [
          { required: true, message: '请输入考试时长', trigger: 'change' },
          { validator: checkNumber, trigger: 'blur' }
        ],
        grade: [
          { required: true, message: '请输入考试总分', trigger: 'change' },
          { validator: checkGrade, trigger: 'blur' }
        ],
        location: [{ required: false, message: '请输入考试地点', trigger: 'change' }],
        Req: [{ required: true, message: '请输入考试要求', trigger: 'change' }]
      }
    };
  },
  created() {
    // 从路由获取 cno
    this.ruleForm.cno = this.$route.query.cno;
    // 预留接口调用：获取考试详情
    // const that = this;
    // axios.get('http://localhost:10086/exams/findById/' + this.ruleForm.cno).then(function (resp) {
    //   that.ruleForm = resp.data;
    // }).catch(function (e) {
    //   that.$message({
    //     showClose: true,
    //     message: '获取考试信息失败',
    //     type: 'error'
    //   });
    // });
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 预留提交接口
          // const that = this;
          // axios.post('http://localhost:10086/exams/update', that.ruleForm).then(function (resp) {
          //   if (resp.data === true) {
          //     that.$message({
          //       showClose: true,
          //       message: '编辑成功',
          //       type: 'success'
          //     });
          //     that.$router.push('/queryExam');
          //   } else {
          //     that.$message({
          //       showClose: true,
          //       message: '编辑失败，请检查数据库',
          //       type: 'error'
          //     });
          //   }
          // }).catch(function (e) {
          //   that.$message({
          //     showClose: true,
          //     message: '编辑失败，网络错误',
          //     type: 'error'
          //   });
          // });
          this.$message({
            showClose: true,
            message: '提交功能待实现',
            type: 'warning'
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
</style>