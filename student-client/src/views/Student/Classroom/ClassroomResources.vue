<template>
    <div id="classroom-availability">
        <div class="header-section">
            <h1>教室占用情况查询</h1>
            <div class="search-controls">
                <label for="semester-select">当前学期: </label>
                <select id="semester-select" v-model="selectedSemester" @change="fetchClassroomStatus">
                    <option v-for="semesterItem in semesters" :key="semesterItem" :value="semesterItem">
                        {{ semesterItem }}
                    </option>
                </select>

                <label for="week-select">第</label>
                <select id="week-select" v-model="currentWeekIndex" @change="fetchClassroomStatus">
                    <option v-for="(weekNum, index) in totalWeeks" :key="index" :value="index">
                        {{ index + 1 }}
                    </option>
                </select>
                <span>周</span>

                <label for="building-select">选择教学楼:</label>
                <select id="building-select" v-model="selectedBuildingPrefix" @change="handleBuildingChange">
                    <option value="">-全部教学楼-</option> <option v-for="(name, prefix) in buildingMap" :key="prefix" :value="prefix">
                    {{ name }}
                </option>
                </select>

                <input type="text" placeholder="教室号" v-model="searchLocation" class="search-input">
                <button @click="fetchClassroomStatus" class="search-button">查询</button>
            </div>
        </div>

        <div v-if="loading" class="loading-message">
            加载中，请稍候...
        </div>

        <div v-else-if="errorMessage" class="error-message">
            {{ errorMessage }}
        </div>

        <div v-else>
            <div class="legend">
                <span>图例:</span>
                <span class="legend-item occupied-legend"></span><span>课程占用</span>
                <span class="legend-item exam-occupied-legend"></span><span>考试占用</span>
                <span class="legend-item available-legend"></span><span>空闲</span>
            </div>

            <table class="classroom-table">
                <thead>
                <tr>
                    <th class="location-col">教室号</th>
                    <th v-for="(day, dayIndex) in daysOfWeek" :key="day">
                        <div class="day-header">
                            <span>{{ day }}</span>
                            <span class="date-header">{{ getFormattedDate(dayIndex) }}</span>
                        </div>
                        <div class="periods-row-header">
                            <span v-for="period in 7" :key="period">{{ period }}</span>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="location in pagedLocations" :key="location">
                    <td class="location-col">{{ location }}</td>
                    <td v-for="(day, dayIndex) in daysOfWeek" :key="day">
                        <div class="periods-row">
                            <div v-for="period in 7" :key="period"
                                 :class="getCellStatus(location, dayIndex + 1, period)">
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>

            <div class="bottom-pagination">
                <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="totalPagedLocationsCount"
                    :page-size="pageSize"
                    :current-page="currentFloorPage"
                    @current-change="handleFloorPageChange"
                    :hide-on-single-page="true">
                </el-pagination>
                <div class="page-jumper">
                    <span>前往</span>
                    <input type="number" v-model.number="jumperPage" class="jumper-input"
                           :min="1" :max="Math.ceil(totalPagedLocationsCount / pageSize)">
                    <button @click="jumpToPage" class="jumper-button">Go</button>
                    <span>页</span>
                    <span class="total-pages">共 {{ Math.ceil(totalPagedLocationsCount / pageSize) }} 页</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
// import { ElPagination, ElMessage } from 'element-plus';

