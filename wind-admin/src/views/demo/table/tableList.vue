<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <el-input v-model="listQuery.title" placeholder="标题" class="filter-item" @keyup.enter="handleFilter" />
        <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
      </div>
      <btn-group/>

      <el-button v-permission="['test:table:table:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="Download" @click="handleImport">
        导入
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="Download" @click="handleExport">
        导出
      </el-button> -->
    </div>

    <el-table
      ref="dragTable"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      :header-cell-style="{background:'#EEF3FB',color:'#0243A3'}"
    >
      <el-table-column label="拖拽" width="80">
        <template #default="{}">
          <svg-icon class="drag-handler" icon-class="drag" />
        </template>
      </el-table-column>
      <el-table-column label="标题" min-width="150px">
        <template #default="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="作者" min-width="110px">
        <template #default="scope">
          <span>{{ scope.row.user.realname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="150px">
        <template #default="scope">
          <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="重要程度" min-width="150px">
        <template #default="scope">
          <span>☆☆☆☆☆☆☆</span>
        </template>
      </el-table-column>
      <el-table-column label="阅读数" min-width="95">
        <template #default="{row}">
          <span v-if="row.readings" class="link-type">{{ row.readings }}</span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="100">
        <template #default="{row}">
          <el-tag :type="statusFilter(row.status)">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="230">
        <template #default="{row}">
          <!-- <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button> -->
          <edit-btn v-permission="['test:table:table:detail']" @click="handleUpdate(row)"/>
          <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdateView(row)">
            编辑(新页签)
          </el-button>
          <el-button v-if="row.status!='published'" size="small" type="success" @click="handleModifyStatus(row,'published')">
            发布
          </el-button>
          <el-button v-if="row.status!='draft'" size="small" @click="handleModifyStatus(row,'draft')">
            草稿
          </el-button>
          <!-- <el-button v-permission="['test:table:table:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(row)">
            删除
          </el-button> -->
          <delete-btn v-permission="['test:table:table:delete']" @click="handleDelete(row)"/>
        </template>
      </el-table-column>
    </el-table>
    <import ref="import" template-url="/test/table/table/template" import-url="/test/table/table/import" />
    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <table-form ref="form" @refresh-list="getList" />
    <el-dialog
    class="deletedialog"
      v-model="dialogVisible"
      :show-close="false"
      :before-close="handleClose">
      <div>您确定删除该条数据吗？</div>
      <div class="btn">
        <span @click="dialogVisible = false">取消</span>
        <span @click="hdelete">确定</span>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { deleteTable, fetchTableList, exportTable } from '@/api/demo/table/table'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import tableForm from './tableForm'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import Sortable from 'sortablejs'
import Import from '@/components/Import/import'
import SvgIcon from '@/components/SvgIcon/index.vue'
import BtnGroup from '@/components/btn/BtnGroup.vue'
import EditBtn from '../../../components/btn/components/EditBtn.vue'
import DeleteBtn from '../../../components/btn/components/DeleteBtn.vue'

export default {
  name: 'ComplexTable',
  components: { SvgIcon, Import, tableForm, Pagination, BtnGroup, EditBtn, DeleteBtn },
  directives: { waves, permission },
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
        level: undefined,
        title: undefined,
        type: undefined
      },
      downloadLoading: false,
      dialogVisible: false,
      rowid: undefined
    }
  },
  created() {
    this.getList()
  },
  methods: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    getList() {
      this.listLoading = true
      fetchTableList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.setSort()
        // Just to simulate the time of the request
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
    handleImport() {
      this.$refs.import.show()
    },
    handleExport() {
      this.downloadLoading = true
      exportTable(this.listQuery).then(response => {
        this.downloadLoading = true
        if (response.data.code === 0) {
          import('@/vendor/Export2Excel').then(excel => {
            excel.export_byte_to_excel(response.data.data.bytes, response.data.data.title)
            this.downloadLoading = false
          })
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleUpdateView(row) {
      this.$router.push({
        path: '/demo/tableView/' + row.id + '/' + row.title
      })
    },
    handleDelete(row) {
      this.dialogVisible = true
      this.rowid = row.id
    },
    hdelete() {
      deleteTable(this.rowid).then(response => {
        if (response.data.code === 0) {
          this.getList()
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
        this.dialogVisible = false
      })
    },
    handleCreate() {
      this.$refs.form.handleCreate()
    },
    setSort() {
      const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper tbody')[0]
      this.sortable = Sortable.create(el, {
        handle: '.drag-handler',
        setData: function(dataTransfer) {
          // to avoid Firefox bug
          // Detail see : https://github.com/RubaXa/Sortable/issues/1012
          dataTransfer.setData('Text', '')
        },
        onEnd: evt => {
          const targetRow = this.list.splice(evt.oldIndex, 1)[0]
          this.list.splice(evt.newIndex, 0, targetRow)
          // TODO：这里完成自己的业务处理
          const temp = this.list
          this.list = []
          this.$nextTick(() => {
            this.list = temp
          })
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
:deep(.el-dialog.deletedialog){
  width: 300px;
  .el-dialog__body{
    display: flex;
    align-items: center;
  flex-direction: column;
  justify-content: center;
  .btn{
    display: flex;
    justify-content: space-around;
    width: 100%;
    margin-top: 50px;
    >:last-child{
      // background-color: #000;
      color: #0243A3;
    }
  }
  }
}
</style>
