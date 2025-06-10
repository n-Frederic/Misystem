<template>
    <div class="schedule-container">
        <div class="header-controls">
            <span class="label">选择学期：</span>
            <el-select v-model="selectedTerm" placeholder="请选择学期" @change="fetchStudentSchedule">
                <el-option
                        v-for="term in termList"
                        :key="term"
                        :label="term"
                        :value="term">
                </el-option>
            </el-select>
        </div>

        <div class="schedule-table">
            <div class="schedule-header">
                <div class="schedule-cell empty-cell"></div>
                <div class="schedule-cell day-header" v-for="day in daysOfWeek" :key="day">{{ day }}</div>
            </div>

            <div class="schedule-row" v-for="(session, sessionIndex) in sessionsOfDay" :key="sessionIndex">
                <div class="schedule-cell session-header">
                    <div class="session-name">{{ session.name }}</div>
                    <div class="session-time">{{ session.time }}</div>
                </div>
                <div
                        class="schedule-cell course-cell"
                        v-for="(day, dayIndex) in daysOfWeek"
                        :key="dayIndex"
                >
                    <div v-if="getCourseInfo(dayIndex, sessionIndex)">
                        <p class="course-id">{{ getCourseInfo(dayIndex, sessionIndex).cid }}</p>
                        <p class="course-name">{{ getCourseInfo(dayIndex, sessionIndex).cname }}</p>
                        <p class="teacher-name">{{ getCourseInfo(dayIndex, sessionIndex).tname }}</p>
                        <p class="location">{{ getCourseInfo(dayIndex, sessionIndex).location }}</p>
                    </div>
                    <div v-else class="no-course"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            sid: '', // 学生ID，从 sessionStorage 获取
            selectedTerm: '', // 当前选中的学期
            termList: [], // 用于存储从后端获取的所有学期列表

            daysOfWeek: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
            sessionsOfDay: [
                { name: '第1节', time: '08:00~09:50' },
                { name: '第2节', time: '10:10~12:00' },
                { name: '第3节', time: '12:10~14:00' },
                { name: '第4节', time: '14:10~16:00' },
                { name: '第5节', time: '16:20~18:10' },
                { name: '第6节', time: '19:00~20:50' },
                { name: '第7节', time: '21:00~21:50' },
            ],
            scheduleData: {}, // 存储课表数据，键为 "dayIndex-sessionIndex"
        };
    },
    mounted() {
        this.sid = sessionStorage.getItem('sid');
        // 1. 获取所有学期列表
        this.fetchAllTerms();
    },
    methods: {
        // 获取所有学期列表
        fetchAllTerms() {
            const that = this;
            axios.get('http://localhost:10086/SCT/findAllTerm')
                .then(function (resp) {
                    if (resp.data && resp.data.length > 0) {
                        that.termList = resp.data;
                        // 2. 设置默认学期：优先使用 sessionStorage 中的，否则使用获取到的第一个学期
                        that.selectedTerm = sessionStorage.getItem('currentTerm') || that.termList[0];
                        // 3. 获取学生课表数据
                        that.fetchStudentSchedule();
                    } else {
                        that.$message({
                            showClose: true,
                            message: '未找到学期信息。',
                            type: 'warning'
                        });
                    }
                })
                .catch(function (error) {
                    console.error("获取学期列表失败:", error);
                    that.$message({
                        showClose: true,
                        message: '获取学期列表失败，请检查网络或后端服务。',
                        type: 'error'
                    });
                });
        },

        // 获取学生课表数据
        fetchStudentSchedule() {
            if (!this.sid || !this.selectedTerm) {
                this.$message({
                    showClose: true,
                    message: '学生ID或学期未选择，无法加载课表。',
                    type: 'warning'
                });
                return;
            }

            const that = this;
            this.scheduleData = {}; // 清空已有课表数据，防止旧数据残留

            axios.post('http://localhost:10086/SCT/findBySid', {
                sid: this.sid,
                term: this.selectedTerm
            }).then(function (resp) {
                if (resp.data && Array.isArray(resp.data) && resp.data.length > 0) {
                    resp.data.forEach(course => {
                        // 假设后端返回的 day (0-6 代表周一到周日) 和 period (0-6 代表第1到第7节) 已经是0索引
                        // 如果后端返回的是1索引值，你可能需要减1，例如：
                        // const dayIndex = course.day - 1;
                        // const sessionIndex = course.period - 1;
                        const dayIndex = course.day; // 确保这里的值与 daysOfWeek 数组的索引对应
                        const sessionIndex = course.period; // 确保这里的值与 sessionsOfDay 数组的索引对应

                        that.$set(that.scheduleData, `<span class="math-inline">\{dayIndex\}\-</span>{sessionIndex}`, {
                            cid: course.cid,
                            cname: course.cname,
                            tname: course.tname,
                            location: course.location
                        });
                    });

                    that.$message({
                        showClose: true,
                        message: '课表加载成功',
                        type: 'success'
                    });
                } else {
                    that.$message({
                        showClose: true,
                        message: '当前学期无课程安排或未选课。',
                        type: 'info'
                    });
                }
            }).catch(function (error) {
                console.error("获取课表失败:", error);
                that.$message({
                    showClose: true,
                    message: '获取课表失败，请检查网络或后端服务。',
                    type: 'error'
                });
            });
        },

        // 根据星期和节次索引获取课程信息
        getCourseInfo(dayIndex, sessionIndex) {
            const key = `<span class="math-inline">\{dayIndex\}\-</span>{sessionIndex}`;
            return this.scheduleData[key];
        }
    }
};
</script>

<style scoped>
.schedule-container {
    padding: 20px;
}

.header-controls {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
}

.header-controls .label {
    margin-right: 10px;
    font-weight: bold;
    color: #606266;
}

.schedule-table {
    display: grid;
    grid-template-columns: 80px repeat(7, 1fr); /* 1列用于节次，7列用于星期 */
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    overflow: hidden;
}

.schedule-header, .schedule-row {
    display: contents; /* 让子元素直接作为 grid items */
}

.schedule-cell {
    border: 1px solid #EBEEF5;
    padding: 8px;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 80px; /* 课程单元格的最小高度 */
    box-sizing: border-box;
}

.empty-cell {
    background-color: #FAFAFA;
}

.day-header{
    background-color: #F5F7FA;
    font-weight: bold;
    color: #303133;
}

.session-header {
    display: flex;
    flex-direction: column;
    background-color: #F5F7FA;
    font-size: 12px;
    font-weight: bold;
    color: #303133;
}

.session-time {
    color: #888888;
    font-size: 10px;
    margin-top: 2px;
}

.course-cell {
    background-color: #FFFFFF;
    cursor: pointer;
    position: relative;
    transition: background-color 0.2s ease;
}

.course-cell:hover {
    background-color: #F2F6FC;
    text-align: left;
}

.course-id, .course-name, .teacher-name, .location {
    color: black;
    font-weight: normal;
    font-family: 'SimSun', serif;
    line-height: 1.5;
    margin: 5px 0;
    text-align: left;
}

.location {
    color: #888888;
}

.no-course {
    color: #C0C4CC;
    font-size: 0.8em;
}
</style>