export default {
    // components: {
    //   ElPagination,
    // },
    setup() {
        // --- 数据 ---
        const semesters = ref([]);
        const selectedSemester = ref(null);
        const courseData = ref([]); // 课程占用数据
        const examData = ref([]);   // 考试占用数据
        const loading = ref(false);
        const errorMessage = ref('');
        const searchLocation = ref('');

        // --- 教学楼类型控制 ---
        const buildingMap = ref({
            'YF': '逸夫教学楼',
            'SY': '思源教学楼',
            'JY': '建艺教学楼',
            'DQ': '东区教学楼'
        });
        const selectedBuildingPrefix = ref('');

        // --- 周数控制 ---
        const currentWeekIndex = ref(0);
        const weeksInSemester = ref(20);

        // --- 日期计算基准 ---
        // 根据 1.png 图片中的日期 "6/9" 调整
        const FIRST_WEEK_MONDAY_2025 = new Date('2025-02-24T00:00:00');

        // --- 教学楼分页 ---
        const totalFloors = ref(6);
        const classroomsPerFloor = 10;
        const currentFloorPage = ref(1);
        const pageSize = ref(classroomsPerFloor);
        const jumperPage = ref(1);

        // 生成所有教学楼教室的完整列表 (或当前选中教学楼的列表)
        const allClassrooms = computed(() => {
            const rooms = [];
            const prefixesToGenerate = selectedBuildingPrefix.value === ''
                ? Object.keys(buildingMap.value)
                : [selectedBuildingPrefix.value];

            prefixesToGenerate.forEach(prefix => {
                for (let floor = 1; floor <= totalFloors.value; floor++) {
                    for (let room = 1; room <= classroomsPerFloor; room++) {
                        let roomNumber = room.toString();
                        if (room < 10) {
                            roomNumber = `0${room}`;
                        }
                        rooms.push(`${prefix}${floor}${roomNumber}`);
                    }
                }
            });
            return rooms.sort();
        });

        // 根据当前页码和搜索条件过滤并获取当前楼层的教室列表
        const pagedLocations = computed(() => {
            let filteredBySearch = allClassrooms.value;

            if (searchLocation.value) {
                filteredBySearch = filteredBySearch.filter(location =>
                    location.toUpperCase().includes(searchLocation.value.toUpperCase())
                );
            }

            if (filteredBySearch.length === 0) {
                return [];
            }

            const startIndex = (currentFloorPage.value - 1) * pageSize.value;
            const endIndex = startIndex + pageSize.value;

            return filteredBySearch.slice(startIndex, endIndex);
        });

        // 计算当前经过教学楼选择和搜索过滤后的总教室数（用于分页器的total属性）
        const totalPagedLocationsCount = computed(() => {
            let filteredBySearch = allClassrooms.value;
            if (searchLocation.value) {
                filteredBySearch = filteredBySearch.filter(location =>
                    location.toUpperCase().includes(searchLocation.value.toUpperCase())
                );
            }
            return filteredBySearch.length;
        });

        const daysOfWeek = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'];

        const totalWeeks = computed(() => {
            return weeksInSemester.value;
        });

        const getFormattedDate = (dayIndex) => {
            const date = new Date(FIRST_WEEK_MONDAY_2025);
            date.setDate(date.getDate() + (currentWeekIndex.value * 7));
            date.setDate(date.getDate() + dayIndex);
            return `${date.getMonth() + 1}/${date.getDate()}`;
        };

        const getFullDateForDay = (dayIndex) => {
            const date = new Date(FIRST_WEEK_MONDAY_2025);
            date.setDate(date.getDate() + (currentWeekIndex.value * 7));
            date.setDate(date.getDate() + dayIndex);
            const year = date.getFullYear();
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const day = date.getDate().toString().padStart(2, '0');
            return `${year}-${month}-${day}`;
        };

        const fetchSemesters = async () => {
            loading.value = true;
            errorMessage.value = '';
            try {
                const response = await axios.get('http://localhost:10086/SC/findAllTerm');
                semesters.value = response.data;

                if (semesters.value && semesters.value.length > 0) {
                    selectedSemester.value = semesters.value[0];
                    fetchClassroomStatus();
                } else {
                    selectedSemester.value = '25-春季学期';
                    console.warn('未能从后端获取到学期信息，已使用默认学期。');
                }
            } catch (error) {
                console.error('获取学期信息失败:', error);
                errorMessage.value = '获取学期信息失败，请检查网络或后端服务';
                selectedSemester.value = '25-春季学期';
            } finally {
                loading.value = false;
            }
        };

        const fetchClassroomStatus = async () => {
            if (!selectedSemester.value) return;

            loading.value = true;
            errorMessage.value = '';
            courseData.value = [];
            examData.value = []; // 在每次查询前清空数据

            try {
                // --- 1. 获取课程占用数据 ---
                // 只有当当前周数在1-16周范围内时才请求课程数据
                if (currentWeekIndex.value + 1 <= 16) {
                    const courseParams = {
                        yearSemester: selectedSemester.value,
                        week: currentWeekIndex.value + 1,
                    };
                    if (selectedBuildingPrefix.value !== '') {
                        courseParams.buildingPrefix = selectedBuildingPrefix.value;
                        courseParams.floor = currentFloorPage.value;
                    }
                    if (searchLocation.value) {
                        courseParams.keywordLocation = searchLocation.value;
                    }

                    const courseResponse = await axios.get('http://localhost:10086/course/getAllCourses', { params: courseParams });
                    courseData.value = courseResponse.data;
                } else {
                    // 如果当前周数大于16周，则清空课程数据
                    courseData.value = [];
                }


                // --- 2. 获取考试占用数据 ---
                const examRequests = [];
                for (let i = 0; i < 7; i++) {
                    const dateStr = getFullDateForDay(i);
                    const examParams = {
                        semester: selectedSemester.value,
                        // 根据后端findAllExam接口是否接收日期参数来决定是否添加
                        // date: dateStr, // 如果findAllExam不接收日期参数，请注释掉此行
                        // 后端 findAllExam 接口可能不接收 buildingPrefix, floor, keywordLocation
                        // 如果后端接口 findAllExam 没有任何参数，请将 params: examParams 删除，只保留 axios.get('http://localhost:10086/exams/findAllExam')
                    };
                    // 如果 findAllExam 不接收建筑前缀或楼层参数，请注释掉以下几行
                    if (selectedBuildingPrefix.value !== '') {
                        examParams.buildingPrefix = selectedBuildingPrefix.value;
                        examParams.floor = currentFloorPage.value;
                    }
                    if (searchLocation.value) {
                        examParams.keywordLocation = searchLocation.value;
                    }
                    // 调用考试接口
                    // 请根据您的后端 `/exams/findAllExam` 接口实际接收的参数来决定 `params` 对象内容
                    examRequests.push(axios.get('http://localhost:10086/exams/findAllExam', { params: examParams }));
                }
                const examResponses = await Promise.all(examRequests);
                // 将所有考试数据扁平化到一个数组中
                examData.value = examResponses.flatMap(res => res.data);

                // --- 检查考试数据 ---
                console.log('从后端获取到的考试数据:', examData.value);
                // 你可以在这里进一步检查数据的格式，例如：
                // if (examData.value.length > 0 && !examData.value[0].location) {
                //   console.warn('考试数据可能缺少 "location" 字段');
                // }
                // if (examData.value.length > 0 && !examData.value[0].day) {
                //   console.warn('考试数据可能缺少 "day" 字段');
                // }


            } catch (error) {
                console.error('获取占用情况出错:', error);
                errorMessage.value = '无法获取教室占用情况，请稍后再试。';
            } finally {
                loading.value = false;
            }
        };

        /**
         * 根据传入的教室、星期数字和节次判断占用状态，并返回对应的CSS类。
         * 优先显示考试占用。
         * @param {string} location 前端显示的教室名称
         * @param {number} dayNumber 星期数字 (1=星期一, 7=星期日)
         * @param {number} period 节次 (1-7)
         * @returns {string} CSS类名 'occupied', 'exam-occupied' 或 'available'
         */
        const getCellStatus = (location, dayNumber, period) => {
            // 获取当前单元格对应的日期
            const cellDate = new Date(FIRST_WEEK_MONDAY_2025);
            cellDate.setDate(cellDate.getDate() + (currentWeekIndex.value * 7) + (dayNumber - 1));

            const year = cellDate.getFullYear();
            const month = (cellDate.getMonth() + 1).toString().padStart(2, '0');
            const day = cellDate.getDate().toString().padStart(2, '0');
            const formattedDate = `${year}-${month}-${day}`; // 用于匹配考试数据的日期格式

            // 检查考试占用
            const isExamOccupied = examData.value.some(item => {
                // 根据您提供的后端返回格式 {location:YF409, day:2025-06-14T16:00:00.000+00:00, period:4}
                // 从后端返回的 item.day (例如 "2025-06-14T16:00:00.000+00:00") 中提取日期部分
                const itemDayFormatted = item.day ? String(item.day).substring(0, 10) : ''; // 确保 item.day 是字符串，然后提取 "YYYY-MM-DD"

                return String(item.location) === location &&
                    itemDayFormatted === formattedDate && // 使用提取出的日期部分进行匹配
                    item.period === period;
            });

            // 检查课程占用：只有当当前周数在1-16周范围内时才检查课程占用
            let isCourseOccupied = false;
            if (currentWeekIndex.value + 1 <= 16) {
                isCourseOccupied = courseData.value.some(item => {
                    return String(item.location) === location &&
                        item.day === dayNumber &&
                        item.period === period;
                });
            }

            if (isExamOccupied) {
                return 'exam-occupied'; // 考试占用优先
            } else if (isCourseOccupied) {
                return 'occupied';
            } else {
                return 'available';
            }
        };

        const handleBuildingChange = () => {
            currentFloorPage.value = 1;
            jumperPage.value = 1;
            searchLocation.value = '';
            fetchClassroomStatus();
        };

        const handleFloorPageChange = (newPage) => {
            currentFloorPage.value = newPage;
            jumperPage.value = newPage;
            fetchClassroomStatus();
        };

        const jumpToPage = () => {
            const maxPage = Math.ceil(totalPagedLocationsCount.value / pageSize.value);
            if (jumperPage.value >= 1 && jumperPage.value <= maxPage) {
                currentFloorPage.value = jumperPage.value;
                fetchClassroomStatus();
            } else {
                console.warn(`请输入有效页码，范围在 1 到 ${maxPage} 之间。`);
                jumperPage.value = currentFloorPage.value;
            }
        };

        watch([selectedSemester, currentWeekIndex, selectedBuildingPrefix], () => {
            if (selectedSemester.value) {
                fetchClassroomStatus();
            }
        });

        watch(searchLocation, () => {
            currentFloorPage.value = 1;
            jumperPage.value = 1;
            fetchClassroomStatus();
        });


        onMounted(() => {
            fetchSemesters();
        });

        return {
            semesters,
            selectedSemester,
            courseData,
            examData,
            loading,
            errorMessage,
            buildingMap,
            selectedBuildingPrefix,
            searchLocation,
            currentWeekIndex,
            weeksInSemester,
            totalWeeks,
            daysOfWeek,
            pagedLocations,
            totalPagedLocationsCount,
            pageSize,
            currentFloorPage,
            jumperPage,
            getFormattedDate,
            fetchClassroomStatus,
            getCellStatus,
            handleBuildingChange,
            handleFloorPageChange,
            jumpToPage,
        };
    },
};
</script>

