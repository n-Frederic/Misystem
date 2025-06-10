<template>
    <div class="classroom-occupancy">
        <el-card>
            <div slot="header" class="clearfix">
                <span>教室占用情况查询</span>
            </div>

            <div class="search-panel">
                <el-select v-model="searchForm.semester" placeholder="选择学期" @change="handleSearch" size="small">
                    <el-option
                        v-for="item in semesterOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>

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
                semester: '2024-2025-2', // 默认学期
                week: 16,               // 默认周次
                building: '',           // 默认教学楼
                classroom: ''           // 默认教室号
            },
            semesterOptions: [
                { label: '2024-2025-2', value: '2024-2025-2' },
                { label: '2023-2024-1', value: '2023-2024-1' },
            ],
            buildingOptions: [
                { label: '全部教学楼', value: '' },
                { label: '源西楼', value: 'YX' },
                { label: '未教学楼', value: 'WJ' },
            ],
            daysOfWeek: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
            sessionsPerDay: 7, // 每天的节次数，根据图片，每格代表一节

            allOccupancyData: [], // 存储从后端获取的原始完整数据
            filteredOccupancyData: [], // 存储经过筛选后的数据
            paginatedClassroomData: [], // 存储当前页显示的数据

            pageSize: 10, // 每页显示的教室数量
            currentPage: 1, // 当前页码
            totalClassrooms: 0, // 教室总数

            currentBaseDate: null, // 用于计算日期
        };
    },
    created() {
        this.fetchOccupancyData();
        this.calculateBaseDate();
    },
    methods: {
        async fetchOccupancyData() {
            // 真实后端API调用示例
            // try {
            //   const response = await axios.get('/api/classroom-occupancy', { params: this.searchForm });
            //   this.allOccupancyData = response.data;
            //   this.applyFilters();
            // } catch (error) {
            //   console.error('获取教室占用数据失败:', error);
            //   this.$message.error('获取教室占用数据失败');
            //   this.allOccupancyData = [];
            //   this.applyFilters();
            // }

            // 以下是模拟数据，用于演示。
            const mockData = [];
            const buildings = { 'YX': '源西楼', 'WJ': '未教学楼', 'JS': '建设楼' };
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

            this.allOccupancyData = mockData;
            this.applyFilters();
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
            this.currentPage = 1;
            this.updatePaginatedData();
        },

        updatePaginatedData() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            this.paginatedClassroomData = this.filteredOccupancyData.slice(start, end);
        },

        handleSearch() {
            // 筛选条件变化时，重新应用筛选（而不是重新拉取所有数据，如果后端支持前端筛选）
            // 如果后端只根据学期和周次返回数据，那么学期周次变化时才需要fetchOccupancyData
            // 这里为了演示，每次查询都重新模拟所有数据并筛选
            this.fetchOccupancyData();
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
            const semesterStartDate = new Date('2024-09-02'); // 2024年9月2日是星期一
            const daysToAdd = (this.searchForm.week - 1) * 7;
            const baseDate = new Date(semesterStartDate);
            baseDate.setDate(baseDate.getDate() + daysToAdd);
            this.currentBaseDate = baseDate;
        },

        getFormattedDate(dayIndex) {
            if (!this.currentBaseDate) return '';
            const date = new Date(this.currentBaseDate);
            date.setDate(date.getDate() + dayIndex);
            return `${date.getMonth() + 1}/${date.getDate()}`;
        }
    },
    watch: {
        'searchForm.week': 'calculateBaseDate',
        'searchForm.semester': 'fetchOccupancyData',
        'searchForm.building': 'applyFilters',
        'searchForm.classroom': 'applyFilters',
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
    white-space: nowrap; /* 防止图例标题换行 */
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
    white-space: nowrap; /* 防止单个图例项换行 */
}

.color-box {
    width: 18px;
    height: 18px;
    border-radius: 4px;
    margin-right: 8px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.1); /* 增加内阴影使其更立体 */
}

/* 定义颜色 - 保持一致性 */
.occupancy-course { background-color: #F56C6C; /* 红色 */ }
.occupancy-free { background-color: #EBEEF5; /* 浅灰色 */ }
.occupancy-exam { background-color: #E6A23C; /* 黄色 */ }
.occupancy-experiment { background-color: #409EFF; /* 蓝色 */ }
.occupancy-other { background-color: #909399; /* 棕色/深灰色 */ }


.occupancy-grid {
    display: grid;
    grid-template-columns: 120px repeat(7, 1fr);
    border: 1px solid #DCDFE6;
    border-radius: 8px;
    overflow: hidden;
    background-color: #ffffff;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05); /* 添加阴影 */
}

.grid-header, .grid-row {
    display: contents;
}

.grid-cell {
    border: 1px solid #EBEEF5; /* 更细的网格线 */
    box-sizing: border-box;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-size: 14px;
    color: #606266;
    padding: 0; /* 单元格内部的padding由内容控制 */
}

.time-header {
    background-color: #F5F7FA;
    border-top-left-radius: 8px;
}

.day-header {
    background-color: #F5F7FA;
    font-weight: bold;
    position: relative; /* 用于定位节次编号 */
    padding-top: 8px; /* 为日期和节次编号留出空间 */
    padding-bottom: 8px;
}
/* 最后一个星期头部右侧圆角 */
.grid-header .grid-cell:last-child {
    border-top-right-radius: 8px;
}

.date-hint {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
    margin-bottom: 8px; /* 和节次编号分离 */
}

.session-numbers {
    display: flex;
    justify-content: space-around; /* 均匀分布节次编号 */
    width: 100%;
    font-size: 12px;
    color: #909399;
    border-top: 1px solid #DCDFE6; /* 节次编号上方加线 */
    padding-top: 4px;
}
.session-numbers span {
    flex: 1; /* 每个节次编号占据等宽 */
    text-align: center;
}


.classroom-label {
    background-color: #F5F7FA;
    font-weight: bold;
    padding: 8px 5px; /* 教室标签有自己的内边距 */
    white-space: nowrap; /* 防止教室名称换行 */
    overflow: hidden;
    text-overflow: ellipsis;
}

.occupancy-day-cell {
    display: grid;
    /* 使用 grid-template-rows: repeat(7, 1fr) 确保每个小格子高度平分 */
    grid-template-rows: repeat(v-bind(sessionsPerDay), 1fr);
    padding: 0;
    border: none;
}

.occupancy-day-cell > div {
    width: 100%;
    /* min-height: 28px; /* 调整为更小的高度，根据图片估计 */
    height: auto; /* 让内容决定高度，但父容器会平均分配 */
    border: 1px solid #DCDFE6; /* 明显的分隔线 */
    border-width: 0 1px 1px 0; /* 只在右侧和下方有边框 */
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px; /* 格子内的文字大小 */
    cursor: pointer;
    transition: background-color 0.2s ease;
    color: #303133;
}

/* 移除每列最右侧的垂直线，和每行最下方的水平线 */
.occupancy-day-cell:last-child > div {
    border-right-width: 0;
}
.occupancy-day-cell > div:last-child {
    border-bottom-width: 0;
}


.pagination-bottom {
    margin-top: 20px;
    text-align: right;
    padding: 10px 0;
}
</style>