<template>
    <div class="classroom-occupancy">
        <el-card>
            <div slot="header" class="clearfix">
                <span>教室占用情况查询</span>
            </div>

            <div class="search-panel">
                <el-select v-model="searchForm.semester" placeholder="选择学期" @change="handleSearch">
                    <el-option
                            v-for="item in semesterOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <el-select v-model="searchForm.week" placeholder="选择周次" @change="handleSearch">
                    <el-option
                            v-for="n in 20" :key="n"
                            :label="`第${n}周`"
                            :value="n">
                    </el-option>
                </el-select>

                <el-select v-model="searchForm.building" placeholder="选择教学楼" @change="handleSearch">
                    <el-option
                            v-for="item in buildingOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <el-input v-model="searchForm.classroom" placeholder="教室号" clearable @keyup.enter.native="handleSearch"></el-input>

                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <div class="legend">
                <span class="legend-item"><i class="color-box red"></i> 排课占用</span>
                <span class="legend-item"><i class="color-box gray"></i> 空闲</span>
            </div>

            <div class="occupancy-grid">
                <div class="grid-header">
                    <div class="grid-cell time-header"></div>
                    <div class="grid-cell day-header" v-for="(dayName, dayIndex) in daysOfWeek" :key="dayIndex">
                        {{ dayName }} <br/>
                        <span class="date-hint">{{ getFormattedDate(dayIndex) }}</span>
                    </div>
                </div>

                <div class="grid-row" v-for="(classroomOccupancy, classroomIndex) in currentClassroomData" :key="classroomIndex">
                    <div class="grid-cell classroom-label">
                        {{ classroomOccupancy.classroomName }}
                    </div>
                    <div
                            class="grid-cell occupancy-cell"
                            v-for="(occupancyStatus, dayIndex) in classroomOccupancy.schedule"
                            :key="dayIndex"
                    >
                        <div
                                :class="getCellClass(occupancyStatus[sessionIndex])"
                                v-for="(sessionStatus, sessionIndex) in occupancyStatus"
                                :key="sessionIndex"
                                :title="getOccupancyDetails(occupancyStatus[sessionIndex])"
                        >
                        </div>
                    </div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
