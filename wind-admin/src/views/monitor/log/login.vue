<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <span>登陆状态:</span>
          <el-select v-model="listQuery.status" placeholder="请选择登陆状态" style="width: 200px">
            <el-option label="全部状态" value="" />
            <el-option
              v-for="item in dictList('login_status')"
              :key="item.label + 'loginstatus'"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>

        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="Download" @click="handleDownload">导出</el-button>
        <el-button :loading="batchDeleteLoading" class="filter-item" type="danger" icon="Delete" @click="handleBatchDelete">删除</el-button>
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
        <el-table-column min-width="120" label="登陆用户">
          <template #default="scope">
            <span>{{ scope.row.loginName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="登陆IP">
          <template #default="scope">
            <span>{{ scope.row.loginIp }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="登录地点">
          <template #default="scope">
            <span>{{ scope.row.loginLocation }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="浏览器">
          <template #default="scope">
            <span>{{ scope.row.browser }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="操作系统">
          <template #default="scope">
            <span>{{ scope.row.os }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="登陆状态">
          <template #default="scope">
            <el-tag :type="statusTypeFilter(scope.row.status)">{{ dictLabel(scope.row.status, 'login_status') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column min-width="200" label="操作信息">
          <template #default="scope">
            <span class="tip" @click="detail(scope.row.msg)">{{ scope.row.msg }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="160" label="操作时间">
          <template #default="scope">
            <span>{{ parseTime(scope.row.loginTime, '{y}-{m}-{d} {h}:{i}') }}</span>
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
      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" title="详情" :close-on-click-modal="false">
        <div v-html="msg" ></div>
        <template #footer>
          <el-button @click="dialogFormVisible = false">关闭</el-button>
        </template>

      </el-dialog>
    </div>
  </el-card>
</template>

<script>
import { fetchLoginLogList, deleteLoginLog, batchDeleteLoginLog, exportLoginLog } from '@/api/monitor/log/login'
import Pagination from '@/components/Pagination/index.vue'
import { textToHtml } from '@/utils'

export default {
  name: 'ScheduleJobLogList',
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
        importance: undefined,
        title: undefined,
        type: undefined
      },
      msg: undefined,
      showReviewer: false,
      dialogFormVisible: false,
      multipleSelection: [],
      batchDeleteLoading: false,
      downloadLoading: false,
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
    detail(msg) {
      this.msg = textToHtml(msg)
      this.dialogFormVisible = true
    },
    statusTypeFilter(status) {
      const statusMap = {
        '-1': 'danger',
        0: 'info',
        1: 'success'
      }
      return statusMap[status]
    },
    getList() {
      this.listLoading = true
      fetchLoginLogList(this.listQuery).then(response => {
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
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLoginLog(row.id).then(() => {
          this.$message.success('删除成功')
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDownload() {
      this.downloadLoading = false
      exportLoginLog(this.listQuery).then(response => {
        this.downloadLoading = true
        if (response.data.code === 0) {
          import('@/vendor/Export2Excel').then(excel => {
            excel.export_byte_to_excel(response.data.data.bytes, response.data.data.title)
            this.downloadLoading = false
          })
        } else {
          this.$message.error(response.data.errmsg)
        }
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length) {
        this.$confirm('确定删除该数据吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.batchDeleteLoading = true
          const list = this.multipleSelection
          const ids = []
          list.forEach(function(value, index, array) {
            ids.push(value.id)
          })
          const idsStr = ids.join(',')
          batchDeleteLoginLog(idsStr).then(() => {
            this.$message.success('提交成功')
            this.$refs.multipleTable.clearSelection()
            this.batchDeleteLoading = false
            this.getList()
          }).catch(() => {
            this.batchDeleteLoading = false
          })
        })
      } else {
        this.$message({
          message: '至少选择一条日志',
          type: 'warning'
        })
      }
    }
  }
}
</script>
<style scoped>
.tip{
  width:100%;

  word-break:keep-all; /* 不换行 */

  white-space:nowrap; /* 不换行 */

  overflow:hidden; /* 内容超出宽度时隐藏超出部分的内容 */

  text-overflow:ellipsis; /* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/

}
</style>
