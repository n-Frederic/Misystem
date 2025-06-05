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
                <el-select v-model="searchForm.yearSemester" placeholder="请选择学期">
                    <el-option label="2024-2025-2" value="2024-2025-2"></el-option>
                </el-select>
                <el-input v-model="searchForm.examName" placeholder="考试名称" clearable></el-input>
                <el-input v-model="searchForm.courseName" placeholder="考试课程" clearable></el-input>
                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <el-table :data="filteredExams" style="width: 100%" border>
                <el-table-column prop="examNumber" label="序号" width="80"></el-table-column>
                <el-table-column prop="examName" label="考试" width="250"></el-table-column>
                <el-table-column prop="course" label="课程" width="200"></el-table-column>
                <el-table-column prop="venue" label="时间地点"></el-table-column>
                <el-table-column prop="examMethod" label="考试方式" width="100"></el-table-column>
                <el-table-column prop="remarks" label="备注" width="150"></el-table-column>
                <el-table-column prop="registrationInfo" label="报名信息" width="100"></el-table-column>
                <el-table-column prop="examStatus" label="考试状态" width="100"></el-table-column>
                <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<script>
export default {
    name: 'ExamSchedule',
    data() {
        return {
            searchForm: {
                yearSemester: '2024-2025-2', // 默认选中
                examName: '',
                courseName: '',
            },
            allExams: [
                // 这些数据通常会来自API调用
                {
                    examNumber: 1,
                    examName: '[2024-2025-2] 2025 春季学期期末考试',
                    course: '算法设计与分析 [03]',
                    venue: '2025/07/03 09:00-11:00 源西楼 SX106',
                    examMethod: '闭卷',
                    remarks: '可以带计算器',
                    registrationInfo: '不需报名',
                    examStatus: '正在进行',
                },
                {
                    examNumber: 2,
                    examName: '[2024-2025-2] 2025 春季学期期末考试',
                    course: '软件系统分析与设计 [01]',
                    venue: '2025/06/30 14:30-16:30 源西楼 SX302',
                    examMethod: '闭卷',
                    remarks: '-',
                    registrationInfo: '不需报名',
                    examStatus: '正在进行',
                },
                {
                    examNumber: 3,
                    examName: '[2024-2025-2] 2025 春季学期期末考试',
                    course: '习近平新时代中国特色社会主义思想概论 [09]',
                    venue: '2025/06/26 09:00-11:00 未教学楼 YF310',
                    examMethod: '闭卷',
                    remarks: '-',
                    registrationInfo: '不需报名',
                    examStatus: '正在进行',
                },
                {
                    examNumber: 4,
                    examName: '[2024-2025-2] 2025 春季学期期末考试',
                    course: '大数据概论 [03]',
                    venue: '2025/06/25 14:30-16:30 源西楼 SX203',
                    examMethod: '开卷',
                    remarks: '可以带计算器',
                    registrationInfo: '不需报名',
                    examStatus: '正在进行',
                },
                // ... 更多考试数据
            ],
            filteredExams: [],
        };
    },
    created() {
        // 在实际应用中，你会在这里获取数据
        this.fetchExamData();
    },
    methods: {
        fetchExamData() {
            // 模拟API调用
            // 在真实场景中，使用 axios 或 fetch:
            // axios.get('/api/exams').then(response => {
            //   this.allExams = response.data;
            //   this.applyFilters();
            // });
            this.applyFilters(); // 应用初始过滤器以显示所有数据
        },
        handleSearch() {
            this.applyFilters();
        },
        applyFilters() {
            this.filteredExams = this.allExams.filter(exam => {
                const matchesYearSemester = this.searchForm.yearSemester ? exam.examName.includes(this.searchForm.yearSemester) : true;
                const matchesExamName = this.searchForm.examName ? exam.examName.includes(this.searchForm.examName) : true;
                const matchesCourseName = this.searchForm.courseName ? exam.course.includes(this.searchForm.courseName) : true;
                return matchesYearSemester && matchesExamName && matchesCourseName;
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