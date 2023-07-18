<template>
  <div style="width: 100%">
    <el-input v-model="name" :style="{width: width}" readonly :clearable="clearable">
      <template #append>
        <el-button icon="Search" @click="show" />
      </template>
    </el-input>
    <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" title="选择用户" destroy-on-close append-to-body>
      <div>
        <div class="filter-container">
          <el-input v-model="listQuery.realname" class="filter-item" placeholder="请输入姓名" @keyup.enter="handleFilter" />
          <el-input v-model="listQuery.username" class="filter-item" placeholder="请输入用户名" @keyup.enter="handleFilter" />
          <el-input v-model="listQuery.phone" class="filter-item" placeholder="请输入手机号码" @keyup.enter="handleFilter" />
          <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">查询</el-button>
        </div>

        <el-table
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          fit
          highlight-current-row
          style="width: 100%"
          header-cell-class-name="header-cell"
        >
          <el-table-column min-width="50" label="选择">
            <template #default="scope">
              <el-radio v-model="selectCurentUserId" :label="scope.row.id" @change="selectUser(scope.row)"><i></i></el-radio>
            </template>
          </el-table-column>
          <el-table-column min-width="120" label="姓名">
            <template #default="scope">
              <span>{{ scope.row.realname }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" label="用户名">
            <template #default="scope">
              <span>{{ scope.row.username }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" label="联系电话">
            <template #default="scope">
              <span>{{ scope.row.phone }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" label="部门">
            <template #default="scope">
              <span>{{ scope.row.organization.name }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />
      </div>
      <template #footer>
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="select">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchUser } from '@/api/sys/user'
import { objectMerge } from '@/utils/index'
import Pagination from '@/components/Pagination'

export default {
  name: 'SystemUser',
  components: { Pagination },
  props: {
    modelValue: {
      type: String,
      default: undefined
    },
    width: {
      type: String,
      default: '100%'
    },
    clearable: {
      type: Boolean,
      default: true
    },
    query: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      name: undefined,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      },
      dialogFormVisible: false,
      selectCurentUserId: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.getUserInfo()
      }
    }
  },
  created() {
    this.name = undefined
  },
  methods: {
    show() {
      this.listQuery = objectMerge(this.listQuery, this.query)
      this.getList()
      this.dialogFormVisible = true
    },
    getList() {
      this.list = []
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    selectUser(row) {
      this.selectCurentUserId = row.id
      this.name = row.realname + '(' + row.username + ')'
    },
    select() {
      if (this.selectCurentUserId === undefined) {
        this.$message.warning('请选择一个用户')
        return
      }
      this.$emit('update:modelValue', this.selectCurentUserId)
      this.dialogFormVisible = false
    },
    getUserInfo() {
      this.name = undefined
      if (this.modelValue === undefined) {
        return
      }
      fetchUser(this.modelValue).then(response => {
        if (response.data.code === 0) {
          this.name = response.data.data.realname + '(' + response.data.data.username + ')'
        }
      })
    }
  }

}
</script>

