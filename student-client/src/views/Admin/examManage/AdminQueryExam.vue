<template>
    <div>
        <el-card>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%">
                <el-table-column
                        fixed
                        prop="cno"
                        label="课程编号"
                        width="100">
                </el-table-column>
                <el-table-column
                        fixed
                        prop="cname"
                        label="课程名称"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="day"
                        label="考试日期"
                        width="120">
                        <template slot-scope="scope">
                          {{ scope.row.day | dateFormat }}
                        </template>
                </el-table-column>
                <el-table-column
                        prop="period"
                        label="考试时段"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="durationMin"
                        label="考试时长(分钟)"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="grade"
                        label="考试总分"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="location"
                        label="考试地点"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="req"
                        label="考试要求"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="150">
                    <template slot-scope="scope">
                        <el-popconfirm
                                confirm-button-text="删除"
                                cancel-button-text="取消"
                                icon="el-icon-info"
                                icon-color="red"
                                title="删除不可复原"
                                @confirm="deleteExam(scope.row)">
                            <el-button slot="reference" type="text" size="small">删除</el-button>
                        </el-popconfirm>
                        <el-button @click="editExam(scope.row)" type="text" size="small">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<script>
import moment from 'moment';

export default {
    name: 'AdminQueryExam',
    data() {
        return {
            tableData: []
        };
    },
    filters: {
        dateFormat(value) {
            if (!value) return '';
            return moment(value).format('YYYY-MM-DD'); // 格式化日期
        }
    },
    created() {
        this.loadExams();
    },
    methods: {
        loadExams() {
            const that = this;
            axios.get('http://localhost:10086/exams/findAllExam').then(function (resp) {
                console.log('接口返回数据:', resp.data); // 调试用
                that.tableData = resp.data;
            }).catch(function (e) {
                console.error('接口错误:', e); // 调试用
                that.$message({
                    showClose: true,
                    message: '获取考试数据失败: ' + e.message,
                    type: 'error'
                });
            });
        },
        deleteExam(row) {
            const that = this;
            axios.get('http://localhost:10086/exams/deletegrade/' + row.sid+'/'+row.cno).then(function (resp) {
                if (resp.data === true) {
                    that.$message({
                        showClose: true,
                        message: '删除成功',
                        type: 'success'
                    });
                    that.loadExams(); // 重新加载数据
                } else {
                    that.$message({
                        showClose: true,
                        message: '删除失败，请检查数据库连接',
                        type: 'error'
                    });
                }
            }).catch(function (e) {
                that.$message({
                    showClose: true,
                    message: '删除失败，存在外键依赖或网络错误: ' + e.message,
                    type: 'error'
                });
            });
        },
        editExam(row) {
            this.$router.push({
                path: '/editorExam',
                query: {
                    cno: row.cno
                }
            });
        }
    }
}
</script>

<style scoped>
</style>