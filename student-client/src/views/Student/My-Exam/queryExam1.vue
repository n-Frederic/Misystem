<template>
    <div class="exam-schedule-container">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>考试安排信息</span>
            </div>
            <div class="header-notes">
                <p>1. 考试时间地点信息在 <span style="color: red;">报名完成后</span> 由教务处统一安排</p>
                <p>2. 逾期未报名，将无法参加考试，请密切关注相关考试安排</p>
            </div>

            <div class="search-panel">
                <el-select v-model="searchForm.yearSemester" placeholder="请选择学期" @change="handleSearch">
                    <el-option
                            v-for="semester in semesterOptions"
                            :key="semester"
                            :label="semester"
                            :value="semester">
                    </el-option>
                </el-select>
                <el-input v-model="searchForm.examName" placeholder="考试名称" clearable></el-input>
                <el-input v-model="searchForm.courseName" placeholder="考试课程" clearable></el-input>
                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <el-table :data="filteredExams" style="width: 100%" border>
                <el-table-column
                        prop="examNumber"
                        label="序号"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="examName"
                        label="考试"
                        width="250">
                </el-table-column>
                <el-table-column
                        prop="course"
                        label="课程"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="Time"
                        label="时间">
                </el-table-column>
                <el-table-column
                        prop="venue"
                        label="地点">
                </el-table-column>
                <el-table-column
                        prop="examMethod"
                        label="考试方式"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="remarks"
                        label="备注"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="registrationInfo"
                        label="报名信息"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="examStatus"
                        label="考试状态"
                        width="100">
                </el-table-column>
                <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<script>
import axios from 'axios'; // 导入 axios

export default {
    name: 'ExamSchedule',
    data() {
        return {
            searchForm: {
                yearSemester: '', // 初始为空，将由后端数据填充或设置为默认值
                examName: '',
                courseName: '',
            },
            allExams: [], // 考试数据将从后端获取
            filteredExams: [],
            semesterOptions: [], // 用于存储所有学期选项
        };
    },
    created() {
        this.fetchSemesters(); // 组件创建时首先获取学期信息
        // 考试数据会在 `searchForm.yearSemester` 发生变化时通过 `watch` 触发 `fetchExamData`
    },
    watch: {
        // 监听 searchForm.yearSemester 的变化，一旦有了值就立即获取考试数据
        'searchForm.yearSemester': {
            handler(newVal) {
                if (newVal) { // 只有当学期有值时才去获取考试数据
                    this.fetchExamData();
                }
            },
            immediate: true // 立即执行一次 handler，以便组件初次加载时根据默认学期获取数据
        }
    },
    methods: {
        /**
         * 获取所有学期信息
         */
        async fetchSemesters() {
            try {
                // 假设后端获取学期列表的接口是 /semesters
                const response = await axios.get('http://localhost:10086/SCT/findAllTerm');
                this.semesterOptions = response.data; // 假设返回的是一个字符串数组，例如 ["2024-2025-2", "2023-2024-1"]

                // 如果当前没有选择学期，则默认选中第一个学期
                if (!this.searchForm.yearSemester && this.semesterOptions.length > 0) {
                    this.searchForm.yearSemester = this.semesterOptions[0];
                }
            } catch (error) {
                console.error('获取学期信息失败:', error);
                this.$message.error('获取学期信息失败，请检查网络或后端服务');
                // 如果API调用失败，可以提供一个硬编码的默认学期作为回退
                this.semesterOptions = ['2024-2025-2'];
                if (!this.searchForm.yearSemester) {
                    this.searchForm.yearSemester = '2024-2025-2';
                }
            }
        },

        /**
         * 根据查询条件获取考试安排信息
         */
        async fetchExamData() {
            if (!this.searchForm.yearSemester) {
                // 如果没有选择学期，则不清空数据也不发送请求
                this.allExams = [];
                this.filteredExams = [];
                return;
            }
            try {
                const response = await axios.get('http://localhost:10086/exams/findExam', {
                    params: {
                        yearSemester: this.searchForm.yearSemester,
                        examName: this.searchForm.examName,
                        courseName: this.searchForm.courseName,
                    },
                });
                this.allExams = response.data; // 假设后端返回的是已过滤的考试列表
                this.applyFilters(); // 即使后端已过滤，这里也调用一次，以防万一或用于进一步客户端过滤
            } catch (error) {
                console.error('获取考试安排数据失败:', error);
                this.$message.error('获取考试安排信息失败，请检查网络或后端服务');
                this.allExams = []; // 清空数据
                this.applyFilters();
            }
        },

        /**
         * 处理查询按钮点击事件
         */
        handleSearch() {
            // 当点击查询按钮或学期选择框变化时，重新从后端获取数据
            this.fetchExamData();
        },

        /**
         * 应用客户端过滤器（如果后端未完全过滤）
         */
        applyFilters() {
            // 注意：如果后端已经根据 examName 和 courseName 进行了精确过滤，
            // 这里的客户端过滤可能就不再需要。
            // 但为了兼容性，保留此方法作为最终的客户端筛选。
            this.filteredExams = this.allExams.filter(exam => {
                const matchesExamName = this.searchForm.examName ? exam.examName.includes(this.searchForm.examName) : true;
                const matchesCourseName = this.searchForm.courseName ? exam.course.includes(this.searchForm.courseName) : true;
                // 注意：yearSemester 的过滤已经在 fetchExamData 中由后端完成，所以这里不再重复过滤
                return matchesExamName && matchesCourseName;
            });
        },
        // handleOperation(row) {
        //   console.log('操作行:', row);
        //   // 根据实际需求处理操作，例如跳转到详情页
        // },
    },
};
</script>

<style scoped>
.exam-schedule-container {
    padding: 20px;
}

.header-notes {
    margin-bottom: 20px;
    background-color: #f0f9eb; /* Light green background for notes */
    border-left: 5px solid #67c23a; /* Green border */
    padding: 10px 15px;
    color: #606266;
    font-size: 14px;
}

.header-notes p {
    margin: 5px 0;
}

.search-panel {
    display: flex;
    gap: 10px; /* Space between search elements */
    margin-bottom: 20px;
    align-items: center;
}

.search-panel .el-input,
.search-panel .el-select {
    width: 200px; /* Adjust width as needed */
}
</style>