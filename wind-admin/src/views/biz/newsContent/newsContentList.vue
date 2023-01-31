<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>内容标题:</span>
        <el-input v-model="listQuery.newsContentTitle" placeholder="请输入内容标题" />
      </div>
      <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-permission="['biz:newsContent:newscontent:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
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
      <el-table-column label="内容标题" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.newsContentTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column label="新闻类型" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.newsTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.newsReleaseTime | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="相关图片" min-width="150px">
        <template slot-scope="{row}">
          <div v-for="(img,index) in row.img" :key="index">
            <el-image :src="img" :preview-src-list="row.img" style="width: 75px; height: 75px" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="230px" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-permission="['biz:newsContent:newscontent:update']" type="primary" text size="small" icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:newsContent:newscontent:delete']" type="danger" text size="small" icon="Delete" @click="handleDelete(row,'deleted')">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

    <news-content-form ref="form" @refreshList="getList" />

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
