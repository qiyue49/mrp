<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>菜单名称:</span>
          <el-input v-model="listQuery.keyword" placeholder="请输入菜单名称" @keyup.enter="handleFilter" />
        </div>
        <el-button v-permission="['sys:menu:list']" class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button v-permission="['sys:menu:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table v-loading="listLoading" :data="list" row-key="id" lazy :load="load" header-cell-class-name="header-cell">
        <el-table-column ming-width="150" label="名称">
          <template #default="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="前端地址">
          <template #default="scope">
            <span>{{ scope.row.path }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="前端组件">
          <template #default="scope">
            <span>{{ scope.row.component }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="80" label="排序">
          <template #default="scope">
            <el-input v-model="scope.row.sort" @change="handleChangeSort(scope.row.id, scope.row.sort)" />
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="图标">
          <template #default="scope">
            <el-icon v-if="scope.row.icon" class="icon"><component :is="scope.row.icon"/></el-icon>
            <span>{{ scope.row.icon }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="100" label="是否可用">
          <template #default="scope">
            <span>{{ dictLabel(scope.row.enabled, 'sf') }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="操作">
          <template #default="scope">
            <el-button v-permission="['sys:menu:update']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['sys:menu:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            <el-button v-if="scope.row.type === '2'" v-permission="['sys:menu:generate:button']" size="small" type="primary" plain icon="Coordinate" @click="handleGenerateButton(scope.row)">生成按钮</el-button>
          </template>
        </el-table-column>
      </el-table>

      <menu-form ref="form" @refresh-list="getList" />

      <menu-gen-button ref="genButtonFrom" @refresh-list="getList" />
    </div>
  </el-card>
</template>

<script>
import { fetchMenuList, deleteMenu, changeSort } from '@/api/sys/menu'
import MenuForm from './menuForm'
import MenuGenButton from './menuGenButton'
export default {
  name: 'MenuComponent',
  components: { MenuGenButton, MenuForm },
  data() {
    return {
      expandAll: true,
      tableKey: 0,
      list: [],
      listLoading: true,
      listQuery: {
        keyword: undefined
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
      fetchMenuList(this.listQuery).then(response => {
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
    handleFilter() {
      this.getList()
    },
    handleGenerateButton(row) {
      this.$refs.genButtonFrom.handleGenerateButton(row)
    },
    handleChangeSort(rowId, value) {
      changeSort(rowId, value).then(response => {
        if (response.data.code === 0) {
          this.$message.success(response.data.msg)
          // this.getList()
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    handleUpdate(row) {
      this.$refs.form.setList(this.list)
      this.$refs.form.handleUpdate(row.id)
    },
    handleCreate() {
      this.$refs.form.setList(this.list)
      this.$refs.form.handleCreate()
    },
    handleDelete(row) {
      this.$confirm('确认要删除菜单么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(response => {
          const data = response.data
          if (data.code === 0) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(data.errmsg)
          }
        })
      })
    }
  }
}
</script>
