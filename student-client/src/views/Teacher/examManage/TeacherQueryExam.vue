<template>
  <div class="exam-schedule-container">
    <el-card class="exam">
      <div slot="header" class="content">
        <span>Exam Schedule</span>
      </div>
      <div class="search-panel">
        <el-select v-model="searchForm.term" placeholder="Select Term" @change="handleSearch">
          <el-option
              v-for="semester in semesterOptions"
              :key="semester.value"
              :label="semester.label"
              :value="semester.value"
          ></el-option>
        </el-select>
        <el-button type="primary" @click="handleSearch">Search</el-button>
      </div>
      <el-table :data="exams" style="width: 100%" border>
        <el-table-column prop="cno" label="ID" width="80"></el-table-column>
        <el-table-column prop="cname" label="Exam" width="250"></el-table-column>
        <el-table-column prop="day" label="Date" width="200">
          <template slot-scope="scope">
            {{ formatDate(scope.row.day) }}
          </template>
        </el-table-column>
        <el-table-column prop="period" label="Time"></el-table-column>
        <el-table-column prop="durationMin" label="Duration"></el-table-column>
        <el-table-column prop="grade" label="Grade" width="100"></el-table-column>
        <el-table-column prop="location" label="Location" width="150"></el-table-column>
        <el-table-column prop="req" label="Requirements" width="100"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'TermQueryExam',
  data() {
    return {
      searchForm: {
        term: ''
      },
      exams: [],
      semesterOptions: []
    };
  },
  created() {
    this.fetchSemesters();
  },
  watch: {
    'searchForm.term': {
      handler(newVal) {
        if (newVal) {
          this.fetchExamData();
        }
      },
      immediate: true
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      const value = new Date(date);
      if (isNaN(value.getTime())) return '';
      return `${value.getFullYear()}-${String(value.getMonth() + 1).padStart(2, '0')}-${String(value.getDate()).padStart(2, '0')}`;
    },
    async fetchSemesters() {
      try {
        const response = await axios.get('http://localhost:10086/SCT/findAllTerm');
        this.semesterOptions = response.data.map(termString => {
          const parts = termString.split('-');
          const yearSuffix = parts[0];
          const season = parts[1];
          const yearPrefix = '20';
          const fullYear = parseInt(`${yearPrefix}${yearSuffix}`);
          const semesterValue = season.includes('Spring') ? '1' : season.includes('Fall') ? '2' : '3';
          return {
            label: termString,
            value: `${fullYear}-${semesterValue}`
          };
        }).sort((a, b) => {
          const yearA = parseInt(a.value.split('-')[0]);
          const yearB = parseInt(b.value.split('-')[0]);
          const semA = parseInt(a.value.split('-')[1]);
          const semB = parseInt(b.value.split('-')[1]);
          return yearB !== yearA ? yearB - yearA : semB - semA;
        });
        if (!this.searchForm.term && this.semesterOptions.length > 0) {
          this.searchForm.term = this.semesterOptions[0].value;
        }
      } catch (error) {
        this.$message.error('Failed to fetch semesters');
        this.semesterOptions = [{ label: '2024-2025 Semester 2', value: '2024-2' }];
        if (!this.searchForm.term) {
          this.searchForm.term = '2024-2';
        }
      }
    },
    async fetchExamData() {
      if (!this.searchForm.term) {
        this.exams = [];
        return;
      }
      try {
        const tid = sessionStorage.getItem('tid');
        if (!tid) {
          this.$message.error('Please log in');
          this.exams = [];
          return;
        }
        const response = await axios.get(`http://localhost:10086/exams/findTeacherExam/${tid}/${this.searchForm.term}`);
        this.exams = Array.isArray(response.data) ? response.data : [];
      } catch (error) {
        this.$message.error('Failed to fetch exams');
        this.exams = [];
      }
    },
    handleSearch() {
      this.fetchExamData();
    }
  }
};
</script>

<style scoped>
.exam-schedule-container {
  padding: 20px;
}

.search-panel {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.search-panel .el-select {
  width: 200px;
}
</style>