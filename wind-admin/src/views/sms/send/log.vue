<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>手机号码:</span>
          <el-input v-model="listQuery.phone" placeholder="请输入手机号码" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>模板编码:</span>
          <el-input v-model="listQuery.code" placeholder="请输入模板编码" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>发送状态:</span>
          <el-select v-model="listQuery.status" placeholder="请选择发送状态">
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
        <el-button :loading="sendMsgLoading" class="filter-item" type="primary" icon="Document" @click="handleRetrySendMsg">短信重发</el-button>
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleSendMsg">发送短信</el-button>
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
        <el-table-column min-width="120" label="联系电话">
          <template #default="scope">
            <span>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="200" label="模版名称">
          <template #default="scope">
            <span>{{ scope.row.templateName }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="模版编码">
          <template #default="scope">
            <span>{{ scope.row.sendCode }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="发送状态">
          <template #default="scope">
            <span>{{ statusFilter(scope.row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="响应消息ID">
          <template #default="scope">
            <span>{{ scope.row.smsid }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="重试次数">
          <template #default="scope">
            <span>{{ scope.row.tryNum }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="返回消息">
          <template #default="scope">
            <span>{{ scope.row.msg }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="160" label="响应时间">
          <template #default="scope">
            <span>{{ scope.row.responseDate }}</span>
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

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" title="发送短信">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
          <el-form-item label="电话号码" prop="phone">
            <el-input
              v-model="temp.phone"
              :autosize="{ minRows: 8, maxRows: 12}"
              type="textarea"
              placeholder="电话号码，多个以英文逗号“,”隔开"
            />
          </el-form-item>
          <el-form-item label="模版编码" prop="code">
            <el-input v-model="temp.code" placeholder="请设置模版编码" />
          </el-form-item>
          <el-form-item label="变量值" prop="data">
            <el-input ref="jsonEditor" v-model="temp.data" type="textarea"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="runSendMsg">发送短信</el-button>
        </template>
      </el-dialog>

    </div>
  </el-card>
</template>

<script>
import Pagination from '@/components/Pagination/index.vue'
import { fetchList, sendMsg, deleteSendlog, retrySend } from '@/api/sms/sendlog'

export default {
  name: 'SmsSendlogList',
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
      showReviewer: false,
      rules: {
        phone: [{ required: true, message: '手机号码必填', trigger: 'blur' }],
        code: [{ required: true, message: '编码必填', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      temp: {
        phone: '',
        code: '',
        data: JSON.parse('{}')
      },
      multipleSelection: [],
      sendMsgLoading: false,
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
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    statusFilter(status) {
      const statusMap = {
        '-1': '发送失败',
        0: '发送中',
        1: '发送成功'
      }
      return statusMap[status + '']
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.sendMsgLoading = false
      this.temp = {
        phone: '',
        code: '',
        data: JSON.parse('{}')
      }
    },
    handleSendMsg() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    runSendMsg() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.sendMsgLoading = true
          sendMsg(this.temp).then(response => {
            this.sendMsgLoading = false
            if (response.data.code === 0) {
              this.dialogFormVisible = false
              this.getList()
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    },
    handleDelete(row) {
      deleteSendlog(row.id).then(() => {
        this.$message.success('删除成功')
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleRetrySendMsg() {
      if (this.multipleSelection.length) {
        this.sendMsgLoading = true
        const list = this.multipleSelection
        const ids = []
        list.forEach(function(value, index, array) {
          ids.push(value.id)
        })
        const idsStr = ids.join(',')
        retrySend(idsStr).then(() => {
          this.$message.success('提交成功')
          this.$refs.multipleTable.clearSelection()
          this.sendMsgLoading = false
        }).catch(() => {
          this.sendMsgLoading = false
        })
      } else {
        this.$message({
          message: '至少选择一条重发',
          type: 'warning'
        })
      }
    }
  }
}
</script>
