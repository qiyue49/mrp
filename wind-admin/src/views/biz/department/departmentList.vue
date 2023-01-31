<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-permission="['biz:department:department:add']" class="filter-item" style="margin-left: 10px;" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
    </div>

    <el-table :data="list" style="width: 100%;" row-key="id" border lazy :load="load">
      <el-table-column label="名称" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.user.realname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-permission="['biz:department:department:update']" type="primary" text size="small" icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:department:department:delete']" type="danger" text size="small" icon="Delete" @click="handleDelete(row,'deleted')">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <department-form ref="form" @refreshList="getList" />

  </div>
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
