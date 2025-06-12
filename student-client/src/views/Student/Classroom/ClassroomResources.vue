<template>
    <div class="classroom-occupancy">
        <el-card>
            <div slot="header" class="clearfix">
                <span>教室占用情况查询</span>
            </div>

            <div class="search-panel">
        <span class="current-semester-display">
          当前学期: {{ searchForm.yearSemester || '加载中...' }}
        </span>

                <el-select v-model="searchForm.week" placeholder="选择周次" @change="handleSearch" size="small">
                    <el-option
                        v-for="n in 20" :key="n"
                        :label="`第${n}周`"
                        :value="n">
                    </el-option>
                </el-select>

                <el-select v-model="searchForm.building" placeholder="选择教学楼" @change="handleSearch" size="small">
                    <el-option
                        v-for="item in buildingOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>

                <el-input v-model="searchForm.classroom" placeholder="教室号" clearable @keyup.enter.native="handleSearch" size="small"></el-input>

                <el-button type="primary" @click="handleSearch" size="small">查询</el-button>
            </div>

            <div class="legend-panel">
                <div class="legend-title">图例:</div>
                <div class="legend-items">
                    <span class="legend-item"><i class="color-box occupancy-course"></i> 排课占用</span>
                    <span class="legend-item"><i class="color-box occupancy-free"></i> 空闲</span>
                    <span class="legend-item"><i class="color-box occupancy-exam"></i> 考试占用</span>
                    <span class="legend-item"><i class="color-box occupancy-experiment"></i> 实验占用</span>
                    <span class="legend-item"><i class="color-box occupancy-other"></i> 其他安排</span>
                </div>
            </div>

            <div class="occupancy-grid">
                <div class="grid-header">
                    <div class="grid-cell time-header"></div>
                    <div class="grid-cell day-header" v-for="(dayName, dayIndex) in daysOfWeek" :key="dayIndex">
                        {{ dayName }} <br/>
                        <span class="date-hint">{{ getFormattedDate(dayIndex) }}</span>
                        <div class="session-numbers">
                            <span v-for="n in sessionsPerDay" :key="n">{{ n }}</span>
                        </div>
                    </div>
                </div>

                <div class="grid-row" v-for="(classroomOccupancy, classroomIndex) in paginatedClassroomData" :key="classroomIndex">
                    <div class="grid-cell classroom-label">
                        {{ classroomOccupancy.classroomName }}
                    </div>
                    <div
                        class="grid-cell occupancy-day-cell"
                        v-for="(occupancyStatusOfDay, dayIndex) in classroomOccupancy.schedule"
                        :key="dayIndex"
                    >
                        <div
                            :class="getCellClass(occupancyStatusOfDay[sessionIndex])"
                            v-for="(sessionStatus, sessionIndex) in occupancyStatusOfDay"
                            :key="sessionIndex"
                            :title="getOccupancyDetails(sessionStatus)"
                        >
                        </div>
                    </div>
                </div>
                <div v-if="!paginatedClassroomData || paginatedClassroomData.length === 0" class="no-data-message">
                    <div style="grid-column: 1 / span 8; text-align: center; padding: 20px;">
                        暂无教室占用数据
                    </div>
                </div>
            </div>

            <el-pagination
                background
                layout="prev, pager, next, total"
                :total="totalClassrooms"
                :page-size="pageSize"
                :current-page.sync="currentPage"
                @current-change="handlePageChange"
                class="pagination-bottom"
            >
            </el-pagination>
        </el-card>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ClassroomOccupancyQuery',
    data() {
        return {
            searchForm: {
                yearSemester: '', // 用于存储当前学期
                week: 16, // 默认周次设置为第16周，方便调试看到数据，与目标图 image_d4e27a.png 保持一致
                building: '', // 默认空字符串，对应“全部教学楼”
                classroom: ''
            },
            buildingOptions: [
                { label: '全部教学楼', value: '' },
                { label: '思源楼', value: 'SY' },
                { label: '逸夫教学楼', value: 'YF' },
                { label: '建艺楼', value: 'JY' },
            ],
            daysOfWeek: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
            sessionsPerDay: 7,

            allOccupancyData: [],
            filteredOccupancyData: [],
            paginatedClassroomData: [],

            pageSize: 10,
            currentPage: 1,
            totalClassrooms: 0,

            currentBaseDate: null, // 当前周的星期一的日期对象
        };
    },
    async created() {
        await this.fetchCurrentSemester(); // 1. 先获取当前学期
        this.calculateBaseDate(); // 2. 根据学期和周次计算日期
        this.fetchOccupancyData(); // 3. 再获取教室占用数据
    },
    methods: {
        async fetchCurrentSemester() {
            try {
                const response = await axios.get('http://localhost:10086/SC/findAllTerm');
                const semesters = response.data;
                if (semesters && semesters.length > 0) {
                    this.searchForm.yearSemester = semesters[0]; // 假设后端返回的第一个是当前学期
                } else {
                    this.searchForm.yearSemester = '25-春季学期'; // 回退到默认值
                    this.$message.warning('未能从后端获取到学期信息，已使用默认学期。');
                }
            } catch (error) {
                console.error('获取学期信息失败:', error);
                this.$message.error('获取学期信息失败，请检查网络或后端服务');
                this.searchForm.yearSemester = '25-春季学期'; // API调用失败时的回退
            }
        },

        async fetchOccupancyData() {
            // 真实后端API调用示例 (请在生产环境使用)
            // try {
            //   const response = await axios.get('/api/classroom-occupancy', { params: this.searchForm });
            //   this.allOccupancyData = response.data;
            //   this.applyFilters();
            // } catch (error) {
            //   console.error('获取教室占用数据失败:', error);
            //   this.$message.error('获取教室占用数据失败');
            //   this.allOccupancyData = [];
            // }

            // 以下是模拟数据，用于演示和调试。请在真实项目中删除或注释掉。
            const mockData = [];
            const buildings = { 'SY': '思源楼', 'YF': '逸夫教学楼', 'JY': '建艺楼' };
            let roomCounter = 101;

            for (let bCode in buildings) {
                for (let i = 0; i < 20; i++) { // 每个教学楼生成20个教室
                    const roomName = `${bCode}${roomCounter++}`;
                    mockData.push({
                        classroomName: `${roomName} (${Math.floor(Math.random() * 100) + 30})`, // 随机容量
                        schedule: Array(7).fill(null).map(() =>
                            Array(this.sessionsPerDay).fill(null).map(() => {
                                const rand = Math.random();
                                if (rand < 0.3) return { type: 'course', details: `课程 (A${Math.floor(Math.random()*100)})` };
                                if (rand < 0.4) return { type: 'exam', details: `考试 (E${Math.floor(Math.random()*10)})` };
                                if (rand < 0.45) return { type: 'experiment', details: `实验 (L${Math.floor(Math.random()*5)})` };
                                if (rand < 0.48) return { type: 'other', details: `活动 (O${Math.floor(Math.random()*3)})` };
                                return { type: 'free', details: '' };
                            })
                        ),
                    });
                }
            }

            this.allOccupancyData = mockData; // 填充模拟数据
            this.applyFilters(); // 应用筛选并更新分页数据
        },

        applyFilters() {
            let filtered = this.allOccupancyData;

            if (this.searchForm.building) {
                const buildingPrefix = this.searchForm.building;
                filtered = filtered.filter(classroom =>
                    classroom.classroomName.startsWith(buildingPrefix)
                );
            }

            if (this.searchForm.classroom) {
                const classroomName = this.searchForm.classroom.toUpperCase();
                filtered = filtered.filter(classroom =>
                    classroom.classroomName.includes(classroomName)
                );
            }

            this.filteredOccupancyData = filtered;
            this.totalClassrooms = filtered.length;
            this.currentPage = 1; // 筛选条件变化时重置页码
            this.updatePaginatedData();
        },

        updatePaginatedData() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            this.paginatedClassroomData = this.filteredOccupancyData.slice(start, end);
        },

        handleSearch() {
            // 触发筛选，如果需要重新从后端获取数据，则调用 fetchOccupancyData()
            // 如果只是前端筛选，则调用 applyFilters()
            this.applyFilters();
        },

        handlePageChange(newPage) {
            this.currentPage = newPage;
            this.updatePaginatedData();
        },

        getCellClass(status) {
            if (!status || status.type === 'free') {
                return 'occupancy-free';
            } else if (status.type === 'course') {
                return 'occupancy-course';
            } else if (status.type === 'exam') {
                return 'occupancy-exam';
            } else if (status.type === 'experiment') {
                return 'occupancy-experiment';
            } else if (status.type === 'other') {
                return 'occupancy-other';
            }
            return '';
        },

        getOccupancyDetails(status) {
            return status && status.details ? status.details : '空闲';
        },

        calculateBaseDate() {
            if (!this.searchForm.yearSemester) {
                this.currentBaseDate = null;
                return;
            }

            let semesterStartDate;
            const yearMatch = this.searchForm.yearSemester.match(/^(\d{2})/); // 匹配开头的两位数字
            let yearPrefix = new Date().getFullYear(); // 默认当前年份作为备用

            if (yearMatch && yearMatch[1]) {
                yearPrefix = parseInt(yearMatch[1], 10);
                // 假设两位数年份在 70 以下是 20XX 年 (如 25 -> 2025)，否则是 19XX 年 (如 98 -> 1998)
                yearPrefix = (yearPrefix < 70) ? (2000 + yearPrefix) : (1900 + yearPrefix);
            }
            const fullYear = yearPrefix; // 最终的完整年份

            if (this.searchForm.yearSemester.includes('春季学期')) {
                semesterStartDate = new Date(fullYear, 1, 24); // 2月24日 (月份是0-indexed，1代表2月)
            } else if (this.searchForm.yearSemester.includes('秋季学期')) {
                semesterStartDate = new Date(fullYear, 8, 1); // 9月1日 (月份是0-indexed，8代表9月)
            } else {
                console.warn('无法识别的学期格式，使用默认起始日期。');
                semesterStartDate = new Date(fullYear, 0, 1); // 默认年初
            }

            // 调整为星期一：如果 semesterStartDate 不是星期一，向前调整到最近的星期一
            const dayOfWeek = semesterStartDate.getDay(); // 0为星期天，1为星期一 ... 6为星期六
            const daysToMonday = dayOfWeek === 0 ? -6 : -(dayOfWeek - 1); // 如果是星期天，向前调整6天回到上周一；否则，向前调整 (当前星期几 - 1) 天
            semesterStartDate.setDate(semesterStartDate.getDate() + daysToMonday);

            const daysToAdd = (this.searchForm.week - 1) * 7;
            const baseDate = new Date(semesterStartDate);
            baseDate.setDate(baseDate.getDate() + daysToAdd);
            this.currentBaseDate = baseDate;
        },

        getFormattedDate(dayIndex) {
            if (!this.currentBaseDate) return '';
            const date = new Date(this.currentBaseDate);
            date.setDate(date.getDate() + dayIndex);
            // 核心修复：确保这里只返回纯文本日期，不包含任何HTML标签
            return `${date.getMonth() + 1}/${date.getDate()}`;
        }
    },
    watch: {
        'searchForm.week': 'calculateBaseDate', // 周次变化时重新计算日期
        'searchForm.yearSemester': function(newVal, oldVal) {
            if (newVal !== oldVal && newVal) {
                this.calculateBaseDate(); // 学期变化时重新计算日期
            }
        },
        'searchForm.building': 'applyFilters', // 教学楼变化时应用筛选
        'searchForm.classroom': 'applyFilters', // 教室号变化时应用筛选
    }
};
</script>

