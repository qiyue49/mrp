<template>
  <el-row>
    <el-col :span="6">
      <div>
        <div class="filter-container left-tree-list">
          <el-table :data="treeList" row-key="id" border lazy :load="load" @row-click="getListByTreeId">
            <el-table-column prop="name" label="名称" sortable />
          </el-table>
        </div>
      </div>
    </el-col>
    <el-col :span="18">
      <div class="app-container">
        <div class="filter-container">
          <div class="filter-item">
            <span>姓名:</span>
            <el-input v-model="listQuery.name" placeholder="请输入姓名" />
          </div>
          <div class="filter-item">
            <span>性别:</span>
            <el-radio
              v-for="item in dictList('sex')"
              :key="'type' + item.label"
              v-model="listQuery.sex"
              :label="item.value">
              {{ item.label }}
            </el-radio>
          </div>
          <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
            搜索
          </el-button>
          <el-button v-permission="['biz:employee:employee:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
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
          <el-table-column label="姓名" min-width="150px">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="性别" min-width="150px">
            <template slot-scope="{row}">
              <span>{{  dictLabel(row.sex, 'sex') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="生日" min-width="150px">
            <template slot-scope="{row}">
              <span>{{ row.birthday | parseTime('{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="工号" min-width="150px">
            <template slot-scope="{row}">
              <span>{{ row.cardNumber }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="230">
            <template slot-scope="{row}">
              <el-button v-permission="['biz:employee:employee:update']" type="primary" text size="mini" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button v-permission="['biz:employee:employee:delete']" type="primary" text size="mini" @click="handleDelete(row,'deleted')">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

        <employee-form ref="form" @refresh-list="getList" />
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { deleteEmployee, fetchEmployeeList } from '@/api/biz/employee/employee'
import { fetchDepartmentList } from '@/api/biz/department/department'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import employeeForm from './employeeForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'EmployeeList',
  components: { employeeForm, Pagination },
  directives: { waves, permission },
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
        sex: undefined,
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
      fetchDepartmentList(this.listQuery).then(response => {
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
      this.listQuery.orgId = data.id
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchEmployeeList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleUpdate(row) {
      this.$refs.form.setList(this.treeList)
      this.$refs.form.handleUpdate(row.id)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteEmployee(row.id).then(response => {
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
      this.$refs.form.setList(this.treeList)
      this.$refs.form.handleCreate()
    }
  }
}
</script>

