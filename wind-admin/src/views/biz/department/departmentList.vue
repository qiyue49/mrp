<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <el-button v-permission="['biz:department:department:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>

      <el-table :data="list" style="width: 100%;" row-key="id" lazy :load="load" header-cell-class-name="header-cell">
        <el-table-column label="名称" min-width="150px">
          <template #default="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编码" min-width="150px">
          <template #default="{row}">
            <span>{{ row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用户" min-width="150px">
          <template #default="{row}">
            <span>{{ row.user.realname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="{row}">
            <el-button v-permission="['biz:department:department:update']" type="primary" icon="EditPen" plain size="small" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['biz:department:department:delete']" plain type="danger" icon="Delete" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <department-form ref="form" @refresh-list="getList" />

    </div>
  </el-card>
</template>

<script>
import { deleteDepartment, fetchDepartmentList } from '@/api/biz/department/department'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import departmentForm from './departmentForm'
export default {
  name: 'Department',
  components: { departmentForm },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: [],
      listLoading: true,
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
      this.list = []
      fetchDepartmentList(this.listQuery).then(response => {
        if (response.data.code === 0) {
          this.list = response.data.data
        } else {
          this.$message.error(response.data.msg)
        }
        this.listLoading = false
      })
    },
    load(tree, treeNode, resolve) {
      resolve(tree.children)
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
        deleteDepartment(row.id).then(response => {
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
