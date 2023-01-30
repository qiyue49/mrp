<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.title" placeholder="标题" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-permission="['test:table:table:add']" class="filter-item" style="margin-left: 10px;" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleImport">
        导入
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleExport">
        导出
      </el-button>
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
    >
      <el-table-column align="center" label="拖拽" width="80">
        <template #default="{}">
          <svg-icon class="drag-handler" icon-class="drag" />
        </template>
      </el-table-column>
      <el-table-column label="标题" min-width="150px">
        <template #default="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="作者" min-width="110px" align="center">
        <template #default="scope">
          <span>{{ scope.row.user.realname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="150px" align="center">
        <template #default="scope">
          <span>{{ scope.row.publishDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="重要程度" min-width="80px">
        <template #default="scope">
          <svg-icon v-for="n in +scope.row.level" :key="n" icon-class="star" class="meta-item__icon" />
        </template>
      </el-table-column>
      <el-table-column label="阅读数" align="center" min-width="95">
        <template #default="{row}">
          <span v-if="row.readings" class="link-type" @click="handleFetchPv(row.readings)">{{ row.readings }}</span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="100">
        <template #default="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template #default="{row}">
          <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['test:table:table:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdateView(row)">
            编辑(新页签)
          </el-button>
          <el-button v-if="row.status!='published'" size="mini" type="success" @click="handleModifyStatus(row,'published')">
            发布
          </el-button>
          <el-button v-if="row.status!='draft'" size="mini" @click="handleModifyStatus(row,'draft')">
            草稿
          </el-button>
          <el-button v-permission="['test:table:table:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <import ref="import" template-url="/test/table/table/template" import-url="/test/table/table/import" />
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

    <table-form ref="form" @refreshList="getList" />

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

export default {
  name: 'ComplexTable',
  components: { Import, tableForm, Pagination },
  directives: { waves, permission },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
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
      const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      this.sortable = Sortable.create(el, {
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
