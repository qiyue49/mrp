<template>
  <el-row>
    <el-col :span="6">
      <div class="app-container calendar-list-container">
        <div id="treeBox" class="filter-container">
          <el-table :data="treeList" style="width: 100%;" row-key="id" border lazy :load="load" @row-click="getListByTreeId">
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
            <el-input v-model="listQuery.name" placeholder="请输入姓名" style="width: 200px" />
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
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
            {{ $t('table.search') }}
          </el-button>
          <el-button v-permission="['biz:employee:employee:add']" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
            {{ $t('table.add') }}
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
          <el-table-column label="性别" min-width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.sex | dictLabel('sex') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="生日" min-width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.birthday | parseTime('{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="工号" min-width="150px">
            <template slot-scope="{row}">
              <span>{{ row.cardNumber }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
            <template slot-scope="{row}">
              <el-button v-permission="['biz:employee:employee:update']" type="text" size="mini" @click="handleUpdate(row)">
                {{ $t('table.edit') }}
              </el-button>
              <el-button v-permission="['biz:employee:employee:delete']" type="text" size="mini" @click="handleDelete(row,'deleted')">
                {{ $t('table.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

        <employee-form ref="form" @refreshList="getList" />
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
<style lang="scss">
#treeBox {
  margin-top: 1px;
  .el-table thead {
    display: none;
  }
}
</style>
