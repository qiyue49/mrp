<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input v-model="listQuery.jobName" style="width: 200px;" class="filter-item" placeholder="请输入任务名称" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.executeClass" style="width: 200px;" class="filter-item" placeholder="请输入执行类" @keyup.enter.native="handleFilter" />

      <el-select v-model="listQuery.status" style="width: 200px;" class="filter-item" placeholder="请选择状态">
        <el-option label="全部状态" value="" />
        <el-option
          v-for="item in statusOptions"
          :key="item.label + 'filter_status'"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-waves class="filter-item" type="danger" icon="el-icon-delete" @click="handleBathDelete">{{ $t('table.delete') }}</el-button>
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
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column width="120" align="center" label="任务名称">
        <template #default="scope">
          <span>{{ scope.row.jobName }}</span>
        </template>
      </el-table-column>
      <el-table-column width="80" align="center" label="任务分组">
        <template #default="scope">
          <span>{{ scope.row.jobGroup }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="执行类">
        <template #default="scope">
          <span>{{ scope.row.executeClass }}</span>
        </template>
      </el-table-column>
      <el-table-column width="80" align="center" label="方法名">
        <template #default="scope">
          <span>{{ scope.row.methodName }}</span>
        </template>
      </el-table-column>
      <el-table-column width="80" align="center" label="方法参数">
        <template #default="scope">
          <span>{{ scope.row.methodParams }}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="日志信息">
        <template #default="scope">
          <span>{{ scope.row.jobMessage }}</span>
        </template>
      </el-table-column>
      <el-table-column width="80" align="center" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status | statusTypeFilter">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="操作时间">
        <template #default="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column :label="$t('table.actions')" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="el-icon-delete" class="delete-text-btn" @click="handleDelete(scope.row)">{{ $t('table.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />
  </div>
</template>

<script>
import { fetchScheduleJobLogList, deleteScheduleJobLog, batchDeleteScheduleJobLog } from '@/api/tool/task/log'
import waves from '@/directive/waves'
import Pagination from '../../../components/Pagination' // 水波纹指令

export default {
  name: 'ScheduleJobLogList',
  components: { Pagination },
  directives: {
    waves
  },
  filters: {
    statusTypeFilter(status) {
      const statusMap = {
        '-1': 'danger',
        '0': 'info',
        '1': 'success'
      }
      return statusMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        '-1': '失败',
        '0': '普通',
        '1': '成功'
      }
      return statusMap[status + '']
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      ids: undefined,
      pageArray: this.$store.getters.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.getters.defaultPageSize,
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
      batchDeleteScheduleJobLog(this.ids).then((response) => {
        if (response.data.code === 0) {
          this.getList()
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    handleDelete(row) {
      deleteScheduleJobLog(row.id).then((response) => {
        if (response.data.code === 0) {
          this.getList()
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
      })
    }
  }
}
</script>
