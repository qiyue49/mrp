<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>附件名称:</span>
          <el-input v-model="listQuery.fileName" placeholder="请输入附件名称" @keyup.enter="handleFilter" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
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
        <el-table-column min-width="200" label="文件名称">
          <template #default="scope">
            <span>{{ scope.row.fileName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="文件路径">
          <template #default="scope">
            <span><a :href="scope.row.filePath" target="_blank">{{ scope.row.filePath }}</a></span>
          </template>
        </el-table-column>
        <el-table-column min-width="90" label="文件大小">
          <template #default="scope">
            <span>{{ scope.row.fileSize }} K</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="扩展名">
          <template #default="scope">
            <span>{{ scope.row.fileExtension }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="160" label="上传时间">
          <template #default="scope">
            <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="120" label="上传的IP">
          <template #default="scope">
            <span>{{ scope.row.uploadIp }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="80">
          <template #default="scope">
            <el-button size="small" type="danger" plain icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    </div>
  </el-card>
</template>

<script>
import { fetchAttachmentList, deleteAttachment } from '@/api/oss/attachment'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'SysSendlogList',
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
      batchDeleteLoading: false,
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchAttachmentList(this.listQuery).then(response => {
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
    resetTemp() {
      this.temp = {
        phone: '',
        code: '',
        data: ''
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAttachment(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
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
          deleteAttachment(idsStr).then(response => {
            if (response.data.code === 0) {
              this.$message.success('提交成功')
              this.$refs.multipleTable.clearSelection()
              this.batchDeleteLoading = false
              this.getList()
            }
          }).catch(() => {
            this.batchDeleteLoading = false
          })
          this.getList()
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
