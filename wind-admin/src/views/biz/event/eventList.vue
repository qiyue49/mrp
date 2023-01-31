<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>标题:</span>
        <el-input v-model="listQuery.title" placeholder="请输入标题" />
      </div>
      <div class="filter-item">
        <span>用户:</span>
        <el-input v-model="listQuery.userId" placeholder="请输入用户" />
      </div>
      <div class="filter-item">
        <span>类型:</span>
        <el-select v-model="listQuery.type" clearable placeholder="请选择类型" style="width: 200px">
          <el-option
            v-for="item in dictList('event_type')"
            :key="'type' + item.label"
            :label="item.label"
            :value="item.value" />
        </el-select>
      </div>
      <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-permission="['biz:event:event:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
    </div>

    <el-table
      ref="table"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="标题" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日期" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.date | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{  dictLabel(row.type, 'event_type') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户" min-width="150px">
        <template slot-scope="{row}">
          <span v-if="row.user">{{ row.user.realname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属机构" min-width="150px">
        <template slot-scope="{row}">
          <span v-if="row.organization">{{ row.organization.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-permission="['biz:event:event:update']" type="primary" text size="small" icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:event:event:delete']" type="danger" text size="small" icon="Delete" @click="handleDelete(row,'deleted')">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

    <event-form ref="form" @refreshList="getList" />

  </div>
</template>

<script>
import { deleteEvent, fetchEventList } from '@/api/biz/event/event'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import eventForm from './eventForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'EventList',
  components: { eventForm, Pagination },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        title: undefined,
        userId: undefined,
        type: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchEventList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteEvent(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    handleCreate() {
      this.$refs.form.handleCreate()
    }
  }
}
</script>
