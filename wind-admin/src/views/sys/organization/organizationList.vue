<template>
  <div>
    <div class="filter-container">
      <div class="filter-item">
        <span>名称:</span>
        <el-input v-model="listQuery.name" placeholder="请输入名称" @keyup.enter="handleFilter" />
      </div>
      <el-button v-permission="['sys:organization:list']" v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-permission="['sys:organization:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" style="width: 100%;" row-key="id" border>
      <el-table-column prop="name" label="名称" width="180" />
      <el-table-column label="备注">
        <template #default="scope">
          <span style="color:sandybrown">{{ scope.row.remarks }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-permission="['sys:organization:update']" size="small" type="primary" text icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:organization:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <organization-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { fetchOrganizationList, deleteOrganization } from '@/api/sys/organization'
import permission from '@/directive/permission/permission'
import waves from '@/directive/waves'
import OrganizationForm from './organizationForm' // 水波纹指令

export default {
  name: 'OrganizationList',
  components: { OrganizationForm },
  directives: {
    waves, permission
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
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
      this.list = []
      fetchOrganizationList(this.listQuery).then(response => {
        const organizationList = response.data.data
        for (var i = 0; i < organizationList.length; i++) {
          this.list.push({
            id: organizationList[i].id,
            name: organizationList[i].name,
            children: organizationList[i].children
          })
        }
        this.$refs.form.setList(this.list)
        this.listLoading = false
      })
    },
    handleFilter() {
      this.getList()
    },
    handleCreate() {
      this.$refs.form.handleCreate()
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
        deleteOrganization(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    }
  }
}
</script>
