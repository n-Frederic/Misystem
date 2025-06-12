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
                        :key="semester.value"     :label="semester.label"   :value="semester.value">  </el-option>
                </el-select>
                <el-input v-model="searchForm.examName" placeholder="考试名称" clearable></el-input>
                <el-input v-model="searchForm.courseName" placeholder="考试课程" clearable></el-input>
                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <div style="margin-bottom: 15px; padding: 10px; border: 1px dashed #ccc; background-color: #f9f9f9; font-size: 14px;">
                <p>当前请求 SID: <span style="font-weight: bold; color: blue;">{{ debugSid || '未获取' }}</span></p>
                <p>当前请求 Term: <span style="font-weight: bold; color: blue;">{{ debugTerm || '未获取' }}</span></p>
                <p style="color: gray;">(这些值将在发送考试安排请求时使用)</p>
            </div>

            <el-table :data="filteredExams" style="width: 100%" border>
                <el-table-column
                    prop="cno"
                    label="序号"
                    width="80">
                </el-table-column>
                <el-table-column
                    prop="cname"
                    label="考试"
                    width="250">
                </el-table-column>
                <el-table-column
                    prop="day"
                    label="日期"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="period"
                    label="时间">
                </el-table-column>
                <el-table-column
                    prop="durationMin"
                    label="时长">
                </el-table-column>
                <el-table-column
                    prop="grade"
                    label="满分"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="location"
                    label="地点"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="req"
                    label="考试要求"
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

            // 新增用于在页面上显示调试信息的属性
            debugSid: '',
            debugTerm: '',
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
                // 假设后端获取学期列表的接口是 /SCT/findAllTerm
                const response = await axios.get('http://localhost:10086/SCT/findAllTerm');
                // **核心修改：将后端返回的字符串转换为 { label, value } 对象**
                this.semesterOptions = response.data.map(termString => {
                    const parts = termString.split('-'); // 例如 "25", "春季学期"
                    const yearSuffix = parts[0]; // "25"
                    const season = parts[1]; // "春季学期"

                    // 推断完整年份，例如 25 转换为 2025
                    // 这里假设都是当前世纪的年份，如果跨世纪需要更复杂的逻辑
                    const currentYear = new Date().getFullYear(); // 2025
                    const centuryPrefix = String(currentYear).substring(0, 2); // "20"
                    const fullYear = parseInt(`${centuryPrefix}${yearSuffix}`);

                    let semesterValue = '';
                    if (season.includes('春季')) {
                        semesterValue = '1';
                    } else if (season.includes('秋季')) {
                        semesterValue = '2';
                    } else if (season.includes('夏季')) { // 如果有夏季学期
                        semesterValue = '3';
                    }
                    // 注意：这里假设后端期望的格式是 "YYYY-S"
                    return {
                        label: termString,              // 例如 "25-春季学期"
                        value: `${fullYear}-${semesterValue}` // 例如 "2025-1"
                    };
                }).sort((a, b) => { // 排序以确保学期顺序正确，例如最新学期在前
                    const yearA = parseInt(a.value.split('-')[0]);
                    const yearB = parseInt(b.value.split('-')[0]);
                    const semA = parseInt(a.value.split('-')[1]);
                    const semB = parseInt(b.value.split('-')[1]);
                    if (yearA !== yearB) return yearB - yearA; // 年份降序
                    return semB - semA; // 同一年份学期降序
                });


                // 如果当前没有选择学期，则默认选中第一个学期（转换后的 value）
                if (!this.searchForm.yearSemester && this.semesterOptions.length > 0) {
                    this.searchForm.yearSemester = this.semesterOptions[0].value;
                }
            } catch (error) {
                console.error('获取学期信息失败:', error);
                this.$message.error('获取学期信息失败，请检查网络或后端服务');
                if (!this.searchForm.yearSemester) {
                    this.searchForm.yearSemester = '2024-2025-2';
                    sessionStorage.setItem('currentTerm', this.searchForm.yearSemester);
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
                const sid = sessionStorage.getItem('sid');
                const term = this.searchForm.yearSemester;

                // **显示在页面上的调试信息**
                this.debugSid = sid;
                this.debugTerm = term;

                // 调试输出到控制台
                console.log('DEBUG: 准备发送考试安排请求 - SID:', sid, 'Term:', term);

                // **严格检查 sid 和 term 是否存在，避免拼接出 "null" 或 "undefined"**
                if (!sid) {
                    this.$message.error('学生ID缺失，请重新登录。');
                    this.allExams = [];
                    this.applyFilters();
                    return; // 提前退出
                }
                if (!term) {
                    this.$message.error('学期信息缺失，无法获取考试安排。');
                    this.allExams = [];
                    this.applyFilters();
                    return; // 提前退出
                }

                // **确认已将 axios.post 改为 axios.get**
                // 使用模板字符串拼接 URL 更清晰
                const response = await axios.get(`http://localhost:10086/exams/findExam/${sid}/${term}`);

                this.allExams = response.data; // 假设后端返回的是已过滤的考试列表
                this.applyFilters(); // 即使后端已过滤，这里也调用一次，以防万一或用于进一步客户端过滤
                this.$message.success('考试安排数据获取成功！'); // 成功提示
            } catch (error) {
                console.error('获取考试安排数据失败:', error);
                this.$message.error('获取考试安排信息失败，请检查网络或后端服务');
                this.allExams = []; // 清空数据
                this.applyFilters();
            }
        },

        /**
         * 处理查询按钮点击事件或学期变化
         */
        handleSearch() {
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