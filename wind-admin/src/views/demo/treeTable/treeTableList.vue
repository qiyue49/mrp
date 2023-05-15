<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <el-input v-model="listQuery.name" placeholder="请输入名称" @keyup.enter="handleFilter" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>

      <el-table v-loading="listLoading" highlight-current-row :data="list" style="width: 100%;" row-key="id" lazy :load="load" header-cell-class-name="header-cell">
        <el-table-column label="机构名称" min-width="150px">
          <template #default="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="地理编码" min-width="150px">
          <template #default="{row}">
            <span>{{ row.geocoding }}</span>
          </template>
        </el-table-column>
        <el-table-column label="邮政编码" min-width="150px">
          <template #default="{row}">
            <span>{{ row.postalCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="{row}">
            <el-button v-permission="['test:treetable:treetable:detail']" size="small" icon="EditPen" type="primary" plain @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['test:treetable:treetable:delete']" size="small" icon="Delete" plain type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <tree-table-form ref="form" @refresh-list="getList" />

    </div>
  </el-card>
</template>

<script>
import { deleteTreeTable, fetchTreeTableList } from '@/api/demo/treeTable/treeTable'
import treeTableForm from './treeTableForm'
export default {
  name: 'TreeTable',
  components: { treeTableForm },
  data() {
    return {
      tableKey: 0,
      list: [],
      listLoading: true,
      listQuery: {
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchTreeTableList(this.listQuery).then(response => {
        if (response.data.code === 0) {
          this.list = response.data.data
          this.listLoading = false
          this.$refs.form.setList(this.list)
        } else {
          this.$message.error(response.data.msg)
        }
        this.listLoading = false
      })
    },
    load(tree, treeNode, resolve) {
      resolve(tree.children)
    },
    handleFilter() {
      this.getList()
    },
    handleUpdate(row) {
      this.$refs.form.setList(this.list)
      this.$refs.form.handleUpdate(row.id)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTreeTable(row.id).then(response => {
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
      this.$refs.form.setList(this.list)
      this.$refs.form.handleCreate()
    }
  }
}
</script>
