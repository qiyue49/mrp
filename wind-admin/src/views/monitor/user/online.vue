<template>
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
      <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
    </div>

    <el-table
      ref="multipleTable"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      element-loading-text="给我一点时间"
      border
      fit
      highlight-current-row
      tyle="width: 100%"
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
          <span>{{ scope.row.startTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        :current-page.sync="listQuery.page"
        :page-sizes="pageArray"
        :page-size="listQuery.limit"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { fetchMonitorUserOnlineList } from '@/api/monitor/user/online'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'MonitorUserOnlineList',
  directives: {
    waves
  },
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
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
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
