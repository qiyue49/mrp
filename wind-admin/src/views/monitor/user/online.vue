<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>用户名:</span>
          <el-input v-model="listQuery.username" placeholder="请输入用户名" />
        </div>
        <div class="filter-item">
          <span>IP:</span>
          <el-input v-model="listQuery.loginIp" placeholder="请输入IP" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
      </div>

      <el-table
        ref="multipleTable"
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        tyle="width: 100%"
        header-cell-class-name="header-cell"
      >
        <el-table-column min-width="120" label="用户名">
          <template #default="scope">
            <span>{{ scope.row.realname }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="账号">
          <template #default="scope">
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="登陆IP">
          <template #default="scope">
            <span>{{ scope.row.loginIp }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="登录地点">
          <template #default="scope">
            <span>{{ scope.row.loginLocation }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="浏览器">
          <template #default="scope">
            <span>{{ scope.row.browser }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="操作系统">
          <template #default="scope">
            <span>{{ scope.row.os }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="160" label="登陆时间">
          <template #default="scope">
            <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />
    </div>
  </el-card>
</template>

<script>
import { fetchMonitorUserOnlineList } from '@/api/monitor/user/online'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'MonitorUserOnlineList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        realname: undefined,
        username: undefined,
        loginIp: undefined,
        loginLocation: undefined,
        browser: undefined,
        os: undefined
      },
      showReviewer: false,
      dialogFormVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchMonitorUserOnlineList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    }
  }
}
</script>
