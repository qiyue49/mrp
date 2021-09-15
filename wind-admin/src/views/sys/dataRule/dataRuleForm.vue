<template>
  <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :close-on-click-modal="false" append-to-body>
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 80%; margin-left:50px;">
      <el-form-item label="数据权限名称" prop="scopeName">
        <el-input v-model="temp.scopeName" />
      </el-form-item>
      <el-form-item label="资源编号" prop="resourceCode">
        <el-input v-model="temp.resourceCode" />
      </el-form-item>
      <el-form-item label="数据权限类型" prop="scopeType">
        <el-select v-model="temp.scopeType" class="filter-item" placeholder="请选择数据权限类型" style="width: 100%">
          <el-option
            v-for="item in dictList('dataRuleType')"
            :key="item.label + 'scopeType'"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="show('3,4')" label="权限判定关联表" prop="tableName">
        <el-input v-model="temp.tableName" />
      </el-form-item>
      <el-form-item v-if="show('3,4')" label="用户数据表关联字段" prop="userColumn">
        <el-input v-model="temp.userColumn" />
      </el-form-item>
      <el-form-item v-if="show('3,4')" label="用户实体类关联字段" prop="userEntityField">
        <el-input v-model="temp.userEntityField" />
      </el-form-item>
      <el-form-item v-if="show('2,3,4')" label="权限判定字段" prop="scopeColumn">
        <el-input v-model="temp.scopeColumn" />
      </el-form-item>
      <el-form-item label="数据展示字段" prop="scopeField">
        <el-input v-model="temp.scopeField" />
      </el-form-item>
      <el-form-item label="数据权限类名" prop="scopeClass">
        <el-input v-model="temp.scopeClass" />
      </el-form-item>
      <el-form-item v-if="show('5')" label="自定义SQL" prop="scopeValue">
        <el-input v-model="temp.scopeValue" type="textarea" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">
        {{ $t('table.cancel') }}
      </el-button>
      <el-button type="primary" :loading="loading" @click="dialogStatus==='create'?createData():updateData()">
        {{ $t('table.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createDataRule, updateDataRule, getDataRule } from '@/api/sys/dataRule/dataRule'

export default {
  name: 'DataRuleForm',
  data() {
    return {
      rules: {
        resourceCode: [{ required: true, message: '资源编号为必填项', trigger: 'blur' }],
        userColumn: [{ required: true, message: '用户数据表关联字段为必填项', trigger: 'blur' }],
        tableName: [{ required: true, message: '权限判定关联表为必填项', trigger: 'blur' }],
        userEntityField: [{ required: true, message: '用户实体类关联字段为必填项', trigger: 'blur' }],
        scopeName: [{ required: true, message: '数据权限名称为必填项', trigger: 'blur' }],
        scopeField: [{ required: true, message: '数据展示字段为必填项', trigger: 'blur' }, { validator: this.validateScopeField, tigger: 'blur' }],
        scopeClass: [{ required: true, message: '数据权限类名为必填项', trigger: 'blur' }],
        scopeColumn: [{ required: true, message: '权限判定字段为必填项', trigger: 'blur' }],
        scopeValue: [{ required: true, message: '自定义SQL为必填项', trigger: 'blur' }],
        scopeType: [{ required: true, message: '数据权限类型为必填项', trigger: 'blur' }]
      },
      temp: {
        id: undefined,
        menuId: undefined,
        resourceCode: undefined,
        scopeName: undefined,
        scopeField: '*',
        scopeClass: undefined,
        scopeColumn: 'organization_id',
        tableName: 'sys_organization',
        userColumn: 'organization_id',
        userEntityField: 'organizationId',
        scopeType: undefined,
        scopeValue: undefined,
        remark: undefined,
        createUser: undefined,
        createDept: undefined,
        createTime: undefined,
        updateUser: undefined,
        updateTime: undefined,
        status: undefined,
        isDeleted: undefined
      },
      textMap: {
        update: '编辑',
        create: '新建'
      },
      loading: false,
      dialogFormVisible: false,
      dialogStatus: ''
    }
  },
  methods: {
    show(value) {
      const array = value.split(',')
      for (let i = 0; i < array.length; i++) {
        if (array[i] === this.temp.scopeType) {
          return true
        }
      }
      return false
    },
    getList() {
      this.$emit('refreshList')
    },
    validateScopeField(rule, value, callback) {
      if (value === '*' || value === 'id' || value === '' || value === undefined || value === null) {
        callback()
      } else {
        if (value.indexOf('id,') !== 0) {
          callback(new Error('展示字段必须包含id，并且在第一个'))
        } else {
          callback()
        }
      }
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        menuId: undefined,
        resourceCode: undefined,
        scopeName: undefined,
        scopeField: '*',
        scopeClass: undefined,
        scopeColumn: 'organization_id',
        tableName: 'sys_organization',
        userColumn: 'organization_id',
        userEntityField: 'organizationId',
        scopeType: undefined,
        scopeValue: undefined,
        remark: undefined,
        createUser: undefined,
        createDept: undefined,
        createTime: undefined,
        updateUser: undefined,
        updateTime: undefined,
        status: undefined,
        isDeleted: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          createDataRule(this.temp).then(response => {
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
      this.resetTemp()
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      getDataRule(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        } else {
          this.dialogFormVisible = false
          this.$message.error(response.data.msg)
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          updateDataRule(tempData).then(response => {
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
