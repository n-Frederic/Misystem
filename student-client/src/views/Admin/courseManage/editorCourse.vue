<template>
    <div>
        <el-form style="width: 60%" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="课程名称" prop="cname">
                <el-input v-model="ruleForm.cname" :value="ruleForm.cname"></el-input>
            </el-form-item>
            <el-form-item label="学分" prop="ccredit">
                <el-input v-model.number="ruleForm.ccredit" :value="ruleForm.ccredit"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
                <el-button @click="test">test</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
export default {
    data() {
        return {
            ruleForm: {
                cno: null,
                cname: null,
                ccredit: null
            },
            rules: {
                cname: [
                    { required: true, message: '请输入名称', trigger: 'blur' },
                ],
                ccredit: [
                    { required: true, message: '请输入学分', trigger: 'change' },
                    { type: 'number', message: '请输入数字', trigger: 'change'},
                ],
            }
        };
    },
    created() {
        const that = this
        if (this.$route.query.cno === undefined) {
            this.ruleForm.cno = 1
        }
        else {
            this.ruleForm.cno = this.$route.query.cno
        }
        axios.get('http://localhost:10086/course/findById/' + this.ruleForm.cno).then(function (resp) {
            that.ruleForm = resp.data[0]
            console.log(that.ruleForm)
        })
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    const that = this
                    console.log("提交数据:", {
                        cname: this.ruleForm.cname,
                        ccredit: this.ruleForm.ccredit
                    })

                    // 方案一：使用URLSearchParams
                    const params = new URLSearchParams()
                    params.append('cname', this.ruleForm.cname)
                    params.append('ccredit', this.ruleForm.ccredit)

                    axios.post("http://localhost:10086/course/updateCourse", params, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }).then(function (resp) {
                        if (resp.data === true) {
                            that.$message({
                                showClose: true,
                                message: '编辑成功',
                                type: 'success'
                            });
                        } else {
                            that.$message.error('编辑失败，请检查数据库');
                        }
                        that.$router.push("/queryCourse")
                    }).catch(function (error) {
                        console.error("提交失败:", error)
                        that.$message.error('提交失败，请检查网络连接');
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