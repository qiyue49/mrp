<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>Email:</span>
          <el-input v-model="listQuery.email" placeholder="请输入Email" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>主题:</span>
          <el-input v-model="listQuery.subject" placeholder="请输入主题" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>发送状态:</span>
          <el-select v-model="listQuery.status" placeholder="请选择发送状态">
            <el-option label="全部状态" value="" />
            <el-option
              v-for="item in statusOptions"
              :key="'filter_status'+ item.label "
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button :loading="sendEmailLoading" class="filter-item" type="primary" icon="Document" @click="handleRetrySendEmail">邮件重发</el-button>
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleSendEmail">发送邮件</el-button>
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
        <el-table-column min-width="150" label="Email">
          <template #default="scope">
            <span>{{ scope.row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="200" label="发送主题">
          <template #default="scope">
            <span>{{ scope.row.subject }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="发送状态">
          <template #default="scope">
            <span>{{ statusFilter(scope.row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="100" label="重试次数">
          <template #default="scope">
            <span>{{ scope.row.tryNum }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="100" label="返回消息">
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

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" title="发送邮件" width="70%">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px">
          <el-form-item label="Email" prop="email">
            <el-input
              v-model="temp.email"
              placeholder="Email，多个以英文逗号“,”隔开"
            />
          </el-form-item>
          <el-form-item label="模版编码" prop="code">
            <el-select v-model="temp.code" class="filter-item" placeholder="Please select" style="width: 100%" @change="handleSelect">
              <el-option v-for="item in templateList" :key="item.id" :label="item.name + '(' + item.code + ')'" :value="item.code" />
            </el-select>
          </el-form-item>
          <el-form-item label="变量值" prop="data">
            模板变量的json字符串
            <el-input ref="jsonEditor" v-model="temp.data" type="textarea" />
            模板内容:{{ template.templateContent }}
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" :loading="sendEmailLoading" @click="runSendEmail">发送邮件</el-button>
        </template>
      </el-dialog>

    </div>
  </el-card>
</template>

<script>
import Pagination from '@/components/Pagination/index.vue'
import { fetchSendLogList, sendEmail, deleteSendlog, retrySend } from '@/api/email/sendlog'
import { fetchTemplateList } from '@/api/email/template'
import { unescape } from '@/utils/index'
const jsonData = '{}'

export default {
  name: 'EmailSendlogList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      templateList: undefined,
      template: {},
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        subject: undefined,
        email: undefined,
        status: undefined
      },
      showReviewer: false,
      rules: {
        email: [{ required: true, message: 'Email地址必填', trigger: 'blur' }],
        code: [{ required: true, message: '编码必填', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      temp: {
        phone: '',
        code: '',
        data: JSON.parse(jsonData)
      },
      multipleSelection: [],
      sendEmailLoading: false,
      statusOptions: [
        { label: '发送失败', value: '-1' },
        { label: '发送中', value: '0' },
        { label: '发送成功', value: '1' }
      ]
    }
  },
  created() {
    this.getTemplateList()
    this.getList()
  },
  methods: {
    getTemplateList() {
      this.listLoading = true
      fetchTemplateList(this.listQuery).then(response => {
        this.templateList = response.data.data
      })
    },
    getList() {
      this.listLoading = true
      fetchSendLogList(this.listQuery).then(response => {
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
    handleSelect(val) {
      this.templateList.forEach(item => {
        if (item.code === val) {
          this.template = item
          console.log('this.template', this.template)
          this.template.templateContent = unescape(this.template.templateContent)
        }
      })
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.sendEmailLoading = false
      this.temp = {
        phone: '',
        code: '',
        data: JSON.parse(jsonData)
      }
    },
    handleSendEmail() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    runSendEmail() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.sendEmailLoading = true
          sendEmail(this.temp).then(response => {
            this.sendEmailLoading = false
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
    handleRetrySendEmail() {
      if (this.multipleSelection.length) {
        this.sendEmailLoading = true
        const list = this.multipleSelection
        const ids = []
        list.forEach(function(value, index, array) {
          ids.push(value.id)
        })
        const idsStr = ids.join(',')
        retrySend(idsStr).then(() => {
          this.$message.success('提交成功')
          this.$refs.multipleTable.clearSelection()
          this.sendEmailLoading = false
        }).catch(() => {
          this.sendEmailLoading = false
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

