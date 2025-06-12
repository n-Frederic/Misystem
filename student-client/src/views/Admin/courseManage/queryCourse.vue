<template>
    <div>
        <el-container>
            <el-main>
                <el-card>
                    <el-form :inline="true" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                        <!-- 课程查询字段 -->
                        <el-form-item label="课程号" prop="cno">
                            <el-input v-model.number="ruleForm.cno"></el-input>
                        </el-form-item>
                        <el-form-item label="课程名" prop="cname">
                            <el-input v-model="ruleForm.cname"></el-input>
                        </el-form-item>
                        <el-form-item label="课程模糊查询" prop="cFuzzy">
                            <el-switch v-model="ruleForm.cFuzzy"></el-switch>
                        </el-form-item>
                        <el-form-item label="学分下限" prop="lowBound">
                            <el-input v-model.number="ruleForm.lowBound"></el-input>
                        </el-form-item>
                        <el-form-item label="学分上限" prop="highBound">
                            <el-input v-model.number="ruleForm.highBound"></el-input>
                        </el-form-item>

                        <!-- 新增：教师查询字段 -->
                        <el-form-item label="工号" prop="tid">
                            <el-input v-model.number="ruleForm.tid"></el-input>
                        </el-form-item>
                        <el-form-item label="教师名" prop="tname">
                            <el-input v-model="ruleForm.tname"></el-input>
                        </el-form-item>
                        <el-form-item label="教师模糊查询" prop="tFuzzy">
                            <el-switch v-model="ruleForm.tFuzzy"></el-switch>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" @click="resetForm('ruleForm')">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
                <el-card>
                    <course-list :ruleForm="ruleForm" :isActive="true"></course-list>
                </el-card>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import CourseList from "@/views/Admin/courseManage/courseList";

export default {
    components: { CourseList },
    data() {
        return {
            ruleForm: {
                // 原有的课程查询字段
                cno: null,
                cname: null,
                cFuzzy: true,
                lowBound: null,
                highBound: null,

                // 新增的教师查询字段
                tid: null,
                tname: null,
                tFuzzy: true
            },
            rules: {
                cno: [
                    { type: 'number', message: '必须是数字类型' }
                ],
                lowBound: [
                    { type: 'number', message: '必须是数字类型' }
                ],
                highBound: [
                    { type: 'number', message: '必须是数字类型' }
                ],
                // 新增教师字段的验证规则
                tid: [
                    { type: 'number', message: '必须是数字类型' }
                ]
            }
        };
    },
    methods: {
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
</script>