<style scoped>
.classroom-occupancy {
    padding: 20px;
    background-color: #f5f7fa;
}

.el-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}
.clearfix:after {
    clear: both
}
.clearfix span {
    font-weight: bold;
    font-size: 18px;
    color: #303133;
}

.search-panel {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 25px;
    align-items: center;
    background-color: #ffffff;
    padding: 15px;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.current-semester-display {
    font-size: 14px;
    color: #606266;
    font-weight: bold;
    padding: 0 5px;
    white-space: nowrap;
}

.search-panel .el-select,
.search-panel .el-input {
    width: 160px;
}

.legend-panel {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    background-color: #ffffff;
    padding: 10px 15px;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    font-size: 14px;
}

.legend-title {
    font-weight: bold;
    color: #606266;
    margin-right: 15px;
    white-space: nowrap;
}

.legend-items {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
}

.legend-item {
    display: inline-flex;
    align-items: center;
    color: #606266;
    white-space: nowrap;
}

.color-box {
    width: 18px;
    height: 18px;
    border-radius: 4px;
    margin-right: 8px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.1);
}

.occupancy-course { background-color: #F56C6C; }
.occupancy-free { background-color: #EBEEF5; }
.occupancy-exam { background-color: #E6A23C; }
.occupancy-experiment { background-color: #409EFF; }
.occupancy-other { background-color: #909399; }


.occupancy-grid {
    display: grid;
    grid-template-columns: 120px repeat(7, 1fr);
    border: 1px solid #DCDFE6;
    border-radius: 8px;
    overflow: hidden;
    background-color: #ffffff;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.grid-header, .grid-row {
    display: contents;
}

.grid-cell {
    border: 1px solid #EBEEF5;
    box-sizing: border-box;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-size: 14px;
    color: #606266;
    padding: 0;
}

.time-header {
    background-color: #F5F7FA;
    border-top-left-radius: 8px;
}

.day-header {
    background-color: #F5F7FA;
    font-weight: bold;
    position: relative;
    padding-top: 8px;
    padding-bottom: 8px;
}
.grid-header .grid-cell:last-child {
    border-top-right-radius: 8px;
}

.date-hint {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
    margin-bottom: 8px;
}

.session-numbers {
    display: flex;
    justify-content: space-around;
    width: 100%;
    font-size: 12px;
    color: #909399;
    border-top: 1px solid #DCDFE6;
    padding-top: 4px;
}
.session-numbers span {
    flex: 1;
    text-align: center;
}

.classroom-label {
    background-color: #F5F7FA;
    font-weight: bold;
    padding: 8px 5px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.occupancy-day-cell {
    display: grid;
    grid-template-rows: repeat(v-bind(sessionsPerDay), 1fr);
    padding: 0;
    border: none;
}

.occupancy-day-cell > div {
    width: 100%;
    height: auto;
    border: 1px solid #DCDFE6;
    border-width: 0 1px 1px 0;
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px;
    cursor: pointer;
    transition: background-color 0.2s ease;
    color: #303133;
}

.occupancy-day-cell:last-child > div {
    border-right-width: 0;
}
.occupancy-day-cell > div:last-child {
    border-bottom-width: 0;
}

.no-data-message {
    grid-column: 1 / span 8; /* 让消息横跨整个表格 */
    text-align: center;
    padding: 40px 0;
    color: #909399;
    font-size: 16px;
    background-color: #ffffff;
}

.pagination-bottom {
    margin-top: 20px;
    text-align: right;
    padding: 10px 0;
}
</style>