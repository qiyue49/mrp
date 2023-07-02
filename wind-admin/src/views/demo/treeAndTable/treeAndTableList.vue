<template>
  <el-card class="el-card">
    <el-row :gutter="20">
      <el-col :span="6">
        <div>
          <div class="filter-container left-tree-list">
            <el-table :data="treeList" row-key="id" lazy :load="load" highlight-current-row @row-click="getListByTreeId">
              <el-table-column style="color:red;" prop="name" label="名称" sortable />
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :span="18">
        <el-card class="el-card">
          <div class="app-container">
            <div class="filter-container">
              <div class="filter-item">
                <span>部门名称:</span>
                <el-input v-model="listQuery.name" placeholder="请输入部门名称" />
              </div>
              <div class="filter-item">
                <span>类型:</span>
                <el-input v-model="listQuery.type" placeholder="请输入类型" />
              </div>
              <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
                搜索
              </el-button>
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
              header-cell-class-name="header-cell"
              style="width: 100%;">
              <el-table-column label="部门名称" min-width="150px">
                <template #default="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="类型" min-width="150px">
                <template #default="{row}">
                  <span>{{ row.type }}</span>
                </template>
              </el-table-column>
              <el-table-column label="标签" min-width="150px">
                <template #default="{row}">
                  <span>{{ row.tag }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="230">
                <template #default="{row}">
                  <el-button v-permission="['test:treeandtable:treeandtable:detail']" size="small" icon="EditPen" type="primary" plain @click="handleUpdate(row)">
                    编辑
                  </el-button>
                  <el-button v-permission="['test:treeandtable:treeandtable:delete']" size="small" icon="Delete" plain type="danger" @click="handleDelete(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

            <tree-and-table-form ref="form" @refresh-list="getList" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import { deleteTreeAndTable, fetchTreeAndTableList } from '@/api/demo/treeAndTable/treeAndTable'
import { fetchTreeTableList } from '@/api/demo/treeTable/treeTable'
import treeAndTableForm from './treeAndTableForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'TreeAndTableList',
  components: { treeAndTableForm, Pagination },
  data() {
    return {
      tableKey: 0,
      treeList: [],
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        name: undefined,
        type: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      }
    }
  },
  created() {
    this.getTreeList()
    this.getList()
  },
  methods: {
    getTreeList() {
      this.listLoading = true
      fetchTreeTableList(this.listQuery).then(response => {
        if (response.data.code === 0) {
          this.treeList = response.data.data
          this.listLoading = false
          this.$refs.form.setList(this.treeList)
        } else {
          this.$message.error(response.data.msg)
        }
        this.listLoading = false
      })
    },
    load(tree, treeNode, resolve) {
      resolve(tree.children)
    },
    getListByTreeId(data) {
      this.listQuery.areaId = data.id
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchTreeAndTableList(this.listQuery).then(response => {
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
      this.$refs.form.setList(this.list)
      this.$refs.form.handleUpdate(row.id)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTreeAndTable(row.id).then(response => {
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
