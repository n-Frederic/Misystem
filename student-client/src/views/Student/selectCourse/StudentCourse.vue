<template>
  <div class="schedule-container">
    <div class="header-controls">
      <span class="label">选择学期：</span>
      <el-select v-model="selectedTerm" placeholder="请选择学期" @change="fetchStudentSchedule">
        <el-option
            v-for="term in terms"
            :key="term.value"
            :label="term.label"
            :value="term.value">
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
            <p class="course-id">{{ getCourseInfo(dayIndex, sessionIndex).cno }}</p>
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
      terms: [ // 示例学期数据，实际应从后端获取
        {label: '2023-2024学年第一学期', value: '2023-2024-1'},
        {label: '2023-2024学年第二学期', value: '2023-2024-2'},
        {label: '2024-2025学年第一学期', value: '2024-2025-1'},
        {label: '2024-2025学年第二学期', value: '2024-2025-2'},
      ],
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
      scheduleData: {},
    };
  },
  mounted() {
    this.sid = sessionStorage.getItem('sid');
    // 首次加载页面时，设置一个默认学期或从sessionStorage获取上次选中的学期
    this.selectedTerm = sessionStorage.getItem('currentTerm') || this.terms[0].value;
    this.fetchStudentSchedule();
  },
  methods: {
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
      this.scheduleData = {}; // 清空已有课表数据

      axios.post('http://localhost:10086/Schedule/getStudentSchedule', {
        sid: this.sid,
        term: this.selectedTerm
      }).then(function (resp) {
        if (resp.data && resp.data.length > 0) {
          resp.data.forEach(course => {
            // 直接用 day 和 period 构造 key
            const dayIndex = course.day;
            const sessionIndex = course.period;
            const key = `${dayIndex}-${sessionIndex}`;

            that.$set(that.scheduleData, key, {
              cno: course.cno,
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
      const key = `${dayIndex}-${sessionIndex}`;
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
  /* 1列用于节次，7列用于星期 */
  grid-template-columns: 80px repeat(7, 1fr);
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  overflow: hidden; /* 确保边框和阴影显示正确 */
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
  box-sizing: border-box; /* 包含 padding 和 border */
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