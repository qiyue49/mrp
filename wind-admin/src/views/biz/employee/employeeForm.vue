<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form-item label="所属部门" prop="orgId">
            <el-cascader
              v-model="temp.orgId"
              :options="treeList"
              :props="treeProps"
              :show-all-levels="false"
              label="name"
              change-on-select
              clearable
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="temp.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-radio
              v-for="item in dictList('sex')"
              :key="'type' + item.label"
              v-model="temp.sex"
              :label="item.value">
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生日" prop="birthday">
            <el-date-picker v-model="temp.birthday" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工号" prop="cardNumber">
            <el-input v-model="temp.cardNumber" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="照片" prop="avatar">
            <upload-image v-model="temp.avatar" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createEmployee, updateEmployee, getEmployee } from '@/api/biz/employee/employee'
import uploadImage from '@/components/Upload/uploadImage'

export default {
  name: 'EmployeeForm',
  components: { uploadImage },
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        name: [{ required: true, message: '姓名为必填项', trigger: 'blur' }],
        sex: [{ required: true, message: '性别为必填项', trigger: 'blur' }],
        birthday: [{ required: true, message: '生日为必填项', trigger: 'blur' }],
        cardNumber: [{ required: true, message: '工号为必填项', trigger: 'blur' }],
        orgId: [{ required: true, message: '所属部门为必填项', trigger: 'blur' }]
      },
      temp: {},
      treeList: [],
      treeProps: {
        value: 'id',
        label: 'name',
        expandTrigger: 'hover'
      },
      title: undefined,
      dialogFormVisible: false
    }
  },
  methods: {
    setList(treeList) {
      this.treeList = treeList
    },
    getList() {
      this.$emit('refreshList')
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenant: undefined,
        name: undefined,
        sex: undefined,
        birthday: undefined,
        cardNumber: undefined,
        orgId: undefined,
        avatar: undefined
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
      if (this.temp.orgId !== undefined && this.temp.orgId !== '' && this.temp.orgId instanceof Array) {
        this.temp.orgId = this.temp.orgId[this.temp.orgId.length - 1]
      }

      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          createEmployee(this.temp).then(response => {
            if (response.data.code === 0) {
              this.getList()
              this.dialogFormVisible = false
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    },
    handleUpdate(id) {
      this.resetTemp()
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      getEmployee(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        } else {
          this.dialogFormVisible = false
          this.$message.error(response.data.msg)
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          if (this.temp.orgId !== undefined && this.temp.orgId !== '' && this.temp.orgId instanceof Array) {
            this.temp.orgId = this.temp.orgId[this.temp.orgId.length - 1]
          }

          const tempData = Object.assign({}, this.temp)
          updateEmployee(tempData).then(response => {
            if (response.data.code === 0) {
              this.dialogFormVisible = false
              this.getList()
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
