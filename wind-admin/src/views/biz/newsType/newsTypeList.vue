<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>新闻类型名称:</span>
        <el-input v-model="listQuery.newsTypeName" placeholder="请输入新闻类型名称" />
        <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
        <el-button v-permission="['biz:newsType:newstype:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>
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
      :header-cell-style="{background:'#EEF3FB',color:'#0243A3'}"
    >
      <el-table-column label="新闻类型名称" min-width="150px">
        <template #default="{row}">
          <span>{{ row.newsTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="230">
        <template #default="{row}">
          <!-- <el-button v-permission="['biz:newsType:newstype:update']" type="primary" text size="small" icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:newsType:newstype:delete']" type="danger" text size="small" icon="Delete" @click="handleDelete(row)">
            删除
          </el-button> -->
          <edit-btn v-permission="['biz:newsType:newstype:update']" @click="handleUpdate(row)"/>
          <delete-btn v-permission="['biz:newsType:newstype:delete']" @click="handleDelete(row)"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <news-type-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { deleteNewsType, fetchNewsTypeList } from '@/api/biz/newsType/newsType'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import newsTypeForm from './newsTypeForm'
import Pagination from '@/components/Pagination'
import BtnGroup from '@/components/btn/BtnGroup.vue'

export default {
  name: 'NewsTypeList',
  components: { newsTypeForm, Pagination, BtnGroup },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        newsTypeName: undefined,
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
      fetchNewsTypeList(this.listQuery).then(response => {
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
        deleteNewsType(row.id).then(response => {
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
