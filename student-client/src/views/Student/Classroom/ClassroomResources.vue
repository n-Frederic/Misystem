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
                <span class="legend-item occupied-legend"></span><span>已占用</span>
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
                                 :class="getClassroomStatus(location, dayIndex + 1, period)">
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
// 如果你使用了 Element Plus 的分页组件，请确保已安装并在此处导入
// import { ElPagination, ElMessage } from 'element-plus';

export default {
    // 如果使用 Element Plus 分页，需要注册组件
    // components: {
    //   ElPagination,
    // },
    setup() {
        // --- 数据 ---
        const semesters = ref([]);
        const selectedSemester = ref(null);
        const classroomData = ref([]);
        const loading = ref(false);
        const errorMessage = ref('');
        const searchLocation = ref('');

        // --- 教学楼类型控制 ---
        const buildingMap = ref({
            'YF': '逸夫教学楼',
            'SY': '思源教学楼',
            'JY': '建艺教学楼',
            'DQ': '东区教学楼'
            // 可以在这里添加更多教学楼
        });
        const selectedBuildingPrefix = ref(''); // 默认值为 ''，对应 "-全部教学楼-"

        // --- 周数控制 ---
        const currentWeekIndex = ref(0); // 当前周的索引 (0-based)
        const weeksInSemester = ref(18); // 假设一个学期有18周

        // --- 日期计算基准 ---
        // 2025年2月24日是星期一
        const FIRST_WEEK_MONDAY_2025 = new Date('2025-02-24T00:00:00');

        // --- 教学楼分页 ---
        const totalFloors = ref(6); // 假设每种教学楼总共有6层 (1xx - 6xx)
        const classroomsPerFloor = 10; // 每层教室数量 (例如 YF101 - YF110)
        const currentFloorPage = ref(1); // 当前教学楼分页的页码 (1-based)
        const pageSize = ref(classroomsPerFloor); // 每页显示10个教室 (即1层)
        const jumperPage = ref(1); // 跳转页码输入框的值

        // 生成所有教学楼教室的完整列表 (或当前选中教学楼的列表)
        const allClassrooms = computed(() => {
            const rooms = [];
            const prefixesToGenerate = selectedBuildingPrefix.value === ''
                ? Object.keys(buildingMap.value) // 如果是全部，则遍历所有前缀
                : [selectedBuildingPrefix.value]; // 否则只使用当前选中的前缀

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
            return rooms.sort(); // 确保教室号排序
        });

        // 根据当前页码和搜索条件过滤并获取当前楼层的教室列表
        const pagedLocations = computed(() => {
            let filteredBySearch = allClassrooms.value; // 从所有（或特定教学楼）教室中过滤

            if (searchLocation.value) {
                filteredBySearch = filteredBySearch.filter(location =>
                    location.toUpperCase().includes(searchLocation.value.toUpperCase())
                );
            }

            // 如果搜索结果为空，显示一个空数组
            if (filteredBySearch.length === 0) {
                return [];
            }

            const startIndex = (currentFloorPage.value - 1) * pageSize.value;
            const endIndex = startIndex + pageSize.value;

            return filteredBySearch.slice(startIndex, endIndex);
        });

        // 计算当前经过教学楼选择和搜索过滤后的总教室数（用于分页器的total属性）
        const totalPagedLocationsCount = computed(() => {
            let filteredBySearch = allClassrooms.value; // 从所有（或特定教学楼）教室中过滤
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
            classroomData.value = [];

            try {
                const params = {
                    yearSemester: selectedSemester.value,
                    week: currentWeekIndex.value + 1,
                };

                // 关键点：如果选择了“全部教学楼”，则不传递 buildingPrefix，让后端返回所有数据
                // 如果后端支持，也可以传递一个 buildingPrefixes 数组
                if (selectedBuildingPrefix.value !== '') {
                    params.buildingPrefix = selectedBuildingPrefix.value;
                    // 传递楼层参数 (如果后端按楼层分页)
                    params.floor = currentFloorPage.value;
                } else {
                    // 如果是“全部”，并且后端需要特定参数才能返回所有，这里可能需要调整
                    // 例如：params.getAllBuildings = true;
                    // 或者：params.buildingPrefixes = Object.keys(buildingMap.value).join(',');
                }

                // 如果后端支持按教室号模糊查询，可以把 searchLocation 传过去
                if (searchLocation.value) {
                    params.keywordLocation = searchLocation.value;
                }

                // 重要的性能考量：
                // 如果后端不支持按 buildingPrefix/floor/keywordLocation 过滤，
                // 那么此接口会返回所有教室的占用数据。
                // 这将导致 classroomData.value 包含大量数据，前端内存和渲染压力增大。
                // 理想情况是后端返回已经过滤和分页好的数据。
                const response = await axios.get('http://localhost:10086/course/getAllCourses', { params: params });

                classroomData.value = response.data;

            } catch (error) {
                console.error('获取教室占用情况出错:', error);
                errorMessage.value = '无法获取教室占用情况，请稍后再试。';
            } finally {
                loading.value = false;
            }
        };

        const getClassroomStatus = (location, dayNumber, period) => {
            const isOccupied = classroomData.value.some(item => {
                return String(item.location) === location &&
                    item.day === dayNumber &&
                    item.period === period;
            });
            return isOccupied ? 'occupied' : 'available';
        };

        const handleBuildingChange = () => {
            currentFloorPage.value = 1; // 切换教学楼时，重置回第一页
            jumperPage.value = 1; // 重置跳转页码
            searchLocation.value = ''; // 清空搜索框
            fetchClassroomStatus(); // 重新获取数据
        };

        const handleFloorPageChange = (newPage) => {
            currentFloorPage.value = newPage;
            jumperPage.value = newPage; // 同步跳转输入框的值
            fetchClassroomStatus();
        };

        const jumpToPage = () => {
            // 确保跳转页码有效
            const maxPage = Math.ceil(totalPagedLocationsCount.value / pageSize.value);
            if (jumperPage.value >= 1 && jumperPage.value <= maxPage) {
                currentFloorPage.value = jumperPage.value;
                fetchClassroomStatus();
            } else {
                // ElMessage.warning(`请输入有效页码，范围在 1 到 ${maxPage} 之间。`);
                console.warn(`请输入有效页码，范围在 1 到 ${maxPage} 之间。`);
                jumperPage.value = currentFloorPage.value; // 如果输入无效，还原为当前页
            }
        };

        watch([selectedSemester, currentWeekIndex], () => {
            if (selectedSemester.value) {
                fetchClassroomStatus();
            }
        });

        watch(searchLocation, () => {
            // 搜索框内容变化时，将当前页重置为1，并触发数据重新获取
            currentFloorPage.value = 1;
            jumperPage.value = 1;
            // 如果搜索是前端过滤，这里不需要重新获取数据。
            // 但因为“全部教学楼”模式下可能涉及到后端数据量大，
            // 且搜索框可能需要和后端联动，所以这里调用 fetchClassroomStatus 是更安全的做法。
            fetchClassroomStatus();
        });


        onMounted(() => {
            fetchSemesters();
        });

        return {
            semesters,
            selectedSemester,
            classroomData,
            loading,
            errorMessage,
            buildingMap, // 暴露 buildingMap 给模板
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
            getClassroomStatus,
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
    max-width: 1800px; /* 进一步增加最大宽度 */
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
    background-color: #f86657;
}

.available-legend {
    background-color: #abefc8;
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
    background-color: #abefc8;
    cursor: pointer;
}

.occupied {
    background-color: #f86657;
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