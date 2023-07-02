<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>任务名称:</span>
          <el-input v-model="listQuery.jobName" type="primary" placeholder="请输入任务名称" @keyup.enter="handleFilter" />
        </div>
        <el-button v-permission="['task:schedule:job:refresh:job']" type="primary" class="filter-item" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button v-permission="['task:schedule:job:add']" type="primary" class="filter-item" icon="Edit" @click="handleCreate">新增</el-button>
        <el-button v-permission="['task:schedule:job:delete']" :loading="batchDeleteLoading" type="danger" class="filter-item" icon="Delete" @click="handleBatchDelete">删除</el-button>
        <el-button v-permission="['task:schedule:job:refresh:job']" :loading="refreshTaskLoading" type="primary" class="filter-item" icon="Refresh" @click="handleRefreshTask">刷新任务</el-button>
      </div>
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%"
        header-cell-class-name="header-cell"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" />
        <el-table-column min-width="150" label="任务名称">
          <template #default="scope">
            <span>{{ scope.row.jobName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="任务分组">
          <template #default="scope">
            <span>{{ scope.row.jobGroup }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="100" label="执行类">
          <template #default="scope">
            <span>{{ scope.row.executeClass }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="方法名">
          <template #default="scope">
            <span>{{ scope.row.methodName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="100" label="执行表达式">
          <template #default="scope">
            <span>{{ scope.row.cronExpression }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="任务状态">
          <template #default="scope">
            <span>{{ dictLabel(scope.row.jobStatus, 'sf') }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="是否同步">
          <template #default="scope">
            <span>{{ dictLabel(scope.row.isConcurrent, 'sf') }}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="300" label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.jobStatus==='0'" v-permission="['task:schedule:job:change:job:status']" type="primary" plain size="small" icon="VideoPlay" @click="handleChangeJobStatus(scope.row, 'start', '启动')">开始</el-button>
            <el-button v-if="scope.row.jobStatus==='1'" v-permission="['task:schedule:job:change:job:status']" type="primary" plain size="small" icon="VideoPause" @click="handleChangeJobStatus(scope.row, 'stop', '停止')">停止</el-button>
            <el-button v-permission="['task:schedule:job:refresh:job']" size="small" type="primary" plain icon="Refresh" @click="handleRefresh(scope.row)">刷新</el-button>
            <el-button v-permission="['task:schedule:job:change:job:status']" size="small" type="primary" plain icon="ArrowRight" @click="runAJobNow(scope.row)">执行一次</el-button>
            <el-button v-permission="['task:schedule:job:detail']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['task:schedule:job:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 90%; margin-left:50px;">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="任务名称" prop="jobName">
                <el-input v-model="temp.jobName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="任务分组" prop="jobGroup">
                <el-input v-model="temp.jobGroup" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="加载方式" prop="loadWay">
                <el-radio
                  v-for="item in dictList('load_way')"
                  :key="'type' + item.label"
                  v-model="temp.loadWay"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行类" prop="executeClass">
                <el-input v-model="temp.executeClass" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="方法名" prop="methodName">
                <el-input v-model="temp.methodName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方法参数" prop="methodParams">
                <el-input v-model="temp.methodParams" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="cron表达式" prop="cronExpression">
                <el-input v-model="temp.cronExpression" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否同步" prop="isConcurrent">
                <el-radio
                  v-for="item in dictList('sf')"
                  :key="'type' + item.label"
                  v-model="temp.isConcurrent"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="任务描述">
            <el-input
              v-model="temp.templateContent"
              :autosize="{ minRows: 2, maxRows: 4}"
              type="textarea"
              placeholder="请输入任务描述"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
            确定
          </el-button>
        </template>
      </el-dialog>

    </div>
  </el-card>
</template>

<script>
import { fetchScheduleList, createSchedule, deleteSchedule, updateSchedule, refreshTask, refreshSchedule, changeJobStatus, runAJobNow } from '@/api/tool/task/schedule'
import Pagination from '@/components/Pagination/index.vue'

export default {
  name: 'SysScheduleList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      refreshTaskLoading: false,
      batchDeleteLoading: false,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined
      },
      showReviewer: false,
      temp: {
        id: undefined,
        jobName: '',
        cronExpression: '',
        executeClass: '',
        methodName: '',
        methodParams: '',
        misfirePolicy: '',
        loadWay: '1',
        isConcurrent: '1',
        description: '',
        jobStatus: '',
        jobGroup: ''
      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      dialogPvVisible: false,
      pvData: [],
      rules: {
        jobName: [{ required: true, message: '请输入任务名称！', trigger: 'blur' }],
        jobGroup: [{ required: true, message: '请输入任务分组！', trigger: 'blur' }],
        executeClass: [{ required: true, message: '请输入执行类！', trigger: 'blur' }],
        loadWay: [{ required: true, message: '请选择加载方式！', trigger: 'change' }],
        isConcurrent: [{ required: true, message: '请选择是否同步！', trigger: 'change' }],
        methodName: [{ required: true, message: '请输入方法名！', trigger: 'blur' }],
        cronExpression: [{ required: true, message: '请输入cron表达式！', trigger: 'blur' }]
      },
      downloadLoading: false,
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchScheduleList(this.listQuery).then(response => {
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
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        jobName: '',
        cronExpression: '',
        executeClass: '',
        methodName: '',
        methodParams: '',
        misfirePolicy: '',
        loadWay: '1',
        isConcurrent: '1',
        description: '',
        jobStatus: '',
        jobGroup: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.title = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createSchedule(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code !== 0) {
              this.$message({
                message: data.msg,
                type: 'error'
              })
            } else {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
            }
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateSchedule(tempData).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code !== 0) {
              this.$message({
                message: data.msg,
                type: 'error'
              })
            } else {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
            }
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDelete(row) {
      this.$confirm('确认要删除任务么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSchedule(row.id).then(response => {
          const data = response.data
          if (data.code === 0) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(data.errmsg)
          }
        })
      })
    },
    handleRefresh(row) {
      this.$confirm('确认要更新任务么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        refreshSchedule(row.id).then(response => {
          const data = response.data
          if (data.code === 0) {
            this.$message.success('更新成功')
            this.getList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    runAJobNow(row) {
      this.$confirm('确认要执行一次任务么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        runAJobNow(row).then(response => {
          const data = response.data
          if (data.code === 0) {
            this.$message.success('执行成功')
            this.getList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    handleChangeJobStatus(row, cmd, cmdLabel) {
      this.$confirm('你确定要' + cmdLabel + '该计划任务么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        changeJobStatus(row.id, cmd).then(response => {
          const data = response.data
          if (data.code === 0) {
            this.$message.success('更新成功')
            this.getList()
          } else {
            this.$message.error(data.errmsg)
          }
        })
      })
    },
    handleRefreshTask() {
      refreshTask().then(() => {
        this.$message.success('提交成功')
        this.$refs.multipleTable.clearSelection()
        this.refreshTaskLoading = false
      }).catch(() => {
        this.refreshTaskLoading = false
      })
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
        this.$confirm('你确定要删除该计划任务么?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSchedule(idsStr).then(() => {
            this.$message.success('提交成功')
            this.getList()
            this.batchDeleteLoading = false
          }).catch(() => {
            this.batchDeleteLoading = false
          })
        })
        this.getList()
      } else {
        this.$message({
          message: '至少选择一条任务',
          type: 'warning'
        })
      }
    }
  }
}
</script>

