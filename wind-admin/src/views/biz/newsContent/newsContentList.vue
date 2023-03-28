<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>内容标题:</span>
        <el-input v-model="listQuery.newsContentTitle" placeholder="请输入内容标题" />
        <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
          查询
        </el-button>
        <el-button v-permission="['biz:newsContent:newscontent:add']" icon="Plus" class="filter-item" type="danger" @click="handleCreate">
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
      <el-table-column label="内容标题" min-width="150px">
        <template #default="{row}">
          <span>{{ row.newsContentTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column label="新闻类型" min-width="150px">
        <template #default="{row}">
          <span>{{ row.newsTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" min-width="150px">
        <template #default="{row}">
          <span>{{ parseTime( row.newsReleaseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="相关图片" min-width="150px">
        <template #default="{row}">
          <div v-for="(img,index) in row.img" :key="index">
            <el-image :src="img" :preview-src-list="row.img" style="width: 75px; height: 75px" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="230px">
        <template #default="{row}">
          <el-button v-permission="['biz:newsContent:newscontent:update']" type="primary" icon="EditPen" plain size="small" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:newsContent:newscontent:delete']" size="small" plain icon="Delete" type="error" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <news-content-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { deleteNewsContent, fetchNewsContentList } from '@/api/biz/newsContent/newsContent'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import newsContentForm from './newsContentForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'NewsContentList',
  components: { newsContentForm, Pagination },
  directives: { waves, permission },
  data() {
    return {
      imgArray: [],
      dialogContent: false,
      form: {
        name: ' 张三 '
      },
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        newsContentTitle: undefined,
        newsTypeId: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    addContent() {
      this.dialogContent = true
    },
    getList() {
      this.listLoading = true
      fetchNewsContentList(this.listQuery).then(response => {
        this.list = response.data.data
        this.list.forEach(item => {
          if (item.img !== undefined && item.img !== '') {
            item.img = item.img.split(',')
          }
        })
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
        deleteNewsContent(row.id).then(response => {
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
<style scoped>

</style>
