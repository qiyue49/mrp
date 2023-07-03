<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <el-input v-model="listQuery.title" placeholder="标题" @keyup.enter="handleFilter" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
        <el-button v-permission="['test:table:table:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
        <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="Download" @click="handleImport">
          导入
        </el-button>
        <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="Download" @click="handleExport">
          导出
        </el-button>
      </div>

      <el-table
        ref="dragTable"
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%;"
        header-cell-class-name="header-cell"
      >
        <el-table-column label="拖拽" width="80">
          <template #default="{}">
            <svg-icon class="drag-handler" icon-class="ui-move" />
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
            <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdateView(row)">
              编辑(新页签)
            </el-button>
            <el-button v-if="row.status!='published'" size="small" type="success" @click="handleModifyStatus(row,'published')">
              发布
            </el-button>
            <el-button v-if="row.status!='draft'" size="small" @click="handleModifyStatus(row,'draft')">
              草稿
            </el-button>
            <el-button v-permission="['test:table:table:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <import ref="import" template-url="/test/table/table/template" import-url="/test/table/table/import" />
      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <table-form ref="form" @refresh-list="getList" />

    </div>
  </el-card>
</template>

<script>
import { deleteTable, fetchTableList, exportTable } from '@/api/demo/table/table'
import tableForm from './tableForm'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import Sortable from 'sortablejs'
import Import from '@/components/Import/import'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'ComplexTable',
  components: { SvgIcon, Import, tableForm, Pagination },
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
      downloadLoading: false
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
      deleteTable(row.id).then(response => {
        if (response.data.code === 0) {
          this.getList()
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
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
