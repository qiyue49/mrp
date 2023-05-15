<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form-item label="姓名" prop="realname">
            <el-input v-model="temp.realname" />
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="temp.username" />
          </el-form-item>
          <el-form-item label="上级部门" prop="organizationId">
            <system-organization v-model="temp.organizationId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="头像" prop="portrait">
            <upload-image v-model="temp.portrait" :max-count="1" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="temp.email" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="temp.phone" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="temp.id===undefined" label="初始密码" prop="password">
            <el-input v-model="temp.password" type="password" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="可否登录" prop="status">
            <el-radio
              v-for="item in dictList('sf')"
              :key="'type' + item.label"
              v-model="temp.status"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button v-permission="['sys:user:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createUser, updateUser, fetchUser } from '@/api/sys/user'
import { fetchOrganizationList } from '@/api/sys/organization'
import SystemOrganization from '@/components/System/systemOrganization'
import UploadImage from '@/components/Upload/uploadImage'

export default {
  name: 'UserForm',
  components: { UploadImage, SystemOrganization },
  emits: ['refreshList'],
  data() {
    return {
      treeList: [],
      treeProps: {
        value: 'id',
        label: 'name'
      },
      organizationIds: undefined,
      temp: {},
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        organizationId: [{ required: true, message: '请选择部门', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getTreeList()
  },
  methods: {
    getList() {
      this.$emit('refreshList')
    },
    getTreeList() {
      this.listLoading = true
      fetchOrganizationList(this.listQuery).then(response => {
        this.treeList = response.data.data
        this.listLoading = false
      })
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        realname: undefined,
        portrait: undefined,
        phone: undefined,
        job: undefined,
        username: undefined,
        password: undefined,
        organizationId: undefined,
        organization: {
          id: undefined
        }
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
      if (this.temp.username.length < 4) {
        this.$message.error('用户名长度不能小于4')
        return
      }
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createUser(this.temp).then(response => {
            this.loading = false
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
      this.resetTemp() // copy obj
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      fetchUser(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          updateUser(tempData).then(response => {
            this.loading = false
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

<style scoped>

</style>
