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
                { label: '东区', value: 'DQ'}
            ],
            daysOfWeek: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
            sessionsPerDay: 7,

            allOccupancyData: [],
            courseOccupancyData: [], // 仅保留课程数据
            // examOccupancyData: [], // 删除考试数据

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
        // this.fetchOccupancyData(); // 删除冗余方法调用
    },
    methods: {
        async fetchCurrentSemester() {
            try {
                const response = await axios.get('http://localhost:10086/SC/findAllTerm');
                const semesters = response.data;
                if (semesters && semesters.length > 0) {
                    this.searchForm.yearSemester = semesters[0];
                } else {
                    this.searchForm.yearSemester = '25-春季学期';
                    this.$message.warning('未能从后端获取到学期信息，已使用默认学期。');
                }
            } catch (error) {
                console.error('获取学期信息失败:', error);
                this.$message.error('获取学期信息失败，请检查网络或后端服务');
                this.searchForm.yearSemester = '25-春季学期';
            }

            this.fetchClassroomBaseInfo(); // 初始化空壳教室
            await this.fetchCourseOccupancy();   // 只获取课程数据
            //await this.fetchExamOccupancy();     // 删除考试数据获取
            this.mergeAndApplyFilters();         // 调用合并和筛选方法 (现在它只处理课程数据)
        },

        async fetchCourseOccupancy() {
            try {
                const params = {
                    term: this.searchForm.yearSemester,
                    week: this.searchForm.week,
                    building: this.searchForm.building, // 确保这些参数被发送到后端
                    classroom: this.searchForm.classroom
                };
                // **替换为你的实际课程表 API 接口**
                const response = await axios.get('http://localhost:10086/course/findCourse', { params: params });
                const courseSchedules = response.data; // 假设后端返回的是课程安排数组

                this.courseOccupancyData = this.updateOccupancyData(
                    JSON.parse(JSON.stringify(this.allOccupancyData)), // 基于模拟生成的基础数据
                    courseSchedules,
                    'course'
                );

                this.mergeAndApplyFilters();

            } catch (error) {
                console.error('获取课程占用数据失败:', error);
                this.$message.error('获取课程占用数据失败，请检查网络或后端服务11');
                this.courseOccupancyData = JSON.parse(JSON.stringify(this.allOccupancyData)); // 失败时重置

                this.mergeAndApplyFilters();
            }
        },


        fetchClassroomBaseInfo() {
            // 模拟数据：生成一些教学楼和教室的空壳
            const mockClassrooms = [];
            const buildings = { 'SY': '思源楼', 'YF': '逸夫教学楼', 'JY': '建艺楼', 'DQ': '东区'};
            let roomCounter = 101;

            let roomNumBase = 100; // 新增/修改：初始教室号基数

            for (let bCode in buildings) {
                roomNumBase = 100; // 新增/修改：每个教学楼循环前重置基数
                for (let i = 0; i < 15; i++) {
                    const roomNumber = roomNumBase + i + 1; // 新增/修改：从101开始计算教室号
                    const roomName = `${bCode}${roomNumber}`;
                    mockClassrooms.push({
                        classroomName: `${roomName} (${Math.floor(Math.random() * 100) + 30})`, // 随机容量
                        schedule: Array(7).fill(null).map(() =>
                            Array(this.sessionsPerDay).fill({ type: 'free', details: '' })
                        ),
                    });
                }
            }

            // for (let bCode in buildings) {
            //     for (let i = 0; i < 15; i++) { // 每个教学楼生成15个教室
            //         const roomNumber = roomCounter + i;
            //         const roomName = `${bCode}${roomNumber}`;
            //         mockClassrooms.push({
            //             classroomName: `${roomName} (${Math.floor(Math.random() * 100) + 30})`, // 随机容量
            //             schedule: Array(7).fill(null).map(() =>
            //                 Array(this.sessionsPerDay).fill({ type: 'free', details: '' })
            //             ),
            //         });
            //     }
            //     // roomCounter = 0; // 这行逻辑看起来有点问题，通常每个教学楼的教室号会递增，而不是重置
            //     // 如果你希望每个教学楼的教室号从 101 开始递增，则不需要此行
            //     // 如果你希望它们是 SY101, SY102... YF101, YF102... 则每次循环后保持 roomCounter 即可
            //     // 如果你希望它们是 SY101, SY102... YF201, YF202... 则每次循环结束前 `roomCounter += 100;` 这样的逻辑更合理
            //     // 暂时注释掉这行，假设 roomCounter 应该在不同教学楼之间持续递增，或者每次从101开始
            // }

            this.allOccupancyData = JSON.parse(JSON.stringify(mockClassrooms));
            this.courseOccupancyData = JSON.parse(JSON.stringify(mockClassrooms));
            // this.examOccupancyData = JSON.parse(JSON.stringify(mockClassrooms)); // 删除考试数据初始化
        },

        // 删除冗余的 fetchOccupancyData 方法
        /*
        async fetchOccupancyData() {
            // ... (此方法及其内容将被删除)
        },
        */

        updateOccupancyData(baseOccupancy, schedules, type) {
            const updatedOccupancy = JSON.parse(JSON.stringify(baseOccupancy));

            schedules.forEach(item => {
                const classroomNameFromBackend = item.classroomName || item.name; // 教室名称
                const dayOfWeek = item.dayOfWeek;
                const session = item.session;
                const details = item.details || item.courseName; // 只需要课程名称作为详情

                let classroomEntry = null;
                // 查找对应的教室，支持包含容量的名称
                for (let [idx, room] of updatedOccupancy.entries()) {
                    if (room.classroomName.startsWith(classroomNameFromBackend + ' (') || room.classroomName === classroomNameFromBackend) {
                        classroomEntry = room;
                        break;
                    }
                }

                if (classroomEntry) {
                    const dayIndex = dayOfWeek - 1;
                    const sessionIndex = session - 1;

                    if (dayIndex >= 0 && dayIndex < 7 && sessionIndex >= 0 && sessionIndex < this.sessionsPerDay) {
                        // 由于现在只有课程数据，直接覆盖即可，无需优先级判断
                        classroomEntry.schedule[dayIndex][sessionIndex] = { type: type, details: details };
                    }
                }

                // (原有 classroomEntry 查找逻辑之后)
                if (!classroomEntry) {
                    classroomEntry = updatedOccupancy.find(room => room.classroomName.startsWith(classroomNameFromBackend + ' ('));
                }
            });
            return updatedOccupancy;
        },

        // 简化 mergeAndApplyFilters 方法
        mergeAndApplyFilters() {
            // 不再有考试数据需要合并，直接将 courseOccupancyData 作为最终数据
            this.allOccupancyData = JSON.parse(JSON.stringify(this.courseOccupancyData));

            // 然后应用筛选
            this.applyFilters();
        },

        // 简化 getOccupancyPriority 方法 (不再需要考试、实验、其他)
        getOccupancyPriority(type) {
            switch (type) {
                case 'course': return 4;
                case 'free': return 1;
                default: return 0;
            }
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
            // 重新获取课程数据，然后触发合并和筛选
            this.fetchCourseOccupancy();
            //this.fetchExamOccupancy();
            // mergeAndApplyFilters 会在 fetchCourseOccupancy 完成后触发
            // 因为 courseOccupancyData 的更新会触发 watch 中的 building/classroom，进而调用 mergeAndApplyFilters
            // 或者你可以在这里直接调用：
            // this.mergeAndApplyFilters(); // 如果你不想依赖 watch，可以直接在这里调用
        },

        handlePageChange(newPage) {
            this.currentPage = newPage;
            this.updatePaginatedData();
        },

        // created 钩子在文件顶部已经修改过，这里不再重复
        /*
        async created() {
            await this.fetchCurrentSemester();
            this.calculateBaseDate();
        },
        */

        getCellClass(status) {
            if (!status || status.type === 'free') {
                return 'occupancy-free';
            } else if (status.type === 'course') {
                return 'occupancy-course';
            }
            // 移除其他类型的判断
            /*
            else if (status.type === 'exam') {
                return 'occupancy-exam';
            } else if (status.type === 'experiment') {
                return 'occupancy-experiment';
            } else if (status.type === 'other') {
                return 'occupancy-other';
            }
            */
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
            const yearMatch = this.searchForm.yearSemester.match(/^(\d{2})/);
            let yearPrefix = new Date().getFullYear();

            if (yearMatch && yearMatch[1]) {
                yearPrefix = parseInt(yearMatch[1], 10);
                yearPrefix = (yearPrefix < 70) ? (2000 + yearPrefix) : (1900 + yearPrefix);
            }
            const fullYear = yearPrefix;

            if (this.searchForm.yearSemester.includes('春季学期')) {
                semesterStartDate = new Date(fullYear, 1, 24);
            } else if (this.searchForm.yearSemester.includes('秋季学期')) {
                semesterStartDate = new Date(fullYear, 8, 1);
            } else {
                console.warn('无法识别的学期格式，使用默认起始日期。');
                semesterStartDate = new Date(fullYear, 0, 1);
            }

            const dayOfWeek = semesterStartDate.getDay();
            const daysToMonday = dayOfWeek === 0 ? -6 : -(dayOfWeek - 1);
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
            return `${date.getMonth() + 1}/${date.getDate()}`;
        }
    },
    watch: {
        'searchForm.week': {
            handler(newVal, oldVal) {
                if (newVal !== oldVal) {
                    this.calculateBaseDate();
                    this.fetchCourseOccupancy(); // 只获取课程
                    //this.fetchExamOccupancy(); // 删除对考试的调用
                    this.mergeAndApplyFilters();
                }
            },
            deep: true
        },
        'searchForm.yearSemester': {
            handler(newVal, oldVal) {
                if (newVal !== oldVal && newVal) {
                    this.calculateBaseDate();
                    this.fetchClassroomBaseInfo(); // 重新生成模拟的基础教室
                    this.fetchCourseOccupancy(); // 重新获取课程
                    //this.fetchExamOccupancy(); // 删除对考试的调用
                    this.mergeAndApplyFilters();
                }
            },
            deep: true
        },
        'searchForm.building': 'handleSearch', // 教学楼变化时执行 handleSearch
        'searchForm.classroom': 'handleSearch', // 教室号变化时执行 handleSearch
    }
};
</script>

<style scoped>
/* 样式部分保持不变，但可以考虑删除 .occupancy-exam, .occupancy-experiment, .occupancy-other 的 CSS 规则，因为不再使用这些颜色 */
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
/* 删除这些不再使用的样式 */
/* .occupancy-exam { background-color: #E6A23C; } */
/* .occupancy-experiment { background-color: #409EFF; } */
/* .occupancy-other { background-color: #909399; } */


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