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
    axios.get('http://localhost:10086/SC/findById/' +
        this.ruleForm.sid + '/' +
        this.ruleForm.cno + '/' +
        this.ruleForm.tid + '/' +
        this.ruleForm.term).then(function (resp) {
      that.ruleForm = resp.data
    })
  },
  methods: {
      submitForm(formName) {
          console.log("点击了提交按钮") // 1. 确认方法被调用
          console.log("当前表单数据:", this.ruleForm) // 2. 检查数据

          this.$refs[formName].validate((valid) => {
              console.log("表单验证结果:", valid) // 3. 检查验证是否通过

              if (valid) {
                  console.log("通过验证，开始提交")
                  const that = this
                  const sid = that.ruleForm.sid
                  const cno = that.ruleForm.cno
                  const term = that.ruleForm.term
                  const grade = that.ruleForm.grade

                  console.log("提交参数:", {sid, cno, term, grade}) // 4. 检查参数

                  axios.get("http://localhost:10086/SC/updateById/" + sid + '/' + cno + '/' + term + '/' + grade)
                      .then(function (resp) {
                          console.log("请求成功:", resp.data) // 5. 检查响应
                          if (resp.data === true) {
                              that.$message({
                                  showClose: true,
                                  message: '编辑成功',
                                  type: 'success'
                              });
                          } else {
                              that.$message.error('编辑失败，请检查数据库');
                          }
                          that.$router.push("/queryGradeCourse")
                      })
                      .catch(function (error) {
                          console.error("请求失败:", error) // 6. 捕获错误
                          that.$message.error('请求失败: ' + error.message);
                      })
              } else {
                  console.log("表单验证失败") // 7. 验证失败的情况
                  return false;
              }
          });
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    test() {
      console.log(this.ruleForm)
    }

}
</script>