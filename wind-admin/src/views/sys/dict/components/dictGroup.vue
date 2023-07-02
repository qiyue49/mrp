<template>
  <div id="dictGroupComponent">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>分组名称或编码:</span>
          <el-input v-model="listQuery.keyword" placeholder="请输入分组名称或编码" @keyup.enter="handleFilter" />
        </div>
        <el-button v-permission="['sys:dict:group:list']" class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button v-permission="['sys:dict:group:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%"
        header-cell-class-name="header-cell"
        @row-click="refreshDicts"
      >
        <el-table-column prop="name" label="分组名称" width="160" />
        <el-table-column prop="code" label="分组编码" width="160" />
        <el-table-column prop="remarks" label="备注" width="160" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button v-permission="['sys:dict:group:update']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['sys:dict:group:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="margin-left:50px;">
          <el-form-item label="分组名称" prop="name">
            <el-input v-model="temp.name" />
          </el-form-item>
          <el-form-item label="分组编码" prop="code">
            <el-input v-model="temp.code" />
          </el-form-item>
          <el-form-item label="分组备注">
            <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button v-permission="['sys:dict:group:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
            确定
          </el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { fetchDictGroupList, createDictGroup, deleteDictGroup, updateDictGroup } from '@/api/sys/dictGroup'
import Pagination from '@/components/Pagination/index.vue'

export default {
  name: 'SysDictGroupComponent',
  components: { Pagination },
  emits: ['refreshDicts'],
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined
      },
      showReviewer: false,
      temp: {
        id: undefined,
        name: '',
        code: '',
        remarks: ''
      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        name: [{ required: true, message: '分组名称必填', trigger: 'change' }],
        code: [{ required: true, message: '分组编码必填', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    refreshDicts(row) {
      this.$emit('refreshDicts', row)
    },
    getList() {
      this.listLoading = true
      fetchDictGroupList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: '',
        code: '',
        remarks: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.title = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createDictGroup(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          updateDictGroup(tempData).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.dialogFormVisible = false
              this.$message.success('更新成功')
              this.getList()
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDictGroup(row.id).then((response) => {
          const data = response.data
          if (data.code === 0) {
            this.dialogFormVisible = false
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
