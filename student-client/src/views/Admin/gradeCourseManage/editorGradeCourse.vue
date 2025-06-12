<template>
  <div>
    <el-card>
      <el-form style="width: 60%" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="课程名称" prop="cname">
          <el-input v-model="ruleForm.cname" :value="ruleForm.cname" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="教师名" prop="tname">
          <el-input v-model="ruleForm.tname" :value="ruleForm.tname" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="学生名" prop="sname">
          <el-input v-model="ruleForm.sname" :value="ruleForm.sname" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="分数" prop="grade">
          <el-input v-model.number="ruleForm.grade" :value="ruleForm.grade"></el-input>
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
export default {
  data() {
    var checkGrade = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('成绩不能为空'));
      }
      if (!Number.isInteger(value)) {
        callback(new Error('请输入数字值'));
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
        tid: null,
        tname: null,
      },
      rules: {
        grade: [
          { required: true, message: '请输入学分', trigger: 'change'},
          { type: 'number', message: '请输入数字', trigger: 'change'},
          { validator: checkGrade, trigger: 'blur'}
        ],
      }
    };
  },
  created() {
    const that = this
    this.ruleForm.cno = this.$route.query.cno
    this.ruleForm.tid = this.$route.query.tid
    this.ruleForm.sid = this.$route.query.sid
    this.ruleForm.term = this.$route.query.term
    this.ruleForm.cname = this.$route.query.cname
    this.ruleForm.tname = this.$route.query.tname
    this.ruleForm.sname = this.$route.query.sname
    axios.get(`http://localhost:10086/SC/findById/${this.ruleForm.sid}/${this.ruleForm.cno}/${this.ruleForm.tid}/${this.ruleForm.term}`)
        .then(function (resp) {
          that.ruleForm.grade = resp.data.grade || null; // 只更新 grade
        })
        .catch(error => {
          that.$message.error('获取成绩数据失败，请检查网络或后端服务');
          that.ruleForm.grade = null;
        });
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 通过前端校验
          const that = this
          const sid = that.ruleForm.sid
          const cno = that.ruleForm.cno
          // const tid = that.ruleForm.tid
          const term = that.ruleForm.term
          const grade = that.ruleForm.grade
          axios.get("http://localhost:10086/SC/updateById/" + sid + '/' + cno + '/' + tid + '/' + term + '/' + grade).then(function (resp) {
            if (resp.data === true) {
              that.$message({
                showClose: true,
                message: '编辑成功',
                type: 'success'
              });
            }
            else {
              that.$message.error('编辑失败，请检查数据库');
            }
            if (sessionStorage.getItem('type') === 'admin') {
              that.$router.push("/queryGradeCourse")
            } else {
              that.$router.push("/teacherQueryGradeCourseManage")
            }
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    test() {
      console.log(this.ruleForm)
    }
  }
}
</script>