<style scoped>
#classroom-availability {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    padding: 25px;
    max-width: 1400px;
    margin: 20px auto;
    background-color: #f0f2f5;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.header-section {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    margin-bottom: 25px;
}

h1 {
    text-align: left;
    color: #2c3e50;
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 1.8em;
    font-weight: 600;
    border-bottom: 1px solid #eee;
    padding-bottom: 15px;
}

.search-controls {
    display: flex;
    align-items: center;
    gap: 15px;
    flex-wrap: wrap;
}

.search-controls label {
    font-weight: bold;
    color: #34495e;
    font-size: 1em;
    white-space: nowrap;
}

.search-controls select,
.search-controls .search-input {
    padding: 8px 12px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    font-size: 0.95em;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
    min-width: 100px;
}

.search-controls select:focus,
.search-controls .search-input:focus {
    border-color: #409eff;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    outline: none;
}

.search-input {
    flex-grow: 0.5;
    max-width: 150px;
}

.search-button {
    padding: 8px 20px;
    background-color: #409eff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.95em;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.search-button:hover {
    background-color: #337ecc;
    transform: translateY(-1px);
}

.loading-message, .error-message {
    text-align: center;
    padding: 30px;
    font-size: 1.2em;
    color: #7f8c8d;
    background-color: #ecf0f1;
    border-radius: 8px;
    margin-top: 20px;
}