import axios from 'axios'; // 确保已安装 axios

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
                // ...更多教学楼
            ],
            daysOfWeek: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
            sessionsPerDay: 7, // 每天的节次数，根据图片，每格代表一节

            // 存储从后端获取的原始教室占用数据
            // 结构示例:
            // [
            //   {
            //     classroomName: 'SY101',
            //     schedule: [
            //       [ { type: 'course', details: '数据结构 (王老师)' }, { type: 'free' }, ... ], // 星期一的7节课状态
            //       [ { type: 'free' }, { type: 'course', details: '算法设计 (李老师)' }, ... ], // 星期二的7节课状态
            //       // ... 星期三到星期日
            //     ]
            //   },
            //   // ... 更多教室
            // ]
            allOccupancyData: [],

            // 当前显示在表格中的数据（经过筛选）
            currentClassroomData: [],

            // 用于计算日期，可以替换为更专业的日期库
            currentBaseDate: null,
        };
    },
    created() {
        // 初始加载所有数据
        this.fetchOccupancyData();
        this.calculateBaseDate();
    },
    methods: {
        // 获取教室占用数据
        async fetchOccupancyData() {
            // 在实际项目中，这里会调用后端 API 获取数据
            // 例如:
            // axios.get('/api/classroom-occupancy', { params: this.searchForm })
            //   .then(response => {
            //     this.allOccupancyData = response.data;
            //     this.applyFilters();
            //   })
            //   .catch(error => {
            //     console.error('获取教室占用数据失败:', error);
            //     this.$message.error('获取教室占用数据失败');
            //     this.allOccupancyData = []; // 清空数据
            //     this.applyFilters();
            //   });

            // 以下是模拟数据，用于演示。
            // 模拟后端返回的数据结构，假设每节课的占用状态
            // 0: 空闲 (gray)
            // 1: 排课占用 (red) - 假设你的后端返回这种类型
            // 2: 考试占用 (yellow) - 假设后端返回这种类型
            // 3: 实验占用 (blue) - 假设后端返回这种类型
            // 4: 其他安排 (purple) - 假设后端返回这种类型

            // 模拟数据需要根据你的 `searchForm` 来生成，这里为了简化，直接硬编码一些数据
            const mockData = [
                {
                    classroomName: 'SY101 (90)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY102 (32)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY103 (32)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY104 (32)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY105 (108)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY106 (153)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY107 (253)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY108 (253)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY109 (153)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY201 (110)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY202 (50)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY203 (44)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY204 (44)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
                {
                    classroomName: 'SY205 (50)',
                    schedule: Array(7).fill(null).map(() => Array(this.sessionsPerDay).fill({ type: 'free', details: '' })),
                },
            ];

            // 模拟添加一些“排课占用”（红色）
            // 示例：SY101 星期一 第1、2节有课
            mockData[0].schedule[0][0] = { type: 'course', details: '数据结构 (王老师)' };
            mockData[0].schedule[0][1] = { type: 'course', details: '数据结构 (王老师)' };
            // 示例：SY102 星期三 第3、4节有课
            mockData[1].schedule[2][2] = { type: 'course', details: '高等数学 (李老师)' };
            mockData[1].schedule[2][3] = { type: 'course', details: '高等数学 (李老师)' };
            // 示例：SY107 星期四 第1节有课（图片中SY107周四第一节是浅色）
            mockData[6].schedule[3][0] = { type: 'course', details: '离散数学 (张老师)' };
            // 示例：SY201 星期二 第5节有课
            mockData[9].schedule[1][4] = { type: 'course', details: '操作系统 (刘老师)' };
            // 模拟添加一些“考试占用”（图片中黄色）
            mockData[0].schedule[5][3] = { type: 'exam', details: '期末考试 (高数)' }; // SY101周六第4节
            mockData[6].schedule[5][4] = { type: 'exam', details: '期末考试 (物理)' }; // SY107周六第5节
            // 模拟添加一些“实验占用”（图片中蓝色）
            mockData[6].schedule[5][5] = { type: 'experiment', details: '物理实验 (一班)' }; // SY107周六第6节
            // 模拟添加一些“其他安排”（图片中棕色）
            mockData[1].schedule[5][6] = { type: 'other', details: '讲座 (报告厅)' }; // SY102周六第7节


            this.allOccupancyData = mockData;
            this.applyFilters();
        },

        // 应用筛选条件
        applyFilters() {
            let filtered = this.allOccupancyData;

            // 教学楼筛选
            if (this.searchForm.building) {
                // 假设教室名称格式为 "SY101 (90)"，需要提取 SYXXX 部分来判断教学楼
                const buildingPrefix = this.searchForm.building; // 例如 'SY'
                filtered = filtered.filter(classroom =>
                    classroom.classroomName.startsWith(buildingPrefix)
                );
            }

            // 教室号筛选
            if (this.searchForm.classroom) {
                const classroomName = this.searchForm.classroom.toUpperCase(); // 转换为大写进行匹配
                filtered = filtered.filter(classroom =>
                    classroom.classroomName.includes(classroomName)
                );
            }

            this.currentClassroomData = filtered;
        },

        // 处理查询按钮点击 或 下拉框变化
        handleSearch() {
            this.fetchOccupancyData(); // 重新获取数据（如果实际是后端筛选）
            // 或者如果后端不负责筛选，只在前端筛选：
            // this.applyFilters();
        },

        // 根据占用类型返回对应的 CSS 类名
        getCellClass(status) {
            if (!status || status.type === 'free') {
                return 'occupancy-free'; // 空闲，默认浅灰色背景
            } else if (status.type === 'course') {
                return 'occupancy-course'; // 排课占用，红色背景
            } else if (status.type === 'exam') {
                return 'occupancy-exam'; // 考试占用，黄色背景
            } else if (status.type === 'experiment') {
                return 'occupancy-experiment'; // 实验占用，蓝色背景
            } else if (status.type === 'other') {
                return 'occupancy-other'; // 其他安排，棕色背景
            }
            return ''; // 默认样式
        },

        // 获取占用详情（用于鼠标悬停时的提示）
        getOccupancyDetails(status) {
            return status && status.details ? status.details : '空闲';
        },

        // 计算当前周的基础日期（以星期一为例）
        calculateBaseDate() {
            // 假设当前学期开始日期是 2024-09-02 (星期一)
            const semesterStartDate = new Date('2024-09-02');
            // 根据当前周次计算出这周的星期一的日期
            // 比如第1周就是开始日期，第2周就是开始日期 + 7天
            const daysToAdd = (this.searchForm.week - 1) * 7;
            const baseDate = new Date(semesterStartDate);
            baseDate.setDate(baseDate.getDate() + daysToAdd);
            this.currentBaseDate = baseDate;
        },

        // 获取指定星期的格式化日期
        getFormattedDate(dayIndex) {
            if (!this.currentBaseDate) return '';
            const date = new Date(this.currentBaseDate);
            date.setDate(date.getDate() + dayIndex); // dayIndex: 0 for Mon, 1 for Tue, ...
            return `${date.getMonth() + 1}/${date.getDate()}`;
        }
    },
    watch: {
        // 监听周次变化，重新计算日期
        'searchForm.week': 'calculateBaseDate',
        // 监听学期变化，重新获取数据（如果学期切换会导致完全不同的数据）
        'searchForm.semester': 'fetchOccupancyData',
        // 监听教学楼和教室号变化，重新筛选数据 (如果后端不负责筛选)
        'searchForm.building': 'applyFilters',
        'searchForm.classroom': 'applyFilters',
    }
};
</script>

