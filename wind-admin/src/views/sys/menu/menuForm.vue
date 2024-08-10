<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 90%; margin-left:50px;">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="权限类型" prop="type">
            <el-radio
              v-for="item in dictList('menu_type')"
              :key="'type' + item.label"
              v-model="temp.type"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否显示" prop="enabled">
            <el-radio
              v-for="item in dictList('sf')"
              :key="'enabled' + item.label"
              v-model="temp.enabled"
              :label="item.value"
              :disabled="temp.type === '3'"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外链">
            <el-radio
              v-for="item in dictList('sf')"
              :key="'enabled' + item.label"
              v-model="externalLink"
              :label="item.value"
              :disabled="!(temp.type === '2')"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示卡片" prop="card">
            <el-radio
              v-for="item in dictList('sf')"
              :key="'enabled' + item.label"
              v-model="temp.card"
              :label="item.value"
              :disabled="!(temp.type === '2')"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上级菜单" prop="parentIds">
            <el-cascader
              v-model="parentIds"
              :options="list"
              :props="treeProps"
              :show-all-levels="false"
              label="name"
              change-on-select
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单名称" prop="name">
            <el-input v-model="temp.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权限值" prop="permission">
            <el-input v-model="temp.permission" :disabled="!(temp.type === '3')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="externalLink==='1'?'外链地址':'前端地址'" prop="path">
            <el-input v-model="temp.path" :disabled="!(temp.type === '2')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="前端组件" prop="component">
            <el-input v-model="temp.component" :disabled="!(temp.type === '2') || (externalLink === '1')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="图标" prop="icon">
            <icon-selector v-model="temp.icon" :disabled="temp.type === '3'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input v-model="temp.sort" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请填写备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button v-permission="['sys:menu:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>
<script>
import { createMenu, fetchMenu, updateMenu } from '@/api/sys/menu'
import IconSelector from '@/components/IconSelector/iconSeletor'
import { isExternal } from '@/utils/validate'

export default {
  name: 'MenuForm',
  components: { IconSelector },
  emits: ['refreshList'],
  data() {
    return {
      list: [],
      parentIds: undefined,
      temp: {},
      rules1: {
        type: [{ required: true, message: '菜单名称必填' }],
        name: [{ required: true, message: '菜单名称必填' }],
        sort: [{ required: true, message: '排序必填' }]
      },
      rules2: {
        type: [{ required: true, message: '菜单名称必填' }],
        name: [{ required: true, message: '菜单名称必填' }],
        path: [{ required: true, message: '前端路径必填' }],
        component: [{ required: true, message: '前端组件必填' }],
        sort: [{ required: true, message: '排序必填' }]
      },
      rules3: {
        type: [{ required: true, message: '菜单名称必填' }],
        name: [{ required: true, message: '菜单名称必填' }],
        permission: [{ required: true, message: '权限必填' }],
        sort: [{ required: true, message: '排序必填' }]
      },
      rules4: {
        type: [{ required: true, message: '菜单名称必填' }],
        name: [{ required: true, message: '菜单名称必填' }],
        path: [{ required: true, message: '前端路径必填' }],
        sort: [{ required: true, message: '排序必填' }]
      },
      ruleList: [],
      treeProps: {
        value: 'id',
        label: 'name',
        checkStrictly: true
      },
      externalLink: '0',
      loading: false,
      dialogFormVisible: false,
      displayDisable: false,
      iconFormVisible: false,
      iconStatus: '',
      title: undefined
    }
  },
  computed: {
    rules() {
      if (this.externalLink === '1') {
        return this.rules4
      }
      return this.ruleList[parseInt(this.temp.type) - 1]
    }
  },
  created() {
    this.ruleList = [this.rules1, this.rules2, this.rules3]
  },
  methods: {
    getList(data) {
      this.$emit('refreshList', data)
    },
    setList(list) {
      this.list = list
    },
    resetTemp() {
      this.loading = false
      this.parentIds = undefined
      this.temp = {
        id: undefined,
        name: undefined,
        type: '2',
        path: undefined,
        url: undefined,
        enabled: '1',
        card: '1',
        sort: '0',
        icon: undefined,
        component: undefined,
        remark: undefined
      }
      this.externalLink = '0'
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
      // 预处理提交的数据
      const parentIds = this.parentIds
      if (parentIds instanceof Array && parentIds.length > 0) {
        this.temp.parentId = parentIds[parentIds.length - 1]
      }
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createMenu(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.dialogFormVisible = false
              this.getList(data.data)
              this.$message.success(data.msg)
            } else {
              this.$message.error(data.msg)
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
      fetchMenu(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data // copy obj
          if (this.temp.parentIds !== undefined) {
            let parentIds = this.temp.parentIds.trim()
            if (parentIds.length > 0) {
              parentIds = parentIds.substring(0, parentIds.length - 1)
            }
            this.parentIds = parentIds.split('\/')
            this.externalLink = isExternal(this.temp.path) ? '1' : '0'
          }
        } else {
          this.dialogFormVisible = false
          this.$message.error(response.data.msg)
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          const parentIds = this.parentIds
          if (parentIds instanceof Array && parentIds.length > 0) {
            this.temp.parentId = parentIds[parentIds.length - 1]
          } else {
            this.temp.parentId = undefined
          }
          updateMenu(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.getList(this.temp)
              this.dialogFormVisible = false
              this.$message.success(data.msg)
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }

  }
}
</script>
