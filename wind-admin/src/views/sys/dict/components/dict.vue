<template>
  <div id="dictComponent">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>字典标签或值:</span>
          <el-input v-model="listQuery.keyword" placeholder="请输入字典标签或值" @keyup.enter="handleFilter" />
        </div>
        <el-button v-permission="['sys:dict:list']" class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button v-permission="['sys:dict:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table :key="tableKey" v-loading="listLoading" :data="list" fit highlight-current-row header-cell-class-name="header-cell">
        <el-table-column prop="label" label="字典标签" width="160" />
        <el-table-column prop="value" label="字典值" width="160" />
        <el-table-column prop="remarks" label="备注" width="160" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button v-permission="['sys:dict:update']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['sys:dict:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="margin-left:50px;">
          <el-form-item label="字典标签" prop="label">
            <el-input v-model="temp.label" />
          </el-form-item>
          <el-form-item label="字典值" prop="value">
            <el-input v-model="temp.value" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="temp.sort" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button v-permission="['sys:dict:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
            确定
          </el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { fetchDictList, createDict, deleteDict, updateDict } from '@/api/sys/dict'
// import { store } from '@/stores'
import Pagination from '@/components/Pagination/index.vue'

export default {
  name: 'SysDictComponent',
  components: { Pagination },
  data() {
    return {
      dictGroup: {},
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
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
        label: '',
        value: '',
        sort: 1,
        remarks: ''
      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        label: [{ required: true, message: '字典标签不能为空', trigger: 'change' }],
        value: [{ required: true, message: '字典值不能为空', trigger: 'change' }],
        sort: [{ required: true, message: '排序不能为空', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchDictList(this.listQuery).then(response => {
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
        label: '',
        value: '',
        sort: 1,
        remarks: '',
        gid: this.dictGroup.id
      }
    },
    refreshGroupDict(row) {
      this.listQuery.gid = row.id
      this.listQuery.page = 1
      this.listQuery.keyword = ''
      this.dictGroup = row
      this.getList()
    },
    handleCreate() {
      if (this.dictGroup.id === undefined || this.dictGroup.id === '') {
        this.$message.error('请选择字典分组')
      } else {
        this.resetTemp()
        this.title = '新增'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs.dataForm.clearValidate()
        })
      }
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createDict(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              // store.dispatch('dict/GetDicts').then(() => {
              //   console.log('数据字典加载成功...')
              // })

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
      this.resetTemp()
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
          updateDict(tempData).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              // store.dispatch('dict/GetDicts').then(() => {
              //   console.log('数据字典加载成功...')
              // })
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
        deleteDict(row.id).then((response) => {
          const data = response.data
          if (data.code === 0) {
            // store.dispatch('dict/GetDicts').then(() => {
            //   console.log('数据字典加载成功...')
            // })

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
