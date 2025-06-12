<template>
  <div>
    <el-card>
      <el-form style="width: 60%" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
        <el-form-item label="课程编号" prop="cno">
          <el-input v-model.number="ruleForm.cno" type="number"></el-input>
        </el-form-item>
        <el-form-item label="考试日期" prop="day">
          <el-date-picker
              v-model="ruleForm.day"
              type="date"
              placeholder="选择日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="考试时段" prop="period">
          <el-input v-model.number="ruleForm.period" type="number"></el-input>
        </el-form-item>
        <el-form-item label="考试时长" prop="durationMin">
          <el-input v-model.number="ruleForm.durationMin" type="number"></el-input>
        </el-form-item>
        <el-form-item label="考试总分" prop="grade">
          <el-input v-model.number="ruleForm.grade" type="number"></el-input>
        </el-form-item>
        <el-form-item label="考试地点" prop="location">
          <el-input v-model="ruleForm.location"></el-input>
        </el-form-item>
        <el-form-item label="考试要求" prop="req">
          <el-input v-model="ruleForm.req" type="textarea"></el-input>
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
import axios from 'axios';

export default {
  name: 'AddExam',
  data() {
    const checkNumber = (rule, value, callback) => {
      if (value === null || value === '') return callback(new Error('请输入值'));
      if (!Number.isInteger(value)) callback(new Error('请输入整数'));
      else callback();
    };
    const checkGrade = (rule, value, callback) => {
      if (value === null || value === '') return callback(new Error('请输入考试总分'));
      if (isNaN(value) || value < 0 || value > 100) callback(new Error('总分必须是 0-100 的数字'));
      else callback();
    };
    return {
      ruleForm: {
        cno: null,
        day: null,
        period: null,
        durationMin: null,
        grade: null,
        location: null,
        req: null
      },
      rules: {
        cno: [
          {required: true, message: '请输入课程编号', trigger: 'change'},
          {type: 'number', message: '请输入数字', trigger: 'change'}
        ],
        day: [
          {required: true, message: '请选择考试日期', trigger: 'change'}
        ],
        period: [
          {required: true, message: '请输入考试时段', trigger: 'change'},
          {validator: checkNumber, trigger: 'blur'}
        ],
        durationMin: [
          {required: true, message: '请输入考试时长', trigger: 'change'},
          {validator: checkNumber, trigger: 'blur'}
        ],
        grade: [
          {required: true, message: '请输入考试总分', trigger: 'change'},
          {validator: checkGrade, trigger: 'blur'}
        ],
        location: [
          {required: false, message: '请输入考试地点', trigger: 'change'}
        ],
        req: [
          {required: true, message: '请输入考试要求', trigger: 'change'}
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          axios.post('http://localhost:10086/exams/add', this.ruleForm).then(resp => {
            console.log('Response:', resp.data); // 调试日志
            if (resp.data.success) {
              this.$message({showClose: true, message: '发布成功', type: 'success'});
              this.$router.push('/queryExam');
            } else {
              this.$message({showClose: true, message: resp.data.message || '发布失败', type: 'error'});
            }
          }).catch(e => {
            console.error('Error:', e); // 调试日志
            this.$message({showClose: true, message: '发布失败: ' + e.message, type: 'error'});
          });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.ruleForm = {
        cno: null,
        day: null,
        period: null,
        durationMin: null,
        grade: null,
        location: null,
        req: null
      };
    }
  }
}
</script>

<style scoped>
</style>