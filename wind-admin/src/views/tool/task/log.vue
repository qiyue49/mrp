<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>任务名称:</span>
          <el-input v-model="listQuery.jobName" placeholder="请输入任务名称" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>执行类:</span>
          <el-input v-model="listQuery.executeClass" placeholder="请输入执行类" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>状态:</span>
          <el-select v-model="listQuery.status" placeholder="请选择状态">
            <el-option label="全部状态" value="" />
            <el-option
              v-for="item in statusOptions"
              :key="item.label + 'filter_status'"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>

        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="danger" icon="Delete" @click="handleBathDelete">删除</el-button>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" />
        <el-table-column min-width="120" label="任务名称">
          <template #default="scope">
            <span>{{ scope.row.jobName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="任务分组">
          <template #default="scope">
            <span>{{ scope.row.jobGroup }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="执行类">
          <template #default="scope">
            <span>{{ scope.row.executeClass }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="方法名">
          <template #default="scope">
            <span>{{ scope.row.methodName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="方法参数">
          <template #default="scope">
            <span>{{ scope.row.methodParams }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="200" label="日志信息">
          <template #default="scope">
            <span>{{ scope.row.jobMessage }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="状态">
          <template #default="scope">
            <el-tag :type="statusTypeFilter(scope.row.status)">{{ statusFilter(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column min-width="160" label="操作时间">
          <template #default="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" type="danger" plain icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />
    </div>
  </el-card>
</template>

<script>
import { fetchScheduleJobLogList, deleteScheduleJobLog } from '@/api/tool/task/log'
import Pagination from '../../../components/Pagination'

export default {
  name: 'ScheduleJobLogList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      ids: undefined,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined
      },
      statusOptions: [
        { label: '失败', value: '-1' },
        { label: '普通', value: '0' },
        { label: '成功', value: '1' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchScheduleJobLogList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    statusTypeFilter(status) {
      const statusMap = {
        '-1': 'danger',
        0: 'info',
        1: 'success'
      }
      return statusMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        '-1': '失败',
        0: '普通',
        1: '成功'
      }
      return statusMap[status + '']
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSelectionChange(selection) {
      const array = []
      selection.forEach(item => {
        array.push(item.id)
      })
      this.ids = array.join(',')
    },
    handleBathDelete() {
      if (this.isNull(this.ids)) {
        this.$message({
          message: '至少选择一条日志',
          type: 'warning'
        })
        return
      }
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteScheduleJobLog(this.ids).then((response) => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteScheduleJobLog(row.id).then((response) => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    }
  }
}
</script>