<style scoped>
.classroom-occupancy {
    padding: 20px;
}

.search-panel {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    align-items: center;
}

.search-panel .el-select,
.search-panel .el-input {
    width: 150px; /* 调整宽度 */
}

.legend {
    margin-bottom: 15px;
    font-size: 14px;
}

.legend-item {
    display: inline-flex;
    align-items: center;
    margin-right: 20px;
}

.color-box {
    width: 16px;
    height: 16px;
    border-radius: 4px;
    margin-right: 8px;
    border: 1px solid #ccc; /* 添加边框以区分白色方块 */
}

/* 定义颜色 */
.color-box.red, .occupancy-cell .occupancy-course { background-color: #F56C6C; /* 红色：排课占用 */ }
.color-box.gray, .occupancy-cell .occupancy-free { background-color: #EBEEF5; /* 浅灰色：空闲 */ }
.color-box.yellow, .occupancy-cell .occupancy-exam { background-color: #E6A23C; /* 黄色：考试占用 */ }
.color-box.blue, .occupancy-cell .occupancy-experiment { background-color: #409EFF; /* 蓝色：实验占用 */ }
.color-box.purple, .occupancy-cell .occupancy-other { background-color: #909399; /* 棕色/深灰色：其他安排 */ } /* 示例中棕色不好直接对应，用深灰替代 */


.occupancy-grid {
    display: grid;
    /* 第一列是时间/教室标签，其余7列是星期 */
    grid-template-columns: 120px repeat(7, 1fr);
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    overflow: hidden;
}

.grid-header, .grid-row {
    display: contents; /* 让子元素直接作为 grid items */
}

.grid-cell {
    border: 1px solid #EBEEF5;
    padding: 5px;
    box-sizing: border-box;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column; /* 允许内部元素垂直排列 */
    font-size: 14px;
    color: #606266;
}

.time-header {
    background-color: #F5F7FA;
}

.day-header {
    background-color: #F5F7FA;
    font-weight: bold;
}

.date-hint {
    font-size: 12px;
    color: #909399;
}

.classroom-label {
    background-color: #F5F7FA;
    font-weight: bold;
}

.occupancy-cell {
    display: grid; /* 使每个单元格内部也能分成7小格 */
    grid-template-rows: repeat(v-bind(sessionsPerDay), 1fr); /* 动态生成行数 */
    min-height: 100px; /* 教室行总高度 */
    padding: 0; /* 内部小格自己控制 padding */
    border: none; /* 内部小格的父容器不显示边框 */
}

.occupancy-cell > div {
    width: 100%;
    height: 100%;
    border-bottom: 1px solid #EBEEF5; /* 每节课之间有分割线 */
    /* border-right: 1px solid #EBEEF5;  如果需要细网格线，这里可以加上 */
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px;
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.occupancy-cell > div:last-child {
    border-bottom: none; /* 最后一节课没有底边框 */
}

.occupancy-cell > div:hover {
    filter: brightness(95%); /* 悬停效果 */
}
</style>