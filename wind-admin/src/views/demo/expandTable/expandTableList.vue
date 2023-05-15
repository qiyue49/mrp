<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%;"
        header-cell-class-name="header-cell">
        <el-table-column type="expand">
          <template #default="props">
            <el-form label-position="left" inline class="table-expand" label-width="120px" style="width: 90%; margin-left:50px;">
              <el-form-item label="商品名称">
                <span>{{ props.row.name }}</span>
              </el-form-item>
              <el-form-item label="所属店铺">
                <span>{{ props.row.shop }}</span>
              </el-form-item>
              <el-form-item label="商品分类">
                <span>{{ props.row.category }}</span>
              </el-form-item>
              <el-form-item label="店铺地址">
                <span>{{ props.row.address }}</span>
              </el-form-item>
              <el-form-item label="商品描述">
                <span>{{ props.row.description }}</span>
              </el-form-item>
              <el-form-item label="标签">
                <span>{{ props.row.tag }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="商品名称" min-width="150px">
          <template #default="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属店铺" min-width="150px">
          <template #default="{row}">
            <span>{{ row.shop }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标签" min-width="150px">
          <template #default="{row}">
            <span>{{ row.tag }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="{row}">
            <el-button v-permission="['test:expandtable:expandtable:detail']" size="small" icon="EditPen" type="primary" plain @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['test:expandtable:expandtable:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <expand-table-form ref="form" @refresh-list="getList" />

    </div>
  </el-card>
</template>

<script>
import { deleteExpandTable, fetchExpandTableList } from '@/api/demo/expandTable/expandTable'
import expandTableForm from './expandTableForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'ExpandTableList',
  components: { expandTableForm, Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
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
      fetchExpandTableList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        // Just to simulate the time of the request
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
        deleteExpandTable(row.id).then(response => {
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
<style>
.table-expand {
  font-size: 0;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
