<template>
  <div>
    <div class="filter-container">
      <div class="filter-item">
        <span>URI:</span>
        <el-input v-model="listQuery.requestUri" placeholder="请输入URI" />
        <span>操作IP:</span>
        <el-input v-model="listQuery.operationIp" placeholder="请输入操作IP" />
        <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="danger" icon="Document" @click="handleBatchDelete">删除</el-button>
      </div>
    </div>
    <el-table
      ref="multipleTable"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      :header-cell-style="{background:'#EEF3FB',color:'#0243A3'}"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" />
      <el-table-column min-width="120" label="操作标题">
        <template #default="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="操作类型">
        <template #default="scope">
          <span>{{ scope.row.logType }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="120" label="URI">
        <template #default="scope">
          <span>{{ scope.row.requestUri }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="操作人">
        <template #default="scope">
          <span>{{ scope.row.operationName }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="操作IP">
        <template #default="scope">
          <span>{{ scope.row.operationIp }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="浏览器">
        <template #default="scope">
          <span>{{ scope.row.browser }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="100" label="操作系统">
        <template #default="scope">
          <span>{{ scope.row.os }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="请求状态">
        <template #default="scope">
          <el-tag :type="statusTypeFilter(scope.row.status)">{{ statusFilter(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="160" label="操作时间">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <!-- <el-button size="small" type="danger" text icon="Delete" @click="handleDelete(scope.row)">删除</el-button> -->
          <delete-btn @click="handleDelete(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />
  </div>
</template>

<script>
import { fetchOperationLogList, deleteOperationLog, batchDeleteOperationLog } from '@/api/monitor/log/operation'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination/index.vue' // 水波纹指令
import BtnGroup from '@/components/btn/BtnGroup.vue'
import DeleteBtn from '../../../components/btn/components/DeleteBtn.vue'

export default {
  name: 'ScheduleJobLogList',
  components: { Pagination, BtnGroup, DeleteBtn },
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
        importance: undefined,
        title: undefined,
        type: undefined
      },
      showReviewer: false,
      dialogFormVisible: false,
      multipleSelection: [],
      batchDeleteLoading: false,
      statusOptions: [
        { label: '发送失败', value: '-1' },
        { label: '发送中', value: '0' },
        { label: '发送成功', value: '1' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    statusTypeFilter(status) {
      const statusMap = {
        0: 'danger',
        1: 'success'
      }
      return statusMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        0: '失败',
        1: '成功'
      }
      return statusMap[status]
    },
    getList() {
      this.listLoading = true
      fetchOperationLogList(this.listQuery).then(response => {
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
    },
    handleDelete(row) {
      deleteOperationLog(row.id).then(() => {
        this.$message.success('删除成功')
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBatchDelete() {
      if (this.multipleSelection.length) {
        this.batchDeleteLoading = true
        const list = this.multipleSelection
        const ids = []
        list.forEach(function(value, index, array) {
          ids.push(value.id)
        })
        const idsStr = ids.join(',')
        batchDeleteOperationLog(idsStr).then(() => {
          this.$message.success('提交成功')
          this.$refs.multipleTable.clearSelection()
          this.batchDeleteLoading = false
          this.getList()
        }).catch(() => {
          this.batchDeleteLoading = false
        })
      } else {
        this.$message({
          message: '至少选择一条日志',
          type: 'warning'
        })
      }
    },
    change(t) {
      if (t < 10) {
        return '0' + t
      } else {
        return t
      }
    },
    timestampToTime(timestamp) {
      const date = new Date(timestamp * 1000) // 时间戳为10位需*1000，时间戳为13位的话不需乘1000
      const Y = date.getFullYear() + '-'
      const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      const D = this.change(date.getDate()) + ' '
      const h = this.change(date.getHours()) + ':'
      const m = this.change(date.getMinutes()) + ':'
      const s = this.change(date.getSeconds())
      return Y + M + D + h + m + s
    }
  }
}
</script>