.error-message {
    color: #e74c3c;
    font-weight: bold;
    background-color: #fdeded;
    border: 1px solid #e74c3c;
}

.legend {
    margin: 20px 0;
    text-align: left;
    font-size: 0.95em;
    color: #555;
    display: flex;
    align-items: center;
    gap: 10px;
}

.legend span {
    font-weight: bold;
}

.legend-item {
    display: inline-block;
    width: 18px;
    height: 18px;
    border-radius: 4px;
    vertical-align: middle;
}

.occupied-legend {
    background-color: #e74c3c;
}

.exam-occupied-legend {
    background-color: #3498db;
}

.available-legend {
    background-color: #27ae60;
}

.classroom-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
    background-color: white;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
    border-radius: 10px;
    overflow: hidden;
}

.classroom-table th,
.classroom-table td {
    border: 1px solid #e0e0e0;
    padding: 0;
    text-align: center;
    vertical-align: top;
    position: relative;
    min-width: 80px;
}

.classroom-table th.location-col,
.classroom-table td.location-col {
    padding: 15px 10px;
    background-color: #f7f7f7;
    font-weight: bold;
    color: #333;
    width: 80px;
    min-width: unset;
}

.classroom-table th {
    background-color: #e8f5e9;
    color: #333;
    font-weight: 700;
    font-size: 0.95em;
}

.day-header {
    padding: 10px 0 5px;
    border-bottom: 1px solid #d0d0d0;
    display: flex;
    flex-direction: column;
    gap: 3px;
}

.date-header {
    font-size: 0.8em;
    color: #666;
}

.periods-row-header {
    display: flex;
    justify-content: space-around;
    padding: 5px 0;
    background-color: #f0f8f0;
    font-size: 0.8em;
    font-weight: normal;
    color: #555;
}

.periods-row-header span {
    width: calc(100% / 7);
    text-align: center;
}

.periods-row {
    display: flex;
    height: 30px;
    width: 100%;
}

.periods-row div {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-left: 1px solid #eee;
    box-sizing: border-box;
    transition: background-color 0.2s ease;
}

.periods-row div:first-child {
    border-left: none;
}

.available {
    background-color: #27ae60;
    cursor: pointer;
}

.occupied {
    background-color: #e74c3c;
}

.exam-occupied {
    background-color: #3498db;
}

.bottom-pagination {
    margin-top: 25px;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 15px;
}

.page-jumper {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-left: 15px;
    color: #555;
}

.jumper-input {
    width: 60px;
    padding: 6px 8px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    font-size: 0.9em;
    text-align: center;
}

.jumper-button {
    padding: 6px 12px;
    background-color: #409eff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s ease;
}

.jumper-button:hover {
    background-color: #337ecc;
}

.total-pages {
    font-size: 0.9em;
    margin-left: 5px;
    color: #777;
}
</style>